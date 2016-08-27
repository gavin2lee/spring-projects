/*    */ package org.springframework.beans.factory.parsing;
/*    */ 
/*    */ import org.springframework.beans.BeanMetadataElement;
/*    */ import org.springframework.core.io.Resource;
/*    */ import org.springframework.util.Assert;
/*    */ 
/*    */ public class ImportDefinition
/*    */   implements BeanMetadataElement
/*    */ {
/*    */   private final String importedResource;
/*    */   private final Resource[] actualResources;
/*    */   private final Object source;
/*    */ 
/*    */   public ImportDefinition(String importedResource)
/*    */   {
/* 44 */     this(importedResource, null, null);
/*    */   }
/*    */ 
/*    */   public ImportDefinition(String importedResource, Object source)
/*    */   {
/* 53 */     this(importedResource, null, source);
/*    */   }
/*    */ 
/*    */   public ImportDefinition(String importedResource, Resource[] actualResources, Object source)
/*    */   {
/* 62 */     Assert.notNull(importedResource, "Imported resource must not be null");
/* 63 */     this.importedResource = importedResource;
/* 64 */     this.actualResources = actualResources;
/* 65 */     this.source = source;
/*    */   }
/*    */ 
/*    */   public final String getImportedResource()
/*    */   {
/* 73 */     return this.importedResource;
/*    */   }
/*    */ 
/*    */   public final Resource[] getActualResources() {
/* 77 */     return this.actualResources;
/*    */   }
/*    */ 
/*    */   public final Object getSource()
/*    */   {
/* 82 */     return this.source;
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.ImportDefinition
 * JD-Core Version:    0.6.2
 */