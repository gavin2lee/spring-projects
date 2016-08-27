package org.springframework.beans.factory.config;

public abstract interface SingletonBeanRegistry
{
  public abstract void registerSingleton(String paramString, Object paramObject);

  public abstract Object getSingleton(String paramString);

  public abstract boolean containsSingleton(String paramString);

  public abstract String[] getSingletonNames();

  public abstract int getSingletonCount();

  public abstract Object getSingletonMutex();
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.config.SingletonBeanRegistry
 * JD-Core Version:    0.6.2
 */