package org.springframework.beans.factory;

public abstract interface SmartFactoryBean<T> extends FactoryBean<T>
{
  public abstract boolean isPrototype();

  public abstract boolean isEagerInit();
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.SmartFactoryBean
 * JD-Core Version:    0.6.2
 */