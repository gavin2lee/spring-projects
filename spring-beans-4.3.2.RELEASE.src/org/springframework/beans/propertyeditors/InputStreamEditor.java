/*    */ package org.springframework.beans.propertyeditors;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ import java.io.IOException;
/*    */ import org.springframework.core.io.Resource;
/*    */ import org.springframework.core.io.ResourceEditor;
/*    */ import org.springframework.util.Assert;
/*    */ 
/*    */ public class InputStreamEditor extends PropertyEditorSupport
/*    */ {
/*    */   private final ResourceEditor resourceEditor;
/*    */ 
/*    */   public InputStreamEditor()
/*    */   {
/* 53 */     this.resourceEditor = new ResourceEditor();
/*    */   }
/*    */ 
/*    */   public InputStreamEditor(ResourceEditor resourceEditor)
/*    */   {
/* 61 */     Assert.notNull(resourceEditor, "ResourceEditor must not be null");
/* 62 */     this.resourceEditor = resourceEditor;
/*    */   }
/*    */ 
/*    */   public void setAsText(String text)
/*    */     throws IllegalArgumentException
/*    */   {
/* 68 */     this.resourceEditor.setAsText(text);
/* 69 */     Resource resource = (Resource)this.resourceEditor.getValue();
/*    */     try {
/* 71 */       setValue(resource != null ? resource.getInputStream() : null);
/*    */     }
/*    */     catch (IOException ex) {
/* 74 */       throw new IllegalArgumentException("Failed to retrieve InputStream for " + resource, ex);
/*    */     }
/*    */   }
/*    */ 
/*    */   public String getAsText()
/*    */   {
/* 84 */     return null;
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.InputStreamEditor
 * JD-Core Version:    0.6.2
 */