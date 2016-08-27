/*    */ package org.springframework.beans.propertyeditors;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ import java.util.UUID;
/*    */ import org.springframework.util.StringUtils;
/*    */ 
/*    */ public class UUIDEditor extends PropertyEditorSupport
/*    */ {
/*    */   public void setAsText(String text)
/*    */     throws IllegalArgumentException
/*    */   {
/* 36 */     if (StringUtils.hasText(text)) {
/* 37 */       setValue(UUID.fromString(text));
/*    */     }
/*    */     else
/* 40 */       setValue(null);
/*    */   }
/*    */ 
/*    */   public String getAsText()
/*    */   {
/* 46 */     UUID value = (UUID)getValue();
/* 47 */     return value != null ? value.toString() : "";
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.UUIDEditor
 * JD-Core Version:    0.6.2
 */