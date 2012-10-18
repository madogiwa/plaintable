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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class Schema implements Serializable {

	private String prefix;

	private String name;

	private PrimaryKey primaryKey;

	private Set<ReferenceKey> referenceKeySet = new TreeSet<ReferenceKey>(new Comparator<ReferenceKey>() {
		public int compare(ReferenceKey o1, ReferenceKey o2) {
			return o1.getName().compareTo(o2.getName());
		}
	});

	private Map<String, AttributeColumn> attributeSet = new TreeMap<String, AttributeColumn>(new Comparator<String>() {
		public int compare(String o1, String o2) {
			return o1.compareTo(o2);
		}
	});

	private Set<Index> indexSet = new TreeSet<Index>(new Comparator<Index>() {
		public int compare(Index o1, Index o2) {
			return o1.getColumnsAsString().compareTo(o2.getColumnsAsString());
		}
	});

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

		// sort
		List<ReferenceKey> list = new ArrayList<ReferenceKey>(referenceKeySet);
		Collections.sort(list, new Comparator<ReferenceKey>() {
			public int compare(ReferenceKey o1, ReferenceKey o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		referenceKeySet = new HashSet<ReferenceKey>(list);
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
	public ReferenceKey getReferenceKey(String name) {
		for(ReferenceKey key : referenceKeySet) {
			if (key.getName().equals(name)) {
				return key;
			}
		}

		return null;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Schema schema = (Schema) o;

		if (attributeSet != null ? !attributeSet.equals(schema.attributeSet) : schema.attributeSet != null)
			return false;
		if (indexSet != null ? !indexSet.equals(schema.indexSet) : schema.indexSet != null) return false;
		if (name != null ? !name.equals(schema.name) : schema.name != null) return false;
		if (prefix != null ? !prefix.equals(schema.prefix) : schema.prefix != null) return false;
		if (primaryKey != null ? !primaryKey.equals(schema.primaryKey) : schema.primaryKey != null) return false;
		if (referenceKeySet != null ? !referenceKeySet.equals(schema.referenceKeySet) : schema.referenceKeySet != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = prefix != null ? prefix.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (primaryKey != null ? primaryKey.hashCode() : 0);
		result = 31 * result + (referenceKeySet != null ? referenceKeySet.hashCode() : 0);
		result = 31 * result + (attributeSet != null ? attributeSet.hashCode() : 0);
		result = 31 * result + (indexSet != null ? indexSet.hashCode() : 0);
		return result;
	}
}
