/*    */ package org.springframework.beans.factory;
/*    */ 
/*    */ import org.springframework.beans.FatalBeanException;
/*    */ 
/*    */ public class BeanExpressionException extends FatalBeanException
/*    */ {
/*    */   public BeanExpressionException(String msg)
/*    */   {
/* 35 */     super(msg);
/*    */   }
/*    */ 
/*    */   public BeanExpressionException(String msg, Throwable cause)
/*    */   {
/* 45 */     super(msg, cause);
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.BeanExpressionException
 * JD-Core Version:    0.6.2
 */