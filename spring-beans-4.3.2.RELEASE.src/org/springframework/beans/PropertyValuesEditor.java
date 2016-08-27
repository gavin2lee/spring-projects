/*    */ package org.springframework.beans;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ import java.util.Properties;
/*    */ import org.springframework.beans.propertyeditors.PropertiesEditor;
/*    */ 
/*    */ public class PropertyValuesEditor extends PropertyEditorSupport
/*    */ {
/* 39 */   private final PropertiesEditor propertiesEditor = new PropertiesEditor();
/*    */ 
/*    */   public void setAsText(String text) throws IllegalArgumentException
/*    */   {
/* 43 */     this.propertiesEditor.setAsText(text);
/* 44 */     Properties props = (Properties)this.propertiesEditor.getValue();
/* 45 */     setValue(new MutablePropertyValues(props));
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.PropertyValuesEditor
 * JD-Core Version:    0.6.2
 */