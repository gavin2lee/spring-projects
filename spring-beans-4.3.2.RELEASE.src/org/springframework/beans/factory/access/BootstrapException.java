/*    */ package org.springframework.beans.factory.access;
/*    */ 
/*    */ import org.springframework.beans.FatalBeanException;
/*    */ 
/*    */ public class BootstrapException extends FatalBeanException
/*    */ {
/*    */   public BootstrapException(String msg)
/*    */   {
/* 35 */     super(msg);
/*    */   }
/*    */ 
/*    */   public BootstrapException(String msg, Throwable cause)
/*    */   {
/* 45 */     super(msg, cause);
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.access.BootstrapException
 * JD-Core Version:    0.6.2
 */