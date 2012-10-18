package org.madogiwa.plaintable.schema;

import org.madogiwa.plaintable.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Hidenori Sugiyama
 *
 */
public class Index {

	private Schema schema;

	private boolean unique = false;

	private Set<Column> columns  = new HashSet<Column>();

	public Index(Schema schema, Column column) {
		this.schema = schema;
		addColumn(column);
	}

	public void addColumn(Column column) {
		columns.add(column);
	}

	public Set<Column> getColumns() {
		return columns;
	}

	public String getColumnsAsString() {
		List<String> list = new ArrayList<String>();
		for(Column column : columns) {
			list.add(column.getName());
		}
		return StringUtils.join(list);
	}

	/**
	 * @return the unique
	 */
	public boolean isUnique() {
		return unique;
	}

	/**
	 * @param unique
	 *            the unique to set
	 */
	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Index index = (Index) o;

		if (schema == null || index.schema == null) return false;
		if (!schema.getFullName().equals(index.schema.getFullName())) return false;

		if (unique != index.unique) return false;
		if (columns != null ? !columns.equals(index.columns) : index.columns != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = (unique ? 1 : 0);
		result = 31 * result + (columns != null ? columns.hashCode() : 0);
		return result;
	}
}
