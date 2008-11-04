package de.objectcode.time4u.client.ui.dnd;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.server.api.data.TodoSummary;

public class TodoTransfer extends ByteArrayTransfer
{
  private static TodoTransfer INSTANCE = new TodoTransfer();
  private static final String TYPE_NAME = "time4u-todo-transfer-format";
  private static final int TYPEID = registerType(TYPE_NAME);

  public static TodoTransfer getInstance()
  {
    return INSTANCE;
  }

  private TodoTransfer()
  {

  }

  @Override
  protected int[] getTypeIds()
  {
    return new int[] { TYPEID };
  }

  @Override
  protected String[] getTypeNames()
  {
    return new String[] { TYPE_NAME };
  }

  @Override
  protected void javaToNative(final Object object, final TransferData transferData)
  {
    try {
      final byte[] bytes = ((TodoSummary) object).getId().getBytes("UTF-8");

      if (bytes != null) {
        super.javaToNative(bytes, transferData);
      }
    } catch (final Exception e) {
    }
  }

  @Override
  protected Object nativeToJava(final TransferData transferData)
  {
    final byte[] bytes = (byte[]) super.nativeToJava(transferData);

    return fromByteArray(bytes);
  }

  protected TodoSummary fromByteArray(final byte[] bytes)
  {
    try {
      final String todoId = new String(bytes, "UTF-8");

      return RepositoryFactory.getRepository().getTodoRepository().getTodoSummary(todoId);
    } catch (final Exception e) {
      return null;
    }
  }
}
