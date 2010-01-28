package de.objectcode.time4u.server.web.gwt.login.client;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UserAccountInfo implements IsSerializable{
	private String userId;
	private String givenName;
	private String surname;
	private String email;
	
	public UserAccountInfo() {		
	}
	
	public UserAccountInfo(String userId, String givenName, String surname,
			String email) {
		this.userId = userId;
		this.givenName = givenName;
		this.surname = surname;
		this.email = email;
	}

	public String getUserId() {
		return userId;
	}

	public String getGivenName() {
		return givenName;
	}

	public String getSurname() {
		return surname;
	}

	public String getEmail() {
		return email;
	}
	
}
