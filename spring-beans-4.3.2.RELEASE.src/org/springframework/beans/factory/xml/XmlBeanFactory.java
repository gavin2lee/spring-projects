/*    */ package org.springframework.beans.factory.xml;
/*    */ 
/*    */ import org.springframework.beans.BeansException;
/*    */ import org.springframework.beans.factory.BeanFactory;
/*    */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*    */ import org.springframework.core.io.Resource;
/*    */ 
/*    */ @Deprecated
/*    */ public class XmlBeanFactory extends DefaultListableBeanFactory
/*    */ {
/* 57 */   private final XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(this);
/*    */ 
/*    */   public XmlBeanFactory(Resource resource)
/*    */     throws BeansException
/*    */   {
/* 67 */     this(resource, null);
/*    */   }
/*    */ 
/*    */   public XmlBeanFactory(Resource resource, BeanFactory parentBeanFactory)
/*    */     throws BeansException
/*    */   {
/* 78 */     super(parentBeanFactory);
/* 79 */     this.reader.loadBeanDefinitions(resource);
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.xml.XmlBeanFactory
 * JD-Core Version:    0.6.2
 */