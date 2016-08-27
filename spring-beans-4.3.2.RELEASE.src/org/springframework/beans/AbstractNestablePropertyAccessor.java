/*      */ package org.springframework.beans;
/*      */ 
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.UndeclaredThrowableException;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.apache.commons.logging.LogFactory;
/*      */ import org.springframework.core.CollectionFactory;
/*      */ import org.springframework.core.ResolvableType;
/*      */ import org.springframework.core.convert.ConversionException;
/*      */ import org.springframework.core.convert.ConverterNotFoundException;
/*      */ import org.springframework.core.convert.TypeDescriptor;
/*      */ import org.springframework.lang.UsesJava8;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.ObjectUtils;
/*      */ import org.springframework.util.StringUtils;
/*      */ 
/*      */ public abstract class AbstractNestablePropertyAccessor extends AbstractPropertyAccessor
/*      */ {
/*   76 */   private static final Log logger = LogFactory.getLog(AbstractNestablePropertyAccessor.class);
/*      */ 
/*   78 */   private static Class<?> javaUtilOptionalClass = null;
/*      */ 
/*   91 */   private int autoGrowCollectionLimit = 2147483647;
/*      */   Object wrappedObject;
/*   95 */   private String nestedPath = "";
/*      */   Object rootObject;
/*      */   private Map<String, AbstractNestablePropertyAccessor> nestedPropertyAccessors;
/*      */ 
/*      */   protected AbstractNestablePropertyAccessor()
/*      */   {
/*  111 */     this(true);
/*      */   }
/*      */ 
/*      */   protected AbstractNestablePropertyAccessor(boolean registerDefaultEditors)
/*      */   {
/*  121 */     if (registerDefaultEditors) {
/*  122 */       registerDefaultEditors();
/*      */     }
/*  124 */     this.typeConverterDelegate = new TypeConverterDelegate(this);
/*      */   }
/*      */ 
/*      */   protected AbstractNestablePropertyAccessor(Object object)
/*      */   {
/*  132 */     registerDefaultEditors();
/*  133 */     setWrappedInstance(object);
/*      */   }
/*      */ 
/*      */   protected AbstractNestablePropertyAccessor(Class<?> clazz)
/*      */   {
/*  141 */     registerDefaultEditors();
/*  142 */     setWrappedInstance(BeanUtils.instantiateClass(clazz));
/*      */   }
/*      */ 
/*      */   protected AbstractNestablePropertyAccessor(Object object, String nestedPath, Object rootObject)
/*      */   {
/*  153 */     registerDefaultEditors();
/*  154 */     setWrappedInstance(object, nestedPath, rootObject);
/*      */   }
/*      */ 
/*      */   protected AbstractNestablePropertyAccessor(Object object, String nestedPath, AbstractNestablePropertyAccessor parent)
/*      */   {
/*  165 */     setWrappedInstance(object, nestedPath, parent.getWrappedInstance());
/*  166 */     setExtractOldValueForEditor(parent.isExtractOldValueForEditor());
/*  167 */     setAutoGrowNestedPaths(parent.isAutoGrowNestedPaths());
/*  168 */     setAutoGrowCollectionLimit(parent.getAutoGrowCollectionLimit());
/*  169 */     setConversionService(parent.getConversionService());
/*      */   }
/*      */ 
/*      */   public void setAutoGrowCollectionLimit(int autoGrowCollectionLimit)
/*      */   {
/*  178 */     this.autoGrowCollectionLimit = autoGrowCollectionLimit;
/*      */   }
/*      */ 
/*      */   public int getAutoGrowCollectionLimit()
/*      */   {
/*  185 */     return this.autoGrowCollectionLimit;
/*      */   }
/*      */ 
/*      */   public void setWrappedInstance(Object object)
/*      */   {
/*  194 */     setWrappedInstance(object, "", null);
/*      */   }
/*      */ 
/*      */   public void setWrappedInstance(Object object, String nestedPath, Object rootObject)
/*      */   {
/*  205 */     Assert.notNull(object, "Target object must not be null");
/*  206 */     if (object.getClass() == javaUtilOptionalClass) {
/*  207 */       this.wrappedObject = OptionalUnwrapper.unwrap(object);
/*      */     }
/*      */     else {
/*  210 */       this.wrappedObject = object;
/*      */     }
/*  212 */     this.nestedPath = (nestedPath != null ? nestedPath : "");
/*  213 */     this.rootObject = (!"".equals(this.nestedPath) ? rootObject : this.wrappedObject);
/*  214 */     this.nestedPropertyAccessors = null;
/*  215 */     this.typeConverterDelegate = new TypeConverterDelegate(this, this.wrappedObject);
/*      */   }
/*      */ 
/*      */   public final Object getWrappedInstance() {
/*  219 */     return this.wrappedObject;
/*      */   }
/*      */ 
/*      */   public final Class<?> getWrappedClass() {
/*  223 */     return this.wrappedObject != null ? this.wrappedObject.getClass() : null;
/*      */   }
/*      */ 
/*      */   public final String getNestedPath()
/*      */   {
/*  230 */     return this.nestedPath;
/*      */   }
/*      */ 
/*      */   public final Object getRootInstance()
/*      */   {
/*  238 */     return this.rootObject;
/*      */   }
/*      */ 
/*      */   public final Class<?> getRootClass()
/*      */   {
/*  246 */     return this.rootObject != null ? this.rootObject.getClass() : null;
/*      */   }
/*      */ 
/*      */   public void setPropertyValue(String propertyName, Object value) throws BeansException
/*      */   {
/*      */     try
/*      */     {
/*  253 */       nestedPa = getPropertyAccessorForPropertyPath(propertyName);
/*      */     }
/*      */     catch (NotReadablePropertyException ex)
/*      */     {
/*      */       AbstractNestablePropertyAccessor nestedPa;
/*  256 */       throw new NotWritablePropertyException(getRootClass(), new StringBuilder().append(this.nestedPath).append(propertyName).toString(), new StringBuilder().append("Nested property in path '").append(propertyName).append("' does not exist").toString(), ex);
/*      */     }
/*      */     AbstractNestablePropertyAccessor nestedPa;
/*  259 */     PropertyTokenHolder tokens = getPropertyNameTokens(getFinalPath(nestedPa, propertyName));
/*  260 */     nestedPa.setPropertyValue(tokens, new PropertyValue(propertyName, value));
/*      */   }
/*      */ 
/*      */   public void setPropertyValue(PropertyValue pv) throws BeansException
/*      */   {
/*  265 */     PropertyTokenHolder tokens = (PropertyTokenHolder)pv.resolvedTokens;
/*  266 */     if (tokens == null) {
/*  267 */       String propertyName = pv.getName();
/*      */       try
/*      */       {
/*  270 */         nestedPa = getPropertyAccessorForPropertyPath(propertyName);
/*      */       }
/*      */       catch (NotReadablePropertyException ex)
/*      */       {
/*      */         AbstractNestablePropertyAccessor nestedPa;
/*  273 */         throw new NotWritablePropertyException(getRootClass(), new StringBuilder().append(this.nestedPath).append(propertyName).toString(), new StringBuilder().append("Nested property in path '").append(propertyName).append("' does not exist").toString(), ex);
/*      */       }
/*      */       AbstractNestablePropertyAccessor nestedPa;
/*  276 */       tokens = getPropertyNameTokens(getFinalPath(nestedPa, propertyName));
/*  277 */       if (nestedPa == this) {
/*  278 */         pv.getOriginalPropertyValue().resolvedTokens = tokens;
/*      */       }
/*  280 */       nestedPa.setPropertyValue(tokens, pv);
/*      */     }
/*      */     else {
/*  283 */       setPropertyValue(tokens, pv);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void setPropertyValue(PropertyTokenHolder tokens, PropertyValue pv) throws BeansException
/*      */   {
/*  289 */     String propertyName = tokens.canonicalName;
/*  290 */     String actualName = tokens.actualName;
/*      */ 
/*  292 */     if (tokens.keys != null)
/*      */     {
/*  294 */       PropertyTokenHolder getterTokens = new PropertyTokenHolder();
/*  295 */       getterTokens.canonicalName = tokens.canonicalName;
/*  296 */       getterTokens.actualName = tokens.actualName;
/*  297 */       getterTokens.keys = new String[tokens.keys.length - 1];
/*  298 */       System.arraycopy(tokens.keys, 0, getterTokens.keys, 0, tokens.keys.length - 1);
/*      */       try
/*      */       {
/*  301 */         propValue = getPropertyValue(getterTokens);
/*      */       }
/*      */       catch (NotReadablePropertyException ex)
/*      */       {
/*      */         Object propValue;
/*  304 */         throw new NotWritablePropertyException(getRootClass(), new StringBuilder().append(this.nestedPath).append(propertyName).toString(), new StringBuilder().append("Cannot access indexed value in property referenced in indexed property path '").append(propertyName).append("'").toString(), ex);
/*      */       }
/*      */       Object propValue;
/*  309 */       String key = tokens.keys[(tokens.keys.length - 1)];
/*  310 */       if (propValue == null)
/*      */       {
/*  312 */         if (isAutoGrowNestedPaths())
/*      */         {
/*  314 */           int lastKeyIndex = tokens.canonicalName.lastIndexOf(91);
/*  315 */           getterTokens.canonicalName = tokens.canonicalName.substring(0, lastKeyIndex);
/*  316 */           propValue = setDefaultValue(getterTokens);
/*      */         }
/*      */         else {
/*  319 */           throw new NullValueInNestedPathException(getRootClass(), new StringBuilder().append(this.nestedPath).append(propertyName).toString(), new StringBuilder().append("Cannot access indexed value in property referenced in indexed property path '").append(propertyName).append("': returned null").toString());
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  324 */       if (propValue.getClass().isArray()) {
/*  325 */         PropertyHandler ph = getLocalPropertyHandler(actualName);
/*  326 */         Class requiredType = propValue.getClass().getComponentType();
/*  327 */         int arrayIndex = Integer.parseInt(key);
/*  328 */         Object oldValue = null;
/*      */         try {
/*  330 */           if ((isExtractOldValueForEditor()) && (arrayIndex < Array.getLength(propValue))) {
/*  331 */             oldValue = Array.get(propValue, arrayIndex);
/*      */           }
/*  333 */           Object convertedValue = convertIfNecessary(propertyName, oldValue, pv.getValue(), requiredType, ph
/*  334 */             .nested(tokens.keys.length));
/*      */ 
/*  335 */           int length = Array.getLength(propValue);
/*  336 */           if ((arrayIndex >= length) && (arrayIndex < this.autoGrowCollectionLimit)) {
/*  337 */             Class componentType = propValue.getClass().getComponentType();
/*  338 */             Object newArray = Array.newInstance(componentType, arrayIndex + 1);
/*  339 */             System.arraycopy(propValue, 0, newArray, 0, length);
/*  340 */             setPropertyValue(actualName, newArray);
/*  341 */             propValue = getPropertyValue(actualName);
/*      */           }
/*  343 */           Array.set(propValue, arrayIndex, convertedValue);
/*      */         }
/*      */         catch (IndexOutOfBoundsException ex) {
/*  346 */           throw new InvalidPropertyException(getRootClass(), new StringBuilder().append(this.nestedPath).append(propertyName).toString(), new StringBuilder().append("Invalid array index in property path '").append(propertyName).append("'").toString(), ex);
/*      */         }
/*      */ 
/*      */       }
/*  350 */       else if ((propValue instanceof List)) {
/*  351 */         PropertyHandler ph = getPropertyHandler(actualName);
/*  352 */         Class requiredType = ph.getCollectionType(tokens.keys.length);
/*  353 */         List list = (List)propValue;
/*  354 */         int index = Integer.parseInt(key);
/*  355 */         Object oldValue = null;
/*  356 */         if ((isExtractOldValueForEditor()) && (index < list.size())) {
/*  357 */           oldValue = list.get(index);
/*      */         }
/*  359 */         Object convertedValue = convertIfNecessary(propertyName, oldValue, pv.getValue(), requiredType, ph
/*  360 */           .nested(tokens.keys.length));
/*      */ 
/*  361 */         int size = list.size();
/*  362 */         if ((index >= size) && (index < this.autoGrowCollectionLimit)) {
/*  363 */           for (int i = size; i < index; i++) {
/*      */             try {
/*  365 */               list.add(null);
/*      */             }
/*      */             catch (NullPointerException ex) {
/*  368 */               throw new InvalidPropertyException(getRootClass(), new StringBuilder().append(this.nestedPath).append(propertyName).toString(), new StringBuilder().append("Cannot set element with index ").append(index).append(" in List of size ").append(size).append(", accessed using property path '").append(propertyName).append("': List does not support filling up gaps with null elements").toString());
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*  374 */           list.add(convertedValue);
/*      */         }
/*      */         else {
/*      */           try {
/*  378 */             list.set(index, convertedValue);
/*      */           }
/*      */           catch (IndexOutOfBoundsException ex) {
/*  381 */             throw new InvalidPropertyException(getRootClass(), new StringBuilder().append(this.nestedPath).append(propertyName).toString(), new StringBuilder().append("Invalid list index in property path '").append(propertyName).append("'").toString(), ex);
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*  386 */       else if ((propValue instanceof Map)) {
/*  387 */         PropertyHandler ph = getLocalPropertyHandler(actualName);
/*  388 */         Class mapKeyType = ph.getMapKeyType(tokens.keys.length);
/*  389 */         Class mapValueType = ph.getMapValueType(tokens.keys.length);
/*  390 */         Map map = (Map)propValue;
/*      */ 
/*  393 */         TypeDescriptor typeDescriptor = TypeDescriptor.valueOf(mapKeyType);
/*  394 */         Object convertedMapKey = convertIfNecessary(null, null, key, mapKeyType, typeDescriptor);
/*  395 */         Object oldValue = null;
/*  396 */         if (isExtractOldValueForEditor()) {
/*  397 */           oldValue = map.get(convertedMapKey);
/*      */         }
/*      */ 
/*  401 */         Object convertedMapValue = convertIfNecessary(propertyName, oldValue, pv.getValue(), mapValueType, ph
/*  402 */           .nested(tokens.keys.length));
/*      */ 
/*  403 */         map.put(convertedMapKey, convertedMapValue);
/*      */       }
/*      */       else {
/*  406 */         throw new InvalidPropertyException(getRootClass(), new StringBuilder().append(this.nestedPath).append(propertyName).toString(), new StringBuilder().append("Property referenced in indexed property path '").append(propertyName).append("' is neither an array nor a List nor a Map; returned value was [").append(propValue).append("]").toString());
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  413 */       PropertyHandler ph = getLocalPropertyHandler(actualName);
/*  414 */       if ((ph == null) || (!ph.isWritable())) {
/*  415 */         if (pv.isOptional()) {
/*  416 */           if (logger.isDebugEnabled()) {
/*  417 */             logger.debug(new StringBuilder().append("Ignoring optional value for property '").append(actualName).append("' - property not found on bean class [")
/*  418 */               .append(getRootClass().getName()).append("]").toString());
/*      */           }
/*  420 */           return;
/*      */         }
/*      */ 
/*  423 */         throw createNotWritablePropertyException(propertyName);
/*      */       }
/*      */ 
/*  426 */       Object oldValue = null;
/*      */       try {
/*  428 */         Object originalValue = pv.getValue();
/*  429 */         Object valueToApply = originalValue;
/*  430 */         if (!Boolean.FALSE.equals(pv.conversionNecessary)) {
/*  431 */           if (pv.isConverted()) {
/*  432 */             valueToApply = pv.getConvertedValue();
/*      */           }
/*      */           else {
/*  435 */             if ((isExtractOldValueForEditor()) && (ph.isReadable())) {
/*      */               try {
/*  437 */                 oldValue = ph.getValue();
/*      */               }
/*      */               catch (Exception ex) {
/*  440 */                 if ((ex instanceof PrivilegedActionException)) {
/*  441 */                   ex = ((PrivilegedActionException)ex).getException();
/*      */                 }
/*  443 */                 if (logger.isDebugEnabled()) {
/*  444 */                   logger.debug(new StringBuilder().append("Could not read previous value of property '").append(this.nestedPath).append(propertyName).append("'").toString(), ex);
/*      */                 }
/*      */               }
/*      */             }
/*      */ 
/*  449 */             valueToApply = convertForProperty(propertyName, oldValue, originalValue, ph
/*  450 */               .toTypeDescriptor());
/*      */           }
/*  452 */           pv.getOriginalPropertyValue().conversionNecessary = Boolean.valueOf(valueToApply != originalValue);
/*      */         }
/*  454 */         ph.setValue(this.wrappedObject, valueToApply);
/*      */       }
/*      */       catch (TypeMismatchException ex) {
/*  457 */         throw ex;
/*      */       }
/*      */       catch (InvocationTargetException ex)
/*      */       {
/*  461 */         PropertyChangeEvent propertyChangeEvent = new PropertyChangeEvent(this.rootObject, new StringBuilder().append(this.nestedPath).append(propertyName).toString(), oldValue, pv
/*  461 */           .getValue());
/*  462 */         if ((ex.getTargetException() instanceof ClassCastException)) {
/*  463 */           throw new TypeMismatchException(propertyChangeEvent, ph.getPropertyType(), ex.getTargetException());
/*      */         }
/*      */ 
/*  466 */         Throwable cause = ex.getTargetException();
/*  467 */         if ((cause instanceof UndeclaredThrowableException))
/*      */         {
/*  469 */           cause = cause.getCause();
/*      */         }
/*  471 */         throw new MethodInvocationException(propertyChangeEvent, cause);
/*      */       }
/*      */       catch (Exception ex)
/*      */       {
/*  476 */         PropertyChangeEvent pce = new PropertyChangeEvent(this.rootObject, new StringBuilder().append(this.nestedPath).append(propertyName).toString(), oldValue, pv
/*  476 */           .getValue());
/*  477 */         throw new MethodInvocationException(pce, ex);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public Class<?> getPropertyType(String propertyName) throws BeansException
/*      */   {
/*      */     try {
/*  485 */       PropertyHandler ph = getPropertyHandler(propertyName);
/*  486 */       if (ph != null) {
/*  487 */         return ph.getPropertyType();
/*      */       }
/*      */ 
/*  491 */       Object value = getPropertyValue(propertyName);
/*  492 */       if (value != null) {
/*  493 */         return value.getClass();
/*      */       }
/*      */ 
/*  497 */       Class editorType = guessPropertyTypeFromEditors(propertyName);
/*  498 */       if (editorType != null) {
/*  499 */         return editorType;
/*      */       }
/*      */     }
/*      */     catch (InvalidPropertyException localInvalidPropertyException)
/*      */     {
/*      */     }
/*      */ 
/*  506 */     return null;
/*      */   }
/*      */ 
/*      */   public TypeDescriptor getPropertyTypeDescriptor(String propertyName) throws BeansException
/*      */   {
/*      */     try {
/*  512 */       AbstractNestablePropertyAccessor nestedPa = getPropertyAccessorForPropertyPath(propertyName);
/*  513 */       String finalPath = getFinalPath(nestedPa, propertyName);
/*  514 */       PropertyTokenHolder tokens = getPropertyNameTokens(finalPath);
/*  515 */       PropertyHandler ph = nestedPa.getLocalPropertyHandler(tokens.actualName);
/*  516 */       if (ph != null) {
/*  517 */         if (tokens.keys != null) {
/*  518 */           if ((ph.isReadable()) || (ph.isWritable())) {
/*  519 */             return ph.nested(tokens.keys.length);
/*      */           }
/*      */ 
/*      */         }
/*  523 */         else if ((ph.isReadable()) || (ph.isWritable())) {
/*  524 */           return ph.toTypeDescriptor();
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (InvalidPropertyException localInvalidPropertyException)
/*      */     {
/*      */     }
/*      */ 
/*  532 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean isReadableProperty(String propertyName)
/*      */   {
/*      */     try {
/*  538 */       PropertyHandler ph = getPropertyHandler(propertyName);
/*  539 */       if (ph != null) {
/*  540 */         return ph.isReadable();
/*      */       }
/*      */ 
/*  544 */       getPropertyValue(propertyName);
/*  545 */       return true;
/*      */     }
/*      */     catch (InvalidPropertyException localInvalidPropertyException)
/*      */     {
/*      */     }
/*      */ 
/*  551 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isWritableProperty(String propertyName)
/*      */   {
/*      */     try {
/*  557 */       PropertyHandler ph = getPropertyHandler(propertyName);
/*  558 */       if (ph != null) {
/*  559 */         return ph.isWritable();
/*      */       }
/*      */ 
/*  563 */       getPropertyValue(propertyName);
/*  564 */       return true;
/*      */     }
/*      */     catch (InvalidPropertyException localInvalidPropertyException)
/*      */     {
/*      */     }
/*      */ 
/*  570 */     return false;
/*      */   }
/*      */ 
/*      */   private Object convertIfNecessary(String propertyName, Object oldValue, Object newValue, Class<?> requiredType, TypeDescriptor td) throws TypeMismatchException
/*      */   {
/*      */     try {
/*  576 */       return this.typeConverterDelegate.convertIfNecessary(propertyName, oldValue, newValue, requiredType, td);
/*      */     }
/*      */     catch (ConverterNotFoundException ex) {
/*  579 */       PropertyChangeEvent pce = new PropertyChangeEvent(this.rootObject, new StringBuilder().append(this.nestedPath).append(propertyName).toString(), oldValue, newValue);
/*      */ 
/*  581 */       throw new ConversionNotSupportedException(pce, td.getType(), ex);
/*      */     }
/*      */     catch (ConversionException ex) {
/*  584 */       PropertyChangeEvent pce = new PropertyChangeEvent(this.rootObject, new StringBuilder().append(this.nestedPath).append(propertyName).toString(), oldValue, newValue);
/*      */ 
/*  586 */       throw new TypeMismatchException(pce, requiredType, ex);
/*      */     }
/*      */     catch (IllegalStateException ex) {
/*  589 */       PropertyChangeEvent pce = new PropertyChangeEvent(this.rootObject, new StringBuilder().append(this.nestedPath).append(propertyName).toString(), oldValue, newValue);
/*      */ 
/*  591 */       throw new ConversionNotSupportedException(pce, requiredType, ex);
/*      */     }
/*      */     catch (Throwable ex) {
/*  594 */       PropertyChangeEvent pce = new PropertyChangeEvent(this.rootObject, new StringBuilder().append(this.nestedPath).append(propertyName).toString(), oldValue, newValue);
/*      */ 
/*  596 */       throw new TypeMismatchException(pce, requiredType, ex);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Object convertForProperty(String propertyName, Object oldValue, Object newValue, TypeDescriptor td)
/*      */     throws TypeMismatchException
/*      */   {
/*  603 */     return convertIfNecessary(propertyName, oldValue, newValue, td.getType(), td);
/*      */   }
/*      */ 
/*      */   public Object getPropertyValue(String propertyName) throws BeansException
/*      */   {
/*  608 */     AbstractNestablePropertyAccessor nestedPa = getPropertyAccessorForPropertyPath(propertyName);
/*  609 */     PropertyTokenHolder tokens = getPropertyNameTokens(getFinalPath(nestedPa, propertyName));
/*  610 */     return nestedPa.getPropertyValue(tokens);
/*      */   }
/*      */ 
/*      */   protected Object getPropertyValue(PropertyTokenHolder tokens) throws BeansException
/*      */   {
/*  615 */     String propertyName = tokens.canonicalName;
/*  616 */     String actualName = tokens.actualName;
/*  617 */     PropertyHandler ph = getLocalPropertyHandler(actualName);
/*  618 */     if ((ph == null) || (!ph.isReadable()))
/*  619 */       throw new NotReadablePropertyException(getRootClass(), new StringBuilder().append(this.nestedPath).append(propertyName).toString());
/*      */     try
/*      */     {
/*  622 */       Object value = ph.getValue();
/*  623 */       if (tokens.keys != null) {
/*  624 */         if (value == null) {
/*  625 */           if (isAutoGrowNestedPaths()) {
/*  626 */             value = setDefaultValue(tokens.actualName);
/*      */           }
/*      */           else {
/*  629 */             throw new NullValueInNestedPathException(getRootClass(), new StringBuilder().append(this.nestedPath).append(propertyName).toString(), new StringBuilder().append("Cannot access indexed value of property referenced in indexed property path '").append(propertyName).append("': returned null").toString());
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  634 */         String indexedPropertyName = tokens.actualName;
/*      */ 
/*  636 */         for (int i = 0; i < tokens.keys.length; i++) {
/*  637 */           String key = tokens.keys[i];
/*  638 */           if (value == null) {
/*  639 */             throw new NullValueInNestedPathException(getRootClass(), new StringBuilder().append(this.nestedPath).append(propertyName).toString(), new StringBuilder().append("Cannot access indexed value of property referenced in indexed property path '").append(propertyName).append("': returned null").toString());
/*      */           }
/*      */ 
/*  643 */           if (value.getClass().isArray()) {
/*  644 */             int index = Integer.parseInt(key);
/*  645 */             value = growArrayIfNecessary(value, index, indexedPropertyName);
/*  646 */             value = Array.get(value, index);
/*      */           }
/*  648 */           else if ((value instanceof List)) {
/*  649 */             int index = Integer.parseInt(key);
/*  650 */             List list = (List)value;
/*  651 */             growCollectionIfNecessary(list, index, indexedPropertyName, ph, i + 1);
/*  652 */             value = list.get(index);
/*      */           }
/*  654 */           else if ((value instanceof Set))
/*      */           {
/*  656 */             Set set = (Set)value;
/*  657 */             int index = Integer.parseInt(key);
/*  658 */             if ((index < 0) || (index >= set.size()))
/*      */             {
/*  661 */               throw new InvalidPropertyException(getRootClass(), new StringBuilder().append(this.nestedPath).append(propertyName).toString(), new StringBuilder().append("Cannot get element with index ").append(index).append(" from Set of size ")
/*  661 */                 .append(set
/*  661 */                 .size()).append(", accessed using property path '").append(propertyName).append("'").toString());
/*      */             }
/*  663 */             Iterator it = set.iterator();
/*  664 */             for (int j = 0; it.hasNext(); j++) {
/*  665 */               Object elem = it.next();
/*  666 */               if (j == index) {
/*  667 */                 value = elem;
/*  668 */                 break;
/*      */               }
/*      */             }
/*      */           }
/*  672 */           else if ((value instanceof Map)) {
/*  673 */             Map map = (Map)value;
/*  674 */             Class mapKeyType = ph.getResolvableType().getNested(i + 1).asMap().resolveGeneric(new int[] { 0 });
/*      */ 
/*  677 */             TypeDescriptor typeDescriptor = TypeDescriptor.valueOf(mapKeyType);
/*  678 */             Object convertedMapKey = convertIfNecessary(null, null, key, mapKeyType, typeDescriptor);
/*  679 */             value = map.get(convertedMapKey);
/*      */           }
/*      */           else {
/*  682 */             throw new InvalidPropertyException(getRootClass(), new StringBuilder().append(this.nestedPath).append(propertyName).toString(), new StringBuilder().append("Property referenced in indexed property path '").append(propertyName).append("' is neither an array nor a List nor a Set nor a Map; returned value was [").append(value).append("]").toString());
/*      */           }
/*      */ 
/*  686 */           indexedPropertyName = new StringBuilder().append(indexedPropertyName).append("[").append(key).append("]").toString();
/*      */         }
/*      */       }
/*  689 */       return value;
/*      */     }
/*      */     catch (IndexOutOfBoundsException ex) {
/*  692 */       throw new InvalidPropertyException(getRootClass(), new StringBuilder().append(this.nestedPath).append(propertyName).toString(), new StringBuilder().append("Index of out of bounds in property path '").append(propertyName).append("'").toString(), ex);
/*      */     }
/*      */     catch (NumberFormatException ex)
/*      */     {
/*  696 */       throw new InvalidPropertyException(getRootClass(), new StringBuilder().append(this.nestedPath).append(propertyName).toString(), new StringBuilder().append("Invalid index in property path '").append(propertyName).append("'").toString(), ex);
/*      */     }
/*      */     catch (TypeMismatchException ex)
/*      */     {
/*  700 */       throw new InvalidPropertyException(getRootClass(), new StringBuilder().append(this.nestedPath).append(propertyName).toString(), new StringBuilder().append("Invalid index in property path '").append(propertyName).append("'").toString(), ex);
/*      */     }
/*      */     catch (InvocationTargetException ex)
/*      */     {
/*  704 */       throw new InvalidPropertyException(getRootClass(), new StringBuilder().append(this.nestedPath).append(propertyName).toString(), new StringBuilder().append("Getter for property '").append(actualName).append("' threw exception").toString(), ex);
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/*  708 */       throw new InvalidPropertyException(getRootClass(), new StringBuilder().append(this.nestedPath).append(propertyName).toString(), new StringBuilder().append("Illegal attempt to get property '").append(actualName).append("' threw exception").toString(), ex);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected PropertyHandler getPropertyHandler(String propertyName)
/*      */     throws BeansException
/*      */   {
/*  723 */     Assert.notNull(propertyName, "Property name must not be null");
/*  724 */     AbstractNestablePropertyAccessor nestedPa = getPropertyAccessorForPropertyPath(propertyName);
/*  725 */     return nestedPa.getLocalPropertyHandler(getFinalPath(nestedPa, propertyName));
/*      */   }
/*      */ 
/*      */   protected abstract PropertyHandler getLocalPropertyHandler(String paramString);
/*      */ 
/*      */   protected abstract AbstractNestablePropertyAccessor newNestedPropertyAccessor(Object paramObject, String paramString);
/*      */ 
/*      */   protected abstract NotWritablePropertyException createNotWritablePropertyException(String paramString);
/*      */ 
/*      */   private Object growArrayIfNecessary(Object array, int index, String name)
/*      */   {
/*  752 */     if (!isAutoGrowNestedPaths()) {
/*  753 */       return array;
/*      */     }
/*  755 */     int length = Array.getLength(array);
/*  756 */     if ((index >= length) && (index < this.autoGrowCollectionLimit)) {
/*  757 */       Class componentType = array.getClass().getComponentType();
/*  758 */       Object newArray = Array.newInstance(componentType, index + 1);
/*  759 */       System.arraycopy(array, 0, newArray, 0, length);
/*  760 */       for (int i = length; i < Array.getLength(newArray); i++) {
/*  761 */         Array.set(newArray, i, newValue(componentType, null, name));
/*      */       }
/*  763 */       setPropertyValue(name, newArray);
/*  764 */       return getPropertyValue(name);
/*      */     }
/*      */ 
/*  767 */     return array;
/*      */   }
/*      */ 
/*      */   private void growCollectionIfNecessary(Collection<Object> collection, int index, String name, PropertyHandler ph, int nestingLevel)
/*      */   {
/*  774 */     if (!isAutoGrowNestedPaths()) {
/*  775 */       return;
/*      */     }
/*  777 */     int size = collection.size();
/*  778 */     if ((index >= size) && (index < this.autoGrowCollectionLimit)) {
/*  779 */       Class elementType = ph.getResolvableType().getNested(nestingLevel).asCollection().resolveGeneric(new int[0]);
/*  780 */       if (elementType != null)
/*  781 */         for (int i = collection.size(); i < index + 1; i++)
/*  782 */           collection.add(newValue(elementType, null, name));
/*      */     }
/*      */   }
/*      */ 
/*      */   protected String getFinalPath(AbstractNestablePropertyAccessor pa, String nestedPath)
/*      */   {
/*  795 */     if (pa == this) {
/*  796 */       return nestedPath;
/*      */     }
/*  798 */     return nestedPath.substring(PropertyAccessorUtils.getLastNestedPropertySeparatorIndex(nestedPath) + 1);
/*      */   }
/*      */ 
/*      */   protected AbstractNestablePropertyAccessor getPropertyAccessorForPropertyPath(String propertyPath)
/*      */   {
/*  808 */     int pos = PropertyAccessorUtils.getFirstNestedPropertySeparatorIndex(propertyPath);
/*      */ 
/*  810 */     if (pos > -1) {
/*  811 */       String nestedProperty = propertyPath.substring(0, pos);
/*  812 */       String nestedPath = propertyPath.substring(pos + 1);
/*  813 */       AbstractNestablePropertyAccessor nestedPa = getNestedPropertyAccessor(nestedProperty);
/*  814 */       return nestedPa.getPropertyAccessorForPropertyPath(nestedPath);
/*      */     }
/*      */ 
/*  817 */     return this;
/*      */   }
/*      */ 
/*      */   private AbstractNestablePropertyAccessor getNestedPropertyAccessor(String nestedProperty)
/*      */   {
/*  830 */     if (this.nestedPropertyAccessors == null) {
/*  831 */       this.nestedPropertyAccessors = new HashMap();
/*      */     }
/*      */ 
/*  834 */     PropertyTokenHolder tokens = getPropertyNameTokens(nestedProperty);
/*  835 */     String canonicalName = tokens.canonicalName;
/*  836 */     Object value = getPropertyValue(tokens);
/*  837 */     if ((value == null) || ((value.getClass() == javaUtilOptionalClass) && (OptionalUnwrapper.isEmpty(value)))) {
/*  838 */       if (isAutoGrowNestedPaths()) {
/*  839 */         value = setDefaultValue(tokens);
/*      */       }
/*      */       else {
/*  842 */         throw new NullValueInNestedPathException(getRootClass(), new StringBuilder().append(this.nestedPath).append(canonicalName).toString());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  847 */     AbstractNestablePropertyAccessor nestedPa = (AbstractNestablePropertyAccessor)this.nestedPropertyAccessors.get(canonicalName);
/*  848 */     if (nestedPa != null) { if (nestedPa.getWrappedInstance() == 
/*  849 */         (value
/*  849 */         .getClass() == javaUtilOptionalClass ? OptionalUnwrapper.unwrap(value) : value)); } else { if (logger.isTraceEnabled()) {
/*  851 */         logger.trace(new StringBuilder().append("Creating new nested ").append(getClass().getSimpleName()).append(" for property '").append(canonicalName).append("'").toString());
/*      */       }
/*  853 */       nestedPa = newNestedPropertyAccessor(value, new StringBuilder().append(this.nestedPath).append(canonicalName).append(".").toString());
/*      */ 
/*  855 */       copyDefaultEditorsTo(nestedPa);
/*  856 */       copyCustomEditorsTo(nestedPa, canonicalName);
/*  857 */       this.nestedPropertyAccessors.put(canonicalName, nestedPa); break label323;
/*      */     }
/*      */ 
/*  860 */     if (logger.isTraceEnabled()) {
/*  861 */       logger.trace(new StringBuilder().append("Using cached nested property accessor for property '").append(canonicalName).append("'").toString());
/*      */     }
/*      */ 
/*  864 */     label323: return nestedPa;
/*      */   }
/*      */ 
/*      */   private Object setDefaultValue(String propertyName) {
/*  868 */     PropertyTokenHolder tokens = new PropertyTokenHolder();
/*  869 */     tokens.actualName = propertyName;
/*  870 */     tokens.canonicalName = propertyName;
/*  871 */     return setDefaultValue(tokens);
/*      */   }
/*      */ 
/*      */   private Object setDefaultValue(PropertyTokenHolder tokens) {
/*  875 */     PropertyValue pv = createDefaultPropertyValue(tokens);
/*  876 */     setPropertyValue(tokens, pv);
/*  877 */     return getPropertyValue(tokens);
/*      */   }
/*      */ 
/*      */   private PropertyValue createDefaultPropertyValue(PropertyTokenHolder tokens) {
/*  881 */     TypeDescriptor desc = getPropertyTypeDescriptor(tokens.canonicalName);
/*  882 */     Class type = desc.getType();
/*  883 */     if (type == null) {
/*  884 */       throw new NullValueInNestedPathException(getRootClass(), new StringBuilder().append(this.nestedPath).append(tokens.canonicalName).toString(), "Could not determine property type for auto-growing a default value");
/*      */     }
/*      */ 
/*  887 */     Object defaultValue = newValue(type, desc, tokens.canonicalName);
/*  888 */     return new PropertyValue(tokens.canonicalName, defaultValue);
/*      */   }
/*      */ 
/*      */   private Object newValue(Class<?> type, TypeDescriptor desc, String name) {
/*      */     try {
/*  893 */       if (type.isArray()) {
/*  894 */         Class componentType = type.getComponentType();
/*      */ 
/*  896 */         if (componentType.isArray()) {
/*  897 */           Object array = Array.newInstance(componentType, 1);
/*  898 */           Array.set(array, 0, Array.newInstance(componentType.getComponentType(), 0));
/*  899 */           return array;
/*      */         }
/*      */ 
/*  902 */         return Array.newInstance(componentType, 0);
/*      */       }
/*      */ 
/*  905 */       if (Collection.class.isAssignableFrom(type)) {
/*  906 */         TypeDescriptor elementDesc = desc != null ? desc.getElementTypeDescriptor() : null;
/*  907 */         return CollectionFactory.createCollection(type, elementDesc != null ? elementDesc.getType() : null, 16);
/*      */       }
/*  909 */       if (Map.class.isAssignableFrom(type)) {
/*  910 */         TypeDescriptor keyDesc = desc != null ? desc.getMapKeyTypeDescriptor() : null;
/*  911 */         return CollectionFactory.createMap(type, keyDesc != null ? keyDesc.getType() : null, 16);
/*      */       }
/*      */ 
/*  914 */       return BeanUtils.instantiate(type);
/*      */     }
/*      */     catch (Throwable ex)
/*      */     {
/*  919 */       throw new NullValueInNestedPathException(getRootClass(), new StringBuilder().append(this.nestedPath).append(name).toString(), new StringBuilder().append("Could not instantiate property type [")
/*  919 */         .append(type
/*  919 */         .getName()).append("] to auto-grow nested property path").toString(), ex);
/*      */     }
/*      */   }
/*      */ 
/*      */   private PropertyTokenHolder getPropertyNameTokens(String propertyName)
/*      */   {
/*  929 */     PropertyTokenHolder tokens = new PropertyTokenHolder();
/*  930 */     String actualName = null;
/*  931 */     List keys = new ArrayList(2);
/*  932 */     int searchIndex = 0;
/*  933 */     while (searchIndex != -1) {
/*  934 */       int keyStart = propertyName.indexOf("[", searchIndex);
/*  935 */       searchIndex = -1;
/*  936 */       if (keyStart != -1) {
/*  937 */         int keyEnd = propertyName.indexOf("]", keyStart + "[".length());
/*  938 */         if (keyEnd != -1) {
/*  939 */           if (actualName == null) {
/*  940 */             actualName = propertyName.substring(0, keyStart);
/*      */           }
/*  942 */           String key = propertyName.substring(keyStart + "[".length(), keyEnd);
/*  943 */           if (((key.length() > 1) && (key.startsWith("'")) && (key.endsWith("'"))) || (
/*  944 */             (key
/*  944 */             .startsWith("\"")) && 
/*  944 */             (key.endsWith("\"")))) {
/*  945 */             key = key.substring(1, key.length() - 1);
/*      */           }
/*  947 */           keys.add(key);
/*  948 */           searchIndex = keyEnd + "]".length();
/*      */         }
/*      */       }
/*      */     }
/*  952 */     tokens.actualName = (actualName != null ? actualName : propertyName);
/*  953 */     tokens.canonicalName = tokens.actualName;
/*  954 */     if (!keys.isEmpty())
/*      */     {
/*      */       PropertyTokenHolder tmp224_223 = tokens; tmp224_223.canonicalName = new StringBuilder().append(tmp224_223.canonicalName).append("[")
/*  956 */         .append(StringUtils.collectionToDelimitedString(keys, "]["))
/*  956 */         .append("]").toString();
/*      */ 
/*  958 */       tokens.keys = StringUtils.toStringArray(keys);
/*      */     }
/*  960 */     return tokens;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  965 */     StringBuilder sb = new StringBuilder(getClass().getName());
/*  966 */     if (this.wrappedObject != null) {
/*  967 */       sb.append(": wrapping object [").append(ObjectUtils.identityToString(this.wrappedObject)).append("]");
/*      */     }
/*      */     else {
/*  970 */       sb.append(": no wrapped object set");
/*      */     }
/*  972 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*      */     try
/*      */     {
/*   83 */       javaUtilOptionalClass = ClassUtils.forName("java.util.Optional", AbstractNestablePropertyAccessor.class
/*   83 */         .getClassLoader());
/*      */     }
/*      */     catch (ClassNotFoundException localClassNotFoundException)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   @UsesJava8
/*      */   private static class OptionalUnwrapper
/*      */   {
/*      */     public static Object unwrap(Object optionalObject)
/*      */     {
/* 1046 */       Optional optional = (Optional)optionalObject;
/* 1047 */       Assert.isTrue(optional.isPresent(), "Optional value must be present");
/* 1048 */       Object result = optional.get();
/* 1049 */       Assert.isTrue(!(result instanceof Optional), "Multi-level Optional usage not supported");
/* 1050 */       return result;
/*      */     }
/*      */ 
/*      */     public static boolean isEmpty(Object optionalObject) {
/* 1054 */       return !((Optional)optionalObject).isPresent();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected static class PropertyTokenHolder
/*      */   {
/*      */     public String canonicalName;
/*      */     public String actualName;
/*      */     public String[] keys;
/*      */   }
/*      */ 
/*      */   protected static abstract class PropertyHandler
/*      */   {
/*      */     private final Class<?> propertyType;
/*      */     private final boolean readable;
/*      */     private final boolean writable;
/*      */ 
/*      */     public PropertyHandler(Class<?> propertyType, boolean readable, boolean writable)
/*      */     {
/*  988 */       this.propertyType = propertyType;
/*  989 */       this.readable = readable;
/*  990 */       this.writable = writable;
/*      */     }
/*      */ 
/*      */     public Class<?> getPropertyType() {
/*  994 */       return this.propertyType;
/*      */     }
/*      */ 
/*      */     public boolean isReadable() {
/*  998 */       return this.readable;
/*      */     }
/*      */ 
/*      */     public boolean isWritable() {
/* 1002 */       return this.writable;
/*      */     }
/*      */ 
/*      */     public abstract TypeDescriptor toTypeDescriptor();
/*      */ 
/*      */     public abstract ResolvableType getResolvableType();
/*      */ 
/*      */     public Class<?> getMapKeyType(int nestingLevel) {
/* 1010 */       return getResolvableType().getNested(nestingLevel).asMap().resolveGeneric(new int[] { 0 });
/*      */     }
/*      */ 
/*      */     public Class<?> getMapValueType(int nestingLevel) {
/* 1014 */       return getResolvableType().getNested(nestingLevel).asMap().resolveGeneric(new int[] { 1 });
/*      */     }
/*      */ 
/*      */     public Class<?> getCollectionType(int nestingLevel) {
/* 1018 */       return getResolvableType().getNested(nestingLevel).asCollection().resolveGeneric(new int[0]);
/*      */     }
/*      */ 
/*      */     public abstract TypeDescriptor nested(int paramInt);
/*      */ 
/*      */     public abstract Object getValue()
/*      */       throws Exception;
/*      */ 
/*      */     public abstract void setValue(Object paramObject1, Object paramObject2)
/*      */       throws Exception;
/*      */   }
/*      */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.AbstractNestablePropertyAccessor
 * JD-Core Version:    0.6.2
 */