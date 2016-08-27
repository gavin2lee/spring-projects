package org.springframework.beans.factory.xml;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.w3c.dom.Node;

public abstract interface BeanDefinitionDecorator
{
  public abstract BeanDefinitionHolder decorate(Node paramNode, BeanDefinitionHolder paramBeanDefinitionHolder, ParserContext paramParserContext);
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.xml.BeanDefinitionDecorator
 * JD-Core Version:    0.6.2
 */