/*     */ package org.springframework.beans.factory.annotation;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.AutowireCandidateQualifier;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ public class BeanFactoryAnnotationUtils
/*     */ {
/*     */   public static <T> T qualifiedBeanOfType(BeanFactory beanFactory, Class<T> beanType, String qualifier)
/*     */   {
/*  54 */     if ((beanFactory instanceof ConfigurableListableBeanFactory))
/*     */     {
/*  56 */       return qualifiedBeanOfType((ConfigurableListableBeanFactory)beanFactory, beanType, qualifier);
/*     */     }
/*  58 */     if (beanFactory.containsBean(qualifier))
/*     */     {
/*  60 */       return beanFactory.getBean(qualifier, beanType);
/*     */     }
/*     */ 
/*  63 */     throw new NoSuchBeanDefinitionException(qualifier, "No matching " + beanType.getSimpleName() + " bean found for bean name '" + qualifier + "'! (Note: Qualifier matching not supported because given " + "BeanFactory does not implement ConfigurableListableBeanFactory.)");
/*     */   }
/*     */ 
/*     */   private static <T> T qualifiedBeanOfType(ConfigurableListableBeanFactory bf, Class<T> beanType, String qualifier)
/*     */   {
/*  80 */     String[] candidateBeans = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(bf, beanType);
/*  81 */     String matchingBean = null;
/*  82 */     for (String beanName : candidateBeans) {
/*  83 */       if (isQualifierMatch(qualifier, beanName, bf)) {
/*  84 */         if (matchingBean != null) {
/*  85 */           throw new NoSuchBeanDefinitionException(qualifier, "No unique " + beanType.getSimpleName() + " bean found for qualifier '" + qualifier + "'");
/*     */         }
/*     */ 
/*  88 */         matchingBean = beanName;
/*     */       }
/*     */     }
/*  91 */     if (matchingBean != null) {
/*  92 */       return bf.getBean(matchingBean, beanType);
/*     */     }
/*  94 */     if (bf.containsBean(qualifier))
/*     */     {
/*  96 */       return bf.getBean(qualifier, beanType);
/*     */     }
/*     */ 
/*  99 */     throw new NoSuchBeanDefinitionException(qualifier, "No matching " + beanType.getSimpleName() + " bean found for qualifier '" + qualifier + "' - neither qualifier match nor bean name match!");
/*     */   }
/*     */ 
/*     */   private static boolean isQualifierMatch(String qualifier, String beanName, ConfigurableListableBeanFactory bf)
/*     */   {
/* 114 */     if (bf.containsBean(beanName)) {
/*     */       try {
/* 116 */         BeanDefinition bd = bf.getMergedBeanDefinition(beanName);
/*     */ 
/* 118 */         if ((bd instanceof AbstractBeanDefinition)) {
/* 119 */           AbstractBeanDefinition abd = (AbstractBeanDefinition)bd;
/* 120 */           AutowireCandidateQualifier candidate = abd.getQualifier(Qualifier.class.getName());
/* 121 */           if (((candidate != null) && (qualifier.equals(candidate.getAttribute(AutowireCandidateQualifier.VALUE_KEY)))) || 
/* 122 */             (qualifier
/* 122 */             .equals(beanName)) || 
/* 122 */             (ObjectUtils.containsElement(bf.getAliases(beanName), qualifier))) {
/* 123 */             return true;
/*     */           }
/*     */         }
/*     */ 
/* 127 */         if ((bd instanceof RootBeanDefinition)) {
/* 128 */           Method factoryMethod = ((RootBeanDefinition)bd).getResolvedFactoryMethod();
/* 129 */           if (factoryMethod != null) {
/* 130 */             Qualifier targetAnnotation = (Qualifier)AnnotationUtils.getAnnotation(factoryMethod, Qualifier.class);
/* 131 */             if (targetAnnotation != null) {
/* 132 */               return qualifier.equals(targetAnnotation.value());
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/* 137 */         Class beanType = bf.getType(beanName);
/* 138 */         if (beanType != null) {
/* 139 */           Qualifier targetAnnotation = (Qualifier)AnnotationUtils.getAnnotation(beanType, Qualifier.class);
/* 140 */           if (targetAnnotation != null) {
/* 141 */             return qualifier.equals(targetAnnotation.value());
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (NoSuchBeanDefinitionException localNoSuchBeanDefinitionException)
/*     */       {
/*     */       }
/*     */     }
/* 149 */     return false;
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils
 * JD-Core Version:    0.6.2
 */