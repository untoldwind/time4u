package de.objectcode.time4u.server.web.gwt.login.server;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.PasswordEncoder;

import de.objectcode.time4u.server.utils.IPasswordEncoder;

public class PasswordEndcoderAdapter implements PasswordEncoder {

	IPasswordEncoder passwordEncoder;
	boolean allowAnyPassword = false;

	public String encodePassword(String rawPassword, Object salt)
			throws DataAccessException {
		return passwordEncoder.encrypt(rawPassword.toCharArray());
	}

	public boolean isPasswordValid(String encPassword, String rawPassword,
			Object salt) throws DataAccessException {
		if (allowAnyPassword)
			return true;

		return passwordEncoder.verify(rawPassword.toCharArray(), encPassword);
	}

	@Required
	public void setPasswordEncoder(IPasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public void setAllowAnyPassword(boolean allowAnyPassword) {
		this.allowAnyPassword = allowAnyPassword;
	}

}
