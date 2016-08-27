/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ public class RootBeanDefinition extends AbstractBeanDefinition
/*     */ {
/*  51 */   boolean allowCaching = true;
/*     */   private BeanDefinitionHolder decoratedDefinition;
/*     */   private volatile Class<?> targetType;
/*  57 */   boolean isFactoryMethodUnique = false;
/*     */ 
/*  59 */   final Object constructorArgumentLock = new Object();
/*     */   Object resolvedConstructorOrFactoryMethod;
/*     */   volatile Class<?> resolvedFactoryMethodReturnType;
/*  68 */   boolean constructorArgumentsResolved = false;
/*     */   Object[] resolvedConstructorArguments;
/*     */   Object[] preparedConstructorArguments;
/*  76 */   final Object postProcessingLock = new Object();
/*     */ 
/*  79 */   boolean postProcessed = false;
/*     */   volatile Boolean beforeInstantiationResolved;
/*     */   private Set<Member> externallyManagedConfigMembers;
/*     */   private Set<String> externallyManagedInitMethods;
/*     */   private Set<String> externallyManagedDestroyMethods;
/*     */ 
/*     */   public RootBeanDefinition()
/*     */   {
/*     */   }
/*     */ 
/*     */   public RootBeanDefinition(Class<?> beanClass)
/*     */   {
/* 112 */     setBeanClass(beanClass);
/*     */   }
/*     */ 
/*     */   public RootBeanDefinition(Class<?> beanClass, int autowireMode, boolean dependencyCheck)
/*     */   {
/* 125 */     setBeanClass(beanClass);
/* 126 */     setAutowireMode(autowireMode);
/* 127 */     if ((dependencyCheck) && (getResolvedAutowireMode() != 3))
/* 128 */       setDependencyCheck(1);
/*     */   }
/*     */ 
/*     */   public RootBeanDefinition(Class<?> beanClass, ConstructorArgumentValues cargs, MutablePropertyValues pvs)
/*     */   {
/* 140 */     super(cargs, pvs);
/* 141 */     setBeanClass(beanClass);
/*     */   }
/*     */ 
/*     */   public RootBeanDefinition(String beanClassName)
/*     */   {
/* 151 */     setBeanClassName(beanClassName);
/*     */   }
/*     */ 
/*     */   public RootBeanDefinition(String beanClassName, ConstructorArgumentValues cargs, MutablePropertyValues pvs)
/*     */   {
/* 163 */     super(cargs, pvs);
/* 164 */     setBeanClassName(beanClassName);
/*     */   }
/*     */ 
/*     */   public RootBeanDefinition(RootBeanDefinition original)
/*     */   {
/* 173 */     super(original);
/* 174 */     this.allowCaching = original.allowCaching;
/* 175 */     this.decoratedDefinition = original.decoratedDefinition;
/* 176 */     this.targetType = original.targetType;
/* 177 */     this.isFactoryMethodUnique = original.isFactoryMethodUnique;
/*     */   }
/*     */ 
/*     */   RootBeanDefinition(BeanDefinition original)
/*     */   {
/* 186 */     super(original);
/*     */   }
/*     */ 
/*     */   public String getParentName()
/*     */   {
/* 192 */     return null;
/*     */   }
/*     */ 
/*     */   public void setParentName(String parentName)
/*     */   {
/* 197 */     if (parentName != null)
/* 198 */       throw new IllegalArgumentException("Root bean cannot be changed into a child bean with parent reference");
/*     */   }
/*     */ 
/*     */   public void setDecoratedDefinition(BeanDefinitionHolder decoratedDefinition)
/*     */   {
/* 206 */     this.decoratedDefinition = decoratedDefinition;
/*     */   }
/*     */ 
/*     */   public BeanDefinitionHolder getDecoratedDefinition()
/*     */   {
/* 213 */     return this.decoratedDefinition;
/*     */   }
/*     */ 
/*     */   public void setTargetType(Class<?> targetType)
/*     */   {
/* 220 */     this.targetType = targetType;
/*     */   }
/*     */ 
/*     */   public Class<?> getTargetType()
/*     */   {
/* 228 */     return this.targetType;
/*     */   }
/*     */ 
/*     */   public void setUniqueFactoryMethodName(String name)
/*     */   {
/* 235 */     Assert.hasText(name, "Factory method name must not be empty");
/* 236 */     setFactoryMethodName(name);
/* 237 */     this.isFactoryMethodUnique = true;
/*     */   }
/*     */ 
/*     */   public boolean isFactoryMethod(Method candidate)
/*     */   {
/* 244 */     return (candidate != null) && (candidate.getName().equals(getFactoryMethodName()));
/*     */   }
/*     */ 
/*     */   public Method getResolvedFactoryMethod()
/*     */   {
/* 252 */     synchronized (this.constructorArgumentLock) {
/* 253 */       Object candidate = this.resolvedConstructorOrFactoryMethod;
/* 254 */       return (candidate instanceof Method) ? (Method)candidate : null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void registerExternallyManagedConfigMember(Member configMember) {
/* 259 */     synchronized (this.postProcessingLock) {
/* 260 */       if (this.externallyManagedConfigMembers == null) {
/* 261 */         this.externallyManagedConfigMembers = new HashSet(1);
/*     */       }
/* 263 */       this.externallyManagedConfigMembers.add(configMember);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isExternallyManagedConfigMember(Member configMember) {
/* 268 */     synchronized (this.postProcessingLock)
/*     */     {
/* 270 */       return (this.externallyManagedConfigMembers != null) && 
/* 270 */         (this.externallyManagedConfigMembers
/* 270 */         .contains(configMember));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void registerExternallyManagedInitMethod(String initMethod)
/*     */   {
/* 275 */     synchronized (this.postProcessingLock) {
/* 276 */       if (this.externallyManagedInitMethods == null) {
/* 277 */         this.externallyManagedInitMethods = new HashSet(1);
/*     */       }
/* 279 */       this.externallyManagedInitMethods.add(initMethod);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isExternallyManagedInitMethod(String initMethod) {
/* 284 */     synchronized (this.postProcessingLock)
/*     */     {
/* 286 */       return (this.externallyManagedInitMethods != null) && 
/* 286 */         (this.externallyManagedInitMethods
/* 286 */         .contains(initMethod));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void registerExternallyManagedDestroyMethod(String destroyMethod)
/*     */   {
/* 291 */     synchronized (this.postProcessingLock) {
/* 292 */       if (this.externallyManagedDestroyMethods == null) {
/* 293 */         this.externallyManagedDestroyMethods = new HashSet(1);
/*     */       }
/* 295 */       this.externallyManagedDestroyMethods.add(destroyMethod);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isExternallyManagedDestroyMethod(String destroyMethod) {
/* 300 */     synchronized (this.postProcessingLock)
/*     */     {
/* 302 */       return (this.externallyManagedDestroyMethods != null) && 
/* 302 */         (this.externallyManagedDestroyMethods
/* 302 */         .contains(destroyMethod));
/*     */     }
/*     */   }
/*     */ 
/*     */   public RootBeanDefinition cloneBeanDefinition()
/*     */   {
/* 309 */     return new RootBeanDefinition(this);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/* 314 */     return (this == other) || (((other instanceof RootBeanDefinition)) && (super.equals(other)));
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 319 */     return "Root bean: " + super.toString();
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.support.RootBeanDefinition
 * JD-Core Version:    0.6.2
 */