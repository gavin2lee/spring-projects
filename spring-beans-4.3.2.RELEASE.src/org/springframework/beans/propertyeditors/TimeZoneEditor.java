/*    */ package org.springframework.beans.propertyeditors;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ import java.util.TimeZone;
/*    */ import org.springframework.util.StringUtils;
/*    */ 
/*    */ public class TimeZoneEditor extends PropertyEditorSupport
/*    */ {
/*    */   public void setAsText(String text)
/*    */     throws IllegalArgumentException
/*    */   {
/* 39 */     setValue(StringUtils.parseTimeZoneString(text));
/*    */   }
/*    */ 
/*    */   public String getAsText()
/*    */   {
/* 44 */     TimeZone value = (TimeZone)getValue();
/* 45 */     return value != null ? value.getID() : "";
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.TimeZoneEditor
 * JD-Core Version:    0.6.2
 */