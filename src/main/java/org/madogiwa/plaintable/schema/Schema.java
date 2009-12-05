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
import java.util.HashSet;
import java.util.Set;

import org.madogiwa.plaintable.criteria.Context;

/**
 * @author Hidenori Sugiyama
 *
 */
public class Schema implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;

	private long version;

	private SyntheticKey syntheticKey;

	private Set<ReferenceKey> referenceKeySet = new HashSet<ReferenceKey>();

	private Set<AttributeColumn> attributeSet = new HashSet<AttributeColumn>();

	/**
	 * @param name
	 * @param version
	 */
	public Schema(String name, long version) {
		this.name = name;
		this.version = version;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the version
	 */
	public long getVersion() {
		return version;
	}

	/**
	 * @return
	 */
	public SyntheticKey getSyntheticKey() {
		return syntheticKey;
	}

	/**
	 * @param name
	 */
	public void setSyntheticKey(String name) {
		setSyntheticKey(new SyntheticKey(this, name));
	}

	/**
	 * @param key
	 */
	public void setSyntheticKey(SyntheticKey key) {
		this.syntheticKey = key;
	}

	/**
	 * @param name
	 * @param target
	 */
	public void addReferenceKey(String name, Class<? extends Serializable> target) {
		addReferenceKey(name, target, false);
	}

	/**
	 * @param name
	 * @param target
	 * @param cascade
	 */
	public void addReferenceKey(String name, Class<? extends Serializable> target, boolean cascade) {
		addReferenceKey(new ReferenceKey(this, name, new SchemaReference(target), cascade));
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
	 * @param attr
	 */
	public void addAttribute(AttributeColumn attr) {
		attributeSet.add(attr);
	}

	/**
	 * @return
	 */
	public Set<AttributeColumn> getAttributes() {
		return attributeSet;
	}

	/**
	 * @return
	 */
	public Set<Column> getColumns() {
		Set<Column> columnSet = new HashSet<Column>();
		columnSet.add(syntheticKey);
		columnSet.addAll(referenceKeySet);
		columnSet.addAll(attributeSet);
		return columnSet;
	}

	/* (non-Javadoc)
	 * @see org.madogiwa.plaintable.criteria.ISource#getAlias()
	 */
	public String getAlias() {
		return getName();
	}

	/* (non-Javadoc)
	 * @see org.madogiwa.plaintable.criteria.ISource#getSQLString(org.madogiwa.plaintable.criteria.CriteriaContext)
	 */
	public String getSQLString(Context context) {
		return " " + getName() + " ";
	}

}
