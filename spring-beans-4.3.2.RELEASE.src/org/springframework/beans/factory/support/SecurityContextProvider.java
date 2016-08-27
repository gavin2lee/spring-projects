package org.springframework.beans.factory.support;

import java.security.AccessControlContext;

public abstract interface SecurityContextProvider
{
  public abstract AccessControlContext getAccessControlContext();
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.support.SecurityContextProvider
 * JD-Core Version:    0.6.2
 */