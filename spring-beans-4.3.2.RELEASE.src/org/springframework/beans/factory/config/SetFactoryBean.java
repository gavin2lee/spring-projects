/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.TypeConverter;
/*     */ import org.springframework.core.GenericCollectionTypeResolver;
/*     */ 
/*     */ public class SetFactoryBean extends AbstractFactoryBean<Set<Object>>
/*     */ {
/*     */   private Set<?> sourceSet;
/*     */   private Class<? extends Set> targetSetClass;
/*     */ 
/*     */   public void setSourceSet(Set<?> sourceSet)
/*     */   {
/*  47 */     this.sourceSet = sourceSet;
/*     */   }
/*     */ 
/*     */   public void setTargetSetClass(Class<? extends Set> targetSetClass)
/*     */   {
/*  58 */     if (targetSetClass == null) {
/*  59 */       throw new IllegalArgumentException("'targetSetClass' must not be null");
/*     */     }
/*  61 */     if (!Set.class.isAssignableFrom(targetSetClass)) {
/*  62 */       throw new IllegalArgumentException("'targetSetClass' must implement [java.util.Set]");
/*     */     }
/*  64 */     this.targetSetClass = targetSetClass;
/*     */   }
/*     */ 
/*     */   public Class<Set> getObjectType()
/*     */   {
/*  71 */     return Set.class;
/*     */   }
/*     */ 
/*     */   protected Set<Object> createInstance()
/*     */   {
/*  77 */     if (this.sourceSet == null) {
/*  78 */       throw new IllegalArgumentException("'sourceSet' is required");
/*     */     }
/*  80 */     Set result = null;
/*  81 */     if (this.targetSetClass != null) {
/*  82 */       result = (Set)BeanUtils.instantiateClass(this.targetSetClass);
/*     */     }
/*     */     else {
/*  85 */       result = new LinkedHashSet(this.sourceSet.size());
/*     */     }
/*  87 */     Class valueType = null;
/*  88 */     if (this.targetSetClass != null)
/*  89 */       valueType = GenericCollectionTypeResolver.getCollectionType(this.targetSetClass);
/*     */     TypeConverter converter;
/*     */     Iterator localIterator;
/*  91 */     if (valueType != null) {
/*  92 */       converter = getBeanTypeConverter();
/*  93 */       for (localIterator = this.sourceSet.iterator(); localIterator.hasNext(); ) { Object elem = localIterator.next();
/*  94 */         result.add(converter.convertIfNecessary(elem, valueType)); }
/*     */     }
/*     */     else
/*     */     {
/*  98 */       result.addAll(this.sourceSet);
/*     */     }
/* 100 */     return result;
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.config.SetFactoryBean
 * JD-Core Version:    0.6.2
 */