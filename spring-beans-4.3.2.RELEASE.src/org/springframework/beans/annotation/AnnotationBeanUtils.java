/*    */ package org.springframework.beans.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Arrays;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import org.springframework.beans.BeanWrapper;
/*    */ import org.springframework.beans.PropertyAccessorFactory;
/*    */ import org.springframework.util.ReflectionUtils;
/*    */ import org.springframework.util.StringValueResolver;
/*    */ 
/*    */ public abstract class AnnotationBeanUtils
/*    */ {
/*    */   public static void copyPropertiesToBean(Annotation ann, Object bean, String[] excludedProperties)
/*    */   {
/* 48 */     copyPropertiesToBean(ann, bean, null, excludedProperties);
/*    */   }
/*    */ 
/*    */   public static void copyPropertiesToBean(Annotation ann, Object bean, StringValueResolver valueResolver, String[] excludedProperties)
/*    */   {
/* 62 */     Set excluded = new HashSet(Arrays.asList(excludedProperties));
/* 63 */     Method[] annotationProperties = ann.annotationType().getDeclaredMethods();
/* 64 */     BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(bean);
/* 65 */     for (Method annotationProperty : annotationProperties) {
/* 66 */       String propertyName = annotationProperty.getName();
/* 67 */       if ((!excluded.contains(propertyName)) && (bw.isWritableProperty(propertyName))) {
/* 68 */         Object value = ReflectionUtils.invokeMethod(annotationProperty, ann);
/* 69 */         if ((valueResolver != null) && ((value instanceof String))) {
/* 70 */           value = valueResolver.resolveStringValue((String)value);
/*    */         }
/* 72 */         bw.setPropertyValue(propertyName, value);
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.annotation.AnnotationBeanUtils
 * JD-Core Version:    0.6.2
 */