/*    */ package org.springframework.beans.propertyeditors;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ 
/*    */ public class ByteArrayPropertyEditor extends PropertyEditorSupport
/*    */ {
/*    */   public void setAsText(String text)
/*    */   {
/* 33 */     setValue(text != null ? text.getBytes() : null);
/*    */   }
/*    */ 
/*    */   public String getAsText()
/*    */   {
/* 38 */     byte[] value = (byte[])getValue();
/* 39 */     return value != null ? new String(value) : "";
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.ByteArrayPropertyEditor
 * JD-Core Version:    0.6.2
 */