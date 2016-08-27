/*    */ package org.springframework.beans.factory.support;
/*    */ 
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*    */ import org.springframework.beans.factory.config.DependencyDescriptor;
/*    */ 
/*    */ public class SimpleAutowireCandidateResolver
/*    */   implements AutowireCandidateResolver
/*    */ {
/*    */   public boolean isAutowireCandidate(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor)
/*    */   {
/* 41 */     return bdHolder.getBeanDefinition().isAutowireCandidate();
/*    */   }
/*    */ 
/*    */   public Object getSuggestedValue(DependencyDescriptor descriptor)
/*    */   {
/* 46 */     return null;
/*    */   }
/*    */ 
/*    */   public Object getLazyResolutionProxyIfNecessary(DependencyDescriptor descriptor, String beanName)
/*    */   {
/* 51 */     return null;
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.support.SimpleAutowireCandidateResolver
 * JD-Core Version:    0.6.2
 */