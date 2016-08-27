/*     */ package org.springframework.beans.factory.annotation;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.PropertyValues;
/*     */ import org.springframework.beans.TypeConverter;
/*     */ import org.springframework.beans.factory.BeanCreationException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.InjectionPoint;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.UnsatisfiedDependencyException;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.config.DependencyDescriptor;
/*     */ import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
/*     */ import org.springframework.beans.factory.support.LookupOverride;
/*     */ import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
/*     */ import org.springframework.beans.factory.support.MethodOverrides;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.core.BridgeMethodResolver;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.PriorityOrdered;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.ReflectionUtils.FieldCallback;
/*     */ import org.springframework.util.ReflectionUtils.MethodCallback;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ public class AutowiredAnnotationBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter
/*     */   implements MergedBeanDefinitionPostProcessor, PriorityOrdered, BeanFactoryAware
/*     */ {
/* 120 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/* 122 */   private final Set<Class<? extends Annotation>> autowiredAnnotationTypes = new LinkedHashSet();
/*     */ 
/* 125 */   private String requiredParameterName = "required";
/*     */ 
/* 127 */   private boolean requiredParameterValue = true;
/*     */ 
/* 129 */   private int order = 2147483645;
/*     */   private ConfigurableListableBeanFactory beanFactory;
/* 134 */   private final Set<String> lookupMethodsChecked = Collections.newSetFromMap(new ConcurrentHashMap(256))
/* 134 */     ;
/*     */ 
/* 136 */   private final Map<Class<?>, Constructor<?>[]> candidateConstructorsCache = new ConcurrentHashMap(256);
/*     */ 
/* 139 */   private final Map<String, InjectionMetadata> injectionMetadataCache = new ConcurrentHashMap(256);
/*     */ 
/*     */   public AutowiredAnnotationBeanPostProcessor()
/*     */   {
/* 150 */     this.autowiredAnnotationTypes.add(Autowired.class);
/* 151 */     this.autowiredAnnotationTypes.add(Value.class);
/*     */     try {
/* 153 */       this.autowiredAnnotationTypes.add(
/* 154 */         ClassUtils.forName("javax.inject.Inject", AutowiredAnnotationBeanPostProcessor.class
/* 154 */         .getClassLoader()));
/* 155 */       this.logger.info("JSR-330 'javax.inject.Inject' annotation found and supported for autowiring");
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setAutowiredAnnotationType(Class<? extends Annotation> autowiredAnnotationType)
/*     */   {
/* 173 */     Assert.notNull(autowiredAnnotationType, "'autowiredAnnotationType' must not be null");
/* 174 */     this.autowiredAnnotationTypes.clear();
/* 175 */     this.autowiredAnnotationTypes.add(autowiredAnnotationType);
/*     */   }
/*     */ 
/*     */   public void setAutowiredAnnotationTypes(Set<Class<? extends Annotation>> autowiredAnnotationTypes)
/*     */   {
/* 188 */     Assert.notEmpty(autowiredAnnotationTypes, "'autowiredAnnotationTypes' must not be empty");
/* 189 */     this.autowiredAnnotationTypes.clear();
/* 190 */     this.autowiredAnnotationTypes.addAll(autowiredAnnotationTypes);
/*     */   }
/*     */ 
/*     */   public void setRequiredParameterName(String requiredParameterName)
/*     */   {
/* 199 */     this.requiredParameterName = requiredParameterName;
/*     */   }
/*     */ 
/*     */   public void setRequiredParameterValue(boolean requiredParameterValue)
/*     */   {
/* 210 */     this.requiredParameterValue = requiredParameterValue;
/*     */   }
/*     */ 
/*     */   public void setOrder(int order) {
/* 214 */     this.order = order;
/*     */   }
/*     */ 
/*     */   public int getOrder()
/*     */   {
/* 219 */     return this.order;
/*     */   }
/*     */ 
/*     */   public void setBeanFactory(BeanFactory beanFactory) throws BeansException
/*     */   {
/* 224 */     if (!(beanFactory instanceof ConfigurableListableBeanFactory)) {
/* 225 */       throw new IllegalArgumentException("AutowiredAnnotationBeanPostProcessor requires a ConfigurableListableBeanFactory");
/*     */     }
/*     */ 
/* 228 */     this.beanFactory = ((ConfigurableListableBeanFactory)beanFactory);
/*     */   }
/*     */ 
/*     */   public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName)
/*     */   {
/* 234 */     if (beanType != null) {
/* 235 */       InjectionMetadata metadata = findAutowiringMetadata(beanName, beanType, null);
/* 236 */       metadata.checkConfigMembers(beanDefinition);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, final String beanName) throws BeansException
/*     */   {
/* 242 */     if (!this.lookupMethodsChecked.contains(beanName)) {
/* 243 */       ReflectionUtils.doWithMethods(beanClass, new ReflectionUtils.MethodCallback()
/*     */       {
/*     */         public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
/* 246 */           Lookup lookup = (Lookup)method.getAnnotation(Lookup.class);
/* 247 */           if (lookup != null) {
/* 248 */             LookupOverride override = new LookupOverride(method, lookup.value());
/*     */             try {
/* 250 */               RootBeanDefinition mbd = (RootBeanDefinition)AutowiredAnnotationBeanPostProcessor.this.beanFactory.getMergedBeanDefinition(beanName);
/* 251 */               mbd.getMethodOverrides().addOverride(override);
/*     */             }
/*     */             catch (NoSuchBeanDefinitionException ex) {
/* 254 */               throw new BeanCreationException(beanName, "Cannot apply @Lookup to beans without corresponding bean definition");
/*     */             }
/*     */           }
/*     */         }
/*     */       });
/* 260 */       this.lookupMethodsChecked.add(beanName);
/*     */     }
/*     */ 
/* 264 */     Constructor[] candidateConstructors = (Constructor[])this.candidateConstructorsCache.get(beanClass);
/* 265 */     if (candidateConstructors == null) {
/* 266 */       synchronized (this.candidateConstructorsCache) {
/* 267 */         candidateConstructors = (Constructor[])this.candidateConstructorsCache.get(beanClass);
/* 268 */         if (candidateConstructors == null) {
/* 269 */           Constructor[] rawCandidates = beanClass.getDeclaredConstructors();
/* 270 */           List candidates = new ArrayList(rawCandidates.length);
/* 271 */           Constructor requiredConstructor = null;
/* 272 */           Constructor defaultConstructor = null;
/* 273 */           for (Constructor candidate : rawCandidates) {
/* 274 */             AnnotationAttributes ann = findAutowiredAnnotation(candidate);
/* 275 */             if (ann == null) {
/* 276 */               Class userClass = ClassUtils.getUserClass(beanClass);
/* 277 */               if (userClass != beanClass) {
/*     */                 try
/*     */                 {
/* 280 */                   Constructor superCtor = userClass
/* 280 */                     .getDeclaredConstructor(candidate
/* 280 */                     .getParameterTypes());
/* 281 */                   ann = findAutowiredAnnotation(superCtor);
/*     */                 }
/*     */                 catch (NoSuchMethodException localNoSuchMethodException)
/*     */                 {
/*     */                 }
/*     */               }
/*     */             }
/* 288 */             if (ann != null) {
/* 289 */               if (requiredConstructor != null) {
/* 290 */                 throw new BeanCreationException(beanName, "Invalid autowire-marked constructor: " + candidate + ". Found constructor with 'required' Autowired annotation already: " + requiredConstructor);
/*     */               }
/*     */ 
/* 295 */               if (candidate.getParameterTypes().length == 0) {
/* 296 */                 throw new IllegalStateException("Autowired annotation requires at least one argument: " + candidate);
/*     */               }
/*     */ 
/* 299 */               boolean required = determineRequiredStatus(ann);
/* 300 */               if (required) {
/* 301 */                 if (!candidates.isEmpty()) {
/* 302 */                   throw new BeanCreationException(beanName, "Invalid autowire-marked constructors: " + candidates + ". Found constructor with 'required' Autowired annotation: " + candidate);
/*     */                 }
/*     */ 
/* 307 */                 requiredConstructor = candidate;
/*     */               }
/* 309 */               candidates.add(candidate);
/*     */             }
/* 311 */             else if (candidate.getParameterTypes().length == 0) {
/* 312 */               defaultConstructor = candidate;
/*     */             }
/*     */           }
/* 315 */           if (!candidates.isEmpty())
/*     */           {
/* 317 */             if (requiredConstructor == null) {
/* 318 */               if (defaultConstructor != null) {
/* 319 */                 candidates.add(defaultConstructor);
/*     */               }
/* 321 */               else if ((candidates.size() == 1) && (this.logger.isWarnEnabled())) {
/* 322 */                 this.logger.warn("Inconsistent constructor declaration on bean with name '" + beanName + "': single autowire-marked constructor flagged as optional - this constructor " + "is effectively required since there is no default constructor to fall back to: " + candidates
/* 325 */                   .get(0));
/*     */               }
/*     */ 
/*     */             }
/*     */ 
/* 328 */             candidateConstructors = (Constructor[])candidates.toArray(new Constructor[candidates.size()]);
/*     */           }
/* 330 */           else if ((rawCandidates.length == 1) && (rawCandidates[0].getParameterTypes().length > 0)) {
/* 331 */             candidateConstructors = new Constructor[] { rawCandidates[0] };
/*     */           }
/*     */           else {
/* 334 */             candidateConstructors = new Constructor[0];
/*     */           }
/* 336 */           this.candidateConstructorsCache.put(beanClass, candidateConstructors);
/*     */         }
/*     */       }
/*     */     }
/* 340 */     return candidateConstructors.length > 0 ? candidateConstructors : null;
/*     */   }
/*     */ 
/*     */   public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName)
/*     */     throws BeansException
/*     */   {
/* 347 */     InjectionMetadata metadata = findAutowiringMetadata(beanName, bean.getClass(), pvs);
/*     */     try {
/* 349 */       metadata.inject(bean, beanName, pvs);
/*     */     }
/*     */     catch (BeanCreationException ex) {
/* 352 */       throw ex;
/*     */     }
/*     */     catch (Throwable ex) {
/* 355 */       throw new BeanCreationException(beanName, "Injection of autowired dependencies failed", ex);
/*     */     }
/* 357 */     return pvs;
/*     */   }
/*     */ 
/*     */   public void processInjection(Object bean)
/*     */     throws BeansException
/*     */   {
/* 367 */     Class clazz = bean.getClass();
/* 368 */     InjectionMetadata metadata = findAutowiringMetadata(clazz.getName(), clazz, null);
/*     */     try {
/* 370 */       metadata.inject(bean, null, null);
/*     */     }
/*     */     catch (BeanCreationException ex) {
/* 373 */       throw ex;
/*     */     }
/*     */     catch (Throwable ex) {
/* 376 */       throw new BeanCreationException("Injection of autowired dependencies failed for class [" + clazz + "]", ex);
/*     */     }
/*     */   }
/*     */ 
/*     */   private InjectionMetadata findAutowiringMetadata(String beanName, Class<?> clazz, PropertyValues pvs)
/*     */   {
/* 383 */     String cacheKey = StringUtils.hasLength(beanName) ? beanName : clazz.getName();
/*     */ 
/* 385 */     InjectionMetadata metadata = (InjectionMetadata)this.injectionMetadataCache.get(cacheKey);
/* 386 */     if (InjectionMetadata.needsRefresh(metadata, clazz)) {
/* 387 */       synchronized (this.injectionMetadataCache) {
/* 388 */         metadata = (InjectionMetadata)this.injectionMetadataCache.get(cacheKey);
/* 389 */         if (InjectionMetadata.needsRefresh(metadata, clazz)) {
/* 390 */           if (metadata != null)
/* 391 */             metadata.clear(pvs);
/*     */           try
/*     */           {
/* 394 */             metadata = buildAutowiringMetadata(clazz);
/* 395 */             this.injectionMetadataCache.put(cacheKey, metadata);
/*     */           }
/*     */           catch (NoClassDefFoundError err) {
/* 398 */             throw new IllegalStateException("Failed to introspect bean class [" + clazz.getName() + "] for autowiring metadata: could not find class that it depends on", err);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 404 */     return metadata;
/*     */   }
/*     */ 
/*     */   private InjectionMetadata buildAutowiringMetadata(final Class<?> clazz) {
/* 408 */     LinkedList elements = new LinkedList();
/* 409 */     Class targetClass = clazz;
/*     */     do
/*     */     {
/* 412 */       final LinkedList currElements = new LinkedList();
/*     */ 
/* 415 */       ReflectionUtils.doWithLocalFields(targetClass, new ReflectionUtils.FieldCallback()
/*     */       {
/*     */         public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
/* 418 */           AnnotationAttributes ann = AutowiredAnnotationBeanPostProcessor.this.findAutowiredAnnotation(field);
/* 419 */           if (ann != null) {
/* 420 */             if (Modifier.isStatic(field.getModifiers())) {
/* 421 */               if (AutowiredAnnotationBeanPostProcessor.this.logger.isWarnEnabled()) {
/* 422 */                 AutowiredAnnotationBeanPostProcessor.this.logger.warn("Autowired annotation is not supported on static fields: " + field);
/*     */               }
/* 424 */               return;
/*     */             }
/* 426 */             boolean required = AutowiredAnnotationBeanPostProcessor.this.determineRequiredStatus(ann);
/* 427 */             currElements.add(new AutowiredAnnotationBeanPostProcessor.AutowiredFieldElement(AutowiredAnnotationBeanPostProcessor.this, field, required));
/*     */           }
/*     */         }
/*     */       });
/* 432 */       ReflectionUtils.doWithLocalMethods(targetClass, new ReflectionUtils.MethodCallback()
/*     */       {
/*     */         public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
/* 435 */           Method bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
/* 436 */           if (!BridgeMethodResolver.isVisibilityBridgeMethodPair(method, bridgedMethod)) {
/* 437 */             return;
/*     */           }
/* 439 */           AnnotationAttributes ann = AutowiredAnnotationBeanPostProcessor.this.findAutowiredAnnotation(bridgedMethod);
/* 440 */           if ((ann != null) && (method.equals(ClassUtils.getMostSpecificMethod(method, clazz)))) {
/* 441 */             if (Modifier.isStatic(method.getModifiers())) {
/* 442 */               if (AutowiredAnnotationBeanPostProcessor.this.logger.isWarnEnabled()) {
/* 443 */                 AutowiredAnnotationBeanPostProcessor.this.logger.warn("Autowired annotation is not supported on static methods: " + method);
/*     */               }
/* 445 */               return;
/*     */             }
/* 447 */             if ((method.getParameterTypes().length == 0) && 
/* 448 */               (AutowiredAnnotationBeanPostProcessor.this.logger.isWarnEnabled())) {
/* 449 */               AutowiredAnnotationBeanPostProcessor.this.logger.warn("Autowired annotation should be used on methods with parameters: " + method);
/*     */             }
/*     */ 
/* 452 */             boolean required = AutowiredAnnotationBeanPostProcessor.this.determineRequiredStatus(ann);
/* 453 */             PropertyDescriptor pd = BeanUtils.findPropertyForMethod(bridgedMethod, clazz);
/* 454 */             currElements.add(new AutowiredAnnotationBeanPostProcessor.AutowiredMethodElement(AutowiredAnnotationBeanPostProcessor.this, method, required, pd));
/*     */           }
/*     */         }
/*     */       });
/* 459 */       elements.addAll(0, currElements);
/* 460 */       targetClass = targetClass.getSuperclass();
/*     */     }
/* 462 */     while ((targetClass != null) && (targetClass != Object.class));
/*     */ 
/* 464 */     return new InjectionMetadata(clazz, elements);
/*     */   }
/*     */ 
/*     */   private AnnotationAttributes findAutowiredAnnotation(AccessibleObject ao) {
/* 468 */     if (ao.getAnnotations().length > 0) {
/* 469 */       for (Class type : this.autowiredAnnotationTypes) {
/* 470 */         AnnotationAttributes attributes = AnnotatedElementUtils.getMergedAnnotationAttributes(ao, type);
/* 471 */         if (attributes != null) {
/* 472 */           return attributes;
/*     */         }
/*     */       }
/*     */     }
/* 476 */     return null;
/*     */   }
/*     */ 
/*     */   protected boolean determineRequiredStatus(AnnotationAttributes ann)
/*     */   {
/* 489 */     return (!ann.containsKey(this.requiredParameterName)) || 
/* 489 */       (this.requiredParameterValue == ann
/* 489 */       .getBoolean(this.requiredParameterName));
/*     */   }
/*     */ 
/*     */   protected <T> Map<String, T> findAutowireCandidates(Class<T> type)
/*     */     throws BeansException
/*     */   {
/* 499 */     if (this.beanFactory == null) {
/* 500 */       throw new IllegalStateException("No BeanFactory configured - override the getBeanOfType method or specify the 'beanFactory' property");
/*     */     }
/*     */ 
/* 503 */     return BeanFactoryUtils.beansOfTypeIncludingAncestors(this.beanFactory, type);
/*     */   }
/*     */ 
/*     */   private void registerDependentBeans(String beanName, Set<String> autowiredBeanNames)
/*     */   {
/* 510 */     if (beanName != null)
/* 511 */       for (String autowiredBeanName : autowiredBeanNames) {
/* 512 */         if (this.beanFactory.containsBean(autowiredBeanName)) {
/* 513 */           this.beanFactory.registerDependentBean(autowiredBeanName, beanName);
/*     */         }
/* 515 */         if (this.logger.isDebugEnabled())
/* 516 */           this.logger.debug("Autowiring by type from bean name '" + beanName + "' to bean named '" + autowiredBeanName + "'");
/*     */       }
/*     */   }
/*     */ 
/*     */   private Object resolvedCachedArgument(String beanName, Object cachedArgument)
/*     */   {
/* 527 */     if ((cachedArgument instanceof DependencyDescriptor)) {
/* 528 */       DependencyDescriptor descriptor = (DependencyDescriptor)cachedArgument;
/* 529 */       return this.beanFactory.resolveDependency(descriptor, beanName, null, null);
/*     */     }
/*     */ 
/* 532 */     return cachedArgument;
/*     */   }
/*     */ 
/*     */   private static class ShortcutDependencyDescriptor extends DependencyDescriptor
/*     */   {
/*     */     private final String shortcutName;
/*     */     private final Class<?> requiredType;
/*     */ 
/*     */     public ShortcutDependencyDescriptor(DependencyDescriptor original, String shortcutName, Class<?> requiredType)
/*     */     {
/* 714 */       super();
/* 715 */       this.shortcutName = shortcutName;
/* 716 */       this.requiredType = requiredType;
/*     */     }
/*     */ 
/*     */     public Object resolveShortcut(BeanFactory beanFactory)
/*     */     {
/* 721 */       return resolveCandidate(this.shortcutName, this.requiredType, beanFactory);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class AutowiredMethodElement extends InjectionMetadata.InjectedElement
/*     */   {
/*     */     private final boolean required;
/* 608 */     private volatile boolean cached = false;
/*     */     private volatile Object[] cachedMethodArguments;
/*     */ 
/*     */     public AutowiredMethodElement(Method method, boolean required, PropertyDescriptor pd)
/*     */     {
/* 613 */       super(pd);
/* 614 */       this.required = required;
/*     */     }
/*     */ 
/*     */     protected void inject(Object bean, String beanName, PropertyValues pvs) throws Throwable
/*     */     {
/* 619 */       if (checkPropertySkipping(pvs)) {
/* 620 */         return;
/*     */       }
/* 622 */       Method method = (Method)this.member;
/*     */       Object[] arguments;
/*     */       Object[] arguments;
/* 624 */       if (this.cached)
/*     */       {
/* 626 */         arguments = resolveCachedArguments(beanName);
/*     */       }
/*     */       else {
/* 629 */         Class[] paramTypes = method.getParameterTypes();
/* 630 */         arguments = new Object[paramTypes.length];
/* 631 */         DependencyDescriptor[] descriptors = new DependencyDescriptor[paramTypes.length];
/* 632 */         Set autowiredBeanNames = new LinkedHashSet(paramTypes.length);
/* 633 */         TypeConverter typeConverter = AutowiredAnnotationBeanPostProcessor.this.beanFactory.getTypeConverter();
/* 634 */         for (int i = 0; i < arguments.length; i++) {
/* 635 */           MethodParameter methodParam = new MethodParameter(method, i);
/* 636 */           DependencyDescriptor currDesc = new DependencyDescriptor(methodParam, this.required);
/* 637 */           currDesc.setContainingClass(bean.getClass());
/* 638 */           descriptors[i] = currDesc;
/*     */           try {
/* 640 */             Object arg = AutowiredAnnotationBeanPostProcessor.this.beanFactory.resolveDependency(currDesc, beanName, autowiredBeanNames, typeConverter);
/* 641 */             if ((arg == null) && (!this.required)) {
/* 642 */               arguments = null;
/* 643 */               break;
/*     */             }
/* 645 */             arguments[i] = arg;
/*     */           }
/*     */           catch (BeansException ex) {
/* 648 */             throw new UnsatisfiedDependencyException(null, beanName, new InjectionPoint(methodParam), ex);
/*     */           }
/*     */         }
/* 651 */         synchronized (this) {
/* 652 */           if (!this.cached) {
/* 653 */             if (arguments != null) {
/* 654 */               this.cachedMethodArguments = new Object[paramTypes.length];
/* 655 */               for (int i = 0; i < arguments.length; i++) {
/* 656 */                 this.cachedMethodArguments[i] = descriptors[i];
/*     */               }
/* 658 */               AutowiredAnnotationBeanPostProcessor.this.registerDependentBeans(beanName, autowiredBeanNames);
/* 659 */               if (autowiredBeanNames.size() == paramTypes.length) {
/* 660 */                 Iterator it = autowiredBeanNames.iterator();
/* 661 */                 for (int i = 0; i < paramTypes.length; i++) {
/* 662 */                   String autowiredBeanName = (String)it.next();
/* 663 */                   if ((AutowiredAnnotationBeanPostProcessor.this.beanFactory.containsBean(autowiredBeanName)) && 
/* 664 */                     (AutowiredAnnotationBeanPostProcessor.this.beanFactory.isTypeMatch(autowiredBeanName, paramTypes[i]))) {
/* 665 */                     this.cachedMethodArguments[i] = new AutowiredAnnotationBeanPostProcessor.ShortcutDependencyDescriptor(descriptors[i], autowiredBeanName, paramTypes[i]);
/*     */                   }
/*     */                 }
/*     */               }
/*     */ 
/*     */             }
/*     */             else
/*     */             {
/* 673 */               this.cachedMethodArguments = null;
/*     */             }
/* 675 */             this.cached = true;
/*     */           }
/*     */         }
/*     */       }
/* 679 */       if (arguments != null)
/*     */         try {
/* 681 */           ReflectionUtils.makeAccessible(method);
/* 682 */           method.invoke(bean, arguments);
/*     */         }
/*     */         catch (InvocationTargetException ex) {
/* 685 */           throw ex.getTargetException();
/*     */         }
/*     */     }
/*     */ 
/*     */     private Object[] resolveCachedArguments(String beanName)
/*     */     {
/* 691 */       if (this.cachedMethodArguments == null) {
/* 692 */         return null;
/*     */       }
/* 694 */       Object[] arguments = new Object[this.cachedMethodArguments.length];
/* 695 */       for (int i = 0; i < arguments.length; i++) {
/* 696 */         arguments[i] = AutowiredAnnotationBeanPostProcessor.this.resolvedCachedArgument(beanName, this.cachedMethodArguments[i]);
/*     */       }
/* 698 */       return arguments;
/*     */     }
/*     */   }
/*     */ 
/*     */   private class AutowiredFieldElement extends InjectionMetadata.InjectedElement
/*     */   {
/*     */     private final boolean required;
/* 544 */     private volatile boolean cached = false;
/*     */     private volatile Object cachedFieldValue;
/*     */ 
/*     */     public AutowiredFieldElement(Field field, boolean required)
/*     */     {
/* 549 */       super(null);
/* 550 */       this.required = required;
/*     */     }
/*     */ 
/*     */     protected void inject(Object bean, String beanName, PropertyValues pvs) throws Throwable
/*     */     {
/* 555 */       Field field = (Field)this.member;
/*     */       Object value;
/*     */       Object value;
/* 557 */       if (this.cached) {
/* 558 */         value = AutowiredAnnotationBeanPostProcessor.this.resolvedCachedArgument(beanName, this.cachedFieldValue);
/*     */       }
/*     */       else {
/* 561 */         DependencyDescriptor desc = new DependencyDescriptor(field, this.required);
/* 562 */         desc.setContainingClass(bean.getClass());
/* 563 */         Set autowiredBeanNames = new LinkedHashSet(1);
/* 564 */         TypeConverter typeConverter = AutowiredAnnotationBeanPostProcessor.this.beanFactory.getTypeConverter();
/*     */         try {
/* 566 */           value = AutowiredAnnotationBeanPostProcessor.this.beanFactory.resolveDependency(desc, beanName, autowiredBeanNames, typeConverter);
/*     */         }
/*     */         catch (BeansException ex)
/*     */         {
/*     */           Object value;
/* 569 */           throw new UnsatisfiedDependencyException(null, beanName, new InjectionPoint(field), ex);
/*     */         }
/* 571 */         synchronized (this) {
/* 572 */           if (!this.cached) {
/* 573 */             if ((value != null) || (this.required)) {
/* 574 */               this.cachedFieldValue = desc;
/* 575 */               AutowiredAnnotationBeanPostProcessor.this.registerDependentBeans(beanName, autowiredBeanNames);
/* 576 */               if (autowiredBeanNames.size() == 1) {
/* 577 */                 String autowiredBeanName = (String)autowiredBeanNames.iterator().next();
/* 578 */                 if ((AutowiredAnnotationBeanPostProcessor.this.beanFactory.containsBean(autowiredBeanName)) && 
/* 579 */                   (AutowiredAnnotationBeanPostProcessor.this.beanFactory.isTypeMatch(autowiredBeanName, field.getType()))) {
/* 580 */                   this.cachedFieldValue = new AutowiredAnnotationBeanPostProcessor.ShortcutDependencyDescriptor(desc, autowiredBeanName, field
/* 581 */                     .getType());
/*     */                 }
/*     */               }
/*     */             }
/*     */             else
/*     */             {
/* 587 */               this.cachedFieldValue = null;
/*     */             }
/* 589 */             this.cached = true;
/*     */           }
/*     */         }
/*     */       }
/* 593 */       if (value != null) {
/* 594 */         ReflectionUtils.makeAccessible(field);
/* 595 */         field.set(bean, value);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor
 * JD-Core Version:    0.6.2
 */