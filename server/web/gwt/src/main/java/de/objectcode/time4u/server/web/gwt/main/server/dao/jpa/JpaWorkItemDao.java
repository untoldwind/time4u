package de.objectcode.time4u.server.web.gwt.main.server.dao.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.objectcode.time4u.server.entities.DayInfoEntity;
import de.objectcode.time4u.server.web.gwt.main.client.service.DayInfoSummary;
import de.objectcode.time4u.server.web.gwt.main.server.dao.IWorkItemDao;

@Transactional(propagation = Propagation.MANDATORY)
@Repository("workItemDao")
public class JpaWorkItemDao extends JpaDaoBase implements IWorkItemDao {

	@SuppressWarnings("unchecked")
	public List<DayInfoSummary> findDayInfoDTOSummary(String personId,
			Date start, Date end) {
		Query query = entityManager
				.createQuery("from "
						+ DayInfoEntity.class.getName()
						+ " d where d.person.id = :personId and d.date >= :start and d.date <= :end order by d.date asc");

		query.setParameter("personId", personId);
		query.setParameter("start", start);
		query.setParameter("end", end);

		List<DayInfoEntity> result = query.getResultList();

		List<DayInfoSummary> ret = new ArrayList<DayInfoSummary>();

		for (DayInfoEntity taskEntity : result) {
			ret.add(toDTOSummary(taskEntity));
		}

		return ret;
	}

	private DayInfoSummary toDTOSummary(DayInfoEntity dayInfoEntity) {
		return new DayInfoSummary(dayInfoEntity.getId(), dayInfoEntity
				.getDate(), dayInfoEntity.isHasWorkItems(), dayInfoEntity
				.isHasInvalidWorkItems(), dayInfoEntity.isHasTags(),
				dayInfoEntity.getRegularTime());
	}
}
