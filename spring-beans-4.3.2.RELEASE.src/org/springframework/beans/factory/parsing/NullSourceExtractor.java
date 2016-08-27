/*    */ package org.springframework.beans.factory.parsing;
/*    */ 
/*    */ import org.springframework.core.io.Resource;
/*    */ 
/*    */ public class NullSourceExtractor
/*    */   implements SourceExtractor
/*    */ {
/*    */   public Object extractSource(Object sourceCandidate, Resource definitionResource)
/*    */   {
/* 38 */     return null;
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.NullSourceExtractor
 * JD-Core Version:    0.6.2
 */