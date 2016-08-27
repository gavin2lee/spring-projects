/*    */ package org.springframework.beans;
/*    */ 
/*    */ public abstract class PropertyAccessorFactory
/*    */ {
/*    */   public static BeanWrapper forBeanPropertyAccess(Object target)
/*    */   {
/* 37 */     return new BeanWrapperImpl(target);
/*    */   }
/*    */ 
/*    */   public static ConfigurablePropertyAccessor forDirectFieldAccess(Object target)
/*    */   {
/* 48 */     return new DirectFieldAccessor(target);
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.PropertyAccessorFactory
 * JD-Core Version:    0.6.2
 */