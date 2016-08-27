/*    */ package org.springframework.beans;
/*    */ 
/*    */ public class FatalBeanException extends BeansException
/*    */ {
/*    */   public FatalBeanException(String msg)
/*    */   {
/* 33 */     super(msg);
/*    */   }
/*    */ 
/*    */   public FatalBeanException(String msg, Throwable cause)
/*    */   {
/* 43 */     super(msg, cause);
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.FatalBeanException
 * JD-Core Version:    0.6.2
 */