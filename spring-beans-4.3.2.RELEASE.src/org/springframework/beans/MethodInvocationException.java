/*    */ package org.springframework.beans;
/*    */ 
/*    */ import java.beans.PropertyChangeEvent;
/*    */ 
/*    */ public class MethodInvocationException extends PropertyAccessException
/*    */ {
/*    */   public static final String ERROR_CODE = "methodInvocation";
/*    */ 
/*    */   public MethodInvocationException(PropertyChangeEvent propertyChangeEvent, Throwable cause)
/*    */   {
/* 42 */     super(propertyChangeEvent, "Property '" + propertyChangeEvent.getPropertyName() + "' threw exception", cause);
/*    */   }
/*    */ 
/*    */   public String getErrorCode()
/*    */   {
/* 47 */     return "methodInvocation";
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.MethodInvocationException
 * JD-Core Version:    0.6.2
 */