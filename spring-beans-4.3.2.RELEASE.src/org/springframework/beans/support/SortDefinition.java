package org.springframework.beans.support;

public abstract interface SortDefinition
{
  public abstract String getProperty();

  public abstract boolean isIgnoreCase();

  public abstract boolean isAscending();
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.support.SortDefinition
 * JD-Core Version:    0.6.2
 */