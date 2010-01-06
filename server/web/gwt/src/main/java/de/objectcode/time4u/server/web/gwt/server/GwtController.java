package de.objectcode.time4u.server.web.gwt.server;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class GwtController extends RemoteServiceServlet implements Controller,
		ServletContextAware {
	private static final long serialVersionUID = -2752883309003572211L;

	private ServletContext context;

	/**
	 * Implements Spring Controller interface method.
	 * 
	 * Call GWT's RemoteService doPost() method and return null.
	 * 
	 * @param request
	 *            current HTTP request
	 * @param response
	 *            current HTTP response
	 * @return a ModelAndView to render, or null if handled directly
	 * @throws Exception
	 *             in case of errors
	 */
	@Transactional
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		doPost(request, response);
		return null; // response handled by GWT RPC over XmlHttpRequest
	}

	public void setServletContext(ServletContext context) {
		this.context = context;
	}

	public ServletContext getServletContext() {
		return context;
	}

}
