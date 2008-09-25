package de.objectcode.time4u.client.ui.dnd;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.server.api.data.Project;
import de.objectcode.time4u.server.api.data.Task;

public class TaskTransfer extends ByteArrayTransfer
{
  private static TaskTransfer INSTANCE = new TaskTransfer();
  private static final String TYPE_NAME = "time4u-task-transfer-format";
  private static final int TYPEID = registerType(TYPE_NAME);

  public static TaskTransfer getInstance()
  {
    return INSTANCE;
  }

  private TaskTransfer()
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
    final byte[] bytes = toByteArray((ProjectTask) object);
    if (bytes != null) {
      super.javaToNative(bytes, transferData);
    }
  }

  @Override
  protected Object nativeToJava(final TransferData transferData)
  {
    final byte[] bytes = (byte[]) super.nativeToJava(transferData);
    return fromByteArray(bytes);
  }

  protected byte[] toByteArray(final ProjectTask projectTask)
  {
    final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
    final DataOutputStream out = new DataOutputStream(byteOut);

    byte[] bytes = null;

    try {
      out.writeLong(projectTask.getProject().getId());
      out.writeLong(projectTask.getTask().getId());
      out.close();
      bytes = byteOut.toByteArray();
    } catch (final IOException e) {
    }
    return bytes;
  }

  protected ProjectTask fromByteArray(final byte[] bytes)
  {
    final DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));

    try {
      final long projectId = in.readLong();
      final long taskId = in.readLong();
      return new ProjectTask(RepositoryFactory.getRepository().getProjectRepository().getProject(projectId),
          RepositoryFactory.getRepository().getTaskRepository().getTask(taskId));
    } catch (final Exception e) {
      return null;
    }
  }

  public static class ProjectTask
  {
    private final Project m_project;
    private final Task m_task;

    public ProjectTask(final Project project, final Task task)
    {
      m_project = project;
      m_task = task;
    }

    public Project getProject()
    {
      return m_project;
    }

    public Task getTask()
    {
      return m_task;
    }

  }
}
