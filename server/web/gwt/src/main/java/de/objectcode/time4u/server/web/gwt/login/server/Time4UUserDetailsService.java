package de.objectcode.time4u.server.web.gwt.login.server;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;

public class Time4UUserDetailsService extends JpaDaoSupport implements UserDetailsService {

	public UserDetails loadUserByUsername(String userId)
			throws UsernameNotFoundException, DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

}
