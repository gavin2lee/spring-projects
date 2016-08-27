/*    */ package org.springframework.beans.factory;
/*    */ 
/*    */ import org.springframework.beans.FatalBeanException;
/*    */ 
/*    */ public class CannotLoadBeanClassException extends FatalBeanException
/*    */ {
/*    */   private String resourceDescription;
/*    */   private String beanName;
/*    */   private String beanClassName;
/*    */ 
/*    */   public CannotLoadBeanClassException(String resourceDescription, String beanName, String beanClassName, ClassNotFoundException cause)
/*    */   {
/* 49 */     super("Cannot find class [" + beanClassName + "] for bean with name '" + beanName + "' defined in " + resourceDescription, cause);
/*    */ 
/* 51 */     this.resourceDescription = resourceDescription;
/* 52 */     this.beanName = beanName;
/* 53 */     this.beanClassName = beanClassName;
/*    */   }
/*    */ 
/*    */   public CannotLoadBeanClassException(String resourceDescription, String beanName, String beanClassName, LinkageError cause)
/*    */   {
/* 67 */     super("Error loading class [" + beanClassName + "] for bean with name '" + beanName + "' defined in " + resourceDescription + ": problem with class file or dependent class", cause);
/*    */ 
/* 69 */     this.resourceDescription = resourceDescription;
/* 70 */     this.beanName = beanName;
/* 71 */     this.beanClassName = beanClassName;
/*    */   }
/*    */ 
/*    */   public String getResourceDescription()
/*    */   {
/* 80 */     return this.resourceDescription;
/*    */   }
/*    */ 
/*    */   public String getBeanName()
/*    */   {
/* 87 */     return this.beanName;
/*    */   }
/*    */ 
/*    */   public String getBeanClassName()
/*    */   {
/* 94 */     return this.beanClassName;
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.CannotLoadBeanClassException
 * JD-Core Version:    0.6.2
 */