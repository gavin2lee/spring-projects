/*    */ package org.springframework.beans.factory;
/*    */ 
/*    */ import org.springframework.beans.FatalBeanException;
/*    */ 
/*    */ public class FactoryBeanNotInitializedException extends FatalBeanException
/*    */ {
/*    */   public FactoryBeanNotInitializedException()
/*    */   {
/* 44 */     super("FactoryBean is not fully initialized yet");
/*    */   }
/*    */ 
/*    */   public FactoryBeanNotInitializedException(String msg)
/*    */   {
/* 52 */     super(msg);
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.FactoryBeanNotInitializedException
 * JD-Core Version:    0.6.2
 */