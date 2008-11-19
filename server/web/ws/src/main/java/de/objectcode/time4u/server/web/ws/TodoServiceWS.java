package de.objectcode.time4u.server.web.ws;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.time4u.server.api.ITodoService;
import de.objectcode.time4u.server.api.data.FilterResult;
import de.objectcode.time4u.server.api.data.Todo;
import de.objectcode.time4u.server.api.data.TodoGroup;
import de.objectcode.time4u.server.api.data.TodoSummary;
import de.objectcode.time4u.server.api.filter.TodoFilter;

@WebService(targetNamespace = "http://objectcode.de/time4u/api/ws", endpointInterface = "de.objectcode.time4u.server.api.ITodoService")
@SOAPBinding(style = Style.RPC)
public class TodoServiceWS implements ITodoService
{
  private static final Log LOG = LogFactory.getLog(TodoServiceWS.class);

  ITodoService m_todoService;

  public TodoServiceWS() throws Exception
  {
    final InitialContext ctx = new InitialContext();

    m_todoService = (ITodoService) ctx.lookup("time4u-server/TodoService/remote");
  }

  public FilterResult<TodoSummary> getTodos(final TodoFilter filter)
  {
    if (LOG.isInfoEnabled()) {
      LOG.info("getTodos: " + filter);
    }

    return m_todoService.getTodos(filter);
  }

  public Todo storeTodo(final Todo todo)
  {
    if (LOG.isInfoEnabled()) {
      LOG.info("storeTodo: " + todo);
    }
    return m_todoService.storeTodo(todo);
  }

  public TodoGroup storeTodoGroup(final TodoGroup todoGroup)
  {
    if (LOG.isInfoEnabled()) {
      LOG.info("storeTodoGroup: " + todoGroup);
    }
    return m_todoService.storeTodoGroup(todoGroup);
  }

}
