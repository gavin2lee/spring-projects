/*    */ package org.springframework.beans.factory.parsing;
/*    */ 
/*    */ import org.springframework.util.StringUtils;
/*    */ 
/*    */ public class QualifierEntry
/*    */   implements ParseState.Entry
/*    */ {
/*    */   private String typeName;
/*    */ 
/*    */   public QualifierEntry(String typeName)
/*    */   {
/* 33 */     if (!StringUtils.hasText(typeName)) {
/* 34 */       throw new IllegalArgumentException("Invalid qualifier type '" + typeName + "'.");
/*    */     }
/* 36 */     this.typeName = typeName;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 41 */     return "Qualifier '" + this.typeName + "'";
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.QualifierEntry
 * JD-Core Version:    0.6.2
 */