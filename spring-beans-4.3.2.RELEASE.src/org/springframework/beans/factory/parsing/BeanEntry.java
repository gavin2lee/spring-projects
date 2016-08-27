/*    */ package org.springframework.beans.factory.parsing;
/*    */ 
/*    */ public class BeanEntry
/*    */   implements ParseState.Entry
/*    */ {
/*    */   private String beanDefinitionName;
/*    */ 
/*    */   public BeanEntry(String beanDefinitionName)
/*    */   {
/* 35 */     this.beanDefinitionName = beanDefinitionName;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 41 */     return "Bean '" + this.beanDefinitionName + "'";
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.BeanEntry
 * JD-Core Version:    0.6.2
 */