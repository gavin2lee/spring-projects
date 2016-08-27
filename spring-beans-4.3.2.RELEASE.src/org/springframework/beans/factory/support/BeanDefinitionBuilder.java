/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*     */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ public class BeanDefinitionBuilder
/*     */ {
/*     */   private AbstractBeanDefinition beanDefinition;
/*     */   private int constructorArgIndex;
/*     */ 
/*     */   public static BeanDefinitionBuilder genericBeanDefinition()
/*     */   {
/*  39 */     BeanDefinitionBuilder builder = new BeanDefinitionBuilder();
/*  40 */     builder.beanDefinition = new GenericBeanDefinition();
/*  41 */     return builder;
/*     */   }
/*     */ 
/*     */   public static BeanDefinitionBuilder genericBeanDefinition(Class<?> beanClass)
/*     */   {
/*  49 */     BeanDefinitionBuilder builder = new BeanDefinitionBuilder();
/*  50 */     builder.beanDefinition = new GenericBeanDefinition();
/*  51 */     builder.beanDefinition.setBeanClass(beanClass);
/*  52 */     return builder;
/*     */   }
/*     */ 
/*     */   public static BeanDefinitionBuilder genericBeanDefinition(String beanClassName)
/*     */   {
/*  60 */     BeanDefinitionBuilder builder = new BeanDefinitionBuilder();
/*  61 */     builder.beanDefinition = new GenericBeanDefinition();
/*  62 */     builder.beanDefinition.setBeanClassName(beanClassName);
/*  63 */     return builder;
/*     */   }
/*     */ 
/*     */   public static BeanDefinitionBuilder rootBeanDefinition(Class<?> beanClass)
/*     */   {
/*  71 */     return rootBeanDefinition(beanClass, null);
/*     */   }
/*     */ 
/*     */   public static BeanDefinitionBuilder rootBeanDefinition(Class<?> beanClass, String factoryMethodName)
/*     */   {
/*  80 */     BeanDefinitionBuilder builder = new BeanDefinitionBuilder();
/*  81 */     builder.beanDefinition = new RootBeanDefinition();
/*  82 */     builder.beanDefinition.setBeanClass(beanClass);
/*  83 */     builder.beanDefinition.setFactoryMethodName(factoryMethodName);
/*  84 */     return builder;
/*     */   }
/*     */ 
/*     */   public static BeanDefinitionBuilder rootBeanDefinition(String beanClassName)
/*     */   {
/*  92 */     return rootBeanDefinition(beanClassName, null);
/*     */   }
/*     */ 
/*     */   public static BeanDefinitionBuilder rootBeanDefinition(String beanClassName, String factoryMethodName)
/*     */   {
/* 101 */     BeanDefinitionBuilder builder = new BeanDefinitionBuilder();
/* 102 */     builder.beanDefinition = new RootBeanDefinition();
/* 103 */     builder.beanDefinition.setBeanClassName(beanClassName);
/* 104 */     builder.beanDefinition.setFactoryMethodName(factoryMethodName);
/* 105 */     return builder;
/*     */   }
/*     */ 
/*     */   public static BeanDefinitionBuilder childBeanDefinition(String parentName)
/*     */   {
/* 113 */     BeanDefinitionBuilder builder = new BeanDefinitionBuilder();
/* 114 */     builder.beanDefinition = new ChildBeanDefinition(parentName);
/* 115 */     return builder;
/*     */   }
/*     */ 
/*     */   public AbstractBeanDefinition getRawBeanDefinition()
/*     */   {
/* 141 */     return this.beanDefinition;
/*     */   }
/*     */ 
/*     */   public AbstractBeanDefinition getBeanDefinition()
/*     */   {
/* 148 */     this.beanDefinition.validate();
/* 149 */     return this.beanDefinition;
/*     */   }
/*     */ 
/*     */   public BeanDefinitionBuilder setParentName(String parentName)
/*     */   {
/* 157 */     this.beanDefinition.setParentName(parentName);
/* 158 */     return this;
/*     */   }
/*     */ 
/*     */   public BeanDefinitionBuilder setFactoryMethod(String factoryMethod)
/*     */   {
/* 165 */     this.beanDefinition.setFactoryMethodName(factoryMethod);
/* 166 */     return this;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public BeanDefinitionBuilder addConstructorArg(Object value)
/*     */   {
/* 177 */     return addConstructorArgValue(value);
/*     */   }
/*     */ 
/*     */   public BeanDefinitionBuilder addConstructorArgValue(Object value)
/*     */   {
/* 185 */     this.beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(this.constructorArgIndex++, value);
/*     */ 
/* 187 */     return this;
/*     */   }
/*     */ 
/*     */   public BeanDefinitionBuilder addConstructorArgReference(String beanName)
/*     */   {
/* 195 */     this.beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(this.constructorArgIndex++, new RuntimeBeanReference(beanName));
/*     */ 
/* 197 */     return this;
/*     */   }
/*     */ 
/*     */   public BeanDefinitionBuilder addPropertyValue(String name, Object value)
/*     */   {
/* 204 */     this.beanDefinition.getPropertyValues().add(name, value);
/* 205 */     return this;
/*     */   }
/*     */ 
/*     */   public BeanDefinitionBuilder addPropertyReference(String name, String beanName)
/*     */   {
/* 214 */     this.beanDefinition.getPropertyValues().add(name, new RuntimeBeanReference(beanName));
/* 215 */     return this;
/*     */   }
/*     */ 
/*     */   public BeanDefinitionBuilder setInitMethodName(String methodName)
/*     */   {
/* 222 */     this.beanDefinition.setInitMethodName(methodName);
/* 223 */     return this;
/*     */   }
/*     */ 
/*     */   public BeanDefinitionBuilder setDestroyMethodName(String methodName)
/*     */   {
/* 230 */     this.beanDefinition.setDestroyMethodName(methodName);
/* 231 */     return this;
/*     */   }
/*     */ 
/*     */   public BeanDefinitionBuilder setScope(String scope)
/*     */   {
/* 241 */     this.beanDefinition.setScope(scope);
/* 242 */     return this;
/*     */   }
/*     */ 
/*     */   public BeanDefinitionBuilder setAbstract(boolean flag)
/*     */   {
/* 249 */     this.beanDefinition.setAbstract(flag);
/* 250 */     return this;
/*     */   }
/*     */ 
/*     */   public BeanDefinitionBuilder setLazyInit(boolean lazy)
/*     */   {
/* 257 */     this.beanDefinition.setLazyInit(lazy);
/* 258 */     return this;
/*     */   }
/*     */ 
/*     */   public BeanDefinitionBuilder setAutowireMode(int autowireMode)
/*     */   {
/* 265 */     this.beanDefinition.setAutowireMode(autowireMode);
/* 266 */     return this;
/*     */   }
/*     */ 
/*     */   public BeanDefinitionBuilder setDependencyCheck(int dependencyCheck)
/*     */   {
/* 273 */     this.beanDefinition.setDependencyCheck(dependencyCheck);
/* 274 */     return this;
/*     */   }
/*     */ 
/*     */   public BeanDefinitionBuilder addDependsOn(String beanName)
/*     */   {
/* 282 */     if (this.beanDefinition.getDependsOn() == null) {
/* 283 */       this.beanDefinition.setDependsOn(new String[] { beanName });
/*     */     }
/*     */     else {
/* 286 */       String[] added = (String[])ObjectUtils.addObjectToArray(this.beanDefinition.getDependsOn(), beanName);
/* 287 */       this.beanDefinition.setDependsOn(added);
/*     */     }
/* 289 */     return this;
/*     */   }
/*     */ 
/*     */   public BeanDefinitionBuilder setRole(int role)
/*     */   {
/* 296 */     this.beanDefinition.setRole(role);
/* 297 */     return this;
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.support.BeanDefinitionBuilder
 * JD-Core Version:    0.6.2
 */