/*    */ package org.springframework.beans.factory;
/*    */ 
/*    */ public class BeanCreationNotAllowedException extends BeanCreationException
/*    */ {
/*    */   public BeanCreationNotAllowedException(String beanName, String msg)
/*    */   {
/* 36 */     super(beanName, msg);
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.BeanCreationNotAllowedException
 * JD-Core Version:    0.6.2
 */