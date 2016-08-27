package org.springframework.beans.factory.access;

import org.springframework.beans.factory.BeanFactory;

public abstract interface BeanFactoryReference
{
  public abstract BeanFactory getFactory();

  public abstract void release();
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.access.BeanFactoryReference
 * JD-Core Version:    0.6.2
 */