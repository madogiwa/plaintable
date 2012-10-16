package org.madogiwa.plaintable.schema;

import java.util.HashSet;
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

}
