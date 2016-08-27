/*    */ package org.springframework.beans.factory;
/*    */ 
/*    */ import org.springframework.beans.FatalBeanException;
/*    */ 
/*    */ public class BeanInitializationException extends FatalBeanException
/*    */ {
/*    */   public BeanInitializationException(String msg)
/*    */   {
/* 42 */     super(msg);
/*    */   }
/*    */ 
/*    */   public BeanInitializationException(String msg, Throwable cause)
/*    */   {
/* 52 */     super(msg, cause);
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.BeanInitializationException
 * JD-Core Version:    0.6.2
 */