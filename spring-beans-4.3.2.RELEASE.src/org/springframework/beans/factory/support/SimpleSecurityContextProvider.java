/*    */ package org.springframework.beans.factory.support;
/*    */ 
/*    */ import java.security.AccessControlContext;
/*    */ import java.security.AccessController;
/*    */ 
/*    */ public class SimpleSecurityContextProvider
/*    */   implements SecurityContextProvider
/*    */ {
/*    */   private final AccessControlContext acc;
/*    */ 
/*    */   public SimpleSecurityContextProvider()
/*    */   {
/* 39 */     this(null);
/*    */   }
/*    */ 
/*    */   public SimpleSecurityContextProvider(AccessControlContext acc)
/*    */   {
/* 50 */     this.acc = acc;
/*    */   }
/*    */ 
/*    */   public AccessControlContext getAccessControlContext()
/*    */   {
/* 56 */     return this.acc != null ? this.acc : AccessController.getContext();
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.support.SimpleSecurityContextProvider
 * JD-Core Version:    0.6.2
 */