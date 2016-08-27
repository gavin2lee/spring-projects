package org.springframework.beans;

import java.lang.reflect.Field;
import org.springframework.core.MethodParameter;

public abstract interface TypeConverter
{
  public abstract <T> T convertIfNecessary(Object paramObject, Class<T> paramClass)
    throws TypeMismatchException;

  public abstract <T> T convertIfNecessary(Object paramObject, Class<T> paramClass, MethodParameter paramMethodParameter)
    throws TypeMismatchException;

  public abstract <T> T convertIfNecessary(Object paramObject, Class<T> paramClass, Field paramField)
    throws TypeMismatchException;
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.TypeConverter
 * JD-Core Version:    0.6.2
 */