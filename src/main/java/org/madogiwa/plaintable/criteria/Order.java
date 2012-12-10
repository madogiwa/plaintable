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

import org.madogiwa.plaintable.dialect.Dialect;
import org.madogiwa.plaintable.schema.Column;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class Order implements Serializable, Cloneable {

	private List<OrderItem> orderList = new ArrayList<OrderItem>();

	/**
	 * 
	 */
	public Order() {

	}

	/**
	 * @param column
	 * @return
	 */
	public Order ascendingOrder(Column column) {
		return add(column, true);
	}

	/**
	 * @param column
	 * @return
	 */
	public Order descendingOrder(Column column) {
		return add(column, false);
	}

	/**
	 * @param column
	 * @param ascending
	 * @return
	 */
	public Order add(Column column, boolean ascending) {
		orderList.add(new OrderItem(new TableSource(column.getSchema()),
				column, ascending));
		return this;
	}

	/**
	 * @param source
	 * @param column
	 * @param ascending
	 * @return
	 */
	public Order add(Source source, Column column, boolean ascending) {
		orderList.add(new OrderItem(source, column, ascending));
		return this;
	}

	public boolean notEmpty() {
		return (orderList.size() != 0) ? true : false;
	}

	/**
	 * @param context
	 * @return
	 */
	public String getSQLString(Context context) {
		StringBuffer sql = new StringBuffer();

		for (OrderItem order : orderList) {
			sql.append(order.getSQLString(context));
			sql.append(",");
		}
		sql.deleteCharAt(sql.lastIndexOf(","));

		return sql.toString();
	}

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();    //To change body of overridden methods use File | Settings | File Templates.
    }

	private class OrderItem implements Serializable, Cloneable {

		private Source source;

		private Column column;

		private boolean ascending;

		public OrderItem(Source source, Column column, boolean ascending) {
			this.source = source;
			this.column = column;
			this.ascending = ascending;
		}

		public String getSQLString(Context context) {
			return String.format("%s.%s %s", context.quote(source.getAlias()),
					context.quote(column.getName()), getOrderString());
		}

		private String getOrderString() {
			return (ascending) ? "ASC" : "DESC";
		}

	}

}
