/*    */ package org.springframework.beans.factory;
/*    */ 
/*    */ import org.springframework.beans.BeansException;
/*    */ 
/*    */ public class BeanNotOfRequiredTypeException extends BeansException
/*    */ {
/*    */   private String beanName;
/*    */   private Class<?> requiredType;
/*    */   private Class<?> actualType;
/*    */ 
/*    */   public BeanNotOfRequiredTypeException(String beanName, Class<?> requiredType, Class<?> actualType)
/*    */   {
/* 48 */     super("Bean named '" + beanName + "' is expected to be of type [" + requiredType.getName() + "] but was actually of type [" + actualType
/* 49 */       .getName() + "]");
/* 50 */     this.beanName = beanName;
/* 51 */     this.requiredType = requiredType;
/* 52 */     this.actualType = actualType;
/*    */   }
/*    */ 
/*    */   public String getBeanName()
/*    */   {
/* 60 */     return this.beanName;
/*    */   }
/*    */ 
/*    */   public Class<?> getRequiredType()
/*    */   {
/* 67 */     return this.requiredType;
/*    */   }
/*    */ 
/*    */   public Class<?> getActualType()
/*    */   {
/* 74 */     return this.actualType;
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.BeanNotOfRequiredTypeException
 * JD-Core Version:    0.6.2
 */