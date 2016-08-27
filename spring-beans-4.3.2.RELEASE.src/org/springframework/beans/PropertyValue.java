/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ public class PropertyValue extends BeanMetadataAttributeAccessor
/*     */   implements Serializable
/*     */ {
/*     */   private final String name;
/*     */   private final Object value;
/*     */   private Object source;
/*  50 */   private boolean optional = false;
/*     */ 
/*  52 */   private boolean converted = false;
/*     */   private Object convertedValue;
/*     */   volatile Boolean conversionNecessary;
/*     */   volatile transient Object resolvedTokens;
/*     */ 
/*     */   public PropertyValue(String name, Object value)
/*     */   {
/*  69 */     this.name = name;
/*  70 */     this.value = value;
/*     */   }
/*     */ 
/*     */   public PropertyValue(PropertyValue original)
/*     */   {
/*  78 */     Assert.notNull(original, "Original must not be null");
/*  79 */     this.name = original.getName();
/*  80 */     this.value = original.getValue();
/*  81 */     this.source = original.getSource();
/*  82 */     this.optional = original.isOptional();
/*  83 */     this.converted = original.converted;
/*  84 */     this.convertedValue = original.convertedValue;
/*  85 */     this.conversionNecessary = original.conversionNecessary;
/*  86 */     this.resolvedTokens = original.resolvedTokens;
/*  87 */     copyAttributesFrom(original);
/*     */   }
/*     */ 
/*     */   public PropertyValue(PropertyValue original, Object newValue)
/*     */   {
/*  97 */     Assert.notNull(original, "Original must not be null");
/*  98 */     this.name = original.getName();
/*  99 */     this.value = newValue;
/* 100 */     this.source = original;
/* 101 */     this.optional = original.isOptional();
/* 102 */     this.conversionNecessary = original.conversionNecessary;
/* 103 */     this.resolvedTokens = original.resolvedTokens;
/* 104 */     copyAttributesFrom(original);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 112 */     return this.name;
/*     */   }
/*     */ 
/*     */   public Object getValue()
/*     */   {
/* 122 */     return this.value;
/*     */   }
/*     */ 
/*     */   public PropertyValue getOriginalPropertyValue()
/*     */   {
/* 131 */     PropertyValue original = this;
/* 132 */     while (((original.source instanceof PropertyValue)) && (original.source != original)) {
/* 133 */       original = (PropertyValue)original.source;
/*     */     }
/* 135 */     return original;
/*     */   }
/*     */ 
/*     */   public void setOptional(boolean optional) {
/* 139 */     this.optional = optional;
/*     */   }
/*     */ 
/*     */   public boolean isOptional() {
/* 143 */     return this.optional;
/*     */   }
/*     */ 
/*     */   public synchronized boolean isConverted()
/*     */   {
/* 151 */     return this.converted;
/*     */   }
/*     */ 
/*     */   public synchronized void setConvertedValue(Object value)
/*     */   {
/* 159 */     this.converted = true;
/* 160 */     this.convertedValue = value;
/*     */   }
/*     */ 
/*     */   public synchronized Object getConvertedValue()
/*     */   {
/* 168 */     return this.convertedValue;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/* 174 */     if (this == other) {
/* 175 */       return true;
/*     */     }
/* 177 */     if (!(other instanceof PropertyValue)) {
/* 178 */       return false;
/*     */     }
/* 180 */     PropertyValue otherPv = (PropertyValue)other;
/*     */ 
/* 183 */     return (this.name.equals(otherPv.name)) && 
/* 182 */       (ObjectUtils.nullSafeEquals(this.value, otherPv.value)) && 
/* 183 */       (ObjectUtils.nullSafeEquals(this.source, otherPv.source));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 188 */     return this.name.hashCode() * 29 + ObjectUtils.nullSafeHashCode(this.value);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 193 */     return "bean property '" + this.name + "'";
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.PropertyValue
 * JD-Core Version:    0.6.2
 */