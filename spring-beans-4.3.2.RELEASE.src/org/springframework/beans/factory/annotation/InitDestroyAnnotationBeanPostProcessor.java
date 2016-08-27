/*     */ package org.springframework.beans.factory.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanCreationException;
/*     */ import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
/*     */ import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.core.PriorityOrdered;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.ReflectionUtils.MethodCallback;
/*     */ 
/*     */ public class InitDestroyAnnotationBeanPostProcessor
/*     */   implements DestructionAwareBeanPostProcessor, MergedBeanDefinitionPostProcessor, PriorityOrdered, Serializable
/*     */ {
/*  77 */   protected transient Log logger = LogFactory.getLog(getClass());
/*     */   private Class<? extends Annotation> initAnnotationType;
/*     */   private Class<? extends Annotation> destroyAnnotationType;
/*  83 */   private int order = 2147483647;
/*     */ 
/*  85 */   private final transient Map<Class<?>, LifecycleMetadata> lifecycleMetadataCache = new ConcurrentHashMap(256);
/*     */ 
/*     */   public void setInitAnnotationType(Class<? extends Annotation> initAnnotationType)
/*     */   {
/*  97 */     this.initAnnotationType = initAnnotationType;
/*     */   }
/*     */ 
/*     */   public void setDestroyAnnotationType(Class<? extends Annotation> destroyAnnotationType)
/*     */   {
/* 108 */     this.destroyAnnotationType = destroyAnnotationType;
/*     */   }
/*     */ 
/*     */   public void setOrder(int order) {
/* 112 */     this.order = order;
/*     */   }
/*     */ 
/*     */   public int getOrder()
/*     */   {
/* 117 */     return this.order;
/*     */   }
/*     */ 
/*     */   public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName)
/*     */   {
/* 123 */     if (beanType != null) {
/* 124 */       LifecycleMetadata metadata = findLifecycleMetadata(beanType);
/* 125 */       metadata.checkConfigMembers(beanDefinition);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException
/*     */   {
/* 131 */     LifecycleMetadata metadata = findLifecycleMetadata(bean.getClass());
/*     */     try {
/* 133 */       metadata.invokeInitMethods(bean, beanName);
/*     */     }
/*     */     catch (InvocationTargetException ex) {
/* 136 */       throw new BeanCreationException(beanName, "Invocation of init method failed", ex.getTargetException());
/*     */     }
/*     */     catch (Throwable ex) {
/* 139 */       throw new BeanCreationException(beanName, "Failed to invoke init method", ex);
/*     */     }
/* 141 */     return bean;
/*     */   }
/*     */ 
/*     */   public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException
/*     */   {
/* 146 */     return bean;
/*     */   }
/*     */ 
/*     */   public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException
/*     */   {
/* 151 */     LifecycleMetadata metadata = findLifecycleMetadata(bean.getClass());
/*     */     try {
/* 153 */       metadata.invokeDestroyMethods(bean, beanName);
/*     */     }
/*     */     catch (InvocationTargetException ex) {
/* 156 */       String msg = "Invocation of destroy method failed on bean with name '" + beanName + "'";
/* 157 */       if (this.logger.isDebugEnabled()) {
/* 158 */         this.logger.warn(msg, ex.getTargetException());
/*     */       }
/*     */       else
/* 161 */         this.logger.warn(msg + ": " + ex.getTargetException());
/*     */     }
/*     */     catch (Throwable ex)
/*     */     {
/* 165 */       this.logger.error("Failed to invoke destroy method on bean with name '" + beanName + "'", ex);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean requiresDestruction(Object bean)
/*     */   {
/* 171 */     return findLifecycleMetadata(bean.getClass()).hasDestroyMethods();
/*     */   }
/*     */ 
/*     */   private LifecycleMetadata findLifecycleMetadata(Class<?> clazz)
/*     */   {
/* 176 */     if (this.lifecycleMetadataCache == null)
/*     */     {
/* 178 */       return buildLifecycleMetadata(clazz);
/*     */     }
/*     */ 
/* 181 */     LifecycleMetadata metadata = (LifecycleMetadata)this.lifecycleMetadataCache.get(clazz);
/* 182 */     if (metadata == null) {
/* 183 */       synchronized (this.lifecycleMetadataCache) {
/* 184 */         metadata = (LifecycleMetadata)this.lifecycleMetadataCache.get(clazz);
/* 185 */         if (metadata == null) {
/* 186 */           metadata = buildLifecycleMetadata(clazz);
/* 187 */           this.lifecycleMetadataCache.put(clazz, metadata);
/*     */         }
/* 189 */         return metadata;
/*     */       }
/*     */     }
/* 192 */     return metadata;
/*     */   }
/*     */ 
/*     */   private LifecycleMetadata buildLifecycleMetadata(final Class<?> clazz) {
/* 196 */     final boolean debug = this.logger.isDebugEnabled();
/* 197 */     LinkedList initMethods = new LinkedList();
/* 198 */     LinkedList destroyMethods = new LinkedList();
/* 199 */     Class targetClass = clazz;
/*     */     do
/*     */     {
/* 202 */       final LinkedList currInitMethods = new LinkedList();
/* 203 */       final LinkedList currDestroyMethods = new LinkedList();
/*     */ 
/* 205 */       ReflectionUtils.doWithLocalMethods(targetClass, new ReflectionUtils.MethodCallback()
/*     */       {
/*     */         public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
/* 208 */           if ((InitDestroyAnnotationBeanPostProcessor.this.initAnnotationType != null) && 
/* 209 */             (method.getAnnotation(InitDestroyAnnotationBeanPostProcessor.this.initAnnotationType) != null)) {
/* 210 */             InitDestroyAnnotationBeanPostProcessor.LifecycleElement element = new InitDestroyAnnotationBeanPostProcessor.LifecycleElement(method);
/* 211 */             currInitMethods.add(element);
/* 212 */             if (debug) {
/* 213 */               InitDestroyAnnotationBeanPostProcessor.this.logger.debug("Found init method on class [" + clazz.getName() + "]: " + method);
/*     */             }
/*     */           }
/*     */ 
/* 217 */           if ((InitDestroyAnnotationBeanPostProcessor.this.destroyAnnotationType != null) && 
/* 218 */             (method.getAnnotation(InitDestroyAnnotationBeanPostProcessor.this.destroyAnnotationType) != null)) {
/* 219 */             currDestroyMethods.add(new InitDestroyAnnotationBeanPostProcessor.LifecycleElement(method));
/* 220 */             if (debug)
/* 221 */               InitDestroyAnnotationBeanPostProcessor.this.logger.debug("Found destroy method on class [" + clazz.getName() + "]: " + method);
/*     */           }
/*     */         }
/*     */       });
/* 228 */       initMethods.addAll(0, currInitMethods);
/* 229 */       destroyMethods.addAll(currDestroyMethods);
/* 230 */       targetClass = targetClass.getSuperclass();
/*     */     }
/* 232 */     while ((targetClass != null) && (targetClass != Object.class));
/*     */ 
/* 234 */     return new LifecycleMetadata(clazz, initMethods, destroyMethods);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream ois)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 244 */     ois.defaultReadObject();
/*     */ 
/* 247 */     this.logger = LogFactory.getLog(getClass());
/*     */   }
/*     */ 
/*     */   private static class LifecycleElement
/*     */   {
/*     */     private final Method method;
/*     */     private final String identifier;
/*     */ 
/*     */     public LifecycleElement(Method method)
/*     */     {
/* 347 */       if (method.getParameterTypes().length != 0) {
/* 348 */         throw new IllegalStateException("Lifecycle method annotation requires a no-arg method: " + method);
/*     */       }
/* 350 */       this.method = method;
/* 351 */       this.identifier = (Modifier.isPrivate(method.getModifiers()) ? method
/* 352 */         .getDeclaringClass() + "." + method.getName() : method.getName());
/*     */     }
/*     */ 
/*     */     public Method getMethod() {
/* 356 */       return this.method;
/*     */     }
/*     */ 
/*     */     public String getIdentifier() {
/* 360 */       return this.identifier;
/*     */     }
/*     */ 
/*     */     public void invoke(Object target) throws Throwable {
/* 364 */       ReflectionUtils.makeAccessible(this.method);
/* 365 */       this.method.invoke(target, (Object[])null);
/*     */     }
/*     */ 
/*     */     public boolean equals(Object other)
/*     */     {
/* 370 */       if (this == other) {
/* 371 */         return true;
/*     */       }
/* 373 */       if (!(other instanceof LifecycleElement)) {
/* 374 */         return false;
/*     */       }
/* 376 */       LifecycleElement otherElement = (LifecycleElement)other;
/* 377 */       return this.identifier.equals(otherElement.identifier);
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 382 */       return this.identifier.hashCode();
/*     */     }
/*     */   }
/*     */ 
/*     */   private class LifecycleMetadata
/*     */   {
/*     */     private final Class<?> targetClass;
/*     */     private final Collection<InitDestroyAnnotationBeanPostProcessor.LifecycleElement> initMethods;
/*     */     private final Collection<InitDestroyAnnotationBeanPostProcessor.LifecycleElement> destroyMethods;
/*     */     private volatile Set<InitDestroyAnnotationBeanPostProcessor.LifecycleElement> checkedInitMethods;
/*     */     private volatile Set<InitDestroyAnnotationBeanPostProcessor.LifecycleElement> checkedDestroyMethods;
/*     */ 
/*     */     public LifecycleMetadata(Collection<InitDestroyAnnotationBeanPostProcessor.LifecycleElement> targetClass, Collection<InitDestroyAnnotationBeanPostProcessor.LifecycleElement> initMethods)
/*     */     {
/* 269 */       this.targetClass = targetClass;
/* 270 */       this.initMethods = initMethods;
/* 271 */       this.destroyMethods = destroyMethods;
/*     */     }
/*     */ 
/*     */     public void checkConfigMembers(RootBeanDefinition beanDefinition) {
/* 275 */       Set checkedInitMethods = new LinkedHashSet(this.initMethods.size());
/* 276 */       for (Iterator localIterator = this.initMethods.iterator(); localIterator.hasNext(); ) { element = (InitDestroyAnnotationBeanPostProcessor.LifecycleElement)localIterator.next();
/* 277 */         String methodIdentifier = element.getIdentifier();
/* 278 */         if (!beanDefinition.isExternallyManagedInitMethod(methodIdentifier)) {
/* 279 */           beanDefinition.registerExternallyManagedInitMethod(methodIdentifier);
/* 280 */           checkedInitMethods.add(element);
/* 281 */           if (InitDestroyAnnotationBeanPostProcessor.this.logger.isDebugEnabled())
/* 282 */             InitDestroyAnnotationBeanPostProcessor.this.logger.debug("Registered init method on class [" + this.targetClass.getName() + "]: " + element);
/*     */         }
/*     */       }
/*     */       InitDestroyAnnotationBeanPostProcessor.LifecycleElement element;
/* 286 */       Object checkedDestroyMethods = new LinkedHashSet(this.destroyMethods.size());
/* 287 */       for (InitDestroyAnnotationBeanPostProcessor.LifecycleElement element : this.destroyMethods) {
/* 288 */         String methodIdentifier = element.getIdentifier();
/* 289 */         if (!beanDefinition.isExternallyManagedDestroyMethod(methodIdentifier)) {
/* 290 */           beanDefinition.registerExternallyManagedDestroyMethod(methodIdentifier);
/* 291 */           ((Set)checkedDestroyMethods).add(element);
/* 292 */           if (InitDestroyAnnotationBeanPostProcessor.this.logger.isDebugEnabled()) {
/* 293 */             InitDestroyAnnotationBeanPostProcessor.this.logger.debug("Registered destroy method on class [" + this.targetClass.getName() + "]: " + element);
/*     */           }
/*     */         }
/*     */       }
/* 297 */       this.checkedInitMethods = checkedInitMethods;
/* 298 */       this.checkedDestroyMethods = ((Set)checkedDestroyMethods);
/*     */     }
/*     */ 
/*     */     public void invokeInitMethods(Object target, String beanName) throws Throwable {
/* 302 */       Collection initMethodsToIterate = this.checkedInitMethods != null ? this.checkedInitMethods : this.initMethods;
/*     */       boolean debug;
/* 304 */       if (!initMethodsToIterate.isEmpty()) {
/* 305 */         debug = InitDestroyAnnotationBeanPostProcessor.this.logger.isDebugEnabled();
/* 306 */         for (InitDestroyAnnotationBeanPostProcessor.LifecycleElement element : initMethodsToIterate) {
/* 307 */           if (debug) {
/* 308 */             InitDestroyAnnotationBeanPostProcessor.this.logger.debug("Invoking init method on bean '" + beanName + "': " + element.getMethod());
/*     */           }
/* 310 */           element.invoke(target);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public void invokeDestroyMethods(Object target, String beanName) throws Throwable {
/* 316 */       Collection destroyMethodsToUse = this.checkedDestroyMethods != null ? this.checkedDestroyMethods : this.destroyMethods;
/*     */       boolean debug;
/* 318 */       if (!destroyMethodsToUse.isEmpty()) {
/* 319 */         debug = InitDestroyAnnotationBeanPostProcessor.this.logger.isDebugEnabled();
/* 320 */         for (InitDestroyAnnotationBeanPostProcessor.LifecycleElement element : destroyMethodsToUse) {
/* 321 */           if (debug) {
/* 322 */             InitDestroyAnnotationBeanPostProcessor.this.logger.debug("Invoking destroy method on bean '" + beanName + "': " + element.getMethod());
/*     */           }
/* 324 */           element.invoke(target);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean hasDestroyMethods() {
/* 330 */       Collection destroyMethodsToUse = this.checkedDestroyMethods != null ? this.checkedDestroyMethods : this.destroyMethods;
/*     */ 
/* 332 */       return !destroyMethodsToUse.isEmpty();
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.annotation.InitDestroyAnnotationBeanPostProcessor
 * JD-Core Version:    0.6.2
 */