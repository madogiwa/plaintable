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

import org.madogiwa.plaintable.Path;
import org.madogiwa.plaintable.criteria.value.*;
import org.madogiwa.plaintable.schema.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class Projection implements Serializable, Cloneable {

	private List<ProjectionItem> columnList = new ArrayList<ProjectionItem>();

	private GroupBy groupBy = new GroupBy();

	private Having having = new Having();

	private boolean distinct = false;

	private ISource source;

	public Projection(ISource source) {
		this.source = source;
	}

	public Projection add(NumericAttribute column) {
		add(new NumericColumnReference(column),
				new Path(column).getPathString());
		return this;
	}

	public Projection add(TextAttribute column) {
		add(new StringColumnReference(column), new Path(column).getPathString());
		return this;
	}

	public Projection add(DateTimeAttribute column) {
		add(new DateTimeColumnReference(column),
				new Path(column).getPathString());
		return this;
	}

	public Projection add(BinaryAttribute column) {
		add(new BinaryColumnReference(new TableSource(column.getSchema()),
				column.getName()), new Path(column).getPathString());
		return this;
	}

	public Projection add(KeyColumn column) {
		add(new KeyColumnReference(column), new Path(column).getPathString());
		return this;
	}

	public Projection add(ValueExpression expr, String alias) {
		columnList.add(new ProjectionItem(expr, alias));
		return this;
	}

	public void addAll(Schema schema) {
		for (Column column : schema.getColumns()) {
			if (NumericAttribute.class.isAssignableFrom(column.getClass())) {
				add((NumericAttribute) column);
			} else if (TextAttribute.class.isAssignableFrom(column.getClass())) {
				add((TextAttribute) column);
			} else if (DateTimeAttribute.class.isAssignableFrom(column
					.getClass())) {
				add((DateTimeAttribute) column);
			} else if (BinaryAttribute.class
					.isAssignableFrom(column.getClass())) {
				add((BinaryAttribute) column);
			} else if (KeyColumn.class.isAssignableFrom(column.getClass())) {
				add((KeyColumn) column);
			}
		}
	}

	public Having getHaving() {
		return having;
	}

	public GroupBy getGroupBy() {
		return groupBy;
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	public boolean isGroupBy() {
		return groupBy.isGroupBy();
	}

	public boolean isSingleColumn() {
		return (columnList.size() == 1) ? true : false;
	}

	public boolean isDistinct() {
		return distinct;
	}

	public boolean hasHaving() {
		return having.hasHaving();
	}

	public String getAlias(int index) {
		return columnList.get(index).alias;
	}

	public String getSQLString(Context context) {
		StringBuilder sql = new StringBuilder();

		if (columnList.size() != 0) {
			for (ProjectionItem item : columnList) {
				sql.append(String.format(" %s AS %s,", item.expr
						.getSQLString(context),
						context.quote(item.alias)));
			}
			sql.deleteCharAt(sql.lastIndexOf(","));
		} else {
			for (Path path : source.getPathList()) {
				sql.append(String.format(" %s AS %s,", context.quotePath(path.getPathString()),
						context.quote(path.getPathString())));
			}
			sql.deleteCharAt(sql.lastIndexOf(","));
		}

		return sql.toString();
	}

    public List<String> getPathList() {
        List<String> pathList = new ArrayList<String>();

        if (columnList.size() != 0) {
            for (ProjectionItem item : columnList) {
                pathList.add(item.alias.toLowerCase());
            }
        } else {
            for (Path path : source.getPathList()) {
                pathList.add(path.getPathString().toLowerCase());
            }
        }

        return pathList;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();    //To change body of overridden methods use File | Settings | File Templates.
    }

    private class ProjectionItem {

		public ValueExpression expr;

		public String alias;

		/**
		 * @param expr
		 * @param alias
		 */
		public ProjectionItem(ValueExpression expr, String alias) {

			this.expr = expr;
			this.alias = alias;

		}

	}

}
