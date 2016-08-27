/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.BeanMetadataElement;
/*     */ import org.springframework.beans.Mergeable;
/*     */ 
/*     */ public class ManagedSet<E> extends LinkedHashSet<E>
/*     */   implements Mergeable, BeanMetadataElement
/*     */ {
/*     */   private Object source;
/*     */   private String elementTypeName;
/*     */   private boolean mergeEnabled;
/*     */ 
/*     */   public ManagedSet()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ManagedSet(int initialCapacity)
/*     */   {
/*  47 */     super(initialCapacity);
/*     */   }
/*     */ 
/*     */   public void setSource(Object source)
/*     */   {
/*  56 */     this.source = source;
/*     */   }
/*     */ 
/*     */   public Object getSource()
/*     */   {
/*  61 */     return this.source;
/*     */   }
/*     */ 
/*     */   public void setElementTypeName(String elementTypeName)
/*     */   {
/*  68 */     this.elementTypeName = elementTypeName;
/*     */   }
/*     */ 
/*     */   public String getElementTypeName()
/*     */   {
/*  75 */     return this.elementTypeName;
/*     */   }
/*     */ 
/*     */   public void setMergeEnabled(boolean mergeEnabled)
/*     */   {
/*  83 */     this.mergeEnabled = mergeEnabled;
/*     */   }
/*     */ 
/*     */   public boolean isMergeEnabled()
/*     */   {
/*  88 */     return this.mergeEnabled;
/*     */   }
/*     */ 
/*     */   public Set<E> merge(Object parent)
/*     */   {
/*  94 */     if (!this.mergeEnabled) {
/*  95 */       throw new IllegalStateException("Not allowed to merge when the 'mergeEnabled' property is set to 'false'");
/*     */     }
/*  97 */     if (parent == null) {
/*  98 */       return this;
/*     */     }
/* 100 */     if (!(parent instanceof Set)) {
/* 101 */       throw new IllegalArgumentException("Cannot merge with object of type [" + parent.getClass() + "]");
/*     */     }
/* 103 */     Set merged = new ManagedSet();
/* 104 */     merged.addAll((Set)parent);
/* 105 */     merged.addAll(this);
/* 106 */     return merged;
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.support.ManagedSet
 * JD-Core Version:    0.6.2
 */