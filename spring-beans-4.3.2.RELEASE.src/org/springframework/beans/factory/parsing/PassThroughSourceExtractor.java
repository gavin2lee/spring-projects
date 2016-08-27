/*    */ package org.springframework.beans.factory.parsing;
/*    */ 
/*    */ import org.springframework.core.io.Resource;
/*    */ 
/*    */ public class PassThroughSourceExtractor
/*    */   implements SourceExtractor
/*    */ {
/*    */   public Object extractSource(Object sourceCandidate, Resource definingResource)
/*    */   {
/* 44 */     return sourceCandidate;
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.PassThroughSourceExtractor
 * JD-Core Version:    0.6.2
 */