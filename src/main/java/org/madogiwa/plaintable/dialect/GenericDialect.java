package org.madogiwa.plaintable.dialect;

import org.madogiwa.plaintable.schema.ReferenceKey;
import org.madogiwa.plaintable.schema.SchemaReference;

/**
 * User: sugiyama
 * To change this template use File | Settings | File Templates.
 */
public abstract class GenericDialect implements Dialect {

	public String buildForeignKeyConstraint(ReferenceKey key) {
		StringBuilder query = new StringBuilder();

		query.append(String.format(" %s %s ", quote(key.getName()), getSQLType(ReferenceKey.class, -1)));
		query.append(key.isNullable() ? "" : " NOT NULL ");

		SchemaReference schemaRef = key.getTarget();
		query.append(String.format(
			" REFERENCES %s(%s) ",
			quote(schemaRef.getSchema().getFullName()),
			quote(schemaRef.getSchema().getPrimaryKey().getName())));
		query.append(key.isCascade() ? " ON DELETE CASCADE " : "");

		return query.toString();
	}

}
