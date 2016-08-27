/*    */ package org.springframework.beans.factory.serviceloader;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
/*    */ import java.util.ServiceLoader;
/*    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*    */ 
/*    */ public class ServiceListFactoryBean extends AbstractServiceLoaderBasedFactoryBean
/*    */   implements BeanClassLoaderAware
/*    */ {
/*    */   protected Object getObjectToExpose(ServiceLoader<?> serviceLoader)
/*    */   {
/* 38 */     List result = new LinkedList();
/* 39 */     for (Iterator localIterator = serviceLoader.iterator(); localIterator.hasNext(); ) { Object loaderObject = localIterator.next();
/* 40 */       result.add(loaderObject);
/*    */     }
/* 42 */     return result;
/*    */   }
/*    */ 
/*    */   public Class<?> getObjectType()
/*    */   {
/* 47 */     return List.class;
/*    */   }
/*    */ }

/* Location:           D:\repo\org\springframework\spring-beans\4.3.2.RELEASE\spring-beans-4.3.2.RELEASE.jar
 * Qualified Name:     org.springframework.beans.factory.serviceloader.ServiceListFactoryBean
 * JD-Core Version:    0.6.2
 */