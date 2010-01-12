package de.objectcode.time4u.server.web.gwt.login.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationException;
import org.springframework.security.AuthenticationManager;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import de.objectcode.time4u.server.web.gwt.login.client.LoginService;
import de.objectcode.time4u.server.web.gwt.utils.server.GwtController;

@Controller
@RequestMapping({"/LoginUI/login.service", "/MainUI/login.service", "/time4u-gwt/LoginUI/login.service", "/time4u-gwt/MainUI/login.service"})
public class LoginServiceImpl extends GwtController implements LoginService {

	private static final long serialVersionUID = 1L;

	private AuthenticationManager authenticationManager;

	public boolean login(String userId, String password) {
		UsernamePasswordAuthenticationToken tocken = new UsernamePasswordAuthenticationToken(
				userId, password);

		Authentication authResult;

		try {
			authResult = authenticationManager.authenticate(tocken);
		} catch (AuthenticationException e) {
			SecurityContextHolder.getContext().setAuthentication(null);

			return false;
		}

		SecurityContextHolder.getContext().setAuthentication(authResult);

		return true;
	}

	public void logout() {
		SecurityContextHolder.getContext().setAuthentication(null);
	}

	@Autowired
	public void setAuthenticationManager(
			AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

}
