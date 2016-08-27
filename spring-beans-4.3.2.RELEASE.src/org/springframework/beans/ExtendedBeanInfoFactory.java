/*    */ package org.springframework.beans;
/*    */ 
/*    */ import java.beans.BeanInfo;
/*    */ import java.beans.IntrospectionException;
/*    */ import java.beans.Introspector;
/*    */ import java.lang.reflect.Method;
/*    */ import org.springframework.core.Ordered;
/*    */ 
/*    */ public class ExtendedBeanInfoFactory
/*    */   implements BeanInfoFactory, Ordered
/*    */ {
/*    */   public BeanInfo getBeanInfo(Class<?> beanClass)
/*    */     throws IntrospectionException
/*    */   {
/* 46 */     return supports(beanClass) ? new ExtendedBeanInfo(Introspector.getBeanInfo(beanClass)) : null;
/*    */   }
/*    */ 
/*    */   private boolean supports(Class<?> beanClass)
/*    */   {
/* 54 */     for (Method method : beanClass.getMethods()) {
/* 55 */       if (ExtendedBeanInfo.isCandidateWriteMethod(method)) {
/* 56 */         return true;
/*    */       }
/*    */     }
/* 59 */     return false;
/*    */   }
/*    */ 
/*    */   public int getOrder()
/*    */   {
/* 64 */     return 2147483647;
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.ExtendedBeanInfoFactory
 * JD-Core Version:    0.6.2
 */