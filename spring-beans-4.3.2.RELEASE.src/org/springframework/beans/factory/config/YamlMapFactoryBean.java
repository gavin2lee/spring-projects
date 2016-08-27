/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Properties;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ 
/*     */ public class YamlMapFactoryBean extends YamlProcessor
/*     */   implements FactoryBean<Map<String, Object>>, InitializingBean
/*     */ {
/*  69 */   private boolean singleton = true;
/*     */   private Map<String, Object> map;
/*     */ 
/*     */   public void setSingleton(boolean singleton)
/*     */   {
/*  79 */     this.singleton = singleton;
/*     */   }
/*     */ 
/*     */   public boolean isSingleton()
/*     */   {
/*  84 */     return this.singleton;
/*     */   }
/*     */ 
/*     */   public void afterPropertiesSet()
/*     */   {
/*  89 */     if (isSingleton())
/*  90 */       this.map = createMap();
/*     */   }
/*     */ 
/*     */   public Map<String, Object> getObject()
/*     */   {
/*  96 */     return this.map != null ? this.map : createMap();
/*     */   }
/*     */ 
/*     */   public Class<?> getObjectType()
/*     */   {
/* 101 */     return Map.class;
/*     */   }
/*     */ 
/*     */   protected Map<String, Object> createMap()
/*     */   {
/* 115 */     final Map result = new LinkedHashMap();
/* 116 */     process(new YamlProcessor.MatchCallback()
/*     */     {
/*     */       public void process(Properties properties, Map<String, Object> map) {
/* 119 */         YamlMapFactoryBean.this.merge(result, map);
/*     */       }
/*     */     });
/* 122 */     return result;
/*     */   }
/*     */ 
/*     */   private void merge(Map<String, Object> output, Map<String, Object> map)
/*     */   {
/* 127 */     for (Map.Entry entry : map.entrySet()) {
/* 128 */       String key = (String)entry.getKey();
/* 129 */       Object value = entry.getValue();
/* 130 */       Object existing = output.get(key);
/* 131 */       if (((value instanceof Map)) && ((existing instanceof Map))) {
/* 132 */         Map result = new LinkedHashMap((Map)existing);
/* 133 */         merge(result, (Map)value);
/* 134 */         output.put(key, result);
/*     */       }
/*     */       else {
/* 137 */         output.put(key, value);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.config.YamlMapFactoryBean
 * JD-Core Version:    0.6.2
 */