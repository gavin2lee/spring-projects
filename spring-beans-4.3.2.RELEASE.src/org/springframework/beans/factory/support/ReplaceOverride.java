/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ public class ReplaceOverride extends MethodOverride
/*     */ {
/*     */   private final String methodReplacerBeanName;
/*  41 */   private List<String> typeIdentifiers = new LinkedList();
/*     */ 
/*     */   public ReplaceOverride(String methodName, String methodReplacerBeanName)
/*     */   {
/*  50 */     super(methodName);
/*  51 */     Assert.notNull(methodName, "Method replacer bean name must not be null");
/*  52 */     this.methodReplacerBeanName = methodReplacerBeanName;
/*     */   }
/*     */ 
/*     */   public String getMethodReplacerBeanName()
/*     */   {
/*  60 */     return this.methodReplacerBeanName;
/*     */   }
/*     */ 
/*     */   public void addTypeIdentifier(String identifier)
/*     */   {
/*  69 */     this.typeIdentifiers.add(identifier);
/*     */   }
/*     */ 
/*     */   public boolean matches(Method method)
/*     */   {
/*  74 */     if (!method.getName().equals(getMethodName())) {
/*  75 */       return false;
/*     */     }
/*  77 */     if (!isOverloaded())
/*     */     {
/*  79 */       return true;
/*     */     }
/*     */ 
/*  82 */     if (this.typeIdentifiers.size() != method.getParameterTypes().length) {
/*  83 */       return false;
/*     */     }
/*  85 */     for (int i = 0; i < this.typeIdentifiers.size(); i++) {
/*  86 */       String identifier = (String)this.typeIdentifiers.get(i);
/*  87 */       if (!method.getParameterTypes()[i].getName().contains(identifier)) {
/*  88 */         return false;
/*     */       }
/*     */     }
/*  91 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/*  97 */     if ((!(other instanceof ReplaceOverride)) || (!super.equals(other))) {
/*  98 */       return false;
/*     */     }
/* 100 */     ReplaceOverride that = (ReplaceOverride)other;
/*     */ 
/* 102 */     return (ObjectUtils.nullSafeEquals(this.methodReplacerBeanName, that.methodReplacerBeanName)) && 
/* 102 */       (ObjectUtils.nullSafeEquals(this.typeIdentifiers, that.typeIdentifiers));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 107 */     int hashCode = super.hashCode();
/* 108 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.methodReplacerBeanName);
/* 109 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.typeIdentifiers);
/* 110 */     return hashCode;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 115 */     return "Replace override for method '" + getMethodName() + "'";
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.support.ReplaceOverride
 * JD-Core Version:    0.6.2
 */