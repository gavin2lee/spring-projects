/*    */ package org.springframework.beans.factory.parsing;
/*    */ 
/*    */ import org.springframework.util.Assert;
/*    */ 
/*    */ public class ConstructorArgumentEntry
/*    */   implements ParseState.Entry
/*    */ {
/*    */   private final int index;
/*    */ 
/*    */   public ConstructorArgumentEntry()
/*    */   {
/* 39 */     this.index = -1;
/*    */   }
/*    */ 
/*    */   public ConstructorArgumentEntry(int index)
/*    */   {
/* 50 */     Assert.isTrue(index >= 0, "Constructor argument index must be greater than or equal to zero");
/* 51 */     this.index = index;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 57 */     return new StringBuilder().append("Constructor-arg").append(this.index >= 0 ? new StringBuilder().append(" #").append(this.index).toString() : "").toString();
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.ConstructorArgumentEntry
 * JD-Core Version:    0.6.2
 */