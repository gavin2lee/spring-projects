/*    */ package org.springframework.beans;
/*    */ 
/*    */ public class NullValueInNestedPathException extends InvalidPropertyException
/*    */ {
/*    */   public NullValueInNestedPathException(Class<?> beanClass, String propertyName)
/*    */   {
/* 38 */     super(beanClass, propertyName, "Value of nested property '" + propertyName + "' is null");
/*    */   }
/*    */ 
/*    */   public NullValueInNestedPathException(Class<?> beanClass, String propertyName, String msg)
/*    */   {
/* 48 */     super(beanClass, propertyName, msg);
/*    */   }
/*    */ 
/*    */   public NullValueInNestedPathException(Class<?> beanClass, String propertyName, String msg, Throwable cause)
/*    */   {
/* 60 */     super(beanClass, propertyName, msg, cause);
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.NullValueInNestedPathException
 * JD-Core Version:    0.6.2
 */