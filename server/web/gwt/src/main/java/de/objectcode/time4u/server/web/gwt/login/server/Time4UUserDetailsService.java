package de.objectcode.time4u.server.web.gwt.login.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import de.objectcode.time4u.server.entities.account.UserAccountEntity;
import de.objectcode.time4u.server.entities.account.UserRoleEntity;

public class Time4UUserDetailsService extends JpaDaoSupport implements
		UserDetailsService {

	@Transactional(readOnly = false)
	public UserDetails loadUserByUsername(final String userId)
			throws UsernameNotFoundException, DataAccessException {
		return getJpaTemplate().execute(new JpaCallback<UserDetails>() {

			public UserDetails doInJpa(EntityManager entityManager)
					throws PersistenceException {
				Query query = entityManager.createQuery("from "
						+ UserAccountEntity.class.getName()
						+ " a where a.userId = :userId");

				query.setParameter("userId", userId);

				UserAccountEntity account = (UserAccountEntity) query
						.getSingleResult();

				if (account != null) {
					return new Time4UUserDetails(account);
				}
				return null;
			}
		});
	}

	public static class Time4UUserDetails implements UserDetails {
		private static final long serialVersionUID = 1L;

		boolean active;
		String personId;
		String givenName;
		String surname;
		String email;
		UserAccountEntity userAccountEntity;
		List<GrantedAuthority> grantedAuthorities;

		public Time4UUserDetails(UserAccountEntity userAccountEntity) {
			this.personId = userAccountEntity.getPerson().getId();
			this.active = !userAccountEntity.getPerson().isDeleted() && (userAccountEntity.getPerson().getActive() != null ? userAccountEntity.getPerson().getActive() : true);
			this.givenName = userAccountEntity.getPerson().getGivenName();
			this.surname = userAccountEntity.getPerson().getSurname();
			this.email = userAccountEntity.getPerson().getEmail();
			this.userAccountEntity = userAccountEntity;
			this.grantedAuthorities = new ArrayList<GrantedAuthority>();

			for (UserRoleEntity role : userAccountEntity.getRoles()) {
				this.grantedAuthorities.add( new GrantedAuthorityImpl("ROLE_" + role
						.getRoleId().toUpperCase()));
			}
		}

		public String getPersonId() {
			return personId;
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


		public Collection<GrantedAuthority> getAuthorities() {
			return grantedAuthorities;
		}

		public String getPassword() {
			return userAccountEntity.getHashedPassword();
		}

		public String getUsername() {
			return userAccountEntity.getUserId();
		}

		public boolean isAccountNonExpired() {
			return true;
		}

		public boolean isAccountNonLocked() {
			return active;
		}

		public boolean isCredentialsNonExpired() {
			return true;
		}

		public boolean isEnabled() {
			return true;
		}
	}
}
