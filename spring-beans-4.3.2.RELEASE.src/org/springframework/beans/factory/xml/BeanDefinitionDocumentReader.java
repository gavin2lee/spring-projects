package org.springframework.beans.factory.xml;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.w3c.dom.Document;

public abstract interface BeanDefinitionDocumentReader
{
  public abstract void registerBeanDefinitions(Document paramDocument, XmlReaderContext paramXmlReaderContext)
    throws BeanDefinitionStoreException;
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.xml.BeanDefinitionDocumentReader
 * JD-Core Version:    0.6.2
 */