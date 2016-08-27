package org.springframework.beans.factory.parsing;

public abstract interface ProblemReporter
{
  public abstract void fatal(Problem paramProblem);

  public abstract void error(Problem paramProblem);

  public abstract void warning(Problem paramProblem);
}

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.ProblemReporter
 * JD-Core Version:    0.6.2
 */