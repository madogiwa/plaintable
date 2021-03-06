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
package org.madogiwa.plaintable.criteria;

import org.madogiwa.plaintable.Path;
import org.madogiwa.plaintable.schema.Column;
import org.madogiwa.plaintable.schema.Schema;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class TableSource extends Source {

	private Schema schema;

	private String alias;

	/**
	 * @param schema
	 */
	public TableSource(Schema schema) {
		this(schema, schema.getFullName());
	}

	/**
	 * @param schema
	 * @param alias
	 */
	public TableSource(Schema schema, String alias) {
		this.schema = schema;
		this.alias = alias;
	}

    public Schema getSchema() {
        return schema;
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.criteria.ISource#getAlias()
	 */
	public String getAlias() {
		return alias;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.madogiwa.plaintable.criteria.IExpression#getSQLString(org.madogiwa
	 * .plaintable.criteria.Context)
	 */
	public String getSQLString(Context context) {
		return String.format("%s AS %s", context.quote(schema.getFullName()), context.quote(getAlias()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.criteria.ISource#getPathList()
	 */
	public List<Path> getPathList() {
		List<Path> list = new ArrayList<Path>();
		for (Column column : schema.getColumns()) {
			list.add(new Path(alias, column.getName()));
		}
		return list;
	}

    public Schema getBaseSchema() {
        return schema;
    }

}
