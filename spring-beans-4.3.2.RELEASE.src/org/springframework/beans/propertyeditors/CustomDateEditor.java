/*     */ package org.springframework.beans.propertyeditors;
/*     */ 
/*     */ import java.beans.PropertyEditorSupport;
/*     */ import java.text.DateFormat;
/*     */ import java.text.ParseException;
/*     */ import java.util.Date;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ public class CustomDateEditor extends PropertyEditorSupport
/*     */ {
/*     */   private final DateFormat dateFormat;
/*     */   private final boolean allowEmpty;
/*     */   private final int exactDateLength;
/*     */ 
/*     */   public CustomDateEditor(DateFormat dateFormat, boolean allowEmpty)
/*     */   {
/*  63 */     this.dateFormat = dateFormat;
/*  64 */     this.allowEmpty = allowEmpty;
/*  65 */     this.exactDateLength = -1;
/*     */   }
/*     */ 
/*     */   public CustomDateEditor(DateFormat dateFormat, boolean allowEmpty, int exactDateLength)
/*     */   {
/*  87 */     this.dateFormat = dateFormat;
/*  88 */     this.allowEmpty = allowEmpty;
/*  89 */     this.exactDateLength = exactDateLength;
/*     */   }
/*     */ 
/*     */   public void setAsText(String text)
/*     */     throws IllegalArgumentException
/*     */   {
/*  98 */     if ((this.allowEmpty) && (!StringUtils.hasText(text)))
/*     */     {
/* 100 */       setValue(null);
/*     */     } else {
/* 102 */       if ((text != null) && (this.exactDateLength >= 0) && (text.length() != this.exactDateLength)) {
/* 103 */         throw new IllegalArgumentException("Could not parse date: it is not exactly" + this.exactDateLength + "characters long");
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 108 */         setValue(this.dateFormat.parse(text));
/*     */       }
/*     */       catch (ParseException ex) {
/* 111 */         throw new IllegalArgumentException("Could not parse date: " + ex.getMessage(), ex);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getAsText()
/*     */   {
/* 121 */     Date value = (Date)getValue();
/* 122 */     return value != null ? this.dateFormat.format(value) : "";
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.CustomDateEditor
 * JD-Core Version:    0.6.2
 */