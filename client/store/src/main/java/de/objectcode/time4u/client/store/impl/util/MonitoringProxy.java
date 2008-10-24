package de.objectcode.time4u.client.store.impl.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MonitoringProxy
{
  private final static boolean DEBUG = false;

  @SuppressWarnings("unchecked")
  public static <T> T getMonitoringProxy(final Class<T> apiInterface, final T implementation)
  {
    if (DEBUG) {
      return (T) Proxy.newProxyInstance(MonitoringProxy.class.getClassLoader(), new Class[] { apiInterface },
          new MonitoringInvocationHandler<T>(implementation));
    }
    return implementation;
  }

  private static class MonitoringInvocationHandler<T> implements InvocationHandler
  {
    T m_implementation;

    public MonitoringInvocationHandler(final T implementation)
    {
      m_implementation = implementation;
    }

    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable
    {
      final long start = System.currentTimeMillis();

      try {
        return method.invoke(m_implementation, args);
      } finally {
        System.out.println(method.getName() + " : " + (System.currentTimeMillis() - start) + " ms");
      }
    }
  }
}
