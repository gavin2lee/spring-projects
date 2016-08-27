/*    */ package org.springframework.beans.factory.parsing;
/*    */ 
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.config.BeanReference;
/*    */ 
/*    */ public abstract class AbstractComponentDefinition
/*    */   implements ComponentDefinition
/*    */ {
/*    */   public String getDescription()
/*    */   {
/* 40 */     return getName();
/*    */   }
/*    */ 
/*    */   public BeanDefinition[] getBeanDefinitions()
/*    */   {
/* 48 */     return new BeanDefinition[0];
/*    */   }
/*    */ 
/*    */   public BeanDefinition[] getInnerBeanDefinitions()
/*    */   {
/* 56 */     return new BeanDefinition[0];
/*    */   }
/*    */ 
/*    */   public BeanReference[] getBeanReferences()
/*    */   {
/* 64 */     return new BeanReference[0];
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 72 */     return getDescription();
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.AbstractComponentDefinition
 * JD-Core Version:    0.6.2
 */