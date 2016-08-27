/*    */ package org.springframework.beans.propertyeditors;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ import java.util.regex.Pattern;
/*    */ 
/*    */ public class PatternEditor extends PropertyEditorSupport
/*    */ {
/*    */   private final int flags;
/*    */ 
/*    */   public PatternEditor()
/*    */   {
/* 40 */     this.flags = 0;
/*    */   }
/*    */ 
/*    */   public PatternEditor(int flags)
/*    */   {
/* 54 */     this.flags = flags;
/*    */   }
/*    */ 
/*    */   public void setAsText(String text)
/*    */   {
/* 60 */     setValue(text != null ? Pattern.compile(text, this.flags) : null);
/*    */   }
/*    */ 
/*    */   public String getAsText()
/*    */   {
/* 65 */     Pattern value = (Pattern)getValue();
/* 66 */     return value != null ? value.pattern() : "";
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.PatternEditor
 * JD-Core Version:    0.6.2
 */