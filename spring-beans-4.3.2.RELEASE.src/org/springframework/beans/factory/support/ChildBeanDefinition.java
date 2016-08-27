/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ public class ChildBeanDefinition extends AbstractBeanDefinition
/*     */ {
/*     */   private String parentName;
/*     */ 
/*     */   public ChildBeanDefinition(String parentName)
/*     */   {
/*  65 */     this.parentName = parentName;
/*     */   }
/*     */ 
/*     */   public ChildBeanDefinition(String parentName, MutablePropertyValues pvs)
/*     */   {
/*  74 */     super(null, pvs);
/*  75 */     this.parentName = parentName;
/*     */   }
/*     */ 
/*     */   public ChildBeanDefinition(String parentName, ConstructorArgumentValues cargs, MutablePropertyValues pvs)
/*     */   {
/*  87 */     super(cargs, pvs);
/*  88 */     this.parentName = parentName;
/*     */   }
/*     */ 
/*     */   public ChildBeanDefinition(String parentName, Class<?> beanClass, ConstructorArgumentValues cargs, MutablePropertyValues pvs)
/*     */   {
/* 102 */     super(cargs, pvs);
/* 103 */     this.parentName = parentName;
/* 104 */     setBeanClass(beanClass);
/*     */   }
/*     */ 
/*     */   public ChildBeanDefinition(String parentName, String beanClassName, ConstructorArgumentValues cargs, MutablePropertyValues pvs)
/*     */   {
/* 119 */     super(cargs, pvs);
/* 120 */     this.parentName = parentName;
/* 121 */     setBeanClassName(beanClassName);
/*     */   }
/*     */ 
/*     */   public ChildBeanDefinition(ChildBeanDefinition original)
/*     */   {
/* 130 */     super(original);
/*     */   }
/*     */ 
/*     */   public void setParentName(String parentName)
/*     */   {
/* 136 */     this.parentName = parentName;
/*     */   }
/*     */ 
/*     */   public String getParentName()
/*     */   {
/* 141 */     return this.parentName;
/*     */   }
/*     */ 
/*     */   public void validate() throws BeanDefinitionValidationException
/*     */   {
/* 146 */     super.validate();
/* 147 */     if (this.parentName == null)
/* 148 */       throw new BeanDefinitionValidationException("'parentName' must be set in ChildBeanDefinition");
/*     */   }
/*     */ 
/*     */   public AbstractBeanDefinition cloneBeanDefinition()
/*     */   {
/* 155 */     return new ChildBeanDefinition(this);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/* 160 */     if (this == other) {
/* 161 */       return true;
/*     */     }
/* 163 */     if (!(other instanceof ChildBeanDefinition)) {
/* 164 */       return false;
/*     */     }
/* 166 */     ChildBeanDefinition that = (ChildBeanDefinition)other;
/* 167 */     return (ObjectUtils.nullSafeEquals(this.parentName, that.parentName)) && (super.equals(other));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 172 */     return ObjectUtils.nullSafeHashCode(this.parentName) * 29 + super.hashCode();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 177 */     StringBuilder sb = new StringBuilder("Child bean with parent '");
/* 178 */     sb.append(this.parentName).append("': ").append(super.toString());
/* 179 */     return sb.toString();
/*     */   }
/*     */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.support.ChildBeanDefinition
 * JD-Core Version:    0.6.2
 */