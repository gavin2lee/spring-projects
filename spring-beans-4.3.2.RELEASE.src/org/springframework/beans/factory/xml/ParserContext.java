/*     */ package org.springframework.beans.factory.xml;
/*     */ 
/*     */ import java.util.Stack;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*     */ import org.springframework.beans.factory.parsing.ComponentDefinition;
/*     */ import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ 
/*     */ public final class ParserContext
/*     */ {
/*     */   private final XmlReaderContext readerContext;
/*     */   private final BeanDefinitionParserDelegate delegate;
/*     */   private BeanDefinition containingBeanDefinition;
/*  47 */   private final Stack<ComponentDefinition> containingComponents = new Stack();
/*     */ 
/*     */   public ParserContext(XmlReaderContext readerContext, BeanDefinitionParserDelegate delegate)
/*     */   {
/*  51 */     this.readerContext = readerContext;
/*  52 */     this.delegate = delegate;
/*     */   }
/*     */ 
/*     */   public ParserContext(XmlReaderContext readerContext, BeanDefinitionParserDelegate delegate, BeanDefinition containingBeanDefinition)
/*     */   {
/*  58 */     this.readerContext = readerContext;
/*  59 */     this.delegate = delegate;
/*  60 */     this.containingBeanDefinition = containingBeanDefinition;
/*     */   }
/*     */ 
/*     */   public final XmlReaderContext getReaderContext()
/*     */   {
/*  65 */     return this.readerContext;
/*     */   }
/*     */ 
/*     */   public final BeanDefinitionRegistry getRegistry() {
/*  69 */     return this.readerContext.getRegistry();
/*     */   }
/*     */ 
/*     */   public final BeanDefinitionParserDelegate getDelegate() {
/*  73 */     return this.delegate;
/*     */   }
/*     */ 
/*     */   public final BeanDefinition getContainingBeanDefinition() {
/*  77 */     return this.containingBeanDefinition;
/*     */   }
/*     */ 
/*     */   public final boolean isNested() {
/*  81 */     return this.containingBeanDefinition != null;
/*     */   }
/*     */ 
/*     */   public boolean isDefaultLazyInit() {
/*  85 */     return "true".equals(this.delegate.getDefaults().getLazyInit());
/*     */   }
/*     */ 
/*     */   public Object extractSource(Object sourceCandidate) {
/*  89 */     return this.readerContext.extractSource(sourceCandidate);
/*     */   }
/*     */ 
/*     */   public CompositeComponentDefinition getContainingComponent()
/*     */   {
/*  94 */     return !this.containingComponents.isEmpty() ? 
/*  94 */       (CompositeComponentDefinition)this.containingComponents
/*  94 */       .lastElement() : null;
/*     */   }
/*     */ 
/*     */   public void pushContainingComponent(CompositeComponentDefinition containingComponent) {
/*  98 */     this.containingComponents.push(containingComponent);
/*     */   }
/*     */ 
/*     */   public CompositeComponentDefinition popContainingComponent() {
/* 102 */     return (CompositeComponentDefinition)this.containingComponents.pop();
/*     */   }
/*     */ 
/*     */   public void popAndRegisterContainingComponent() {
/* 106 */     registerComponent(popContainingComponent());
/*     */   }
/*     */ 
/*     */   public void registerComponent(ComponentDefinition component) {
/* 110 */     CompositeComponentDefinition containingComponent = getContainingComponent();
/* 111 */     if (containingComponent != null) {
/* 112 */       containingComponent.addNestedComponent(component);
/*     */     }
/*     */     else
/* 115 */       this.readerContext.fireComponentRegistered(component);
/*     */   }
/*     */ 
/*     */   public void registerBeanComponent(BeanComponentDefinition component)
/*     */   {
/* 120 */     BeanDefinitionReaderUtils.registerBeanDefinition(component, getRegistry());
/* 121 */     registerComponent(component);
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.xml.ParserContext
 * JD-Core Version:    0.6.2
 */