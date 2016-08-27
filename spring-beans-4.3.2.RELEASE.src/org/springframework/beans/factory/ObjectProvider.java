package org.springframework.beans.factory;

import org.springframework.beans.BeansException;

public abstract interface ObjectProvider<T> extends ObjectFactory<T>
{
  public abstract T getObject(Object[] paramArrayOfObject)
    throws BeansException;

  public abstract T getIfAvailable()
    throws BeansException;

  public abstract T getIfUnique()
    throws BeansException;
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.ObjectProvider
 * JD-Core Version:    0.6.2
 */