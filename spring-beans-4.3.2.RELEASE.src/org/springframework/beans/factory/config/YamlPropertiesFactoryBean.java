/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ 
/*     */ public class YamlPropertiesFactoryBean extends YamlProcessor
/*     */   implements FactoryBean<Properties>, InitializingBean
/*     */ {
/*  73 */   private boolean singleton = true;
/*     */   private Properties properties;
/*     */ 
/*     */   public void setSingleton(boolean singleton)
/*     */   {
/*  83 */     this.singleton = singleton;
/*     */   }
/*     */ 
/*     */   public boolean isSingleton()
/*     */   {
/*  88 */     return this.singleton;
/*     */   }
/*     */ 
/*     */   public void afterPropertiesSet()
/*     */   {
/*  93 */     if (isSingleton())
/*  94 */       this.properties = createProperties();
/*     */   }
/*     */ 
/*     */   public Properties getObject()
/*     */   {
/* 100 */     return this.properties != null ? this.properties : createProperties();
/*     */   }
/*     */ 
/*     */   public Class<?> getObjectType()
/*     */   {
/* 105 */     return Properties.class;
/*     */   }
/*     */ 
/*     */   protected Properties createProperties()
/*     */   {
/* 119 */     final Properties result = new Properties();
/* 120 */     process(new YamlProcessor.MatchCallback()
/*     */     {
/*     */       public void process(Properties properties, Map<String, Object> map) {
/* 123 */         result.putAll(properties);
/*     */       }
/*     */     });
/* 126 */     return result;
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.config.YamlPropertiesFactoryBean
 * JD-Core Version:    0.6.2
 */