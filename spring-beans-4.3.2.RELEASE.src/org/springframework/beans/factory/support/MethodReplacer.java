package org.springframework.beans.factory.support;

import java.lang.reflect.Method;

public abstract interface MethodReplacer
{
  public abstract Object reimplement(Object paramObject, Method paramMethod, Object[] paramArrayOfObject)
    throws Throwable;
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.support.MethodReplacer
 * JD-Core Version:    0.6.2
 */