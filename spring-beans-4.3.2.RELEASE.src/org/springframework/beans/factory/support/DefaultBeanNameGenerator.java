/*    */ package org.springframework.beans.factory.support;
/*    */ 
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ 
/*    */ public class DefaultBeanNameGenerator
/*    */   implements BeanNameGenerator
/*    */ {
/*    */   public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry)
/*    */   {
/* 32 */     return BeanDefinitionReaderUtils.generateBeanName(definition, registry);
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.support.DefaultBeanNameGenerator
 * JD-Core Version:    0.6.2
 */