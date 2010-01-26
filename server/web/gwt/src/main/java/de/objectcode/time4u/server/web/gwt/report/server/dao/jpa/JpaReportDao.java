package de.objectcode.time4u.server.web.gwt.report.server.dao.jpa;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.el.ELContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.objectcode.time4u.server.ejb.seam.api.report.BaseReportDefinition;
import de.objectcode.time4u.server.ejb.seam.api.report.ColumnDefinition;
import de.objectcode.time4u.server.ejb.seam.api.report.ColumnType;
import de.objectcode.time4u.server.ejb.seam.api.report.IReportDataCollector;
import de.objectcode.time4u.server.ejb.seam.api.report.ReportResult;
import de.objectcode.time4u.server.ejb.seam.api.report.parameter.BaseParameterValue;
import de.objectcode.time4u.server.ejb.seam.impl.DayInfoRowDataIterator;
import de.objectcode.time4u.server.ejb.seam.impl.DayInfoRowDataIteratorWithFill;
import de.objectcode.time4u.server.ejb.seam.impl.IRowDataIterator;
import de.objectcode.time4u.server.ejb.seam.impl.TodoRowDataIterator;
import de.objectcode.time4u.server.ejb.seam.impl.WorkItemRowDataIterator;
import de.objectcode.time4u.server.ejb.util.ReportEL;
import de.objectcode.time4u.server.entities.DayInfoEntity;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.TeamEntity;
import de.objectcode.time4u.server.entities.TodoEntity;
import de.objectcode.time4u.server.entities.WorkItemEntity;
import de.objectcode.time4u.server.web.gwt.report.client.service.ReportColumnDefinition;
import de.objectcode.time4u.server.web.gwt.report.client.service.ReportColumnType;
import de.objectcode.time4u.server.web.gwt.report.client.service.ReportTableData;
import de.objectcode.time4u.server.web.gwt.report.server.dao.IReportDao;
import de.objectcode.time4u.server.web.gwt.utils.server.JpaDaoBase;

@Repository("reportDao")
@Transactional(propagation = Propagation.MANDATORY)
public class JpaReportDao extends JpaDaoBase implements IReportDao {

	public ReportTableData generateReport(
			BaseReportDefinition reportDefinition,
			Map<String, BaseParameterValue> parameters, String personId) {

		ReportResult reportResult = doGenerateReport(reportDefinition,
				parameters, personId);

		List<ReportColumnDefinition> columns = new ArrayList<ReportColumnDefinition>();

		for (ColumnDefinition columnDefinition : reportResult.getColumns()) {
			columns.add(transform(columnDefinition));
		}

		List<ReportColumnDefinition> groupByColumns = new ArrayList<ReportColumnDefinition>();

		for (ColumnDefinition columnDefinition : reportResult
				.getGroupByColumns()) {
			groupByColumns.add(transform(columnDefinition));
		}

		// TODO:

		return new ReportTableData(reportResult.getName(), columns,
				groupByColumns);
	}

	@SuppressWarnings("unchecked")
	protected ReportResult doGenerateReport(
			final BaseReportDefinition reportDefinition,
			final Map<String, BaseParameterValue> parameters, String personId) {
		final Set<String> allowedPersonIds = new HashSet<String>();

		PersonEntity person = entityManager.find(PersonEntity.class, personId);

		allowedPersonIds.add(person.getId());
		for (final TeamEntity team : person.getResponsibleFor()) {
			for (final PersonEntity member : team.getMembers()) {
				allowedPersonIds.add(member.getId());
			}
		}

		final StringBuffer queryStr = new StringBuffer();
		String orderStr = null;
		IRowDataIterator<?> rowDataIterator = null;
		switch (reportDefinition.getEntityType()) {
		case WORKITEM:
			queryStr.append("from ");
			queryStr.append(WorkItemEntity.class.getName());
			queryStr
					.append(" w join fetch w.dayInfo where w.dayInfo.person.id in (:allowedPersons)");
			orderStr = " order by w.dayInfo.date asc, w.begin asc";
			rowDataIterator = new WorkItemRowDataIterator();
			break;
		case DAYINFO:
			queryStr.append("select distinct d from ");
			queryStr.append(DayInfoEntity.class.getName());
			queryStr
					.append(" d left outer join fetch d.tags left outer join fetch d.workItems join fetch d.person where d.person.id in (:allowedPersons)");
			orderStr = " order by d.date asc";
			if (reportDefinition.isFill()) {
				rowDataIterator = new DayInfoRowDataIteratorWithFill();
			} else {
				rowDataIterator = new DayInfoRowDataIterator();
			}
			break;
		case TODO:
			queryStr.append("from ");
			queryStr.append(TodoEntity.class.getName());
			queryStr.append(" t where t.reporter.id in (:allowedPersons)");
			orderStr = " order by t.header asc";
			rowDataIterator = new TodoRowDataIterator();
			break;
		}

		final ELContext context = ReportEL.createELContext();

		for (final Map.Entry<String, BaseParameterValue> parameter : parameters
				.entrySet()) {
			context.getVariableMapper().setVariable(
					parameter.getKey(),
					ReportEL.getExpressionFactory().createValueExpression(
							parameter.getValue(), Object.class));
		}

		if (reportDefinition.getFilter() != null) {
			queryStr.append(" and ");
			queryStr.append(reportDefinition.getFilter().getWhereClause(
					reportDefinition.getEntityType(), parameters, context));
		}

		queryStr.append(orderStr);

		final Query query = entityManager.createQuery(queryStr.toString());

		query.setParameter("allowedPersons", allowedPersonIds);
		if (reportDefinition.getFilter() != null) {
			reportDefinition.getFilter().setQueryParameters(
					reportDefinition.getEntityType(), query, parameters,
					context);
		}

		final IReportDataCollector dataCollector = reportDefinition
				.createDataCollector();

		rowDataIterator.iterate(query.getResultList(), dataCollector);

		entityManager.clear();

		return dataCollector.getReportResult();
	}

	protected static ReportColumnDefinition transform(
			ColumnDefinition columnDefinition) {
		return new ReportColumnDefinition(transform(columnDefinition
				.getColumnType()), columnDefinition.getHeader(),
				columnDefinition.getIndex());
	}

	protected static ReportColumnType transform(ColumnType columnType) {
		switch (columnType) {
		case DATE:
			return ReportColumnType.DATE;
		case DESCRIPTION:
			return ReportColumnType.DESCRIPTION;
		case INTEGER:
			return ReportColumnType.INTEGER;
		case TIME:
			return ReportColumnType.TIME;
		case NAME:
			return ReportColumnType.NAME;
		case NAME_ARRAY:
			return ReportColumnType.NAME_ARRAY;
		case TIMESTAMP:
			return ReportColumnType.TIMESTAMP;
		}
		throw new RuntimeException("Invalid column type:" + columnType);
	}
}