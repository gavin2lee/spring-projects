/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.FactoryBeanNotInitializedException;
/*     */ 
/*     */ public class MethodInvokingFactoryBean extends MethodInvokingBean
/*     */   implements FactoryBean<Object>
/*     */ {
/*  85 */   private boolean singleton = true;
/*     */ 
/*  87 */   private boolean initialized = false;
/*     */   private Object singletonObject;
/*     */ 
/*     */   public void setSingleton(boolean singleton)
/*     */   {
/*  98 */     this.singleton = singleton;
/*     */   }
/*     */ 
/*     */   public void afterPropertiesSet() throws Exception
/*     */   {
/* 103 */     prepare();
/* 104 */     if (this.singleton) {
/* 105 */       this.initialized = true;
/* 106 */       this.singletonObject = invokeWithTargetException();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object getObject()
/*     */     throws Exception
/*     */   {
/* 118 */     if (this.singleton) {
/* 119 */       if (!this.initialized) {
/* 120 */         throw new FactoryBeanNotInitializedException();
/*     */       }
/*     */ 
/* 123 */       return this.singletonObject;
/*     */     }
/*     */ 
/* 127 */     return invokeWithTargetException();
/*     */   }
/*     */ 
/*     */   public Class<?> getObjectType()
/*     */   {
/* 137 */     if (!isPrepared())
/*     */     {
/* 139 */       return null;
/*     */     }
/* 141 */     return getPreparedMethod().getReturnType();
/*     */   }
/*     */ 
/*     */   public boolean isSingleton()
/*     */   {
/* 146 */     return this.singleton;
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.config.MethodInvokingFactoryBean
 * JD-Core Version:    0.6.2
 */