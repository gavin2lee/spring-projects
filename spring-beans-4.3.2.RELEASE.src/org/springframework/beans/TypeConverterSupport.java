/*    */ package org.springframework.beans;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.core.convert.ConversionException;
/*    */ import org.springframework.core.convert.ConverterNotFoundException;
/*    */ 
/*    */ public abstract class TypeConverterSupport extends PropertyEditorRegistrySupport
/*    */   implements TypeConverter
/*    */ {
/*    */   TypeConverterDelegate typeConverterDelegate;
/*    */ 
/*    */   public <T> T convertIfNecessary(Object value, Class<T> requiredType)
/*    */     throws TypeMismatchException
/*    */   {
/* 40 */     return doConvert(value, requiredType, null, null);
/*    */   }
/*    */ 
/*    */   public <T> T convertIfNecessary(Object value, Class<T> requiredType, MethodParameter methodParam)
/*    */     throws TypeMismatchException
/*    */   {
/* 47 */     return doConvert(value, requiredType, methodParam, null);
/*    */   }
/*    */ 
/*    */   public <T> T convertIfNecessary(Object value, Class<T> requiredType, Field field)
/*    */     throws TypeMismatchException
/*    */   {
/* 54 */     return doConvert(value, requiredType, null, field);
/*    */   }
/*    */ 
/*    */   private <T> T doConvert(Object value, Class<T> requiredType, MethodParameter methodParam, Field field) throws TypeMismatchException
/*    */   {
/*    */     try {
/* 60 */       if (field != null) {
/* 61 */         return this.typeConverterDelegate.convertIfNecessary(value, requiredType, field);
/*    */       }
/*    */ 
/* 64 */       return this.typeConverterDelegate.convertIfNecessary(value, requiredType, methodParam);
/*    */     }
/*    */     catch (ConverterNotFoundException ex)
/*    */     {
/* 68 */       throw new ConversionNotSupportedException(value, requiredType, ex);
/*    */     }
/*    */     catch (ConversionException ex) {
/* 71 */       throw new TypeMismatchException(value, requiredType, ex);
/*    */     }
/*    */     catch (IllegalStateException ex) {
/* 74 */       throw new ConversionNotSupportedException(value, requiredType, ex);
/*    */     }
/*    */     catch (Throwable ex) {
/* 77 */       throw new TypeMismatchException(value, requiredType, ex);
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.TypeConverterSupport
 * JD-Core Version:    0.6.2
 */