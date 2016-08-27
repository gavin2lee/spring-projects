/*    */ package org.springframework.beans.factory.access.el;
/*    */ 
/*    */ import javax.el.ELContext;
/*    */ import org.springframework.beans.factory.BeanFactory;
/*    */ import org.springframework.util.Assert;
/*    */ 
/*    */ public class SimpleSpringBeanELResolver extends SpringBeanELResolver
/*    */ {
/*    */   private final BeanFactory beanFactory;
/*    */ 
/*    */   public SimpleSpringBeanELResolver(BeanFactory beanFactory)
/*    */   {
/* 41 */     Assert.notNull(beanFactory, "BeanFactory must not be null");
/* 42 */     this.beanFactory = beanFactory;
/*    */   }
/*    */ 
/*    */   protected BeanFactory getBeanFactory(ELContext elContext)
/*    */   {
/* 47 */     return this.beanFactory;
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.access.el.SimpleSpringBeanELResolver
 * JD-Core Version:    0.6.2
 */