/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.TypeConverter;
/*     */ import org.springframework.core.GenericCollectionTypeResolver;
/*     */ 
/*     */ public class ListFactoryBean extends AbstractFactoryBean<List<Object>>
/*     */ {
/*     */   private List<?> sourceList;
/*     */   private Class<? extends List> targetListClass;
/*     */ 
/*     */   public void setSourceList(List<?> sourceList)
/*     */   {
/*  47 */     this.sourceList = sourceList;
/*     */   }
/*     */ 
/*     */   public void setTargetListClass(Class<? extends List> targetListClass)
/*     */   {
/*  58 */     if (targetListClass == null) {
/*  59 */       throw new IllegalArgumentException("'targetListClass' must not be null");
/*     */     }
/*  61 */     if (!List.class.isAssignableFrom(targetListClass)) {
/*  62 */       throw new IllegalArgumentException("'targetListClass' must implement [java.util.List]");
/*     */     }
/*  64 */     this.targetListClass = targetListClass;
/*     */   }
/*     */ 
/*     */   public Class<List> getObjectType()
/*     */   {
/*  71 */     return List.class;
/*     */   }
/*     */ 
/*     */   protected List<Object> createInstance()
/*     */   {
/*  77 */     if (this.sourceList == null) {
/*  78 */       throw new IllegalArgumentException("'sourceList' is required");
/*     */     }
/*  80 */     List result = null;
/*  81 */     if (this.targetListClass != null) {
/*  82 */       result = (List)BeanUtils.instantiateClass(this.targetListClass);
/*     */     }
/*     */     else {
/*  85 */       result = new ArrayList(this.sourceList.size());
/*     */     }
/*  87 */     Class valueType = null;
/*  88 */     if (this.targetListClass != null)
/*  89 */       valueType = GenericCollectionTypeResolver.getCollectionType(this.targetListClass);
/*     */     TypeConverter converter;
/*     */     Iterator localIterator;
/*  91 */     if (valueType != null) {
/*  92 */       converter = getBeanTypeConverter();
/*  93 */       for (localIterator = this.sourceList.iterator(); localIterator.hasNext(); ) { Object elem = localIterator.next();
/*  94 */         result.add(converter.convertIfNecessary(elem, valueType)); }
/*     */     }
/*     */     else
/*     */     {
/*  98 */       result.addAll(this.sourceList);
/*     */     }
/* 100 */     return result;
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.config.ListFactoryBean
 * JD-Core Version:    0.6.2
 */