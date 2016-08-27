/*    */ package org.springframework.beans.factory.parsing;
/*    */ 
/*    */ import org.springframework.beans.BeanMetadataElement;
/*    */ import org.springframework.util.Assert;
/*    */ 
/*    */ public class AliasDefinition
/*    */   implements BeanMetadataElement
/*    */ {
/*    */   private final String beanName;
/*    */   private final String alias;
/*    */   private final Object source;
/*    */ 
/*    */   public AliasDefinition(String beanName, String alias)
/*    */   {
/* 44 */     this(beanName, alias, null);
/*    */   }
/*    */ 
/*    */   public AliasDefinition(String beanName, String alias, Object source)
/*    */   {
/* 54 */     Assert.notNull(beanName, "Bean name must not be null");
/* 55 */     Assert.notNull(alias, "Alias must not be null");
/* 56 */     this.beanName = beanName;
/* 57 */     this.alias = alias;
/* 58 */     this.source = source;
/*    */   }
/*    */ 
/*    */   public final String getBeanName()
/*    */   {
/* 66 */     return this.beanName;
/*    */   }
/*    */ 
/*    */   public final String getAlias()
/*    */   {
/* 73 */     return this.alias;
/*    */   }
/*    */ 
/*    */   public final Object getSource()
/*    */   {
/* 78 */     return this.source;
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.AliasDefinition
 * JD-Core Version:    0.6.2
 */