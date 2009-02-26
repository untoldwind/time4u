package de.objectcode.time4u.client.ui.dnd;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.server.api.data.ProjectSummary;

public class ProjectTransfer extends ByteArrayTransfer
{
  private static ProjectTransfer INSTANCE = new ProjectTransfer();
  private static final String TYPE_NAME = "time4u-project-transfer-format";
  private static final int TYPEID = registerType(TYPE_NAME);

  public static ProjectTransfer getInstance()
  {
    return INSTANCE;
  }

  private ProjectTransfer()
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
      final byte[] bytes = ((ProjectSummary) object).getId().getBytes("UTF-8");

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

  protected ProjectSummary fromByteArray(final byte[] bytes)
  {
    try {
      final String projectId = new String(bytes, "UTF-8");

      return RepositoryFactory.getRepository().getProjectRepository().getProjectSummary(projectId);
    } catch (final Exception e) {
      return null;
    }
  }
}
