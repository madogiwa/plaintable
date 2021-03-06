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
package org.madogiwa.plaintable.schema;

import java.io.Serializable;

/**
 * @author Hidenori Sugiyama
 * 
 */
public abstract class Column implements Serializable {

	private Schema schema;

	private String name;

	/**
	 * @param schema
	 * @param name
	 */
	public Column(Schema schema, String name) {
		this.schema = schema;
		this.name = name;
	}

	/**
	 * @return
	 */
	public Schema getSchema() {
		return schema;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return
	 */
	public String getPath() {
		return schema.getFullName() + "." + name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Column column = (Column) o;

		if (schema == null || column.schema == null) return false;
		if (!schema.getFullName().equals(column.schema.getFullName())) return false;

		if (name != null ? !name.equals(column.name) : column.name != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = schema != null ? schema.getFullName().hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		return result;
	}

}
