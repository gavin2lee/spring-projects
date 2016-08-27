/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanCreationException;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.BeanIsNotAFactoryException;
/*     */ import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
/*     */ import org.springframework.beans.factory.SmartFactoryBean;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ public class StaticListableBeanFactory
/*     */   implements ListableBeanFactory
/*     */ {
/*     */   private final Map<String, Object> beans;
/*     */ 
/*     */   public StaticListableBeanFactory()
/*     */   {
/*  70 */     this.beans = new LinkedHashMap();
/*     */   }
/*     */ 
/*     */   public StaticListableBeanFactory(Map<String, Object> beans)
/*     */   {
/*  84 */     Assert.notNull(beans, "Beans Map must not be null");
/*  85 */     this.beans = beans;
/*     */   }
/*     */ 
/*     */   public void addBean(String name, Object bean)
/*     */   {
/*  96 */     this.beans.put(name, bean);
/*     */   }
/*     */ 
/*     */   public Object getBean(String name)
/*     */     throws BeansException
/*     */   {
/* 106 */     String beanName = BeanFactoryUtils.transformedBeanName(name);
/* 107 */     Object bean = this.beans.get(beanName);
/*     */ 
/* 109 */     if (bean == null)
/*     */     {
/* 111 */       throw new NoSuchBeanDefinitionException(beanName, "Defined beans are [" + 
/* 111 */         StringUtils.collectionToCommaDelimitedString(this.beans
/* 111 */         .keySet()) + "]");
/*     */     }
/*     */ 
/* 116 */     if ((BeanFactoryUtils.isFactoryDereference(name)) && (!(bean instanceof FactoryBean))) {
/* 117 */       throw new BeanIsNotAFactoryException(beanName, bean.getClass());
/*     */     }
/*     */ 
/* 120 */     if (((bean instanceof FactoryBean)) && (!BeanFactoryUtils.isFactoryDereference(name))) {
/*     */       try {
/* 122 */         return ((FactoryBean)bean).getObject();
/*     */       }
/*     */       catch (Exception ex) {
/* 125 */         throw new BeanCreationException(beanName, "FactoryBean threw exception on object creation", ex);
/*     */       }
/*     */     }
/*     */ 
/* 129 */     return bean;
/*     */   }
/*     */ 
/*     */   public <T> T getBean(String name, Class<T> requiredType)
/*     */     throws BeansException
/*     */   {
/* 136 */     Object bean = getBean(name);
/* 137 */     if ((requiredType != null) && (!requiredType.isAssignableFrom(bean.getClass()))) {
/* 138 */       throw new BeanNotOfRequiredTypeException(name, requiredType, bean.getClass());
/*     */     }
/* 140 */     return bean;
/*     */   }
/*     */ 
/*     */   public <T> T getBean(Class<T> requiredType) throws BeansException
/*     */   {
/* 145 */     String[] beanNames = getBeanNamesForType(requiredType);
/* 146 */     if (beanNames.length == 1) {
/* 147 */       return getBean(beanNames[0], requiredType);
/*     */     }
/* 149 */     if (beanNames.length > 1) {
/* 150 */       throw new NoUniqueBeanDefinitionException(requiredType, beanNames);
/*     */     }
/*     */ 
/* 153 */     throw new NoSuchBeanDefinitionException(requiredType);
/*     */   }
/*     */ 
/*     */   public Object getBean(String name, Object[] args)
/*     */     throws BeansException
/*     */   {
/* 159 */     if (args != null) {
/* 160 */       throw new UnsupportedOperationException("StaticListableBeanFactory does not support explicit bean creation arguments");
/*     */     }
/*     */ 
/* 163 */     return getBean(name);
/*     */   }
/*     */ 
/*     */   public <T> T getBean(Class<T> requiredType, Object[] args) throws BeansException
/*     */   {
/* 168 */     if (args != null) {
/* 169 */       throw new UnsupportedOperationException("StaticListableBeanFactory does not support explicit bean creation arguments");
/*     */     }
/*     */ 
/* 172 */     return getBean(requiredType);
/*     */   }
/*     */ 
/*     */   public boolean containsBean(String name)
/*     */   {
/* 177 */     return this.beans.containsKey(name);
/*     */   }
/*     */ 
/*     */   public boolean isSingleton(String name) throws NoSuchBeanDefinitionException
/*     */   {
/* 182 */     Object bean = getBean(name);
/*     */ 
/* 184 */     return ((bean instanceof FactoryBean)) && (((FactoryBean)bean).isSingleton());
/*     */   }
/*     */ 
/*     */   public boolean isPrototype(String name) throws NoSuchBeanDefinitionException
/*     */   {
/* 189 */     Object bean = getBean(name);
/*     */ 
/* 192 */     return (((bean instanceof SmartFactoryBean)) && (((SmartFactoryBean)bean).isPrototype())) || (((bean instanceof FactoryBean)) && 
/* 192 */       (!((FactoryBean)bean)
/* 192 */       .isSingleton()));
/*     */   }
/*     */ 
/*     */   public boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException
/*     */   {
/* 197 */     Class type = getType(name);
/* 198 */     return (type != null) && (typeToMatch.isAssignableFrom(type));
/*     */   }
/*     */ 
/*     */   public boolean isTypeMatch(String name, Class<?> typeToMatch) throws NoSuchBeanDefinitionException
/*     */   {
/* 203 */     Class type = getType(name);
/* 204 */     return (typeToMatch == null) || ((type != null) && (typeToMatch.isAssignableFrom(type)));
/*     */   }
/*     */ 
/*     */   public Class<?> getType(String name) throws NoSuchBeanDefinitionException
/*     */   {
/* 209 */     String beanName = BeanFactoryUtils.transformedBeanName(name);
/*     */ 
/* 211 */     Object bean = this.beans.get(beanName);
/* 212 */     if (bean == null)
/*     */     {
/* 214 */       throw new NoSuchBeanDefinitionException(beanName, "Defined beans are [" + 
/* 214 */         StringUtils.collectionToCommaDelimitedString(this.beans
/* 214 */         .keySet()) + "]");
/*     */     }
/*     */ 
/* 217 */     if (((bean instanceof FactoryBean)) && (!BeanFactoryUtils.isFactoryDereference(name)))
/*     */     {
/* 219 */       return ((FactoryBean)bean).getObjectType();
/*     */     }
/* 221 */     return bean.getClass();
/*     */   }
/*     */ 
/*     */   public String[] getAliases(String name)
/*     */   {
/* 226 */     return new String[0];
/*     */   }
/*     */ 
/*     */   public boolean containsBeanDefinition(String name)
/*     */   {
/* 236 */     return this.beans.containsKey(name);
/*     */   }
/*     */ 
/*     */   public int getBeanDefinitionCount()
/*     */   {
/* 241 */     return this.beans.size();
/*     */   }
/*     */ 
/*     */   public String[] getBeanDefinitionNames()
/*     */   {
/* 246 */     return StringUtils.toStringArray(this.beans.keySet());
/*     */   }
/*     */ 
/*     */   public String[] getBeanNamesForType(ResolvableType type)
/*     */   {
/* 251 */     boolean isFactoryType = (type != null) && (FactoryBean.class.isAssignableFrom(type.getRawClass()));
/* 252 */     List matches = new ArrayList();
/* 253 */     for (Map.Entry entry : this.beans.entrySet()) {
/* 254 */       String name = (String)entry.getKey();
/* 255 */       Object beanInstance = entry.getValue();
/* 256 */       if (((beanInstance instanceof FactoryBean)) && (!isFactoryType)) {
/* 257 */         Class objectType = ((FactoryBean)beanInstance).getObjectType();
/* 258 */         if ((objectType != null) && ((type == null) || (type.isAssignableFrom(objectType)))) {
/* 259 */           matches.add(name);
/*     */         }
/*     */ 
/*     */       }
/* 263 */       else if ((type == null) || (type.isInstance(beanInstance))) {
/* 264 */         matches.add(name);
/*     */       }
/*     */     }
/*     */ 
/* 268 */     return StringUtils.toStringArray(matches);
/*     */   }
/*     */ 
/*     */   public String[] getBeanNamesForType(Class<?> type)
/*     */   {
/* 273 */     return getBeanNamesForType(ResolvableType.forClass(type));
/*     */   }
/*     */ 
/*     */   public String[] getBeanNamesForType(Class<?> type, boolean includeNonSingletons, boolean allowEagerInit)
/*     */   {
/* 278 */     return getBeanNamesForType(ResolvableType.forClass(type));
/*     */   }
/*     */ 
/*     */   public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException
/*     */   {
/* 283 */     return getBeansOfType(type, true, true);
/*     */   }
/*     */ 
/*     */   public <T> Map<String, T> getBeansOfType(Class<T> type, boolean includeNonSingletons, boolean allowEagerInit)
/*     */     throws BeansException
/*     */   {
/* 291 */     boolean isFactoryType = (type != null) && (FactoryBean.class.isAssignableFrom(type));
/* 292 */     Map matches = new LinkedHashMap();
/*     */ 
/* 294 */     for (Map.Entry entry : this.beans.entrySet()) {
/* 295 */       String beanName = (String)entry.getKey();
/* 296 */       Object beanInstance = entry.getValue();
/*     */ 
/* 298 */       if (((beanInstance instanceof FactoryBean)) && (!isFactoryType))
/*     */       {
/* 300 */         FactoryBean factory = (FactoryBean)beanInstance;
/* 301 */         Class objectType = factory.getObjectType();
/* 302 */         if (((includeNonSingletons) || (factory.isSingleton())) && (objectType != null) && ((type == null) || 
/* 303 */           (type
/* 303 */           .isAssignableFrom(objectType))))
/*     */         {
/* 304 */           matches.put(beanName, getBean(beanName, type));
/*     */         }
/*     */ 
/*     */       }
/* 308 */       else if ((type == null) || (type.isInstance(beanInstance)))
/*     */       {
/* 311 */         if (isFactoryType) {
/* 312 */           beanName = "&" + beanName;
/*     */         }
/* 314 */         matches.put(beanName, beanInstance);
/*     */       }
/*     */     }
/*     */ 
/* 318 */     return matches;
/*     */   }
/*     */ 
/*     */   public String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType)
/*     */   {
/* 323 */     List results = new ArrayList();
/* 324 */     for (String beanName : this.beans.keySet()) {
/* 325 */       if (findAnnotationOnBean(beanName, annotationType) != null) {
/* 326 */         results.add(beanName);
/*     */       }
/*     */     }
/* 329 */     return (String[])results.toArray(new String[results.size()]);
/*     */   }
/*     */ 
/*     */   public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType)
/*     */     throws BeansException
/*     */   {
/* 336 */     Map results = new LinkedHashMap();
/* 337 */     for (String beanName : this.beans.keySet()) {
/* 338 */       if (findAnnotationOnBean(beanName, annotationType) != null) {
/* 339 */         results.put(beanName, getBean(beanName));
/*     */       }
/*     */     }
/* 342 */     return results;
/*     */   }
/*     */ 
/*     */   public <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType)
/*     */     throws NoSuchBeanDefinitionException
/*     */   {
/* 349 */     return AnnotationUtils.findAnnotation(getType(beanName), annotationType);
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.support.StaticListableBeanFactory
 * JD-Core Version:    0.6.2
 */