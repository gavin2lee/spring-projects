/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.util.Properties;
/*     */ import java.util.prefs.BackingStoreException;
/*     */ import java.util.prefs.Preferences;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ 
/*     */ public class PreferencesPlaceholderConfigurer extends PropertyPlaceholderConfigurer
/*     */   implements InitializingBean
/*     */ {
/*     */   private String systemTreePath;
/*     */   private String userTreePath;
/*     */   private Preferences systemPrefs;
/*     */   private Preferences userPrefs;
/*     */ 
/*     */   public void setSystemTreePath(String systemTreePath)
/*     */   {
/*  61 */     this.systemTreePath = systemTreePath;
/*     */   }
/*     */ 
/*     */   public void setUserTreePath(String userTreePath)
/*     */   {
/*  69 */     this.userTreePath = userTreePath;
/*     */   }
/*     */ 
/*     */   public void afterPropertiesSet()
/*     */   {
/*  79 */     this.systemPrefs = (this.systemTreePath != null ? 
/*  80 */       Preferences.systemRoot().node(this.systemTreePath) : Preferences.systemRoot());
/*  81 */     this.userPrefs = (this.userTreePath != null ? 
/*  82 */       Preferences.userRoot().node(this.userTreePath) : Preferences.userRoot());
/*     */   }
/*     */ 
/*     */   protected String resolvePlaceholder(String placeholder, Properties props)
/*     */   {
/*  92 */     String path = null;
/*  93 */     String key = placeholder;
/*  94 */     int endOfPath = placeholder.lastIndexOf('/');
/*  95 */     if (endOfPath != -1) {
/*  96 */       path = placeholder.substring(0, endOfPath);
/*  97 */       key = placeholder.substring(endOfPath + 1);
/*     */     }
/*  99 */     String value = resolvePlaceholder(path, key, this.userPrefs);
/* 100 */     if (value == null) {
/* 101 */       value = resolvePlaceholder(path, key, this.systemPrefs);
/* 102 */       if (value == null) {
/* 103 */         value = props.getProperty(placeholder);
/*     */       }
/*     */     }
/* 106 */     return value;
/*     */   }
/*     */ 
/*     */   protected String resolvePlaceholder(String path, String key, Preferences preferences)
/*     */   {
/* 117 */     if (path != null) {
/*     */       try
/*     */       {
/* 120 */         if (preferences.nodeExists(path)) {
/* 121 */           return preferences.node(path).get(key, null);
/*     */         }
/*     */ 
/* 124 */         return null;
/*     */       }
/*     */       catch (BackingStoreException ex)
/*     */       {
/* 128 */         throw new BeanDefinitionStoreException("Cannot access specified node path [" + path + "]", ex);
/*     */       }
/*     */     }
/*     */ 
/* 132 */     return preferences.get(key, null);
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.config.PreferencesPlaceholderConfigurer
 * JD-Core Version:    0.6.2
 */