/*     */ package org.springframework.beans.factory.parsing;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.beans.PropertyValue;
/*     */ import org.springframework.beans.PropertyValues;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.config.BeanReference;
/*     */ 
/*     */ public class BeanComponentDefinition extends BeanDefinitionHolder
/*     */   implements ComponentDefinition
/*     */ {
/*     */   private BeanDefinition[] innerBeanDefinitions;
/*     */   private BeanReference[] beanReferences;
/*     */ 
/*     */   public BeanComponentDefinition(BeanDefinition beanDefinition, String beanName)
/*     */   {
/*  49 */     super(beanDefinition, beanName);
/*  50 */     findInnerBeanDefinitionsAndBeanReferences(beanDefinition);
/*     */   }
/*     */ 
/*     */   public BeanComponentDefinition(BeanDefinition beanDefinition, String beanName, String[] aliases)
/*     */   {
/*  60 */     super(beanDefinition, beanName, aliases);
/*  61 */     findInnerBeanDefinitionsAndBeanReferences(beanDefinition);
/*     */   }
/*     */ 
/*     */   public BeanComponentDefinition(BeanDefinitionHolder holder)
/*     */   {
/*  70 */     super(holder);
/*  71 */     findInnerBeanDefinitionsAndBeanReferences(holder.getBeanDefinition());
/*     */   }
/*     */ 
/*     */   private void findInnerBeanDefinitionsAndBeanReferences(BeanDefinition beanDefinition)
/*     */   {
/*  76 */     List innerBeans = new ArrayList();
/*  77 */     List references = new ArrayList();
/*  78 */     PropertyValues propertyValues = beanDefinition.getPropertyValues();
/*  79 */     for (int i = 0; i < propertyValues.getPropertyValues().length; i++) {
/*  80 */       PropertyValue propertyValue = propertyValues.getPropertyValues()[i];
/*  81 */       Object value = propertyValue.getValue();
/*  82 */       if ((value instanceof BeanDefinitionHolder)) {
/*  83 */         innerBeans.add(((BeanDefinitionHolder)value).getBeanDefinition());
/*     */       }
/*  85 */       else if ((value instanceof BeanDefinition)) {
/*  86 */         innerBeans.add((BeanDefinition)value);
/*     */       }
/*  88 */       else if ((value instanceof BeanReference)) {
/*  89 */         references.add((BeanReference)value);
/*     */       }
/*     */     }
/*  92 */     this.innerBeanDefinitions = ((BeanDefinition[])innerBeans.toArray(new BeanDefinition[innerBeans.size()]));
/*  93 */     this.beanReferences = ((BeanReference[])references.toArray(new BeanReference[references.size()]));
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  99 */     return getBeanName();
/*     */   }
/*     */ 
/*     */   public String getDescription()
/*     */   {
/* 104 */     return getShortDescription();
/*     */   }
/*     */ 
/*     */   public BeanDefinition[] getBeanDefinitions()
/*     */   {
/* 109 */     return new BeanDefinition[] { getBeanDefinition() };
/*     */   }
/*     */ 
/*     */   public BeanDefinition[] getInnerBeanDefinitions()
/*     */   {
/* 114 */     return this.innerBeanDefinitions;
/*     */   }
/*     */ 
/*     */   public BeanReference[] getBeanReferences()
/*     */   {
/* 119 */     return this.beanReferences;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 129 */     return getDescription();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/* 138 */     return (this == other) || (((other instanceof BeanComponentDefinition)) && (super.equals(other)));
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.BeanComponentDefinition
 * JD-Core Version:    0.6.2
 */