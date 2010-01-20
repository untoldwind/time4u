package de.objectcode.time4u.server.web.gwt.admin.server.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.web.gwt.admin.client.service.PersonSummary;
import de.objectcode.time4u.server.web.gwt.admin.server.dao.IPersonDao;
import de.objectcode.time4u.server.web.gwt.utils.server.JpaDaoBase;

@Repository("adminPersonDao")
@Transactional(propagation = Propagation.MANDATORY)
public class JpaPersonDao extends JpaDaoBase implements IPersonDao {

	@SuppressWarnings("unchecked")
	public PersonSummary.Page findPersonSummaryPage(int pageNumber, int pageSize,
			PersonSummary.Projections sorting, boolean ascending) {

		Query countQuery = entityManager.createQuery("select count(*) from "
				+ PersonEntity.class.getName()
				+ " as p where p.deleted = false");

		long count = (Long) countQuery.getSingleResult();

		StringBuffer queryString = new StringBuffer("from ");
		queryString.append(PersonEntity.class.getName()).append(" as p");
		queryString.append(" where p.deleted = false");
		queryString.append(" order by");

		if (sorting.isSortable()) {
			queryString.append(" p.").append(sorting.getColumn()).append(
					ascending ? " asc," : " desc,");
		}
		queryString.append(" p.id asc");

		Query dataQuery = entityManager.createQuery(queryString.toString());

		dataQuery.setFirstResult(pageNumber * pageSize);
		dataQuery.setMaxResults(pageSize);

		List<PersonEntity> result = dataQuery.getResultList();

		List<PersonSummary> ret = new ArrayList<PersonSummary>();

		for (PersonEntity personEntity : result) {
			ret.add(toDTOSummary(personEntity));
		}

		return new PersonSummary.Page(pageNumber, pageSize, (int) count, ret);
	}

	static PersonSummary toDTOSummary(PersonEntity personEntity) {
		return new PersonSummary(personEntity.getId(),
				personEntity.getActive() == null || personEntity.getActive(),
				personEntity.getGivenName(), personEntity.getSurname(),
				personEntity.getEmail(), personEntity.getLastSynchronize());
	}
}
