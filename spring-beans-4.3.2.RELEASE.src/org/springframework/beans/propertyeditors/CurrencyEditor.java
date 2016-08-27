/*    */ package org.springframework.beans.propertyeditors;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ import java.util.Currency;
/*    */ 
/*    */ public class CurrencyEditor extends PropertyEditorSupport
/*    */ {
/*    */   public void setAsText(String text)
/*    */     throws IllegalArgumentException
/*    */   {
/* 34 */     setValue(Currency.getInstance(text));
/*    */   }
/*    */ 
/*    */   public String getAsText()
/*    */   {
/* 39 */     Currency value = (Currency)getValue();
/* 40 */     return value != null ? value.getCurrencyCode() : "";
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.CurrencyEditor
 * JD-Core Version:    0.6.2
 */