/*    */ package org.springframework.beans.factory;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import org.springframework.util.StringUtils;
/*    */ 
/*    */ public class NoUniqueBeanDefinitionException extends NoSuchBeanDefinitionException
/*    */ {
/*    */   private int numberOfBeansFound;
/*    */   private Collection<String> beanNamesFound;
/*    */ 
/*    */   public NoUniqueBeanDefinitionException(Class<?> type, int numberOfBeansFound, String message)
/*    */   {
/* 47 */     super(type, message);
/* 48 */     this.numberOfBeansFound = numberOfBeansFound;
/*    */   }
/*    */ 
/*    */   public NoUniqueBeanDefinitionException(Class<?> type, Collection<String> beanNamesFound)
/*    */   {
/* 57 */     this(type, beanNamesFound.size(), "expected single matching bean but found " + beanNamesFound.size() + ": " + 
/* 58 */       StringUtils.collectionToCommaDelimitedString(beanNamesFound));
/*    */ 
/* 59 */     this.beanNamesFound = beanNamesFound;
/*    */   }
/*    */ 
/*    */   public NoUniqueBeanDefinitionException(Class<?> type, String[] beanNamesFound)
/*    */   {
/* 68 */     this(type, Arrays.asList(beanNamesFound));
/*    */   }
/*    */ 
/*    */   public int getNumberOfBeansFound()
/*    */   {
/* 79 */     return this.numberOfBeansFound;
/*    */   }
/*    */ 
/*    */   public Collection<String> getBeanNamesFound()
/*    */   {
/* 89 */     return this.beanNamesFound;
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.NoUniqueBeanDefinitionException
 * JD-Core Version:    0.6.2
 */