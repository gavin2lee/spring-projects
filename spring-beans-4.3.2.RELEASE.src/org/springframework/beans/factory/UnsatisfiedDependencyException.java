/*     */ package org.springframework.beans.factory;
/*     */ 
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ public class UnsatisfiedDependencyException extends BeanCreationException
/*     */ {
/*     */   private InjectionPoint injectionPoint;
/*     */ 
/*     */   public UnsatisfiedDependencyException(String resourceDescription, String beanName, String propertyName, String msg)
/*     */   {
/*  47 */     super(resourceDescription, beanName, new StringBuilder().append("Unsatisfied dependency expressed through bean property '").append(propertyName).append("'").append(msg != null ? new StringBuilder().append(": ").append(msg).toString() : "").toString());
/*     */   }
/*     */ 
/*     */   public UnsatisfiedDependencyException(String resourceDescription, String beanName, String propertyName, BeansException ex)
/*     */   {
/*  62 */     this(resourceDescription, beanName, propertyName, ex != null ? ex.getMessage() : "");
/*  63 */     initCause(ex);
/*     */   }
/*     */ 
/*     */   public UnsatisfiedDependencyException(String resourceDescription, String beanName, InjectionPoint injectionPoint, String msg)
/*     */   {
/*  77 */     super(resourceDescription, beanName, new StringBuilder().append("Unsatisfied dependency expressed through ").append(injectionPoint).append(": ").append(msg).toString());
/*  78 */     this.injectionPoint = injectionPoint;
/*     */   }
/*     */ 
/*     */   public UnsatisfiedDependencyException(String resourceDescription, String beanName, InjectionPoint injectionPoint, BeansException ex)
/*     */   {
/*  92 */     this(resourceDescription, beanName, injectionPoint, ex != null ? ex.getMessage() : "");
/*  93 */     initCause(ex);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public UnsatisfiedDependencyException(String resourceDescription, String beanName, int ctorArgIndex, Class<?> ctorArgType, String msg)
/*     */   {
/* 109 */     super(resourceDescription, beanName, new StringBuilder().append("Unsatisfied dependency expressed through constructor argument with index ").append(ctorArgIndex).append(" of type [")
/* 111 */       .append(ClassUtils.getQualifiedName(ctorArgType))
/* 111 */       .append("]").append(msg != null ? new StringBuilder().append(": ").append(msg).toString() : "").toString());
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public UnsatisfiedDependencyException(String resourceDescription, String beanName, int ctorArgIndex, Class<?> ctorArgType, BeansException ex)
/*     */   {
/* 128 */     this(resourceDescription, beanName, ctorArgIndex, ctorArgType, ex != null ? ex.getMessage() : "");
/* 129 */     initCause(ex);
/*     */   }
/*     */ 
/*     */   public InjectionPoint getInjectionPoint()
/*     */   {
/* 138 */     return this.injectionPoint;
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.UnsatisfiedDependencyException
 * JD-Core Version:    0.6.2
 */