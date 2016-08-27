package org.springframework.beans.factory.parsing;

import org.springframework.core.io.Resource;

public abstract interface SourceExtractor
{
  public abstract Object extractSource(Object paramObject, Resource paramResource);
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.SourceExtractor
 * JD-Core Version:    0.6.2
 */