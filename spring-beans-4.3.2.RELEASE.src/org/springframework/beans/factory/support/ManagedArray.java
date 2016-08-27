/*    */ package org.springframework.beans.factory.support;
/*    */ 
/*    */ import org.springframework.util.Assert;
/*    */ 
/*    */ public class ManagedArray extends ManagedList<Object>
/*    */ {
/*    */   volatile Class<?> resolvedElementType;
/*    */ 
/*    */   public ManagedArray(String elementTypeName, int size)
/*    */   {
/* 41 */     super(size);
/* 42 */     Assert.notNull(elementTypeName, "elementTypeName must not be null");
/* 43 */     setElementTypeName(elementTypeName);
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.support.ManagedArray
 * JD-Core Version:    0.6.2
 */