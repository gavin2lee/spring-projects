package org.springframework.beans.factory;

public abstract interface DisposableBean
{
  public abstract void destroy()
    throws Exception;
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.DisposableBean
 * JD-Core Version:    0.6.2
 */