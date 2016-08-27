/*      */ package org.springframework.beans.factory.support;
/*      */ 
/*      */ import java.beans.PropertyDescriptor;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.TreeSet;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.springframework.beans.BeanUtils;
/*      */ import org.springframework.beans.BeanWrapper;
/*      */ import org.springframework.beans.BeanWrapperImpl;
/*      */ import org.springframework.beans.BeansException;
/*      */ import org.springframework.beans.MutablePropertyValues;
/*      */ import org.springframework.beans.PropertyAccessorUtils;
/*      */ import org.springframework.beans.PropertyValue;
/*      */ import org.springframework.beans.PropertyValues;
/*      */ import org.springframework.beans.TypeConverter;
/*      */ import org.springframework.beans.factory.Aware;
/*      */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*      */ import org.springframework.beans.factory.BeanCreationException;
/*      */ import org.springframework.beans.factory.BeanCurrentlyInCreationException;
/*      */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*      */ import org.springframework.beans.factory.BeanFactory;
/*      */ import org.springframework.beans.factory.BeanFactoryAware;
/*      */ import org.springframework.beans.factory.BeanNameAware;
/*      */ import org.springframework.beans.factory.FactoryBean;
/*      */ import org.springframework.beans.factory.InitializingBean;
/*      */ import org.springframework.beans.factory.ObjectFactory;
/*      */ import org.springframework.beans.factory.UnsatisfiedDependencyException;
/*      */ import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
/*      */ import org.springframework.beans.factory.config.BeanDefinition;
/*      */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*      */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*      */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*      */ import org.springframework.beans.factory.config.ConstructorArgumentValues.ValueHolder;
/*      */ import org.springframework.beans.factory.config.DependencyDescriptor;
/*      */ import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
/*      */ import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
/*      */ import org.springframework.beans.factory.config.TypedStringValue;
/*      */ import org.springframework.core.DefaultParameterNameDiscoverer;
/*      */ import org.springframework.core.GenericTypeResolver;
/*      */ import org.springframework.core.MethodParameter;
/*      */ import org.springframework.core.ParameterNameDiscoverer;
/*      */ import org.springframework.core.PriorityOrdered;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.ObjectUtils;
/*      */ import org.springframework.util.ReflectionUtils;
/*      */ import org.springframework.util.ReflectionUtils.MethodCallback;
/*      */ import org.springframework.util.StringUtils;
/*      */ 
/*      */ public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory
/*      */   implements AutowireCapableBeanFactory
/*      */ {
/*  121 */   private InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();
/*      */ 
/*  124 */   private ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
/*      */ 
/*  127 */   private boolean allowCircularReferences = true;
/*      */ 
/*  133 */   private boolean allowRawInjectionDespiteWrapping = false;
/*      */ 
/*  139 */   private final Set<Class<?>> ignoredDependencyTypes = new HashSet();
/*      */ 
/*  145 */   private final Set<Class<?>> ignoredDependencyInterfaces = new HashSet();
/*      */ 
/*  148 */   private final Map<String, BeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap(16);
/*      */ 
/*  152 */   private final ConcurrentMap<Class<?>, PropertyDescriptor[]> filteredPropertyDescriptorsCache = new ConcurrentHashMap(256);
/*      */ 
/*      */   public AbstractAutowireCapableBeanFactory()
/*      */   {
/*  161 */     ignoreDependencyInterface(BeanNameAware.class);
/*  162 */     ignoreDependencyInterface(BeanFactoryAware.class);
/*  163 */     ignoreDependencyInterface(BeanClassLoaderAware.class);
/*      */   }
/*      */ 
/*      */   public AbstractAutowireCapableBeanFactory(BeanFactory parentBeanFactory)
/*      */   {
/*  171 */     this();
/*  172 */     setParentBeanFactory(parentBeanFactory);
/*      */   }
/*      */ 
/*      */   public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy)
/*      */   {
/*  182 */     this.instantiationStrategy = instantiationStrategy;
/*      */   }
/*      */ 
/*      */   protected InstantiationStrategy getInstantiationStrategy()
/*      */   {
/*  189 */     return this.instantiationStrategy;
/*      */   }
/*      */ 
/*      */   public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer)
/*      */   {
/*  198 */     this.parameterNameDiscoverer = parameterNameDiscoverer;
/*      */   }
/*      */ 
/*      */   protected ParameterNameDiscoverer getParameterNameDiscoverer()
/*      */   {
/*  206 */     return this.parameterNameDiscoverer;
/*      */   }
/*      */ 
/*      */   public void setAllowCircularReferences(boolean allowCircularReferences)
/*      */   {
/*  223 */     this.allowCircularReferences = allowCircularReferences;
/*      */   }
/*      */ 
/*      */   public void setAllowRawInjectionDespiteWrapping(boolean allowRawInjectionDespiteWrapping)
/*      */   {
/*  241 */     this.allowRawInjectionDespiteWrapping = allowRawInjectionDespiteWrapping;
/*      */   }
/*      */ 
/*      */   public void ignoreDependencyType(Class<?> type)
/*      */   {
/*  249 */     this.ignoredDependencyTypes.add(type);
/*      */   }
/*      */ 
/*      */   public void ignoreDependencyInterface(Class<?> ifc)
/*      */   {
/*  263 */     this.ignoredDependencyInterfaces.add(ifc);
/*      */   }
/*      */ 
/*      */   public void copyConfigurationFrom(ConfigurableBeanFactory otherFactory)
/*      */   {
/*  268 */     super.copyConfigurationFrom(otherFactory);
/*  269 */     if ((otherFactory instanceof AbstractAutowireCapableBeanFactory)) {
/*  270 */       AbstractAutowireCapableBeanFactory otherAutowireFactory = (AbstractAutowireCapableBeanFactory)otherFactory;
/*      */ 
/*  272 */       this.instantiationStrategy = otherAutowireFactory.instantiationStrategy;
/*  273 */       this.allowCircularReferences = otherAutowireFactory.allowCircularReferences;
/*  274 */       this.ignoredDependencyTypes.addAll(otherAutowireFactory.ignoredDependencyTypes);
/*  275 */       this.ignoredDependencyInterfaces.addAll(otherAutowireFactory.ignoredDependencyInterfaces);
/*      */     }
/*      */   }
/*      */ 
/*      */   public <T> T createBean(Class<T> beanClass)
/*      */     throws BeansException
/*      */   {
/*  288 */     RootBeanDefinition bd = new RootBeanDefinition(beanClass);
/*  289 */     bd.setScope("prototype");
/*  290 */     bd.allowCaching = ClassUtils.isCacheSafe(beanClass, getBeanClassLoader());
/*  291 */     return createBean(beanClass.getName(), bd, null);
/*      */   }
/*      */ 
/*      */   public void autowireBean(Object existingBean)
/*      */   {
/*  297 */     RootBeanDefinition bd = new RootBeanDefinition(ClassUtils.getUserClass(existingBean));
/*  298 */     bd.setScope("prototype");
/*  299 */     bd.allowCaching = ClassUtils.isCacheSafe(bd.getBeanClass(), getBeanClassLoader());
/*  300 */     BeanWrapper bw = new BeanWrapperImpl(existingBean);
/*  301 */     initBeanWrapper(bw);
/*  302 */     populateBean(bd.getBeanClass().getName(), bd, bw);
/*      */   }
/*      */ 
/*      */   public Object configureBean(Object existingBean, String beanName) throws BeansException
/*      */   {
/*  307 */     markBeanAsCreated(beanName);
/*  308 */     BeanDefinition mbd = getMergedBeanDefinition(beanName);
/*  309 */     RootBeanDefinition bd = null;
/*  310 */     if ((mbd instanceof RootBeanDefinition)) {
/*  311 */       RootBeanDefinition rbd = (RootBeanDefinition)mbd;
/*  312 */       bd = rbd.isPrototype() ? rbd : rbd.cloneBeanDefinition();
/*      */     }
/*  314 */     if (!mbd.isPrototype()) {
/*  315 */       if (bd == null) {
/*  316 */         bd = new RootBeanDefinition(mbd);
/*      */       }
/*  318 */       bd.setScope("prototype");
/*  319 */       bd.allowCaching = ClassUtils.isCacheSafe(ClassUtils.getUserClass(existingBean), getBeanClassLoader());
/*      */     }
/*  321 */     BeanWrapper bw = new BeanWrapperImpl(existingBean);
/*  322 */     initBeanWrapper(bw);
/*  323 */     populateBean(beanName, bd, bw);
/*  324 */     return initializeBean(beanName, existingBean, bd);
/*      */   }
/*      */ 
/*      */   public Object resolveDependency(DependencyDescriptor descriptor, String beanName) throws BeansException
/*      */   {
/*  329 */     return resolveDependency(descriptor, beanName, null, null);
/*      */   }
/*      */ 
/*      */   public Object createBean(Class<?> beanClass, int autowireMode, boolean dependencyCheck)
/*      */     throws BeansException
/*      */   {
/*  340 */     RootBeanDefinition bd = new RootBeanDefinition(beanClass, autowireMode, dependencyCheck);
/*  341 */     bd.setScope("prototype");
/*  342 */     return createBean(beanClass.getName(), bd, null);
/*      */   }
/*      */ 
/*      */   public Object autowire(Class<?> beanClass, int autowireMode, boolean dependencyCheck)
/*      */     throws BeansException
/*      */   {
/*  348 */     final RootBeanDefinition bd = new RootBeanDefinition(beanClass, autowireMode, dependencyCheck);
/*  349 */     bd.setScope("prototype");
/*  350 */     if (bd.getResolvedAutowireMode() == 3) {
/*  351 */       return autowireConstructor(beanClass.getName(), bd, null, null).getWrappedInstance();
/*      */     }
/*      */ 
/*  355 */     final BeanFactory parent = this;
/*      */     Object bean;
/*      */     Object bean;
/*  356 */     if (System.getSecurityManager() != null) {
/*  357 */       bean = AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public Object run() {
/*  360 */           return AbstractAutowireCapableBeanFactory.this.getInstantiationStrategy().instantiate(bd, null, parent);
/*      */         }
/*      */       }
/*      */       , getAccessControlContext());
/*      */     }
/*      */     else {
/*  365 */       bean = getInstantiationStrategy().instantiate(bd, null, parent);
/*      */     }
/*  367 */     populateBean(beanClass.getName(), bd, new BeanWrapperImpl(bean));
/*  368 */     return bean;
/*      */   }
/*      */ 
/*      */   public void autowireBeanProperties(Object existingBean, int autowireMode, boolean dependencyCheck)
/*      */     throws BeansException
/*      */   {
/*  376 */     if (autowireMode == 3) {
/*  377 */       throw new IllegalArgumentException("AUTOWIRE_CONSTRUCTOR not supported for existing bean instance");
/*      */     }
/*      */ 
/*  381 */     RootBeanDefinition bd = new RootBeanDefinition(
/*  381 */       ClassUtils.getUserClass(existingBean), 
/*  381 */       autowireMode, dependencyCheck);
/*  382 */     bd.setScope("prototype");
/*  383 */     BeanWrapper bw = new BeanWrapperImpl(existingBean);
/*  384 */     initBeanWrapper(bw);
/*  385 */     populateBean(bd.getBeanClass().getName(), bd, bw);
/*      */   }
/*      */ 
/*      */   public void applyBeanPropertyValues(Object existingBean, String beanName) throws BeansException
/*      */   {
/*  390 */     markBeanAsCreated(beanName);
/*  391 */     BeanDefinition bd = getMergedBeanDefinition(beanName);
/*  392 */     BeanWrapper bw = new BeanWrapperImpl(existingBean);
/*  393 */     initBeanWrapper(bw);
/*  394 */     applyPropertyValues(beanName, bd, bw, bd.getPropertyValues());
/*      */   }
/*      */ 
/*      */   public Object initializeBean(Object existingBean, String beanName)
/*      */   {
/*  399 */     return initializeBean(beanName, existingBean, null);
/*      */   }
/*      */ 
/*      */   public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
/*      */     throws BeansException
/*      */   {
/*  406 */     Object result = existingBean;
/*  407 */     for (BeanPostProcessor beanProcessor : getBeanPostProcessors()) {
/*  408 */       result = beanProcessor.postProcessBeforeInitialization(result, beanName);
/*  409 */       if (result == null) {
/*  410 */         return result;
/*      */       }
/*      */     }
/*  413 */     return result;
/*      */   }
/*      */ 
/*      */   public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
/*      */     throws BeansException
/*      */   {
/*  420 */     Object result = existingBean;
/*  421 */     for (BeanPostProcessor beanProcessor : getBeanPostProcessors()) {
/*  422 */       result = beanProcessor.postProcessAfterInitialization(result, beanName);
/*  423 */       if (result == null) {
/*  424 */         return result;
/*      */       }
/*      */     }
/*  427 */     return result;
/*      */   }
/*      */ 
/*      */   public void destroyBean(Object existingBean)
/*      */   {
/*  432 */     new DisposableBeanAdapter(existingBean, getBeanPostProcessors(), getAccessControlContext()).destroy();
/*      */   }
/*      */ 
/*      */   protected Object createBean(String beanName, RootBeanDefinition mbd, Object[] args)
/*      */     throws BeanCreationException
/*      */   {
/*  447 */     if (this.logger.isDebugEnabled()) {
/*  448 */       this.logger.debug("Creating instance of bean '" + beanName + "'");
/*      */     }
/*  450 */     RootBeanDefinition mbdToUse = mbd;
/*      */ 
/*  455 */     Class resolvedClass = resolveBeanClass(mbd, beanName, new Class[0]);
/*  456 */     if ((resolvedClass != null) && (!mbd.hasBeanClass()) && (mbd.getBeanClassName() != null)) {
/*  457 */       mbdToUse = new RootBeanDefinition(mbd);
/*  458 */       mbdToUse.setBeanClass(resolvedClass);
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  463 */       mbdToUse.prepareMethodOverrides();
/*      */     }
/*      */     catch (BeanDefinitionValidationException ex) {
/*  466 */       throw new BeanDefinitionStoreException(mbdToUse.getResourceDescription(), beanName, "Validation of method overrides failed", ex);
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  472 */       Object bean = resolveBeforeInstantiation(beanName, mbdToUse);
/*  473 */       if (bean != null)
/*  474 */         return bean;
/*      */     }
/*      */     catch (Throwable ex)
/*      */     {
/*  478 */       throw new BeanCreationException(mbdToUse.getResourceDescription(), beanName, "BeanPostProcessor before instantiation of bean failed", ex);
/*      */     }
/*      */ 
/*  482 */     Object beanInstance = doCreateBean(beanName, mbdToUse, args);
/*  483 */     if (this.logger.isDebugEnabled()) {
/*  484 */       this.logger.debug("Finished creating instance of bean '" + beanName + "'");
/*      */     }
/*  486 */     return beanInstance;
/*      */   }
/*      */ 
/*      */   protected Object doCreateBean(final String beanName, final RootBeanDefinition mbd, Object[] args)
/*      */   {
/*  505 */     BeanWrapper instanceWrapper = null;
/*  506 */     if (mbd.isSingleton()) {
/*  507 */       instanceWrapper = (BeanWrapper)this.factoryBeanInstanceCache.remove(beanName);
/*      */     }
/*  509 */     if (instanceWrapper == null) {
/*  510 */       instanceWrapper = createBeanInstance(beanName, mbd, args);
/*      */     }
/*  512 */     final Object bean = instanceWrapper != null ? instanceWrapper.getWrappedInstance() : null;
/*  513 */     Class beanType = instanceWrapper != null ? instanceWrapper.getWrappedClass() : null;
/*      */ 
/*  516 */     synchronized (mbd.postProcessingLock) {
/*  517 */       if (!mbd.postProcessed) {
/*  518 */         applyMergedBeanDefinitionPostProcessors(mbd, beanType, beanName);
/*  519 */         mbd.postProcessed = true;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  526 */     boolean earlySingletonExposure = (mbd.isSingleton()) && (this.allowCircularReferences) && 
/*  526 */       (isSingletonCurrentlyInCreation(beanName));
/*      */ 
/*  527 */     if (earlySingletonExposure) {
/*  528 */       if (this.logger.isDebugEnabled()) {
/*  529 */         this.logger.debug("Eagerly caching bean '" + beanName + "' to allow for resolving potential circular references");
/*      */       }
/*      */ 
/*  532 */       addSingletonFactory(beanName, new ObjectFactory()
/*      */       {
/*      */         public Object getObject() throws BeansException {
/*  535 */           return AbstractAutowireCapableBeanFactory.this.getEarlyBeanReference(beanName, mbd, bean);
/*      */         }
/*      */ 
/*      */       });
/*      */     }
/*      */ 
/*  541 */     Object exposedObject = bean;
/*      */     try {
/*  543 */       populateBean(beanName, mbd, instanceWrapper);
/*  544 */       if (exposedObject != null)
/*  545 */         exposedObject = initializeBean(beanName, exposedObject, mbd);
/*      */     }
/*      */     catch (Throwable ex)
/*      */     {
/*  549 */       if (((ex instanceof BeanCreationException)) && (beanName.equals(((BeanCreationException)ex).getBeanName()))) {
/*  550 */         throw ((BeanCreationException)ex);
/*      */       }
/*      */ 
/*  553 */       throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Initialization of bean failed", ex);
/*      */     }
/*      */ 
/*  557 */     if (earlySingletonExposure) {
/*  558 */       Object earlySingletonReference = getSingleton(beanName, false);
/*  559 */       if (earlySingletonReference != null) {
/*  560 */         if (exposedObject == bean) {
/*  561 */           exposedObject = earlySingletonReference;
/*      */         }
/*  563 */         else if ((!this.allowRawInjectionDespiteWrapping) && (hasDependentBean(beanName))) {
/*  564 */           String[] dependentBeans = getDependentBeans(beanName);
/*  565 */           Set actualDependentBeans = new LinkedHashSet(dependentBeans.length);
/*  566 */           for (String dependentBean : dependentBeans) {
/*  567 */             if (!removeSingletonIfCreatedForTypeCheckOnly(dependentBean)) {
/*  568 */               actualDependentBeans.add(dependentBean);
/*      */             }
/*      */           }
/*  571 */           if (!actualDependentBeans.isEmpty())
/*      */           {
/*  574 */             throw new BeanCurrentlyInCreationException(beanName, "Bean with name '" + beanName + "' has been injected into other beans [" + 
/*  574 */               StringUtils.collectionToCommaDelimitedString(actualDependentBeans) + 
/*  574 */               "] in its raw version as part of a circular reference, but has eventually been " + "wrapped. This means that said other beans do not use the final version of the " + "bean. This is often the result of over-eager type matching - consider using " + "'getBeanNamesOfType' with the 'allowEagerInit' flag turned off, for example.");
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  586 */       registerDisposableBeanIfNecessary(beanName, bean, mbd);
/*      */     }
/*      */     catch (BeanDefinitionValidationException ex) {
/*  589 */       throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Invalid destruction signature", ex);
/*      */     }
/*      */ 
/*  592 */     return exposedObject;
/*      */   }
/*      */ 
/*      */   protected Class<?> predictBeanType(String beanName, RootBeanDefinition mbd, Class<?>[] typesToMatch)
/*      */   {
/*  597 */     Class targetType = determineTargetType(beanName, mbd, typesToMatch);
/*      */ 
/*  601 */     if ((targetType != null) && (!mbd.isSynthetic()) && (hasInstantiationAwareBeanPostProcessors())) {
/*  602 */       for (BeanPostProcessor bp : getBeanPostProcessors()) {
/*  603 */         if ((bp instanceof SmartInstantiationAwareBeanPostProcessor)) {
/*  604 */           SmartInstantiationAwareBeanPostProcessor ibp = (SmartInstantiationAwareBeanPostProcessor)bp;
/*  605 */           Class predicted = ibp.predictBeanType(targetType, beanName);
/*  606 */           if ((predicted != null) && ((typesToMatch.length != 1) || (FactoryBean.class != typesToMatch[0]) || 
/*  607 */             (FactoryBean.class
/*  607 */             .isAssignableFrom(predicted))))
/*      */           {
/*  608 */             return predicted;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  613 */     return targetType;
/*      */   }
/*      */ 
/*      */   protected Class<?> determineTargetType(String beanName, RootBeanDefinition mbd, Class<?>[] typesToMatch)
/*      */   {
/*  625 */     Class targetType = mbd.getTargetType();
/*  626 */     if (targetType == null)
/*      */     {
/*  628 */       targetType = mbd.getFactoryMethodName() != null ? getTypeForFactoryMethod(beanName, mbd, typesToMatch) : 
/*  628 */         resolveBeanClass(mbd, beanName, typesToMatch);
/*      */ 
/*  629 */       if ((ObjectUtils.isEmpty(typesToMatch)) || (getTempClassLoader() == null)) {
/*  630 */         mbd.setTargetType(targetType);
/*      */       }
/*      */     }
/*  633 */     return targetType;
/*      */   }
/*      */ 
/*      */   protected Class<?> getTypeForFactoryMethod(String beanName, RootBeanDefinition mbd, Class<?>[] typesToMatch)
/*      */   {
/*  651 */     Class preResolved = mbd.resolvedFactoryMethodReturnType;
/*  652 */     if (preResolved != null) {
/*  653 */       return preResolved;
/*      */     }
/*      */ 
/*  657 */     boolean isStatic = true;
/*      */ 
/*  659 */     String factoryBeanName = mbd.getFactoryBeanName();
/*      */     Class factoryClass;
/*  660 */     if (factoryBeanName != null) {
/*  661 */       if (factoryBeanName.equals(beanName)) {
/*  662 */         throw new BeanDefinitionStoreException(mbd.getResourceDescription(), beanName, "factory-bean reference points back to the same bean definition");
/*      */       }
/*      */ 
/*  666 */       Class factoryClass = getType(factoryBeanName);
/*  667 */       isStatic = false;
/*      */     }
/*      */     else
/*      */     {
/*  671 */       factoryClass = resolveBeanClass(mbd, beanName, typesToMatch);
/*      */     }
/*      */ 
/*  674 */     if (factoryClass == null) {
/*  675 */       return null;
/*      */     }
/*      */ 
/*  680 */     Class commonType = null;
/*  681 */     boolean cache = false;
/*  682 */     int minNrOfArgs = mbd.getConstructorArgumentValues().getArgumentCount();
/*  683 */     Method[] candidates = ReflectionUtils.getUniqueDeclaredMethods(factoryClass);
/*  684 */     for (Method factoryMethod : candidates) {
/*  685 */       if ((Modifier.isStatic(factoryMethod.getModifiers()) == isStatic) && 
/*  686 */         (factoryMethod
/*  686 */         .getName().equals(mbd.getFactoryMethodName())) && 
/*  687 */         (factoryMethod
/*  687 */         .getParameterTypes().length >= minNrOfArgs))
/*      */       {
/*  689 */         if (factoryMethod.getTypeParameters().length > 0) {
/*      */           try
/*      */           {
/*  692 */             Class[] paramTypes = factoryMethod.getParameterTypes();
/*  693 */             String[] paramNames = null;
/*  694 */             ParameterNameDiscoverer pnd = getParameterNameDiscoverer();
/*  695 */             if (pnd != null) {
/*  696 */               paramNames = pnd.getParameterNames(factoryMethod);
/*      */             }
/*  698 */             ConstructorArgumentValues cav = mbd.getConstructorArgumentValues();
/*  699 */             Set usedValueHolders = new HashSet(paramTypes.length);
/*      */ 
/*  701 */             Object[] args = new Object[paramTypes.length];
/*  702 */             for (int i = 0; i < args.length; i++) {
/*  703 */               ConstructorArgumentValues.ValueHolder valueHolder = cav.getArgumentValue(i, paramTypes[i], paramNames != null ? paramNames[i] : null, usedValueHolders);
/*      */ 
/*  705 */               if (valueHolder == null) {
/*  706 */                 valueHolder = cav.getGenericArgumentValue(null, null, usedValueHolders);
/*      */               }
/*  708 */               if (valueHolder != null) {
/*  709 */                 args[i] = valueHolder.getValue();
/*  710 */                 usedValueHolders.add(valueHolder);
/*      */               }
/*      */             }
/*  713 */             Class returnType = AutowireUtils.resolveReturnTypeForFactoryMethod(factoryMethod, args, 
/*  714 */               getBeanClassLoader());
/*  715 */             if (returnType != null) {
/*  716 */               cache = true;
/*  717 */               commonType = ClassUtils.determineCommonAncestor(returnType, commonType);
/*      */             }
/*      */           }
/*      */           catch (Throwable ex) {
/*  721 */             if (this.logger.isDebugEnabled()) {
/*  722 */               this.logger.debug("Failed to resolve generic return type for factory method: " + ex);
/*      */             }
/*      */           }
/*      */         }
/*      */         else {
/*  727 */           commonType = ClassUtils.determineCommonAncestor(factoryMethod.getReturnType(), commonType);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  732 */     if (commonType != null)
/*      */     {
/*  734 */       if (cache) {
/*  735 */         mbd.resolvedFactoryMethodReturnType = commonType;
/*      */       }
/*  737 */       return commonType;
/*      */     }
/*      */ 
/*  741 */     return null;
/*      */   }
/*      */ 
/*      */   protected Class<?> getTypeForFactoryBean(String beanName, RootBeanDefinition mbd)
/*      */   {
/*  759 */     final Object objectType = new Object()
/*      */     {
/*  758 */       Class<?> value = null;
/*      */     };
/*  760 */     String factoryBeanName = mbd.getFactoryBeanName();
/*  761 */     final String factoryMethodName = mbd.getFactoryMethodName();
/*      */ 
/*  763 */     if (factoryBeanName != null) {
/*  764 */       if (factoryMethodName != null)
/*      */       {
/*  766 */         BeanDefinition fbDef = getBeanDefinition(factoryBeanName);
/*  767 */         if (((fbDef instanceof AbstractBeanDefinition)) && (((AbstractBeanDefinition)fbDef).hasBeanClass()))
/*      */         {
/*  769 */           Class fbClass = ClassUtils.getUserClass(((AbstractBeanDefinition)fbDef).getBeanClass());
/*      */ 
/*  772 */           ReflectionUtils.doWithMethods(fbClass, new ReflectionUtils.MethodCallback()
/*      */           {
/*      */             public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException
/*      */             {
/*  776 */               if ((method.getName().equals(factoryMethodName)) && 
/*  777 */                 (FactoryBean.class
/*  777 */                 .isAssignableFrom(method
/*  777 */                 .getReturnType())))
/*  778 */                 objectType.value = GenericTypeResolver.resolveReturnTypeArgument(method, FactoryBean.class);
/*      */             }
/*      */           });
/*  782 */           if ((objectType.value != null) && (Object.class != objectType.value)) {
/*  783 */             return objectType.value;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  790 */       if (!isBeanEligibleForMetadataCaching(factoryBeanName)) {
/*  791 */         return null;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  797 */     FactoryBean fb = mbd.isSingleton() ? 
/*  796 */       getSingletonFactoryBeanForTypeCheck(beanName, mbd) : 
/*  797 */       getNonSingletonFactoryBeanForTypeCheck(beanName, mbd);
/*      */ 
/*  799 */     if (fb != null)
/*      */     {
/*  801 */       objectType.value = getTypeForFactoryBean(fb);
/*  802 */       if (objectType.value != null) {
/*  803 */         return objectType.value;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  808 */     return super.getTypeForFactoryBean(beanName, mbd);
/*      */   }
/*      */ 
/*      */   protected Object getEarlyBeanReference(String beanName, RootBeanDefinition mbd, Object bean)
/*      */   {
/*  820 */     Object exposedObject = bean;
/*  821 */     if ((bean != null) && (!mbd.isSynthetic()) && (hasInstantiationAwareBeanPostProcessors())) {
/*  822 */       for (BeanPostProcessor bp : getBeanPostProcessors()) {
/*  823 */         if ((bp instanceof SmartInstantiationAwareBeanPostProcessor)) {
/*  824 */           SmartInstantiationAwareBeanPostProcessor ibp = (SmartInstantiationAwareBeanPostProcessor)bp;
/*  825 */           exposedObject = ibp.getEarlyBeanReference(exposedObject, beanName);
/*  826 */           if (exposedObject == null) {
/*  827 */             return exposedObject;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  832 */     return exposedObject;
/*      */   }
/*      */ 
/*      */   private FactoryBean<?> getSingletonFactoryBeanForTypeCheck(String beanName, RootBeanDefinition mbd)
/*      */   {
/*  849 */     synchronized (getSingletonMutex()) {
/*  850 */       BeanWrapper bw = (BeanWrapper)this.factoryBeanInstanceCache.get(beanName);
/*  851 */       if (bw != null) {
/*  852 */         return (FactoryBean)bw.getWrappedInstance();
/*      */       }
/*  854 */       if ((isSingletonCurrentlyInCreation(beanName)) || (
/*  855 */         (mbd
/*  855 */         .getFactoryBeanName() != null) && (isSingletonCurrentlyInCreation(mbd.getFactoryBeanName())))) {
/*  856 */         return null;
/*      */       }
/*  858 */       Object instance = null;
/*      */       try
/*      */       {
/*  861 */         beforeSingletonCreation(beanName);
/*      */ 
/*  863 */         instance = resolveBeforeInstantiation(beanName, mbd);
/*  864 */         if (instance == null) {
/*  865 */           bw = createBeanInstance(beanName, mbd, null);
/*  866 */           instance = bw.getWrappedInstance();
/*      */         }
/*      */       }
/*      */       finally
/*      */       {
/*  871 */         afterSingletonCreation(beanName);
/*      */       }
/*  873 */       FactoryBean fb = getFactoryBean(beanName, instance);
/*  874 */       if (bw != null) {
/*  875 */         this.factoryBeanInstanceCache.put(beanName, bw);
/*      */       }
/*  877 */       return fb;
/*      */     }
/*      */   }
/*      */ 
/*      */   private FactoryBean<?> getNonSingletonFactoryBeanForTypeCheck(String beanName, RootBeanDefinition mbd)
/*      */   {
/*  890 */     if (isPrototypeCurrentlyInCreation(beanName)) {
/*  891 */       return null;
/*      */     }
/*  893 */     Object instance = null;
/*      */     try
/*      */     {
/*  896 */       beforePrototypeCreation(beanName);
/*      */ 
/*  898 */       instance = resolveBeforeInstantiation(beanName, mbd);
/*  899 */       if (instance == null) {
/*  900 */         BeanWrapper bw = createBeanInstance(beanName, mbd, null);
/*  901 */         instance = bw.getWrappedInstance();
/*      */       }
/*      */     }
/*      */     catch (BeanCreationException ex)
/*      */     {
/*  906 */       if (this.logger.isDebugEnabled()) {
/*  907 */         this.logger.debug("Bean creation exception on non-singleton FactoryBean type check: " + ex);
/*      */       }
/*  909 */       onSuppressedException(ex);
/*  910 */       return null;
/*      */     }
/*      */     finally
/*      */     {
/*  914 */       afterPrototypeCreation(beanName);
/*      */     }
/*  916 */     return getFactoryBean(beanName, instance);
/*      */   }
/*      */ 
/*      */   protected void applyMergedBeanDefinitionPostProcessors(RootBeanDefinition mbd, Class<?> beanType, String beanName)
/*      */     throws BeansException
/*      */   {
/*      */     try
/*      */     {
/*  932 */       for (BeanPostProcessor bp : getBeanPostProcessors())
/*  933 */         if ((bp instanceof MergedBeanDefinitionPostProcessor)) {
/*  934 */           MergedBeanDefinitionPostProcessor bdp = (MergedBeanDefinitionPostProcessor)bp;
/*  935 */           bdp.postProcessMergedBeanDefinition(mbd, beanType, beanName);
/*      */         }
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/*  940 */       throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Post-processing failed of bean type [" + beanType + "] failed", ex);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Object resolveBeforeInstantiation(String beanName, RootBeanDefinition mbd)
/*      */   {
/*  953 */     Object bean = null;
/*  954 */     if (!Boolean.FALSE.equals(mbd.beforeInstantiationResolved))
/*      */     {
/*  956 */       if ((!mbd.isSynthetic()) && (hasInstantiationAwareBeanPostProcessors())) {
/*  957 */         Class targetType = determineTargetType(beanName, mbd, new Class[0]);
/*  958 */         if (targetType != null) {
/*  959 */           bean = applyBeanPostProcessorsBeforeInstantiation(targetType, beanName);
/*  960 */           if (bean != null) {
/*  961 */             bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
/*      */           }
/*      */         }
/*      */       }
/*  965 */       mbd.beforeInstantiationResolved = Boolean.valueOf(bean != null);
/*      */     }
/*  967 */     return bean;
/*      */   }
/*      */ 
/*      */   protected Object applyBeanPostProcessorsBeforeInstantiation(Class<?> beanClass, String beanName)
/*      */     throws BeansException
/*      */   {
/*  985 */     for (BeanPostProcessor bp : getBeanPostProcessors()) {
/*  986 */       if ((bp instanceof InstantiationAwareBeanPostProcessor)) {
/*  987 */         InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor)bp;
/*  988 */         Object result = ibp.postProcessBeforeInstantiation(beanClass, beanName);
/*  989 */         if (result != null) {
/*  990 */           return result;
/*      */         }
/*      */       }
/*      */     }
/*  994 */     return null;
/*      */   }
/*      */ 
/*      */   protected BeanWrapper createBeanInstance(String beanName, RootBeanDefinition mbd, Object[] args)
/*      */   {
/* 1010 */     Class beanClass = resolveBeanClass(mbd, beanName, new Class[0]);
/*      */ 
/* 1012 */     if ((beanClass != null) && (!Modifier.isPublic(beanClass.getModifiers())) && (!mbd.isNonPublicAccessAllowed()))
/*      */     {
/* 1014 */       throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Bean class isn't public, and non-public access not allowed: " + beanClass
/* 1014 */         .getName());
/*      */     }
/*      */ 
/* 1017 */     if (mbd.getFactoryMethodName() != null) {
/* 1018 */       return instantiateUsingFactoryMethod(beanName, mbd, args);
/*      */     }
/*      */ 
/* 1022 */     boolean resolved = false;
/* 1023 */     boolean autowireNecessary = false;
/* 1024 */     if (args == null) {
/* 1025 */       synchronized (mbd.constructorArgumentLock) {
/* 1026 */         if (mbd.resolvedConstructorOrFactoryMethod != null) {
/* 1027 */           resolved = true;
/* 1028 */           autowireNecessary = mbd.constructorArgumentsResolved;
/*      */         }
/*      */       }
/*      */     }
/* 1032 */     if (resolved) {
/* 1033 */       if (autowireNecessary) {
/* 1034 */         return autowireConstructor(beanName, mbd, null, null);
/*      */       }
/*      */ 
/* 1037 */       return instantiateBean(beanName, mbd);
/*      */     }
/*      */ 
/* 1042 */     Constructor[] ctors = determineConstructorsFromBeanPostProcessors(beanClass, beanName);
/* 1043 */     if ((ctors != null) || 
/* 1044 */       (mbd
/* 1044 */       .getResolvedAutowireMode() == 3) || 
/* 1045 */       (mbd
/* 1045 */       .hasConstructorArgumentValues()) || (!ObjectUtils.isEmpty(args))) {
/* 1046 */       return autowireConstructor(beanName, mbd, ctors, args);
/*      */     }
/*      */ 
/* 1050 */     return instantiateBean(beanName, mbd);
/*      */   }
/*      */ 
/*      */   protected Constructor<?>[] determineConstructorsFromBeanPostProcessors(Class<?> beanClass, String beanName)
/*      */     throws BeansException
/*      */   {
/* 1065 */     if ((beanClass != null) && (hasInstantiationAwareBeanPostProcessors())) {
/* 1066 */       for (BeanPostProcessor bp : getBeanPostProcessors()) {
/* 1067 */         if ((bp instanceof SmartInstantiationAwareBeanPostProcessor)) {
/* 1068 */           SmartInstantiationAwareBeanPostProcessor ibp = (SmartInstantiationAwareBeanPostProcessor)bp;
/* 1069 */           Constructor[] ctors = ibp.determineCandidateConstructors(beanClass, beanName);
/* 1070 */           if (ctors != null) {
/* 1071 */             return ctors;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1076 */     return null;
/*      */   }
/*      */ 
/*      */   protected BeanWrapper instantiateBean(final String beanName, final RootBeanDefinition mbd)
/*      */   {
/*      */     try
/*      */     {
/* 1088 */       final BeanFactory parent = this;
/*      */       Object beanInstance;
/*      */       Object beanInstance;
/* 1089 */       if (System.getSecurityManager() != null) {
/* 1090 */         beanInstance = AccessController.doPrivileged(new PrivilegedAction()
/*      */         {
/*      */           public Object run() {
/* 1093 */             return AbstractAutowireCapableBeanFactory.this.getInstantiationStrategy().instantiate(mbd, beanName, parent);
/*      */           }
/*      */         }
/*      */         , getAccessControlContext());
/*      */       }
/*      */       else {
/* 1098 */         beanInstance = getInstantiationStrategy().instantiate(mbd, beanName, parent);
/*      */       }
/* 1100 */       BeanWrapper bw = new BeanWrapperImpl(beanInstance);
/* 1101 */       initBeanWrapper(bw);
/* 1102 */       return bw;
/*      */     }
/*      */     catch (Throwable ex) {
/* 1105 */       throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Instantiation of bean failed", ex);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected BeanWrapper instantiateUsingFactoryMethod(String beanName, RootBeanDefinition mbd, Object[] explicitArgs)
/*      */   {
/* 1123 */     return new ConstructorResolver(this).instantiateUsingFactoryMethod(beanName, mbd, explicitArgs);
/*      */   }
/*      */ 
/*      */   protected BeanWrapper autowireConstructor(String beanName, RootBeanDefinition mbd, Constructor<?>[] ctors, Object[] explicitArgs)
/*      */   {
/* 1143 */     return new ConstructorResolver(this).autowireConstructor(beanName, mbd, ctors, explicitArgs);
/*      */   }
/*      */ 
/*      */   protected void populateBean(String beanName, RootBeanDefinition mbd, BeanWrapper bw)
/*      */   {
/* 1154 */     PropertyValues pvs = mbd.getPropertyValues();
/*      */ 
/* 1156 */     if (bw == null) {
/* 1157 */       if (!pvs.isEmpty())
/*      */       {
/* 1159 */         throw new BeanCreationException(mbd
/* 1159 */           .getResourceDescription(), beanName, "Cannot apply property values to null instance");
/*      */       }
/*      */ 
/* 1163 */       return;
/*      */     }
/*      */ 
/* 1170 */     boolean continueWithPropertyPopulation = true;
/*      */ 
/* 1172 */     if ((!mbd.isSynthetic()) && (hasInstantiationAwareBeanPostProcessors())) {
/* 1173 */       for (BeanPostProcessor bp : getBeanPostProcessors()) {
/* 1174 */         if ((bp instanceof InstantiationAwareBeanPostProcessor)) {
/* 1175 */           InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor)bp;
/* 1176 */           if (!ibp.postProcessAfterInstantiation(bw.getWrappedInstance(), beanName)) {
/* 1177 */             continueWithPropertyPopulation = false;
/* 1178 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1184 */     if (!continueWithPropertyPopulation) {
/* 1185 */       return;
/*      */     }
/*      */ 
/* 1188 */     if ((mbd.getResolvedAutowireMode() == 1) || 
/* 1189 */       (mbd
/* 1189 */       .getResolvedAutowireMode() == 2)) {
/* 1190 */       MutablePropertyValues newPvs = new MutablePropertyValues(pvs);
/*      */ 
/* 1193 */       if (mbd.getResolvedAutowireMode() == 1) {
/* 1194 */         autowireByName(beanName, mbd, bw, newPvs);
/*      */       }
/*      */ 
/* 1198 */       if (mbd.getResolvedAutowireMode() == 2) {
/* 1199 */         autowireByType(beanName, mbd, bw, newPvs);
/*      */       }
/*      */ 
/* 1202 */       pvs = newPvs;
/*      */     }
/*      */ 
/* 1205 */     boolean hasInstAwareBpps = hasInstantiationAwareBeanPostProcessors();
/* 1206 */     boolean needsDepCheck = mbd.getDependencyCheck() != 0;
/*      */ 
/* 1208 */     if ((hasInstAwareBpps) || (needsDepCheck)) {
/* 1209 */       PropertyDescriptor[] filteredPds = filterPropertyDescriptorsForDependencyCheck(bw, mbd.allowCaching);
/* 1210 */       if (hasInstAwareBpps) {
/* 1211 */         for (BeanPostProcessor bp : getBeanPostProcessors()) {
/* 1212 */           if ((bp instanceof InstantiationAwareBeanPostProcessor)) {
/* 1213 */             InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor)bp;
/* 1214 */             pvs = ibp.postProcessPropertyValues(pvs, filteredPds, bw.getWrappedInstance(), beanName);
/* 1215 */             if (pvs == null) {
/* 1216 */               return;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 1221 */       if (needsDepCheck) {
/* 1222 */         checkDependencies(beanName, mbd, filteredPds, pvs);
/*      */       }
/*      */     }
/*      */ 
/* 1226 */     applyPropertyValues(beanName, mbd, bw, pvs);
/*      */   }
/*      */ 
/*      */   protected void autowireByName(String beanName, AbstractBeanDefinition mbd, BeanWrapper bw, MutablePropertyValues pvs)
/*      */   {
/* 1241 */     String[] propertyNames = unsatisfiedNonSimpleProperties(mbd, bw);
/* 1242 */     for (String propertyName : propertyNames)
/* 1243 */       if (containsBean(propertyName)) {
/* 1244 */         Object bean = getBean(propertyName);
/* 1245 */         pvs.add(propertyName, bean);
/* 1246 */         registerDependentBean(propertyName, beanName);
/* 1247 */         if (this.logger.isDebugEnabled()) {
/* 1248 */           this.logger.debug("Added autowiring by name from bean name '" + beanName + "' via property '" + propertyName + "' to bean named '" + propertyName + "'");
/*      */         }
/*      */ 
/*      */       }
/* 1253 */       else if (this.logger.isTraceEnabled()) {
/* 1254 */         this.logger.trace("Not autowiring property '" + propertyName + "' of bean '" + beanName + "' by name: no matching bean found");
/*      */       }
/*      */   }
/*      */ 
/*      */   protected void autowireByType(String beanName, AbstractBeanDefinition mbd, BeanWrapper bw, MutablePropertyValues pvs)
/*      */   {
/* 1275 */     TypeConverter converter = getCustomTypeConverter();
/* 1276 */     if (converter == null) {
/* 1277 */       converter = bw;
/*      */     }
/*      */ 
/* 1280 */     Set autowiredBeanNames = new LinkedHashSet(4);
/* 1281 */     String[] propertyNames = unsatisfiedNonSimpleProperties(mbd, bw);
/* 1282 */     for (String propertyName : propertyNames)
/*      */       try {
/* 1284 */         PropertyDescriptor pd = bw.getPropertyDescriptor(propertyName);
/*      */ 
/* 1287 */         if (Object.class != pd.getPropertyType()) {
/* 1288 */           MethodParameter methodParam = BeanUtils.getWriteMethodParameter(pd);
/*      */ 
/* 1290 */           boolean eager = !PriorityOrdered.class.isAssignableFrom(bw.getWrappedClass());
/* 1291 */           DependencyDescriptor desc = new AutowireByTypeDependencyDescriptor(methodParam, eager);
/* 1292 */           Object autowiredArgument = resolveDependency(desc, beanName, autowiredBeanNames, converter);
/* 1293 */           if (autowiredArgument != null) {
/* 1294 */             pvs.add(propertyName, autowiredArgument);
/*      */           }
/* 1296 */           for (String autowiredBeanName : autowiredBeanNames) {
/* 1297 */             registerDependentBean(autowiredBeanName, beanName);
/* 1298 */             if (this.logger.isDebugEnabled()) {
/* 1299 */               this.logger.debug("Autowiring by type from bean name '" + beanName + "' via property '" + propertyName + "' to bean named '" + autowiredBeanName + "'");
/*      */             }
/*      */           }
/*      */ 
/* 1303 */           autowiredBeanNames.clear();
/*      */         }
/*      */       }
/*      */       catch (BeansException ex) {
/* 1307 */         throw new UnsatisfiedDependencyException(mbd.getResourceDescription(), beanName, propertyName, ex);
/*      */       }
/*      */   }
/*      */ 
/*      */   protected String[] unsatisfiedNonSimpleProperties(AbstractBeanDefinition mbd, BeanWrapper bw)
/*      */   {
/* 1323 */     Set result = new TreeSet();
/* 1324 */     PropertyValues pvs = mbd.getPropertyValues();
/* 1325 */     PropertyDescriptor[] pds = bw.getPropertyDescriptors();
/* 1326 */     for (PropertyDescriptor pd : pds) {
/* 1327 */       if ((pd.getWriteMethod() != null) && (!isExcludedFromDependencyCheck(pd)) && (!pvs.contains(pd.getName())) && 
/* 1328 */         (!BeanUtils.isSimpleProperty(pd
/* 1328 */         .getPropertyType()))) {
/* 1329 */         result.add(pd.getName());
/*      */       }
/*      */     }
/* 1332 */     return StringUtils.toStringArray(result);
/*      */   }
/*      */ 
/*      */   protected PropertyDescriptor[] filterPropertyDescriptorsForDependencyCheck(BeanWrapper bw, boolean cache)
/*      */   {
/* 1345 */     PropertyDescriptor[] filtered = (PropertyDescriptor[])this.filteredPropertyDescriptorsCache.get(bw.getWrappedClass());
/* 1346 */     if (filtered == null) {
/* 1347 */       filtered = filterPropertyDescriptorsForDependencyCheck(bw);
/* 1348 */       if (cache)
/*      */       {
/* 1350 */         PropertyDescriptor[] existing = (PropertyDescriptor[])this.filteredPropertyDescriptorsCache
/* 1350 */           .putIfAbsent(bw
/* 1350 */           .getWrappedClass(), filtered);
/* 1351 */         if (existing != null) {
/* 1352 */           filtered = existing;
/*      */         }
/*      */       }
/*      */     }
/* 1356 */     return filtered;
/*      */   }
/*      */ 
/*      */   protected PropertyDescriptor[] filterPropertyDescriptorsForDependencyCheck(BeanWrapper bw)
/*      */   {
/* 1368 */     List pds = new LinkedList(
/* 1368 */       Arrays.asList(bw
/* 1368 */       .getPropertyDescriptors()));
/* 1369 */     for (Iterator it = pds.iterator(); it.hasNext(); ) {
/* 1370 */       PropertyDescriptor pd = (PropertyDescriptor)it.next();
/* 1371 */       if (isExcludedFromDependencyCheck(pd)) {
/* 1372 */         it.remove();
/*      */       }
/*      */     }
/* 1375 */     return (PropertyDescriptor[])pds.toArray(new PropertyDescriptor[pds.size()]);
/*      */   }
/*      */ 
/*      */   protected boolean isExcludedFromDependencyCheck(PropertyDescriptor pd)
/*      */   {
/* 1391 */     return (AutowireUtils.isExcludedFromDependencyCheck(pd)) || 
/* 1390 */       (this.ignoredDependencyTypes
/* 1390 */       .contains(pd
/* 1390 */       .getPropertyType())) || 
/* 1391 */       (AutowireUtils.isSetterDefinedInInterface(pd, this.ignoredDependencyInterfaces));
/*      */   }
/*      */ 
/*      */   protected void checkDependencies(String beanName, AbstractBeanDefinition mbd, PropertyDescriptor[] pds, PropertyValues pvs)
/*      */     throws UnsatisfiedDependencyException
/*      */   {
/* 1408 */     int dependencyCheck = mbd.getDependencyCheck();
/* 1409 */     for (PropertyDescriptor pd : pds)
/* 1410 */       if ((pd.getWriteMethod() != null) && (!pvs.contains(pd.getName()))) {
/* 1411 */         boolean isSimple = BeanUtils.isSimpleProperty(pd.getPropertyType());
/* 1412 */         boolean unsatisfied = (dependencyCheck == 3) || ((isSimple) && (dependencyCheck == 2)) || ((!isSimple) && (dependencyCheck == 1));
/*      */ 
/* 1415 */         if (unsatisfied)
/* 1416 */           throw new UnsatisfiedDependencyException(mbd.getResourceDescription(), beanName, pd.getName(), "Set this property value or disable dependency checking for this bean.");
/*      */       }
/*      */   }
/*      */ 
/*      */   protected void applyPropertyValues(String beanName, BeanDefinition mbd, BeanWrapper bw, PropertyValues pvs)
/*      */   {
/* 1433 */     if ((pvs == null) || (pvs.isEmpty())) {
/* 1434 */       return;
/*      */     }
/*      */ 
/* 1437 */     MutablePropertyValues mpvs = null;
/*      */ 
/* 1440 */     if ((System.getSecurityManager() != null) && 
/* 1441 */       ((bw instanceof BeanWrapperImpl)))
/* 1442 */       ((BeanWrapperImpl)bw).setSecurityContext(getAccessControlContext());
/*      */     List original;
/*      */     List original;
/* 1446 */     if ((pvs instanceof MutablePropertyValues)) {
/* 1447 */       mpvs = (MutablePropertyValues)pvs;
/* 1448 */       if (mpvs.isConverted()) {
/*      */         try
/*      */         {
/* 1451 */           bw.setPropertyValues(mpvs);
/* 1452 */           return;
/*      */         }
/*      */         catch (BeansException ex)
/*      */         {
/* 1456 */           throw new BeanCreationException(mbd
/* 1456 */             .getResourceDescription(), beanName, "Error setting property values", ex);
/*      */         }
/*      */       }
/* 1459 */       original = mpvs.getPropertyValueList();
/*      */     }
/*      */     else {
/* 1462 */       original = Arrays.asList(pvs.getPropertyValues());
/*      */     }
/*      */ 
/* 1465 */     TypeConverter converter = getCustomTypeConverter();
/* 1466 */     if (converter == null) {
/* 1467 */       converter = bw;
/*      */     }
/* 1469 */     BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this, beanName, mbd, converter);
/*      */ 
/* 1472 */     List deepCopy = new ArrayList(original.size());
/* 1473 */     boolean resolveNecessary = false;
/* 1474 */     for (PropertyValue pv : original) {
/* 1475 */       if (pv.isConverted()) {
/* 1476 */         deepCopy.add(pv);
/*      */       }
/*      */       else {
/* 1479 */         String propertyName = pv.getName();
/* 1480 */         Object originalValue = pv.getValue();
/* 1481 */         Object resolvedValue = valueResolver.resolveValueIfNecessary(pv, originalValue);
/* 1482 */         Object convertedValue = resolvedValue;
/*      */ 
/* 1484 */         boolean convertible = (bw.isWritableProperty(propertyName)) && 
/* 1484 */           (!PropertyAccessorUtils.isNestedOrIndexedProperty(propertyName));
/*      */ 
/* 1485 */         if (convertible) {
/* 1486 */           convertedValue = convertForProperty(resolvedValue, propertyName, bw, converter);
/*      */         }
/*      */ 
/* 1490 */         if (resolvedValue == originalValue) {
/* 1491 */           if (convertible) {
/* 1492 */             pv.setConvertedValue(convertedValue);
/*      */           }
/* 1494 */           deepCopy.add(pv);
/*      */         }
/* 1496 */         else if ((convertible) && ((originalValue instanceof TypedStringValue)) && 
/* 1497 */           (!((TypedStringValue)originalValue)
/* 1497 */           .isDynamic()) && (!(convertedValue instanceof Collection)) && 
/* 1498 */           (!ObjectUtils.isArray(convertedValue)))
/*      */         {
/* 1499 */           pv.setConvertedValue(convertedValue);
/* 1500 */           deepCopy.add(pv);
/*      */         }
/*      */         else {
/* 1503 */           resolveNecessary = true;
/* 1504 */           deepCopy.add(new PropertyValue(pv, convertedValue));
/*      */         }
/*      */       }
/*      */     }
/* 1508 */     if ((mpvs != null) && (!resolveNecessary)) {
/* 1509 */       mpvs.setConverted();
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1514 */       bw.setPropertyValues(new MutablePropertyValues(deepCopy));
/*      */     }
/*      */     catch (BeansException ex)
/*      */     {
/* 1518 */       throw new BeanCreationException(mbd
/* 1518 */         .getResourceDescription(), beanName, "Error setting property values", ex);
/*      */     }
/*      */   }
/*      */ 
/*      */   private Object convertForProperty(Object value, String propertyName, BeanWrapper bw, TypeConverter converter)
/*      */   {
/* 1526 */     if ((converter instanceof BeanWrapperImpl)) {
/* 1527 */       return ((BeanWrapperImpl)converter).convertForProperty(value, propertyName);
/*      */     }
/*      */ 
/* 1530 */     PropertyDescriptor pd = bw.getPropertyDescriptor(propertyName);
/* 1531 */     MethodParameter methodParam = BeanUtils.getWriteMethodParameter(pd);
/* 1532 */     return converter.convertIfNecessary(value, pd.getPropertyType(), methodParam);
/*      */   }
/*      */ 
/*      */   protected Object initializeBean(final String beanName, final Object bean, RootBeanDefinition mbd)
/*      */   {
/* 1555 */     if (System.getSecurityManager() != null) {
/* 1556 */       AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public Object run() {
/* 1559 */           AbstractAutowireCapableBeanFactory.this.invokeAwareMethods(beanName, bean);
/* 1560 */           return null;
/*      */         }
/*      */       }
/*      */       , getAccessControlContext());
/*      */     }
/*      */     else {
/* 1565 */       invokeAwareMethods(beanName, bean);
/*      */     }
/*      */ 
/* 1568 */     Object wrappedBean = bean;
/* 1569 */     if ((mbd == null) || (!mbd.isSynthetic())) {
/* 1570 */       wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
/*      */     }
/*      */     try
/*      */     {
/* 1574 */       invokeInitMethods(beanName, wrappedBean, mbd);
/*      */     }
/*      */     catch (Throwable ex)
/*      */     {
/* 1578 */       throw new BeanCreationException(mbd != null ? mbd
/* 1578 */         .getResourceDescription() : null, beanName, "Invocation of init method failed", ex);
/*      */     }
/*      */ 
/* 1582 */     if ((mbd == null) || (!mbd.isSynthetic())) {
/* 1583 */       wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
/*      */     }
/* 1585 */     return wrappedBean;
/*      */   }
/*      */ 
/*      */   private void invokeAwareMethods(String beanName, Object bean) {
/* 1589 */     if ((bean instanceof Aware)) {
/* 1590 */       if ((bean instanceof BeanNameAware)) {
/* 1591 */         ((BeanNameAware)bean).setBeanName(beanName);
/*      */       }
/* 1593 */       if ((bean instanceof BeanClassLoaderAware)) {
/* 1594 */         ((BeanClassLoaderAware)bean).setBeanClassLoader(getBeanClassLoader());
/*      */       }
/* 1596 */       if ((bean instanceof BeanFactoryAware))
/* 1597 */         ((BeanFactoryAware)bean).setBeanFactory(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void invokeInitMethods(String beanName, final Object bean, RootBeanDefinition mbd)
/*      */     throws Throwable
/*      */   {
/* 1617 */     boolean isInitializingBean = bean instanceof InitializingBean;
/* 1618 */     if ((isInitializingBean) && ((mbd == null) || (!mbd.isExternallyManagedInitMethod("afterPropertiesSet")))) {
/* 1619 */       if (this.logger.isDebugEnabled()) {
/* 1620 */         this.logger.debug("Invoking afterPropertiesSet() on bean with name '" + beanName + "'");
/*      */       }
/* 1622 */       if (System.getSecurityManager() != null) {
/*      */         try {
/* 1624 */           AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */           {
/*      */             public Object run() throws Exception {
/* 1627 */               ((InitializingBean)bean).afterPropertiesSet();
/* 1628 */               return null;
/*      */             }
/*      */           }
/*      */           , getAccessControlContext());
/*      */         }
/*      */         catch (PrivilegedActionException pae) {
/* 1633 */           throw pae.getException();
/*      */         }
/*      */       }
/*      */       else {
/* 1637 */         ((InitializingBean)bean).afterPropertiesSet();
/*      */       }
/*      */     }
/*      */ 
/* 1641 */     if (mbd != null) {
/* 1642 */       String initMethodName = mbd.getInitMethodName();
/* 1643 */       if ((initMethodName != null) && ((!isInitializingBean) || (!"afterPropertiesSet".equals(initMethodName))) && 
/* 1644 */         (!mbd
/* 1644 */         .isExternallyManagedInitMethod(initMethodName)))
/*      */       {
/* 1645 */         invokeCustomInitMethod(beanName, bean, mbd);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void invokeCustomInitMethod(String beanName, final Object bean, RootBeanDefinition mbd)
/*      */     throws Throwable
/*      */   {
/* 1658 */     String initMethodName = mbd.getInitMethodName();
/*      */ 
/* 1661 */     final Method initMethod = mbd.isNonPublicAccessAllowed() ? 
/* 1660 */       BeanUtils.findMethod(bean
/* 1660 */       .getClass(), initMethodName, new Class[0]) : 
/* 1661 */       ClassUtils.getMethodIfAvailable(bean
/* 1661 */       .getClass(), initMethodName, new Class[0]);
/* 1662 */     if (initMethod == null) {
/* 1663 */       if (mbd.isEnforceInitMethod()) {
/* 1664 */         throw new BeanDefinitionValidationException("Couldn't find an init method named '" + initMethodName + "' on bean with name '" + beanName + "'");
/*      */       }
/*      */ 
/* 1668 */       if (this.logger.isDebugEnabled()) {
/* 1669 */         this.logger.debug("No default init method named '" + initMethodName + "' found on bean with name '" + beanName + "'");
/*      */       }
/*      */ 
/* 1673 */       return;
/*      */     }
/*      */ 
/* 1677 */     if (this.logger.isDebugEnabled()) {
/* 1678 */       this.logger.debug("Invoking init method  '" + initMethodName + "' on bean with name '" + beanName + "'");
/*      */     }
/*      */ 
/* 1681 */     if (System.getSecurityManager() != null) {
/* 1682 */       AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */       {
/*      */         public Object run() throws Exception {
/* 1685 */           ReflectionUtils.makeAccessible(initMethod);
/* 1686 */           return null;
/*      */         }
/*      */       });
/*      */       try {
/* 1690 */         AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */         {
/*      */           public Object run() throws Exception {
/* 1693 */             initMethod.invoke(bean, new Object[0]);
/* 1694 */             return null;
/*      */           }
/*      */         }
/*      */         , getAccessControlContext());
/*      */       }
/*      */       catch (PrivilegedActionException pae) {
/* 1699 */         InvocationTargetException ex = (InvocationTargetException)pae.getException();
/* 1700 */         throw ex.getTargetException();
/*      */       }
/*      */     }
/*      */     else {
/*      */       try {
/* 1705 */         ReflectionUtils.makeAccessible(initMethod);
/* 1706 */         initMethod.invoke(bean, new Object[0]);
/*      */       }
/*      */       catch (InvocationTargetException ex) {
/* 1709 */         throw ex.getTargetException();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Object postProcessObjectFromFactoryBean(Object object, String beanName)
/*      */   {
/* 1723 */     return applyBeanPostProcessorsAfterInitialization(object, beanName);
/*      */   }
/*      */ 
/*      */   protected void removeSingleton(String beanName)
/*      */   {
/* 1731 */     super.removeSingleton(beanName);
/* 1732 */     this.factoryBeanInstanceCache.remove(beanName);
/*      */   }
/*      */ 
/*      */   private static class AutowireByTypeDependencyDescriptor extends DependencyDescriptor
/*      */   {
/*      */     public AutowireByTypeDependencyDescriptor(MethodParameter methodParameter, boolean eager)
/*      */     {
/* 1744 */       super(false, eager);
/*      */     }
/*      */ 
/*      */     public String getDependencyName()
/*      */     {
/* 1749 */       return null;
/*      */     }
/*      */   }
/*      */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory
 * JD-Core Version:    0.6.2
 */