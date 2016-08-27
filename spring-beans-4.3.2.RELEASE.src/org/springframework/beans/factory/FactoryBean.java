package org.springframework.beans.factory;

public abstract interface FactoryBean<T>
{
  public abstract T getObject()
    throws Exception;

  public abstract Class<?> getObjectType();

  public abstract boolean isSingleton();
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.FactoryBean
 * JD-Core Version:    0.6.2
 */