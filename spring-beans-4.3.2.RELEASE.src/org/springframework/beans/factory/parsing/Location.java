/*    */ package org.springframework.beans.factory.parsing;
/*    */ 
/*    */ import org.springframework.core.io.Resource;
/*    */ import org.springframework.util.Assert;
/*    */ 
/*    */ public class Location
/*    */ {
/*    */   private final Resource resource;
/*    */   private final Object source;
/*    */ 
/*    */   public Location(Resource resource)
/*    */   {
/* 47 */     this(resource, null);
/*    */   }
/*    */ 
/*    */   public Location(Resource resource, Object source)
/*    */   {
/* 57 */     Assert.notNull(resource, "Resource must not be null");
/* 58 */     this.resource = resource;
/* 59 */     this.source = source;
/*    */   }
/*    */ 
/*    */   public Resource getResource()
/*    */   {
/* 67 */     return this.resource;
/*    */   }
/*    */ 
/*    */   public Object getSource()
/*    */   {
/* 77 */     return this.source;
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.Location
 * JD-Core Version:    0.6.2
 */