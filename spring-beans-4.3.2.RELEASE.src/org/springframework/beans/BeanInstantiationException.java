/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ 
/*     */ public class BeanInstantiationException extends FatalBeanException
/*     */ {
/*     */   private Class<?> beanClass;
/*     */   private Constructor<?> constructor;
/*     */   private Method constructingMethod;
/*     */ 
/*     */   public BeanInstantiationException(Class<?> beanClass, String msg)
/*     */   {
/*  45 */     this(beanClass, msg, null);
/*     */   }
/*     */ 
/*     */   public BeanInstantiationException(Class<?> beanClass, String msg, Throwable cause)
/*     */   {
/*  55 */     super("Failed to instantiate [" + beanClass.getName() + "]: " + msg, cause);
/*  56 */     this.beanClass = beanClass;
/*     */   }
/*     */ 
/*     */   public BeanInstantiationException(Constructor<?> constructor, String msg, Throwable cause)
/*     */   {
/*  67 */     super("Failed to instantiate [" + constructor.getDeclaringClass().getName() + "]: " + msg, cause);
/*  68 */     this.beanClass = constructor.getDeclaringClass();
/*  69 */     this.constructor = constructor;
/*     */   }
/*     */ 
/*     */   public BeanInstantiationException(Method constructingMethod, String msg, Throwable cause)
/*     */   {
/*  81 */     super("Failed to instantiate [" + constructingMethod.getReturnType().getName() + "]: " + msg, cause);
/*  82 */     this.beanClass = constructingMethod.getReturnType();
/*  83 */     this.constructingMethod = constructingMethod;
/*     */   }
/*     */ 
/*     */   public Class<?> getBeanClass()
/*     */   {
/*  92 */     return this.beanClass;
/*     */   }
/*     */ 
/*     */   public Constructor<?> getConstructor()
/*     */   {
/* 102 */     return this.constructor;
/*     */   }
/*     */ 
/*     */   public Method getConstructingMethod()
/*     */   {
/* 112 */     return this.constructingMethod;
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.BeanInstantiationException
 * JD-Core Version:    0.6.2
 */