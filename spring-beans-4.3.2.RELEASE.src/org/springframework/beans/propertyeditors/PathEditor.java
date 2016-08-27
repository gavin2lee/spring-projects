/*     */ package org.springframework.beans.propertyeditors;
/*     */ 
/*     */ import java.beans.PropertyEditorSupport;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.nio.file.FileSystemNotFoundException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceEditor;
/*     */ import org.springframework.lang.UsesJava7;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ @UsesJava7
/*     */ public class PathEditor extends PropertyEditorSupport
/*     */ {
/*     */   private final ResourceEditor resourceEditor;
/*     */ 
/*     */   public PathEditor()
/*     */   {
/*  67 */     this.resourceEditor = new ResourceEditor();
/*     */   }
/*     */ 
/*     */   public PathEditor(ResourceEditor resourceEditor)
/*     */   {
/*  75 */     Assert.notNull(resourceEditor, "ResourceEditor must not be null");
/*  76 */     this.resourceEditor = resourceEditor;
/*     */   }
/*     */ 
/*     */   public void setAsText(String text)
/*     */     throws IllegalArgumentException
/*     */   {
/*  82 */     if ((!text.startsWith("/")) && (!text.startsWith("classpath:"))) {
/*     */       try {
/*  84 */         URI uri = new URI(text);
/*  85 */         if (uri.getScheme() != null)
/*     */         {
/*  87 */           setValue(Paths.get(uri).normalize());
/*  88 */           return;
/*     */         }
/*     */       }
/*     */       catch (URISyntaxException localURISyntaxException)
/*     */       {
/*     */       }
/*     */       catch (FileSystemNotFoundException localFileSystemNotFoundException)
/*     */       {
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 100 */     this.resourceEditor.setAsText(text);
/* 101 */     Resource resource = (Resource)this.resourceEditor.getValue();
/*     */     try {
/* 103 */       setValue(resource != null ? resource.getFile().toPath() : null);
/*     */     }
/*     */     catch (IOException ex) {
/* 106 */       throw new IllegalArgumentException("Failed to retrieve file for " + resource, ex);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getAsText()
/*     */   {
/* 112 */     Path value = (Path)getValue();
/* 113 */     return value != null ? value.toString() : "";
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.PathEditor
 * JD-Core Version:    0.6.2
 */