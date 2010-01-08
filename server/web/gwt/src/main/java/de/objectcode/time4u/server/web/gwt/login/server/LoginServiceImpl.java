package de.objectcode.time4u.server.web.gwt.login.server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import de.objectcode.time4u.server.web.gwt.login.client.LoginService;
import de.objectcode.time4u.server.web.gwt.utils.server.GwtController;

@Controller
@RequestMapping("/LoginUI/login.service")
public class LoginServiceImpl extends GwtController implements LoginService {

	private static final long serialVersionUID = 1L;

	public boolean login(String userId, String password) {
		return true;
	}


}
