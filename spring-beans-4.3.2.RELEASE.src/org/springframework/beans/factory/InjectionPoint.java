/*     */ package org.springframework.beans.factory;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ public class InjectionPoint
/*     */ {
/*     */   protected MethodParameter methodParameter;
/*     */   protected Field field;
/*     */   private volatile Annotation[] fieldAnnotations;
/*     */ 
/*     */   public InjectionPoint(MethodParameter methodParameter)
/*     */   {
/*  50 */     Assert.notNull(methodParameter, "MethodParameter must not be null");
/*  51 */     this.methodParameter = methodParameter;
/*     */   }
/*     */ 
/*     */   public InjectionPoint(Field field)
/*     */   {
/*  59 */     Assert.notNull(field, "Field must not be null");
/*  60 */     this.field = field;
/*     */   }
/*     */ 
/*     */   protected InjectionPoint(InjectionPoint original)
/*     */   {
/*  68 */     this.methodParameter = (original.methodParameter != null ? new MethodParameter(original.methodParameter) : null);
/*     */ 
/*  70 */     this.field = original.field;
/*  71 */     this.fieldAnnotations = original.fieldAnnotations;
/*     */   }
/*     */ 
/*     */   protected InjectionPoint()
/*     */   {
/*     */   }
/*     */ 
/*     */   public MethodParameter getMethodParameter()
/*     */   {
/*  87 */     return this.methodParameter;
/*     */   }
/*     */ 
/*     */   public Field getField()
/*     */   {
/*  96 */     return this.field;
/*     */   }
/*     */ 
/*     */   public Annotation[] getAnnotations()
/*     */   {
/* 103 */     if (this.field != null) {
/* 104 */       if (this.fieldAnnotations == null) {
/* 105 */         this.fieldAnnotations = this.field.getAnnotations();
/*     */       }
/* 107 */       return this.fieldAnnotations;
/*     */     }
/*     */ 
/* 110 */     return this.methodParameter.getParameterAnnotations();
/*     */   }
/*     */ 
/*     */   public Class<?> getDeclaredType()
/*     */   {
/* 119 */     return this.field != null ? this.field.getType() : this.methodParameter.getParameterType();
/*     */   }
/*     */ 
/*     */   public Member getMember()
/*     */   {
/* 127 */     return this.field != null ? this.field : this.methodParameter.getMember();
/*     */   }
/*     */ 
/*     */   public AnnotatedElement getAnnotatedElement()
/*     */   {
/* 140 */     return this.field != null ? this.field : this.methodParameter.getAnnotatedElement();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/* 146 */     if (this == other) {
/* 147 */       return true;
/*     */     }
/* 149 */     if (getClass() != other.getClass()) {
/* 150 */       return false;
/*     */     }
/* 152 */     InjectionPoint otherPoint = (InjectionPoint)other;
/*     */ 
/* 154 */     return this.field != null ? this.field.equals(otherPoint.field) : this.methodParameter
/* 154 */       .equals(otherPoint.methodParameter);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 159 */     return this.field != null ? this.field.hashCode() : this.methodParameter.hashCode();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 164 */     return this.field != null ? "field '" + this.field.getName() + "'" : this.methodParameter.toString();
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.InjectionPoint
 * JD-Core Version:    0.6.2
 */