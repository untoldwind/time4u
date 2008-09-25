package de.objectcode.time4u.client.ui;

/**
 * Interface defining the application's command IDs. Key bindings can be defined for specific commands. To associate an
 * action with a command, use IAction.setActionDefinitionId(commandId).
 * 
 * @see org.eclipse.jface.action.IAction#setActionDefinitionId(String)
 */
public interface ICommandIds
{
  String CMD_OPEN = "de.objectcode.time4u.client.open";
  String CMD_OPEN_MESSAGE = "de.objectcode.time4u.client.openMessage";

  String CMD_IMPORT = "de.objectcode.time4u.client.import";
  String CMD_EXPORT = "de.objectcode.time4u.client.export";
  String CMD_SYNCHRONIZE = "de.objectcode.time4u.client.synchronize";
  String CMD_SYNCHRONIZE_FULL = "de.objectcode.time4u.client.synchronize.full";
  String CMD_SYNCHRONIZE_NEXT = "de.objectcode.time4u.client.synchronize.next";
  String CMD_MIGRATE = "de.objectcode.time4u.client.migrate";

  String CMD_PUNCHOUT = "de.objectcode.time4u.client.punchOut";
  String CMD_PUNCHIN = "de.objectcode.time4u.client.punchIn";

  String CMD_PROJECT_EDIT = "de.objectcode.time4u.client.project.edit";
  String CMD_TASK_EDIT = "de.objectcode.time4u.client.task.edit";
  String CMD_WORKITEM_EDIT = "de.objectcode.time4u.client.workitem.edit";

  String CMD_TODO_PUNCHIN = "de.objectcode.time4u.client.todo.punchin";
}
