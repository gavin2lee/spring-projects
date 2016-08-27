/*    */ package org.springframework.beans.factory.config;
/*    */ 
/*    */ import java.beans.PropertyDescriptor;
/*    */ import java.lang.reflect.Constructor;
/*    */ import org.springframework.beans.BeansException;
/*    */ import org.springframework.beans.PropertyValues;
/*    */ 
/*    */ public abstract class InstantiationAwareBeanPostProcessorAdapter
/*    */   implements SmartInstantiationAwareBeanPostProcessor
/*    */ {
/*    */   public Class<?> predictBeanType(Class<?> beanClass, String beanName)
/*    */   {
/* 44 */     return null;
/*    */   }
/*    */ 
/*    */   public Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, String beanName) throws BeansException
/*    */   {
/* 49 */     return null;
/*    */   }
/*    */ 
/*    */   public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException
/*    */   {
/* 54 */     return bean;
/*    */   }
/*    */ 
/*    */   public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException
/*    */   {
/* 59 */     return null;
/*    */   }
/*    */ 
/*    */   public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException
/*    */   {
/* 64 */     return true;
/*    */   }
/*    */ 
/*    */   public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName)
/*    */     throws BeansException
/*    */   {
/* 72 */     return pvs;
/*    */   }
/*    */ 
/*    */   public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException
/*    */   {
/* 77 */     return bean;
/*    */   }
/*    */ 
/*    */   public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException
/*    */   {
/* 82 */     return bean;
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter
 * JD-Core Version:    0.6.2
 */