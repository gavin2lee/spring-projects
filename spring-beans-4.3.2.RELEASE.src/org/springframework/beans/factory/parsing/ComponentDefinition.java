package org.springframework.beans.factory.parsing;

import org.springframework.beans.BeanMetadataElement;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanReference;

public abstract interface ComponentDefinition extends BeanMetadataElement
{
  public abstract String getName();

  public abstract String getDescription();

  public abstract BeanDefinition[] getBeanDefinitions();

  public abstract BeanDefinition[] getInnerBeanDefinitions();

  public abstract BeanReference[] getBeanReferences();
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.ComponentDefinition
 * JD-Core Version:    0.6.2
 */