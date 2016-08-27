/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.CollectionFactory;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.convert.ConversionFailedException;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.NumberUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ class TypeConverterDelegate
/*     */ {
/*  55 */   private static final Log logger = LogFactory.getLog(TypeConverterDelegate.class);
/*     */ 
/*  58 */   private static Object javaUtilOptionalEmpty = null;
/*     */   private final PropertyEditorRegistrySupport propertyEditorRegistry;
/*     */   private final Object targetObject;
/*     */ 
/*     */   public TypeConverterDelegate(PropertyEditorRegistrySupport propertyEditorRegistry)
/*     */   {
/*  81 */     this(propertyEditorRegistry, null);
/*     */   }
/*     */ 
/*     */   public TypeConverterDelegate(PropertyEditorRegistrySupport propertyEditorRegistry, Object targetObject)
/*     */   {
/*  90 */     this.propertyEditorRegistry = propertyEditorRegistry;
/*  91 */     this.targetObject = targetObject;
/*     */   }
/*     */ 
/*     */   public <T> T convertIfNecessary(Object newValue, Class<T> requiredType, MethodParameter methodParam)
/*     */     throws IllegalArgumentException
/*     */   {
/* 108 */     return convertIfNecessary(null, null, newValue, requiredType, methodParam != null ? new TypeDescriptor(methodParam) : 
/* 109 */       TypeDescriptor.valueOf(requiredType));
/*     */   }
/*     */ 
/*     */   public <T> T convertIfNecessary(Object newValue, Class<T> requiredType, Field field)
/*     */     throws IllegalArgumentException
/*     */   {
/* 125 */     return convertIfNecessary(null, null, newValue, requiredType, field != null ? new TypeDescriptor(field) : 
/* 126 */       TypeDescriptor.valueOf(requiredType));
/*     */   }
/*     */ 
/*     */   public <T> T convertIfNecessary(String propertyName, Object oldValue, Object newValue, Class<T> requiredType)
/*     */     throws IllegalArgumentException
/*     */   {
/* 143 */     return convertIfNecessary(propertyName, oldValue, newValue, requiredType, TypeDescriptor.valueOf(requiredType));
/*     */   }
/*     */ 
/*     */   public <T> T convertIfNecessary(String propertyName, Object oldValue, Object newValue, Class<T> requiredType, TypeDescriptor typeDescriptor)
/*     */     throws IllegalArgumentException
/*     */   {
/* 163 */     PropertyEditor editor = this.propertyEditorRegistry.findCustomEditor(requiredType, propertyName);
/*     */ 
/* 165 */     ConversionFailedException conversionAttemptEx = null;
/*     */ 
/* 168 */     ConversionService conversionService = this.propertyEditorRegistry.getConversionService();
/* 169 */     if ((editor == null) && (conversionService != null) && (newValue != null) && (typeDescriptor != null)) {
/* 170 */       TypeDescriptor sourceTypeDesc = TypeDescriptor.forObject(newValue);
/* 171 */       if (conversionService.canConvert(sourceTypeDesc, typeDescriptor)) {
/*     */         try {
/* 173 */           return conversionService.convert(newValue, sourceTypeDesc, typeDescriptor);
/*     */         }
/*     */         catch (ConversionFailedException ex)
/*     */         {
/* 177 */           conversionAttemptEx = ex;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 182 */     Object convertedValue = newValue;
/*     */ 
/* 185 */     if ((editor != null) || ((requiredType != null) && (!ClassUtils.isAssignableValue(requiredType, convertedValue)))) {
/* 186 */       if ((typeDescriptor != null) && (requiredType != null) && (Collection.class.isAssignableFrom(requiredType)) && ((convertedValue instanceof String)))
/*     */       {
/* 188 */         TypeDescriptor elementTypeDesc = typeDescriptor.getElementTypeDescriptor();
/* 189 */         if (elementTypeDesc != null) {
/* 190 */           Class elementType = elementTypeDesc.getType();
/* 191 */           if ((Class.class == elementType) || (Enum.class.isAssignableFrom(elementType))) {
/* 192 */             convertedValue = StringUtils.commaDelimitedListToStringArray((String)convertedValue);
/*     */           }
/*     */         }
/*     */       }
/* 196 */       if (editor == null) {
/* 197 */         editor = findDefaultEditor(requiredType);
/*     */       }
/* 199 */       convertedValue = doConvertValue(oldValue, convertedValue, requiredType, editor);
/*     */     }
/*     */ 
/* 202 */     boolean standardConversion = false;
/*     */ 
/* 204 */     if (requiredType != null)
/*     */     {
/* 207 */       if (convertedValue != null) {
/* 208 */         if (Object.class == requiredType) {
/* 209 */           return convertedValue;
/*     */         }
/* 211 */         if (requiredType.isArray())
/*     */         {
/* 213 */           if (((convertedValue instanceof String)) && (Enum.class.isAssignableFrom(requiredType.getComponentType()))) {
/* 214 */             convertedValue = StringUtils.commaDelimitedListToStringArray((String)convertedValue);
/*     */           }
/* 216 */           return convertToTypedArray(convertedValue, propertyName, requiredType.getComponentType());
/*     */         }
/* 218 */         if ((convertedValue instanceof Collection))
/*     */         {
/* 220 */           convertedValue = convertToTypedCollection((Collection)convertedValue, propertyName, requiredType, typeDescriptor);
/*     */ 
/* 222 */           standardConversion = true;
/*     */         }
/* 224 */         else if ((convertedValue instanceof Map))
/*     */         {
/* 226 */           convertedValue = convertToTypedMap((Map)convertedValue, propertyName, requiredType, typeDescriptor);
/*     */ 
/* 228 */           standardConversion = true;
/*     */         }
/* 230 */         if ((convertedValue.getClass().isArray()) && (Array.getLength(convertedValue) == 1)) {
/* 231 */           convertedValue = Array.get(convertedValue, 0);
/* 232 */           standardConversion = true;
/*     */         }
/* 234 */         if ((String.class == requiredType) && (ClassUtils.isPrimitiveOrWrapper(convertedValue.getClass())))
/*     */         {
/* 236 */           return convertedValue.toString();
/*     */         }
/* 238 */         if (((convertedValue instanceof String)) && (!requiredType.isInstance(convertedValue))) {
/* 239 */           if ((conversionAttemptEx == null) && (!requiredType.isInterface()) && (!requiredType.isEnum())) {
/*     */             try {
/* 241 */               Constructor strCtor = requiredType.getConstructor(new Class[] { String.class });
/* 242 */               return BeanUtils.instantiateClass(strCtor, new Object[] { convertedValue });
/*     */             }
/*     */             catch (NoSuchMethodException ex)
/*     */             {
/* 246 */               if (logger.isTraceEnabled())
/* 247 */                 logger.trace(new StringBuilder().append("No String constructor found on type [").append(requiredType.getName()).append("]").toString(), ex);
/*     */             }
/*     */             catch (Exception ex)
/*     */             {
/* 251 */               if (logger.isDebugEnabled()) {
/* 252 */                 logger.debug(new StringBuilder().append("Construction via String failed for type [").append(requiredType.getName()).append("]").toString(), ex);
/*     */               }
/*     */             }
/*     */           }
/* 256 */           String trimmedValue = ((String)convertedValue).trim();
/* 257 */           if ((requiredType.isEnum()) && ("".equals(trimmedValue)))
/*     */           {
/* 259 */             return null;
/*     */           }
/* 261 */           convertedValue = attemptToConvertStringToEnum(requiredType, trimmedValue, convertedValue);
/* 262 */           standardConversion = true;
/*     */         }
/* 264 */         else if (((convertedValue instanceof Number)) && (Number.class.isAssignableFrom(requiredType))) {
/* 265 */           convertedValue = NumberUtils.convertNumberToTargetClass((Number)convertedValue, requiredType);
/*     */ 
/* 267 */           standardConversion = true;
/*     */         }
/*     */ 
/*     */       }
/* 272 */       else if ((javaUtilOptionalEmpty != null) && (requiredType == javaUtilOptionalEmpty.getClass())) {
/* 273 */         convertedValue = javaUtilOptionalEmpty;
/*     */       }
/*     */ 
/* 277 */       if (!ClassUtils.isAssignableValue(requiredType, convertedValue)) {
/* 278 */         if (conversionAttemptEx != null)
/*     */         {
/* 280 */           throw conversionAttemptEx;
/*     */         }
/* 282 */         if (conversionService != null)
/*     */         {
/* 285 */           TypeDescriptor sourceTypeDesc = TypeDescriptor.forObject(newValue);
/* 286 */           if (conversionService.canConvert(sourceTypeDesc, typeDescriptor)) {
/* 287 */             return conversionService.convert(newValue, sourceTypeDesc, typeDescriptor);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 292 */         StringBuilder msg = new StringBuilder();
/* 293 */         msg.append("Cannot convert value of type [").append(ClassUtils.getDescriptiveType(newValue));
/* 294 */         msg.append("] to required type [").append(ClassUtils.getQualifiedName(requiredType)).append("]");
/* 295 */         if (propertyName != null) {
/* 296 */           msg.append(" for property '").append(propertyName).append("'");
/*     */         }
/* 298 */         if (editor != null) {
/* 299 */           msg.append(": PropertyEditor [").append(editor.getClass().getName()).append("] returned inappropriate value of type [")
/* 300 */             .append(
/* 301 */             ClassUtils.getDescriptiveType(convertedValue))
/* 301 */             .append("]");
/*     */ 
/* 302 */           throw new IllegalArgumentException(msg.toString());
/*     */         }
/*     */ 
/* 305 */         msg.append(": no matching editors or conversion strategy found");
/* 306 */         throw new IllegalStateException(msg.toString());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 311 */     if (conversionAttemptEx != null) {
/* 312 */       if ((editor == null) && (!standardConversion) && (requiredType != null) && (Object.class != requiredType)) {
/* 313 */         throw conversionAttemptEx;
/*     */       }
/* 315 */       logger.debug("Original ConversionService attempt failed - ignored since PropertyEditor based conversion eventually succeeded", conversionAttemptEx);
/*     */     }
/*     */ 
/* 319 */     return convertedValue;
/*     */   }
/*     */ 
/*     */   private Object attemptToConvertStringToEnum(Class<?> requiredType, String trimmedValue, Object currentConvertedValue) {
/* 323 */     Object convertedValue = currentConvertedValue;
/*     */ 
/* 325 */     if (Enum.class == requiredType)
/*     */     {
/* 327 */       int index = trimmedValue.lastIndexOf(".");
/* 328 */       if (index > -1) {
/* 329 */         String enumType = trimmedValue.substring(0, index);
/* 330 */         String fieldName = trimmedValue.substring(index + 1);
/* 331 */         ClassLoader cl = this.targetObject.getClass().getClassLoader();
/*     */         try {
/* 333 */           Class enumValueType = ClassUtils.forName(enumType, cl);
/* 334 */           Field enumField = enumValueType.getField(fieldName);
/* 335 */           convertedValue = enumField.get(null);
/*     */         }
/*     */         catch (ClassNotFoundException ex) {
/* 338 */           if (logger.isTraceEnabled())
/* 339 */             logger.trace(new StringBuilder().append("Enum class [").append(enumType).append("] cannot be loaded").toString(), ex);
/*     */         }
/*     */         catch (Throwable ex)
/*     */         {
/* 343 */           if (logger.isTraceEnabled()) {
/* 344 */             logger.trace(new StringBuilder().append("Field [").append(fieldName).append("] isn't an enum value for type [").append(enumType).append("]").toString(), ex);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 350 */     if (convertedValue == currentConvertedValue)
/*     */     {
/*     */       try
/*     */       {
/* 355 */         Field enumField = requiredType.getField(trimmedValue);
/* 356 */         convertedValue = enumField.get(null);
/*     */       }
/*     */       catch (Throwable ex) {
/* 359 */         if (logger.isTraceEnabled()) {
/* 360 */           logger.trace(new StringBuilder().append("Field [").append(convertedValue).append("] isn't an enum value").toString(), ex);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 365 */     return convertedValue;
/*     */   }
/*     */ 
/*     */   private PropertyEditor findDefaultEditor(Class<?> requiredType)
/*     */   {
/* 373 */     PropertyEditor editor = null;
/* 374 */     if (requiredType != null)
/*     */     {
/* 376 */       editor = this.propertyEditorRegistry.getDefaultEditor(requiredType);
/* 377 */       if ((editor == null) && (String.class != requiredType))
/*     */       {
/* 379 */         editor = BeanUtils.findEditorByConvention(requiredType);
/*     */       }
/*     */     }
/* 382 */     return editor;
/*     */   }
/*     */ 
/*     */   private Object doConvertValue(Object oldValue, Object newValue, Class<?> requiredType, PropertyEditor editor)
/*     */   {
/* 397 */     Object convertedValue = newValue;
/*     */ 
/* 399 */     if ((editor != null) && (!(convertedValue instanceof String)))
/*     */     {
/*     */       try
/*     */       {
/* 405 */         editor.setValue(convertedValue);
/* 406 */         Object newConvertedValue = editor.getValue();
/* 407 */         if (newConvertedValue != convertedValue) {
/* 408 */           convertedValue = newConvertedValue;
/*     */ 
/* 411 */           editor = null;
/*     */         }
/*     */       }
/*     */       catch (Exception ex) {
/* 415 */         if (logger.isDebugEnabled()) {
/* 416 */           logger.debug(new StringBuilder().append("PropertyEditor [").append(editor.getClass().getName()).append("] does not support setValue call").toString(), ex);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 422 */     Object returnValue = convertedValue;
/*     */ 
/* 424 */     if ((requiredType != null) && (!requiredType.isArray()) && ((convertedValue instanceof String[])))
/*     */     {
/* 428 */       if (logger.isTraceEnabled()) {
/* 429 */         logger.trace(new StringBuilder().append("Converting String array to comma-delimited String [").append(convertedValue).append("]").toString());
/*     */       }
/* 431 */       convertedValue = StringUtils.arrayToCommaDelimitedString((String[])convertedValue);
/*     */     }
/*     */ 
/* 434 */     if ((convertedValue instanceof String)) {
/* 435 */       if (editor != null)
/*     */       {
/* 437 */         if (logger.isTraceEnabled()) {
/* 438 */           logger.trace(new StringBuilder().append("Converting String to [").append(requiredType).append("] using property editor [").append(editor).append("]").toString());
/*     */         }
/* 440 */         String newTextValue = (String)convertedValue;
/* 441 */         return doConvertTextValue(oldValue, newTextValue, editor);
/*     */       }
/* 443 */       if (String.class == requiredType) {
/* 444 */         returnValue = convertedValue;
/*     */       }
/*     */     }
/*     */ 
/* 448 */     return returnValue;
/*     */   }
/*     */ 
/*     */   private Object doConvertTextValue(Object oldValue, String newTextValue, PropertyEditor editor)
/*     */   {
/*     */     try
/*     */     {
/* 460 */       editor.setValue(oldValue);
/*     */     }
/*     */     catch (Exception ex) {
/* 463 */       if (logger.isDebugEnabled()) {
/* 464 */         logger.debug(new StringBuilder().append("PropertyEditor [").append(editor.getClass().getName()).append("] does not support setValue call").toString(), ex);
/*     */       }
/*     */     }
/*     */ 
/* 468 */     editor.setAsText(newTextValue);
/* 469 */     return editor.getValue();
/*     */   }
/*     */ 
/*     */   private Object convertToTypedArray(Object input, String propertyName, Class<?> componentType) {
/* 473 */     if ((input instanceof Collection))
/*     */     {
/* 475 */       Collection coll = (Collection)input;
/* 476 */       Object result = Array.newInstance(componentType, coll.size());
/* 477 */       int i = 0;
/* 478 */       for (Iterator it = coll.iterator(); it.hasNext(); i++) {
/* 479 */         Object value = convertIfNecessary(
/* 480 */           buildIndexedPropertyName(propertyName, i), 
/* 480 */           null, it.next(), componentType);
/* 481 */         Array.set(result, i, value);
/*     */       }
/* 483 */       return result;
/*     */     }
/* 485 */     if (input.getClass().isArray())
/*     */     {
/* 487 */       if ((componentType.equals(input.getClass().getComponentType())) && 
/* 488 */         (!this.propertyEditorRegistry
/* 488 */         .hasCustomEditorForElement(componentType, propertyName)))
/*     */       {
/* 489 */         return input;
/*     */       }
/* 491 */       int arrayLength = Array.getLength(input);
/* 492 */       Object result = Array.newInstance(componentType, arrayLength);
/* 493 */       for (int i = 0; i < arrayLength; i++) {
/* 494 */         Object value = convertIfNecessary(
/* 495 */           buildIndexedPropertyName(propertyName, i), 
/* 495 */           null, Array.get(input, i), componentType);
/* 496 */         Array.set(result, i, value);
/*     */       }
/* 498 */       return result;
/*     */     }
/*     */ 
/* 502 */     Object result = Array.newInstance(componentType, 1);
/* 503 */     Object value = convertIfNecessary(
/* 504 */       buildIndexedPropertyName(propertyName, 0), 
/* 504 */       null, input, componentType);
/* 505 */     Array.set(result, 0, value);
/* 506 */     return result;
/*     */   }
/*     */ 
/*     */   private Collection<?> convertToTypedCollection(Collection<?> original, String propertyName, Class<?> requiredType, TypeDescriptor typeDescriptor)
/*     */   {
/* 514 */     if (!Collection.class.isAssignableFrom(requiredType)) {
/* 515 */       return original;
/*     */     }
/*     */ 
/* 518 */     boolean approximable = CollectionFactory.isApproximableCollectionType(requiredType);
/* 519 */     if ((!approximable) && (!canCreateCopy(requiredType))) {
/* 520 */       if (logger.isDebugEnabled()) {
/* 521 */         logger.debug(new StringBuilder().append("Custom Collection type [").append(original.getClass().getName()).append("] does not allow for creating a copy - injecting original Collection as-is").toString());
/*     */       }
/*     */ 
/* 524 */       return original;
/*     */     }
/*     */ 
/* 527 */     boolean originalAllowed = requiredType.isInstance(original);
/* 528 */     TypeDescriptor elementType = typeDescriptor.getElementTypeDescriptor();
/* 529 */     if ((elementType == null) && (originalAllowed) && 
/* 530 */       (!this.propertyEditorRegistry
/* 530 */       .hasCustomEditorForElement(null, propertyName)))
/*     */     {
/* 531 */       return original;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 536 */       Iterator it = original.iterator();
/* 537 */       if (it == null) {
/* 538 */         if (logger.isDebugEnabled()) {
/* 539 */           logger.debug(new StringBuilder().append("Collection of type [").append(original.getClass().getName()).append("] returned null Iterator - injecting original Collection as-is").toString());
/*     */         }
/*     */ 
/* 542 */         return original;
/*     */       }
/*     */     }
/*     */     catch (Throwable ex) {
/* 546 */       if (logger.isDebugEnabled()) {
/* 547 */         logger.debug(new StringBuilder().append("Cannot access Collection of type [").append(original.getClass().getName()).append("] - injecting original Collection as-is: ").append(ex).toString());
/*     */       }
/*     */ 
/* 550 */       return original;
/*     */     }
/*     */     Iterator it;
/*     */     try
/*     */     {
/*     */       Collection convertedCopy;
/* 555 */       if (approximable) {
/* 556 */         convertedCopy = CollectionFactory.createApproximateCollection(original, original.size());
/*     */       }
/*     */       else
/* 559 */         convertedCopy = (Collection)requiredType.newInstance();
/*     */     }
/*     */     catch (Throwable ex)
/*     */     {
/*     */       Collection convertedCopy;
/* 563 */       if (logger.isDebugEnabled()) {
/* 564 */         logger.debug(new StringBuilder().append("Cannot create copy of Collection type [").append(original.getClass().getName()).append("] - injecting original Collection as-is: ").append(ex).toString());
/*     */       }
/*     */ 
/* 567 */       return original;
/*     */     }
/*     */     Collection convertedCopy;
/* 570 */     for (int i = 0; 
/* 571 */       it.hasNext(); i++) {
/* 572 */       Object element = it.next();
/* 573 */       String indexedPropertyName = buildIndexedPropertyName(propertyName, i);
/* 574 */       Object convertedElement = convertIfNecessary(indexedPropertyName, null, element, elementType != null ? elementType
/* 575 */         .getType() : null, elementType);
/*     */       try {
/* 577 */         convertedCopy.add(convertedElement);
/*     */       }
/*     */       catch (Throwable ex) {
/* 580 */         if (logger.isDebugEnabled()) {
/* 581 */           logger.debug(new StringBuilder().append("Collection type [").append(original.getClass().getName()).append("] seems to be read-only - injecting original Collection as-is: ").append(ex).toString());
/*     */         }
/*     */ 
/* 584 */         return original;
/*     */       }
/* 586 */       originalAllowed = (originalAllowed) && (element == convertedElement);
/*     */     }
/* 588 */     return originalAllowed ? original : convertedCopy;
/*     */   }
/*     */ 
/*     */   private Map<?, ?> convertToTypedMap(Map<?, ?> original, String propertyName, Class<?> requiredType, TypeDescriptor typeDescriptor)
/*     */   {
/* 595 */     if (!Map.class.isAssignableFrom(requiredType)) {
/* 596 */       return original;
/*     */     }
/*     */ 
/* 599 */     boolean approximable = CollectionFactory.isApproximableMapType(requiredType);
/* 600 */     if ((!approximable) && (!canCreateCopy(requiredType))) {
/* 601 */       if (logger.isDebugEnabled()) {
/* 602 */         logger.debug(new StringBuilder().append("Custom Map type [").append(original.getClass().getName()).append("] does not allow for creating a copy - injecting original Map as-is").toString());
/*     */       }
/*     */ 
/* 605 */       return original;
/*     */     }
/*     */ 
/* 608 */     boolean originalAllowed = requiredType.isInstance(original);
/* 609 */     TypeDescriptor keyType = typeDescriptor.getMapKeyTypeDescriptor();
/* 610 */     TypeDescriptor valueType = typeDescriptor.getMapValueTypeDescriptor();
/* 611 */     if ((keyType == null) && (valueType == null) && (originalAllowed) && 
/* 612 */       (!this.propertyEditorRegistry
/* 612 */       .hasCustomEditorForElement(null, propertyName)))
/*     */     {
/* 613 */       return original;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 618 */       Iterator it = original.entrySet().iterator();
/* 619 */       if (it == null) {
/* 620 */         if (logger.isDebugEnabled()) {
/* 621 */           logger.debug(new StringBuilder().append("Map of type [").append(original.getClass().getName()).append("] returned null Iterator - injecting original Map as-is").toString());
/*     */         }
/*     */ 
/* 624 */         return original;
/*     */       }
/*     */     }
/*     */     catch (Throwable ex) {
/* 628 */       if (logger.isDebugEnabled()) {
/* 629 */         logger.debug(new StringBuilder().append("Cannot access Map of type [").append(original.getClass().getName()).append("] - injecting original Map as-is: ").append(ex).toString());
/*     */       }
/*     */ 
/* 632 */       return original;
/*     */     }
/*     */     Iterator it;
/*     */     try
/*     */     {
/*     */       Map convertedCopy;
/* 637 */       if (approximable) {
/* 638 */         convertedCopy = CollectionFactory.createApproximateMap(original, original.size());
/*     */       }
/*     */       else
/* 641 */         convertedCopy = (Map)requiredType.newInstance();
/*     */     }
/*     */     catch (Throwable ex)
/*     */     {
/*     */       Map convertedCopy;
/* 645 */       if (logger.isDebugEnabled()) {
/* 646 */         logger.debug(new StringBuilder().append("Cannot create copy of Map type [").append(original.getClass().getName()).append("] - injecting original Map as-is: ").append(ex).toString());
/*     */       }
/*     */ 
/* 649 */       return original;
/*     */     }
/*     */     Map convertedCopy;
/* 652 */     while (it.hasNext()) {
/* 653 */       Map.Entry entry = (Map.Entry)it.next();
/* 654 */       Object key = entry.getKey();
/* 655 */       Object value = entry.getValue();
/* 656 */       String keyedPropertyName = buildKeyedPropertyName(propertyName, key);
/* 657 */       Object convertedKey = convertIfNecessary(keyedPropertyName, null, key, keyType != null ? keyType
/* 658 */         .getType() : null, keyType);
/* 659 */       Object convertedValue = convertIfNecessary(keyedPropertyName, null, value, valueType != null ? valueType
/* 660 */         .getType() : null, valueType);
/*     */       try {
/* 662 */         convertedCopy.put(convertedKey, convertedValue);
/*     */       }
/*     */       catch (Throwable ex) {
/* 665 */         if (logger.isDebugEnabled()) {
/* 666 */           logger.debug(new StringBuilder().append("Map type [").append(original.getClass().getName()).append("] seems to be read-only - injecting original Map as-is: ").append(ex).toString());
/*     */         }
/*     */ 
/* 669 */         return original;
/*     */       }
/* 671 */       originalAllowed = (originalAllowed) && (key == convertedKey) && (value == convertedValue);
/*     */     }
/* 673 */     return originalAllowed ? original : convertedCopy;
/*     */   }
/*     */ 
/*     */   private String buildIndexedPropertyName(String propertyName, int index) {
/* 677 */     return propertyName != null ? new StringBuilder().append(propertyName).append("[").append(index).append("]").toString() : null;
/*     */   }
/*     */ 
/*     */   private String buildKeyedPropertyName(String propertyName, Object key)
/*     */   {
/* 683 */     return propertyName != null ? new StringBuilder().append(propertyName).append("[").append(key).append("]").toString() : null;
/*     */   }
/*     */ 
/*     */   private boolean canCreateCopy(Class<?> requiredType)
/*     */   {
/* 690 */     return (!requiredType.isInterface()) && (!Modifier.isAbstract(requiredType.getModifiers())) && 
/* 690 */       (Modifier.isPublic(requiredType
/* 690 */       .getModifiers())) && (ClassUtils.hasConstructor(requiredType, new Class[0]));
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  62 */       Class clazz = ClassUtils.forName("java.util.Optional", TypeConverterDelegate.class.getClassLoader());
/*  63 */       javaUtilOptionalEmpty = ClassUtils.getMethod(clazz, "empty", new Class[0]).invoke(null, new Object[0]);
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.TypeConverterDelegate
 * JD-Core Version:    0.6.2
 */