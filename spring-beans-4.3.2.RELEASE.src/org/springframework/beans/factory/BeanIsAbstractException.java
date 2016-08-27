/*    */ package org.springframework.beans.factory;
/*    */ 
/*    */ public class BeanIsAbstractException extends BeanCreationException
/*    */ {
/*    */   public BeanIsAbstractException(String beanName)
/*    */   {
/* 35 */     super(beanName, "Bean definition is abstract");
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.BeanIsAbstractException
 * JD-Core Version:    0.6.2
 */