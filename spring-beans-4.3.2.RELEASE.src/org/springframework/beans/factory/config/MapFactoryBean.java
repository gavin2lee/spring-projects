/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.TypeConverter;
/*     */ import org.springframework.core.GenericCollectionTypeResolver;
/*     */ 
/*     */ public class MapFactoryBean extends AbstractFactoryBean<Map<Object, Object>>
/*     */ {
/*     */   private Map<?, ?> sourceMap;
/*     */   private Class<? extends Map> targetMapClass;
/*     */ 
/*     */   public void setSourceMap(Map<?, ?> sourceMap)
/*     */   {
/*  47 */     this.sourceMap = sourceMap;
/*     */   }
/*     */ 
/*     */   public void setTargetMapClass(Class<? extends Map> targetMapClass)
/*     */   {
/*  58 */     if (targetMapClass == null) {
/*  59 */       throw new IllegalArgumentException("'targetMapClass' must not be null");
/*     */     }
/*  61 */     if (!Map.class.isAssignableFrom(targetMapClass)) {
/*  62 */       throw new IllegalArgumentException("'targetMapClass' must implement [java.util.Map]");
/*     */     }
/*  64 */     this.targetMapClass = targetMapClass;
/*     */   }
/*     */ 
/*     */   public Class<Map> getObjectType()
/*     */   {
/*  71 */     return Map.class;
/*     */   }
/*     */ 
/*     */   protected Map<Object, Object> createInstance()
/*     */   {
/*  77 */     if (this.sourceMap == null) {
/*  78 */       throw new IllegalArgumentException("'sourceMap' is required");
/*     */     }
/*  80 */     Map result = null;
/*  81 */     if (this.targetMapClass != null) {
/*  82 */       result = (Map)BeanUtils.instantiateClass(this.targetMapClass);
/*     */     }
/*     */     else {
/*  85 */       result = new LinkedHashMap(this.sourceMap.size());
/*     */     }
/*  87 */     Class keyType = null;
/*  88 */     Class valueType = null;
/*  89 */     if (this.targetMapClass != null) {
/*  90 */       keyType = GenericCollectionTypeResolver.getMapKeyType(this.targetMapClass);
/*  91 */       valueType = GenericCollectionTypeResolver.getMapValueType(this.targetMapClass);
/*     */     }
/*     */     TypeConverter converter;
/*  93 */     if ((keyType != null) || (valueType != null)) {
/*  94 */       converter = getBeanTypeConverter();
/*  95 */       for (Map.Entry entry : this.sourceMap.entrySet()) {
/*  96 */         Object convertedKey = converter.convertIfNecessary(entry.getKey(), keyType);
/*  97 */         Object convertedValue = converter.convertIfNecessary(entry.getValue(), valueType);
/*  98 */         result.put(convertedKey, convertedValue);
/*     */       }
/*     */     }
/*     */     else {
/* 102 */       result.putAll(this.sourceMap);
/*     */     }
/* 104 */     return result;
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.config.MapFactoryBean
 * JD-Core Version:    0.6.2
 */