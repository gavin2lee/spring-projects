package org.springframework.beans.factory;

import org.springframework.beans.BeansException;

public abstract interface ObjectFactory<T>
{
  public abstract T getObject()
    throws BeansException;
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.ObjectFactory
 * JD-Core Version:    0.6.2
 */