package de.objectcode.time4u.server.web.gwt.admin.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import de.objectcode.time4u.server.web.gwt.utils.client.service.BaseDataPage;
import de.objectcode.time4u.server.web.gwt.utils.client.service.IProjection;

public class TeamSummary implements IsSerializable {

	public static enum Projections implements IProjection<TeamSummary> {
		NAME("name", true) {
			public Object project(TeamSummary dto) {
				return dto.getName();
			}
		},
		DESCRIPTION("description", true) {
			public Object project(TeamSummary dto) {
				return dto.getDescription();
			}
		};

		private final String column;
		private final boolean sortable;

		private Projections(String column, boolean sortable) {
			this.column = column;
			this.sortable = sortable;
		}

		public String getColumn() {
			return column;
		}

		public boolean isSortable() {
			return sortable;
		}
	}

	public static class Page extends BaseDataPage<TeamSummary> {

		public Page() {

		}

		public Page(int pageNumber, int pageSize, int totalNumber,
				List<TeamSummary> pageData) {
			super(pageNumber, pageSize, totalNumber);

			this.pageData = pageData;
		}

		private List<TeamSummary> pageData;

		public List<TeamSummary> getPageData() {
			return pageData;
		}
	}

	private String id;
	private String name;
	private String description;

	public TeamSummary() {
	}

	public TeamSummary(String id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public int hashCode() {
		return ((id == null) ? 0 : id.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TeamSummary other = (TeamSummary) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
