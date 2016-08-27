/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*     */ import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ class DisposableBeanAdapter
/*     */   implements DisposableBean, Runnable, Serializable
/*     */ {
/*     */   private static final String CLOSE_METHOD_NAME = "close";
/*     */   private static final String SHUTDOWN_METHOD_NAME = "shutdown";
/*  69 */   private static final Log logger = LogFactory.getLog(DisposableBeanAdapter.class);
/*     */   private static Class<?> closeableInterface;
/*     */   private final Object bean;
/*     */   private final String beanName;
/*     */   private final boolean invokeDisposableBean;
/*     */   private final boolean nonPublicAccessAllowed;
/*     */   private final AccessControlContext acc;
/*     */   private String destroyMethodName;
/*     */   private transient Method destroyMethod;
/*     */   private List<DestructionAwareBeanPostProcessor> beanPostProcessors;
/*     */ 
/*     */   public DisposableBeanAdapter(Object bean, String beanName, RootBeanDefinition beanDefinition, List<BeanPostProcessor> postProcessors, AccessControlContext acc)
/*     */   {
/* 112 */     Assert.notNull(bean, "Disposable bean must not be null");
/* 113 */     this.bean = bean;
/* 114 */     this.beanName = beanName;
/* 115 */     this.invokeDisposableBean = (((this.bean instanceof Serializable)) && 
/* 116 */       (!beanDefinition
/* 116 */       .isExternallyManagedDestroyMethod("destroy")));
/*     */ 
/* 117 */     this.nonPublicAccessAllowed = beanDefinition.isNonPublicAccessAllowed();
/* 118 */     this.acc = acc;
/* 119 */     String destroyMethodName = inferDestroyMethodIfNecessary(bean, beanDefinition);
/* 120 */     if ((destroyMethodName != null) && ((!this.invokeDisposableBean) || (!"destroy".equals(destroyMethodName))) && 
/* 121 */       (!beanDefinition
/* 121 */       .isExternallyManagedDestroyMethod(destroyMethodName)))
/*     */     {
/* 122 */       this.destroyMethodName = destroyMethodName;
/* 123 */       this.destroyMethod = determineDestroyMethod();
/* 124 */       if (this.destroyMethod == null) {
/* 125 */         if (beanDefinition.isEnforceDestroyMethod()) {
/* 126 */           throw new BeanDefinitionValidationException("Couldn't find a destroy method named '" + destroyMethodName + "' on bean with name '" + beanName + "'");
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 131 */         Class[] paramTypes = this.destroyMethod.getParameterTypes();
/* 132 */         if (paramTypes.length > 1) {
/* 133 */           throw new BeanDefinitionValidationException("Method '" + destroyMethodName + "' of bean '" + beanName + "' has more than one parameter - not supported as destroy method");
/*     */         }
/*     */ 
/* 136 */         if ((paramTypes.length == 1) && (Boolean.TYPE != paramTypes[0])) {
/* 137 */           throw new BeanDefinitionValidationException("Method '" + destroyMethodName + "' of bean '" + beanName + "' has a non-boolean parameter - not supported as destroy method");
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 142 */     this.beanPostProcessors = filterPostProcessors(postProcessors, bean);
/*     */   }
/*     */ 
/*     */   public DisposableBeanAdapter(Object bean, List<BeanPostProcessor> postProcessors, AccessControlContext acc)
/*     */   {
/* 152 */     Assert.notNull(bean, "Disposable bean must not be null");
/* 153 */     this.bean = bean;
/* 154 */     this.beanName = null;
/* 155 */     this.invokeDisposableBean = (this.bean instanceof Serializable);
/* 156 */     this.nonPublicAccessAllowed = true;
/* 157 */     this.acc = acc;
/* 158 */     this.beanPostProcessors = filterPostProcessors(postProcessors, bean);
/*     */   }
/*     */ 
/*     */   private DisposableBeanAdapter(Object bean, String beanName, boolean invokeDisposableBean, boolean nonPublicAccessAllowed, String destroyMethodName, List<DestructionAwareBeanPostProcessor> postProcessors)
/*     */   {
/* 168 */     this.bean = bean;
/* 169 */     this.beanName = beanName;
/* 170 */     this.invokeDisposableBean = invokeDisposableBean;
/* 171 */     this.nonPublicAccessAllowed = nonPublicAccessAllowed;
/* 172 */     this.acc = null;
/* 173 */     this.destroyMethodName = destroyMethodName;
/* 174 */     this.beanPostProcessors = postProcessors;
/*     */   }
/*     */ 
/*     */   private String inferDestroyMethodIfNecessary(Object bean, RootBeanDefinition beanDefinition)
/*     */   {
/* 192 */     String destroyMethodName = beanDefinition.getDestroyMethodName();
/* 193 */     if (("(inferred)".equals(destroyMethodName)) || ((destroyMethodName == null) && 
/* 194 */       (closeableInterface
/* 194 */       .isInstance(bean))))
/*     */     {
/* 197 */       if (!(bean instanceof Serializable)) {
/*     */         try {
/* 199 */           return bean.getClass().getMethod("close", new Class[0]).getName();
/*     */         }
/*     */         catch (NoSuchMethodException ex) {
/*     */           try {
/* 203 */             return bean.getClass().getMethod("shutdown", new Class[0]).getName();
/*     */           }
/*     */           catch (NoSuchMethodException localNoSuchMethodException1)
/*     */           {
/*     */           }
/*     */         }
/*     */       }
/* 210 */       return null;
/*     */     }
/* 212 */     return StringUtils.hasLength(destroyMethodName) ? destroyMethodName : null;
/*     */   }
/*     */ 
/*     */   private List<DestructionAwareBeanPostProcessor> filterPostProcessors(List<BeanPostProcessor> processors, Object bean)
/*     */   {
/* 221 */     List filteredPostProcessors = null;
/* 222 */     if (!CollectionUtils.isEmpty(processors)) {
/* 223 */       filteredPostProcessors = new ArrayList(processors.size());
/* 224 */       for (BeanPostProcessor processor : processors) {
/* 225 */         if ((processor instanceof DestructionAwareBeanPostProcessor)) {
/* 226 */           DestructionAwareBeanPostProcessor dabpp = (DestructionAwareBeanPostProcessor)processor;
/*     */           try {
/* 228 */             if (dabpp.requiresDestruction(bean)) {
/* 229 */               filteredPostProcessors.add(dabpp);
/*     */             }
/*     */ 
/*     */           }
/*     */           catch (AbstractMethodError err)
/*     */           {
/* 235 */             filteredPostProcessors.add(dabpp);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 240 */     return filteredPostProcessors;
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/* 246 */     destroy();
/*     */   }
/*     */ 
/*     */   public void destroy()
/*     */   {
/* 251 */     if (!CollectionUtils.isEmpty(this.beanPostProcessors)) {
/* 252 */       for (DestructionAwareBeanPostProcessor processor : this.beanPostProcessors) {
/* 253 */         processor.postProcessBeforeDestruction(this.bean, this.beanName);
/*     */       }
/*     */     }
/*     */ 
/* 257 */     if (this.invokeDisposableBean) {
/* 258 */       if (logger.isDebugEnabled())
/* 259 */         logger.debug("Invoking destroy() on bean with name '" + this.beanName + "'");
/*     */       try
/*     */       {
/* 262 */         if (System.getSecurityManager() != null) {
/* 263 */           AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */           {
/*     */             public Object run() throws Exception {
/* 266 */               ((DisposableBean)DisposableBeanAdapter.this.bean).destroy();
/* 267 */               return null;
/*     */             }
/*     */           }
/*     */           , this.acc);
/*     */         }
/*     */         else
/*     */         {
/* 272 */           ((Serializable)this.bean).destroy();
/*     */         }
/*     */       }
/*     */       catch (Throwable ex) {
/* 276 */         String msg = "Invocation of destroy method failed on bean with name '" + this.beanName + "'";
/* 277 */         if (logger.isDebugEnabled()) {
/* 278 */           logger.warn(msg, ex);
/*     */         }
/*     */         else {
/* 281 */           logger.warn(msg + ": " + ex);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 286 */     if (this.destroyMethod != null) {
/* 287 */       invokeCustomDestroyMethod(this.destroyMethod);
/*     */     }
/* 289 */     else if (this.destroyMethodName != null) {
/* 290 */       Method methodToCall = determineDestroyMethod();
/* 291 */       if (methodToCall != null)
/* 292 */         invokeCustomDestroyMethod(methodToCall);
/*     */     }
/*     */   }
/*     */ 
/*     */   private Method determineDestroyMethod()
/*     */   {
/*     */     try
/*     */     {
/* 300 */       if (System.getSecurityManager() != null) {
/* 301 */         return (Method)AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public Method run() {
/* 304 */             return DisposableBeanAdapter.this.findDestroyMethod();
/*     */           }
/*     */         });
/*     */       }
/*     */ 
/* 309 */       return findDestroyMethod();
/*     */     }
/*     */     catch (IllegalArgumentException ex)
/*     */     {
/* 314 */       throw new BeanDefinitionValidationException("Could not find unique destroy method on bean with name '" + this.beanName + ": " + ex
/* 314 */         .getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   private Method findDestroyMethod()
/*     */   {
/* 321 */     return this.nonPublicAccessAllowed ? 
/* 320 */       BeanUtils.findMethodWithMinimalParameters(this.bean
/* 320 */       .getClass(), this.destroyMethodName) : 
/* 321 */       BeanUtils.findMethodWithMinimalParameters(this.bean
/* 321 */       .getClass().getMethods(), this.destroyMethodName);
/*     */   }
/*     */ 
/*     */   private void invokeCustomDestroyMethod(final Method destroyMethod)
/*     */   {
/* 331 */     Class[] paramTypes = destroyMethod.getParameterTypes();
/* 332 */     final Object[] args = new Object[paramTypes.length];
/* 333 */     if (paramTypes.length == 1) {
/* 334 */       args[0] = Boolean.TRUE;
/*     */     }
/* 336 */     if (logger.isDebugEnabled()) {
/* 337 */       logger.debug("Invoking destroy method '" + this.destroyMethodName + "' on bean with name '" + this.beanName + "'");
/*     */     }
/*     */     try
/*     */     {
/* 341 */       if (System.getSecurityManager() != null) {
/* 342 */         AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public Object run() {
/* 345 */             ReflectionUtils.makeAccessible(destroyMethod);
/* 346 */             return null;
/*     */           }
/*     */         });
/*     */         try {
/* 350 */           AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */           {
/*     */             public Object run() throws Exception {
/* 353 */               destroyMethod.invoke(DisposableBeanAdapter.this.bean, args);
/* 354 */               return null;
/*     */             }
/*     */           }
/*     */           , this.acc);
/*     */         }
/*     */         catch (PrivilegedActionException pax)
/*     */         {
/* 359 */           throw ((InvocationTargetException)pax.getException());
/*     */         }
/*     */       }
/*     */       else {
/* 363 */         ReflectionUtils.makeAccessible(destroyMethod);
/* 364 */         destroyMethod.invoke(this.bean, args);
/*     */       }
/*     */     }
/*     */     catch (InvocationTargetException ex) {
/* 368 */       String msg = "Invocation of destroy method '" + this.destroyMethodName + "' failed on bean with name '" + this.beanName + "'";
/*     */ 
/* 370 */       if (logger.isDebugEnabled()) {
/* 371 */         logger.warn(msg, ex.getTargetException());
/*     */       }
/*     */       else
/* 374 */         logger.warn(msg + ": " + ex.getTargetException());
/*     */     }
/*     */     catch (Throwable ex)
/*     */     {
/* 378 */       logger.error("Couldn't invoke destroy method '" + this.destroyMethodName + "' on bean with name '" + this.beanName + "'", ex);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Object writeReplace()
/*     */   {
/* 389 */     List serializablePostProcessors = null;
/* 390 */     if (this.beanPostProcessors != null) {
/* 391 */       serializablePostProcessors = new ArrayList();
/* 392 */       for (DestructionAwareBeanPostProcessor postProcessor : this.beanPostProcessors) {
/* 393 */         if ((postProcessor instanceof Serializable)) {
/* 394 */           serializablePostProcessors.add(postProcessor);
/*     */         }
/*     */       }
/*     */     }
/* 398 */     return new DisposableBeanAdapter(this.bean, this.beanName, this.invokeDisposableBean, this.nonPublicAccessAllowed, this.destroyMethodName, serializablePostProcessors);
/*     */   }
/*     */ 
/*     */   public static boolean hasDestroyMethod(Object bean, RootBeanDefinition beanDefinition)
/*     */   {
/* 409 */     if (((bean instanceof Serializable)) || (closeableInterface.isInstance(bean))) {
/* 410 */       return true;
/*     */     }
/* 412 */     String destroyMethodName = beanDefinition.getDestroyMethodName();
/* 413 */     if ("(inferred)".equals(destroyMethodName))
/*     */     {
/* 415 */       return (ClassUtils.hasMethod(bean.getClass(), "close", new Class[0])) || 
/* 415 */         (ClassUtils.hasMethod(bean
/* 415 */         .getClass(), "shutdown", new Class[0]));
/*     */     }
/* 417 */     return StringUtils.hasLength(destroyMethodName);
/*     */   }
/*     */ 
/*     */   public static boolean hasApplicableProcessors(Object bean, List<BeanPostProcessor> postProcessors)
/*     */   {
/* 426 */     if (!CollectionUtils.isEmpty(postProcessors)) {
/* 427 */       for (BeanPostProcessor processor : postProcessors) {
/* 428 */         if ((processor instanceof DestructionAwareBeanPostProcessor)) {
/* 429 */           DestructionAwareBeanPostProcessor dabpp = (DestructionAwareBeanPostProcessor)processor;
/*     */           try {
/* 431 */             if (dabpp.requiresDestruction(bean)) {
/* 432 */               return true;
/*     */             }
/*     */ 
/*     */           }
/*     */           catch (AbstractMethodError err)
/*     */           {
/* 438 */             return true;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 443 */     return false;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  75 */       closeableInterface = ClassUtils.forName("java.lang.AutoCloseable", DisposableBeanAdapter.class
/*  76 */         .getClassLoader());
/*     */     }
/*     */     catch (ClassNotFoundException ex) {
/*  79 */       closeableInterface = Closeable.class;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.support.DisposableBeanAdapter
 * JD-Core Version:    0.6.2
 */