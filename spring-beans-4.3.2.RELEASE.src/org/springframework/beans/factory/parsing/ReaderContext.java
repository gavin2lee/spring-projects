/*     */ package org.springframework.beans.factory.parsing;
/*     */ 
/*     */ import org.springframework.core.io.Resource;
/*     */ 
/*     */ public class ReaderContext
/*     */ {
/*     */   private final Resource resource;
/*     */   private final ProblemReporter problemReporter;
/*     */   private final ReaderEventListener eventListener;
/*     */   private final SourceExtractor sourceExtractor;
/*     */ 
/*     */   public ReaderContext(Resource resource, ProblemReporter problemReporter, ReaderEventListener eventListener, SourceExtractor sourceExtractor)
/*     */   {
/*  43 */     this.resource = resource;
/*  44 */     this.problemReporter = problemReporter;
/*  45 */     this.eventListener = eventListener;
/*  46 */     this.sourceExtractor = sourceExtractor;
/*     */   }
/*     */ 
/*     */   public final Resource getResource() {
/*  50 */     return this.resource;
/*     */   }
/*     */ 
/*     */   public void fatal(String message, Object source)
/*     */   {
/*  55 */     fatal(message, source, null, null);
/*     */   }
/*     */ 
/*     */   public void fatal(String message, Object source, Throwable ex) {
/*  59 */     fatal(message, source, null, ex);
/*     */   }
/*     */ 
/*     */   public void fatal(String message, Object source, ParseState parseState) {
/*  63 */     fatal(message, source, parseState, null);
/*     */   }
/*     */ 
/*     */   public void fatal(String message, Object source, ParseState parseState, Throwable cause) {
/*  67 */     Location location = new Location(getResource(), source);
/*  68 */     this.problemReporter.fatal(new Problem(message, location, parseState, cause));
/*     */   }
/*     */ 
/*     */   public void error(String message, Object source) {
/*  72 */     error(message, source, null, null);
/*     */   }
/*     */ 
/*     */   public void error(String message, Object source, Throwable ex) {
/*  76 */     error(message, source, null, ex);
/*     */   }
/*     */ 
/*     */   public void error(String message, Object source, ParseState parseState) {
/*  80 */     error(message, source, parseState, null);
/*     */   }
/*     */ 
/*     */   public void error(String message, Object source, ParseState parseState, Throwable cause) {
/*  84 */     Location location = new Location(getResource(), source);
/*  85 */     this.problemReporter.error(new Problem(message, location, parseState, cause));
/*     */   }
/*     */ 
/*     */   public void warning(String message, Object source) {
/*  89 */     warning(message, source, null, null);
/*     */   }
/*     */ 
/*     */   public void warning(String message, Object source, Throwable ex) {
/*  93 */     warning(message, source, null, ex);
/*     */   }
/*     */ 
/*     */   public void warning(String message, Object source, ParseState parseState) {
/*  97 */     warning(message, source, parseState, null);
/*     */   }
/*     */ 
/*     */   public void warning(String message, Object source, ParseState parseState, Throwable cause) {
/* 101 */     Location location = new Location(getResource(), source);
/* 102 */     this.problemReporter.warning(new Problem(message, location, parseState, cause));
/*     */   }
/*     */ 
/*     */   public void fireDefaultsRegistered(DefaultsDefinition defaultsDefinition)
/*     */   {
/* 107 */     this.eventListener.defaultsRegistered(defaultsDefinition);
/*     */   }
/*     */ 
/*     */   public void fireComponentRegistered(ComponentDefinition componentDefinition) {
/* 111 */     this.eventListener.componentRegistered(componentDefinition);
/*     */   }
/*     */ 
/*     */   public void fireAliasRegistered(String beanName, String alias, Object source) {
/* 115 */     this.eventListener.aliasRegistered(new AliasDefinition(beanName, alias, source));
/*     */   }
/*     */ 
/*     */   public void fireImportProcessed(String importedResource, Object source) {
/* 119 */     this.eventListener.importProcessed(new ImportDefinition(importedResource, source));
/*     */   }
/*     */ 
/*     */   public void fireImportProcessed(String importedResource, Resource[] actualResources, Object source) {
/* 123 */     this.eventListener.importProcessed(new ImportDefinition(importedResource, actualResources, source));
/*     */   }
/*     */ 
/*     */   public SourceExtractor getSourceExtractor()
/*     */   {
/* 128 */     return this.sourceExtractor;
/*     */   }
/*     */ 
/*     */   public Object extractSource(Object sourceCandidate) {
/* 132 */     return this.sourceExtractor.extractSource(sourceCandidate, this.resource);
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.ReaderContext
 * JD-Core Version:    0.6.2
 */