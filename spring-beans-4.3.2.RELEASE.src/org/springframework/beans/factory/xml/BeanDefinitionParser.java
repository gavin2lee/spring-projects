package org.springframework.beans.factory.xml;

import org.springframework.beans.factory.config.BeanDefinition;
import org.w3c.dom.Element;

public abstract interface BeanDefinitionParser
{
  public abstract BeanDefinition parse(Element paramElement, ParserContext paramParserContext);
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.xml.BeanDefinitionParser
 * JD-Core Version:    0.6.2
 */