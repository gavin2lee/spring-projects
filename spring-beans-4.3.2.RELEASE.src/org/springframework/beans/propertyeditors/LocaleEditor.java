/*    */ package org.springframework.beans.propertyeditors;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ import org.springframework.util.StringUtils;
/*    */ 
/*    */ public class LocaleEditor extends PropertyEditorSupport
/*    */ {
/*    */   public void setAsText(String text)
/*    */   {
/* 39 */     setValue(StringUtils.parseLocaleString(text));
/*    */   }
/*    */ 
/*    */   public String getAsText()
/*    */   {
/* 44 */     Object value = getValue();
/* 45 */     return value != null ? value.toString() : "";
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.LocaleEditor
 * JD-Core Version:    0.6.2
 */