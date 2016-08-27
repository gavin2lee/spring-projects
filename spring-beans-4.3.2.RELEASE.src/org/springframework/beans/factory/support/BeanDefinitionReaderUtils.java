/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ public class BeanDefinitionReaderUtils
/*     */ {
/*     */   public static final String GENERATED_BEAN_NAME_SEPARATOR = "#";
/*     */ 
/*     */   public static AbstractBeanDefinition createBeanDefinition(String parentName, String className, ClassLoader classLoader)
/*     */     throws ClassNotFoundException
/*     */   {
/*  59 */     GenericBeanDefinition bd = new GenericBeanDefinition();
/*  60 */     bd.setParentName(parentName);
/*  61 */     if (className != null) {
/*  62 */       if (classLoader != null) {
/*  63 */         bd.setBeanClass(ClassUtils.forName(className, classLoader));
/*     */       }
/*     */       else {
/*  66 */         bd.setBeanClassName(className);
/*     */       }
/*     */     }
/*  69 */     return bd;
/*     */   }
/*     */ 
/*     */   public static String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry, boolean isInnerBean)
/*     */     throws BeanDefinitionStoreException
/*     */   {
/*  89 */     String generatedBeanName = definition.getBeanClassName();
/*  90 */     if (generatedBeanName == null) {
/*  91 */       if (definition.getParentName() != null) {
/*  92 */         generatedBeanName = definition.getParentName() + "$child";
/*     */       }
/*  94 */       else if (definition.getFactoryBeanName() != null) {
/*  95 */         generatedBeanName = definition.getFactoryBeanName() + "$created";
/*     */       }
/*     */     }
/*  98 */     if (!StringUtils.hasText(generatedBeanName)) {
/*  99 */       throw new BeanDefinitionStoreException("Unnamed bean definition specifies neither 'class' nor 'parent' nor 'factory-bean' - can't generate bean name");
/*     */     }
/*     */ 
/* 103 */     String id = generatedBeanName;
/* 104 */     if (isInnerBean)
/*     */     {
/* 106 */       id = generatedBeanName + "#" + ObjectUtils.getIdentityHexString(definition);
/*     */     }
/*     */     else
/*     */     {
/* 111 */       int counter = -1;
/* 112 */       while ((counter == -1) || (registry.containsBeanDefinition(id))) {
/* 113 */         counter++;
/* 114 */         id = generatedBeanName + "#" + counter;
/*     */       }
/*     */     }
/* 117 */     return id;
/*     */   }
/*     */ 
/*     */   public static String generateBeanName(BeanDefinition beanDefinition, BeanDefinitionRegistry registry)
/*     */     throws BeanDefinitionStoreException
/*     */   {
/* 133 */     return generateBeanName(beanDefinition, registry, false);
/*     */   }
/*     */ 
/*     */   public static void registerBeanDefinition(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry)
/*     */     throws BeanDefinitionStoreException
/*     */   {
/* 147 */     String beanName = definitionHolder.getBeanName();
/* 148 */     registry.registerBeanDefinition(beanName, definitionHolder.getBeanDefinition());
/*     */ 
/* 151 */     String[] aliases = definitionHolder.getAliases();
/* 152 */     if (aliases != null)
/* 153 */       for (String alias : aliases)
/* 154 */         registry.registerAlias(beanName, alias);
/*     */   }
/*     */ 
/*     */   public static String registerWithGeneratedName(AbstractBeanDefinition definition, BeanDefinitionRegistry registry)
/*     */     throws BeanDefinitionStoreException
/*     */   {
/* 172 */     String generatedName = generateBeanName(definition, registry, false);
/* 173 */     registry.registerBeanDefinition(generatedName, definition);
/* 174 */     return generatedName;
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.support.BeanDefinitionReaderUtils
 * JD-Core Version:    0.6.2
 */