/*    */ package org.springframework.beans.factory;
/*    */ 
/*    */ public class BeanCurrentlyInCreationException extends BeanCreationException
/*    */ {
/*    */   public BeanCurrentlyInCreationException(String beanName)
/*    */   {
/* 35 */     super(beanName, "Requested bean is currently in creation: Is there an unresolvable circular reference?");
/*    */   }
/*    */ 
/*    */   public BeanCurrentlyInCreationException(String beanName, String msg)
/*    */   {
/* 45 */     super(beanName, msg);
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.BeanCurrentlyInCreationException
 * JD-Core Version:    0.6.2
 */