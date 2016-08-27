/*      */ package org.springframework.beans.factory.support;
/*      */ 
/*      */ import java.beans.PropertyEditor;
/*      */ import java.security.AccessControlContext;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.springframework.beans.BeanUtils;
/*      */ import org.springframework.beans.BeanWrapper;
/*      */ import org.springframework.beans.BeansException;
/*      */ import org.springframework.beans.PropertyEditorRegistrar;
/*      */ import org.springframework.beans.PropertyEditorRegistry;
/*      */ import org.springframework.beans.PropertyEditorRegistrySupport;
/*      */ import org.springframework.beans.SimpleTypeConverter;
/*      */ import org.springframework.beans.TypeConverter;
/*      */ import org.springframework.beans.TypeMismatchException;
/*      */ import org.springframework.beans.factory.BeanCreationException;
/*      */ import org.springframework.beans.factory.BeanCurrentlyInCreationException;
/*      */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*      */ import org.springframework.beans.factory.BeanFactory;
/*      */ import org.springframework.beans.factory.BeanFactoryUtils;
/*      */ import org.springframework.beans.factory.BeanIsAbstractException;
/*      */ import org.springframework.beans.factory.BeanIsNotAFactoryException;
/*      */ import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
/*      */ import org.springframework.beans.factory.CannotLoadBeanClassException;
/*      */ import org.springframework.beans.factory.FactoryBean;
/*      */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*      */ import org.springframework.beans.factory.ObjectFactory;
/*      */ import org.springframework.beans.factory.SmartFactoryBean;
/*      */ import org.springframework.beans.factory.config.BeanDefinition;
/*      */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*      */ import org.springframework.beans.factory.config.BeanExpressionContext;
/*      */ import org.springframework.beans.factory.config.BeanExpressionResolver;
/*      */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*      */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*      */ import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
/*      */ import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
/*      */ import org.springframework.beans.factory.config.Scope;
/*      */ import org.springframework.core.DecoratingClassLoader;
/*      */ import org.springframework.core.NamedThreadLocal;
/*      */ import org.springframework.core.ResolvableType;
/*      */ import org.springframework.core.convert.ConversionService;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.ObjectUtils;
/*      */ import org.springframework.util.StringUtils;
/*      */ import org.springframework.util.StringValueResolver;
/*      */ 
/*      */ public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport
/*      */   implements ConfigurableBeanFactory
/*      */ {
/*      */   private BeanFactory parentBeanFactory;
/*  119 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*      */   private ClassLoader tempClassLoader;
/*  125 */   private boolean cacheBeanMetadata = true;
/*      */   private BeanExpressionResolver beanExpressionResolver;
/*      */   private ConversionService conversionService;
/*  134 */   private final Set<PropertyEditorRegistrar> propertyEditorRegistrars = new LinkedHashSet(4);
/*      */   private TypeConverter typeConverter;
/*  141 */   private final Map<Class<?>, Class<? extends PropertyEditor>> customEditors = new HashMap(4);
/*      */ 
/*  145 */   private final List<StringValueResolver> embeddedValueResolvers = new LinkedList();
/*      */ 
/*  148 */   private final List<BeanPostProcessor> beanPostProcessors = new ArrayList();
/*      */   private boolean hasInstantiationAwareBeanPostProcessors;
/*      */   private boolean hasDestructionAwareBeanPostProcessors;
/*  157 */   private final Map<String, Scope> scopes = new LinkedHashMap(8);
/*      */   private SecurityContextProvider securityContextProvider;
/*  163 */   private final Map<String, RootBeanDefinition> mergedBeanDefinitions = new ConcurrentHashMap(256);
/*      */ 
/*  168 */   private final Set<String> alreadyCreated = Collections.newSetFromMap(new ConcurrentHashMap(256))
/*  168 */     ;
/*      */ 
/*  171 */   private final ThreadLocal<Object> prototypesCurrentlyInCreation = new NamedThreadLocal("Prototype beans currently in creation");
/*      */ 
/*      */   public AbstractBeanFactory()
/*      */   {
/*      */   }
/*      */ 
/*      */   public AbstractBeanFactory(BeanFactory parentBeanFactory)
/*      */   {
/*  187 */     this.parentBeanFactory = parentBeanFactory;
/*      */   }
/*      */ 
/*      */   public Object getBean(String name)
/*      */     throws BeansException
/*      */   {
/*  197 */     return doGetBean(name, null, null, false);
/*      */   }
/*      */ 
/*      */   public <T> T getBean(String name, Class<T> requiredType) throws BeansException
/*      */   {
/*  202 */     return doGetBean(name, requiredType, null, false);
/*      */   }
/*      */ 
/*      */   public Object getBean(String name, Object[] args) throws BeansException
/*      */   {
/*  207 */     return doGetBean(name, null, args, false);
/*      */   }
/*      */ 
/*      */   public <T> T getBean(String name, Class<T> requiredType, Object[] args)
/*      */     throws BeansException
/*      */   {
/*  220 */     return doGetBean(name, requiredType, args, false);
/*      */   }
/*      */ 
/*      */   protected <T> T doGetBean(String name, Class<T> requiredType, final Object[] args, boolean typeCheckOnly)
/*      */     throws BeansException
/*      */   {
/*  239 */     final String beanName = transformedBeanName(name);
/*      */ 
/*  243 */     Object sharedInstance = getSingleton(beanName);
/*      */     Object bean;
/*  244 */     if ((sharedInstance != null) && (args == null)) {
/*  245 */       if (this.logger.isDebugEnabled()) {
/*  246 */         if (isSingletonCurrentlyInCreation(beanName)) {
/*  247 */           this.logger.debug(new StringBuilder().append("Returning eagerly cached instance of singleton bean '").append(beanName).append("' that is not fully initialized yet - a consequence of a circular reference").toString());
/*      */         }
/*      */         else
/*      */         {
/*  251 */           this.logger.debug(new StringBuilder().append("Returning cached instance of singleton bean '").append(beanName).append("'").toString());
/*      */         }
/*      */       }
/*  254 */       bean = getObjectForBeanInstance(sharedInstance, name, beanName, null);
/*      */     }
/*      */     else
/*      */     {
/*  260 */       if (isPrototypeCurrentlyInCreation(beanName)) {
/*  261 */         throw new BeanCurrentlyInCreationException(beanName);
/*      */       }
/*      */ 
/*  265 */       BeanFactory parentBeanFactory = getParentBeanFactory();
/*  266 */       if ((parentBeanFactory != null) && (!containsBeanDefinition(beanName)))
/*      */       {
/*  268 */         String nameToLookup = originalBeanName(name);
/*  269 */         if (args != null)
/*      */         {
/*  271 */           return parentBeanFactory.getBean(nameToLookup, args);
/*      */         }
/*      */ 
/*  275 */         return parentBeanFactory.getBean(nameToLookup, requiredType);
/*      */       }
/*      */ 
/*  279 */       if (!typeCheckOnly) {
/*  280 */         markBeanAsCreated(beanName);
/*      */       }
/*      */       try
/*      */       {
/*  284 */         final RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/*  285 */         checkMergedBeanDefinition(mbd, beanName, args);
/*      */ 
/*  288 */         String[] dependsOn = mbd.getDependsOn();
/*  289 */         if (dependsOn != null)
/*  290 */           for (String dependsOnBean : dependsOn) {
/*  291 */             if (isDependent(beanName, dependsOnBean)) {
/*  292 */               throw new BeanCreationException(mbd.getResourceDescription(), beanName, new StringBuilder().append("Circular depends-on relationship between '").append(beanName).append("' and '").append(dependsOnBean).append("'").toString());
/*      */             }
/*      */ 
/*  295 */             registerDependentBean(dependsOnBean, beanName);
/*  296 */             getBean(dependsOnBean);
/*      */           }
/*      */         Object bean;
/*  301 */         if (mbd.isSingleton()) {
/*  302 */           sharedInstance = getSingleton(beanName, new ObjectFactory()
/*      */           {
/*      */             public Object getObject() throws BeansException {
/*      */               try {
/*  306 */                 return AbstractBeanFactory.this.createBean(beanName, mbd, args);
/*      */               }
/*      */               catch (BeansException ex)
/*      */               {
/*  312 */                 AbstractBeanFactory.this.destroySingleton(beanName);
/*  313 */                 throw ex;
/*      */               }
/*      */             }
/*      */           });
/*  317 */           bean = getObjectForBeanInstance(sharedInstance, name, beanName, mbd);
/*      */         }
/*      */         else
/*      */         {
/*      */           Object bean;
/*  320 */           if (mbd.isPrototype())
/*      */           {
/*  322 */             Object prototypeInstance = null;
/*      */             try {
/*  324 */               beforePrototypeCreation(beanName);
/*  325 */               prototypeInstance = createBean(beanName, mbd, args);
/*      */             }
/*      */             finally {
/*  328 */               afterPrototypeCreation(beanName);
/*      */             }
/*  330 */             bean = getObjectForBeanInstance(prototypeInstance, name, beanName, mbd);
/*      */           }
/*      */           else
/*      */           {
/*  334 */             String scopeName = mbd.getScope();
/*  335 */             Scope scope = (Scope)this.scopes.get(scopeName);
/*  336 */             if (scope == null)
/*  337 */               throw new IllegalStateException(new StringBuilder().append("No Scope registered for scope name '").append(scopeName).append("'").toString());
/*      */             try
/*      */             {
/*  340 */               Object scopedInstance = scope.get(beanName, new ObjectFactory()
/*      */               {
/*      */                 public Object getObject() throws BeansException {
/*  343 */                   AbstractBeanFactory.this.beforePrototypeCreation(beanName);
/*      */                   try {
/*  345 */                     return AbstractBeanFactory.this.createBean(beanName, mbd, args);
/*      */                   }
/*      */                   finally {
/*  348 */                     AbstractBeanFactory.this.afterPrototypeCreation(beanName);
/*      */                   }
/*      */                 }
/*      */               });
/*  352 */               bean = getObjectForBeanInstance(scopedInstance, name, beanName, mbd);
/*      */             }
/*      */             catch (IllegalStateException ex)
/*      */             {
/*      */               Object bean;
/*  355 */               throw new BeanCreationException(beanName, new StringBuilder().append("Scope '").append(scopeName).append("' is not active for the current thread; consider ").append("defining a scoped proxy for this bean if you intend to refer to it from a singleton").toString(), ex);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (BeansException ex)
/*      */       {
/*      */         Object bean;
/*  363 */         cleanupAfterBeanCreationFailure(beanName);
/*  364 */         throw ex;
/*      */       }
/*      */     }
/*      */     Object bean;
/*  369 */     if ((requiredType != null) && (bean != null) && (!requiredType.isAssignableFrom(bean.getClass()))) {
/*      */       try {
/*  371 */         return getTypeConverter().convertIfNecessary(bean, requiredType);
/*      */       }
/*      */       catch (TypeMismatchException ex) {
/*  374 */         if (this.logger.isDebugEnabled()) {
/*  375 */           this.logger.debug(new StringBuilder().append("Failed to convert bean '").append(name).append("' to required type [")
/*  376 */             .append(ClassUtils.getQualifiedName(requiredType))
/*  376 */             .append("]").toString(), ex);
/*      */         }
/*  378 */         throw new BeanNotOfRequiredTypeException(name, requiredType, bean.getClass());
/*      */       }
/*      */     }
/*  381 */     return bean;
/*      */   }
/*      */ 
/*      */   public boolean containsBean(String name)
/*      */   {
/*  386 */     String beanName = transformedBeanName(name);
/*  387 */     if ((containsSingleton(beanName)) || (containsBeanDefinition(beanName))) {
/*  388 */       return (!BeanFactoryUtils.isFactoryDereference(name)) || (isFactoryBean(name));
/*      */     }
/*      */ 
/*  391 */     BeanFactory parentBeanFactory = getParentBeanFactory();
/*  392 */     return (parentBeanFactory != null) && (parentBeanFactory.containsBean(originalBeanName(name)));
/*      */   }
/*      */ 
/*      */   public boolean isSingleton(String name) throws NoSuchBeanDefinitionException
/*      */   {
/*  397 */     String beanName = transformedBeanName(name);
/*      */ 
/*  399 */     Object beanInstance = getSingleton(beanName, false);
/*  400 */     if (beanInstance != null) {
/*  401 */       if ((beanInstance instanceof FactoryBean)) {
/*  402 */         return (BeanFactoryUtils.isFactoryDereference(name)) || (((FactoryBean)beanInstance).isSingleton());
/*      */       }
/*      */ 
/*  405 */       return !BeanFactoryUtils.isFactoryDereference(name);
/*      */     }
/*      */ 
/*  408 */     if (containsSingleton(beanName)) {
/*  409 */       return true;
/*      */     }
/*      */ 
/*  414 */     BeanFactory parentBeanFactory = getParentBeanFactory();
/*  415 */     if ((parentBeanFactory != null) && (!containsBeanDefinition(beanName)))
/*      */     {
/*  417 */       return parentBeanFactory.isSingleton(originalBeanName(name));
/*      */     }
/*      */ 
/*  420 */     RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/*      */ 
/*  423 */     if (mbd.isSingleton()) {
/*  424 */       if (isFactoryBean(beanName, mbd)) {
/*  425 */         if (BeanFactoryUtils.isFactoryDereference(name)) {
/*  426 */           return true;
/*      */         }
/*  428 */         FactoryBean factoryBean = (FactoryBean)getBean(new StringBuilder().append("&").append(beanName).toString());
/*  429 */         return factoryBean.isSingleton();
/*      */       }
/*      */ 
/*  432 */       return !BeanFactoryUtils.isFactoryDereference(name);
/*      */     }
/*      */ 
/*  436 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isPrototype(String name)
/*      */     throws NoSuchBeanDefinitionException
/*      */   {
/*  443 */     String beanName = transformedBeanName(name);
/*      */ 
/*  445 */     BeanFactory parentBeanFactory = getParentBeanFactory();
/*  446 */     if ((parentBeanFactory != null) && (!containsBeanDefinition(beanName)))
/*      */     {
/*  448 */       return parentBeanFactory.isPrototype(originalBeanName(name));
/*      */     }
/*      */ 
/*  451 */     RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/*  452 */     if (mbd.isPrototype())
/*      */     {
/*  454 */       return (!BeanFactoryUtils.isFactoryDereference(name)) || (isFactoryBean(beanName, mbd));
/*      */     }
/*      */ 
/*  459 */     if (BeanFactoryUtils.isFactoryDereference(name)) {
/*  460 */       return false;
/*      */     }
/*  462 */     if (isFactoryBean(beanName, mbd)) {
/*  463 */       final FactoryBean factoryBean = (FactoryBean)getBean(new StringBuilder().append("&").append(beanName).toString());
/*  464 */       if (System.getSecurityManager() != null) {
/*  465 */         return ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*      */         {
/*      */           public Boolean run() {
/*  468 */             return Boolean.valueOf((((factoryBean instanceof SmartFactoryBean)) && (((SmartFactoryBean)factoryBean).isPrototype())) || 
/*  469 */               (!factoryBean
/*  469 */               .isSingleton()));
/*      */           }
/*      */         }
/*      */         , getAccessControlContext())).booleanValue();
/*      */       }
/*      */ 
/*  475 */       return (((factoryBean instanceof SmartFactoryBean)) && (((SmartFactoryBean)factoryBean).isPrototype())) || 
/*  475 */         (!factoryBean
/*  475 */         .isSingleton());
/*      */     }
/*      */ 
/*  479 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isTypeMatch(String name, ResolvableType typeToMatch)
/*      */     throws NoSuchBeanDefinitionException
/*      */   {
/*  486 */     String beanName = transformedBeanName(name);
/*      */ 
/*  489 */     Object beanInstance = getSingleton(beanName, false);
/*  490 */     if (beanInstance != null) {
/*  491 */       if ((beanInstance instanceof FactoryBean)) {
/*  492 */         if (!BeanFactoryUtils.isFactoryDereference(name)) {
/*  493 */           Class type = getTypeForFactoryBean((FactoryBean)beanInstance);
/*  494 */           return (type != null) && (typeToMatch.isAssignableFrom(type));
/*      */         }
/*      */ 
/*  497 */         return typeToMatch.isInstance(beanInstance);
/*      */       }
/*      */ 
/*  501 */       return (!BeanFactoryUtils.isFactoryDereference(name)) && (typeToMatch.isInstance(beanInstance));
/*      */     }
/*      */ 
/*  504 */     if ((containsSingleton(beanName)) && (!containsBeanDefinition(beanName)))
/*      */     {
/*  506 */       return false;
/*      */     }
/*      */ 
/*  511 */     BeanFactory parentBeanFactory = getParentBeanFactory();
/*  512 */     if ((parentBeanFactory != null) && (!containsBeanDefinition(beanName)))
/*      */     {
/*  514 */       return parentBeanFactory.isTypeMatch(originalBeanName(name), typeToMatch);
/*      */     }
/*      */ 
/*  518 */     RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/*      */ 
/*  520 */     Class classToMatch = typeToMatch.getRawClass();
/*  521 */     Class[] typesToMatch = { FactoryBean.class, FactoryBean.class == classToMatch ? new Class[] { classToMatch } : classToMatch };
/*      */ 
/*  526 */     BeanDefinitionHolder dbd = mbd.getDecoratedDefinition();
/*  527 */     if ((dbd != null) && (!BeanFactoryUtils.isFactoryDereference(name))) {
/*  528 */       RootBeanDefinition tbd = getMergedBeanDefinition(dbd.getBeanName(), dbd.getBeanDefinition(), mbd);
/*  529 */       Class targetClass = predictBeanType(dbd.getBeanName(), tbd, typesToMatch);
/*  530 */       if ((targetClass != null) && (!FactoryBean.class.isAssignableFrom(targetClass))) {
/*  531 */         return typeToMatch.isAssignableFrom(targetClass);
/*      */       }
/*      */     }
/*      */ 
/*  535 */     Class beanType = predictBeanType(beanName, mbd, typesToMatch);
/*  536 */     if (beanType == null) {
/*  537 */       return false;
/*      */     }
/*      */ 
/*  541 */     if (FactoryBean.class.isAssignableFrom(beanType)) {
/*  542 */       if (!BeanFactoryUtils.isFactoryDereference(name))
/*      */       {
/*  544 */         beanType = getTypeForFactoryBean(beanName, mbd);
/*  545 */         if (beanType == null) {
/*  546 */           return false;
/*      */         }
/*      */       }
/*      */     }
/*  550 */     else if (BeanFactoryUtils.isFactoryDereference(name))
/*      */     {
/*  554 */       beanType = predictBeanType(beanName, mbd, new Class[] { FactoryBean.class });
/*  555 */       if ((beanType == null) || (!FactoryBean.class.isAssignableFrom(beanType))) {
/*  556 */         return false;
/*      */       }
/*      */     }
/*      */ 
/*  560 */     return typeToMatch.isAssignableFrom(beanType);
/*      */   }
/*      */ 
/*      */   public boolean isTypeMatch(String name, Class<?> typeToMatch)
/*      */     throws NoSuchBeanDefinitionException
/*      */   {
/*  566 */     return isTypeMatch(name, ResolvableType.forRawClass(typeToMatch));
/*      */   }
/*      */ 
/*      */   public Class<?> getType(String name) throws NoSuchBeanDefinitionException
/*      */   {
/*  571 */     String beanName = transformedBeanName(name);
/*      */ 
/*  574 */     Object beanInstance = getSingleton(beanName, false);
/*  575 */     if (beanInstance != null) {
/*  576 */       if (((beanInstance instanceof FactoryBean)) && (!BeanFactoryUtils.isFactoryDereference(name))) {
/*  577 */         return getTypeForFactoryBean((FactoryBean)beanInstance);
/*      */       }
/*      */ 
/*  580 */       return beanInstance.getClass();
/*      */     }
/*      */ 
/*  583 */     if ((containsSingleton(beanName)) && (!containsBeanDefinition(beanName)))
/*      */     {
/*  585 */       return null;
/*      */     }
/*      */ 
/*  590 */     BeanFactory parentBeanFactory = getParentBeanFactory();
/*  591 */     if ((parentBeanFactory != null) && (!containsBeanDefinition(beanName)))
/*      */     {
/*  593 */       return parentBeanFactory.getType(originalBeanName(name));
/*      */     }
/*      */ 
/*  596 */     RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/*      */ 
/*  600 */     BeanDefinitionHolder dbd = mbd.getDecoratedDefinition();
/*  601 */     if ((dbd != null) && (!BeanFactoryUtils.isFactoryDereference(name))) {
/*  602 */       RootBeanDefinition tbd = getMergedBeanDefinition(dbd.getBeanName(), dbd.getBeanDefinition(), mbd);
/*  603 */       Class targetClass = predictBeanType(dbd.getBeanName(), tbd, new Class[0]);
/*  604 */       if ((targetClass != null) && (!FactoryBean.class.isAssignableFrom(targetClass))) {
/*  605 */         return targetClass;
/*      */       }
/*      */     }
/*      */ 
/*  609 */     Class beanClass = predictBeanType(beanName, mbd, new Class[0]);
/*      */ 
/*  612 */     if ((beanClass != null) && (FactoryBean.class.isAssignableFrom(beanClass))) {
/*  613 */       if (!BeanFactoryUtils.isFactoryDereference(name))
/*      */       {
/*  615 */         return getTypeForFactoryBean(beanName, mbd);
/*      */       }
/*      */ 
/*  618 */       return beanClass;
/*      */     }
/*      */ 
/*  622 */     return !BeanFactoryUtils.isFactoryDereference(name) ? beanClass : null;
/*      */   }
/*      */ 
/*      */   public String[] getAliases(String name)
/*      */   {
/*  629 */     String beanName = transformedBeanName(name);
/*  630 */     List aliases = new ArrayList();
/*  631 */     boolean factoryPrefix = name.startsWith("&");
/*  632 */     String fullBeanName = beanName;
/*  633 */     if (factoryPrefix) {
/*  634 */       fullBeanName = new StringBuilder().append("&").append(beanName).toString();
/*      */     }
/*  636 */     if (!fullBeanName.equals(name)) {
/*  637 */       aliases.add(fullBeanName);
/*      */     }
/*  639 */     String[] retrievedAliases = super.getAliases(beanName);
/*  640 */     for (String retrievedAlias : retrievedAliases) {
/*  641 */       String alias = new StringBuilder().append(factoryPrefix ? "&" : "").append(retrievedAlias).toString();
/*  642 */       if (!alias.equals(name)) {
/*  643 */         aliases.add(alias);
/*      */       }
/*      */     }
/*  646 */     if ((!containsSingleton(beanName)) && (!containsBeanDefinition(beanName))) {
/*  647 */       BeanFactory parentBeanFactory = getParentBeanFactory();
/*  648 */       if (parentBeanFactory != null) {
/*  649 */         aliases.addAll(Arrays.asList(parentBeanFactory.getAliases(fullBeanName)));
/*      */       }
/*      */     }
/*  652 */     return StringUtils.toStringArray(aliases);
/*      */   }
/*      */ 
/*      */   public BeanFactory getParentBeanFactory()
/*      */   {
/*  662 */     return this.parentBeanFactory;
/*      */   }
/*      */ 
/*      */   public boolean containsLocalBean(String name)
/*      */   {
/*  667 */     String beanName = transformedBeanName(name);
/*      */ 
/*  669 */     return ((containsSingleton(beanName)) || (containsBeanDefinition(beanName))) && (
/*  669 */       (!BeanFactoryUtils.isFactoryDereference(name)) || 
/*  669 */       (isFactoryBean(beanName)));
/*      */   }
/*      */ 
/*      */   public void setParentBeanFactory(BeanFactory parentBeanFactory)
/*      */   {
/*  679 */     if ((this.parentBeanFactory != null) && (this.parentBeanFactory != parentBeanFactory)) {
/*  680 */       throw new IllegalStateException(new StringBuilder().append("Already associated with parent BeanFactory: ").append(this.parentBeanFactory).toString());
/*      */     }
/*  682 */     this.parentBeanFactory = parentBeanFactory;
/*      */   }
/*      */ 
/*      */   public void setBeanClassLoader(ClassLoader beanClassLoader)
/*      */   {
/*  687 */     this.beanClassLoader = (beanClassLoader != null ? beanClassLoader : ClassUtils.getDefaultClassLoader());
/*      */   }
/*      */ 
/*      */   public ClassLoader getBeanClassLoader()
/*      */   {
/*  692 */     return this.beanClassLoader;
/*      */   }
/*      */ 
/*      */   public void setTempClassLoader(ClassLoader tempClassLoader)
/*      */   {
/*  697 */     this.tempClassLoader = tempClassLoader;
/*      */   }
/*      */ 
/*      */   public ClassLoader getTempClassLoader()
/*      */   {
/*  702 */     return this.tempClassLoader;
/*      */   }
/*      */ 
/*      */   public void setCacheBeanMetadata(boolean cacheBeanMetadata)
/*      */   {
/*  707 */     this.cacheBeanMetadata = cacheBeanMetadata;
/*      */   }
/*      */ 
/*      */   public boolean isCacheBeanMetadata()
/*      */   {
/*  712 */     return this.cacheBeanMetadata;
/*      */   }
/*      */ 
/*      */   public void setBeanExpressionResolver(BeanExpressionResolver resolver)
/*      */   {
/*  717 */     this.beanExpressionResolver = resolver;
/*      */   }
/*      */ 
/*      */   public BeanExpressionResolver getBeanExpressionResolver()
/*      */   {
/*  722 */     return this.beanExpressionResolver;
/*      */   }
/*      */ 
/*      */   public void setConversionService(ConversionService conversionService)
/*      */   {
/*  727 */     this.conversionService = conversionService;
/*      */   }
/*      */ 
/*      */   public ConversionService getConversionService()
/*      */   {
/*  732 */     return this.conversionService;
/*      */   }
/*      */ 
/*      */   public void addPropertyEditorRegistrar(PropertyEditorRegistrar registrar)
/*      */   {
/*  737 */     Assert.notNull(registrar, "PropertyEditorRegistrar must not be null");
/*  738 */     this.propertyEditorRegistrars.add(registrar);
/*      */   }
/*      */ 
/*      */   public Set<PropertyEditorRegistrar> getPropertyEditorRegistrars()
/*      */   {
/*  745 */     return this.propertyEditorRegistrars;
/*      */   }
/*      */ 
/*      */   public void registerCustomEditor(Class<?> requiredType, Class<? extends PropertyEditor> propertyEditorClass)
/*      */   {
/*  750 */     Assert.notNull(requiredType, "Required type must not be null");
/*  751 */     Assert.isAssignable(PropertyEditor.class, propertyEditorClass);
/*  752 */     this.customEditors.put(requiredType, propertyEditorClass);
/*      */   }
/*      */ 
/*      */   public void copyRegisteredEditorsTo(PropertyEditorRegistry registry)
/*      */   {
/*  757 */     registerCustomEditors(registry);
/*      */   }
/*      */ 
/*      */   public Map<Class<?>, Class<? extends PropertyEditor>> getCustomEditors()
/*      */   {
/*  764 */     return this.customEditors;
/*      */   }
/*      */ 
/*      */   public void setTypeConverter(TypeConverter typeConverter)
/*      */   {
/*  769 */     this.typeConverter = typeConverter;
/*      */   }
/*      */ 
/*      */   protected TypeConverter getCustomTypeConverter()
/*      */   {
/*  777 */     return this.typeConverter;
/*      */   }
/*      */ 
/*      */   public TypeConverter getTypeConverter()
/*      */   {
/*  782 */     TypeConverter customConverter = getCustomTypeConverter();
/*  783 */     if (customConverter != null) {
/*  784 */       return customConverter;
/*      */     }
/*      */ 
/*  788 */     SimpleTypeConverter typeConverter = new SimpleTypeConverter();
/*  789 */     typeConverter.setConversionService(getConversionService());
/*  790 */     registerCustomEditors(typeConverter);
/*  791 */     return typeConverter;
/*      */   }
/*      */ 
/*      */   public void addEmbeddedValueResolver(StringValueResolver valueResolver)
/*      */   {
/*  797 */     Assert.notNull(valueResolver, "StringValueResolver must not be null");
/*  798 */     this.embeddedValueResolvers.add(valueResolver);
/*      */   }
/*      */ 
/*      */   public boolean hasEmbeddedValueResolver()
/*      */   {
/*  803 */     return !this.embeddedValueResolvers.isEmpty();
/*      */   }
/*      */ 
/*      */   public String resolveEmbeddedValue(String value)
/*      */   {
/*  808 */     String result = value;
/*  809 */     for (StringValueResolver resolver : this.embeddedValueResolvers) {
/*  810 */       if (result == null) {
/*  811 */         return null;
/*      */       }
/*  813 */       result = resolver.resolveStringValue(result);
/*      */     }
/*  815 */     return result;
/*      */   }
/*      */ 
/*      */   public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor)
/*      */   {
/*  820 */     Assert.notNull(beanPostProcessor, "BeanPostProcessor must not be null");
/*  821 */     this.beanPostProcessors.remove(beanPostProcessor);
/*  822 */     this.beanPostProcessors.add(beanPostProcessor);
/*  823 */     if ((beanPostProcessor instanceof InstantiationAwareBeanPostProcessor)) {
/*  824 */       this.hasInstantiationAwareBeanPostProcessors = true;
/*      */     }
/*  826 */     if ((beanPostProcessor instanceof DestructionAwareBeanPostProcessor))
/*  827 */       this.hasDestructionAwareBeanPostProcessors = true;
/*      */   }
/*      */ 
/*      */   public int getBeanPostProcessorCount()
/*      */   {
/*  833 */     return this.beanPostProcessors.size();
/*      */   }
/*      */ 
/*      */   public List<BeanPostProcessor> getBeanPostProcessors()
/*      */   {
/*  841 */     return this.beanPostProcessors;
/*      */   }
/*      */ 
/*      */   protected boolean hasInstantiationAwareBeanPostProcessors()
/*      */   {
/*  851 */     return this.hasInstantiationAwareBeanPostProcessors;
/*      */   }
/*      */ 
/*      */   protected boolean hasDestructionAwareBeanPostProcessors()
/*      */   {
/*  861 */     return this.hasDestructionAwareBeanPostProcessors;
/*      */   }
/*      */ 
/*      */   public void registerScope(String scopeName, Scope scope)
/*      */   {
/*  866 */     Assert.notNull(scopeName, "Scope identifier must not be null");
/*  867 */     Assert.notNull(scope, "Scope must not be null");
/*  868 */     if (("singleton".equals(scopeName)) || ("prototype".equals(scopeName))) {
/*  869 */       throw new IllegalArgumentException("Cannot replace existing scopes 'singleton' and 'prototype'");
/*      */     }
/*  871 */     Scope previous = (Scope)this.scopes.put(scopeName, scope);
/*  872 */     if ((previous != null) && (previous != scope)) {
/*  873 */       if (this.logger.isInfoEnabled()) {
/*  874 */         this.logger.info(new StringBuilder().append("Replacing scope '").append(scopeName).append("' from [").append(previous).append("] to [").append(scope).append("]").toString());
/*      */       }
/*      */ 
/*      */     }
/*  878 */     else if (this.logger.isDebugEnabled())
/*  879 */       this.logger.debug(new StringBuilder().append("Registering scope '").append(scopeName).append("' with implementation [").append(scope).append("]").toString());
/*      */   }
/*      */ 
/*      */   public String[] getRegisteredScopeNames()
/*      */   {
/*  886 */     return StringUtils.toStringArray(this.scopes.keySet());
/*      */   }
/*      */ 
/*      */   public Scope getRegisteredScope(String scopeName)
/*      */   {
/*  891 */     Assert.notNull(scopeName, "Scope identifier must not be null");
/*  892 */     return (Scope)this.scopes.get(scopeName);
/*      */   }
/*      */ 
/*      */   public void setSecurityContextProvider(SecurityContextProvider securityProvider)
/*      */   {
/*  901 */     this.securityContextProvider = securityProvider;
/*      */   }
/*      */ 
/*      */   public AccessControlContext getAccessControlContext()
/*      */   {
/*  912 */     return this.securityContextProvider != null ? this.securityContextProvider
/*  911 */       .getAccessControlContext() : 
/*  912 */       AccessController.getContext();
/*      */   }
/*      */ 
/*      */   public void copyConfigurationFrom(ConfigurableBeanFactory otherFactory)
/*      */   {
/*  917 */     Assert.notNull(otherFactory, "BeanFactory must not be null");
/*  918 */     setBeanClassLoader(otherFactory.getBeanClassLoader());
/*  919 */     setCacheBeanMetadata(otherFactory.isCacheBeanMetadata());
/*  920 */     setBeanExpressionResolver(otherFactory.getBeanExpressionResolver());
/*  921 */     if ((otherFactory instanceof AbstractBeanFactory)) {
/*  922 */       AbstractBeanFactory otherAbstractFactory = (AbstractBeanFactory)otherFactory;
/*  923 */       this.customEditors.putAll(otherAbstractFactory.customEditors);
/*  924 */       this.propertyEditorRegistrars.addAll(otherAbstractFactory.propertyEditorRegistrars);
/*  925 */       this.beanPostProcessors.addAll(otherAbstractFactory.beanPostProcessors);
/*  926 */       this.hasInstantiationAwareBeanPostProcessors = ((this.hasInstantiationAwareBeanPostProcessors) || (otherAbstractFactory.hasInstantiationAwareBeanPostProcessors));
/*      */ 
/*  928 */       this.hasDestructionAwareBeanPostProcessors = ((this.hasDestructionAwareBeanPostProcessors) || (otherAbstractFactory.hasDestructionAwareBeanPostProcessors));
/*      */ 
/*  930 */       this.scopes.putAll(otherAbstractFactory.scopes);
/*  931 */       this.securityContextProvider = otherAbstractFactory.securityContextProvider;
/*      */     }
/*      */     else {
/*  934 */       setTypeConverter(otherFactory.getTypeConverter());
/*      */     }
/*      */   }
/*      */ 
/*      */   public BeanDefinition getMergedBeanDefinition(String name)
/*      */     throws BeansException
/*      */   {
/*  951 */     String beanName = transformedBeanName(name);
/*      */ 
/*  954 */     if ((!containsBeanDefinition(beanName)) && ((getParentBeanFactory() instanceof ConfigurableBeanFactory))) {
/*  955 */       return ((ConfigurableBeanFactory)getParentBeanFactory()).getMergedBeanDefinition(beanName);
/*      */     }
/*      */ 
/*  958 */     return getMergedLocalBeanDefinition(beanName);
/*      */   }
/*      */ 
/*      */   public boolean isFactoryBean(String name) throws NoSuchBeanDefinitionException
/*      */   {
/*  963 */     String beanName = transformedBeanName(name);
/*      */ 
/*  965 */     Object beanInstance = getSingleton(beanName, false);
/*  966 */     if (beanInstance != null) {
/*  967 */       return beanInstance instanceof FactoryBean;
/*      */     }
/*  969 */     if (containsSingleton(beanName))
/*      */     {
/*  971 */       return false;
/*      */     }
/*      */ 
/*  975 */     if ((!containsBeanDefinition(beanName)) && ((getParentBeanFactory() instanceof ConfigurableBeanFactory)))
/*      */     {
/*  977 */       return ((ConfigurableBeanFactory)getParentBeanFactory()).isFactoryBean(name);
/*      */     }
/*      */ 
/*  980 */     return isFactoryBean(beanName, getMergedLocalBeanDefinition(beanName));
/*      */   }
/*      */ 
/*      */   public boolean isActuallyInCreation(String beanName)
/*      */   {
/*  985 */     return (isSingletonCurrentlyInCreation(beanName)) || (isPrototypeCurrentlyInCreation(beanName));
/*      */   }
/*      */ 
/*      */   protected boolean isPrototypeCurrentlyInCreation(String beanName)
/*      */   {
/*  994 */     Object curVal = this.prototypesCurrentlyInCreation.get();
/*      */ 
/*  996 */     return (curVal != null) && (
/*  996 */       (curVal
/*  996 */       .equals(beanName)) || 
/*  996 */       (((curVal instanceof Set)) && (((Set)curVal).contains(beanName))));
/*      */   }
/*      */ 
/*      */   protected void beforePrototypeCreation(String beanName)
/*      */   {
/* 1007 */     Object curVal = this.prototypesCurrentlyInCreation.get();
/* 1008 */     if (curVal == null) {
/* 1009 */       this.prototypesCurrentlyInCreation.set(beanName);
/*      */     }
/* 1011 */     else if ((curVal instanceof String)) {
/* 1012 */       Set beanNameSet = new HashSet(2);
/* 1013 */       beanNameSet.add((String)curVal);
/* 1014 */       beanNameSet.add(beanName);
/* 1015 */       this.prototypesCurrentlyInCreation.set(beanNameSet);
/*      */     }
/*      */     else {
/* 1018 */       Set beanNameSet = (Set)curVal;
/* 1019 */       beanNameSet.add(beanName);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void afterPrototypeCreation(String beanName)
/*      */   {
/* 1031 */     Object curVal = this.prototypesCurrentlyInCreation.get();
/* 1032 */     if ((curVal instanceof String)) {
/* 1033 */       this.prototypesCurrentlyInCreation.remove();
/*      */     }
/* 1035 */     else if ((curVal instanceof Set)) {
/* 1036 */       Set beanNameSet = (Set)curVal;
/* 1037 */       beanNameSet.remove(beanName);
/* 1038 */       if (beanNameSet.isEmpty())
/* 1039 */         this.prototypesCurrentlyInCreation.remove();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void destroyBean(String beanName, Object beanInstance)
/*      */   {
/* 1046 */     destroyBean(beanName, beanInstance, getMergedLocalBeanDefinition(beanName));
/*      */   }
/*      */ 
/*      */   protected void destroyBean(String beanName, Object beanInstance, RootBeanDefinition mbd)
/*      */   {
/* 1057 */     new DisposableBeanAdapter(beanInstance, beanName, mbd, getBeanPostProcessors(), getAccessControlContext()).destroy();
/*      */   }
/*      */ 
/*      */   public void destroyScopedBean(String beanName)
/*      */   {
/* 1062 */     RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/* 1063 */     if ((mbd.isSingleton()) || (mbd.isPrototype())) {
/* 1064 */       throw new IllegalArgumentException(new StringBuilder().append("Bean name '").append(beanName).append("' does not correspond to an object in a mutable scope").toString());
/*      */     }
/*      */ 
/* 1067 */     String scopeName = mbd.getScope();
/* 1068 */     Scope scope = (Scope)this.scopes.get(scopeName);
/* 1069 */     if (scope == null) {
/* 1070 */       throw new IllegalStateException(new StringBuilder().append("No Scope SPI registered for scope name '").append(scopeName).append("'").toString());
/*      */     }
/* 1072 */     Object bean = scope.remove(beanName);
/* 1073 */     if (bean != null)
/* 1074 */       destroyBean(beanName, bean, mbd);
/*      */   }
/*      */ 
/*      */   protected String transformedBeanName(String name)
/*      */   {
/* 1090 */     return canonicalName(BeanFactoryUtils.transformedBeanName(name));
/*      */   }
/*      */ 
/*      */   protected String originalBeanName(String name)
/*      */   {
/* 1099 */     String beanName = transformedBeanName(name);
/* 1100 */     if (name.startsWith("&")) {
/* 1101 */       beanName = new StringBuilder().append("&").append(beanName).toString();
/*      */     }
/* 1103 */     return beanName;
/*      */   }
/*      */ 
/*      */   protected void initBeanWrapper(BeanWrapper bw)
/*      */   {
/* 1115 */     bw.setConversionService(getConversionService());
/* 1116 */     registerCustomEditors(bw);
/*      */   }
/*      */ 
/*      */   protected void registerCustomEditors(PropertyEditorRegistry registry)
/*      */   {
/* 1128 */     PropertyEditorRegistrySupport registrySupport = (registry instanceof PropertyEditorRegistrySupport) ? (PropertyEditorRegistrySupport)registry : null;
/*      */ 
/* 1130 */     if (registrySupport != null) {
/* 1131 */       registrySupport.useConfigValueEditors();
/*      */     }
/* 1133 */     if (!this.propertyEditorRegistrars.isEmpty()) {
/* 1134 */       for (PropertyEditorRegistrar registrar : this.propertyEditorRegistrars)
/*      */         try {
/* 1136 */           registrar.registerCustomEditors(registry);
/*      */         }
/*      */         catch (BeanCreationException ex) {
/* 1139 */           Throwable rootCause = ex.getMostSpecificCause();
/* 1140 */           if ((rootCause instanceof BeanCurrentlyInCreationException)) {
/* 1141 */             BeanCreationException bce = (BeanCreationException)rootCause;
/* 1142 */             if (isCurrentlyInCreation(bce.getBeanName())) {
/* 1143 */               if (this.logger.isDebugEnabled()) {
/* 1144 */                 this.logger.debug(new StringBuilder().append("PropertyEditorRegistrar [").append(registrar.getClass().getName()).append("] failed because it tried to obtain currently created bean '")
/* 1146 */                   .append(ex
/* 1146 */                   .getBeanName()).append("': ").append(ex.getMessage()).toString());
/*      */               }
/* 1148 */               onSuppressedException(ex);
/*      */             }
/*      */           }
/*      */           else {
/* 1152 */             throw ex;
/*      */           }
/*      */         }
/*      */     }
/* 1156 */     if (!this.customEditors.isEmpty())
/* 1157 */       for (Map.Entry entry : this.customEditors.entrySet()) {
/* 1158 */         Class requiredType = (Class)entry.getKey();
/* 1159 */         Class editorClass = (Class)entry.getValue();
/* 1160 */         registry.registerCustomEditor(requiredType, (PropertyEditor)BeanUtils.instantiateClass(editorClass));
/*      */       }
/*      */   }
/*      */ 
/*      */   protected RootBeanDefinition getMergedLocalBeanDefinition(String beanName)
/*      */     throws BeansException
/*      */   {
/* 1176 */     RootBeanDefinition mbd = (RootBeanDefinition)this.mergedBeanDefinitions.get(beanName);
/* 1177 */     if (mbd != null) {
/* 1178 */       return mbd;
/*      */     }
/* 1180 */     return getMergedBeanDefinition(beanName, getBeanDefinition(beanName));
/*      */   }
/*      */ 
/*      */   protected RootBeanDefinition getMergedBeanDefinition(String beanName, BeanDefinition bd)
/*      */     throws BeanDefinitionStoreException
/*      */   {
/* 1194 */     return getMergedBeanDefinition(beanName, bd, null);
/*      */   }
/*      */ 
/*      */   protected RootBeanDefinition getMergedBeanDefinition(String beanName, BeanDefinition bd, BeanDefinition containingBd)
/*      */     throws BeanDefinitionStoreException
/*      */   {
/* 1211 */     synchronized (this.mergedBeanDefinitions) {
/* 1212 */       RootBeanDefinition mbd = null;
/*      */ 
/* 1215 */       if (containingBd == null) {
/* 1216 */         mbd = (RootBeanDefinition)this.mergedBeanDefinitions.get(beanName);
/*      */       }
/*      */ 
/* 1219 */       if (mbd == null) {
/* 1220 */         if (bd.getParentName() == null)
/*      */         {
/* 1222 */           if ((bd instanceof RootBeanDefinition)) {
/* 1223 */             mbd = ((RootBeanDefinition)bd).cloneBeanDefinition();
/*      */           }
/*      */           else {
/* 1226 */             mbd = new RootBeanDefinition(bd);
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*      */           try
/*      */           {
/* 1233 */             String parentBeanName = transformedBeanName(bd.getParentName());
/*      */             BeanDefinition pbd;
/* 1234 */             if (!beanName.equals(parentBeanName)) {
/* 1235 */               pbd = getMergedBeanDefinition(parentBeanName);
/*      */             }
/*      */             else
/*      */             {
/*      */               BeanDefinition pbd;
/* 1238 */               if ((getParentBeanFactory() instanceof ConfigurableBeanFactory)) {
/* 1239 */                 pbd = ((ConfigurableBeanFactory)getParentBeanFactory()).getMergedBeanDefinition(parentBeanName);
/*      */               }
/*      */               else
/*      */               {
/* 1243 */                 throw new NoSuchBeanDefinitionException(bd.getParentName(), new StringBuilder().append("Parent name '")
/* 1243 */                   .append(bd
/* 1243 */                   .getParentName()).append("' is equal to bean name '").append(beanName).append("': cannot be resolved without an AbstractBeanFactory parent").toString());
/*      */               }
/*      */             }
/*      */           }
/*      */           catch (NoSuchBeanDefinitionException ex)
/*      */           {
/*      */             BeanDefinition pbd;
/* 1250 */             throw new BeanDefinitionStoreException(bd.getResourceDescription(), beanName, new StringBuilder().append("Could not resolve parent bean definition '")
/* 1250 */               .append(bd
/* 1250 */               .getParentName()).append("'").toString(), ex);
/*      */           }
/*      */           BeanDefinition pbd;
/* 1253 */           mbd = new RootBeanDefinition(pbd);
/* 1254 */           mbd.overrideFrom(bd);
/*      */         }
/*      */ 
/* 1258 */         if (!StringUtils.hasLength(mbd.getScope())) {
/* 1259 */           mbd.setScope("singleton");
/*      */         }
/*      */ 
/* 1266 */         if ((containingBd != null) && (!containingBd.isSingleton()) && (mbd.isSingleton())) {
/* 1267 */           mbd.setScope(containingBd.getScope());
/*      */         }
/*      */ 
/* 1272 */         if ((containingBd == null) && (isCacheBeanMetadata())) {
/* 1273 */           this.mergedBeanDefinitions.put(beanName, mbd);
/*      */         }
/*      */       }
/*      */ 
/* 1277 */       return mbd;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void checkMergedBeanDefinition(RootBeanDefinition mbd, String beanName, Object[] args)
/*      */     throws BeanDefinitionStoreException
/*      */   {
/* 1292 */     if (mbd.isAbstract())
/* 1293 */       throw new BeanIsAbstractException(beanName);
/*      */   }
/*      */ 
/*      */   protected void clearMergedBeanDefinition(String beanName)
/*      */   {
/* 1303 */     this.mergedBeanDefinitions.remove(beanName);
/*      */   }
/*      */ 
/*      */   public void clearMetadataCache()
/*      */   {
/* 1315 */     Iterator mergedBeans = this.mergedBeanDefinitions.keySet().iterator();
/* 1316 */     while (mergedBeans.hasNext())
/* 1317 */       if (!isBeanEligibleForMetadataCaching((String)mergedBeans.next()))
/* 1318 */         mergedBeans.remove();
/*      */   }
/*      */ 
/*      */   protected Class<?> resolveBeanClass(final RootBeanDefinition mbd, String beanName, final Class<?>[] typesToMatch)
/*      */     throws CannotLoadBeanClassException
/*      */   {
/*      */     try
/*      */     {
/* 1337 */       if (mbd.hasBeanClass()) {
/* 1338 */         return mbd.getBeanClass();
/*      */       }
/* 1340 */       if (System.getSecurityManager() != null) {
/* 1341 */         return (Class)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */         {
/*      */           public Class<?> run() throws Exception {
/* 1344 */             return AbstractBeanFactory.this.doResolveBeanClass(mbd, typesToMatch);
/*      */           }
/*      */         }
/*      */         , getAccessControlContext());
/*      */       }
/*      */ 
/* 1349 */       return doResolveBeanClass(mbd, typesToMatch);
/*      */     }
/*      */     catch (PrivilegedActionException pae)
/*      */     {
/* 1353 */       ClassNotFoundException ex = (ClassNotFoundException)pae.getException();
/* 1354 */       throw new CannotLoadBeanClassException(mbd.getResourceDescription(), beanName, mbd.getBeanClassName(), ex);
/*      */     }
/*      */     catch (ClassNotFoundException ex) {
/* 1357 */       throw new CannotLoadBeanClassException(mbd.getResourceDescription(), beanName, mbd.getBeanClassName(), ex);
/*      */     }
/*      */     catch (LinkageError err) {
/* 1360 */       throw new CannotLoadBeanClassException(mbd.getResourceDescription(), beanName, mbd.getBeanClassName(), err);
/*      */     }
/*      */   }
/*      */ 
/*      */   private Class<?> doResolveBeanClass(RootBeanDefinition mbd, Class<?>[] typesToMatch) throws ClassNotFoundException {
/* 1365 */     ClassLoader beanClassLoader = getBeanClassLoader();
/* 1366 */     ClassLoader classLoaderToUse = beanClassLoader;
/* 1367 */     if (!ObjectUtils.isEmpty(typesToMatch))
/*      */     {
/* 1370 */       ClassLoader tempClassLoader = getTempClassLoader();
/* 1371 */       if (tempClassLoader != null) {
/* 1372 */         classLoaderToUse = tempClassLoader;
/* 1373 */         if ((tempClassLoader instanceof DecoratingClassLoader)) {
/* 1374 */           DecoratingClassLoader dcl = (DecoratingClassLoader)tempClassLoader;
/* 1375 */           for (Class typeToMatch : typesToMatch) {
/* 1376 */             dcl.excludeClass(typeToMatch.getName());
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1381 */     String className = mbd.getBeanClassName();
/* 1382 */     if (className != null) {
/* 1383 */       Object evaluated = evaluateBeanDefinitionString(className, mbd);
/* 1384 */       if (!className.equals(evaluated))
/*      */       {
/* 1386 */         if ((evaluated instanceof Class)) {
/* 1387 */           return (Class)evaluated;
/*      */         }
/* 1389 */         if ((evaluated instanceof String)) {
/* 1390 */           return ClassUtils.forName((String)evaluated, classLoaderToUse);
/*      */         }
/*      */ 
/* 1393 */         throw new IllegalStateException(new StringBuilder().append("Invalid class name expression result: ").append(evaluated).toString());
/*      */       }
/*      */ 
/* 1398 */       if (classLoaderToUse != beanClassLoader) {
/* 1399 */         return ClassUtils.forName(className, classLoaderToUse);
/*      */       }
/*      */     }
/* 1402 */     return mbd.resolveBeanClass(beanClassLoader);
/*      */   }
/*      */ 
/*      */   protected Object evaluateBeanDefinitionString(String value, BeanDefinition beanDefinition)
/*      */   {
/* 1414 */     if (this.beanExpressionResolver == null) {
/* 1415 */       return value;
/*      */     }
/* 1417 */     Scope scope = beanDefinition != null ? getRegisteredScope(beanDefinition.getScope()) : null;
/* 1418 */     return this.beanExpressionResolver.evaluate(value, new BeanExpressionContext(this, scope));
/*      */   }
/*      */ 
/*      */   protected Class<?> predictBeanType(String beanName, RootBeanDefinition mbd, Class<?>[] typesToMatch)
/*      */   {
/* 1438 */     if (mbd.getFactoryMethodName() != null) {
/* 1439 */       return null;
/*      */     }
/* 1441 */     return resolveBeanClass(mbd, beanName, typesToMatch);
/*      */   }
/*      */ 
/*      */   protected boolean isFactoryBean(String beanName, RootBeanDefinition mbd)
/*      */   {
/* 1450 */     Class beanType = predictBeanType(beanName, mbd, new Class[] { FactoryBean.class });
/* 1451 */     return (beanType != null) && (FactoryBean.class.isAssignableFrom(beanType));
/*      */   }
/*      */ 
/*      */   protected Class<?> getTypeForFactoryBean(String beanName, RootBeanDefinition mbd)
/*      */   {
/* 1470 */     if (!mbd.isSingleton())
/* 1471 */       return null;
/*      */     try
/*      */     {
/* 1474 */       FactoryBean factoryBean = (FactoryBean)doGetBean(new StringBuilder().append("&").append(beanName).toString(), FactoryBean.class, null, true);
/* 1475 */       return getTypeForFactoryBean(factoryBean);
/*      */     }
/*      */     catch (BeanCreationException ex) {
/* 1478 */       if ((ex instanceof BeanCurrentlyInCreationException)) {
/* 1479 */         if (this.logger.isDebugEnabled()) {
/* 1480 */           this.logger.debug(new StringBuilder().append("Bean currently in creation on FactoryBean type check: ").append(ex).toString());
/*      */         }
/*      */       }
/* 1483 */       else if (mbd.isLazyInit()) {
/* 1484 */         if (this.logger.isDebugEnabled()) {
/* 1485 */           this.logger.debug(new StringBuilder().append("Bean creation exception on lazy FactoryBean type check: ").append(ex).toString());
/*      */         }
/*      */ 
/*      */       }
/* 1489 */       else if (this.logger.isWarnEnabled()) {
/* 1490 */         this.logger.warn(new StringBuilder().append("Bean creation exception on non-lazy FactoryBean type check: ").append(ex).toString());
/*      */       }
/*      */ 
/* 1493 */       onSuppressedException(ex);
/* 1494 */     }return null;
/*      */   }
/*      */ 
/*      */   protected void markBeanAsCreated(String beanName)
/*      */   {
/* 1505 */     if (!this.alreadyCreated.contains(beanName))
/* 1506 */       synchronized (this.mergedBeanDefinitions) {
/* 1507 */         if (!this.alreadyCreated.contains(beanName))
/*      */         {
/* 1510 */           clearMergedBeanDefinition(beanName);
/* 1511 */           this.alreadyCreated.add(beanName);
/*      */         }
/*      */       }
/*      */   }
/*      */ 
/*      */   protected void cleanupAfterBeanCreationFailure(String beanName)
/*      */   {
/* 1522 */     synchronized (this.mergedBeanDefinitions) {
/* 1523 */       this.alreadyCreated.remove(beanName);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected boolean isBeanEligibleForMetadataCaching(String beanName)
/*      */   {
/* 1535 */     return this.alreadyCreated.contains(beanName);
/*      */   }
/*      */ 
/*      */   protected boolean removeSingletonIfCreatedForTypeCheckOnly(String beanName)
/*      */   {
/* 1545 */     if (!this.alreadyCreated.contains(beanName)) {
/* 1546 */       removeSingleton(beanName);
/* 1547 */       return true;
/*      */     }
/*      */ 
/* 1550 */     return false;
/*      */   }
/*      */ 
/*      */   protected boolean hasBeanCreationStarted()
/*      */   {
/* 1561 */     return !this.alreadyCreated.isEmpty();
/*      */   }
/*      */ 
/*      */   protected Object getObjectForBeanInstance(Object beanInstance, String name, String beanName, RootBeanDefinition mbd)
/*      */   {
/* 1577 */     if ((BeanFactoryUtils.isFactoryDereference(name)) && (!(beanInstance instanceof FactoryBean))) {
/* 1578 */       throw new BeanIsNotAFactoryException(transformedBeanName(name), beanInstance.getClass());
/*      */     }
/*      */ 
/* 1584 */     if ((!(beanInstance instanceof FactoryBean)) || (BeanFactoryUtils.isFactoryDereference(name))) {
/* 1585 */       return beanInstance;
/*      */     }
/*      */ 
/* 1588 */     Object object = null;
/* 1589 */     if (mbd == null) {
/* 1590 */       object = getCachedObjectForFactoryBean(beanName);
/*      */     }
/* 1592 */     if (object == null)
/*      */     {
/* 1594 */       FactoryBean factory = (FactoryBean)beanInstance;
/*      */ 
/* 1596 */       if ((mbd == null) && (containsBeanDefinition(beanName))) {
/* 1597 */         mbd = getMergedLocalBeanDefinition(beanName);
/*      */       }
/* 1599 */       boolean synthetic = (mbd != null) && (mbd.isSynthetic());
/* 1600 */       object = getObjectFromFactoryBean(factory, beanName, !synthetic);
/*      */     }
/* 1602 */     return object;
/*      */   }
/*      */ 
/*      */   public boolean isBeanNameInUse(String beanName)
/*      */   {
/* 1612 */     return (isAlias(beanName)) || (containsLocalBean(beanName)) || (hasDependentBean(beanName));
/*      */   }
/*      */ 
/*      */   protected boolean requiresDestruction(Object bean, RootBeanDefinition mbd)
/*      */   {
/* 1628 */     return (bean != null) && (
/* 1627 */       (DisposableBeanAdapter.hasDestroyMethod(bean, mbd)) || 
/* 1627 */       ((hasDestructionAwareBeanPostProcessors()) && 
/* 1628 */       (DisposableBeanAdapter.hasApplicableProcessors(bean, 
/* 1628 */       getBeanPostProcessors()))));
/*      */   }
/*      */ 
/*      */   protected void registerDisposableBeanIfNecessary(String beanName, Object bean, RootBeanDefinition mbd)
/*      */   {
/* 1644 */     AccessControlContext acc = System.getSecurityManager() != null ? getAccessControlContext() : null;
/* 1645 */     if ((!mbd.isPrototype()) && (requiresDestruction(bean, mbd)))
/* 1646 */       if (mbd.isSingleton())
/*      */       {
/* 1650 */         registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, mbd, 
/* 1651 */           getBeanPostProcessors(), acc));
/*      */       }
/*      */       else
/*      */       {
/* 1655 */         Scope scope = (Scope)this.scopes.get(mbd.getScope());
/* 1656 */         if (scope == null) {
/* 1657 */           throw new IllegalStateException(new StringBuilder().append("No Scope registered for scope name '").append(mbd.getScope()).append("'").toString());
/*      */         }
/* 1659 */         scope.registerDestructionCallback(beanName, new DisposableBeanAdapter(bean, beanName, mbd, 
/* 1660 */           getBeanPostProcessors(), acc));
/*      */       }
/*      */   }
/*      */ 
/*      */   protected abstract boolean containsBeanDefinition(String paramString);
/*      */ 
/*      */   protected abstract BeanDefinition getBeanDefinition(String paramString)
/*      */     throws BeansException;
/*      */ 
/*      */   protected abstract Object createBean(String paramString, RootBeanDefinition paramRootBeanDefinition, Object[] paramArrayOfObject)
/*      */     throws BeanCreationException;
/*      */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.support.AbstractBeanFactory
 * JD-Core Version:    0.6.2
 */