/*    */ package org.springframework.beans;
/*    */ 
/*    */ import java.beans.PropertyChangeEvent;
/*    */ 
/*    */ public class ConversionNotSupportedException extends TypeMismatchException
/*    */ {
/*    */   public ConversionNotSupportedException(PropertyChangeEvent propertyChangeEvent, Class<?> requiredType, Throwable cause)
/*    */   {
/* 39 */     super(propertyChangeEvent, requiredType, cause);
/*    */   }
/*    */ 
/*    */   public ConversionNotSupportedException(Object value, Class<?> requiredType, Throwable cause)
/*    */   {
/* 49 */     super(value, requiredType, cause);
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.ConversionNotSupportedException
 * JD-Core Version:    0.6.2
 */