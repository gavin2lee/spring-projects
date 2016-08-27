/*    */ package org.springframework.beans.factory.support;
/*    */ 
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ 
/*    */ public class GenericBeanDefinition extends AbstractBeanDefinition
/*    */ {
/*    */   private String parentName;
/*    */ 
/*    */   public GenericBeanDefinition()
/*    */   {
/*    */   }
/*    */ 
/*    */   public GenericBeanDefinition(BeanDefinition original)
/*    */   {
/* 65 */     super(original);
/*    */   }
/*    */ 
/*    */   public void setParentName(String parentName)
/*    */   {
/* 71 */     this.parentName = parentName;
/*    */   }
/*    */ 
/*    */   public String getParentName()
/*    */   {
/* 76 */     return this.parentName;
/*    */   }
/*    */ 
/*    */   public AbstractBeanDefinition cloneBeanDefinition()
/*    */   {
/* 82 */     return new GenericBeanDefinition(this);
/*    */   }
/*    */ 
/*    */   public boolean equals(Object other)
/*    */   {
/* 87 */     return (this == other) || (((other instanceof GenericBeanDefinition)) && (super.equals(other)));
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 92 */     StringBuilder sb = new StringBuilder("Generic bean");
/* 93 */     if (this.parentName != null) {
/* 94 */       sb.append(" with parent '").append(this.parentName).append("'");
/*    */     }
/* 96 */     sb.append(": ").append(super.toString());
/* 97 */     return sb.toString();
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.support.GenericBeanDefinition
 * JD-Core Version:    0.6.2
 */