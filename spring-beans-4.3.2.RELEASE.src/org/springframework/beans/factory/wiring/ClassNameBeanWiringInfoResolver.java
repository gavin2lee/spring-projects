/*    */ package org.springframework.beans.factory.wiring;
/*    */ 
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.util.ClassUtils;
/*    */ 
/*    */ public class ClassNameBeanWiringInfoResolver
/*    */   implements BeanWiringInfoResolver
/*    */ {
/*    */   public BeanWiringInfo resolveWiringInfo(Object beanInstance)
/*    */   {
/* 36 */     Assert.notNull(beanInstance, "Bean instance must not be null");
/* 37 */     return new BeanWiringInfo(ClassUtils.getUserClass(beanInstance).getName(), true);
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.wiring.ClassNameBeanWiringInfoResolver
 * JD-Core Version:    0.6.2
 */