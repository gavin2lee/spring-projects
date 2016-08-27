/*    */ package org.springframework.beans;
/*    */ 
/*    */ import org.springframework.core.NestedRuntimeException;
/*    */ import org.springframework.util.ObjectUtils;
/*    */ 
/*    */ public abstract class BeansException extends NestedRuntimeException
/*    */ {
/*    */   public BeansException(String msg)
/*    */   {
/* 40 */     super(msg);
/*    */   }
/*    */ 
/*    */   public BeansException(String msg, Throwable cause)
/*    */   {
/* 50 */     super(msg, cause);
/*    */   }
/*    */ 
/*    */   public boolean equals(Object other)
/*    */   {
/* 56 */     if (this == other) {
/* 57 */       return true;
/*    */     }
/* 59 */     if (!(other instanceof BeansException)) {
/* 60 */       return false;
/*    */     }
/* 62 */     BeansException otherBe = (BeansException)other;
/*    */ 
/* 64 */     return (getMessage().equals(otherBe.getMessage())) && 
/* 64 */       (ObjectUtils.nullSafeEquals(getCause(), otherBe.getCause()));
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 69 */     return getMessage().hashCode();
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.BeansException
 * JD-Core Version:    0.6.2
 */