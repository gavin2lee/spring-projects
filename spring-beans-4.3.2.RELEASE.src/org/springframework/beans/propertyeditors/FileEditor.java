/*     */ package org.springframework.beans.propertyeditors;
/*     */ 
/*     */ import java.beans.PropertyEditorSupport;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceEditor;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ResourceUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ public class FileEditor extends PropertyEditorSupport
/*     */ {
/*     */   private final ResourceEditor resourceEditor;
/*     */ 
/*     */   public FileEditor()
/*     */   {
/*  65 */     this.resourceEditor = new ResourceEditor();
/*     */   }
/*     */ 
/*     */   public FileEditor(ResourceEditor resourceEditor)
/*     */   {
/*  73 */     Assert.notNull(resourceEditor, "ResourceEditor must not be null");
/*  74 */     this.resourceEditor = resourceEditor;
/*     */   }
/*     */ 
/*     */   public void setAsText(String text)
/*     */     throws IllegalArgumentException
/*     */   {
/*  80 */     if (!StringUtils.hasText(text)) {
/*  81 */       setValue(null);
/*  82 */       return;
/*     */     }
/*     */ 
/*  87 */     if (!ResourceUtils.isUrl(text)) {
/*  88 */       File file = new File(text);
/*  89 */       if (file.isAbsolute()) {
/*  90 */         setValue(file);
/*  91 */         return;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  96 */     this.resourceEditor.setAsText(text);
/*  97 */     Resource resource = (Resource)this.resourceEditor.getValue();
/*     */ 
/* 100 */     if ((ResourceUtils.isUrl(text)) || (resource.exists())) {
/*     */       try {
/* 102 */         setValue(resource.getFile());
/*     */       }
/*     */       catch (IOException ex)
/*     */       {
/* 106 */         throw new IllegalArgumentException("Could not retrieve file for " + resource + ": " + ex
/* 106 */           .getMessage());
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 111 */       setValue(new File(text));
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getAsText()
/*     */   {
/* 117 */     File value = (File)getValue();
/* 118 */     return value != null ? value.getPath() : "";
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.FileEditor
 * JD-Core Version:    0.6.2
 */