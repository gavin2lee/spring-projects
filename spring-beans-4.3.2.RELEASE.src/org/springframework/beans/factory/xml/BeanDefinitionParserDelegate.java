/*      */ package org.springframework.beans.factory.xml;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.apache.commons.logging.LogFactory;
/*      */ import org.springframework.beans.BeanMetadataAttribute;
/*      */ import org.springframework.beans.BeanMetadataAttributeAccessor;
/*      */ import org.springframework.beans.MutablePropertyValues;
/*      */ import org.springframework.beans.PropertyValue;
/*      */ import org.springframework.beans.factory.config.BeanDefinition;
/*      */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*      */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*      */ import org.springframework.beans.factory.config.ConstructorArgumentValues.ValueHolder;
/*      */ import org.springframework.beans.factory.config.RuntimeBeanNameReference;
/*      */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*      */ import org.springframework.beans.factory.config.TypedStringValue;
/*      */ import org.springframework.beans.factory.parsing.BeanEntry;
/*      */ import org.springframework.beans.factory.parsing.ConstructorArgumentEntry;
/*      */ import org.springframework.beans.factory.parsing.ParseState;
/*      */ import org.springframework.beans.factory.parsing.PropertyEntry;
/*      */ import org.springframework.beans.factory.parsing.QualifierEntry;
/*      */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*      */ import org.springframework.beans.factory.support.AutowireCandidateQualifier;
/*      */ import org.springframework.beans.factory.support.BeanDefinitionDefaults;
/*      */ import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
/*      */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*      */ import org.springframework.beans.factory.support.LookupOverride;
/*      */ import org.springframework.beans.factory.support.ManagedArray;
/*      */ import org.springframework.beans.factory.support.ManagedList;
/*      */ import org.springframework.beans.factory.support.ManagedMap;
/*      */ import org.springframework.beans.factory.support.ManagedProperties;
/*      */ import org.springframework.beans.factory.support.ManagedSet;
/*      */ import org.springframework.beans.factory.support.MethodOverrides;
/*      */ import org.springframework.beans.factory.support.ReplaceOverride;
/*      */ import org.springframework.core.env.Environment;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.CollectionUtils;
/*      */ import org.springframework.util.ObjectUtils;
/*      */ import org.springframework.util.PatternMatchUtils;
/*      */ import org.springframework.util.StringUtils;
/*      */ import org.springframework.util.xml.DomUtils;
/*      */ import org.w3c.dom.Element;
/*      */ import org.w3c.dom.NamedNodeMap;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.NodeList;
/*      */ 
/*      */ public class BeanDefinitionParserDelegate
/*      */ {
/*      */   public static final String BEANS_NAMESPACE_URI = "http://www.springframework.org/schema/beans";
/*      */   public static final String MULTI_VALUE_ATTRIBUTE_DELIMITERS = ",; ";
/*      */   public static final String TRUE_VALUE = "true";
/*      */   public static final String FALSE_VALUE = "false";
/*      */   public static final String DEFAULT_VALUE = "default";
/*      */   public static final String DESCRIPTION_ELEMENT = "description";
/*      */   public static final String AUTOWIRE_NO_VALUE = "no";
/*      */   public static final String AUTOWIRE_BY_NAME_VALUE = "byName";
/*      */   public static final String AUTOWIRE_BY_TYPE_VALUE = "byType";
/*      */   public static final String AUTOWIRE_CONSTRUCTOR_VALUE = "constructor";
/*      */   public static final String AUTOWIRE_AUTODETECT_VALUE = "autodetect";
/*      */   public static final String DEPENDENCY_CHECK_ALL_ATTRIBUTE_VALUE = "all";
/*      */   public static final String DEPENDENCY_CHECK_SIMPLE_ATTRIBUTE_VALUE = "simple";
/*      */   public static final String DEPENDENCY_CHECK_OBJECTS_ATTRIBUTE_VALUE = "objects";
/*      */   public static final String NAME_ATTRIBUTE = "name";
/*      */   public static final String BEAN_ELEMENT = "bean";
/*      */   public static final String META_ELEMENT = "meta";
/*      */   public static final String ID_ATTRIBUTE = "id";
/*      */   public static final String PARENT_ATTRIBUTE = "parent";
/*      */   public static final String CLASS_ATTRIBUTE = "class";
/*      */   public static final String ABSTRACT_ATTRIBUTE = "abstract";
/*      */   public static final String SCOPE_ATTRIBUTE = "scope";
/*      */   private static final String SINGLETON_ATTRIBUTE = "singleton";
/*      */   public static final String LAZY_INIT_ATTRIBUTE = "lazy-init";
/*      */   public static final String AUTOWIRE_ATTRIBUTE = "autowire";
/*      */   public static final String AUTOWIRE_CANDIDATE_ATTRIBUTE = "autowire-candidate";
/*      */   public static final String PRIMARY_ATTRIBUTE = "primary";
/*      */   public static final String DEPENDENCY_CHECK_ATTRIBUTE = "dependency-check";
/*      */   public static final String DEPENDS_ON_ATTRIBUTE = "depends-on";
/*      */   public static final String INIT_METHOD_ATTRIBUTE = "init-method";
/*      */   public static final String DESTROY_METHOD_ATTRIBUTE = "destroy-method";
/*      */   public static final String FACTORY_METHOD_ATTRIBUTE = "factory-method";
/*      */   public static final String FACTORY_BEAN_ATTRIBUTE = "factory-bean";
/*      */   public static final String CONSTRUCTOR_ARG_ELEMENT = "constructor-arg";
/*      */   public static final String INDEX_ATTRIBUTE = "index";
/*      */   public static final String TYPE_ATTRIBUTE = "type";
/*      */   public static final String VALUE_TYPE_ATTRIBUTE = "value-type";
/*      */   public static final String KEY_TYPE_ATTRIBUTE = "key-type";
/*      */   public static final String PROPERTY_ELEMENT = "property";
/*      */   public static final String REF_ATTRIBUTE = "ref";
/*      */   public static final String VALUE_ATTRIBUTE = "value";
/*      */   public static final String LOOKUP_METHOD_ELEMENT = "lookup-method";
/*      */   public static final String REPLACED_METHOD_ELEMENT = "replaced-method";
/*      */   public static final String REPLACER_ATTRIBUTE = "replacer";
/*      */   public static final String ARG_TYPE_ELEMENT = "arg-type";
/*      */   public static final String ARG_TYPE_MATCH_ATTRIBUTE = "match";
/*      */   public static final String REF_ELEMENT = "ref";
/*      */   public static final String IDREF_ELEMENT = "idref";
/*      */   public static final String BEAN_REF_ATTRIBUTE = "bean";
/*      */   public static final String LOCAL_REF_ATTRIBUTE = "local";
/*      */   public static final String PARENT_REF_ATTRIBUTE = "parent";
/*      */   public static final String VALUE_ELEMENT = "value";
/*      */   public static final String NULL_ELEMENT = "null";
/*      */   public static final String ARRAY_ELEMENT = "array";
/*      */   public static final String LIST_ELEMENT = "list";
/*      */   public static final String SET_ELEMENT = "set";
/*      */   public static final String MAP_ELEMENT = "map";
/*      */   public static final String ENTRY_ELEMENT = "entry";
/*      */   public static final String KEY_ELEMENT = "key";
/*      */   public static final String KEY_ATTRIBUTE = "key";
/*      */   public static final String KEY_REF_ATTRIBUTE = "key-ref";
/*      */   public static final String VALUE_REF_ATTRIBUTE = "value-ref";
/*      */   public static final String PROPS_ELEMENT = "props";
/*      */   public static final String PROP_ELEMENT = "prop";
/*      */   public static final String MERGE_ATTRIBUTE = "merge";
/*      */   public static final String QUALIFIER_ELEMENT = "qualifier";
/*      */   public static final String QUALIFIER_ATTRIBUTE_ELEMENT = "attribute";
/*      */   public static final String DEFAULT_LAZY_INIT_ATTRIBUTE = "default-lazy-init";
/*      */   public static final String DEFAULT_MERGE_ATTRIBUTE = "default-merge";
/*      */   public static final String DEFAULT_AUTOWIRE_ATTRIBUTE = "default-autowire";
/*      */   public static final String DEFAULT_DEPENDENCY_CHECK_ATTRIBUTE = "default-dependency-check";
/*      */   public static final String DEFAULT_AUTOWIRE_CANDIDATES_ATTRIBUTE = "default-autowire-candidates";
/*      */   public static final String DEFAULT_INIT_METHOD_ATTRIBUTE = "default-init-method";
/*      */   public static final String DEFAULT_DESTROY_METHOD_ATTRIBUTE = "default-destroy-method";
/*  240 */   protected final Log logger = LogFactory.getLog(getClass());
/*      */   private final XmlReaderContext readerContext;
/*  244 */   private final DocumentDefaultsDefinition defaults = new DocumentDefaultsDefinition();
/*      */ 
/*  246 */   private final ParseState parseState = new ParseState();
/*      */ 
/*  253 */   private final Set<String> usedNames = new HashSet();
/*      */ 
/*      */   public BeanDefinitionParserDelegate(XmlReaderContext readerContext)
/*      */   {
/*  261 */     Assert.notNull(readerContext, "XmlReaderContext must not be null");
/*  262 */     this.readerContext = readerContext;
/*      */   }
/*      */ 
/*      */   public final XmlReaderContext getReaderContext()
/*      */   {
/*  270 */     return this.readerContext;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public final Environment getEnvironment()
/*      */   {
/*  279 */     return this.readerContext.getEnvironment();
/*      */   }
/*      */ 
/*      */   protected Object extractSource(Element ele)
/*      */   {
/*  287 */     return this.readerContext.extractSource(ele);
/*      */   }
/*      */ 
/*      */   protected void error(String message, Node source)
/*      */   {
/*  294 */     this.readerContext.error(message, source, this.parseState.snapshot());
/*      */   }
/*      */ 
/*      */   protected void error(String message, Element source)
/*      */   {
/*  301 */     this.readerContext.error(message, source, this.parseState.snapshot());
/*      */   }
/*      */ 
/*      */   protected void error(String message, Element source, Throwable cause)
/*      */   {
/*  308 */     this.readerContext.error(message, source, this.parseState.snapshot(), cause);
/*      */   }
/*      */ 
/*      */   public void initDefaults(Element root)
/*      */   {
/*  316 */     initDefaults(root, null);
/*      */   }
/*      */ 
/*      */   public void initDefaults(Element root, BeanDefinitionParserDelegate parent)
/*      */   {
/*  328 */     populateDefaults(this.defaults, parent != null ? parent.defaults : null, root);
/*  329 */     this.readerContext.fireDefaultsRegistered(this.defaults);
/*      */   }
/*      */ 
/*      */   protected void populateDefaults(DocumentDefaultsDefinition defaults, DocumentDefaultsDefinition parentDefaults, Element root)
/*      */   {
/*  342 */     String lazyInit = root.getAttribute("default-lazy-init");
/*  343 */     if ("default".equals(lazyInit))
/*      */     {
/*  345 */       lazyInit = parentDefaults != null ? parentDefaults.getLazyInit() : "false";
/*      */     }
/*  347 */     defaults.setLazyInit(lazyInit);
/*      */ 
/*  349 */     String merge = root.getAttribute("default-merge");
/*  350 */     if ("default".equals(merge))
/*      */     {
/*  352 */       merge = parentDefaults != null ? parentDefaults.getMerge() : "false";
/*      */     }
/*  354 */     defaults.setMerge(merge);
/*      */ 
/*  356 */     String autowire = root.getAttribute("default-autowire");
/*  357 */     if ("default".equals(autowire))
/*      */     {
/*  359 */       autowire = parentDefaults != null ? parentDefaults.getAutowire() : "no";
/*      */     }
/*  361 */     defaults.setAutowire(autowire);
/*      */ 
/*  365 */     defaults.setDependencyCheck(root.getAttribute("default-dependency-check"));
/*      */ 
/*  367 */     if (root.hasAttribute("default-autowire-candidates")) {
/*  368 */       defaults.setAutowireCandidates(root.getAttribute("default-autowire-candidates"));
/*      */     }
/*  370 */     else if (parentDefaults != null) {
/*  371 */       defaults.setAutowireCandidates(parentDefaults.getAutowireCandidates());
/*      */     }
/*      */ 
/*  374 */     if (root.hasAttribute("default-init-method")) {
/*  375 */       defaults.setInitMethod(root.getAttribute("default-init-method"));
/*      */     }
/*  377 */     else if (parentDefaults != null) {
/*  378 */       defaults.setInitMethod(parentDefaults.getInitMethod());
/*      */     }
/*      */ 
/*  381 */     if (root.hasAttribute("default-destroy-method")) {
/*  382 */       defaults.setDestroyMethod(root.getAttribute("default-destroy-method"));
/*      */     }
/*  384 */     else if (parentDefaults != null) {
/*  385 */       defaults.setDestroyMethod(parentDefaults.getDestroyMethod());
/*      */     }
/*      */ 
/*  388 */     defaults.setSource(this.readerContext.extractSource(root));
/*      */   }
/*      */ 
/*      */   public DocumentDefaultsDefinition getDefaults()
/*      */   {
/*  396 */     return this.defaults;
/*      */   }
/*      */ 
/*      */   public BeanDefinitionDefaults getBeanDefinitionDefaults()
/*      */   {
/*  404 */     BeanDefinitionDefaults bdd = new BeanDefinitionDefaults();
/*  405 */     bdd.setLazyInit("TRUE".equalsIgnoreCase(this.defaults.getLazyInit()));
/*  406 */     bdd.setDependencyCheck(getDependencyCheck("default"));
/*  407 */     bdd.setAutowireMode(getAutowireMode("default"));
/*  408 */     bdd.setInitMethodName(this.defaults.getInitMethod());
/*  409 */     bdd.setDestroyMethodName(this.defaults.getDestroyMethod());
/*  410 */     return bdd;
/*      */   }
/*      */ 
/*      */   public String[] getAutowireCandidatePatterns()
/*      */   {
/*  418 */     String candidatePattern = this.defaults.getAutowireCandidates();
/*  419 */     return candidatePattern != null ? StringUtils.commaDelimitedListToStringArray(candidatePattern) : null;
/*      */   }
/*      */ 
/*      */   public BeanDefinitionHolder parseBeanDefinitionElement(Element ele)
/*      */   {
/*  429 */     return parseBeanDefinitionElement(ele, null);
/*      */   }
/*      */ 
/*      */   public BeanDefinitionHolder parseBeanDefinitionElement(Element ele, BeanDefinition containingBean)
/*      */   {
/*  438 */     String id = ele.getAttribute("id");
/*  439 */     String nameAttr = ele.getAttribute("name");
/*      */ 
/*  441 */     List aliases = new ArrayList();
/*  442 */     if (StringUtils.hasLength(nameAttr)) {
/*  443 */       String[] nameArr = StringUtils.tokenizeToStringArray(nameAttr, ",; ");
/*  444 */       aliases.addAll(Arrays.asList(nameArr));
/*      */     }
/*      */ 
/*  447 */     String beanName = id;
/*  448 */     if ((!StringUtils.hasText(beanName)) && (!aliases.isEmpty())) {
/*  449 */       beanName = (String)aliases.remove(0);
/*  450 */       if (this.logger.isDebugEnabled()) {
/*  451 */         this.logger.debug("No XML 'id' specified - using '" + beanName + "' as bean name and " + aliases + " as aliases");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  456 */     if (containingBean == null) {
/*  457 */       checkNameUniqueness(beanName, aliases, ele);
/*      */     }
/*      */ 
/*  460 */     AbstractBeanDefinition beanDefinition = parseBeanDefinitionElement(ele, beanName, containingBean);
/*  461 */     if (beanDefinition != null) {
/*  462 */       if (!StringUtils.hasText(beanName)) {
/*      */         try {
/*  464 */           if (containingBean != null) {
/*  465 */             beanName = BeanDefinitionReaderUtils.generateBeanName(beanDefinition, this.readerContext
/*  466 */               .getRegistry(), true);
/*      */           }
/*      */           else {
/*  469 */             beanName = this.readerContext.generateBeanName(beanDefinition);
/*      */ 
/*  473 */             String beanClassName = beanDefinition.getBeanClassName();
/*  474 */             if ((beanClassName != null) && 
/*  475 */               (beanName
/*  475 */               .startsWith(beanClassName)) && 
/*  475 */               (beanName.length() > beanClassName.length()) && 
/*  476 */               (!this.readerContext
/*  476 */               .getRegistry().isBeanNameInUse(beanClassName))) {
/*  477 */               aliases.add(beanClassName);
/*      */             }
/*      */           }
/*  480 */           if (this.logger.isDebugEnabled()) {
/*  481 */             this.logger.debug("Neither XML 'id' nor 'name' specified - using generated bean name [" + beanName + "]");
/*      */           }
/*      */         }
/*      */         catch (Exception ex)
/*      */         {
/*  486 */           error(ex.getMessage(), ele);
/*  487 */           return null;
/*      */         }
/*      */       }
/*  490 */       String[] aliasesArray = StringUtils.toStringArray(aliases);
/*  491 */       return new BeanDefinitionHolder(beanDefinition, beanName, aliasesArray);
/*      */     }
/*      */ 
/*  494 */     return null;
/*      */   }
/*      */ 
/*      */   protected void checkNameUniqueness(String beanName, List<String> aliases, Element beanElement)
/*      */   {
/*  502 */     String foundName = null;
/*      */ 
/*  504 */     if ((StringUtils.hasText(beanName)) && (this.usedNames.contains(beanName))) {
/*  505 */       foundName = beanName;
/*      */     }
/*  507 */     if (foundName == null) {
/*  508 */       foundName = (String)CollectionUtils.findFirstMatch(this.usedNames, aliases);
/*      */     }
/*  510 */     if (foundName != null) {
/*  511 */       error("Bean name '" + foundName + "' is already used in this <beans> element", beanElement);
/*      */     }
/*      */ 
/*  514 */     this.usedNames.add(beanName);
/*  515 */     this.usedNames.addAll(aliases);
/*      */   }
/*      */ 
/*      */   public AbstractBeanDefinition parseBeanDefinitionElement(Element ele, String beanName, BeanDefinition containingBean)
/*      */   {
/*  525 */     this.parseState.push(new BeanEntry(beanName));
/*      */ 
/*  527 */     String className = null;
/*  528 */     if (ele.hasAttribute("class")) {
/*  529 */       className = ele.getAttribute("class").trim();
/*      */     }
/*      */     try
/*      */     {
/*  533 */       String parent = null;
/*  534 */       if (ele.hasAttribute("parent")) {
/*  535 */         parent = ele.getAttribute("parent");
/*      */       }
/*  537 */       AbstractBeanDefinition bd = createBeanDefinition(className, parent);
/*      */ 
/*  539 */       parseBeanDefinitionAttributes(ele, beanName, containingBean, bd);
/*  540 */       bd.setDescription(DomUtils.getChildElementValueByTagName(ele, "description"));
/*      */ 
/*  542 */       parseMetaElements(ele, bd);
/*  543 */       parseLookupOverrideSubElements(ele, bd.getMethodOverrides());
/*  544 */       parseReplacedMethodSubElements(ele, bd.getMethodOverrides());
/*      */ 
/*  546 */       parseConstructorArgElements(ele, bd);
/*  547 */       parsePropertyElements(ele, bd);
/*  548 */       parseQualifierElements(ele, bd);
/*      */ 
/*  550 */       bd.setResource(this.readerContext.getResource());
/*  551 */       bd.setSource(extractSource(ele));
/*      */ 
/*  553 */       return bd;
/*      */     }
/*      */     catch (ClassNotFoundException ex) {
/*  556 */       error("Bean class [" + className + "] not found", ele, ex);
/*      */     }
/*      */     catch (NoClassDefFoundError err) {
/*  559 */       error("Class that bean class [" + className + "] depends on not found", ele, err);
/*      */     }
/*      */     catch (Throwable ex) {
/*  562 */       error("Unexpected failure during bean definition parsing", ele, ex);
/*      */     }
/*      */     finally {
/*  565 */       this.parseState.pop();
/*      */     }
/*      */ 
/*  568 */     return null;
/*      */   }
/*      */ 
/*      */   public AbstractBeanDefinition parseBeanDefinitionAttributes(Element ele, String beanName, BeanDefinition containingBean, AbstractBeanDefinition bd)
/*      */   {
/*  581 */     if (ele.hasAttribute("singleton")) {
/*  582 */       error("Old 1.x 'singleton' attribute in use - upgrade to 'scope' declaration", ele);
/*      */     }
/*  584 */     else if (ele.hasAttribute("scope")) {
/*  585 */       bd.setScope(ele.getAttribute("scope"));
/*      */     }
/*  587 */     else if (containingBean != null)
/*      */     {
/*  589 */       bd.setScope(containingBean.getScope());
/*      */     }
/*      */ 
/*  592 */     if (ele.hasAttribute("abstract")) {
/*  593 */       bd.setAbstract("true".equals(ele.getAttribute("abstract")));
/*      */     }
/*      */ 
/*  596 */     String lazyInit = ele.getAttribute("lazy-init");
/*  597 */     if ("default".equals(lazyInit)) {
/*  598 */       lazyInit = this.defaults.getLazyInit();
/*      */     }
/*  600 */     bd.setLazyInit("true".equals(lazyInit));
/*      */ 
/*  602 */     String autowire = ele.getAttribute("autowire");
/*  603 */     bd.setAutowireMode(getAutowireMode(autowire));
/*      */ 
/*  605 */     String dependencyCheck = ele.getAttribute("dependency-check");
/*  606 */     bd.setDependencyCheck(getDependencyCheck(dependencyCheck));
/*      */ 
/*  608 */     if (ele.hasAttribute("depends-on")) {
/*  609 */       String dependsOn = ele.getAttribute("depends-on");
/*  610 */       bd.setDependsOn(StringUtils.tokenizeToStringArray(dependsOn, ",; "));
/*      */     }
/*      */ 
/*  613 */     String autowireCandidate = ele.getAttribute("autowire-candidate");
/*  614 */     if (("".equals(autowireCandidate)) || ("default".equals(autowireCandidate))) {
/*  615 */       String candidatePattern = this.defaults.getAutowireCandidates();
/*  616 */       if (candidatePattern != null) {
/*  617 */         String[] patterns = StringUtils.commaDelimitedListToStringArray(candidatePattern);
/*  618 */         bd.setAutowireCandidate(PatternMatchUtils.simpleMatch(patterns, beanName));
/*      */       }
/*      */     }
/*      */     else {
/*  622 */       bd.setAutowireCandidate("true".equals(autowireCandidate));
/*      */     }
/*      */ 
/*  625 */     if (ele.hasAttribute("primary")) {
/*  626 */       bd.setPrimary("true".equals(ele.getAttribute("primary")));
/*      */     }
/*      */ 
/*  629 */     if (ele.hasAttribute("init-method")) {
/*  630 */       String initMethodName = ele.getAttribute("init-method");
/*  631 */       if (!"".equals(initMethodName)) {
/*  632 */         bd.setInitMethodName(initMethodName);
/*      */       }
/*      */ 
/*      */     }
/*  636 */     else if (this.defaults.getInitMethod() != null) {
/*  637 */       bd.setInitMethodName(this.defaults.getInitMethod());
/*  638 */       bd.setEnforceInitMethod(false);
/*      */     }
/*      */ 
/*  642 */     if (ele.hasAttribute("destroy-method")) {
/*  643 */       String destroyMethodName = ele.getAttribute("destroy-method");
/*  644 */       bd.setDestroyMethodName(destroyMethodName);
/*      */     }
/*  647 */     else if (this.defaults.getDestroyMethod() != null) {
/*  648 */       bd.setDestroyMethodName(this.defaults.getDestroyMethod());
/*  649 */       bd.setEnforceDestroyMethod(false);
/*      */     }
/*      */ 
/*  653 */     if (ele.hasAttribute("factory-method")) {
/*  654 */       bd.setFactoryMethodName(ele.getAttribute("factory-method"));
/*      */     }
/*  656 */     if (ele.hasAttribute("factory-bean")) {
/*  657 */       bd.setFactoryBeanName(ele.getAttribute("factory-bean"));
/*      */     }
/*      */ 
/*  660 */     return bd;
/*      */   }
/*      */ 
/*      */   protected AbstractBeanDefinition createBeanDefinition(String className, String parentName)
/*      */     throws ClassNotFoundException
/*      */   {
/*  673 */     return BeanDefinitionReaderUtils.createBeanDefinition(parentName, className, this.readerContext
/*  674 */       .getBeanClassLoader());
/*      */   }
/*      */ 
/*      */   public void parseMetaElements(Element ele, BeanMetadataAttributeAccessor attributeAccessor) {
/*  678 */     NodeList nl = ele.getChildNodes();
/*  679 */     for (int i = 0; i < nl.getLength(); i++) {
/*  680 */       Node node = nl.item(i);
/*  681 */       if ((isCandidateElement(node)) && (nodeNameEquals(node, "meta"))) {
/*  682 */         Element metaElement = (Element)node;
/*  683 */         String key = metaElement.getAttribute("key");
/*  684 */         String value = metaElement.getAttribute("value");
/*  685 */         BeanMetadataAttribute attribute = new BeanMetadataAttribute(key, value);
/*  686 */         attribute.setSource(extractSource(metaElement));
/*  687 */         attributeAccessor.addMetadataAttribute(attribute);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getAutowireMode(String attValue)
/*      */   {
/*  694 */     String att = attValue;
/*  695 */     if ("default".equals(att)) {
/*  696 */       att = this.defaults.getAutowire();
/*      */     }
/*  698 */     int autowire = 0;
/*  699 */     if ("byName".equals(att)) {
/*  700 */       autowire = 1;
/*      */     }
/*  702 */     else if ("byType".equals(att)) {
/*  703 */       autowire = 2;
/*      */     }
/*  705 */     else if ("constructor".equals(att)) {
/*  706 */       autowire = 3;
/*      */     }
/*  708 */     else if ("autodetect".equals(att)) {
/*  709 */       autowire = 4;
/*      */     }
/*      */ 
/*  712 */     return autowire;
/*      */   }
/*      */ 
/*      */   public int getDependencyCheck(String attValue) {
/*  716 */     String att = attValue;
/*  717 */     if ("default".equals(att)) {
/*  718 */       att = this.defaults.getDependencyCheck();
/*      */     }
/*  720 */     if ("all".equals(att)) {
/*  721 */       return 3;
/*      */     }
/*  723 */     if ("objects".equals(att)) {
/*  724 */       return 1;
/*      */     }
/*  726 */     if ("simple".equals(att)) {
/*  727 */       return 2;
/*      */     }
/*      */ 
/*  730 */     return 0;
/*      */   }
/*      */ 
/*      */   public void parseConstructorArgElements(Element beanEle, BeanDefinition bd)
/*      */   {
/*  738 */     NodeList nl = beanEle.getChildNodes();
/*  739 */     for (int i = 0; i < nl.getLength(); i++) {
/*  740 */       Node node = nl.item(i);
/*  741 */       if ((isCandidateElement(node)) && (nodeNameEquals(node, "constructor-arg")))
/*  742 */         parseConstructorArgElement((Element)node, bd);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void parsePropertyElements(Element beanEle, BeanDefinition bd)
/*      */   {
/*  751 */     NodeList nl = beanEle.getChildNodes();
/*  752 */     for (int i = 0; i < nl.getLength(); i++) {
/*  753 */       Node node = nl.item(i);
/*  754 */       if ((isCandidateElement(node)) && (nodeNameEquals(node, "property")))
/*  755 */         parsePropertyElement((Element)node, bd);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void parseQualifierElements(Element beanEle, AbstractBeanDefinition bd)
/*      */   {
/*  764 */     NodeList nl = beanEle.getChildNodes();
/*  765 */     for (int i = 0; i < nl.getLength(); i++) {
/*  766 */       Node node = nl.item(i);
/*  767 */       if ((isCandidateElement(node)) && (nodeNameEquals(node, "qualifier")))
/*  768 */         parseQualifierElement((Element)node, bd);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void parseLookupOverrideSubElements(Element beanEle, MethodOverrides overrides)
/*      */   {
/*  777 */     NodeList nl = beanEle.getChildNodes();
/*  778 */     for (int i = 0; i < nl.getLength(); i++) {
/*  779 */       Node node = nl.item(i);
/*  780 */       if ((isCandidateElement(node)) && (nodeNameEquals(node, "lookup-method"))) {
/*  781 */         Element ele = (Element)node;
/*  782 */         String methodName = ele.getAttribute("name");
/*  783 */         String beanRef = ele.getAttribute("bean");
/*  784 */         LookupOverride override = new LookupOverride(methodName, beanRef);
/*  785 */         override.setSource(extractSource(ele));
/*  786 */         overrides.addOverride(override);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void parseReplacedMethodSubElements(Element beanEle, MethodOverrides overrides)
/*      */   {
/*  795 */     NodeList nl = beanEle.getChildNodes();
/*  796 */     for (int i = 0; i < nl.getLength(); i++) {
/*  797 */       Node node = nl.item(i);
/*  798 */       if ((isCandidateElement(node)) && (nodeNameEquals(node, "replaced-method"))) {
/*  799 */         Element replacedMethodEle = (Element)node;
/*  800 */         String name = replacedMethodEle.getAttribute("name");
/*  801 */         String callback = replacedMethodEle.getAttribute("replacer");
/*  802 */         ReplaceOverride replaceOverride = new ReplaceOverride(name, callback);
/*      */ 
/*  804 */         List argTypeEles = DomUtils.getChildElementsByTagName(replacedMethodEle, "arg-type");
/*  805 */         for (Element argTypeEle : argTypeEles) {
/*  806 */           String match = argTypeEle.getAttribute("match");
/*  807 */           match = StringUtils.hasText(match) ? match : DomUtils.getTextValue(argTypeEle);
/*  808 */           if (StringUtils.hasText(match)) {
/*  809 */             replaceOverride.addTypeIdentifier(match);
/*      */           }
/*      */         }
/*  812 */         replaceOverride.setSource(extractSource(replacedMethodEle));
/*  813 */         overrides.addOverride(replaceOverride);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void parseConstructorArgElement(Element ele, BeanDefinition bd)
/*      */   {
/*  822 */     String indexAttr = ele.getAttribute("index");
/*  823 */     String typeAttr = ele.getAttribute("type");
/*  824 */     String nameAttr = ele.getAttribute("name");
/*  825 */     if (StringUtils.hasLength(indexAttr))
/*      */       try {
/*  827 */         int index = Integer.parseInt(indexAttr);
/*  828 */         if (index < 0)
/*  829 */           error("'index' cannot be lower than 0", ele);
/*      */         else
/*      */           try
/*      */           {
/*  833 */             this.parseState.push(new ConstructorArgumentEntry(index));
/*  834 */             Object value = parsePropertyValue(ele, bd, null);
/*  835 */             ConstructorArgumentValues.ValueHolder valueHolder = new ConstructorArgumentValues.ValueHolder(value);
/*  836 */             if (StringUtils.hasLength(typeAttr)) {
/*  837 */               valueHolder.setType(typeAttr);
/*      */             }
/*  839 */             if (StringUtils.hasLength(nameAttr)) {
/*  840 */               valueHolder.setName(nameAttr);
/*      */             }
/*  842 */             valueHolder.setSource(extractSource(ele));
/*  843 */             if (bd.getConstructorArgumentValues().hasIndexedArgumentValue(index)) {
/*  844 */               error("Ambiguous constructor-arg entries for index " + index, ele);
/*      */             }
/*      */             else
/*  847 */               bd.getConstructorArgumentValues().addIndexedArgumentValue(index, valueHolder);
/*      */           }
/*      */           finally
/*      */           {
/*  851 */             this.parseState.pop();
/*      */           }
/*      */       }
/*      */       catch (NumberFormatException ex)
/*      */       {
/*  856 */         error("Attribute 'index' of tag 'constructor-arg' must be an integer", ele);
/*      */       }
/*      */     else
/*      */       try
/*      */       {
/*  861 */         this.parseState.push(new ConstructorArgumentEntry());
/*  862 */         Object value = parsePropertyValue(ele, bd, null);
/*  863 */         ConstructorArgumentValues.ValueHolder valueHolder = new ConstructorArgumentValues.ValueHolder(value);
/*  864 */         if (StringUtils.hasLength(typeAttr)) {
/*  865 */           valueHolder.setType(typeAttr);
/*      */         }
/*  867 */         if (StringUtils.hasLength(nameAttr)) {
/*  868 */           valueHolder.setName(nameAttr);
/*      */         }
/*  870 */         valueHolder.setSource(extractSource(ele));
/*  871 */         bd.getConstructorArgumentValues().addGenericArgumentValue(valueHolder);
/*      */       }
/*      */       finally {
/*  874 */         this.parseState.pop();
/*      */       }
/*      */   }
/*      */ 
/*      */   public void parsePropertyElement(Element ele, BeanDefinition bd)
/*      */   {
/*  883 */     String propertyName = ele.getAttribute("name");
/*  884 */     if (!StringUtils.hasLength(propertyName)) {
/*  885 */       error("Tag 'property' must have a 'name' attribute", ele);
/*  886 */       return;
/*      */     }
/*  888 */     this.parseState.push(new PropertyEntry(propertyName));
/*      */     try {
/*  890 */       if (bd.getPropertyValues().contains(propertyName)) {
/*  891 */         error("Multiple 'property' definitions for property '" + propertyName + "'", ele);
/*  892 */         return;
/*      */       }
/*  894 */       Object val = parsePropertyValue(ele, bd, propertyName);
/*  895 */       PropertyValue pv = new PropertyValue(propertyName, val);
/*  896 */       parseMetaElements(ele, pv);
/*  897 */       pv.setSource(extractSource(ele));
/*  898 */       bd.getPropertyValues().addPropertyValue(pv);
/*      */     }
/*      */     finally {
/*  901 */       this.parseState.pop();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void parseQualifierElement(Element ele, AbstractBeanDefinition bd)
/*      */   {
/*  909 */     String typeName = ele.getAttribute("type");
/*  910 */     if (!StringUtils.hasLength(typeName)) {
/*  911 */       error("Tag 'qualifier' must have a 'type' attribute", ele);
/*  912 */       return;
/*      */     }
/*  914 */     this.parseState.push(new QualifierEntry(typeName));
/*      */     try {
/*  916 */       AutowireCandidateQualifier qualifier = new AutowireCandidateQualifier(typeName);
/*  917 */       qualifier.setSource(extractSource(ele));
/*  918 */       String value = ele.getAttribute("value");
/*  919 */       if (StringUtils.hasLength(value)) {
/*  920 */         qualifier.setAttribute(AutowireCandidateQualifier.VALUE_KEY, value);
/*      */       }
/*  922 */       NodeList nl = ele.getChildNodes();
/*  923 */       for (int i = 0; i < nl.getLength(); i++) {
/*  924 */         Node node = nl.item(i);
/*  925 */         if ((isCandidateElement(node)) && (nodeNameEquals(node, "attribute"))) {
/*  926 */           Element attributeEle = (Element)node;
/*  927 */           String attributeName = attributeEle.getAttribute("key");
/*  928 */           String attributeValue = attributeEle.getAttribute("value");
/*  929 */           if ((StringUtils.hasLength(attributeName)) && (StringUtils.hasLength(attributeValue))) {
/*  930 */             BeanMetadataAttribute attribute = new BeanMetadataAttribute(attributeName, attributeValue);
/*  931 */             attribute.setSource(extractSource(attributeEle));
/*  932 */             qualifier.addMetadataAttribute(attribute);
/*      */           }
/*      */           else {
/*  935 */             error("Qualifier 'attribute' tag must have a 'name' and 'value'", attributeEle);
/*  936 */             return;
/*      */           }
/*      */         }
/*      */       }
/*  940 */       bd.addQualifier(qualifier);
/*      */     }
/*      */     finally {
/*  943 */       this.parseState.pop();
/*      */     }
/*      */   }
/*      */ 
/*      */   public Object parsePropertyValue(Element ele, BeanDefinition bd, String propertyName)
/*      */   {
/*  952 */     String elementName = propertyName != null ? "<property> element for property '" + propertyName + "'" : "<constructor-arg> element";
/*      */ 
/*  957 */     NodeList nl = ele.getChildNodes();
/*  958 */     Element subElement = null;
/*  959 */     for (int i = 0; i < nl.getLength(); i++) {
/*  960 */       Node node = nl.item(i);
/*  961 */       if (((node instanceof Element)) && (!nodeNameEquals(node, "description")) && 
/*  962 */         (!nodeNameEquals(node, "meta")))
/*      */       {
/*  964 */         if (subElement != null) {
/*  965 */           error(elementName + " must not contain more than one sub-element", ele);
/*      */         }
/*      */         else {
/*  968 */           subElement = (Element)node;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  973 */     boolean hasRefAttribute = ele.hasAttribute("ref");
/*  974 */     boolean hasValueAttribute = ele.hasAttribute("value");
/*  975 */     if (((hasRefAttribute) && (hasValueAttribute)) || (((hasRefAttribute) || (hasValueAttribute)) && (subElement != null)))
/*      */     {
/*  977 */       error(elementName + " is only allowed to contain either 'ref' attribute OR 'value' attribute OR sub-element", ele);
/*      */     }
/*      */ 
/*  981 */     if (hasRefAttribute) {
/*  982 */       String refName = ele.getAttribute("ref");
/*  983 */       if (!StringUtils.hasText(refName)) {
/*  984 */         error(elementName + " contains empty 'ref' attribute", ele);
/*      */       }
/*  986 */       RuntimeBeanReference ref = new RuntimeBeanReference(refName);
/*  987 */       ref.setSource(extractSource(ele));
/*  988 */       return ref;
/*      */     }
/*  990 */     if (hasValueAttribute) {
/*  991 */       TypedStringValue valueHolder = new TypedStringValue(ele.getAttribute("value"));
/*  992 */       valueHolder.setSource(extractSource(ele));
/*  993 */       return valueHolder;
/*      */     }
/*  995 */     if (subElement != null) {
/*  996 */       return parsePropertySubElement(subElement, bd);
/*      */     }
/*      */ 
/* 1000 */     error(elementName + " must specify a ref or value", ele);
/* 1001 */     return null;
/*      */   }
/*      */ 
/*      */   public Object parsePropertySubElement(Element ele, BeanDefinition bd)
/*      */   {
/* 1006 */     return parsePropertySubElement(ele, bd, null);
/*      */   }
/*      */ 
/*      */   public Object parsePropertySubElement(Element ele, BeanDefinition bd, String defaultValueType)
/*      */   {
/* 1017 */     if (!isDefaultNamespace(ele)) {
/* 1018 */       return parseNestedCustomElement(ele, bd);
/*      */     }
/* 1020 */     if (nodeNameEquals(ele, "bean")) {
/* 1021 */       BeanDefinitionHolder nestedBd = parseBeanDefinitionElement(ele, bd);
/* 1022 */       if (nestedBd != null) {
/* 1023 */         nestedBd = decorateBeanDefinitionIfRequired(ele, nestedBd, bd);
/*      */       }
/* 1025 */       return nestedBd;
/*      */     }
/* 1027 */     if (nodeNameEquals(ele, "ref"))
/*      */     {
/* 1029 */       String refName = ele.getAttribute("bean");
/* 1030 */       boolean toParent = false;
/* 1031 */       if (!StringUtils.hasLength(refName))
/*      */       {
/* 1033 */         refName = ele.getAttribute("local");
/* 1034 */         if (!StringUtils.hasLength(refName))
/*      */         {
/* 1036 */           refName = ele.getAttribute("parent");
/* 1037 */           toParent = true;
/* 1038 */           if (!StringUtils.hasLength(refName)) {
/* 1039 */             error("'bean', 'local' or 'parent' is required for <ref> element", ele);
/* 1040 */             return null;
/*      */           }
/*      */         }
/*      */       }
/* 1044 */       if (!StringUtils.hasText(refName)) {
/* 1045 */         error("<ref> element contains empty target attribute", ele);
/* 1046 */         return null;
/*      */       }
/* 1048 */       RuntimeBeanReference ref = new RuntimeBeanReference(refName, toParent);
/* 1049 */       ref.setSource(extractSource(ele));
/* 1050 */       return ref;
/*      */     }
/* 1052 */     if (nodeNameEquals(ele, "idref")) {
/* 1053 */       return parseIdRefElement(ele);
/*      */     }
/* 1055 */     if (nodeNameEquals(ele, "value")) {
/* 1056 */       return parseValueElement(ele, defaultValueType);
/*      */     }
/* 1058 */     if (nodeNameEquals(ele, "null"))
/*      */     {
/* 1061 */       TypedStringValue nullHolder = new TypedStringValue(null);
/* 1062 */       nullHolder.setSource(extractSource(ele));
/* 1063 */       return nullHolder;
/*      */     }
/* 1065 */     if (nodeNameEquals(ele, "array")) {
/* 1066 */       return parseArrayElement(ele, bd);
/*      */     }
/* 1068 */     if (nodeNameEquals(ele, "list")) {
/* 1069 */       return parseListElement(ele, bd);
/*      */     }
/* 1071 */     if (nodeNameEquals(ele, "set")) {
/* 1072 */       return parseSetElement(ele, bd);
/*      */     }
/* 1074 */     if (nodeNameEquals(ele, "map")) {
/* 1075 */       return parseMapElement(ele, bd);
/*      */     }
/* 1077 */     if (nodeNameEquals(ele, "props")) {
/* 1078 */       return parsePropsElement(ele);
/*      */     }
/*      */ 
/* 1081 */     error("Unknown property sub-element: [" + ele.getNodeName() + "]", ele);
/* 1082 */     return null;
/*      */   }
/*      */ 
/*      */   public Object parseIdRefElement(Element ele)
/*      */   {
/* 1091 */     String refName = ele.getAttribute("bean");
/* 1092 */     if (!StringUtils.hasLength(refName))
/*      */     {
/* 1094 */       refName = ele.getAttribute("local");
/* 1095 */       if (!StringUtils.hasLength(refName)) {
/* 1096 */         error("Either 'bean' or 'local' is required for <idref> element", ele);
/* 1097 */         return null;
/*      */       }
/*      */     }
/* 1100 */     if (!StringUtils.hasText(refName)) {
/* 1101 */       error("<idref> element contains empty target attribute", ele);
/* 1102 */       return null;
/*      */     }
/* 1104 */     RuntimeBeanNameReference ref = new RuntimeBeanNameReference(refName);
/* 1105 */     ref.setSource(extractSource(ele));
/* 1106 */     return ref;
/*      */   }
/*      */ 
/*      */   public Object parseValueElement(Element ele, String defaultTypeName)
/*      */   {
/* 1114 */     String value = DomUtils.getTextValue(ele);
/* 1115 */     String specifiedTypeName = ele.getAttribute("type");
/* 1116 */     String typeName = specifiedTypeName;
/* 1117 */     if (!StringUtils.hasText(typeName))
/* 1118 */       typeName = defaultTypeName;
/*      */     try
/*      */     {
/* 1121 */       TypedStringValue typedValue = buildTypedStringValue(value, typeName);
/* 1122 */       typedValue.setSource(extractSource(ele));
/* 1123 */       typedValue.setSpecifiedTypeName(specifiedTypeName);
/* 1124 */       return typedValue;
/*      */     }
/*      */     catch (ClassNotFoundException ex) {
/* 1127 */       error("Type class [" + typeName + "] not found for <value> element", ele, ex);
/* 1128 */     }return value;
/*      */   }
/*      */ 
/*      */   protected TypedStringValue buildTypedStringValue(String value, String targetTypeName)
/*      */     throws ClassNotFoundException
/*      */   {
/* 1139 */     ClassLoader classLoader = this.readerContext.getBeanClassLoader();
/*      */     TypedStringValue typedValue;
/*      */     TypedStringValue typedValue;
/* 1141 */     if (!StringUtils.hasText(targetTypeName)) {
/* 1142 */       typedValue = new TypedStringValue(value);
/*      */     }
/*      */     else
/*      */     {
/*      */       TypedStringValue typedValue;
/* 1144 */       if (classLoader != null) {
/* 1145 */         Class targetType = ClassUtils.forName(targetTypeName, classLoader);
/* 1146 */         typedValue = new TypedStringValue(value, targetType);
/*      */       }
/*      */       else {
/* 1149 */         typedValue = new TypedStringValue(value, targetTypeName);
/*      */       }
/*      */     }
/* 1151 */     return typedValue;
/*      */   }
/*      */ 
/*      */   public Object parseArrayElement(Element arrayEle, BeanDefinition bd)
/*      */   {
/* 1158 */     String elementType = arrayEle.getAttribute("value-type");
/* 1159 */     NodeList nl = arrayEle.getChildNodes();
/* 1160 */     ManagedArray target = new ManagedArray(elementType, nl.getLength());
/* 1161 */     target.setSource(extractSource(arrayEle));
/* 1162 */     target.setElementTypeName(elementType);
/* 1163 */     target.setMergeEnabled(parseMergeAttribute(arrayEle));
/* 1164 */     parseCollectionElements(nl, target, bd, elementType);
/* 1165 */     return target;
/*      */   }
/*      */ 
/*      */   public List<Object> parseListElement(Element collectionEle, BeanDefinition bd)
/*      */   {
/* 1172 */     String defaultElementType = collectionEle.getAttribute("value-type");
/* 1173 */     NodeList nl = collectionEle.getChildNodes();
/* 1174 */     ManagedList target = new ManagedList(nl.getLength());
/* 1175 */     target.setSource(extractSource(collectionEle));
/* 1176 */     target.setElementTypeName(defaultElementType);
/* 1177 */     target.setMergeEnabled(parseMergeAttribute(collectionEle));
/* 1178 */     parseCollectionElements(nl, target, bd, defaultElementType);
/* 1179 */     return target;
/*      */   }
/*      */ 
/*      */   public Set<Object> parseSetElement(Element collectionEle, BeanDefinition bd)
/*      */   {
/* 1186 */     String defaultElementType = collectionEle.getAttribute("value-type");
/* 1187 */     NodeList nl = collectionEle.getChildNodes();
/* 1188 */     ManagedSet target = new ManagedSet(nl.getLength());
/* 1189 */     target.setSource(extractSource(collectionEle));
/* 1190 */     target.setElementTypeName(defaultElementType);
/* 1191 */     target.setMergeEnabled(parseMergeAttribute(collectionEle));
/* 1192 */     parseCollectionElements(nl, target, bd, defaultElementType);
/* 1193 */     return target;
/*      */   }
/*      */ 
/*      */   protected void parseCollectionElements(NodeList elementNodes, Collection<Object> target, BeanDefinition bd, String defaultElementType)
/*      */   {
/* 1199 */     for (int i = 0; i < elementNodes.getLength(); i++) {
/* 1200 */       Node node = elementNodes.item(i);
/* 1201 */       if (((node instanceof Element)) && (!nodeNameEquals(node, "description")))
/* 1202 */         target.add(parsePropertySubElement((Element)node, bd, defaultElementType));
/*      */     }
/*      */   }
/*      */ 
/*      */   public Map<Object, Object> parseMapElement(Element mapEle, BeanDefinition bd)
/*      */   {
/* 1211 */     String defaultKeyType = mapEle.getAttribute("key-type");
/* 1212 */     String defaultValueType = mapEle.getAttribute("value-type");
/*      */ 
/* 1214 */     List entryEles = DomUtils.getChildElementsByTagName(mapEle, "entry");
/* 1215 */     ManagedMap map = new ManagedMap(entryEles.size());
/* 1216 */     map.setSource(extractSource(mapEle));
/* 1217 */     map.setKeyTypeName(defaultKeyType);
/* 1218 */     map.setValueTypeName(defaultValueType);
/* 1219 */     map.setMergeEnabled(parseMergeAttribute(mapEle));
/*      */ 
/* 1221 */     for (Element entryEle : entryEles)
/*      */     {
/* 1224 */       NodeList entrySubNodes = entryEle.getChildNodes();
/* 1225 */       Element keyEle = null;
/* 1226 */       Element valueEle = null;
/* 1227 */       for (int j = 0; j < entrySubNodes.getLength(); j++) {
/* 1228 */         Node node = entrySubNodes.item(j);
/* 1229 */         if ((node instanceof Element)) {
/* 1230 */           Element candidateEle = (Element)node;
/* 1231 */           if (nodeNameEquals(candidateEle, "key")) {
/* 1232 */             if (keyEle != null) {
/* 1233 */               error("<entry> element is only allowed to contain one <key> sub-element", entryEle);
/*      */             }
/*      */             else {
/* 1236 */               keyEle = candidateEle;
/*      */             }
/*      */ 
/*      */           }
/* 1241 */           else if (!nodeNameEquals(candidateEle, "description"))
/*      */           {
/* 1244 */             if (valueEle != null) {
/* 1245 */               error("<entry> element must not contain more than one value sub-element", entryEle);
/*      */             }
/*      */             else {
/* 1248 */               valueEle = candidateEle;
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1255 */       Object key = null;
/* 1256 */       boolean hasKeyAttribute = entryEle.hasAttribute("key");
/* 1257 */       boolean hasKeyRefAttribute = entryEle.hasAttribute("key-ref");
/* 1258 */       if (((hasKeyAttribute) && (hasKeyRefAttribute)) || (((hasKeyAttribute) || (hasKeyRefAttribute)) && (keyEle != null)))
/*      */       {
/* 1260 */         error("<entry> element is only allowed to contain either a 'key' attribute OR a 'key-ref' attribute OR a <key> sub-element", entryEle);
/*      */       }
/*      */ 
/* 1263 */       if (hasKeyAttribute) {
/* 1264 */         key = buildTypedStringValueForMap(entryEle.getAttribute("key"), defaultKeyType, entryEle);
/*      */       }
/* 1266 */       else if (hasKeyRefAttribute) {
/* 1267 */         String refName = entryEle.getAttribute("key-ref");
/* 1268 */         if (!StringUtils.hasText(refName)) {
/* 1269 */           error("<entry> element contains empty 'key-ref' attribute", entryEle);
/*      */         }
/* 1271 */         RuntimeBeanReference ref = new RuntimeBeanReference(refName);
/* 1272 */         ref.setSource(extractSource(entryEle));
/* 1273 */         key = ref;
/*      */       }
/* 1275 */       else if (keyEle != null) {
/* 1276 */         key = parseKeyElement(keyEle, bd, defaultKeyType);
/*      */       }
/*      */       else {
/* 1279 */         error("<entry> element must specify a key", entryEle);
/*      */       }
/*      */ 
/* 1283 */       Object value = null;
/* 1284 */       boolean hasValueAttribute = entryEle.hasAttribute("value");
/* 1285 */       boolean hasValueRefAttribute = entryEle.hasAttribute("value-ref");
/* 1286 */       boolean hasValueTypeAttribute = entryEle.hasAttribute("value-type");
/* 1287 */       if (((hasValueAttribute) && (hasValueRefAttribute)) || (((hasValueAttribute) || (hasValueRefAttribute)) && (valueEle != null)))
/*      */       {
/* 1289 */         error("<entry> element is only allowed to contain either 'value' attribute OR 'value-ref' attribute OR <value> sub-element", entryEle);
/*      */       }
/*      */ 
/* 1292 */       if (((hasValueTypeAttribute) && (hasValueRefAttribute)) || ((hasValueTypeAttribute) && (!hasValueAttribute)) || ((hasValueTypeAttribute) && (valueEle != null)))
/*      */       {
/* 1295 */         error("<entry> element is only allowed to contain a 'value-type' attribute when it has a 'value' attribute", entryEle);
/*      */       }
/*      */ 
/* 1298 */       if (hasValueAttribute) {
/* 1299 */         String valueType = entryEle.getAttribute("value-type");
/* 1300 */         if (!StringUtils.hasText(valueType)) {
/* 1301 */           valueType = defaultValueType;
/*      */         }
/* 1303 */         value = buildTypedStringValueForMap(entryEle.getAttribute("value"), valueType, entryEle);
/*      */       }
/* 1305 */       else if (hasValueRefAttribute) {
/* 1306 */         String refName = entryEle.getAttribute("value-ref");
/* 1307 */         if (!StringUtils.hasText(refName)) {
/* 1308 */           error("<entry> element contains empty 'value-ref' attribute", entryEle);
/*      */         }
/* 1310 */         RuntimeBeanReference ref = new RuntimeBeanReference(refName);
/* 1311 */         ref.setSource(extractSource(entryEle));
/* 1312 */         value = ref;
/*      */       }
/* 1314 */       else if (valueEle != null) {
/* 1315 */         value = parsePropertySubElement(valueEle, bd, defaultValueType);
/*      */       }
/*      */       else {
/* 1318 */         error("<entry> element must specify a value", entryEle);
/*      */       }
/*      */ 
/* 1322 */       map.put(key, value);
/*      */     }
/*      */ 
/* 1325 */     return map;
/*      */   }
/*      */ 
/*      */   protected final Object buildTypedStringValueForMap(String value, String defaultTypeName, Element entryEle)
/*      */   {
/*      */     try
/*      */     {
/* 1334 */       TypedStringValue typedValue = buildTypedStringValue(value, defaultTypeName);
/* 1335 */       typedValue.setSource(extractSource(entryEle));
/* 1336 */       return typedValue;
/*      */     }
/*      */     catch (ClassNotFoundException ex) {
/* 1339 */       error("Type class [" + defaultTypeName + "] not found for Map key/value type", entryEle, ex);
/* 1340 */     }return value;
/*      */   }
/*      */ 
/*      */   protected Object parseKeyElement(Element keyEle, BeanDefinition bd, String defaultKeyTypeName)
/*      */   {
/* 1348 */     NodeList nl = keyEle.getChildNodes();
/* 1349 */     Element subElement = null;
/* 1350 */     for (int i = 0; i < nl.getLength(); i++) {
/* 1351 */       Node node = nl.item(i);
/* 1352 */       if ((node instanceof Element))
/*      */       {
/* 1354 */         if (subElement != null) {
/* 1355 */           error("<key> element must not contain more than one value sub-element", keyEle);
/*      */         }
/*      */         else {
/* 1358 */           subElement = (Element)node;
/*      */         }
/*      */       }
/*      */     }
/* 1362 */     return parsePropertySubElement(subElement, bd, defaultKeyTypeName);
/*      */   }
/*      */ 
/*      */   public Properties parsePropsElement(Element propsEle)
/*      */   {
/* 1369 */     ManagedProperties props = new ManagedProperties();
/* 1370 */     props.setSource(extractSource(propsEle));
/* 1371 */     props.setMergeEnabled(parseMergeAttribute(propsEle));
/*      */ 
/* 1373 */     List propEles = DomUtils.getChildElementsByTagName(propsEle, "prop");
/* 1374 */     for (Element propEle : propEles) {
/* 1375 */       String key = propEle.getAttribute("key");
/*      */ 
/* 1378 */       String value = DomUtils.getTextValue(propEle).trim();
/* 1379 */       TypedStringValue keyHolder = new TypedStringValue(key);
/* 1380 */       keyHolder.setSource(extractSource(propEle));
/* 1381 */       TypedStringValue valueHolder = new TypedStringValue(value);
/* 1382 */       valueHolder.setSource(extractSource(propEle));
/* 1383 */       props.put(keyHolder, valueHolder);
/*      */     }
/*      */ 
/* 1386 */     return props;
/*      */   }
/*      */ 
/*      */   public boolean parseMergeAttribute(Element collectionElement)
/*      */   {
/* 1393 */     String value = collectionElement.getAttribute("merge");
/* 1394 */     if ("default".equals(value)) {
/* 1395 */       value = this.defaults.getMerge();
/*      */     }
/* 1397 */     return "true".equals(value);
/*      */   }
/*      */ 
/*      */   public BeanDefinition parseCustomElement(Element ele) {
/* 1401 */     return parseCustomElement(ele, null);
/*      */   }
/*      */ 
/*      */   public BeanDefinition parseCustomElement(Element ele, BeanDefinition containingBd) {
/* 1405 */     String namespaceUri = getNamespaceURI(ele);
/* 1406 */     NamespaceHandler handler = this.readerContext.getNamespaceHandlerResolver().resolve(namespaceUri);
/* 1407 */     if (handler == null) {
/* 1408 */       error("Unable to locate Spring NamespaceHandler for XML schema namespace [" + namespaceUri + "]", ele);
/* 1409 */       return null;
/*      */     }
/* 1411 */     return handler.parse(ele, new ParserContext(this.readerContext, this, containingBd));
/*      */   }
/*      */ 
/*      */   public BeanDefinitionHolder decorateBeanDefinitionIfRequired(Element ele, BeanDefinitionHolder definitionHolder) {
/* 1415 */     return decorateBeanDefinitionIfRequired(ele, definitionHolder, null);
/*      */   }
/*      */ 
/*      */   public BeanDefinitionHolder decorateBeanDefinitionIfRequired(Element ele, BeanDefinitionHolder definitionHolder, BeanDefinition containingBd)
/*      */   {
/* 1421 */     BeanDefinitionHolder finalDefinition = definitionHolder;
/*      */ 
/* 1424 */     NamedNodeMap attributes = ele.getAttributes();
/* 1425 */     for (int i = 0; i < attributes.getLength(); i++) {
/* 1426 */       Node node = attributes.item(i);
/* 1427 */       finalDefinition = decorateIfRequired(node, finalDefinition, containingBd);
/*      */     }
/*      */ 
/* 1431 */     NodeList children = ele.getChildNodes();
/* 1432 */     for (int i = 0; i < children.getLength(); i++) {
/* 1433 */       Node node = children.item(i);
/* 1434 */       if (node.getNodeType() == 1) {
/* 1435 */         finalDefinition = decorateIfRequired(node, finalDefinition, containingBd);
/*      */       }
/*      */     }
/* 1438 */     return finalDefinition;
/*      */   }
/*      */ 
/*      */   public BeanDefinitionHolder decorateIfRequired(Node node, BeanDefinitionHolder originalDef, BeanDefinition containingBd)
/*      */   {
/* 1444 */     String namespaceUri = getNamespaceURI(node);
/* 1445 */     if (!isDefaultNamespace(namespaceUri)) {
/* 1446 */       NamespaceHandler handler = this.readerContext.getNamespaceHandlerResolver().resolve(namespaceUri);
/* 1447 */       if (handler != null) {
/* 1448 */         return handler.decorate(node, originalDef, new ParserContext(this.readerContext, this, containingBd));
/*      */       }
/* 1450 */       if ((namespaceUri != null) && (namespaceUri.startsWith("http://www.springframework.org/"))) {
/* 1451 */         error("Unable to locate Spring NamespaceHandler for XML schema namespace [" + namespaceUri + "]", node);
/*      */       }
/* 1455 */       else if (this.logger.isDebugEnabled()) {
/* 1456 */         this.logger.debug("No Spring NamespaceHandler found for XML schema namespace [" + namespaceUri + "]");
/*      */       }
/*      */     }
/*      */ 
/* 1460 */     return originalDef;
/*      */   }
/*      */ 
/*      */   private BeanDefinitionHolder parseNestedCustomElement(Element ele, BeanDefinition containingBd) {
/* 1464 */     BeanDefinition innerDefinition = parseCustomElement(ele, containingBd);
/* 1465 */     if (innerDefinition == null) {
/* 1466 */       error("Incorrect usage of element '" + ele.getNodeName() + "' in a nested manner. " + "This tag cannot be used nested inside <property>.", ele);
/*      */ 
/* 1468 */       return null;
/*      */     }
/*      */ 
/* 1471 */     String id = ele.getNodeName() + "#" + 
/* 1471 */       ObjectUtils.getIdentityHexString(innerDefinition);
/*      */ 
/* 1472 */     if (this.logger.isDebugEnabled()) {
/* 1473 */       this.logger.debug("Using generated bean name [" + id + "] for nested custom element '" + ele
/* 1474 */         .getNodeName() + "'");
/*      */     }
/* 1476 */     return new BeanDefinitionHolder(innerDefinition, id);
/*      */   }
/*      */ 
/*      */   public String getNamespaceURI(Node node)
/*      */   {
/* 1486 */     return node.getNamespaceURI();
/*      */   }
/*      */ 
/*      */   public String getLocalName(Node node)
/*      */   {
/* 1495 */     return node.getLocalName();
/*      */   }
/*      */ 
/*      */   public boolean nodeNameEquals(Node node, String desiredName)
/*      */   {
/* 1508 */     return (desiredName.equals(node.getNodeName())) || (desiredName.equals(getLocalName(node)));
/*      */   }
/*      */ 
/*      */   public boolean isDefaultNamespace(String namespaceUri) {
/* 1512 */     return (!StringUtils.hasLength(namespaceUri)) || ("http://www.springframework.org/schema/beans".equals(namespaceUri));
/*      */   }
/*      */ 
/*      */   public boolean isDefaultNamespace(Node node) {
/* 1516 */     return isDefaultNamespace(getNamespaceURI(node));
/*      */   }
/*      */ 
/*      */   private boolean isCandidateElement(Node node) {
/* 1520 */     return ((node instanceof Element)) && ((isDefaultNamespace(node)) || (!isDefaultNamespace(node.getParentNode())));
/*      */   }
/*      */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.xml.BeanDefinitionParserDelegate
 * JD-Core Version:    0.6.2
 */