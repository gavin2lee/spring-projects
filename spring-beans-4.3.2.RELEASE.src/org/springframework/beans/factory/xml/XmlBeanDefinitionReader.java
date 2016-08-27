/*     */ package org.springframework.beans.factory.xml;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.parsing.EmptyReaderEventListener;
/*     */ import org.springframework.beans.factory.parsing.FailFastProblemReporter;
/*     */ import org.springframework.beans.factory.parsing.NullSourceExtractor;
/*     */ import org.springframework.beans.factory.parsing.ProblemReporter;
/*     */ import org.springframework.beans.factory.parsing.ReaderEventListener;
/*     */ import org.springframework.beans.factory.parsing.SourceExtractor;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinitionReader;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.core.Constants;
/*     */ import org.springframework.core.NamedThreadLocal;
/*     */ import org.springframework.core.io.DescriptiveResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.io.support.EncodedResource;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.xml.SimpleSaxErrorHandler;
/*     */ import org.springframework.util.xml.XmlValidationModeDetector;
/*     */ import org.w3c.dom.Document;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader
/*     */ {
/*     */   public static final int VALIDATION_NONE = 0;
/*     */   public static final int VALIDATION_AUTO = 1;
/*     */   public static final int VALIDATION_DTD = 2;
/*     */   public static final int VALIDATION_XSD = 3;
/* 101 */   private static final Constants constants = new Constants(XmlBeanDefinitionReader.class);
/*     */ 
/* 103 */   private int validationMode = 1;
/*     */ 
/* 105 */   private boolean namespaceAware = false;
/*     */ 
/* 107 */   private Class<?> documentReaderClass = DefaultBeanDefinitionDocumentReader.class;
/*     */ 
/* 109 */   private ProblemReporter problemReporter = new FailFastProblemReporter();
/*     */ 
/* 111 */   private ReaderEventListener eventListener = new EmptyReaderEventListener();
/*     */ 
/* 113 */   private SourceExtractor sourceExtractor = new NullSourceExtractor();
/*     */   private NamespaceHandlerResolver namespaceHandlerResolver;
/* 117 */   private DocumentLoader documentLoader = new DefaultDocumentLoader();
/*     */   private EntityResolver entityResolver;
/* 121 */   private ErrorHandler errorHandler = new SimpleSaxErrorHandler(this.logger);
/*     */ 
/* 123 */   private final XmlValidationModeDetector validationModeDetector = new XmlValidationModeDetector();
/*     */ 
/* 125 */   private final ThreadLocal<Set<EncodedResource>> resourcesCurrentlyBeingLoaded = new NamedThreadLocal("XML bean definition resources currently being loaded");
/*     */ 
/*     */   public XmlBeanDefinitionReader(BeanDefinitionRegistry registry)
/*     */   {
/* 135 */     super(registry);
/*     */   }
/*     */ 
/*     */   public void setValidating(boolean validating)
/*     */   {
/* 147 */     this.validationMode = (validating ? 1 : 0);
/* 148 */     this.namespaceAware = (!validating);
/*     */   }
/*     */ 
/*     */   public void setValidationModeName(String validationModeName)
/*     */   {
/* 156 */     setValidationMode(constants.asNumber(validationModeName).intValue());
/*     */   }
/*     */ 
/*     */   public void setValidationMode(int validationMode)
/*     */   {
/* 166 */     this.validationMode = validationMode;
/*     */   }
/*     */ 
/*     */   public int getValidationMode()
/*     */   {
/* 173 */     return this.validationMode;
/*     */   }
/*     */ 
/*     */   public void setNamespaceAware(boolean namespaceAware)
/*     */   {
/* 184 */     this.namespaceAware = namespaceAware;
/*     */   }
/*     */ 
/*     */   public boolean isNamespaceAware()
/*     */   {
/* 191 */     return this.namespaceAware;
/*     */   }
/*     */ 
/*     */   public void setProblemReporter(ProblemReporter problemReporter)
/*     */   {
/* 201 */     this.problemReporter = (problemReporter != null ? problemReporter : new FailFastProblemReporter());
/*     */   }
/*     */ 
/*     */   public void setEventListener(ReaderEventListener eventListener)
/*     */   {
/* 211 */     this.eventListener = (eventListener != null ? eventListener : new EmptyReaderEventListener());
/*     */   }
/*     */ 
/*     */   public void setSourceExtractor(SourceExtractor sourceExtractor)
/*     */   {
/* 221 */     this.sourceExtractor = (sourceExtractor != null ? sourceExtractor : new NullSourceExtractor());
/*     */   }
/*     */ 
/*     */   public void setNamespaceHandlerResolver(NamespaceHandlerResolver namespaceHandlerResolver)
/*     */   {
/* 230 */     this.namespaceHandlerResolver = namespaceHandlerResolver;
/*     */   }
/*     */ 
/*     */   public void setDocumentLoader(DocumentLoader documentLoader)
/*     */   {
/* 239 */     this.documentLoader = (documentLoader != null ? documentLoader : new DefaultDocumentLoader());
/*     */   }
/*     */ 
/*     */   public void setEntityResolver(EntityResolver entityResolver)
/*     */   {
/* 248 */     this.entityResolver = entityResolver;
/*     */   }
/*     */ 
/*     */   protected EntityResolver getEntityResolver()
/*     */   {
/* 256 */     if (this.entityResolver == null)
/*     */     {
/* 258 */       ResourceLoader resourceLoader = getResourceLoader();
/* 259 */       if (resourceLoader != null) {
/* 260 */         this.entityResolver = new ResourceEntityResolver(resourceLoader);
/*     */       }
/*     */       else {
/* 263 */         this.entityResolver = new DelegatingEntityResolver(getBeanClassLoader());
/*     */       }
/*     */     }
/* 266 */     return this.entityResolver;
/*     */   }
/*     */ 
/*     */   public void setErrorHandler(ErrorHandler errorHandler)
/*     */   {
/* 278 */     this.errorHandler = errorHandler;
/*     */   }
/*     */ 
/*     */   public void setDocumentReaderClass(Class<?> documentReaderClass)
/*     */   {
/* 288 */     if ((documentReaderClass == null) || (!BeanDefinitionDocumentReader.class.isAssignableFrom(documentReaderClass))) {
/* 289 */       throw new IllegalArgumentException("documentReaderClass must be an implementation of the BeanDefinitionDocumentReader interface");
/*     */     }
/*     */ 
/* 292 */     this.documentReaderClass = documentReaderClass;
/*     */   }
/*     */ 
/*     */   public int loadBeanDefinitions(Resource resource)
/*     */     throws BeanDefinitionStoreException
/*     */   {
/* 304 */     return loadBeanDefinitions(new EncodedResource(resource));
/*     */   }
/*     */ 
/*     */   public int loadBeanDefinitions(EncodedResource encodedResource)
/*     */     throws BeanDefinitionStoreException
/*     */   {
/* 315 */     Assert.notNull(encodedResource, "EncodedResource must not be null");
/* 316 */     if (this.logger.isInfoEnabled()) {
/* 317 */       this.logger.info("Loading XML bean definitions from " + encodedResource.getResource());
/*     */     }
/*     */ 
/* 320 */     Set currentResources = (Set)this.resourcesCurrentlyBeingLoaded.get();
/* 321 */     if (currentResources == null) {
/* 322 */       currentResources = new HashSet(4);
/* 323 */       this.resourcesCurrentlyBeingLoaded.set(currentResources);
/*     */     }
/* 325 */     if (!currentResources.add(encodedResource)) {
/* 326 */       throw new BeanDefinitionStoreException("Detected cyclic loading of " + encodedResource + " - check your import definitions!");
/*     */     }
/*     */     try
/*     */     {
/* 330 */       InputStream inputStream = encodedResource.getResource().getInputStream();
/*     */       try {
/* 332 */         InputSource inputSource = new InputSource(inputStream);
/* 333 */         if (encodedResource.getEncoding() != null) {
/* 334 */           inputSource.setEncoding(encodedResource.getEncoding());
/*     */         }
/* 336 */         int i = doLoadBeanDefinitions(inputSource, encodedResource.getResource());
/*     */ 
/* 339 */         inputStream.close();
/*     */ 
/* 349 */         return i;
/*     */       }
/*     */       finally
/*     */       {
/* 339 */         inputStream.close();
/*     */       }
/*     */     }
/*     */     catch (IOException ex)
/*     */     {
/* 344 */       throw new BeanDefinitionStoreException("IOException parsing XML document from " + encodedResource
/* 344 */         .getResource(), ex);
/*     */     }
/*     */     finally {
/* 347 */       currentResources.remove(encodedResource);
/* 348 */       if (currentResources.isEmpty())
/* 349 */         this.resourcesCurrentlyBeingLoaded.remove();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int loadBeanDefinitions(InputSource inputSource)
/*     */     throws BeanDefinitionStoreException
/*     */   {
/* 361 */     return loadBeanDefinitions(inputSource, "resource loaded through SAX InputSource");
/*     */   }
/*     */ 
/*     */   public int loadBeanDefinitions(InputSource inputSource, String resourceDescription)
/*     */     throws BeanDefinitionStoreException
/*     */   {
/* 375 */     return doLoadBeanDefinitions(inputSource, new DescriptiveResource(resourceDescription));
/*     */   }
/*     */ 
/*     */   protected int doLoadBeanDefinitions(InputSource inputSource, Resource resource)
/*     */     throws BeanDefinitionStoreException
/*     */   {
/*     */     try
/*     */     {
/* 391 */       Document doc = doLoadDocument(inputSource, resource);
/* 392 */       return registerBeanDefinitions(doc, resource);
/*     */     }
/*     */     catch (BeanDefinitionStoreException ex) {
/* 395 */       throw ex;
/*     */     }
/*     */     catch (SAXParseException ex)
/*     */     {
/* 399 */       throw new XmlBeanDefinitionStoreException(resource.getDescription(), "Line " + ex
/* 399 */         .getLineNumber() + " in XML document from " + resource + " is invalid", ex);
/*     */     }
/*     */     catch (SAXException ex) {
/* 402 */       throw new XmlBeanDefinitionStoreException(resource.getDescription(), "XML document from " + resource + " is invalid", ex);
/*     */     }
/*     */     catch (ParserConfigurationException ex)
/*     */     {
/* 406 */       throw new BeanDefinitionStoreException(resource.getDescription(), "Parser configuration exception parsing XML from " + resource, ex);
/*     */     }
/*     */     catch (IOException ex)
/*     */     {
/* 410 */       throw new BeanDefinitionStoreException(resource.getDescription(), "IOException parsing XML document from " + resource, ex);
/*     */     }
/*     */     catch (Throwable ex)
/*     */     {
/* 414 */       throw new BeanDefinitionStoreException(resource.getDescription(), "Unexpected exception parsing XML document from " + resource, ex);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Document doLoadDocument(InputSource inputSource, Resource resource)
/*     */     throws Exception
/*     */   {
/* 429 */     return this.documentLoader.loadDocument(inputSource, getEntityResolver(), this.errorHandler, 
/* 430 */       getValidationModeForResource(resource), 
/* 430 */       isNamespaceAware());
/*     */   }
/*     */ 
/*     */   protected int getValidationModeForResource(Resource resource)
/*     */   {
/* 442 */     int validationModeToUse = getValidationMode();
/* 443 */     if (validationModeToUse != 1) {
/* 444 */       return validationModeToUse;
/*     */     }
/* 446 */     int detectedMode = detectValidationMode(resource);
/* 447 */     if (detectedMode != 1) {
/* 448 */       return detectedMode;
/*     */     }
/*     */ 
/* 453 */     return 3;
/*     */   }
/*     */ 
/*     */   protected int detectValidationMode(Resource resource)
/*     */   {
/* 464 */     if (resource.isOpen()) {
/* 465 */       throw new BeanDefinitionStoreException("Passed-in Resource [" + resource + "] contains an open stream: " + "cannot determine validation mode automatically. Either pass in a Resource " + "that is able to create fresh streams, or explicitly specify the validationMode " + "on your XmlBeanDefinitionReader instance.");
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 474 */       inputStream = resource.getInputStream();
/*     */     }
/*     */     catch (IOException ex)
/*     */     {
/*     */       InputStream inputStream;
/* 477 */       throw new BeanDefinitionStoreException("Unable to determine validation mode for [" + resource + "]: cannot open InputStream. " + "Did you attempt to load directly from a SAX InputSource without specifying the " + "validationMode on your XmlBeanDefinitionReader instance?", ex);
/*     */     }
/*     */     try
/*     */     {
/*     */       InputStream inputStream;
/* 484 */       return this.validationModeDetector.detectValidationMode(inputStream);
/*     */     }
/*     */     catch (IOException ex) {
/* 487 */       throw new BeanDefinitionStoreException("Unable to determine validation mode for [" + resource + "]: an error occurred whilst reading from the InputStream.", ex);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int registerBeanDefinitions(Document doc, Resource resource)
/*     */     throws BeanDefinitionStoreException
/*     */   {
/* 506 */     BeanDefinitionDocumentReader documentReader = createBeanDefinitionDocumentReader();
/* 507 */     int countBefore = getRegistry().getBeanDefinitionCount();
/* 508 */     documentReader.registerBeanDefinitions(doc, createReaderContext(resource));
/* 509 */     return getRegistry().getBeanDefinitionCount() - countBefore;
/*     */   }
/*     */ 
/*     */   protected BeanDefinitionDocumentReader createBeanDefinitionDocumentReader()
/*     */   {
/* 519 */     return (BeanDefinitionDocumentReader)BeanDefinitionDocumentReader.class.cast(BeanUtils.instantiateClass(this.documentReaderClass));
/*     */   }
/*     */ 
/*     */   public XmlReaderContext createReaderContext(Resource resource)
/*     */   {
/* 527 */     return new XmlReaderContext(resource, this.problemReporter, this.eventListener, this.sourceExtractor, this, 
/* 527 */       getNamespaceHandlerResolver());
/*     */   }
/*     */ 
/*     */   public NamespaceHandlerResolver getNamespaceHandlerResolver()
/*     */   {
/* 535 */     if (this.namespaceHandlerResolver == null) {
/* 536 */       this.namespaceHandlerResolver = createDefaultNamespaceHandlerResolver();
/*     */     }
/* 538 */     return this.namespaceHandlerResolver;
/*     */   }
/*     */ 
/*     */   protected NamespaceHandlerResolver createDefaultNamespaceHandlerResolver()
/*     */   {
/* 546 */     return new DefaultNamespaceHandlerResolver(getResourceLoader().getClassLoader());
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.xml.XmlBeanDefinitionReader
 * JD-Core Version:    0.6.2
 */