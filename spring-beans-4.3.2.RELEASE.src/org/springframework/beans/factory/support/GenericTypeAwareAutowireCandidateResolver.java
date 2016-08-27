/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.config.DependencyDescriptor;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ public class GenericTypeAwareAutowireCandidateResolver
/*     */   implements AutowireCandidateResolver, BeanFactoryAware
/*     */ {
/*     */   private BeanFactory beanFactory;
/*     */ 
/*     */   public void setBeanFactory(BeanFactory beanFactory)
/*     */   {
/*  50 */     this.beanFactory = beanFactory;
/*     */   }
/*     */ 
/*     */   protected final BeanFactory getBeanFactory() {
/*  54 */     return this.beanFactory;
/*     */   }
/*     */ 
/*     */   public boolean isAutowireCandidate(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor)
/*     */   {
/*  60 */     if (!bdHolder.getBeanDefinition().isAutowireCandidate())
/*     */     {
/*  62 */       return false;
/*     */     }
/*  64 */     return (descriptor == null) || (checkGenericTypeMatch(bdHolder, descriptor));
/*     */   }
/*     */ 
/*     */   protected boolean checkGenericTypeMatch(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor)
/*     */   {
/*  72 */     ResolvableType dependencyType = descriptor.getResolvableType();
/*  73 */     if ((dependencyType.getType() instanceof Class))
/*     */     {
/*  75 */       return true;
/*     */     }
/*  77 */     ResolvableType targetType = null;
/*  78 */     RootBeanDefinition rbd = null;
/*  79 */     if ((bdHolder.getBeanDefinition() instanceof RootBeanDefinition)) {
/*  80 */       rbd = (RootBeanDefinition)bdHolder.getBeanDefinition();
/*     */     }
/*  82 */     if (rbd != null)
/*     */     {
/*  84 */       targetType = getReturnTypeForFactoryMethod(rbd, descriptor);
/*  85 */       if (targetType == null) {
/*  86 */         RootBeanDefinition dbd = getResolvedDecoratedDefinition(rbd);
/*  87 */         if (dbd != null) {
/*  88 */           targetType = getReturnTypeForFactoryMethod(dbd, descriptor);
/*     */         }
/*     */       }
/*     */     }
/*  92 */     if (targetType == null)
/*     */     {
/*  94 */       if (this.beanFactory != null) {
/*  95 */         Class beanType = this.beanFactory.getType(bdHolder.getBeanName());
/*  96 */         if (beanType != null) {
/*  97 */           targetType = ResolvableType.forClass(ClassUtils.getUserClass(beanType));
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 102 */       if ((targetType == null) && (rbd != null) && (rbd.hasBeanClass()) && (rbd.getFactoryMethodName() == null)) {
/* 103 */         Class beanClass = rbd.getBeanClass();
/* 104 */         if (!FactoryBean.class.isAssignableFrom(beanClass)) {
/* 105 */           targetType = ResolvableType.forClass(ClassUtils.getUserClass(beanClass));
/*     */         }
/*     */       }
/*     */     }
/* 109 */     if ((targetType == null) || ((descriptor.fallbackMatchAllowed()) && (targetType.hasUnresolvableGenerics()))) {
/* 110 */       return true;
/*     */     }
/*     */ 
/* 113 */     return dependencyType.isAssignableFrom(targetType);
/*     */   }
/*     */ 
/*     */   protected RootBeanDefinition getResolvedDecoratedDefinition(RootBeanDefinition rbd) {
/* 117 */     BeanDefinitionHolder decDef = rbd.getDecoratedDefinition();
/* 118 */     if ((decDef != null) && ((this.beanFactory instanceof ConfigurableListableBeanFactory))) {
/* 119 */       ConfigurableListableBeanFactory clbf = (ConfigurableListableBeanFactory)this.beanFactory;
/* 120 */       if (clbf.containsBeanDefinition(decDef.getBeanName())) {
/* 121 */         BeanDefinition dbd = clbf.getMergedBeanDefinition(decDef.getBeanName());
/* 122 */         if ((dbd instanceof RootBeanDefinition)) {
/* 123 */           return (RootBeanDefinition)dbd;
/*     */         }
/*     */       }
/*     */     }
/* 127 */     return null;
/*     */   }
/*     */ 
/*     */   protected ResolvableType getReturnTypeForFactoryMethod(RootBeanDefinition rbd, DependencyDescriptor descriptor)
/*     */   {
/* 133 */     Class preResolved = rbd.resolvedFactoryMethodReturnType;
/* 134 */     if (preResolved != null) {
/* 135 */       return ResolvableType.forClass(preResolved);
/*     */     }
/*     */ 
/* 138 */     Method resolvedFactoryMethod = rbd.getResolvedFactoryMethod();
/* 139 */     if ((resolvedFactoryMethod != null) && 
/* 140 */       (descriptor.getDependencyType().isAssignableFrom(resolvedFactoryMethod.getReturnType())))
/*     */     {
/* 144 */       return ResolvableType.forMethodReturnType(resolvedFactoryMethod);
/*     */     }
/*     */ 
/* 147 */     return null;
/*     */   }
/*     */ 
/*     */   public Object getSuggestedValue(DependencyDescriptor descriptor)
/*     */   {
/* 158 */     return null;
/*     */   }
/*     */ 
/*     */   public Object getLazyResolutionProxyIfNecessary(DependencyDescriptor descriptor, String beanName)
/*     */   {
/* 167 */     return null;
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.support.GenericTypeAwareAutowireCandidateResolver
 * JD-Core Version:    0.6.2
 */