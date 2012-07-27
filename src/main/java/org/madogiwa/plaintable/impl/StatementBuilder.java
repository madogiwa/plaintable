/*
 * Copyright (c) 2009 Hidenori Sugiyama
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * 
 */
package org.madogiwa.plaintable.impl;

import org.madogiwa.plaintable.PlainTableException;
import org.madogiwa.plaintable.criteria.Context;
import org.madogiwa.plaintable.criteria.IQuery;
import org.madogiwa.plaintable.criteria.Restriction;
import org.madogiwa.plaintable.criteria.Window;
import org.madogiwa.plaintable.criteria.value.ValueExpression;
import org.madogiwa.plaintable.dialect.Dialect;
import org.madogiwa.plaintable.provider.RowProvider;
import org.madogiwa.plaintable.schema.Column;
import org.madogiwa.plaintable.schema.Schema;

import java.util.Map;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class StatementBuilder {

	private Dialect dialect;

	/**
	 * @param dialect
	 */
	public StatementBuilder(Dialect dialect) {
		this.dialect = dialect;
	}

	/**
	 * @param schema
	 * @throws PlainTableException
	 */
	public String buildLockSql(Schema schema) throws PlainTableException {
		return String.format("SELECT * FROM %s WHERE name LIKE ? FOR UPDATE",
				DatabaseSchemaImpl.LOCK_TABLE);
	}

	/**
	 * @param context
	 * @param query
	 * @param window
	 * @return
	 * @throws PlainTableException
	 */
	protected String buildSelectSql(Context context, IQuery query, Window window)
			throws PlainTableException {
		String sql = query.getSQLString(context);
		if (dialect.isLimitSupported()) {
			sql = sql
					+ " "
					+ dialect.getLimitFragment(window.getOffset(),
							window.getLimit());
		}
		return context.replaceMarker(sql);
	}

	/**
	 * @param context
	 * @param schema
	 * @param provider
	 * @return
	 */
	public String buildInsertSql(Context context, Schema schema,
			RowProvider provider) {
		StringBuilder sql = new StringBuilder();
		sql.append(String.format("INSERT INTO %s ",
				dialect.quote(schema.getName())));

		sql.append(" ( ");
		Map<Column, ValueExpression> map = provider.getMap();
		for (Column column : map.keySet()) {
			sql.append(String.format(" %s,", dialect.quote(column.getName())));
		}
		sql.deleteCharAt(sql.lastIndexOf(","));
		sql.append(" ) ");

		sql.append(" VALUES ( ");
		for (ValueExpression value : map.values()) {
			sql.append(String.format(" %s,", value.getSQLString(context)));
		}
		sql.deleteCharAt(sql.lastIndexOf(","));
		sql.append(" ) ");

		return context.replaceMarker(sql.toString());
	}

	/**
	 * @param context
	 * @param schema
	 * @param restriction
	 * @param provider
	 * @return
	 */
	public String buildUpdateSql(Context context, Schema schema,
			Restriction restriction, RowProvider provider) {
		StringBuilder sql = new StringBuilder();
		sql.append(String.format("UPDATE %s ",
				dialect.quote(schema.getName())));
		sql.append(" SET ");

		Map<Column, ValueExpression> map = provider.getMap();
		for (Column column : map.keySet()) {
			sql.append(String.format(" %s = %s,", column.getPath(),
					map.get(column).getSQLString(context)));
		}
		sql.deleteCharAt(sql.lastIndexOf(","));

		if (restriction.notEmpty()) {
			sql.append(" WHERE " + restriction.getSQLString(context));
		}

		return context.replaceMarker(sql.toString());
	}

	/**
	 * @param context
	 * @param schema
	 * @param restriction
	 * @return
	 */
	public String buildDeleteSql(Context context, Schema schema,
			Restriction restriction) {
		StringBuilder sql = new StringBuilder();
		sql.append(String.format("DELETE FROM %s ", schema.getName()));

		if (restriction.notEmpty()) {
			sql.append(String.format(" WHERE %s ",
					restriction.getSQLString(context)));
		}

		return context.replaceMarker(sql.toString());
	}

}
