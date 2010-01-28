package de.objectcode.time4u.server.web.gwt.report.client.service;

import com.google.gwt.user.client.rpc.IsSerializable;

public class IdLabelPair implements IsSerializable {
	String id;
	String label;

	public IdLabelPair() {
	}

	public IdLabelPair(String id, String label) {
		this.id = id;
		this.label = label;
	}

	public String getId() {
		return id;
	}

	public String getLabel() {
		return label;
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
		IdLabelPair other = (IdLabelPair) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
