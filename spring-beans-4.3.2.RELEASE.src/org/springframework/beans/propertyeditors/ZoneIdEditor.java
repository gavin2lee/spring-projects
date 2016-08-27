/*    */ package org.springframework.beans.propertyeditors;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ import java.time.ZoneId;
/*    */ import org.springframework.lang.UsesJava8;
/*    */ 
/*    */ @UsesJava8
/*    */ public class ZoneIdEditor extends PropertyEditorSupport
/*    */ {
/*    */   public void setAsText(String text)
/*    */     throws IllegalArgumentException
/*    */   {
/* 38 */     setValue(ZoneId.of(text));
/*    */   }
/*    */ 
/*    */   public String getAsText()
/*    */   {
/* 43 */     ZoneId value = (ZoneId)getValue();
/* 44 */     return value != null ? value.getId() : "";
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.ZoneIdEditor
 * JD-Core Version:    0.6.2
 */