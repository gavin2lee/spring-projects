/*     */ package org.springframework.beans.propertyeditors;
/*     */ 
/*     */ import java.beans.PropertyEditorSupport;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ public class StringArrayPropertyEditor extends PropertyEditorSupport
/*     */ {
/*     */   public static final String DEFAULT_SEPARATOR = ",";
/*     */   private final String separator;
/*     */   private final String charsToDelete;
/*     */   private final boolean emptyArrayAsNull;
/*     */   private final boolean trimValues;
/*     */ 
/*     */   public StringArrayPropertyEditor()
/*     */   {
/*  59 */     this(",", null, false);
/*     */   }
/*     */ 
/*     */   public StringArrayPropertyEditor(String separator)
/*     */   {
/*  68 */     this(separator, null, false);
/*     */   }
/*     */ 
/*     */   public StringArrayPropertyEditor(String separator, boolean emptyArrayAsNull)
/*     */   {
/*  78 */     this(separator, null, emptyArrayAsNull);
/*     */   }
/*     */ 
/*     */   public StringArrayPropertyEditor(String separator, boolean emptyArrayAsNull, boolean trimValues)
/*     */   {
/*  90 */     this(separator, null, emptyArrayAsNull, trimValues);
/*     */   }
/*     */ 
/*     */   public StringArrayPropertyEditor(String separator, String charsToDelete, boolean emptyArrayAsNull)
/*     */   {
/* 103 */     this(separator, charsToDelete, emptyArrayAsNull, true);
/*     */   }
/*     */ 
/*     */   public StringArrayPropertyEditor(String separator, String charsToDelete, boolean emptyArrayAsNull, boolean trimValues)
/*     */   {
/* 118 */     this.separator = separator;
/* 119 */     this.charsToDelete = charsToDelete;
/* 120 */     this.emptyArrayAsNull = emptyArrayAsNull;
/* 121 */     this.trimValues = trimValues;
/*     */   }
/*     */ 
/*     */   public void setAsText(String text) throws IllegalArgumentException
/*     */   {
/* 126 */     String[] array = StringUtils.delimitedListToStringArray(text, this.separator, this.charsToDelete);
/* 127 */     if (this.trimValues) {
/* 128 */       array = StringUtils.trimArrayElements(array);
/*     */     }
/* 130 */     if ((this.emptyArrayAsNull) && (array.length == 0)) {
/* 131 */       setValue(null);
/*     */     }
/*     */     else
/* 134 */       setValue(array);
/*     */   }
/*     */ 
/*     */   public String getAsText()
/*     */   {
/* 140 */     return StringUtils.arrayToDelimitedString(ObjectUtils.toObjectArray(getValue()), this.separator);
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.StringArrayPropertyEditor
 * JD-Core Version:    0.6.2
 */