package de.objectcode.time4u.server.web.gwt.main.server.dao.jpa;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.support.JpaDaoSupport;

public class JpaDaoBase extends JpaDaoSupport {
	@PersistenceUnit
	@Required
	public void setEntityManagerFactoryAuto(EntityManagerFactory enityManagerFactory) {
		super.setEntityManagerFactory(enityManagerFactory);
	}

}
