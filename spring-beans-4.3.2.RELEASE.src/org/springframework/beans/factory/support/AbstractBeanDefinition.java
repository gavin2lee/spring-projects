/*      */ package org.springframework.beans.factory.support;
/*      */ 
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.util.Arrays;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import org.springframework.beans.BeanMetadataAttributeAccessor;
/*      */ import org.springframework.beans.MutablePropertyValues;
/*      */ import org.springframework.beans.factory.config.BeanDefinition;
/*      */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*      */ import org.springframework.core.io.DescriptiveResource;
/*      */ import org.springframework.core.io.Resource;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.ObjectUtils;
/*      */ import org.springframework.util.StringUtils;
/*      */ 
/*      */ public abstract class AbstractBeanDefinition extends BeanMetadataAttributeAccessor
/*      */   implements BeanDefinition, Cloneable
/*      */ {
/*      */   public static final String SCOPE_DEFAULT = "";
/*      */   public static final int AUTOWIRE_NO = 0;
/*      */   public static final int AUTOWIRE_BY_NAME = 1;
/*      */   public static final int AUTOWIRE_BY_TYPE = 2;
/*      */   public static final int AUTOWIRE_CONSTRUCTOR = 3;
/*      */ 
/*      */   @Deprecated
/*      */   public static final int AUTOWIRE_AUTODETECT = 4;
/*      */   public static final int DEPENDENCY_CHECK_NONE = 0;
/*      */   public static final int DEPENDENCY_CHECK_OBJECTS = 1;
/*      */   public static final int DEPENDENCY_CHECK_SIMPLE = 2;
/*      */   public static final int DEPENDENCY_CHECK_ALL = 3;
/*      */   public static final String INFER_METHOD = "(inferred)";
/*      */   private volatile Object beanClass;
/*  139 */   private String scope = "";
/*      */ 
/*  141 */   private boolean abstractFlag = false;
/*      */ 
/*  143 */   private boolean lazyInit = false;
/*      */ 
/*  145 */   private int autowireMode = 0;
/*      */ 
/*  147 */   private int dependencyCheck = 0;
/*      */   private String[] dependsOn;
/*  151 */   private boolean autowireCandidate = true;
/*      */ 
/*  153 */   private boolean primary = false;
/*      */ 
/*  155 */   private final Map<String, AutowireCandidateQualifier> qualifiers = new LinkedHashMap(0);
/*      */ 
/*  158 */   private boolean nonPublicAccessAllowed = true;
/*      */ 
/*  160 */   private boolean lenientConstructorResolution = true;
/*      */   private ConstructorArgumentValues constructorArgumentValues;
/*      */   private MutablePropertyValues propertyValues;
/*  166 */   private MethodOverrides methodOverrides = new MethodOverrides();
/*      */   private String factoryBeanName;
/*      */   private String factoryMethodName;
/*      */   private String initMethodName;
/*      */   private String destroyMethodName;
/*  176 */   private boolean enforceInitMethod = true;
/*      */ 
/*  178 */   private boolean enforceDestroyMethod = true;
/*      */ 
/*  180 */   private boolean synthetic = false;
/*      */ 
/*  182 */   private int role = 0;
/*      */   private String description;
/*      */   private Resource resource;
/*      */ 
/*      */   protected AbstractBeanDefinition()
/*      */   {
/*  193 */     this(null, null);
/*      */   }
/*      */ 
/*      */   protected AbstractBeanDefinition(ConstructorArgumentValues cargs, MutablePropertyValues pvs)
/*      */   {
/*  201 */     setConstructorArgumentValues(cargs);
/*  202 */     setPropertyValues(pvs);
/*      */   }
/*      */ 
/*      */   protected AbstractBeanDefinition(BeanDefinition original)
/*      */   {
/*  211 */     setParentName(original.getParentName());
/*  212 */     setBeanClassName(original.getBeanClassName());
/*  213 */     setFactoryBeanName(original.getFactoryBeanName());
/*  214 */     setFactoryMethodName(original.getFactoryMethodName());
/*  215 */     setScope(original.getScope());
/*  216 */     setAbstract(original.isAbstract());
/*  217 */     setLazyInit(original.isLazyInit());
/*  218 */     setRole(original.getRole());
/*  219 */     setConstructorArgumentValues(new ConstructorArgumentValues(original.getConstructorArgumentValues()));
/*  220 */     setPropertyValues(new MutablePropertyValues(original.getPropertyValues()));
/*  221 */     setSource(original.getSource());
/*  222 */     copyAttributesFrom(original);
/*      */ 
/*  224 */     if ((original instanceof AbstractBeanDefinition)) {
/*  225 */       AbstractBeanDefinition originalAbd = (AbstractBeanDefinition)original;
/*  226 */       if (originalAbd.hasBeanClass()) {
/*  227 */         setBeanClass(originalAbd.getBeanClass());
/*      */       }
/*  229 */       setAutowireMode(originalAbd.getAutowireMode());
/*  230 */       setDependencyCheck(originalAbd.getDependencyCheck());
/*  231 */       setDependsOn(originalAbd.getDependsOn());
/*  232 */       setAutowireCandidate(originalAbd.isAutowireCandidate());
/*  233 */       copyQualifiersFrom(originalAbd);
/*  234 */       setPrimary(originalAbd.isPrimary());
/*  235 */       setNonPublicAccessAllowed(originalAbd.isNonPublicAccessAllowed());
/*  236 */       setLenientConstructorResolution(originalAbd.isLenientConstructorResolution());
/*  237 */       setInitMethodName(originalAbd.getInitMethodName());
/*  238 */       setEnforceInitMethod(originalAbd.isEnforceInitMethod());
/*  239 */       setDestroyMethodName(originalAbd.getDestroyMethodName());
/*  240 */       setEnforceDestroyMethod(originalAbd.isEnforceDestroyMethod());
/*  241 */       setMethodOverrides(new MethodOverrides(originalAbd.getMethodOverrides()));
/*  242 */       setSynthetic(originalAbd.isSynthetic());
/*  243 */       setResource(originalAbd.getResource());
/*      */     }
/*      */     else {
/*  246 */       setResourceDescription(original.getResourceDescription());
/*      */     }
/*      */   }
/*      */ 
/*      */   public void overrideFrom(BeanDefinition other)
/*      */   {
/*  268 */     if (StringUtils.hasLength(other.getBeanClassName())) {
/*  269 */       setBeanClassName(other.getBeanClassName());
/*      */     }
/*  271 */     if (StringUtils.hasLength(other.getFactoryBeanName())) {
/*  272 */       setFactoryBeanName(other.getFactoryBeanName());
/*      */     }
/*  274 */     if (StringUtils.hasLength(other.getFactoryMethodName())) {
/*  275 */       setFactoryMethodName(other.getFactoryMethodName());
/*      */     }
/*  277 */     if (StringUtils.hasLength(other.getScope())) {
/*  278 */       setScope(other.getScope());
/*      */     }
/*  280 */     setAbstract(other.isAbstract());
/*  281 */     setLazyInit(other.isLazyInit());
/*  282 */     setRole(other.getRole());
/*  283 */     getConstructorArgumentValues().addArgumentValues(other.getConstructorArgumentValues());
/*  284 */     getPropertyValues().addPropertyValues(other.getPropertyValues());
/*  285 */     setSource(other.getSource());
/*  286 */     copyAttributesFrom(other);
/*      */ 
/*  288 */     if ((other instanceof AbstractBeanDefinition)) {
/*  289 */       AbstractBeanDefinition otherAbd = (AbstractBeanDefinition)other;
/*  290 */       if (otherAbd.hasBeanClass()) {
/*  291 */         setBeanClass(otherAbd.getBeanClass());
/*      */       }
/*  293 */       setAutowireCandidate(otherAbd.isAutowireCandidate());
/*  294 */       setAutowireMode(otherAbd.getAutowireMode());
/*  295 */       copyQualifiersFrom(otherAbd);
/*  296 */       setPrimary(otherAbd.isPrimary());
/*  297 */       setDependencyCheck(otherAbd.getDependencyCheck());
/*  298 */       setDependsOn(otherAbd.getDependsOn());
/*  299 */       setNonPublicAccessAllowed(otherAbd.isNonPublicAccessAllowed());
/*  300 */       setLenientConstructorResolution(otherAbd.isLenientConstructorResolution());
/*  301 */       if (StringUtils.hasLength(otherAbd.getInitMethodName())) {
/*  302 */         setInitMethodName(otherAbd.getInitMethodName());
/*  303 */         setEnforceInitMethod(otherAbd.isEnforceInitMethod());
/*      */       }
/*  305 */       if (otherAbd.getDestroyMethodName() != null) {
/*  306 */         setDestroyMethodName(otherAbd.getDestroyMethodName());
/*  307 */         setEnforceDestroyMethod(otherAbd.isEnforceDestroyMethod());
/*      */       }
/*  309 */       getMethodOverrides().addOverrides(otherAbd.getMethodOverrides());
/*  310 */       setSynthetic(otherAbd.isSynthetic());
/*  311 */       setResource(otherAbd.getResource());
/*      */     }
/*      */     else {
/*  314 */       setResourceDescription(other.getResourceDescription());
/*      */     }
/*      */   }
/*      */ 
/*      */   public void applyDefaults(BeanDefinitionDefaults defaults)
/*      */   {
/*  323 */     setLazyInit(defaults.isLazyInit());
/*  324 */     setAutowireMode(defaults.getAutowireMode());
/*  325 */     setDependencyCheck(defaults.getDependencyCheck());
/*  326 */     setInitMethodName(defaults.getInitMethodName());
/*  327 */     setEnforceInitMethod(false);
/*  328 */     setDestroyMethodName(defaults.getDestroyMethodName());
/*  329 */     setEnforceDestroyMethod(false);
/*      */   }
/*      */ 
/*      */   public boolean hasBeanClass()
/*      */   {
/*  337 */     return this.beanClass instanceof Class;
/*      */   }
/*      */ 
/*      */   public void setBeanClass(Class<?> beanClass)
/*      */   {
/*  344 */     this.beanClass = beanClass;
/*      */   }
/*      */ 
/*      */   public Class<?> getBeanClass()
/*      */     throws IllegalStateException
/*      */   {
/*  354 */     Object beanClassObject = this.beanClass;
/*  355 */     if (beanClassObject == null) {
/*  356 */       throw new IllegalStateException("No bean class specified on bean definition");
/*      */     }
/*  358 */     if (!(beanClassObject instanceof Class)) {
/*  359 */       throw new IllegalStateException(new StringBuilder().append("Bean class name [").append(beanClassObject).append("] has not been resolved into an actual Class").toString());
/*      */     }
/*      */ 
/*  362 */     return (Class)beanClassObject;
/*      */   }
/*      */ 
/*      */   public void setBeanClassName(String beanClassName)
/*      */   {
/*  367 */     this.beanClass = beanClassName;
/*      */   }
/*      */ 
/*      */   public String getBeanClassName()
/*      */   {
/*  372 */     Object beanClassObject = this.beanClass;
/*  373 */     if ((beanClassObject instanceof Class)) {
/*  374 */       return ((Class)beanClassObject).getName();
/*      */     }
/*      */ 
/*  377 */     return (String)beanClassObject;
/*      */   }
/*      */ 
/*      */   public Class<?> resolveBeanClass(ClassLoader classLoader)
/*      */     throws ClassNotFoundException
/*      */   {
/*  390 */     String className = getBeanClassName();
/*  391 */     if (className == null) {
/*  392 */       return null;
/*      */     }
/*  394 */     Class resolvedClass = ClassUtils.forName(className, classLoader);
/*  395 */     this.beanClass = resolvedClass;
/*  396 */     return resolvedClass;
/*      */   }
/*      */ 
/*      */   public void setScope(String scope)
/*      */   {
/*  412 */     this.scope = scope;
/*      */   }
/*      */ 
/*      */   public String getScope()
/*      */   {
/*  420 */     return this.scope;
/*      */   }
/*      */ 
/*      */   public boolean isSingleton()
/*      */   {
/*  430 */     return ("singleton".equals(this.scope)) || ("".equals(this.scope));
/*      */   }
/*      */ 
/*      */   public boolean isPrototype()
/*      */   {
/*  440 */     return "prototype".equals(this.scope);
/*      */   }
/*      */ 
/*      */   public void setAbstract(boolean abstractFlag)
/*      */   {
/*  450 */     this.abstractFlag = abstractFlag;
/*      */   }
/*      */ 
/*      */   public boolean isAbstract()
/*      */   {
/*  459 */     return this.abstractFlag;
/*      */   }
/*      */ 
/*      */   public void setLazyInit(boolean lazyInit)
/*      */   {
/*  469 */     this.lazyInit = lazyInit;
/*      */   }
/*      */ 
/*      */   public boolean isLazyInit()
/*      */   {
/*  478 */     return this.lazyInit;
/*      */   }
/*      */ 
/*      */   public void setAutowireMode(int autowireMode)
/*      */   {
/*  495 */     this.autowireMode = autowireMode;
/*      */   }
/*      */ 
/*      */   public int getAutowireMode()
/*      */   {
/*  502 */     return this.autowireMode;
/*      */   }
/*      */ 
/*      */   public int getResolvedAutowireMode()
/*      */   {
/*  513 */     if (this.autowireMode == 4)
/*      */     {
/*  517 */       Constructor[] constructors = getBeanClass().getConstructors();
/*  518 */       for (Constructor constructor : constructors) {
/*  519 */         if (constructor.getParameterTypes().length == 0) {
/*  520 */           return 2;
/*      */         }
/*      */       }
/*  523 */       return 3;
/*      */     }
/*      */ 
/*  526 */     return this.autowireMode;
/*      */   }
/*      */ 
/*      */   public void setDependencyCheck(int dependencyCheck)
/*      */   {
/*  540 */     this.dependencyCheck = dependencyCheck;
/*      */   }
/*      */ 
/*      */   public int getDependencyCheck()
/*      */   {
/*  547 */     return this.dependencyCheck;
/*      */   }
/*      */ 
/*      */   public void setDependsOn(String[] dependsOn)
/*      */   {
/*  559 */     this.dependsOn = dependsOn;
/*      */   }
/*      */ 
/*      */   public String[] getDependsOn()
/*      */   {
/*  567 */     return this.dependsOn;
/*      */   }
/*      */ 
/*      */   public void setAutowireCandidate(boolean autowireCandidate)
/*      */   {
/*  575 */     this.autowireCandidate = autowireCandidate;
/*      */   }
/*      */ 
/*      */   public boolean isAutowireCandidate()
/*      */   {
/*  583 */     return this.autowireCandidate;
/*      */   }
/*      */ 
/*      */   public void setPrimary(boolean primary)
/*      */   {
/*  593 */     this.primary = primary;
/*      */   }
/*      */ 
/*      */   public boolean isPrimary()
/*      */   {
/*  603 */     return this.primary;
/*      */   }
/*      */ 
/*      */   public void addQualifier(AutowireCandidateQualifier qualifier)
/*      */   {
/*  612 */     this.qualifiers.put(qualifier.getTypeName(), qualifier);
/*      */   }
/*      */ 
/*      */   public boolean hasQualifier(String typeName)
/*      */   {
/*  619 */     return this.qualifiers.keySet().contains(typeName);
/*      */   }
/*      */ 
/*      */   public AutowireCandidateQualifier getQualifier(String typeName)
/*      */   {
/*  626 */     return (AutowireCandidateQualifier)this.qualifiers.get(typeName);
/*      */   }
/*      */ 
/*      */   public Set<AutowireCandidateQualifier> getQualifiers()
/*      */   {
/*  634 */     return new LinkedHashSet(this.qualifiers.values());
/*      */   }
/*      */ 
/*      */   public void copyQualifiersFrom(AbstractBeanDefinition source)
/*      */   {
/*  642 */     Assert.notNull(source, "Source must not be null");
/*  643 */     this.qualifiers.putAll(source.qualifiers);
/*      */   }
/*      */ 
/*      */   public void setNonPublicAccessAllowed(boolean nonPublicAccessAllowed)
/*      */   {
/*  659 */     this.nonPublicAccessAllowed = nonPublicAccessAllowed;
/*      */   }
/*      */ 
/*      */   public boolean isNonPublicAccessAllowed()
/*      */   {
/*  666 */     return this.nonPublicAccessAllowed;
/*      */   }
/*      */ 
/*      */   public void setLenientConstructorResolution(boolean lenientConstructorResolution)
/*      */   {
/*  676 */     this.lenientConstructorResolution = lenientConstructorResolution;
/*      */   }
/*      */ 
/*      */   public boolean isLenientConstructorResolution()
/*      */   {
/*  683 */     return this.lenientConstructorResolution;
/*      */   }
/*      */ 
/*      */   public void setConstructorArgumentValues(ConstructorArgumentValues constructorArgumentValues)
/*      */   {
/*  690 */     this.constructorArgumentValues = (constructorArgumentValues != null ? constructorArgumentValues : new ConstructorArgumentValues());
/*      */   }
/*      */ 
/*      */   public ConstructorArgumentValues getConstructorArgumentValues()
/*      */   {
/*  699 */     return this.constructorArgumentValues;
/*      */   }
/*      */ 
/*      */   public boolean hasConstructorArgumentValues()
/*      */   {
/*  706 */     return !this.constructorArgumentValues.isEmpty();
/*      */   }
/*      */ 
/*      */   public void setPropertyValues(MutablePropertyValues propertyValues)
/*      */   {
/*  713 */     this.propertyValues = (propertyValues != null ? propertyValues : new MutablePropertyValues());
/*      */   }
/*      */ 
/*      */   public MutablePropertyValues getPropertyValues()
/*      */   {
/*  721 */     return this.propertyValues;
/*      */   }
/*      */ 
/*      */   public void setMethodOverrides(MethodOverrides methodOverrides)
/*      */   {
/*  728 */     this.methodOverrides = (methodOverrides != null ? methodOverrides : new MethodOverrides());
/*      */   }
/*      */ 
/*      */   public MethodOverrides getMethodOverrides()
/*      */   {
/*  737 */     return this.methodOverrides;
/*      */   }
/*      */ 
/*      */   public void setFactoryBeanName(String factoryBeanName)
/*      */   {
/*  743 */     this.factoryBeanName = factoryBeanName;
/*      */   }
/*      */ 
/*      */   public String getFactoryBeanName()
/*      */   {
/*  748 */     return this.factoryBeanName;
/*      */   }
/*      */ 
/*      */   public void setFactoryMethodName(String factoryMethodName)
/*      */   {
/*  753 */     this.factoryMethodName = factoryMethodName;
/*      */   }
/*      */ 
/*      */   public String getFactoryMethodName()
/*      */   {
/*  758 */     return this.factoryMethodName;
/*      */   }
/*      */ 
/*      */   public void setInitMethodName(String initMethodName)
/*      */   {
/*  766 */     this.initMethodName = initMethodName;
/*      */   }
/*      */ 
/*      */   public String getInitMethodName()
/*      */   {
/*  773 */     return this.initMethodName;
/*      */   }
/*      */ 
/*      */   public void setEnforceInitMethod(boolean enforceInitMethod)
/*      */   {
/*  782 */     this.enforceInitMethod = enforceInitMethod;
/*      */   }
/*      */ 
/*      */   public boolean isEnforceInitMethod()
/*      */   {
/*  790 */     return this.enforceInitMethod;
/*      */   }
/*      */ 
/*      */   public void setDestroyMethodName(String destroyMethodName)
/*      */   {
/*  798 */     this.destroyMethodName = destroyMethodName;
/*      */   }
/*      */ 
/*      */   public String getDestroyMethodName()
/*      */   {
/*  805 */     return this.destroyMethodName;
/*      */   }
/*      */ 
/*      */   public void setEnforceDestroyMethod(boolean enforceDestroyMethod)
/*      */   {
/*  814 */     this.enforceDestroyMethod = enforceDestroyMethod;
/*      */   }
/*      */ 
/*      */   public boolean isEnforceDestroyMethod()
/*      */   {
/*  822 */     return this.enforceDestroyMethod;
/*      */   }
/*      */ 
/*      */   public void setSynthetic(boolean synthetic)
/*      */   {
/*  832 */     this.synthetic = synthetic;
/*      */   }
/*      */ 
/*      */   public boolean isSynthetic()
/*      */   {
/*  840 */     return this.synthetic;
/*      */   }
/*      */ 
/*      */   public void setRole(int role)
/*      */   {
/*  847 */     this.role = role;
/*      */   }
/*      */ 
/*      */   public int getRole()
/*      */   {
/*  855 */     return this.role;
/*      */   }
/*      */ 
/*      */   public void setDescription(String description)
/*      */   {
/*  863 */     this.description = description;
/*      */   }
/*      */ 
/*      */   public String getDescription()
/*      */   {
/*  868 */     return this.description;
/*      */   }
/*      */ 
/*      */   public void setResource(Resource resource)
/*      */   {
/*  876 */     this.resource = resource;
/*      */   }
/*      */ 
/*      */   public Resource getResource()
/*      */   {
/*  883 */     return this.resource;
/*      */   }
/*      */ 
/*      */   public void setResourceDescription(String resourceDescription)
/*      */   {
/*  891 */     this.resource = new DescriptiveResource(resourceDescription);
/*      */   }
/*      */ 
/*      */   public String getResourceDescription()
/*      */   {
/*  896 */     return this.resource != null ? this.resource.getDescription() : null;
/*      */   }
/*      */ 
/*      */   public void setOriginatingBeanDefinition(BeanDefinition originatingBd)
/*      */   {
/*  903 */     this.resource = new BeanDefinitionResource(originatingBd);
/*      */   }
/*      */ 
/*      */   public BeanDefinition getOriginatingBeanDefinition()
/*      */   {
/*  909 */     return (this.resource instanceof BeanDefinitionResource) ? ((BeanDefinitionResource)this.resource)
/*  909 */       .getBeanDefinition() : null;
/*      */   }
/*      */ 
/*      */   public void validate()
/*      */     throws BeanDefinitionValidationException
/*      */   {
/*  917 */     if ((!getMethodOverrides().isEmpty()) && (getFactoryMethodName() != null)) {
/*  918 */       throw new BeanDefinitionValidationException("Cannot combine static factory method with method overrides: the static factory method must create the instance");
/*      */     }
/*      */ 
/*  923 */     if (hasBeanClass())
/*  924 */       prepareMethodOverrides();
/*      */   }
/*      */ 
/*      */   public void prepareMethodOverrides()
/*      */     throws BeanDefinitionValidationException
/*      */   {
/*  935 */     MethodOverrides methodOverrides = getMethodOverrides();
/*  936 */     if (!methodOverrides.isEmpty()) {
/*  937 */       Set overrides = methodOverrides.getOverrides();
/*  938 */       synchronized (overrides) {
/*  939 */         for (MethodOverride mo : overrides)
/*  940 */           prepareMethodOverride(mo);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void prepareMethodOverride(MethodOverride mo)
/*      */     throws BeanDefinitionValidationException
/*      */   {
/*  954 */     int count = ClassUtils.getMethodCountForName(getBeanClass(), mo.getMethodName());
/*  955 */     if (count == 0)
/*      */     {
/*  958 */       throw new BeanDefinitionValidationException(new StringBuilder().append("Invalid method override: no method with name '")
/*  957 */         .append(mo
/*  957 */         .getMethodName()).append("' on class [")
/*  958 */         .append(getBeanClassName()).append("]").toString());
/*      */     }
/*  960 */     if (count == 1)
/*      */     {
/*  962 */       mo.setOverloaded(false);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */   {
/*  974 */     return cloneBeanDefinition();
/*      */   }
/*      */ 
/*      */   public abstract AbstractBeanDefinition cloneBeanDefinition();
/*      */ 
/*      */   public boolean equals(Object other)
/*      */   {
/*  987 */     if (this == other) {
/*  988 */       return true;
/*      */     }
/*  990 */     if (!(other instanceof AbstractBeanDefinition)) {
/*  991 */       return false;
/*      */     }
/*      */ 
/*  994 */     AbstractBeanDefinition that = (AbstractBeanDefinition)other;
/*      */ 
/*  996 */     if (!ObjectUtils.nullSafeEquals(getBeanClassName(), that.getBeanClassName())) return false;
/*  997 */     if (!ObjectUtils.nullSafeEquals(this.scope, that.scope)) return false;
/*  998 */     if (this.abstractFlag != that.abstractFlag) return false;
/*  999 */     if (this.lazyInit != that.lazyInit) return false;
/*      */ 
/* 1001 */     if (this.autowireMode != that.autowireMode) return false;
/* 1002 */     if (this.dependencyCheck != that.dependencyCheck) return false;
/* 1003 */     if (!Arrays.equals(this.dependsOn, that.dependsOn)) return false;
/* 1004 */     if (this.autowireCandidate != that.autowireCandidate) return false;
/* 1005 */     if (!ObjectUtils.nullSafeEquals(this.qualifiers, that.qualifiers)) return false;
/* 1006 */     if (this.primary != that.primary) return false;
/*      */ 
/* 1008 */     if (this.nonPublicAccessAllowed != that.nonPublicAccessAllowed) return false;
/* 1009 */     if (this.lenientConstructorResolution != that.lenientConstructorResolution) return false;
/* 1010 */     if (!ObjectUtils.nullSafeEquals(this.constructorArgumentValues, that.constructorArgumentValues)) return false;
/* 1011 */     if (!ObjectUtils.nullSafeEquals(this.propertyValues, that.propertyValues)) return false;
/* 1012 */     if (!ObjectUtils.nullSafeEquals(this.methodOverrides, that.methodOverrides)) return false;
/*      */ 
/* 1014 */     if (!ObjectUtils.nullSafeEquals(this.factoryBeanName, that.factoryBeanName)) return false;
/* 1015 */     if (!ObjectUtils.nullSafeEquals(this.factoryMethodName, that.factoryMethodName)) return false;
/* 1016 */     if (!ObjectUtils.nullSafeEquals(this.initMethodName, that.initMethodName)) return false;
/* 1017 */     if (this.enforceInitMethod != that.enforceInitMethod) return false;
/* 1018 */     if (!ObjectUtils.nullSafeEquals(this.destroyMethodName, that.destroyMethodName)) return false;
/* 1019 */     if (this.enforceDestroyMethod != that.enforceDestroyMethod) return false;
/*      */ 
/* 1021 */     if (this.synthetic != that.synthetic) return false;
/* 1022 */     if (this.role != that.role) return false;
/*      */ 
/* 1024 */     return super.equals(other);
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 1029 */     int hashCode = ObjectUtils.nullSafeHashCode(getBeanClassName());
/* 1030 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.scope);
/* 1031 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.constructorArgumentValues);
/* 1032 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.propertyValues);
/* 1033 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.factoryBeanName);
/* 1034 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.factoryMethodName);
/* 1035 */     hashCode = 29 * hashCode + super.hashCode();
/* 1036 */     return hashCode;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1041 */     StringBuilder sb = new StringBuilder("class [");
/* 1042 */     sb.append(getBeanClassName()).append("]");
/* 1043 */     sb.append("; scope=").append(this.scope);
/* 1044 */     sb.append("; abstract=").append(this.abstractFlag);
/* 1045 */     sb.append("; lazyInit=").append(this.lazyInit);
/* 1046 */     sb.append("; autowireMode=").append(this.autowireMode);
/* 1047 */     sb.append("; dependencyCheck=").append(this.dependencyCheck);
/* 1048 */     sb.append("; autowireCandidate=").append(this.autowireCandidate);
/* 1049 */     sb.append("; primary=").append(this.primary);
/* 1050 */     sb.append("; factoryBeanName=").append(this.factoryBeanName);
/* 1051 */     sb.append("; factoryMethodName=").append(this.factoryMethodName);
/* 1052 */     sb.append("; initMethodName=").append(this.initMethodName);
/* 1053 */     sb.append("; destroyMethodName=").append(this.destroyMethodName);
/* 1054 */     if (this.resource != null) {
/* 1055 */       sb.append("; defined in ").append(this.resource.getDescription());
/*      */     }
/* 1057 */     return sb.toString();
/*      */   }
/*      */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.support.AbstractBeanDefinition
 * JD-Core Version:    0.6.2
 */