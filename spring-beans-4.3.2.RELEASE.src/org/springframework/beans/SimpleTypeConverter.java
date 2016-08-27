/*    */ package org.springframework.beans;
/*    */ 
/*    */ public class SimpleTypeConverter extends TypeConverterSupport
/*    */ {
/*    */   public SimpleTypeConverter()
/*    */   {
/* 36 */     this.typeConverterDelegate = new TypeConverterDelegate(this);
/* 37 */     registerDefaultEditors();
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.SimpleTypeConverter
 * JD-Core Version:    0.6.2
 */