package de.objectcode.time4u.server.web.gwt.main.server.dao.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Required;

public class JpaDaoBase {
	protected EntityManager entityManager;
	
	@PersistenceContext
	@Required
	public void setEntityManagerFactoryAuto(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
