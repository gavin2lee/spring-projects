/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ public class TypeMismatchException extends PropertyAccessException
/*     */ {
/*     */   public static final String ERROR_CODE = "typeMismatch";
/*     */   private transient Object value;
/*     */   private Class<?> requiredType;
/*     */ 
/*     */   public TypeMismatchException(PropertyChangeEvent propertyChangeEvent, Class<?> requiredType)
/*     */   {
/*  49 */     this(propertyChangeEvent, requiredType, null);
/*     */   }
/*     */ 
/*     */   public TypeMismatchException(PropertyChangeEvent propertyChangeEvent, Class<?> requiredType, Throwable cause)
/*     */   {
/*  59 */     super(propertyChangeEvent, new StringBuilder().append("Failed to convert property value of type [")
/*  61 */       .append(ClassUtils.getDescriptiveType(propertyChangeEvent
/*  61 */       .getNewValue())).append("]")
/*  63 */       .append(requiredType != null ? new StringBuilder().append(" to required type [")
/*  63 */       .append(ClassUtils.getQualifiedName(requiredType))
/*  63 */       .append("]").toString() : "")
/*  65 */       .append(propertyChangeEvent
/*  64 */       .getPropertyName() != null ? new StringBuilder().append(" for property '")
/*  65 */       .append(propertyChangeEvent
/*  65 */       .getPropertyName()).append("'").toString() : "").toString(), cause);
/*     */ 
/*  67 */     this.value = propertyChangeEvent.getNewValue();
/*  68 */     this.requiredType = requiredType;
/*     */   }
/*     */ 
/*     */   public TypeMismatchException(Object value, Class<?> requiredType)
/*     */   {
/*  77 */     this(value, requiredType, null);
/*     */   }
/*     */ 
/*     */   public TypeMismatchException(Object value, Class<?> requiredType, Throwable cause)
/*     */   {
/*  87 */     super(new StringBuilder().append("Failed to convert value of type [").append(ClassUtils.getDescriptiveType(value)).append("]")
/*  88 */       .append(requiredType != null ? new StringBuilder().append(" to required type [")
/*  88 */       .append(ClassUtils.getQualifiedName(requiredType))
/*  88 */       .append("]").toString() : "").toString(), cause);
/*     */ 
/*  90 */     this.value = value;
/*  91 */     this.requiredType = requiredType;
/*     */   }
/*     */ 
/*     */   public Object getValue()
/*     */   {
/* 100 */     return this.value;
/*     */   }
/*     */ 
/*     */   public Class<?> getRequiredType()
/*     */   {
/* 107 */     return this.requiredType;
/*     */   }
/*     */ 
/*     */   public String getErrorCode()
/*     */   {
/* 112 */     return "typeMismatch";
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.TypeMismatchException
 * JD-Core Version:    0.6.2
 */