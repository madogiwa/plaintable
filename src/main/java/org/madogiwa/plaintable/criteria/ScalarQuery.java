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

import org.madogiwa.plaintable.criteria.value.ValueExpression;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class ScalarQuery implements IQuery {

	private Query query;

	/**
	 * @param source
	 */
	public ScalarQuery(ISource source, ValueExpression column, String alias) {
		query = new Query(source);
		query.getProjection().add(column, alias);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.criteria.IQuery#getProjection()
	 */
	public Projection getProjection() {
		return query.getProjection();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.madogiwa.plaintable.criteria.IQuery#getSQLString(org.madogiwa.plaintable
	 * .criteria.Context)
	 */
	public String getSQLString(Context context) {
		return query.getSQLString(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.criteria.IQuery#getSource()
	 */
	public ISource getSource() {
		return query.getSource();
	}

}
