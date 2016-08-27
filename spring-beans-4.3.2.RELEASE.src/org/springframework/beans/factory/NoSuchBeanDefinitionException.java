/*     */ package org.springframework.beans.factory;
/*     */ 
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ public class NoSuchBeanDefinitionException extends BeansException
/*     */ {
/*     */   private String beanName;
/*     */   private Class<?> beanType;
/*     */ 
/*     */   public NoSuchBeanDefinitionException(String name)
/*     */   {
/*  48 */     super(new StringBuilder().append("No bean named '").append(name).append("' is defined").toString());
/*  49 */     this.beanName = name;
/*     */   }
/*     */ 
/*     */   public NoSuchBeanDefinitionException(String name, String message)
/*     */   {
/*  58 */     super(new StringBuilder().append("No bean named '").append(name).append("' is defined: ").append(message).toString());
/*  59 */     this.beanName = name;
/*     */   }
/*     */ 
/*     */   public NoSuchBeanDefinitionException(Class<?> type)
/*     */   {
/*  67 */     super(new StringBuilder().append("No qualifying bean of type [").append(type.getName()).append("] is defined").toString());
/*  68 */     this.beanType = type;
/*     */   }
/*     */ 
/*     */   public NoSuchBeanDefinitionException(Class<?> type, String message)
/*     */   {
/*  77 */     super(new StringBuilder().append("No qualifying bean of type [").append(type.getName()).append("] is defined: ").append(message).toString());
/*  78 */     this.beanType = type;
/*     */   }
/*     */ 
/*     */   public NoSuchBeanDefinitionException(Class<?> type, String dependencyDescription, String message)
/*     */   {
/*  88 */     super(new StringBuilder().append("No qualifying bean of type [").append(type.getName()).append("] found for dependency")
/*  89 */       .append(StringUtils.hasLength(dependencyDescription) ? 
/*  89 */       new StringBuilder().append(" [").append(dependencyDescription).append("]").toString() : "").append(": ").append(message).toString());
/*     */ 
/*  91 */     this.beanType = type;
/*     */   }
/*     */ 
/*     */   public String getBeanName()
/*     */   {
/*  99 */     return this.beanName;
/*     */   }
/*     */ 
/*     */   public Class<?> getBeanType()
/*     */   {
/* 106 */     return this.beanType;
/*     */   }
/*     */ 
/*     */   public int getNumberOfBeansFound()
/*     */   {
/* 115 */     return 0;
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.NoSuchBeanDefinitionException
 * JD-Core Version:    0.6.2
 */