package org.springframework.beans;

public abstract interface Mergeable
{
  public abstract boolean isMergeEnabled();

  public abstract Object merge(Object paramObject);
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.Mergeable
 * JD-Core Version:    0.6.2
 */