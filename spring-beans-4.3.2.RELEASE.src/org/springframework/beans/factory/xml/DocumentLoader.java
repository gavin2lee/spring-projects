package org.springframework.beans.factory.xml;

import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;

public abstract interface DocumentLoader
{
  public abstract Document loadDocument(InputSource paramInputSource, EntityResolver paramEntityResolver, ErrorHandler paramErrorHandler, int paramInt, boolean paramBoolean)
    throws Exception;
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.xml.DocumentLoader
 * JD-Core Version:    0.6.2
 */