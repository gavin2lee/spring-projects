/*    */ package org.springframework.beans;
/*    */ 
/*    */ import java.beans.PropertyChangeEvent;
/*    */ import org.springframework.core.ErrorCoded;
/*    */ 
/*    */ public abstract class PropertyAccessException extends BeansException
/*    */   implements ErrorCoded
/*    */ {
/*    */   private transient PropertyChangeEvent propertyChangeEvent;
/*    */ 
/*    */   public PropertyAccessException(PropertyChangeEvent propertyChangeEvent, String msg, Throwable cause)
/*    */   {
/* 43 */     super(msg, cause);
/* 44 */     this.propertyChangeEvent = propertyChangeEvent;
/*    */   }
/*    */ 
/*    */   public PropertyAccessException(String msg, Throwable cause)
/*    */   {
/* 53 */     super(msg, cause);
/*    */   }
/*    */ 
/*    */   public PropertyChangeEvent getPropertyChangeEvent()
/*    */   {
/* 63 */     return this.propertyChangeEvent;
/*    */   }
/*    */ 
/*    */   public String getPropertyName()
/*    */   {
/* 70 */     return this.propertyChangeEvent != null ? this.propertyChangeEvent.getPropertyName() : null;
/*    */   }
/*    */ 
/*    */   public Object getValue()
/*    */   {
/* 77 */     return this.propertyChangeEvent != null ? this.propertyChangeEvent.getNewValue() : null;
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.PropertyAccessException
 * JD-Core Version:    0.6.2
 */