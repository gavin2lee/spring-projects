/*    */ package org.springframework.beans.factory.support;
/*    */ 
/*    */ import org.springframework.beans.FatalBeanException;
/*    */ 
/*    */ public class BeanDefinitionValidationException extends FatalBeanException
/*    */ {
/*    */   public BeanDefinitionValidationException(String msg)
/*    */   {
/* 36 */     super(msg);
/*    */   }
/*    */ 
/*    */   public BeanDefinitionValidationException(String msg, Throwable cause)
/*    */   {
/* 46 */     super(msg, cause);
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.support.BeanDefinitionValidationException
 * JD-Core Version:    0.6.2
 */