package org.springframework.beans.factory.access;

import org.springframework.beans.BeansException;

public abstract interface BeanFactoryLocator
{
  public abstract BeanFactoryReference useBeanFactory(String paramString)
    throws BeansException;
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.access.BeanFactoryLocator
 * JD-Core Version:    0.6.2
 */