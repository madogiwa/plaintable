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
package org.madogiwa.plaintable;

import org.madogiwa.plaintable.criteria.Source;
import org.madogiwa.plaintable.criteria.TableSource;
import org.madogiwa.plaintable.schema.Column;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class Path {

	private String sourceAlias;

	private String columnName;

	public Path(Column column) {
		this(new TableSource(column.getSchema()), column.getName());
	}

	public Path(Source source, String columnName) {
		this(source.getAlias(), columnName);
	}

	public Path(String sourceAlias, String columnName) {
		this.sourceAlias = sourceAlias;
		this.columnName = columnName;
	}

	public String getPathString() {
		return sourceAlias + "." + columnName;
	}

	public String getSourceAlias() {
		return sourceAlias;
	}

	public String getColumnName() {
		return columnName;
	}

}
