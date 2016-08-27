/*    */ package org.springframework.beans.factory.xml;
/*    */ 
/*    */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*    */ import org.xml.sax.SAXException;
/*    */ import org.xml.sax.SAXParseException;
/*    */ 
/*    */ public class XmlBeanDefinitionStoreException extends BeanDefinitionStoreException
/*    */ {
/*    */   public XmlBeanDefinitionStoreException(String resourceDescription, String msg, SAXException cause)
/*    */   {
/* 45 */     super(resourceDescription, msg, cause);
/*    */   }
/*    */ 
/*    */   public int getLineNumber()
/*    */   {
/* 54 */     Throwable cause = getCause();
/* 55 */     if ((cause instanceof SAXParseException)) {
/* 56 */       return ((SAXParseException)cause).getLineNumber();
/*    */     }
/* 58 */     return -1;
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.xml.XmlBeanDefinitionStoreException
 * JD-Core Version:    0.6.2
 */