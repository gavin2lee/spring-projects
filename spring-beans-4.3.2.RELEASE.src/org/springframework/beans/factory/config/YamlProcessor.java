/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.yaml.snakeyaml.Yaml;
/*     */ import org.yaml.snakeyaml.constructor.Constructor;
/*     */ import org.yaml.snakeyaml.nodes.MappingNode;
/*     */ import org.yaml.snakeyaml.parser.ParserException;
/*     */ import org.yaml.snakeyaml.reader.UnicodeReader;
/*     */ 
/*     */ public abstract class YamlProcessor
/*     */ {
/*  52 */   private final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*  54 */   private ResolutionMethod resolutionMethod = ResolutionMethod.OVERRIDE;
/*     */ 
/*  56 */   private Resource[] resources = new Resource[0];
/*     */ 
/*  58 */   private List<DocumentMatcher> documentMatchers = Collections.emptyList();
/*     */ 
/*  60 */   private boolean matchDefault = true;
/*     */ 
/*     */   public void setDocumentMatchers(DocumentMatcher[] matchers)
/*     */   {
/*  89 */     this.documentMatchers = Arrays.asList(matchers);
/*     */   }
/*     */ 
/*     */   public void setMatchDefault(boolean matchDefault)
/*     */   {
/*  99 */     this.matchDefault = matchDefault;
/*     */   }
/*     */ 
/*     */   public void setResolutionMethod(ResolutionMethod resolutionMethod)
/*     */   {
/* 110 */     Assert.notNull(resolutionMethod, "ResolutionMethod must not be null");
/* 111 */     this.resolutionMethod = resolutionMethod;
/*     */   }
/*     */ 
/*     */   public void setResources(Resource[] resources)
/*     */   {
/* 119 */     this.resources = resources;
/*     */   }
/*     */ 
/*     */   protected void process(MatchCallback callback)
/*     */   {
/* 134 */     Yaml yaml = createYaml();
/* 135 */     for (Resource resource : this.resources) {
/* 136 */       boolean found = process(callback, yaml, resource);
/* 137 */       if ((this.resolutionMethod == ResolutionMethod.FIRST_FOUND) && (found))
/* 138 */         return;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Yaml createYaml()
/*     */   {
/* 147 */     return new Yaml(new StrictMapAppenderConstructor());
/*     */   }
/*     */ 
/*     */   private boolean process(MatchCallback callback, Yaml yaml, Resource resource) {
/* 151 */     int count = 0;
/*     */     try {
/* 153 */       if (this.logger.isDebugEnabled()) {
/* 154 */         this.logger.debug(new StringBuilder().append("Loading from YAML: ").append(resource).toString());
/*     */       }
/* 156 */       Reader reader = new UnicodeReader(resource.getInputStream());
/*     */       try {
/* 158 */         for (Iterator localIterator = yaml.loadAll(reader).iterator(); localIterator.hasNext(); ) { Object object = localIterator.next();
/* 159 */           if ((object != null) && (process(asMap(object), callback))) {
/* 160 */             count++;
/* 161 */             if (this.resolutionMethod == ResolutionMethod.FIRST_FOUND) {
/*     */               break;
/*     */             }
/*     */           }
/*     */         }
/* 166 */         if (this.logger.isDebugEnabled()) {
/* 167 */           this.logger.debug(new StringBuilder().append("Loaded ").append(count).append(" document").append(count > 1 ? "s" : "").append(" from YAML resource: ").append(resource).toString());
/*     */         }
/*     */       }
/*     */       finally
/*     */       {
/* 172 */         reader.close();
/*     */       }
/*     */     }
/*     */     catch (IOException ex) {
/* 176 */       handleProcessError(resource, ex);
/*     */     }
/* 178 */     return count > 0;
/*     */   }
/*     */ 
/*     */   private void handleProcessError(Resource resource, IOException ex) {
/* 182 */     if ((this.resolutionMethod != ResolutionMethod.FIRST_FOUND) && (this.resolutionMethod != ResolutionMethod.OVERRIDE_AND_IGNORE))
/*     */     {
/* 184 */       throw new IllegalStateException(ex);
/*     */     }
/* 186 */     if (this.logger.isWarnEnabled())
/* 187 */       this.logger.warn(new StringBuilder().append("Could not load map from ").append(resource).append(": ").append(ex.getMessage()).toString());
/*     */   }
/*     */ 
/*     */   private Map<String, Object> asMap(Object object)
/*     */   {
/* 194 */     Map result = new LinkedHashMap();
/* 195 */     if (!(object instanceof Map))
/*     */     {
/* 197 */       result.put("document", object);
/* 198 */       return result;
/*     */     }
/*     */ 
/* 201 */     Map map = (Map)object;
/* 202 */     for (Map.Entry entry : map.entrySet()) {
/* 203 */       Object value = entry.getValue();
/* 204 */       if ((value instanceof Map)) {
/* 205 */         value = asMap(value);
/*     */       }
/* 207 */       Object key = entry.getKey();
/* 208 */       if ((key instanceof CharSequence)) {
/* 209 */         result.put(key.toString(), value);
/*     */       }
/*     */       else
/*     */       {
/* 213 */         result.put(new StringBuilder().append("[").append(key.toString()).append("]").toString(), value);
/*     */       }
/*     */     }
/* 216 */     return result;
/*     */   }
/*     */ 
/*     */   private boolean process(Map<String, Object> map, MatchCallback callback) {
/* 220 */     Properties properties = new Properties();
/* 221 */     properties.putAll(getFlattenedMap(map));
/*     */ 
/* 223 */     if (this.documentMatchers.isEmpty()) {
/* 224 */       if (this.logger.isDebugEnabled()) {
/* 225 */         this.logger.debug(new StringBuilder().append("Merging document (no matchers set): ").append(map).toString());
/*     */       }
/* 227 */       callback.process(properties, map);
/* 228 */       return true;
/*     */     }
/*     */ 
/* 231 */     MatchStatus result = MatchStatus.ABSTAIN;
/* 232 */     for (DocumentMatcher matcher : this.documentMatchers) {
/* 233 */       MatchStatus match = matcher.matches(properties);
/* 234 */       result = MatchStatus.getMostSpecific(match, result);
/* 235 */       if (match == MatchStatus.FOUND) {
/* 236 */         if (this.logger.isDebugEnabled()) {
/* 237 */           this.logger.debug(new StringBuilder().append("Matched document with document matcher: ").append(properties).toString());
/*     */         }
/* 239 */         callback.process(properties, map);
/* 240 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 244 */     if ((result == MatchStatus.ABSTAIN) && (this.matchDefault)) {
/* 245 */       if (this.logger.isDebugEnabled()) {
/* 246 */         this.logger.debug(new StringBuilder().append("Matched document with default matcher: ").append(map).toString());
/*     */       }
/* 248 */       callback.process(properties, map);
/* 249 */       return true;
/*     */     }
/*     */ 
/* 252 */     if (this.logger.isDebugEnabled()) {
/* 253 */       this.logger.debug(new StringBuilder().append("Unmatched document: ").append(map).toString());
/*     */     }
/* 255 */     return false;
/*     */   }
/*     */ 
/*     */   protected final Map<String, Object> getFlattenedMap(Map<String, Object> source)
/*     */   {
/* 268 */     Map result = new LinkedHashMap();
/* 269 */     buildFlattenedMap(result, source, null);
/* 270 */     return result;
/*     */   }
/*     */ 
/*     */   private void buildFlattenedMap(Map<String, Object> result, Map<String, Object> source, String path) {
/* 274 */     for (Map.Entry entry : source.entrySet()) {
/* 275 */       String key = (String)entry.getKey();
/* 276 */       if (StringUtils.hasText(path)) {
/* 277 */         if (key.startsWith("[")) {
/* 278 */           key = new StringBuilder().append(path).append(key).toString();
/*     */         }
/*     */         else {
/* 281 */           key = new StringBuilder().append(path).append(".").append(key).toString();
/*     */         }
/*     */       }
/* 284 */       Object value = entry.getValue();
/* 285 */       if ((value instanceof String)) {
/* 286 */         result.put(key, value);
/*     */       }
/* 288 */       else if ((value instanceof Map))
/*     */       {
/* 291 */         Map map = (Map)value;
/* 292 */         buildFlattenedMap(result, map, key);
/*     */       }
/*     */       else
/*     */       {
/*     */         int count;
/*     */         Iterator localIterator2;
/* 294 */         if ((value instanceof Collection))
/*     */         {
/* 297 */           Collection collection = (Collection)value;
/* 298 */           count = 0;
/* 299 */           for (localIterator2 = collection.iterator(); localIterator2.hasNext(); ) { Object object = localIterator2.next();
/* 300 */             buildFlattenedMap(result, 
/* 301 */               Collections.singletonMap(new StringBuilder().append("[").append(count++).append("]").toString(), object), 
/* 301 */               key); }
/*     */         }
/*     */         else
/*     */         {
/* 305 */           result.put(key, value != null ? value : "");
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static class StrictMapAppenderConstructor extends Constructor
/*     */   {
/*     */     protected Map<Object, Object> constructMapping(MappingNode node)
/*     */     {
/*     */       try
/*     */       {
/* 403 */         return super.constructMapping(node);
/*     */       }
/*     */       catch (IllegalStateException ex)
/*     */       {
/* 407 */         throw new ParserException("while parsing MappingNode", node
/* 407 */           .getStartMark(), ex.getMessage(), node.getEndMark());
/*     */       }
/*     */     }
/*     */ 
/*     */     protected Map<Object, Object> createDefaultMap()
/*     */     {
/* 413 */       final Map delegate = super.createDefaultMap();
/* 414 */       return new AbstractMap()
/*     */       {
/*     */         public Object put(Object key, Object value) {
/* 417 */           if (delegate.containsKey(key)) {
/* 418 */             throw new IllegalStateException("Duplicate key: " + key);
/*     */           }
/* 420 */           return delegate.put(key, value);
/*     */         }
/*     */ 
/*     */         public Set<Map.Entry<Object, Object>> entrySet() {
/* 424 */           return delegate.entrySet();
/*     */         }
/*     */       };
/*     */     }
/*     */   }
/*     */ 
/*     */   public static enum ResolutionMethod
/*     */   {
/* 376 */     OVERRIDE, 
/*     */ 
/* 381 */     OVERRIDE_AND_IGNORE, 
/*     */ 
/* 386 */     FIRST_FOUND;
/*     */   }
/*     */ 
/*     */   public static enum MatchStatus
/*     */   {
/* 347 */     FOUND, 
/*     */ 
/* 352 */     NOT_FOUND, 
/*     */ 
/* 357 */     ABSTAIN;
/*     */ 
/*     */     public static MatchStatus getMostSpecific(MatchStatus a, MatchStatus b)
/*     */     {
/* 363 */       return a.ordinal() < b.ordinal() ? a : b;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract interface DocumentMatcher
/*     */   {
/*     */     public abstract YamlProcessor.MatchStatus matches(Properties paramProperties);
/*     */   }
/*     */ 
/*     */   public static abstract interface MatchCallback
/*     */   {
/*     */     public abstract void process(Properties paramProperties, Map<String, Object> paramMap);
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.config.YamlProcessor
 * JD-Core Version:    0.6.2
 */