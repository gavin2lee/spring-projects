/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Map;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.InjectionPoint;
/*     */ import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
/*     */ import org.springframework.core.GenericCollectionTypeResolver;
/*     */ import org.springframework.core.GenericTypeResolver;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ParameterNameDiscoverer;
/*     */ import org.springframework.core.ResolvableType;
/*     */ 
/*     */ public class DependencyDescriptor extends InjectionPoint
/*     */   implements Serializable
/*     */ {
/*     */   private final Class<?> declaringClass;
/*     */   private String methodName;
/*     */   private Class<?>[] parameterTypes;
/*     */   private int parameterIndex;
/*     */   private String fieldName;
/*     */   private final boolean required;
/*     */   private final boolean eager;
/*  62 */   private int nestingLevel = 1;
/*     */   private Class<?> containingClass;
/*     */ 
/*     */   public DependencyDescriptor(MethodParameter methodParameter, boolean required)
/*     */   {
/*  74 */     this(methodParameter, required, true);
/*     */   }
/*     */ 
/*     */   public DependencyDescriptor(MethodParameter methodParameter, boolean required, boolean eager)
/*     */   {
/*  85 */     super(methodParameter);
/*  86 */     this.declaringClass = methodParameter.getDeclaringClass();
/*  87 */     if (this.methodParameter.getMethod() != null) {
/*  88 */       this.methodName = methodParameter.getMethod().getName();
/*  89 */       this.parameterTypes = methodParameter.getMethod().getParameterTypes();
/*     */     }
/*     */     else {
/*  92 */       this.parameterTypes = methodParameter.getConstructor().getParameterTypes();
/*     */     }
/*  94 */     this.parameterIndex = methodParameter.getParameterIndex();
/*  95 */     this.containingClass = methodParameter.getContainingClass();
/*  96 */     this.required = required;
/*  97 */     this.eager = eager;
/*     */   }
/*     */ 
/*     */   public DependencyDescriptor(Field field, boolean required)
/*     */   {
/* 107 */     this(field, required, true);
/*     */   }
/*     */ 
/*     */   public DependencyDescriptor(Field field, boolean required, boolean eager)
/*     */   {
/* 118 */     super(field);
/* 119 */     this.declaringClass = field.getDeclaringClass();
/* 120 */     this.fieldName = field.getName();
/* 121 */     this.required = required;
/* 122 */     this.eager = eager;
/*     */   }
/*     */ 
/*     */   public DependencyDescriptor(DependencyDescriptor original)
/*     */   {
/* 130 */     super(original);
/* 131 */     this.declaringClass = original.declaringClass;
/* 132 */     this.methodName = original.methodName;
/* 133 */     this.parameterTypes = original.parameterTypes;
/* 134 */     this.parameterIndex = original.parameterIndex;
/* 135 */     this.fieldName = original.fieldName;
/* 136 */     this.containingClass = original.containingClass;
/* 137 */     this.required = original.required;
/* 138 */     this.eager = original.eager;
/* 139 */     this.nestingLevel = original.nestingLevel;
/*     */   }
/*     */ 
/*     */   public boolean isRequired()
/*     */   {
/* 147 */     return this.required;
/*     */   }
/*     */ 
/*     */   public boolean isEager()
/*     */   {
/* 155 */     return this.eager;
/*     */   }
/*     */ 
/*     */   public Object resolveNotUnique(Class<?> type, Map<String, Object> matchingBeans)
/*     */     throws BeansException
/*     */   {
/* 172 */     throw new NoUniqueBeanDefinitionException(type, matchingBeans.keySet());
/*     */   }
/*     */ 
/*     */   public Object resolveShortcut(BeanFactory beanFactory)
/*     */     throws BeansException
/*     */   {
/* 188 */     return null;
/*     */   }
/*     */ 
/*     */   public Object resolveCandidate(String beanName, Class<?> requiredType, BeanFactory beanFactory)
/*     */     throws BeansException
/*     */   {
/* 207 */     return beanFactory.getBean(beanName, requiredType);
/*     */   }
/*     */ 
/*     */   public void increaseNestingLevel()
/*     */   {
/* 216 */     this.nestingLevel += 1;
/* 217 */     if (this.methodParameter != null)
/* 218 */       this.methodParameter.increaseNestingLevel();
/*     */   }
/*     */ 
/*     */   public void setContainingClass(Class<?> containingClass)
/*     */   {
/* 229 */     this.containingClass = containingClass;
/* 230 */     if (this.methodParameter != null)
/* 231 */       GenericTypeResolver.resolveParameterType(this.methodParameter, containingClass);
/*     */   }
/*     */ 
/*     */   public ResolvableType getResolvableType()
/*     */   {
/* 241 */     return this.field != null ? ResolvableType.forField(this.field, this.nestingLevel, this.containingClass) : 
/* 241 */       ResolvableType.forMethodParameter(this.methodParameter);
/*     */   }
/*     */ 
/*     */   public boolean fallbackMatchAllowed()
/*     */   {
/* 252 */     return false;
/*     */   }
/*     */ 
/*     */   public DependencyDescriptor forFallbackMatch()
/*     */   {
/* 261 */     return new DependencyDescriptor(this)
/*     */     {
/*     */       public boolean fallbackMatchAllowed() {
/* 264 */         return true;
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public void initParameterNameDiscovery(ParameterNameDiscoverer parameterNameDiscoverer)
/*     */   {
/* 276 */     if (this.methodParameter != null)
/* 277 */       this.methodParameter.initParameterNameDiscovery(parameterNameDiscoverer);
/*     */   }
/*     */ 
/*     */   public String getDependencyName()
/*     */   {
/* 286 */     return this.field != null ? this.field.getName() : this.methodParameter.getParameterName();
/*     */   }
/*     */ 
/*     */   public Class<?> getDependencyType()
/*     */   {
/* 294 */     if (this.field != null) {
/* 295 */       if (this.nestingLevel > 1) {
/* 296 */         Type type = this.field.getGenericType();
/* 297 */         for (int i = 2; i <= this.nestingLevel; i++) {
/* 298 */           if ((type instanceof ParameterizedType)) {
/* 299 */             Type[] args = ((ParameterizedType)type).getActualTypeArguments();
/* 300 */             type = args[(args.length - 1)];
/*     */           }
/*     */         }
/*     */ 
/* 304 */         if ((type instanceof Class)) {
/* 305 */           return (Class)type;
/*     */         }
/* 307 */         if ((type instanceof ParameterizedType)) {
/* 308 */           Type arg = ((ParameterizedType)type).getRawType();
/* 309 */           if ((arg instanceof Class)) {
/* 310 */             return (Class)arg;
/*     */           }
/*     */         }
/* 313 */         return Object.class;
/*     */       }
/*     */ 
/* 316 */       return this.field.getType();
/*     */     }
/*     */ 
/* 320 */     return this.methodParameter.getNestedParameterType();
/*     */   }
/*     */ 
/*     */   public Class<?> getCollectionType()
/*     */   {
/* 331 */     return this.field != null ? 
/* 330 */       GenericCollectionTypeResolver.getCollectionFieldType(this.field, this.nestingLevel) : 
/* 331 */       GenericCollectionTypeResolver.getCollectionParameterType(this.methodParameter);
/*     */   }
/*     */ 
/*     */   public Class<?> getMapKeyType()
/*     */   {
/* 341 */     return this.field != null ? 
/* 340 */       GenericCollectionTypeResolver.getMapKeyFieldType(this.field, this.nestingLevel) : 
/* 341 */       GenericCollectionTypeResolver.getMapKeyParameterType(this.methodParameter);
/*     */   }
/*     */ 
/*     */   public Class<?> getMapValueType()
/*     */   {
/* 351 */     return this.field != null ? 
/* 350 */       GenericCollectionTypeResolver.getMapValueFieldType(this.field, this.nestingLevel) : 
/* 351 */       GenericCollectionTypeResolver.getMapValueParameterType(this.methodParameter);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/* 357 */     if (this == other) {
/* 358 */       return true;
/*     */     }
/* 360 */     if (!super.equals(other)) {
/* 361 */       return false;
/*     */     }
/* 363 */     DependencyDescriptor otherDesc = (DependencyDescriptor)other;
/* 364 */     return (this.required == otherDesc.required) && (this.eager == otherDesc.eager) && (this.nestingLevel == otherDesc.nestingLevel) && (this.containingClass == otherDesc.containingClass);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream ois)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 375 */     ois.defaultReadObject();
/*     */     try
/*     */     {
/* 379 */       if (this.fieldName != null) {
/* 380 */         this.field = this.declaringClass.getDeclaredField(this.fieldName);
/*     */       }
/*     */       else {
/* 383 */         if (this.methodName != null) {
/* 384 */           this.methodParameter = new MethodParameter(this.declaringClass
/* 385 */             .getDeclaredMethod(this.methodName, this.parameterTypes), 
/* 385 */             this.parameterIndex);
/*     */         }
/*     */         else {
/* 388 */           this.methodParameter = new MethodParameter(this.declaringClass
/* 389 */             .getDeclaredConstructor(this.parameterTypes), 
/* 389 */             this.parameterIndex);
/*     */         }
/* 391 */         for (int i = 1; i < this.nestingLevel; i++)
/* 392 */           this.methodParameter.increaseNestingLevel();
/*     */       }
/*     */     }
/*     */     catch (Throwable ex)
/*     */     {
/* 397 */       throw new IllegalStateException("Could not find original class structure", ex);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.config.DependencyDescriptor
 * JD-Core Version:    0.6.2
 */