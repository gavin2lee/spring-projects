/*    */ package org.springframework.beans.factory;
/*    */ 
/*    */ public class BeanIsNotAFactoryException extends BeanNotOfRequiredTypeException
/*    */ {
/*    */   public BeanIsNotAFactoryException(String name, Class<?> actualType)
/*    */   {
/* 38 */     super(name, FactoryBean.class, actualType);
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.BeanIsNotAFactoryException
 * JD-Core Version:    0.6.2
 */