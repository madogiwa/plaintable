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
package org.madogiwa.plaintable.criteria.list;

import org.madogiwa.plaintable.criteria.Context;
import org.madogiwa.plaintable.criteria.Query;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class KeyListQuery implements KeyListExpression {

	private Query query;

	public KeyListQuery(Query query) {
		this.query = query;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.madogiwa.plaintable.criteria.Expression#getSQLString(org.madogiwa
	 * .plaintable.criteria.CriteriaContext)
	 */
	public String getSQLString(Context context) {
		return String.format("(%s)", query.getSQLString(context));
	}

}
