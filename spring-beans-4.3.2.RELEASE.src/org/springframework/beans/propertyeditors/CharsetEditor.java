/*    */ package org.springframework.beans.propertyeditors;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ import java.nio.charset.Charset;
/*    */ import org.springframework.util.StringUtils;
/*    */ 
/*    */ public class CharsetEditor extends PropertyEditorSupport
/*    */ {
/*    */   public void setAsText(String text)
/*    */     throws IllegalArgumentException
/*    */   {
/* 39 */     if (StringUtils.hasText(text)) {
/* 40 */       setValue(Charset.forName(text));
/*    */     }
/*    */     else
/* 43 */       setValue(null);
/*    */   }
/*    */ 
/*    */   public String getAsText()
/*    */   {
/* 49 */     Charset value = (Charset)getValue();
/* 50 */     return value != null ? value.name() : "";
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.CharsetEditor
 * JD-Core Version:    0.6.2
 */