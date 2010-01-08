package de.objectcode.time4u.server.web.gwt.login.server;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DataAccessException;
import org.springframework.security.providers.encoding.PasswordEncoder;

import de.objectcode.time4u.server.utils.IPasswordEncoder;

public class PasswordEndcoderAdapter implements PasswordEncoder {

	IPasswordEncoder passwordEncoder;

	public String encodePassword(String rawPassword, Object salt)
			throws DataAccessException {
		return passwordEncoder.encrypt(rawPassword.toCharArray());
	}

	public boolean isPasswordValid(String encPassword, String rawPassword,
			Object salt) throws DataAccessException {
		return passwordEncoder.verify(rawPassword.toCharArray(), encPassword);
	}

	@Required
	public void setPasswordEncoder(IPasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

}
