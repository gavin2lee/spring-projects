/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.PropertyEditorRegistrar;
/*     */ import org.springframework.core.Ordered;
/*     */ 
/*     */ public class CustomEditorConfigurer
/*     */   implements BeanFactoryPostProcessor, Ordered
/*     */ {
/*  97 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*  99 */   private int order = 2147483647;
/*     */   private PropertyEditorRegistrar[] propertyEditorRegistrars;
/*     */   private Map<Class<?>, Class<? extends PropertyEditor>> customEditors;
/*     */ 
/*     */   public void setOrder(int order)
/*     */   {
/* 107 */     this.order = order;
/*     */   }
/*     */ 
/*     */   public int getOrder()
/*     */   {
/* 112 */     return this.order;
/*     */   }
/*     */ 
/*     */   public void setPropertyEditorRegistrars(PropertyEditorRegistrar[] propertyEditorRegistrars)
/*     */   {
/* 126 */     this.propertyEditorRegistrars = propertyEditorRegistrars;
/*     */   }
/*     */ 
/*     */   public void setCustomEditors(Map<Class<?>, Class<? extends PropertyEditor>> customEditors)
/*     */   {
/* 136 */     this.customEditors = customEditors;
/*     */   }
/*     */ 
/*     */   public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
/*     */     throws BeansException
/*     */   {
/* 142 */     if (this.propertyEditorRegistrars != null) {
/* 143 */       for (PropertyEditorRegistrar propertyEditorRegistrar : this.propertyEditorRegistrars) {
/* 144 */         beanFactory.addPropertyEditorRegistrar(propertyEditorRegistrar);
/*     */       }
/*     */     }
/* 147 */     if (this.customEditors != null)
/* 148 */       for (??? = this.customEditors.entrySet().iterator(); ((Iterator)???).hasNext(); ) { Map.Entry entry = (Map.Entry)((Iterator)???).next();
/* 149 */         Class requiredType = (Class)entry.getKey();
/* 150 */         Class propertyEditorClass = (Class)entry.getValue();
/* 151 */         beanFactory.registerCustomEditor(requiredType, propertyEditorClass);
/*     */       }
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.config.CustomEditorConfigurer
 * JD-Core Version:    0.6.2
 */