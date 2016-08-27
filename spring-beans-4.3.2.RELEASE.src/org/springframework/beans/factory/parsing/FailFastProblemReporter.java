/*    */ package org.springframework.beans.factory.parsing;
/*    */ 
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ 
/*    */ public class FailFastProblemReporter
/*    */   implements ProblemReporter
/*    */ {
/* 39 */   private Log logger = LogFactory.getLog(getClass());
/*    */ 
/*    */   public void setLogger(Log logger)
/*    */   {
/* 49 */     this.logger = (logger != null ? logger : LogFactory.getLog(getClass()));
/*    */   }
/*    */ 
/*    */   public void fatal(Problem problem)
/*    */   {
/* 60 */     throw new BeanDefinitionParsingException(problem);
/*    */   }
/*    */ 
/*    */   public void error(Problem problem)
/*    */   {
/* 70 */     throw new BeanDefinitionParsingException(problem);
/*    */   }
/*    */ 
/*    */   public void warning(Problem problem)
/*    */   {
/* 79 */     this.logger.warn(problem, problem.getRootCause());
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.FailFastProblemReporter
 * JD-Core Version:    0.6.2
 */