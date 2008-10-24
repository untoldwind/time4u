package de.objectcode.time4u.client.connection.api;

public class ConnectionException extends Exception
{
  private static final long serialVersionUID = -8316370929959761039L;

  public ConnectionException()
  {
    super();
  }

  public ConnectionException(final String message, final Throwable cause)
  {
    super(message, cause);
  }

  public ConnectionException(final String message)
  {
    super(message);
  }

  public ConnectionException(final Throwable cause)
  {
    super(cause);
  }

}
