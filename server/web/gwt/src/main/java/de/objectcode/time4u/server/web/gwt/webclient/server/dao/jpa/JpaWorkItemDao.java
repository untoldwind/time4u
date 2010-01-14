package de.objectcode.time4u.server.web.gwt.webclient.server.dao.jpa;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.objectcode.time4u.server.entities.DayInfoEntity;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.TimePolicyEntity;
import de.objectcode.time4u.server.entities.WorkItemEntity;
import de.objectcode.time4u.server.web.gwt.utils.server.JpaDaoBase;
import de.objectcode.time4u.server.web.gwt.webclient.client.service.DayInfo;
import de.objectcode.time4u.server.web.gwt.webclient.client.service.DayInfoSummary;
import de.objectcode.time4u.server.web.gwt.webclient.client.service.WorkItem;
import de.objectcode.time4u.server.web.gwt.webclient.server.dao.IWorkItemDao;

@Repository("workItemDao")
@Transactional(propagation = Propagation.MANDATORY)
public class JpaWorkItemDao extends JpaDaoBase implements IWorkItemDao {

	@SuppressWarnings("unchecked")
	public List<DayInfoSummary> findDayInfoDTOSummary(final String personId,
			final Date start, final Date end) {
		Query dayInfoQuery = entityManager
				.createQuery("from "
						+ DayInfoEntity.class.getName()
						+ " d where d.person.id = :personId and d.date >= :start and d.date <= :end order by d.date asc");

		dayInfoQuery.setParameter("personId", personId);
		dayInfoQuery.setParameter("start", start);
		dayInfoQuery.setParameter("end", end);

		List<DayInfoEntity> result = dayInfoQuery.getResultList();

		List<TimePolicyEntity> timePolicies = findTimePolicies(personId);

		TreeMap<Long, DayInfoEntity> dayInfos = new TreeMap<Long, DayInfoEntity>();
		long minDate = Long.MAX_VALUE;
		long maxDate = Long.MIN_VALUE;

		for (final DayInfoEntity dayInfo : result) {
			dayInfos.put(dayInfo.getDate().getTime(), dayInfo);

			if (minDate > dayInfo.getDate().getTime()) {
				minDate = dayInfo.getDate().getTime();
			}
			if (maxDate < dayInfo.getDate().getTime()) {
				maxDate = dayInfo.getDate().getTime();
			}
		}

		List<DayInfoSummary> ret = new ArrayList<DayInfoSummary>();

		if (maxDate > minDate) {
			final Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(minDate);

			PersonEntity person = entityManager.find(PersonEntity.class, personId);
			
			while (calendar.getTimeInMillis() <= maxDate) {
				if (!dayInfos.containsKey(calendar.getTimeInMillis())) {
					final DayInfoEntity transientDayInfo = new DayInfoEntity(
							null, -1L, -1L, person, new java.sql.Date(calendar
									.getTimeInMillis()));

					ret.add(toDTOSummary(transientDayInfo, timePolicies));
				} else {
					ret.add(toDTOSummary(dayInfos.get(calendar
							.getTimeInMillis()), timePolicies));
				}
				calendar.add(Calendar.DAY_OF_MONTH, 1);
			}
		}

		return ret;
	}

	public DayInfo findDayInfoDTO(final String personId, final Date day) {
		Query query = entityManager.createQuery("from "
				+ DayInfoEntity.class.getName()
				+ " d where d.person.id = :personId and d.date = :day");

		query.setParameter("personId", personId);
		query.setParameter("day", day);

		try {
			DayInfoEntity dayInfoEntity = (DayInfoEntity) query
					.getSingleResult();

			List<TimePolicyEntity> timePolicies = findTimePolicies(personId);

			return toDTO(dayInfoEntity, timePolicies);
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<TimePolicyEntity> findTimePolicies(String personId) {
		Query timePolicyQuery = entityManager.createQuery("from "
				+ TimePolicyEntity.class.getName()
				+ " t where t.person.id = :personId order by t.id asc");

		timePolicyQuery.setParameter("personId", personId);

		return timePolicyQuery.getResultList();
	}

	static DayInfoSummary toDTOSummary(DayInfoEntity dayInfoEntity,
			List<TimePolicyEntity> timePolicies) {
		int calculatedRegularTime = dayInfoEntity.getRegularTime();

		if (calculatedRegularTime == -1) {
			for (TimePolicyEntity timePolicy : timePolicies) {
				calculatedRegularTime = timePolicy.getRegularTime(dayInfoEntity
						.getDate());

				if (calculatedRegularTime != -1)
					break;
			}
		}

		return new DayInfoSummary(dayInfoEntity.getId(), dayInfoEntity
				.getDate(), dayInfoEntity.isHasWorkItems(), dayInfoEntity
				.isHasInvalidWorkItems(), dayInfoEntity.isHasTags(),
				dayInfoEntity.getRegularTime(), calculatedRegularTime);
	}

	static DayInfo toDTO(DayInfoEntity dayInfoEntity,
			List<TimePolicyEntity> timePolicies) {
		int calculatedRegularTime = dayInfoEntity.getRegularTime();
		if (calculatedRegularTime == -1) {
			for (TimePolicyEntity timePolicy : timePolicies) {
				calculatedRegularTime = timePolicy.getRegularTime(dayInfoEntity
						.getDate());

				if (calculatedRegularTime != -1)
					break;
			}
		}

		List<WorkItem> workItems = new ArrayList<WorkItem>();

		for (WorkItemEntity workItem : dayInfoEntity.getWorkItems().values()) {
			workItems.add(toDTO(workItem));
		}

		return new DayInfo(dayInfoEntity.getId(), dayInfoEntity.getDate(),
				dayInfoEntity.isHasWorkItems(), dayInfoEntity
						.isHasInvalidWorkItems(), dayInfoEntity.isHasTags(),
				dayInfoEntity.getRegularTime(), calculatedRegularTime,
				workItems);
	}

	static WorkItem toDTO(WorkItemEntity workItemEntity) {
		return new WorkItem(workItemEntity.getId(), JpaProjectDao.toDTO(
				workItemEntity.getProject(), 0), JpaTaskDao
				.toDTO(workItemEntity.getTask()), workItemEntity.getBegin(),
				workItemEntity.getEnd(), workItemEntity.getComment(),
				workItemEntity.isValid(),
				workItemEntity.getTodo() != null ? workItemEntity.getTodo()
						.getId() : null);
	}
}
