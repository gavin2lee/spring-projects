package org.springframework.beans.factory.parsing;

import java.util.EventListener;

public abstract interface ReaderEventListener extends EventListener
{
  public abstract void defaultsRegistered(DefaultsDefinition paramDefaultsDefinition);

  public abstract void componentRegistered(ComponentDefinition paramComponentDefinition);

  public abstract void aliasRegistered(AliasDefinition paramAliasDefinition);

  public abstract void importProcessed(ImportDefinition paramImportDefinition);
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.ReaderEventListener
 * JD-Core Version:    0.6.2
 */