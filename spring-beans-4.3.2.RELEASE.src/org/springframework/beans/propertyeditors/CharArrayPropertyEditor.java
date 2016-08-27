/*    */ package org.springframework.beans.propertyeditors;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ 
/*    */ public class CharArrayPropertyEditor extends PropertyEditorSupport
/*    */ {
/*    */   public void setAsText(String text)
/*    */   {
/* 33 */     setValue(text != null ? text.toCharArray() : null);
/*    */   }
/*    */ 
/*    */   public String getAsText()
/*    */   {
/* 38 */     char[] value = (char[])getValue();
/* 39 */     return value != null ? new String(value) : "";
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.CharArrayPropertyEditor
 * JD-Core Version:    0.6.2
 */