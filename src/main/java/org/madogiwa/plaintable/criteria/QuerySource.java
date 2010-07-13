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

import java.util.List;

import org.madogiwa.plaintable.Path;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class QuerySource extends Source {

	private IQuery query;

	private String alias;

	/**
	 * @param query
	 * @param alias
	 */
	public QuerySource(IQuery query, String alias) {
		this.query = query;
		this.alias = alias;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.criteria.Source#getAlias()
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
		return "(" + query.getSQLString(context) + ")";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.criteria.ISource#getPathList()
	 */
	public List<Path> getPathList() {
		return query.getSource().getPathList();
	}

}
