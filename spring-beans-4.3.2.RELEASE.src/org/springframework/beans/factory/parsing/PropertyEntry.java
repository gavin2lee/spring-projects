/*    */ package org.springframework.beans.factory.parsing;
/*    */ 
/*    */ import org.springframework.util.StringUtils;
/*    */ 
/*    */ public class PropertyEntry
/*    */   implements ParseState.Entry
/*    */ {
/*    */   private final String name;
/*    */ 
/*    */   public PropertyEntry(String name)
/*    */   {
/* 39 */     if (!StringUtils.hasText(name)) {
/* 40 */       throw new IllegalArgumentException("Invalid property name '" + name + "'.");
/*    */     }
/* 42 */     this.name = name;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 48 */     return "Property '" + this.name + "'";
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.PropertyEntry
 * JD-Core Version:    0.6.2
 */