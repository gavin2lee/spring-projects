/*     */ package org.springframework.beans.factory.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.SimpleTypeConverter;
/*     */ import org.springframework.beans.TypeConverter;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.config.DependencyDescriptor;
/*     */ import org.springframework.beans.factory.support.AutowireCandidateQualifier;
/*     */ import org.springframework.beans.factory.support.GenericTypeAwareAutowireCandidateResolver;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ public class QualifierAnnotationAutowireCandidateResolver extends GenericTypeAwareAutowireCandidateResolver
/*     */ {
/*  59 */   private final Set<Class<? extends Annotation>> qualifierTypes = new LinkedHashSet(2);
/*     */ 
/*  61 */   private Class<? extends Annotation> valueAnnotationType = Value.class;
/*     */ 
/*     */   public QualifierAnnotationAutowireCandidateResolver()
/*     */   {
/*  71 */     this.qualifierTypes.add(Qualifier.class);
/*     */     try {
/*  73 */       this.qualifierTypes.add(ClassUtils.forName("javax.inject.Qualifier", QualifierAnnotationAutowireCandidateResolver.class
/*  74 */         .getClassLoader()));
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public QualifierAnnotationAutowireCandidateResolver(Class<? extends Annotation> qualifierType)
/*     */   {
/*  87 */     Assert.notNull(qualifierType, "'qualifierType' must not be null");
/*  88 */     this.qualifierTypes.add(qualifierType);
/*     */   }
/*     */ 
/*     */   public QualifierAnnotationAutowireCandidateResolver(Set<Class<? extends Annotation>> qualifierTypes)
/*     */   {
/*  97 */     Assert.notNull(qualifierTypes, "'qualifierTypes' must not be null");
/*  98 */     this.qualifierTypes.addAll(qualifierTypes);
/*     */   }
/*     */ 
/*     */   public void addQualifierType(Class<? extends Annotation> qualifierType)
/*     */   {
/* 113 */     this.qualifierTypes.add(qualifierType);
/*     */   }
/*     */ 
/*     */   public void setValueAnnotationType(Class<? extends Annotation> valueAnnotationType)
/*     */   {
/* 126 */     this.valueAnnotationType = valueAnnotationType;
/*     */   }
/*     */ 
/*     */   public boolean isAutowireCandidate(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor)
/*     */   {
/* 144 */     boolean match = super.isAutowireCandidate(bdHolder, descriptor);
/* 145 */     if ((match) && (descriptor != null)) {
/* 146 */       match = checkQualifiers(bdHolder, descriptor.getAnnotations());
/* 147 */       if (match) {
/* 148 */         MethodParameter methodParam = descriptor.getMethodParameter();
/* 149 */         if (methodParam != null) {
/* 150 */           Method method = methodParam.getMethod();
/* 151 */           if ((method == null) || (Void.TYPE == method.getReturnType())) {
/* 152 */             match = checkQualifiers(bdHolder, methodParam.getMethodAnnotations());
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 157 */     return match;
/*     */   }
/*     */ 
/*     */   protected boolean checkQualifiers(BeanDefinitionHolder bdHolder, Annotation[] annotationsToSearch)
/*     */   {
/* 164 */     if (ObjectUtils.isEmpty(annotationsToSearch)) {
/* 165 */       return true;
/*     */     }
/* 167 */     SimpleTypeConverter typeConverter = new SimpleTypeConverter();
/* 168 */     for (Annotation annotation : annotationsToSearch) {
/* 169 */       Class type = annotation.annotationType();
/* 170 */       boolean checkMeta = true;
/* 171 */       boolean fallbackToMeta = false;
/* 172 */       if (isQualifier(type)) {
/* 173 */         if (!checkQualifier(bdHolder, annotation, typeConverter)) {
/* 174 */           fallbackToMeta = true;
/*     */         }
/*     */         else {
/* 177 */           checkMeta = false;
/*     */         }
/*     */       }
/* 180 */       if (checkMeta) {
/* 181 */         boolean foundMeta = false;
/* 182 */         for (Annotation metaAnn : type.getAnnotations()) {
/* 183 */           Class metaType = metaAnn.annotationType();
/* 184 */           if (isQualifier(metaType)) {
/* 185 */             foundMeta = true;
/*     */ 
/* 188 */             if (((fallbackToMeta) && (StringUtils.isEmpty(AnnotationUtils.getValue(metaAnn)))) || 
/* 189 */               (!checkQualifier(bdHolder, metaAnn, typeConverter)))
/*     */             {
/* 190 */               return false;
/*     */             }
/*     */           }
/*     */         }
/* 194 */         if ((fallbackToMeta) && (!foundMeta)) {
/* 195 */           return false;
/*     */         }
/*     */       }
/*     */     }
/* 199 */     return true;
/*     */   }
/*     */ 
/*     */   protected boolean isQualifier(Class<? extends Annotation> annotationType)
/*     */   {
/* 206 */     for (Class qualifierType : this.qualifierTypes) {
/* 207 */       if ((annotationType.equals(qualifierType)) || (annotationType.isAnnotationPresent(qualifierType))) {
/* 208 */         return true;
/*     */       }
/*     */     }
/* 211 */     return false;
/*     */   }
/*     */ 
/*     */   protected boolean checkQualifier(BeanDefinitionHolder bdHolder, Annotation annotation, TypeConverter typeConverter)
/*     */   {
/* 220 */     Class type = annotation.annotationType();
/* 221 */     RootBeanDefinition bd = (RootBeanDefinition)bdHolder.getBeanDefinition();
/*     */ 
/* 223 */     AutowireCandidateQualifier qualifier = bd.getQualifier(type.getName());
/* 224 */     if (qualifier == null) {
/* 225 */       qualifier = bd.getQualifier(ClassUtils.getShortName(type));
/*     */     }
/* 227 */     if (qualifier == null)
/*     */     {
/* 229 */       Annotation targetAnnotation = getFactoryMethodAnnotation(bd, type);
/* 230 */       if (targetAnnotation == null) {
/* 231 */         RootBeanDefinition dbd = getResolvedDecoratedDefinition(bd);
/* 232 */         if (dbd != null) {
/* 233 */           targetAnnotation = getFactoryMethodAnnotation(dbd, type);
/*     */         }
/*     */       }
/* 236 */       if (targetAnnotation == null)
/*     */       {
/* 238 */         if (getBeanFactory() != null) {
/*     */           try {
/* 240 */             Class beanType = getBeanFactory().getType(bdHolder.getBeanName());
/* 241 */             if (beanType != null) {
/* 242 */               targetAnnotation = AnnotationUtils.getAnnotation(ClassUtils.getUserClass(beanType), type);
/*     */             }
/*     */           }
/*     */           catch (NoSuchBeanDefinitionException localNoSuchBeanDefinitionException)
/*     */           {
/*     */           }
/*     */         }
/* 249 */         if ((targetAnnotation == null) && (bd.hasBeanClass())) {
/* 250 */           targetAnnotation = AnnotationUtils.getAnnotation(ClassUtils.getUserClass(bd.getBeanClass()), type);
/*     */         }
/*     */       }
/* 253 */       if ((targetAnnotation != null) && (targetAnnotation.equals(annotation))) {
/* 254 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 258 */     Map attributes = AnnotationUtils.getAnnotationAttributes(annotation);
/* 259 */     if ((attributes.isEmpty()) && (qualifier == null))
/*     */     {
/* 261 */       return false;
/*     */     }
/* 263 */     for (Map.Entry entry : attributes.entrySet()) {
/* 264 */       String attributeName = (String)entry.getKey();
/* 265 */       Object expectedValue = entry.getValue();
/* 266 */       Object actualValue = null;
/*     */ 
/* 268 */       if (qualifier != null) {
/* 269 */         actualValue = qualifier.getAttribute(attributeName);
/*     */       }
/* 271 */       if (actualValue == null)
/*     */       {
/* 273 */         actualValue = bd.getAttribute(attributeName);
/*     */       }
/* 275 */       if ((actualValue != null) || (!attributeName.equals(AutowireCandidateQualifier.VALUE_KEY)) || (!(expectedValue instanceof String)) || 
/* 276 */         (!bdHolder
/* 276 */         .matchesName((String)expectedValue)))
/*     */       {
/* 280 */         if ((actualValue == null) && (qualifier != null))
/*     */         {
/* 282 */           actualValue = AnnotationUtils.getDefaultValue(annotation, attributeName);
/*     */         }
/* 284 */         if (actualValue != null) {
/* 285 */           actualValue = typeConverter.convertIfNecessary(actualValue, expectedValue.getClass());
/*     */         }
/* 287 */         if (!expectedValue.equals(actualValue))
/* 288 */           return false;
/*     */       }
/*     */     }
/* 291 */     return true;
/*     */   }
/*     */ 
/*     */   protected Annotation getFactoryMethodAnnotation(RootBeanDefinition bd, Class<? extends Annotation> type) {
/* 295 */     Method resolvedFactoryMethod = bd.getResolvedFactoryMethod();
/* 296 */     return resolvedFactoryMethod != null ? AnnotationUtils.getAnnotation(resolvedFactoryMethod, type) : null;
/*     */   }
/*     */ 
/*     */   public Object getSuggestedValue(DependencyDescriptor descriptor)
/*     */   {
/* 306 */     Object value = findValue(descriptor.getAnnotations());
/* 307 */     if (value == null) {
/* 308 */       MethodParameter methodParam = descriptor.getMethodParameter();
/* 309 */       if (methodParam != null) {
/* 310 */         value = findValue(methodParam.getMethodAnnotations());
/*     */       }
/*     */     }
/* 313 */     return value;
/*     */   }
/*     */ 
/*     */   protected Object findValue(Annotation[] annotationsToSearch)
/*     */   {
/* 320 */     AnnotationAttributes attr = AnnotatedElementUtils.getMergedAnnotationAttributes(
/* 321 */       AnnotatedElementUtils.forAnnotations(annotationsToSearch), 
/* 321 */       this.valueAnnotationType);
/* 322 */     if (attr != null) {
/* 323 */       return extractValue(attr);
/*     */     }
/* 325 */     return null;
/*     */   }
/*     */ 
/*     */   protected Object extractValue(AnnotationAttributes attr)
/*     */   {
/* 333 */     Object value = attr.get("value");
/* 334 */     if (value == null) {
/* 335 */       throw new IllegalStateException("Value annotation must have a value attribute");
/*     */     }
/* 337 */     return value;
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.annotation.QualifierAnnotationAutowireCandidateResolver
 * JD-Core Version:    0.6.2
 */