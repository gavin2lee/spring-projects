/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import org.springframework.beans.BeanInstantiationException;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ public class SimpleInstantiationStrategy
/*     */   implements InstantiationStrategy
/*     */ {
/*  45 */   private static final ThreadLocal<Method> currentlyInvokedFactoryMethod = new ThreadLocal();
/*     */ 
/*     */   public static Method getCurrentlyInvokedFactoryMethod()
/*     */   {
/*  54 */     return (Method)currentlyInvokedFactoryMethod.get();
/*     */   }
/*     */ 
/*     */   public Object instantiate(RootBeanDefinition bd, String beanName, BeanFactory owner)
/*     */   {
/*  61 */     if (bd.getMethodOverrides().isEmpty())
/*     */     {
/*  63 */       synchronized (bd.constructorArgumentLock) {
/*  64 */         Constructor constructorToUse = (Constructor)bd.resolvedConstructorOrFactoryMethod;
/*  65 */         if (constructorToUse == null) {
/*  66 */           final Class clazz = bd.getBeanClass();
/*  67 */           if (clazz.isInterface())
/*  68 */             throw new BeanInstantiationException(clazz, "Specified class is an interface");
/*     */           try
/*     */           {
/*  71 */             if (System.getSecurityManager() != null) {
/*  72 */               constructorToUse = (Constructor)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */               {
/*     */                 public Constructor<?> run() throws Exception {
/*  75 */                   return clazz.getDeclaredConstructor((Class[])null);
/*     */                 }
/*     */               });
/*     */             }
/*     */             else {
/*  80 */               constructorToUse = clazz.getDeclaredConstructor((Class[])null);
/*     */             }
/*  82 */             bd.resolvedConstructorOrFactoryMethod = constructorToUse;
/*     */           }
/*     */           catch (Throwable ex) {
/*  85 */             throw new BeanInstantiationException(clazz, "No default constructor found", ex);
/*     */           }
/*     */         }
/*     */       }
/*     */       Constructor constructorToUse;
/*  89 */       return BeanUtils.instantiateClass(constructorToUse, new Object[0]);
/*     */     }
/*     */ 
/*  93 */     return instantiateWithMethodInjection(bd, beanName, owner);
/*     */   }
/*     */ 
/*     */   protected Object instantiateWithMethodInjection(RootBeanDefinition bd, String beanName, BeanFactory owner)
/*     */   {
/* 104 */     throw new UnsupportedOperationException("Method Injection not supported in SimpleInstantiationStrategy");
/*     */   }
/*     */ 
/*     */   public Object instantiate(RootBeanDefinition bd, String beanName, BeanFactory owner, final Constructor<?> ctor, Object[] args)
/*     */   {
/* 111 */     if (bd.getMethodOverrides().isEmpty()) {
/* 112 */       if (System.getSecurityManager() != null)
/*     */       {
/* 114 */         AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public Object run() {
/* 117 */             ReflectionUtils.makeAccessible(ctor);
/* 118 */             return null;
/*     */           }
/*     */         });
/*     */       }
/* 122 */       return BeanUtils.instantiateClass(ctor, args);
/*     */     }
/*     */ 
/* 125 */     return instantiateWithMethodInjection(bd, beanName, owner, ctor, args);
/*     */   }
/*     */ 
/*     */   protected Object instantiateWithMethodInjection(RootBeanDefinition bd, String beanName, BeanFactory owner, Constructor<?> ctor, Object[] args)
/*     */   {
/* 138 */     throw new UnsupportedOperationException("Method Injection not supported in SimpleInstantiationStrategy");
/*     */   }
/*     */ 
/*     */   public Object instantiate(RootBeanDefinition bd, String beanName, BeanFactory owner, Object factoryBean, final Method factoryMethod, Object[] args)
/*     */   {
/*     */     try
/*     */     {
/* 146 */       if (System.getSecurityManager() != null) {
/* 147 */         AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public Object run() {
/* 150 */             ReflectionUtils.makeAccessible(factoryMethod);
/* 151 */             return null;
/*     */           }
/*     */         });
/*     */       }
/*     */       else {
/* 156 */         ReflectionUtils.makeAccessible(factoryMethod);
/*     */       }
/*     */ 
/* 159 */       Method priorInvokedFactoryMethod = (Method)currentlyInvokedFactoryMethod.get();
/*     */       try {
/* 161 */         currentlyInvokedFactoryMethod.set(factoryMethod);
/* 162 */         return factoryMethod.invoke(factoryBean, args);
/*     */       }
/*     */       finally {
/* 165 */         if (priorInvokedFactoryMethod != null) {
/* 166 */           currentlyInvokedFactoryMethod.set(priorInvokedFactoryMethod);
/*     */         }
/*     */         else {
/* 169 */           currentlyInvokedFactoryMethod.remove();
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (IllegalArgumentException ex)
/*     */     {
/* 176 */       throw new BeanInstantiationException(factoryMethod, "Illegal arguments to factory method '" + factoryMethod
/* 175 */         .getName() + "'; " + "args: " + 
/* 176 */         StringUtils.arrayToCommaDelimitedString(args), 
/* 176 */         ex);
/*     */     }
/*     */     catch (IllegalAccessException ex)
/*     */     {
/* 180 */       throw new BeanInstantiationException(factoryMethod, "Cannot access factory method '" + factoryMethod
/* 180 */         .getName() + "'; is it public?", ex);
/*     */     }
/*     */     catch (InvocationTargetException ex) {
/* 183 */       String msg = "Factory method '" + factoryMethod.getName() + "' threw exception";
/* 184 */       if ((bd.getFactoryBeanName() != null) && ((owner instanceof ConfigurableBeanFactory)) && 
/* 185 */         (((ConfigurableBeanFactory)owner)
/* 185 */         .isCurrentlyInCreation(bd
/* 185 */         .getFactoryBeanName()))) {
/* 186 */         msg = "Circular reference involving containing bean '" + bd.getFactoryBeanName() + "' - consider " + "declaring the factory method as static for independence from its containing instance. " + msg;
/*     */       }
/*     */ 
/* 189 */       throw new BeanInstantiationException(factoryMethod, msg, ex.getTargetException());
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.support.SimpleInstantiationStrategy
 * JD-Core Version:    0.6.2
 */