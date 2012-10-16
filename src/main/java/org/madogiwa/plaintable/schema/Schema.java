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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class Schema implements Serializable {

	private String prefix;

	private String name;

	private PrimaryKey primaryKey;

	private Set<ReferenceKey> referenceKeySet = new HashSet<ReferenceKey>();

	private HashMap<String, AttributeColumn> attributeSet = new HashMap<String, AttributeColumn>();

	private Set<Index> indexSet = new HashSet<Index>();

	/**
	 * @param name
	 */
	public Schema(String name) {
		this("", name);
	}

	/**
	 * @param prefix
	 * @param name
	 */
	public Schema(String prefix, String name) {
		this.prefix = prefix;
		this.name = name;
	}

	/**
	 * @return
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public String getFullName() {
		return prefix + name;
	}

	/**
	 * @return
	 */
	public PrimaryKey getPrimaryKey() {
		return primaryKey;
	}

	/**
	 * @param key
	 */
	public void setPrimaryKey(PrimaryKey key) {
		this.primaryKey = key;
	}

	/**
	 * @param name
	 * @param target
	 */
	public void addReferenceKey(String name,
			Class<? extends Serializable> target) {
		addReferenceKey(name, target, false);
	}

	/**
	 * @param name
	 * @param target
	 * @param cascade
	 */
	public void addReferenceKey(String name,
			Class<? extends Serializable> target, boolean cascade) {
		addReferenceKey(new ReferenceKey(this, name,
				new SchemaReference(target), cascade));
	}

	/**
	 * @param key
	 */
	public void addReferenceKey(ReferenceKey key) {
		referenceKeySet.add(key);
	}

	/**
	 * @return
	 */
	public Set<ReferenceKey> getReferenceKeys() {
		return referenceKeySet;
	}

	/**
	 * @param name
	 * @return
	 */
	public boolean hasReferenceKey(String name) {
		for(ReferenceKey key : referenceKeySet) {
			if (key.getName().equals(name)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @param attr
	 */
	public void addAttribute(AttributeColumn attr) {
		attributeSet.put(attr.getName(), attr);
	}

	/**
	 * @param name
	 * @return
	 */
	public AttributeColumn getAttribute(String name) {
		return attributeSet.get(name);
	}

	/**
	 * @return
	 */
	public Set<AttributeColumn> getAttributes() {
		return new HashSet<AttributeColumn>(attributeSet.values());
	}

	/**
	 * @return
	 */
	public Set<Column> getColumns() {
		Set<Column> columnSet = new HashSet<Column>();
		columnSet.add(primaryKey);
		columnSet.addAll(referenceKeySet);
		columnSet.addAll(attributeSet.values());
		return columnSet;
	}

	/**
	 *
	 * @param index
	 */
	public void addIndex(Index index) {
		indexSet.add(index);
	}

	/**
	 *
	 * @return
	 */
	public Set<Index> getIndices() {
		return indexSet;
	}

}
