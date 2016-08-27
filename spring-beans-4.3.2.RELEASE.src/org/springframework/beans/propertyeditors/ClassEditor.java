/*    */ package org.springframework.beans.propertyeditors;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ import org.springframework.util.ClassUtils;
/*    */ import org.springframework.util.StringUtils;
/*    */ 
/*    */ public class ClassEditor extends PropertyEditorSupport
/*    */ {
/*    */   private final ClassLoader classLoader;
/*    */ 
/*    */   public ClassEditor()
/*    */   {
/* 47 */     this(null);
/*    */   }
/*    */ 
/*    */   public ClassEditor(ClassLoader classLoader)
/*    */   {
/* 56 */     this.classLoader = (classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader());
/*    */   }
/*    */ 
/*    */   public void setAsText(String text)
/*    */     throws IllegalArgumentException
/*    */   {
/* 62 */     if (StringUtils.hasText(text)) {
/* 63 */       setValue(ClassUtils.resolveClassName(text.trim(), this.classLoader));
/*    */     }
/*    */     else
/* 66 */       setValue(null);
/*    */   }
/*    */ 
/*    */   public String getAsText()
/*    */   {
/* 72 */     Class clazz = (Class)getValue();
/* 73 */     if (clazz != null) {
/* 74 */       return ClassUtils.getQualifiedName(clazz);
/*    */     }
/*    */ 
/* 77 */     return "";
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.ClassEditor
 * JD-Core Version:    0.6.2
 */