/*
 * Copyright (c) 2008 Hidenori Sugiyama
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
package org.madogiwa.plaintable.criteria;

import java.io.Serializable;

import org.madogiwa.plaintable.criteria.bool.BooleanExpression;
import org.madogiwa.plaintable.criteria.value.ValueExpression;
import org.madogiwa.plaintable.schema.Column;
import org.madogiwa.plaintable.schema.Schema;

/**
 * @author Hidenori Sugiyama
 * 
 */
@SuppressWarnings("serial")
public class Query implements IQuery, Cloneable, Serializable {

	private ISource source;

	private Restriction restriction;

	private Order order;

	private Projection projection;

	/**
	 * @param schema
	 */
	public Query(Schema schema) {
		this(new TableSource(schema));
	}

	/**
	 * @param source
	 */
	public Query(ISource source) {
		this.source = source;
		projection = new Projection(source);
		restriction = new Restriction();
		order = new Order();
	}

	/**
	 * @return
	 */
	public ISource getSource() {
		return source;
	}

	/**
	 * @return
	 */
	public Restriction getRestriction() {
		return restriction;
	}

	/**
	 * @param restriction
	 * @return
	 */
	public void setRestriction(Restriction restriction) {
		this.restriction = restriction;
	}

	/**
	 * 
	 */
	public void clearRestriction() {
		restriction = null;
	}

	/**
	 * @param expr
	 * @return
	 */
	public Query add(BooleanExpression expr) {
		restriction.add(expr);
		return this;
	}

	/**
	 * @param expr
	 * @param alias
	 * @return
	 */
	public Query add(ValueExpression expr, String alias) {
		projection.add(expr, alias);
		return this;
	}

	/**
	 * @param column
	 * @return
	 */
	public Query ascendingOrder(Column column) {
		order.ascendingOrder(column);
		return this;
	}

	/**
	 * @param column
	 * @return
	 */
	public Query descendingOrder(Column column) {
		order.descendingOrder(column);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.criteria.ICriteria#getProjection()
	 */
	public Projection getProjection() {
		return projection;
	}

	/**
	 * @param projection
	 */
	public void setProjection(Projection projection) {
		this.projection = projection;
	}

	/**
	 * 
	 */
	public void clearProjection() {
		projection = new Projection(source);
	}

	/**
	 * @return
	 */
	public Order getOrder() {
		return order;
	}

	/**
	 * @param order
	 */
	public void setOrder(Order order) {
		this.order = order;
	}

	/**
	 * 
	 */
	public void clearOrder() {
		order = new Order();
	}

	/**
	 * @param context
	 * @return
	 */
	public String getSQLString(Context context) {
		StringBuilder sql = new StringBuilder();

		sql.append(String.format("SELECT %s ", projection.getSQLString(context)));
		sql.append(String.format("FROM %s ", source.getSQLString(context)));

		if (restriction.notEmpty()) {
			sql.append(String.format("WHERE %s ",
					restriction.getSQLString(context)));
		}
		if (projection.isGroupBy()) {
			sql.append(String.format("GROUP BY %s ", projection.getGroupBy()
					.getSQLString(context)));
		}
		if (projection.hasHaving()) {
			sql.append(String.format("HAVING %s ", projection.getHaving()
					.getSQLString(context)));
		}
		if (order.notEmpty()) {
			sql.append(String.format("ORDER BY %s ",
					order.getSQLString(context)));
		}

		return sql.toString();
	}

}
