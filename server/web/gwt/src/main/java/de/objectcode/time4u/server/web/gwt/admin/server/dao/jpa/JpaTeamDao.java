package de.objectcode.time4u.server.web.gwt.admin.server.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.objectcode.time4u.server.entities.TeamEntity;
import de.objectcode.time4u.server.web.gwt.admin.client.service.TeamSummary;
import de.objectcode.time4u.server.web.gwt.admin.server.dao.ITeamDao;
import de.objectcode.time4u.server.web.gwt.utils.server.JpaDaoBase;

@Repository("adminTeamDao")
@Transactional(propagation = Propagation.MANDATORY)
public class JpaTeamDao extends JpaDaoBase implements ITeamDao {

	@SuppressWarnings("unchecked")
	public TeamSummary.Page findTeamSummaryPage(int pageNumber, int pageSize,
			TeamSummary.Projections sorting, boolean ascending) {

		Query countQuery = entityManager.createQuery("select count(*) from "
				+ TeamEntity.class.getName() + " as t where t.deleted = false");

		long count = (Long) countQuery.getSingleResult();

		StringBuffer queryString = new StringBuffer("from ");
		queryString.append(TeamEntity.class.getName()).append(" as t");
		queryString.append(" where t.deleted = false");
		queryString.append(" order by");

		if (sorting.isSortable()) {
			queryString.append(" t.").append(sorting.getColumn()).append(
					ascending ? " asc," : " desc,");
		}
		queryString.append(" t.id asc");

		Query dataQuery = entityManager.createQuery(queryString.toString());

		dataQuery.setFirstResult(pageNumber * pageSize);
		dataQuery.setMaxResults(pageSize);

		List<TeamEntity> result = dataQuery.getResultList();

		List<TeamSummary> ret = new ArrayList<TeamSummary>();

		for (TeamEntity teamEntity : result) {
			ret.add(toDTOSummary(teamEntity));
		}

		return new TeamSummary.Page(pageNumber, pageSize, (int) count, ret);
	}

	static TeamSummary toDTOSummary(TeamEntity teamEntity) {
		return new TeamSummary(teamEntity.getId(), teamEntity.getName(),
				teamEntity.getDescription());
	}
}
