package de.objectcode.time4u.server.api;


/**
 * Generic service exception.
 * 
 * @author junglas
 */
public class ServiceException extends Exception
{
  private static final long serialVersionUID = 6056200409286638700L;

  public ServiceException()
  {
    super();
  }

  public ServiceException(final String message, final Throwable cause)
  {
    super(message, cause);
  }

  public ServiceException(final String message)
  {
    super(message);
  }

  public ServiceException(final Throwable cause)
  {
    super(cause);
  }

}
