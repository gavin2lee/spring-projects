/*    */ package org.springframework.beans.factory.parsing;
/*    */ 
/*    */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*    */ 
/*    */ public class BeanDefinitionParsingException extends BeanDefinitionStoreException
/*    */ {
/*    */   public BeanDefinitionParsingException(Problem problem)
/*    */   {
/* 37 */     super(problem.getResourceDescription(), problem.toString(), problem.getRootCause());
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.BeanDefinitionParsingException
 * JD-Core Version:    0.6.2
 */