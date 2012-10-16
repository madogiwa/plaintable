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

/**
 * @author Hidenori Sugiyama
 * 
 */
public class ReferenceKey extends KeyColumn {

	private SchemaReference target;

	private boolean nullable = false;

	private boolean cascade = false;

	/**
	 * @param table
	 * @param name
	 * @param target
	 */
	public ReferenceKey(Schema table, String name, SchemaReference target) {
		this(table, name, target, false);
	}

	/**
	 * @param table
	 * @param name
	 * @param target
	 * @param cascade
	 */
	public ReferenceKey(Schema table, String name, SchemaReference target,
			boolean cascade) {
		super(table, name);
		this.target = target;
		this.cascade = cascade;
	}

	/**
	 * @return the table
	 */
	public SchemaReference getTarget() {
		return target;
	}

	/**
	 * @param target
	 */
	public void setTarget(SchemaReference target) {
		this.target = target;
	}

	/**
	 * @return the nullable
	 */
	public boolean isNullable() {
		return nullable;
	}

	/**
	 * @param nullable
	 *            the nullable to set
	 */
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	/**
	 * @return the cascade
	 */
	public boolean isCascade() {
		return cascade;
	}

	/**
	 * @param cascade
	 *            the cascade to set
	 */
	public void setCascade(boolean cascade) {
		this.cascade = cascade;
	}

}
