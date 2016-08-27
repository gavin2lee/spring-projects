/*    */ package org.springframework.beans.factory.support;
/*    */ 
/*    */ import java.util.Properties;
/*    */ import org.springframework.beans.BeanMetadataElement;
/*    */ import org.springframework.beans.Mergeable;
/*    */ 
/*    */ public class ManagedProperties extends Properties
/*    */   implements Mergeable, BeanMetadataElement
/*    */ {
/*    */   private Object source;
/*    */   private boolean mergeEnabled;
/*    */ 
/*    */   public void setSource(Object source)
/*    */   {
/* 45 */     this.source = source;
/*    */   }
/*    */ 
/*    */   public Object getSource()
/*    */   {
/* 50 */     return this.source;
/*    */   }
/*    */ 
/*    */   public void setMergeEnabled(boolean mergeEnabled)
/*    */   {
/* 58 */     this.mergeEnabled = mergeEnabled;
/*    */   }
/*    */ 
/*    */   public boolean isMergeEnabled()
/*    */   {
/* 63 */     return this.mergeEnabled;
/*    */   }
/*    */ 
/*    */   public Object merge(Object parent)
/*    */   {
/* 69 */     if (!this.mergeEnabled) {
/* 70 */       throw new IllegalStateException("Not allowed to merge when the 'mergeEnabled' property is set to 'false'");
/*    */     }
/* 72 */     if (parent == null) {
/* 73 */       return this;
/*    */     }
/* 75 */     if (!(parent instanceof Properties)) {
/* 76 */       throw new IllegalArgumentException("Cannot merge with object of type [" + parent.getClass() + "]");
/*    */     }
/* 78 */     Properties merged = new ManagedProperties();
/* 79 */     merged.putAll((Properties)parent);
/* 80 */     merged.putAll(this);
/* 81 */     return merged;
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.support.ManagedProperties
 * JD-Core Version:    0.6.2
 */