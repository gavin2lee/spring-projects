/*     */ package org.springframework.beans.factory.annotation;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.PropertyValues;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.BeanInitializationException;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
/*     */ import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.core.Conventions;
/*     */ import org.springframework.core.PriorityOrdered;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ public class RequiredAnnotationBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter
/*     */   implements MergedBeanDefinitionPostProcessor, PriorityOrdered, BeanFactoryAware
/*     */ {
/*  84 */   public static final String SKIP_REQUIRED_CHECK_ATTRIBUTE = Conventions.getQualifiedAttributeName(RequiredAnnotationBeanPostProcessor.class, "skipRequiredCheck")
/*  84 */     ;
/*     */   private Class<? extends Annotation> requiredAnnotationType;
/*     */   private int order;
/*     */   private ConfigurableListableBeanFactory beanFactory;
/*  97 */   private final Set<String> validatedBeanNames = Collections.newSetFromMap(new ConcurrentHashMap(64))
/*  97 */     ;
/*     */ 
/*     */   public RequiredAnnotationBeanPostProcessor()
/*     */   {
/*  87 */     this.requiredAnnotationType = Required.class;
/*     */ 
/*  89 */     this.order = 2147483646;
/*     */   }
/*     */ 
/*     */   public void setRequiredAnnotationType(Class<? extends Annotation> requiredAnnotationType)
/*     */   {
/* 110 */     Assert.notNull(requiredAnnotationType, "'requiredAnnotationType' must not be null");
/* 111 */     this.requiredAnnotationType = requiredAnnotationType;
/*     */   }
/*     */ 
/*     */   protected Class<? extends Annotation> getRequiredAnnotationType()
/*     */   {
/* 118 */     return this.requiredAnnotationType;
/*     */   }
/*     */ 
/*     */   public void setBeanFactory(BeanFactory beanFactory)
/*     */   {
/* 123 */     if ((beanFactory instanceof ConfigurableListableBeanFactory))
/* 124 */       this.beanFactory = ((ConfigurableListableBeanFactory)beanFactory);
/*     */   }
/*     */ 
/*     */   public void setOrder(int order)
/*     */   {
/* 129 */     this.order = order;
/*     */   }
/*     */ 
/*     */   public int getOrder()
/*     */   {
/* 134 */     return this.order;
/*     */   }
/*     */ 
/*     */   public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName)
/*     */   {
/*     */   }
/*     */ 
/*     */   public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName)
/*     */     throws BeansException
/*     */   {
/* 147 */     if (!this.validatedBeanNames.contains(beanName)) {
/* 148 */       if (!shouldSkip(this.beanFactory, beanName)) {
/* 149 */         List invalidProperties = new ArrayList();
/* 150 */         for (PropertyDescriptor pd : pds) {
/* 151 */           if ((isRequiredProperty(pd)) && (!pvs.contains(pd.getName()))) {
/* 152 */             invalidProperties.add(pd.getName());
/*     */           }
/*     */         }
/* 155 */         if (!invalidProperties.isEmpty()) {
/* 156 */           throw new BeanInitializationException(buildExceptionMessage(invalidProperties, beanName));
/*     */         }
/*     */       }
/* 159 */       this.validatedBeanNames.add(beanName);
/*     */     }
/* 161 */     return pvs;
/*     */   }
/*     */ 
/*     */   protected boolean shouldSkip(ConfigurableListableBeanFactory beanFactory, String beanName)
/*     */   {
/* 176 */     if ((beanFactory == null) || (!beanFactory.containsBeanDefinition(beanName))) {
/* 177 */       return false;
/*     */     }
/* 179 */     BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
/* 180 */     if (beanDefinition.getFactoryBeanName() != null) {
/* 181 */       return true;
/*     */     }
/* 183 */     Object value = beanDefinition.getAttribute(SKIP_REQUIRED_CHECK_ATTRIBUTE);
/* 184 */     return (value != null) && ((Boolean.TRUE.equals(value)) || (Boolean.valueOf(value.toString()).booleanValue()));
/*     */   }
/*     */ 
/*     */   protected boolean isRequiredProperty(PropertyDescriptor propertyDescriptor)
/*     */   {
/* 197 */     Method setter = propertyDescriptor.getWriteMethod();
/* 198 */     return (setter != null) && (AnnotationUtils.getAnnotation(setter, getRequiredAnnotationType()) != null);
/*     */   }
/*     */ 
/*     */   private String buildExceptionMessage(List<String> invalidProperties, String beanName)
/*     */   {
/* 208 */     int size = invalidProperties.size();
/* 209 */     StringBuilder sb = new StringBuilder();
/* 210 */     sb.append(size == 1 ? "Property" : "Properties");
/* 211 */     for (int i = 0; i < size; i++) {
/* 212 */       String propertyName = (String)invalidProperties.get(i);
/* 213 */       if (i > 0) {
/* 214 */         if (i == size - 1) {
/* 215 */           sb.append(" and");
/*     */         }
/*     */         else {
/* 218 */           sb.append(",");
/*     */         }
/*     */       }
/* 221 */       sb.append(" '").append(propertyName).append("'");
/*     */     }
/* 223 */     sb.append(size == 1 ? " is" : " are");
/* 224 */     sb.append(" required for bean '").append(beanName).append("'");
/* 225 */     return sb.toString();
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.annotation.RequiredAnnotationBeanPostProcessor
 * JD-Core Version:    0.6.2
 */