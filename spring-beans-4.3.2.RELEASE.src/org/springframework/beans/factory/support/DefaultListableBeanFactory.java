/*      */ package org.springframework.beans.factory.support;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.NotSerializableException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectStreamException;
/*      */ import java.io.Serializable;
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.lang.ref.Reference;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.lang.reflect.Method;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.IdentityHashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import javax.inject.Provider;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.springframework.beans.BeansException;
/*      */ import org.springframework.beans.TypeConverter;
/*      */ import org.springframework.beans.factory.BeanCreationException;
/*      */ import org.springframework.beans.factory.BeanCurrentlyInCreationException;
/*      */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*      */ import org.springframework.beans.factory.BeanFactory;
/*      */ import org.springframework.beans.factory.BeanFactoryAware;
/*      */ import org.springframework.beans.factory.BeanFactoryUtils;
/*      */ import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
/*      */ import org.springframework.beans.factory.CannotLoadBeanClassException;
/*      */ import org.springframework.beans.factory.FactoryBean;
/*      */ import org.springframework.beans.factory.InjectionPoint;
/*      */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*      */ import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
/*      */ import org.springframework.beans.factory.ObjectFactory;
/*      */ import org.springframework.beans.factory.ObjectProvider;
/*      */ import org.springframework.beans.factory.SmartFactoryBean;
/*      */ import org.springframework.beans.factory.SmartInitializingSingleton;
/*      */ import org.springframework.beans.factory.config.BeanDefinition;
/*      */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*      */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*      */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*      */ import org.springframework.beans.factory.config.DependencyDescriptor;
/*      */ import org.springframework.core.OrderComparator;
/*      */ import org.springframework.core.OrderComparator.OrderSourceProvider;
/*      */ import org.springframework.core.ResolvableType;
/*      */ import org.springframework.core.annotation.AnnotationUtils;
/*      */ import org.springframework.lang.UsesJava8;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.CompositeIterator;
/*      */ import org.springframework.util.ObjectUtils;
/*      */ import org.springframework.util.StringUtils;
/*      */ 
/*      */ public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory
/*      */   implements ConfigurableListableBeanFactory, BeanDefinitionRegistry, Serializable
/*      */ {
/*  118 */   private static Class<?> javaUtilOptionalClass = null;
/*      */ 
/*  120 */   private static Class<?> javaxInjectProviderClass = null;
/*      */ 
/*  141 */   private static final Map<String, Reference<DefaultListableBeanFactory>> serializableFactories = new ConcurrentHashMap(8);
/*      */   private String serializationId;
/*  148 */   private boolean allowBeanDefinitionOverriding = true;
/*      */ 
/*  151 */   private boolean allowEagerClassLoading = true;
/*      */   private Comparator<Object> dependencyComparator;
/*  157 */   private AutowireCandidateResolver autowireCandidateResolver = new SimpleAutowireCandidateResolver();
/*      */ 
/*  160 */   private final Map<Class<?>, Object> resolvableDependencies = new ConcurrentHashMap(16);
/*      */ 
/*  163 */   private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap(256);
/*      */ 
/*  166 */   private final Map<Class<?>, String[]> allBeanNamesByType = new ConcurrentHashMap(64);
/*      */ 
/*  169 */   private final Map<Class<?>, String[]> singletonBeanNamesByType = new ConcurrentHashMap(64);
/*      */ 
/*  172 */   private volatile List<String> beanDefinitionNames = new ArrayList(256);
/*      */ 
/*  175 */   private volatile Set<String> manualSingletonNames = new LinkedHashSet(16);
/*      */   private volatile String[] frozenBeanDefinitionNames;
/*  181 */   private volatile boolean configurationFrozen = false;
/*      */ 
/*      */   public DefaultListableBeanFactory()
/*      */   {
/*      */   }
/*      */ 
/*      */   public DefaultListableBeanFactory(BeanFactory parentBeanFactory)
/*      */   {
/*  196 */     super(parentBeanFactory);
/*      */   }
/*      */ 
/*      */   public void setSerializationId(String serializationId)
/*      */   {
/*  205 */     if (serializationId != null) {
/*  206 */       serializableFactories.put(serializationId, new WeakReference(this));
/*      */     }
/*  208 */     else if (this.serializationId != null) {
/*  209 */       serializableFactories.remove(this.serializationId);
/*      */     }
/*  211 */     this.serializationId = serializationId;
/*      */   }
/*      */ 
/*      */   public String getSerializationId()
/*      */   {
/*  220 */     return this.serializationId;
/*      */   }
/*      */ 
/*      */   public void setAllowBeanDefinitionOverriding(boolean allowBeanDefinitionOverriding)
/*      */   {
/*  231 */     this.allowBeanDefinitionOverriding = allowBeanDefinitionOverriding;
/*      */   }
/*      */ 
/*      */   public boolean isAllowBeanDefinitionOverriding()
/*      */   {
/*  240 */     return this.allowBeanDefinitionOverriding;
/*      */   }
/*      */ 
/*      */   public void setAllowEagerClassLoading(boolean allowEagerClassLoading)
/*      */   {
/*  254 */     this.allowEagerClassLoading = allowEagerClassLoading;
/*      */   }
/*      */ 
/*      */   public boolean isAllowEagerClassLoading()
/*      */   {
/*  263 */     return this.allowEagerClassLoading;
/*      */   }
/*      */ 
/*      */   public void setDependencyComparator(Comparator<Object> dependencyComparator)
/*      */   {
/*  272 */     this.dependencyComparator = dependencyComparator;
/*      */   }
/*      */ 
/*      */   public Comparator<Object> getDependencyComparator()
/*      */   {
/*  279 */     return this.dependencyComparator;
/*      */   }
/*      */ 
/*      */   public void setAutowireCandidateResolver(final AutowireCandidateResolver autowireCandidateResolver)
/*      */   {
/*  288 */     Assert.notNull(autowireCandidateResolver, "AutowireCandidateResolver must not be null");
/*  289 */     if ((autowireCandidateResolver instanceof BeanFactoryAware)) {
/*  290 */       if (System.getSecurityManager() != null) {
/*  291 */         final BeanFactory target = this;
/*  292 */         AccessController.doPrivileged(new PrivilegedAction()
/*      */         {
/*      */           public Object run() {
/*  295 */             ((BeanFactoryAware)autowireCandidateResolver).setBeanFactory(target);
/*  296 */             return null;
/*      */           }
/*      */         }
/*      */         , getAccessControlContext());
/*      */       }
/*      */       else {
/*  301 */         ((BeanFactoryAware)autowireCandidateResolver).setBeanFactory(this);
/*      */       }
/*      */     }
/*  304 */     this.autowireCandidateResolver = autowireCandidateResolver;
/*      */   }
/*      */ 
/*      */   public AutowireCandidateResolver getAutowireCandidateResolver()
/*      */   {
/*  311 */     return this.autowireCandidateResolver;
/*      */   }
/*      */ 
/*      */   public void copyConfigurationFrom(ConfigurableBeanFactory otherFactory)
/*      */   {
/*  317 */     super.copyConfigurationFrom(otherFactory);
/*  318 */     if ((otherFactory instanceof DefaultListableBeanFactory)) {
/*  319 */       DefaultListableBeanFactory otherListableFactory = (DefaultListableBeanFactory)otherFactory;
/*  320 */       this.allowBeanDefinitionOverriding = otherListableFactory.allowBeanDefinitionOverriding;
/*  321 */       this.allowEagerClassLoading = otherListableFactory.allowEagerClassLoading;
/*  322 */       this.autowireCandidateResolver = otherListableFactory.autowireCandidateResolver;
/*  323 */       this.resolvableDependencies.putAll(otherListableFactory.resolvableDependencies);
/*      */     }
/*      */   }
/*      */ 
/*      */   public <T> T getBean(Class<T> requiredType)
/*      */     throws BeansException
/*      */   {
/*  334 */     return getBean(requiredType, (Object[])null);
/*      */   }
/*      */ 
/*      */   public <T> T getBean(Class<T> requiredType, Object[] args) throws BeansException
/*      */   {
/*  339 */     Assert.notNull(requiredType, "Required type must not be null");
/*  340 */     String[] beanNames = getBeanNamesForType(requiredType);
/*  341 */     if (beanNames.length > 1) {
/*  342 */       ArrayList autowireCandidates = new ArrayList();
/*  343 */       for (String beanName : beanNames) {
/*  344 */         if ((!containsBeanDefinition(beanName)) || (getBeanDefinition(beanName).isAutowireCandidate())) {
/*  345 */           autowireCandidates.add(beanName);
/*      */         }
/*      */       }
/*  348 */       if (autowireCandidates.size() > 0) {
/*  349 */         beanNames = (String[])autowireCandidates.toArray(new String[autowireCandidates.size()]);
/*      */       }
/*      */     }
/*  352 */     if (beanNames.length == 1) {
/*  353 */       return getBean(beanNames[0], requiredType, args);
/*      */     }
/*  355 */     if (beanNames.length > 1) {
/*  356 */       Map candidates = new HashMap();
/*  357 */       for (String beanName : beanNames) {
/*  358 */         candidates.put(beanName, getBean(beanName, requiredType, args));
/*      */       }
/*  360 */       String primaryCandidate = determinePrimaryCandidate(candidates, requiredType);
/*  361 */       if (primaryCandidate != null) {
/*  362 */         return getBean(primaryCandidate, requiredType, args);
/*      */       }
/*  364 */       String priorityCandidate = determineHighestPriorityCandidate(candidates, requiredType);
/*  365 */       if (priorityCandidate != null) {
/*  366 */         return getBean(priorityCandidate, requiredType, args);
/*      */       }
/*  368 */       throw new NoUniqueBeanDefinitionException(requiredType, candidates.keySet());
/*      */     }
/*  370 */     if (getParentBeanFactory() != null) {
/*  371 */       return getParentBeanFactory().getBean(requiredType, args);
/*      */     }
/*      */ 
/*  374 */     throw new NoSuchBeanDefinitionException(requiredType);
/*      */   }
/*      */ 
/*      */   public boolean containsBeanDefinition(String beanName)
/*      */   {
/*  385 */     Assert.notNull(beanName, "Bean name must not be null");
/*  386 */     return this.beanDefinitionMap.containsKey(beanName);
/*      */   }
/*      */ 
/*      */   public int getBeanDefinitionCount()
/*      */   {
/*  391 */     return this.beanDefinitionMap.size();
/*      */   }
/*      */ 
/*      */   public String[] getBeanDefinitionNames()
/*      */   {
/*  396 */     if (this.frozenBeanDefinitionNames != null) {
/*  397 */       return this.frozenBeanDefinitionNames;
/*      */     }
/*      */ 
/*  400 */     return StringUtils.toStringArray(this.beanDefinitionNames);
/*      */   }
/*      */ 
/*      */   public String[] getBeanNamesForType(ResolvableType type)
/*      */   {
/*  406 */     return doGetBeanNamesForType(type, true, true);
/*      */   }
/*      */ 
/*      */   public String[] getBeanNamesForType(Class<?> type)
/*      */   {
/*  411 */     return getBeanNamesForType(type, true, true);
/*      */   }
/*      */ 
/*      */   public String[] getBeanNamesForType(Class<?> type, boolean includeNonSingletons, boolean allowEagerInit)
/*      */   {
/*  416 */     if ((!isConfigurationFrozen()) || (type == null) || (!allowEagerInit)) {
/*  417 */       return doGetBeanNamesForType(ResolvableType.forRawClass(type), includeNonSingletons, allowEagerInit);
/*      */     }
/*  419 */     Map cache = includeNonSingletons ? this.allBeanNamesByType : this.singletonBeanNamesByType;
/*      */ 
/*  421 */     String[] resolvedBeanNames = (String[])cache.get(type);
/*  422 */     if (resolvedBeanNames != null) {
/*  423 */       return resolvedBeanNames;
/*      */     }
/*  425 */     resolvedBeanNames = doGetBeanNamesForType(ResolvableType.forRawClass(type), includeNonSingletons, true);
/*  426 */     if (ClassUtils.isCacheSafe(type, getBeanClassLoader())) {
/*  427 */       cache.put(type, resolvedBeanNames);
/*      */     }
/*  429 */     return resolvedBeanNames;
/*      */   }
/*      */ 
/*      */   private String[] doGetBeanNamesForType(ResolvableType type, boolean includeNonSingletons, boolean allowEagerInit) {
/*  433 */     List result = new ArrayList();
/*      */ 
/*  436 */     for (String beanName : this.beanDefinitionNames)
/*      */     {
/*  439 */       if (!isAlias(beanName)) {
/*      */         try {
/*  441 */           RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/*      */ 
/*  443 */           if ((!mbd.isAbstract()) && ((allowEagerInit) || (
/*  444 */             ((mbd
/*  444 */             .hasBeanClass()) || (!mbd.isLazyInit()) || (isAllowEagerClassLoading())) && 
/*  445 */             (!requiresEagerInitForType(mbd
/*  445 */             .getFactoryBeanName())))))
/*      */           {
/*  447 */             boolean isFactoryBean = isFactoryBean(beanName, mbd);
/*      */ 
/*  449 */             boolean matchFound = ((allowEagerInit) || (!isFactoryBean) || (containsSingleton(beanName))) && ((includeNonSingletons) || 
/*  449 */               (isSingleton(beanName))) && 
/*  449 */               (isTypeMatch(beanName, type));
/*  450 */             if ((!matchFound) && (isFactoryBean))
/*      */             {
/*  452 */               beanName = new StringBuilder().append("&").append(beanName).toString();
/*  453 */               matchFound = ((includeNonSingletons) || (mbd.isSingleton())) && (isTypeMatch(beanName, type));
/*      */             }
/*  455 */             if (matchFound)
/*  456 */               result.add(beanName);
/*      */           }
/*      */         }
/*      */         catch (CannotLoadBeanClassException ex)
/*      */         {
/*  461 */           if (allowEagerInit) {
/*  462 */             throw ex;
/*      */           }
/*      */ 
/*  465 */           if (this.logger.isDebugEnabled()) {
/*  466 */             this.logger.debug(new StringBuilder().append("Ignoring bean class loading failure for bean '").append(beanName).append("'").toString(), ex);
/*      */           }
/*  468 */           onSuppressedException(ex);
/*      */         }
/*      */         catch (BeanDefinitionStoreException ex) {
/*  471 */           if (allowEagerInit) {
/*  472 */             throw ex;
/*      */           }
/*      */ 
/*  475 */           if (this.logger.isDebugEnabled()) {
/*  476 */             this.logger.debug(new StringBuilder().append("Ignoring unresolvable metadata in bean definition '").append(beanName).append("'").toString(), ex);
/*      */           }
/*  478 */           onSuppressedException(ex);
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  484 */     for (String beanName : this.manualSingletonNames) {
/*      */       try
/*      */       {
/*  487 */         if (isFactoryBean(beanName)) {
/*  488 */           if (((includeNonSingletons) || (isSingleton(beanName))) && (isTypeMatch(beanName, type))) {
/*  489 */             result.add(beanName);
/*      */           }
/*      */           else
/*      */           {
/*  494 */             beanName = new StringBuilder().append("&").append(beanName).toString();
/*      */           }
/*      */         }
/*  497 */         else if (isTypeMatch(beanName, type)) {
/*  498 */           result.add(beanName);
/*      */         }
/*      */       }
/*      */       catch (NoSuchBeanDefinitionException ex)
/*      */       {
/*  503 */         if (this.logger.isDebugEnabled()) {
/*  504 */           this.logger.debug(new StringBuilder().append("Failed to check manually registered singleton with name '").append(beanName).append("'").toString(), ex);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  509 */     return StringUtils.toStringArray(result);
/*      */   }
/*      */ 
/*      */   private boolean requiresEagerInitForType(String factoryBeanName)
/*      */   {
/*  520 */     return (factoryBeanName != null) && (isFactoryBean(factoryBeanName)) && (!containsSingleton(factoryBeanName));
/*      */   }
/*      */ 
/*      */   public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException
/*      */   {
/*  525 */     return getBeansOfType(type, true, true);
/*      */   }
/*      */ 
/*      */   public <T> Map<String, T> getBeansOfType(Class<T> type, boolean includeNonSingletons, boolean allowEagerInit)
/*      */     throws BeansException
/*      */   {
/*  532 */     String[] beanNames = getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
/*  533 */     Map result = new LinkedHashMap(beanNames.length);
/*  534 */     for (String beanName : beanNames) {
/*      */       try {
/*  536 */         result.put(beanName, getBean(beanName, type));
/*      */       }
/*      */       catch (BeanCreationException ex) {
/*  539 */         Throwable rootCause = ex.getMostSpecificCause();
/*  540 */         if ((rootCause instanceof BeanCurrentlyInCreationException)) {
/*  541 */           BeanCreationException bce = (BeanCreationException)rootCause;
/*  542 */           if (isCurrentlyInCreation(bce.getBeanName())) {
/*  543 */             if (this.logger.isDebugEnabled()) {
/*  544 */               this.logger.debug(new StringBuilder().append("Ignoring match to currently created bean '").append(beanName).append("': ")
/*  545 */                 .append(ex
/*  545 */                 .getMessage()).toString());
/*      */             }
/*  547 */             onSuppressedException(ex);
/*      */ 
/*  550 */             continue;
/*      */           }
/*      */         }
/*  553 */         throw ex;
/*      */       }
/*      */     }
/*  556 */     return result;
/*      */   }
/*      */ 
/*      */   public String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType)
/*      */   {
/*  561 */     List results = new ArrayList();
/*  562 */     for (String beanName : this.beanDefinitionNames) {
/*  563 */       BeanDefinition beanDefinition = getBeanDefinition(beanName);
/*  564 */       if ((!beanDefinition.isAbstract()) && (findAnnotationOnBean(beanName, annotationType) != null)) {
/*  565 */         results.add(beanName);
/*      */       }
/*      */     }
/*  568 */     for (String beanName : this.manualSingletonNames) {
/*  569 */       if ((!results.contains(beanName)) && (findAnnotationOnBean(beanName, annotationType) != null)) {
/*  570 */         results.add(beanName);
/*      */       }
/*      */     }
/*  573 */     return (String[])results.toArray(new String[results.size()]);
/*      */   }
/*      */ 
/*      */   public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType)
/*      */   {
/*  578 */     String[] beanNames = getBeanNamesForAnnotation(annotationType);
/*  579 */     Map results = new LinkedHashMap(beanNames.length);
/*  580 */     for (String beanName : beanNames) {
/*  581 */       results.put(beanName, getBean(beanName));
/*      */     }
/*  583 */     return results;
/*      */   }
/*      */ 
/*      */   public <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType)
/*      */     throws NoSuchBeanDefinitionException
/*      */   {
/*  596 */     Annotation ann = null;
/*  597 */     Class beanType = getType(beanName);
/*  598 */     if (beanType != null) {
/*  599 */       ann = AnnotationUtils.findAnnotation(beanType, annotationType);
/*      */     }
/*  601 */     if ((ann == null) && (containsBeanDefinition(beanName))) {
/*  602 */       BeanDefinition bd = getMergedBeanDefinition(beanName);
/*  603 */       if ((bd instanceof AbstractBeanDefinition)) {
/*  604 */         AbstractBeanDefinition abd = (AbstractBeanDefinition)bd;
/*  605 */         if (abd.hasBeanClass()) {
/*  606 */           ann = AnnotationUtils.findAnnotation(abd.getBeanClass(), annotationType);
/*      */         }
/*      */       }
/*      */     }
/*  610 */     return ann;
/*      */   }
/*      */ 
/*      */   public void registerResolvableDependency(Class<?> dependencyType, Object autowiredValue)
/*      */   {
/*  620 */     Assert.notNull(dependencyType, "Dependency type must not be null");
/*  621 */     if (autowiredValue != null) {
/*  622 */       if ((!(autowiredValue instanceof ObjectFactory)) && (!dependencyType.isInstance(autowiredValue)))
/*      */       {
/*  624 */         throw new IllegalArgumentException(new StringBuilder().append("Value [").append(autowiredValue).append("] does not implement specified dependency type [")
/*  624 */           .append(dependencyType
/*  624 */           .getName()).append("]").toString());
/*      */       }
/*  626 */       this.resolvableDependencies.put(dependencyType, autowiredValue);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isAutowireCandidate(String beanName, DependencyDescriptor descriptor)
/*      */     throws NoSuchBeanDefinitionException
/*      */   {
/*  634 */     return isAutowireCandidate(beanName, descriptor, getAutowireCandidateResolver());
/*      */   }
/*      */ 
/*      */   protected boolean isAutowireCandidate(String beanName, DependencyDescriptor descriptor, AutowireCandidateResolver resolver)
/*      */     throws NoSuchBeanDefinitionException
/*      */   {
/*  648 */     String beanDefinitionName = BeanFactoryUtils.transformedBeanName(beanName);
/*  649 */     if (containsBeanDefinition(beanDefinitionName)) {
/*  650 */       return isAutowireCandidate(beanName, getMergedLocalBeanDefinition(beanDefinitionName), descriptor, resolver);
/*      */     }
/*  652 */     if (containsSingleton(beanName)) {
/*  653 */       return isAutowireCandidate(beanName, new RootBeanDefinition(getType(beanName)), descriptor, resolver);
/*      */     }
/*  655 */     if ((getParentBeanFactory() instanceof DefaultListableBeanFactory))
/*      */     {
/*  657 */       return ((DefaultListableBeanFactory)getParentBeanFactory()).isAutowireCandidate(beanName, descriptor, resolver);
/*      */     }
/*  659 */     if ((getParentBeanFactory() instanceof Serializable))
/*      */     {
/*  661 */       return ((Serializable)getParentBeanFactory()).isAutowireCandidate(beanName, descriptor);
/*      */     }
/*      */ 
/*  664 */     return true;
/*      */   }
/*      */ 
/*      */   protected boolean isAutowireCandidate(String beanName, RootBeanDefinition mbd, DependencyDescriptor descriptor, AutowireCandidateResolver resolver)
/*      */   {
/*  680 */     String beanDefinitionName = BeanFactoryUtils.transformedBeanName(beanName);
/*  681 */     resolveBeanClass(mbd, beanDefinitionName, new Class[0]);
/*  682 */     if (mbd.isFactoryMethodUnique)
/*      */     {
/*      */       boolean resolve;
/*  684 */       synchronized (mbd.constructorArgumentLock) {
/*  685 */         resolve = mbd.resolvedConstructorOrFactoryMethod == null;
/*      */       }
/*      */       boolean resolve;
/*  687 */       if (resolve) {
/*  688 */         new ConstructorResolver(this).resolveFactoryMethodIfPossible(mbd);
/*      */       }
/*      */     }
/*  691 */     return resolver.isAutowireCandidate(new BeanDefinitionHolder(mbd, beanName, 
/*  692 */       getAliases(beanDefinitionName)), 
/*  692 */       descriptor);
/*      */   }
/*      */ 
/*      */   public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException
/*      */   {
/*  697 */     BeanDefinition bd = (BeanDefinition)this.beanDefinitionMap.get(beanName);
/*  698 */     if (bd == null) {
/*  699 */       if (this.logger.isTraceEnabled()) {
/*  700 */         this.logger.trace(new StringBuilder().append("No bean named '").append(beanName).append("' found in ").append(this).toString());
/*      */       }
/*  702 */       throw new NoSuchBeanDefinitionException(beanName);
/*      */     }
/*  704 */     return bd;
/*      */   }
/*      */ 
/*      */   public Iterator<String> getBeanNamesIterator()
/*      */   {
/*  709 */     CompositeIterator iterator = new CompositeIterator();
/*  710 */     iterator.add(this.beanDefinitionNames.iterator());
/*  711 */     iterator.add(this.manualSingletonNames.iterator());
/*  712 */     return iterator;
/*      */   }
/*      */ 
/*      */   public void clearMetadataCache()
/*      */   {
/*  717 */     super.clearMetadataCache();
/*  718 */     clearByTypeCache();
/*      */   }
/*      */ 
/*      */   public void freezeConfiguration()
/*      */   {
/*  723 */     this.configurationFrozen = true;
/*  724 */     this.frozenBeanDefinitionNames = StringUtils.toStringArray(this.beanDefinitionNames);
/*      */   }
/*      */ 
/*      */   public boolean isConfigurationFrozen()
/*      */   {
/*  729 */     return this.configurationFrozen;
/*      */   }
/*      */ 
/*      */   protected boolean isBeanEligibleForMetadataCaching(String beanName)
/*      */   {
/*  739 */     return (this.configurationFrozen) || (super.isBeanEligibleForMetadataCaching(beanName));
/*      */   }
/*      */ 
/*      */   public void preInstantiateSingletons() throws BeansException
/*      */   {
/*  744 */     if (this.logger.isDebugEnabled()) {
/*  745 */       this.logger.debug(new StringBuilder().append("Pre-instantiating singletons in ").append(this).toString());
/*      */     }
/*      */ 
/*  750 */     List beanNames = new ArrayList(this.beanDefinitionNames);
/*      */ 
/*  753 */     for (String beanName : beanNames) {
/*  754 */       RootBeanDefinition bd = getMergedLocalBeanDefinition(beanName);
/*  755 */       if ((!bd.isAbstract()) && (bd.isSingleton()) && (!bd.isLazyInit())) {
/*  756 */         if (isFactoryBean(beanName)) {
/*  757 */           final FactoryBean factory = (FactoryBean)getBean(new StringBuilder().append("&").append(beanName).toString());
/*      */           boolean isEagerInit;
/*      */           boolean isEagerInit;
/*  759 */           if ((System.getSecurityManager() != null) && ((factory instanceof SmartFactoryBean))) {
/*  760 */             isEagerInit = ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*      */             {
/*      */               public Boolean run() {
/*  763 */                 return Boolean.valueOf(((SmartFactoryBean)factory).isEagerInit());
/*      */               }
/*      */             }
/*      */             , getAccessControlContext())).booleanValue();
/*      */           }
/*      */           else
/*      */           {
/*  769 */             isEagerInit = ((factory instanceof SmartFactoryBean)) && 
/*  769 */               (((SmartFactoryBean)factory)
/*  769 */               .isEagerInit());
/*      */           }
/*  771 */           if (isEagerInit)
/*  772 */             getBean(beanName);
/*      */         }
/*      */         else
/*      */         {
/*  776 */           getBean(beanName);
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  782 */     for (String beanName : beanNames) {
/*  783 */       Object singletonInstance = getSingleton(beanName);
/*  784 */       if ((singletonInstance instanceof SmartInitializingSingleton)) {
/*  785 */         final SmartInitializingSingleton smartSingleton = (SmartInitializingSingleton)singletonInstance;
/*  786 */         if (System.getSecurityManager() != null) {
/*  787 */           AccessController.doPrivileged(new PrivilegedAction()
/*      */           {
/*      */             public Object run() {
/*  790 */               smartSingleton.afterSingletonsInstantiated();
/*  791 */               return null;
/*      */             }
/*      */           }
/*      */           , getAccessControlContext());
/*      */         }
/*      */         else
/*  796 */           smartSingleton.afterSingletonsInstantiated();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
/*      */     throws BeanDefinitionStoreException
/*      */   {
/*  811 */     Assert.hasText(beanName, "Bean name must not be empty");
/*  812 */     Assert.notNull(beanDefinition, "BeanDefinition must not be null");
/*      */ 
/*  814 */     if ((beanDefinition instanceof AbstractBeanDefinition)) {
/*      */       try {
/*  816 */         ((AbstractBeanDefinition)beanDefinition).validate();
/*      */       }
/*      */       catch (BeanDefinitionValidationException ex) {
/*  819 */         throw new BeanDefinitionStoreException(beanDefinition.getResourceDescription(), beanName, "Validation of bean definition failed", ex);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  826 */     BeanDefinition oldBeanDefinition = (BeanDefinition)this.beanDefinitionMap.get(beanName);
/*  827 */     if (oldBeanDefinition != null) {
/*  828 */       if (!isAllowBeanDefinitionOverriding()) {
/*  829 */         throw new BeanDefinitionStoreException(beanDefinition.getResourceDescription(), beanName, new StringBuilder().append("Cannot register bean definition [").append(beanDefinition).append("] for bean '").append(beanName).append("': There is already [").append(oldBeanDefinition).append("] bound.").toString());
/*      */       }
/*      */ 
/*  833 */       if (oldBeanDefinition.getRole() < beanDefinition.getRole())
/*      */       {
/*  835 */         if (this.logger.isWarnEnabled()) {
/*  836 */           this.logger.warn(new StringBuilder().append("Overriding user-defined bean definition for bean '").append(beanName).append("' with a framework-generated bean definition: replacing [").append(oldBeanDefinition).append("] with [").append(beanDefinition).append("]").toString());
/*      */         }
/*      */ 
/*      */       }
/*  841 */       else if (!beanDefinition.equals(oldBeanDefinition)) {
/*  842 */         if (this.logger.isInfoEnabled()) {
/*  843 */           this.logger.info(new StringBuilder().append("Overriding bean definition for bean '").append(beanName).append("' with a different definition: replacing [").append(oldBeanDefinition).append("] with [").append(beanDefinition).append("]").toString());
/*      */         }
/*      */ 
/*      */       }
/*  849 */       else if (this.logger.isDebugEnabled()) {
/*  850 */         this.logger.debug(new StringBuilder().append("Overriding bean definition for bean '").append(beanName).append("' with an equivalent definition: replacing [").append(oldBeanDefinition).append("] with [").append(beanDefinition).append("]").toString());
/*      */       }
/*      */ 
/*  855 */       this.beanDefinitionMap.put(beanName, beanDefinition);
/*      */     }
/*      */     else {
/*  858 */       if (hasBeanCreationStarted())
/*      */       {
/*  860 */         synchronized (this.beanDefinitionMap) {
/*  861 */           this.beanDefinitionMap.put(beanName, beanDefinition);
/*  862 */           List updatedDefinitions = new ArrayList(this.beanDefinitionNames.size() + 1);
/*  863 */           updatedDefinitions.addAll(this.beanDefinitionNames);
/*  864 */           updatedDefinitions.add(beanName);
/*  865 */           this.beanDefinitionNames = updatedDefinitions;
/*  866 */           if (this.manualSingletonNames.contains(beanName)) {
/*  867 */             Set updatedSingletons = new LinkedHashSet(this.manualSingletonNames);
/*  868 */             updatedSingletons.remove(beanName);
/*  869 */             this.manualSingletonNames = updatedSingletons;
/*      */           }
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  875 */         this.beanDefinitionMap.put(beanName, beanDefinition);
/*  876 */         this.beanDefinitionNames.add(beanName);
/*  877 */         this.manualSingletonNames.remove(beanName);
/*      */       }
/*  879 */       this.frozenBeanDefinitionNames = null;
/*      */     }
/*      */ 
/*  882 */     if ((oldBeanDefinition != null) || (containsSingleton(beanName)))
/*  883 */       resetBeanDefinition(beanName);
/*      */   }
/*      */ 
/*      */   public void removeBeanDefinition(String beanName)
/*      */     throws NoSuchBeanDefinitionException
/*      */   {
/*  889 */     Assert.hasText(beanName, "'beanName' must not be empty");
/*      */ 
/*  891 */     BeanDefinition bd = (BeanDefinition)this.beanDefinitionMap.remove(beanName);
/*  892 */     if (bd == null) {
/*  893 */       if (this.logger.isTraceEnabled()) {
/*  894 */         this.logger.trace(new StringBuilder().append("No bean named '").append(beanName).append("' found in ").append(this).toString());
/*      */       }
/*  896 */       throw new NoSuchBeanDefinitionException(beanName);
/*      */     }
/*      */ 
/*  899 */     if (hasBeanCreationStarted())
/*      */     {
/*  901 */       synchronized (this.beanDefinitionMap) {
/*  902 */         List updatedDefinitions = new ArrayList(this.beanDefinitionNames);
/*  903 */         updatedDefinitions.remove(beanName);
/*  904 */         this.beanDefinitionNames = updatedDefinitions;
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  909 */       this.beanDefinitionNames.remove(beanName);
/*      */     }
/*  911 */     this.frozenBeanDefinitionNames = null;
/*      */ 
/*  913 */     resetBeanDefinition(beanName);
/*      */   }
/*      */ 
/*      */   protected void resetBeanDefinition(String beanName)
/*      */   {
/*  923 */     clearMergedBeanDefinition(beanName);
/*      */ 
/*  928 */     destroySingleton(beanName);
/*      */ 
/*  931 */     for (String bdName : this.beanDefinitionNames)
/*  932 */       if (!beanName.equals(bdName)) {
/*  933 */         BeanDefinition bd = (BeanDefinition)this.beanDefinitionMap.get(bdName);
/*  934 */         if (beanName.equals(bd.getParentName()))
/*  935 */           resetBeanDefinition(bdName);
/*      */       }
/*      */   }
/*      */ 
/*      */   protected boolean allowAliasOverriding()
/*      */   {
/*  946 */     return isAllowBeanDefinitionOverriding();
/*      */   }
/*      */ 
/*      */   public void registerSingleton(String beanName, Object singletonObject) throws IllegalStateException
/*      */   {
/*  951 */     super.registerSingleton(beanName, singletonObject);
/*      */ 
/*  953 */     if (hasBeanCreationStarted())
/*      */     {
/*  955 */       synchronized (this.beanDefinitionMap) {
/*  956 */         if (!this.beanDefinitionMap.containsKey(beanName)) {
/*  957 */           Set updatedSingletons = new LinkedHashSet(this.manualSingletonNames.size() + 1);
/*  958 */           updatedSingletons.addAll(this.manualSingletonNames);
/*  959 */           updatedSingletons.add(beanName);
/*  960 */           this.manualSingletonNames = updatedSingletons;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*  966 */     else if (!this.beanDefinitionMap.containsKey(beanName)) {
/*  967 */       this.manualSingletonNames.add(beanName);
/*      */     }
/*      */ 
/*  971 */     clearByTypeCache();
/*      */   }
/*      */ 
/*      */   public void destroySingleton(String beanName)
/*      */   {
/*  976 */     super.destroySingleton(beanName);
/*  977 */     this.manualSingletonNames.remove(beanName);
/*  978 */     clearByTypeCache();
/*      */   }
/*      */ 
/*      */   public void destroySingletons()
/*      */   {
/*  983 */     super.destroySingletons();
/*  984 */     this.manualSingletonNames.clear();
/*  985 */     clearByTypeCache();
/*      */   }
/*      */ 
/*      */   private void clearByTypeCache()
/*      */   {
/*  992 */     this.allBeanNamesByType.clear();
/*  993 */     this.singletonBeanNamesByType.clear();
/*      */   }
/*      */ 
/*      */   public Object resolveDependency(DependencyDescriptor descriptor, String beanName, Set<String> autowiredBeanNames, TypeConverter typeConverter)
/*      */     throws BeansException
/*      */   {
/* 1005 */     descriptor.initParameterNameDiscovery(getParameterNameDiscoverer());
/* 1006 */     if (javaUtilOptionalClass == descriptor.getDependencyType()) {
/* 1007 */       return new OptionalDependencyFactory(null).createOptionalDependency(descriptor, beanName, new Object[0]);
/*      */     }
/* 1009 */     if ((ObjectFactory.class == descriptor.getDependencyType()) || 
/* 1010 */       (ObjectProvider.class == descriptor
/* 1010 */       .getDependencyType())) {
/* 1011 */       return new DependencyObjectProvider(descriptor, beanName);
/*      */     }
/* 1013 */     if (javaxInjectProviderClass == descriptor.getDependencyType()) {
/* 1014 */       return new Jsr330ProviderFactory(null).createDependencyProvider(descriptor, beanName);
/*      */     }
/*      */ 
/* 1017 */     Object result = getAutowireCandidateResolver().getLazyResolutionProxyIfNecessary(descriptor, beanName);
/* 1018 */     if (result == null) {
/* 1019 */       result = doResolveDependency(descriptor, beanName, autowiredBeanNames, typeConverter);
/*      */     }
/* 1021 */     return result;
/*      */   }
/*      */ 
/*      */   public Object doResolveDependency(DependencyDescriptor descriptor, String beanName, Set<String> autowiredBeanNames, TypeConverter typeConverter)
/*      */     throws BeansException
/*      */   {
/* 1028 */     InjectionPoint previousInjectionPoint = ConstructorResolver.setCurrentInjectionPoint(descriptor);
/*      */     try {
/* 1030 */       Object shortcut = descriptor.resolveShortcut(this);
/* 1031 */       if (shortcut != null) {
/* 1032 */         return shortcut;
/*      */       }
/*      */ 
/* 1035 */       Class type = descriptor.getDependencyType();
/* 1036 */       Object value = getAutowireCandidateResolver().getSuggestedValue(descriptor);
/*      */       BeanDefinition bd;
/* 1037 */       if (value != null) {
/* 1038 */         if ((value instanceof String)) {
/* 1039 */           String strVal = resolveEmbeddedValue((String)value);
/* 1040 */           bd = (beanName != null) && (containsBean(beanName)) ? getMergedBeanDefinition(beanName) : null;
/* 1041 */           value = evaluateBeanDefinitionString(strVal, bd);
/*      */         }
/* 1043 */         TypeConverter converter = typeConverter != null ? typeConverter : getTypeConverter();
/*      */ 
/* 1046 */         return descriptor.getField() != null ? converter
/* 1045 */           .convertIfNecessary(value, type, descriptor
/* 1045 */           .getField()) : converter
/* 1046 */           .convertIfNecessary(value, type, descriptor
/* 1046 */           .getMethodParameter());
/*      */       }
/*      */ 
/* 1049 */       Object multipleBeans = resolveMultipleBeans(descriptor, beanName, autowiredBeanNames, typeConverter);
/* 1050 */       if (multipleBeans != null) {
/* 1051 */         return multipleBeans;
/*      */       }
/*      */ 
/* 1054 */       Map matchingBeans = findAutowireCandidates(beanName, type, descriptor);
/* 1055 */       if (matchingBeans.isEmpty()) {
/* 1056 */         if (descriptor.isRequired()) {
/* 1057 */           raiseNoMatchingBeanFound(type, descriptor.getResolvableType().toString(), descriptor);
/*      */         }
/* 1059 */         return null;
/*      */       }
/*      */       Object localObject3;
/* 1061 */       if (matchingBeans.size() > 1) {
/* 1062 */         String primaryBeanName = determineAutowireCandidate(matchingBeans, descriptor);
/* 1063 */         if (primaryBeanName == null) {
/* 1064 */           if ((descriptor.isRequired()) || (!indicatesMultipleBeans(type))) {
/* 1065 */             return descriptor.resolveNotUnique(type, matchingBeans);
/*      */           }
/*      */ 
/* 1071 */           return null;
/*      */         }
/*      */ 
/* 1074 */         if (autowiredBeanNames != null) {
/* 1075 */           autowiredBeanNames.add(primaryBeanName);
/*      */         }
/* 1077 */         return matchingBeans.get(primaryBeanName);
/*      */       }
/*      */ 
/* 1080 */       Map.Entry entry = (Map.Entry)matchingBeans.entrySet().iterator().next();
/* 1081 */       if (autowiredBeanNames != null) {
/* 1082 */         autowiredBeanNames.add(entry.getKey());
/*      */       }
/* 1084 */       return entry.getValue();
/*      */     }
/*      */     finally {
/* 1087 */       ConstructorResolver.setCurrentInjectionPoint(previousInjectionPoint);
/*      */     }
/*      */   }
/*      */ 
/*      */   private Object resolveMultipleBeans(DependencyDescriptor descriptor, String beanName, Set<String> autowiredBeanNames, TypeConverter typeConverter)
/*      */   {
/* 1094 */     Class type = descriptor.getDependencyType();
/* 1095 */     if (type.isArray()) {
/* 1096 */       Class componentType = type.getComponentType();
/* 1097 */       DependencyDescriptor targetDesc = new DependencyDescriptor(descriptor);
/* 1098 */       targetDesc.increaseNestingLevel();
/* 1099 */       Map matchingBeans = findAutowireCandidates(beanName, componentType, targetDesc);
/* 1100 */       if (matchingBeans.isEmpty()) {
/* 1101 */         return null;
/*      */       }
/* 1103 */       if (autowiredBeanNames != null) {
/* 1104 */         autowiredBeanNames.addAll(matchingBeans.keySet());
/*      */       }
/* 1106 */       TypeConverter converter = typeConverter != null ? typeConverter : getTypeConverter();
/* 1107 */       Object result = converter.convertIfNecessary(matchingBeans.values(), type);
/* 1108 */       if ((getDependencyComparator() != null) && ((result instanceof Object[]))) {
/* 1109 */         Arrays.sort((Object[])result, adaptDependencyComparator(matchingBeans));
/*      */       }
/* 1111 */       return result;
/*      */     }
/* 1113 */     if ((Collection.class.isAssignableFrom(type)) && (type.isInterface())) {
/* 1114 */       Class elementType = descriptor.getCollectionType();
/* 1115 */       if (elementType == null) {
/* 1116 */         return null;
/*      */       }
/* 1118 */       DependencyDescriptor targetDesc = new DependencyDescriptor(descriptor);
/* 1119 */       targetDesc.increaseNestingLevel();
/* 1120 */       Map matchingBeans = findAutowireCandidates(beanName, elementType, targetDesc);
/* 1121 */       if (matchingBeans.isEmpty()) {
/* 1122 */         return null;
/*      */       }
/* 1124 */       if (autowiredBeanNames != null) {
/* 1125 */         autowiredBeanNames.addAll(matchingBeans.keySet());
/*      */       }
/* 1127 */       TypeConverter converter = typeConverter != null ? typeConverter : getTypeConverter();
/* 1128 */       Object result = converter.convertIfNecessary(matchingBeans.values(), type);
/* 1129 */       if ((getDependencyComparator() != null) && ((result instanceof List))) {
/* 1130 */         Collections.sort((List)result, adaptDependencyComparator(matchingBeans));
/*      */       }
/* 1132 */       return result;
/*      */     }
/* 1134 */     if ((Map.class.isAssignableFrom(type)) && (type.isInterface())) {
/* 1135 */       Class keyType = descriptor.getMapKeyType();
/* 1136 */       if (String.class != keyType) {
/* 1137 */         return null;
/*      */       }
/* 1139 */       Class valueType = descriptor.getMapValueType();
/* 1140 */       if (valueType == null) {
/* 1141 */         return null;
/*      */       }
/* 1143 */       DependencyDescriptor targetDesc = new DependencyDescriptor(descriptor);
/* 1144 */       targetDesc.increaseNestingLevel();
/* 1145 */       Map matchingBeans = findAutowireCandidates(beanName, valueType, targetDesc);
/* 1146 */       if (matchingBeans.isEmpty()) {
/* 1147 */         return null;
/*      */       }
/* 1149 */       if (autowiredBeanNames != null) {
/* 1150 */         autowiredBeanNames.addAll(matchingBeans.keySet());
/*      */       }
/* 1152 */       return matchingBeans;
/*      */     }
/*      */ 
/* 1155 */     return null;
/*      */   }
/*      */ 
/*      */   private boolean indicatesMultipleBeans(Class<?> type)
/*      */   {
/* 1161 */     return (type.isArray()) || ((type.isInterface()) && (
/* 1161 */       (Collection.class
/* 1161 */       .isAssignableFrom(type)) || 
/* 1161 */       (Map.class.isAssignableFrom(type))));
/*      */   }
/*      */ 
/*      */   private Comparator<Object> adaptDependencyComparator(Map<String, Object> matchingBeans) {
/* 1165 */     Comparator comparator = getDependencyComparator();
/* 1166 */     if ((comparator instanceof OrderComparator)) {
/* 1167 */       return ((OrderComparator)comparator).withSourceProvider(
/* 1168 */         createFactoryAwareOrderSourceProvider(matchingBeans));
/*      */     }
/*      */ 
/* 1171 */     return comparator;
/*      */   }
/*      */ 
/*      */   private FactoryAwareOrderSourceProvider createFactoryAwareOrderSourceProvider(Map<String, Object> beans)
/*      */   {
/* 1176 */     IdentityHashMap instancesToBeanNames = new IdentityHashMap();
/* 1177 */     for (Map.Entry entry : beans.entrySet()) {
/* 1178 */       instancesToBeanNames.put(entry.getValue(), entry.getKey());
/*      */     }
/* 1180 */     return new FactoryAwareOrderSourceProvider(instancesToBeanNames);
/*      */   }
/*      */ 
/*      */   protected Map<String, Object> findAutowireCandidates(String beanName, Class<?> requiredType, DependencyDescriptor descriptor)
/*      */   {
/* 1199 */     String[] candidateNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(this, requiredType, true, descriptor
/* 1200 */       .isEager());
/* 1201 */     Map result = new LinkedHashMap(candidateNames.length);
/* 1202 */     for (Object localObject1 = this.resolvableDependencies.keySet().iterator(); ((Iterator)localObject1).hasNext(); ) { autowiringType = (Class)((Iterator)localObject1).next();
/* 1203 */       if (autowiringType.isAssignableFrom(requiredType)) {
/* 1204 */         autowiringValue = this.resolvableDependencies.get(autowiringType);
/* 1205 */         autowiringValue = AutowireUtils.resolveAutowiringValue(autowiringValue, requiredType);
/* 1206 */         if (requiredType.isInstance(autowiringValue)) {
/* 1207 */           result.put(ObjectUtils.identityToString(autowiringValue), autowiringValue);
/* 1208 */           break;
/*      */         }
/*      */       }
/*      */     }
/* 1212 */     localObject1 = candidateNames; Class autowiringType = localObject1.length;
/*      */     String candidateName;
/* 1212 */     for (Object autowiringValue = 0; autowiringValue < autowiringType; autowiringValue++) { candidateName = localObject1[autowiringValue];
/* 1213 */       if ((!isSelfReference(beanName, candidateName)) && (isAutowireCandidate(candidateName, descriptor))) {
/* 1214 */         result.put(candidateName, descriptor.resolveCandidate(candidateName, requiredType, this));
/*      */       }
/*      */     }
/* 1217 */     if ((result.isEmpty()) && (!indicatesMultipleBeans(requiredType)))
/*      */     {
/* 1219 */       DependencyDescriptor fallbackDescriptor = descriptor.forFallbackMatch();
/* 1220 */       autowiringType = candidateNames; autowiringValue = autowiringType.length; for (candidateName = 0; candidateName < autowiringValue; candidateName++) { String candidateName = autowiringType[candidateName];
/* 1221 */         if ((!isSelfReference(beanName, candidateName)) && (isAutowireCandidate(candidateName, fallbackDescriptor))) {
/* 1222 */           result.put(candidateName, descriptor.resolveCandidate(candidateName, requiredType, this));
/*      */         }
/*      */       }
/* 1225 */       if (result.isEmpty())
/*      */       {
/* 1227 */         autowiringType = candidateNames; autowiringValue = autowiringType.length; for (candidateName = 0; candidateName < autowiringValue; candidateName++) { String candidateName = autowiringType[candidateName];
/* 1228 */           if ((isSelfReference(beanName, candidateName)) && (isAutowireCandidate(candidateName, fallbackDescriptor))) {
/* 1229 */             result.put(candidateName, descriptor.resolveCandidate(candidateName, requiredType, this));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1234 */     return result;
/*      */   }
/*      */ 
/*      */   protected String determineAutowireCandidate(Map<String, Object> candidateBeans, DependencyDescriptor descriptor)
/*      */   {
/* 1246 */     Class requiredType = descriptor.getDependencyType();
/* 1247 */     String primaryCandidate = determinePrimaryCandidate(candidateBeans, requiredType);
/* 1248 */     if (primaryCandidate != null) {
/* 1249 */       return primaryCandidate;
/*      */     }
/* 1251 */     String priorityCandidate = determineHighestPriorityCandidate(candidateBeans, requiredType);
/* 1252 */     if (priorityCandidate != null) {
/* 1253 */       return priorityCandidate;
/*      */     }
/*      */ 
/* 1256 */     for (Map.Entry entry : candidateBeans.entrySet()) {
/* 1257 */       String candidateBeanName = (String)entry.getKey();
/* 1258 */       Object beanInstance = entry.getValue();
/* 1259 */       if (((beanInstance != null) && (this.resolvableDependencies.containsValue(beanInstance))) || 
/* 1260 */         (matchesBeanName(candidateBeanName, descriptor
/* 1260 */         .getDependencyName()))) {
/* 1261 */         return candidateBeanName;
/*      */       }
/*      */     }
/* 1264 */     return null;
/*      */   }
/*      */ 
/*      */   protected String determinePrimaryCandidate(Map<String, Object> candidateBeans, Class<?> requiredType)
/*      */   {
/* 1276 */     String primaryBeanName = null;
/* 1277 */     for (Map.Entry entry : candidateBeans.entrySet()) {
/* 1278 */       String candidateBeanName = (String)entry.getKey();
/* 1279 */       Object beanInstance = entry.getValue();
/* 1280 */       if (isPrimary(candidateBeanName, beanInstance)) {
/* 1281 */         if (primaryBeanName != null) {
/* 1282 */           boolean candidateLocal = containsBeanDefinition(candidateBeanName);
/* 1283 */           boolean primaryLocal = containsBeanDefinition(primaryBeanName);
/* 1284 */           if ((candidateLocal) && (primaryLocal))
/*      */           {
/* 1286 */             throw new NoUniqueBeanDefinitionException(requiredType, candidateBeans.size(), new StringBuilder().append("more than one 'primary' bean found among candidates: ")
/* 1286 */               .append(candidateBeans
/* 1286 */               .keySet()).toString());
/*      */           }
/* 1288 */           if (candidateLocal)
/* 1289 */             primaryBeanName = candidateBeanName;
/*      */         }
/*      */         else
/*      */         {
/* 1293 */           primaryBeanName = candidateBeanName;
/*      */         }
/*      */       }
/*      */     }
/* 1297 */     return primaryBeanName;
/*      */   }
/*      */ 
/*      */   protected String determineHighestPriorityCandidate(Map<String, Object> candidateBeans, Class<?> requiredType)
/*      */   {
/* 1312 */     String highestPriorityBeanName = null;
/* 1313 */     Integer highestPriority = null;
/* 1314 */     for (Map.Entry entry : candidateBeans.entrySet()) {
/* 1315 */       String candidateBeanName = (String)entry.getKey();
/* 1316 */       Object beanInstance = entry.getValue();
/* 1317 */       Integer candidatePriority = getPriority(beanInstance);
/* 1318 */       if (candidatePriority != null) {
/* 1319 */         if (highestPriorityBeanName != null) {
/* 1320 */           if (candidatePriority.equals(highestPriority))
/*      */           {
/* 1323 */             throw new NoUniqueBeanDefinitionException(requiredType, candidateBeans.size(), new StringBuilder().append("Multiple beans found with the same priority ('").append(highestPriority).append("') ").append("among candidates: ")
/* 1323 */               .append(candidateBeans
/* 1323 */               .keySet()).toString());
/*      */           }
/* 1325 */           if (candidatePriority.intValue() < highestPriority.intValue()) {
/* 1326 */             highestPriorityBeanName = candidateBeanName;
/* 1327 */             highestPriority = candidatePriority;
/*      */           }
/*      */         }
/*      */         else {
/* 1331 */           highestPriorityBeanName = candidateBeanName;
/* 1332 */           highestPriority = candidatePriority;
/*      */         }
/*      */       }
/*      */     }
/* 1336 */     return highestPriorityBeanName;
/*      */   }
/*      */ 
/*      */   protected boolean isPrimary(String beanName, Object beanInstance)
/*      */   {
/* 1347 */     if (containsBeanDefinition(beanName)) {
/* 1348 */       return getMergedLocalBeanDefinition(beanName).isPrimary();
/*      */     }
/* 1350 */     BeanFactory parentFactory = getParentBeanFactory();
/*      */ 
/* 1352 */     return ((parentFactory instanceof DefaultListableBeanFactory)) && 
/* 1352 */       (((DefaultListableBeanFactory)parentFactory)
/* 1352 */       .isPrimary(beanName, beanInstance));
/*      */   }
/*      */ 
/*      */   protected Integer getPriority(Object beanInstance)
/*      */   {
/* 1368 */     Comparator comparator = getDependencyComparator();
/* 1369 */     if ((comparator instanceof OrderComparator)) {
/* 1370 */       return ((OrderComparator)comparator).getPriority(beanInstance);
/*      */     }
/* 1372 */     return null;
/*      */   }
/*      */ 
/*      */   protected boolean matchesBeanName(String beanName, String candidateName)
/*      */   {
/* 1381 */     return (candidateName != null) && (
/* 1381 */       (candidateName
/* 1381 */       .equals(beanName)) || 
/* 1381 */       (ObjectUtils.containsElement(getAliases(beanName), candidateName)));
/*      */   }
/*      */ 
/*      */   private boolean isSelfReference(String beanName, String candidateName)
/*      */   {
/* 1392 */     return (beanName != null) && (candidateName != null) && (
/* 1391 */       (beanName
/* 1391 */       .equals(candidateName)) || 
/* 1391 */       ((containsBeanDefinition(candidateName)) && 
/* 1392 */       (beanName
/* 1392 */       .equals(getMergedLocalBeanDefinition(candidateName)
/* 1392 */       .getFactoryBeanName()))));
/*      */   }
/*      */ 
/*      */   private void raiseNoMatchingBeanFound(Class<?> type, String dependencyDescription, DependencyDescriptor descriptor)
/*      */     throws BeansException
/*      */   {
/* 1402 */     checkBeanNotOfRequiredType(type, descriptor);
/*      */ 
/* 1406 */     throw new NoSuchBeanDefinitionException(type, dependencyDescription, new StringBuilder().append("expected at least 1 bean which qualifies as autowire candidate for this dependency. Dependency annotations: ")
/* 1406 */       .append(ObjectUtils.nullSafeToString(descriptor
/* 1406 */       .getAnnotations())).toString());
/*      */   }
/*      */ 
/*      */   private void checkBeanNotOfRequiredType(Class<?> type, DependencyDescriptor descriptor)
/*      */   {
/* 1414 */     for (String beanName : this.beanDefinitionNames) {
/* 1415 */       RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/* 1416 */       Class targetType = mbd.getTargetType();
/* 1417 */       if ((targetType != null) && (type.isAssignableFrom(targetType)) && 
/* 1418 */         (isAutowireCandidate(beanName, mbd, descriptor, 
/* 1418 */         getAutowireCandidateResolver())))
/*      */       {
/* 1420 */         Object beanInstance = getSingleton(beanName, false);
/* 1421 */         Class beanType = beanInstance != null ? beanInstance.getClass() : predictBeanType(beanName, mbd, new Class[0]);
/* 1422 */         if (type != beanType) {
/* 1423 */           throw new BeanNotOfRequiredTypeException(beanName, type, beanType);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1428 */     if ((getParentBeanFactory() instanceof DefaultListableBeanFactory))
/* 1429 */       ((DefaultListableBeanFactory)getParentBeanFactory()).checkBeanNotOfRequiredType(type, descriptor);
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1436 */     StringBuilder sb = new StringBuilder(ObjectUtils.identityToString(this));
/* 1437 */     sb.append(": defining beans [");
/* 1438 */     sb.append(StringUtils.collectionToCommaDelimitedString(this.beanDefinitionNames));
/* 1439 */     sb.append("]; ");
/* 1440 */     BeanFactory parent = getParentBeanFactory();
/* 1441 */     if (parent == null) {
/* 1442 */       sb.append("root of factory hierarchy");
/*      */     }
/*      */     else {
/* 1445 */       sb.append("parent: ").append(ObjectUtils.identityToString(parent));
/*      */     }
/* 1447 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream ois)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1456 */     throw new NotSerializableException("DefaultListableBeanFactory itself is not deserializable - just a SerializedBeanFactoryReference is");
/*      */   }
/*      */ 
/*      */   protected Object writeReplace() throws ObjectStreamException
/*      */   {
/* 1461 */     if (this.serializationId != null) {
/* 1462 */       return new SerializedBeanFactoryReference(this.serializationId);
/*      */     }
/*      */ 
/* 1465 */     throw new NotSerializableException("DefaultListableBeanFactory has no serialization id");
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*      */     try
/*      */     {
/*  125 */       javaUtilOptionalClass = ClassUtils.forName("java.util.Optional", DefaultListableBeanFactory.class
/*  125 */         .getClassLoader());
/*      */     }
/*      */     catch (ClassNotFoundException localClassNotFoundException1)
/*      */     {
/*      */     }
/*      */     try
/*      */     {
/*  132 */       javaxInjectProviderClass = ClassUtils.forName("javax.inject.Provider", DefaultListableBeanFactory.class
/*  132 */         .getClassLoader());
/*      */     }
/*      */     catch (ClassNotFoundException localClassNotFoundException2)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   private class FactoryAwareOrderSourceProvider
/*      */     implements OrderComparator.OrderSourceProvider
/*      */   {
/*      */     private final Map<Object, String> instancesToBeanNames;
/*      */ 
/*      */     public FactoryAwareOrderSourceProvider()
/*      */     {
/* 1641 */       this.instancesToBeanNames = instancesToBeanNames;
/*      */     }
/*      */ 
/*      */     public Object getOrderSource(Object obj)
/*      */     {
/* 1646 */       RootBeanDefinition beanDefinition = getRootBeanDefinition((String)this.instancesToBeanNames.get(obj));
/* 1647 */       if (beanDefinition == null) {
/* 1648 */         return null;
/*      */       }
/* 1650 */       List sources = new ArrayList();
/* 1651 */       Method factoryMethod = beanDefinition.getResolvedFactoryMethod();
/* 1652 */       if (factoryMethod != null) {
/* 1653 */         sources.add(factoryMethod);
/*      */       }
/* 1655 */       Class targetType = beanDefinition.getTargetType();
/* 1656 */       if ((targetType != null) && (!targetType.equals(obj.getClass()))) {
/* 1657 */         sources.add(targetType);
/*      */       }
/* 1659 */       return sources.toArray(new Object[sources.size()]);
/*      */     }
/*      */ 
/*      */     private RootBeanDefinition getRootBeanDefinition(String beanName) {
/* 1663 */       if ((beanName != null) && (DefaultListableBeanFactory.this.containsBeanDefinition(beanName))) {
/* 1664 */         BeanDefinition bd = DefaultListableBeanFactory.this.getMergedBeanDefinition(beanName);
/* 1665 */         if ((bd instanceof RootBeanDefinition)) {
/* 1666 */           return (RootBeanDefinition)bd;
/*      */         }
/*      */       }
/* 1669 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class Jsr330ProviderFactory
/*      */   {
/*      */     private Jsr330ProviderFactory()
/*      */     {
/*      */     }
/*      */ 
/*      */     public Object createDependencyProvider(DependencyDescriptor descriptor, String beanName)
/*      */     {
/* 1624 */       return new DefaultListableBeanFactory.Jsr330DependencyProvider(DefaultListableBeanFactory.this, descriptor, beanName);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class Jsr330DependencyProvider extends DefaultListableBeanFactory.DependencyObjectProvider
/*      */     implements Provider<Object>
/*      */   {
/*      */     public Jsr330DependencyProvider(DependencyDescriptor descriptor, String beanName)
/*      */     {
/* 1608 */       super(descriptor, beanName);
/*      */     }
/*      */ 
/*      */     public Object get() throws BeansException
/*      */     {
/* 1613 */       return getObject();
/*      */     }
/*      */   }
/*      */ 
/*      */   private class DependencyObjectProvider
/*      */     implements ObjectProvider<Object>, Serializable
/*      */   {
/*      */     private final DependencyDescriptor descriptor;
/*      */     private final boolean optional;
/*      */     private final String beanName;
/*      */ 
/*      */     public DependencyObjectProvider(DependencyDescriptor descriptor, String beanName)
/*      */     {
/* 1532 */       this.descriptor = new DependencyDescriptor(descriptor);
/* 1533 */       this.descriptor.increaseNestingLevel();
/* 1534 */       this.optional = this.descriptor.getDependencyType().equals(DefaultListableBeanFactory.javaUtilOptionalClass);
/* 1535 */       this.beanName = beanName;
/*      */     }
/*      */ 
/*      */     public Object getObject() throws BeansException
/*      */     {
/* 1540 */       if (this.optional) {
/* 1541 */         return new DefaultListableBeanFactory.OptionalDependencyFactory(DefaultListableBeanFactory.this, null).createOptionalDependency(this.descriptor, this.beanName, new Object[0]);
/*      */       }
/*      */ 
/* 1544 */       return DefaultListableBeanFactory.this.doResolveDependency(this.descriptor, this.beanName, null, null);
/*      */     }
/*      */ 
/*      */     public Object getObject(final Object[] args)
/*      */       throws BeansException
/*      */     {
/* 1550 */       if (this.optional) {
/* 1551 */         return new DefaultListableBeanFactory.OptionalDependencyFactory(DefaultListableBeanFactory.this, null).createOptionalDependency(this.descriptor, this.beanName, args);
/*      */       }
/*      */ 
/* 1554 */       DependencyDescriptor descriptorToUse = new DependencyDescriptor(this.descriptor)
/*      */       {
/*      */         public Object resolveCandidate(String beanName, Class<?> requiredType, BeanFactory beanFactory) {
/* 1557 */           return ((AbstractBeanFactory)beanFactory).getBean(beanName, requiredType, args);
/*      */         }
/*      */       };
/* 1560 */       return DefaultListableBeanFactory.this.doResolveDependency(descriptorToUse, this.beanName, null, null);
/*      */     }
/*      */ 
/*      */     public Object getIfAvailable()
/*      */       throws BeansException
/*      */     {
/* 1566 */       if (this.optional) {
/* 1567 */         return new DefaultListableBeanFactory.OptionalDependencyFactory(DefaultListableBeanFactory.this, null).createOptionalDependency(this.descriptor, this.beanName, new Object[0]);
/*      */       }
/*      */ 
/* 1570 */       DependencyDescriptor descriptorToUse = new DependencyDescriptor(this.descriptor)
/*      */       {
/*      */         public boolean isRequired() {
/* 1573 */           return false;
/*      */         }
/*      */       };
/* 1576 */       return DefaultListableBeanFactory.this.doResolveDependency(descriptorToUse, this.beanName, null, null);
/*      */     }
/*      */ 
/*      */     public Object getIfUnique()
/*      */       throws BeansException
/*      */     {
/* 1582 */       DependencyDescriptor descriptorToUse = new DependencyDescriptor(this.descriptor)
/*      */       {
/*      */         public boolean isRequired() {
/* 1585 */           return false;
/*      */         }
/*      */ 
/*      */         public Object resolveNotUnique(Class<?> type, Map<String, Object> matchingBeans) {
/* 1589 */           return null;
/*      */         }
/*      */       };
/* 1592 */       if (this.optional) {
/* 1593 */         return new DefaultListableBeanFactory.OptionalDependencyFactory(DefaultListableBeanFactory.this, null).createOptionalDependency(descriptorToUse, this.beanName, new Object[0]);
/*      */       }
/*      */ 
/* 1596 */       return DefaultListableBeanFactory.this.doResolveDependency(descriptorToUse, this.beanName, null, null);
/*      */     }
/*      */   }
/*      */ 
/*      */   @UsesJava8
/*      */   private class OptionalDependencyFactory
/*      */   {
/*      */     private OptionalDependencyFactory()
/*      */     {
/*      */     }
/*      */ 
/*      */     public Object createOptionalDependency(DependencyDescriptor descriptor, String beanName, final Object[] args)
/*      */     {
/* 1503 */       DependencyDescriptor descriptorToUse = new DependencyDescriptor(descriptor)
/*      */       {
/*      */         public boolean isRequired() {
/* 1506 */           return false;
/*      */         }
/*      */ 
/*      */         public Object resolveCandidate(String beanName, Class<?> requiredType, BeanFactory beanFactory)
/*      */         {
/* 1511 */           return !ObjectUtils.isEmpty(args) ? beanFactory.getBean(beanName, new Object[] { requiredType, args }) : super
/* 1511 */             .resolveCandidate(beanName, requiredType, beanFactory);
/*      */         }
/*      */       };
/* 1514 */       descriptorToUse.increaseNestingLevel();
/* 1515 */       return Optional.ofNullable(DefaultListableBeanFactory.this.doResolveDependency(descriptorToUse, beanName, null, null));
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class SerializedBeanFactoryReference
/*      */     implements Serializable
/*      */   {
/*      */     private final String id;
/*      */ 
/*      */     public SerializedBeanFactoryReference(String id)
/*      */     {
/* 1479 */       this.id = id;
/*      */     }
/*      */ 
/*      */     private Object readResolve() {
/* 1483 */       Reference ref = (Reference)DefaultListableBeanFactory.serializableFactories.get(this.id);
/* 1484 */       if (ref != null) {
/* 1485 */         Object result = ref.get();
/* 1486 */         if (result != null) {
/* 1487 */           return result;
/*      */         }
/*      */       }
/*      */ 
/* 1491 */       return new StaticListableBeanFactory(Collections.emptyMap());
/*      */     }
/*      */   }
/*      */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.support.DefaultListableBeanFactory
 * JD-Core Version:    0.6.2
 */