/*    */ package org.springframework.beans;
/*    */ 
/*    */ public class NotReadablePropertyException extends InvalidPropertyException
/*    */ {
/*    */   public NotReadablePropertyException(Class<?> beanClass, String propertyName)
/*    */   {
/* 35 */     super(beanClass, propertyName, "Bean property '" + propertyName + "' is not readable or has an invalid getter method: " + "Does the return type of the getter match the parameter type of the setter?");
/*    */   }
/*    */ 
/*    */   public NotReadablePropertyException(Class<?> beanClass, String propertyName, String msg)
/*    */   {
/* 47 */     super(beanClass, propertyName, msg);
/*    */   }
/*    */ 
/*    */   public NotReadablePropertyException(Class<?> beanClass, String propertyName, String msg, Throwable cause)
/*    */   {
/* 59 */     super(beanClass, propertyName, msg, cause);
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.NotReadablePropertyException
 * JD-Core Version:    0.6.2
 */