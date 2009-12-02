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
package org.madogiwa.plaintable.criteria;

import java.util.ArrayList;
import java.util.List;

import org.madogiwa.plaintable.criteria.bool.BooleanExpression;

/**
 * @author Hidenori Sugiyama
 *
 */
public class Restriction implements Cloneable {

	private List<BooleanExpression> exprList = new ArrayList<BooleanExpression>();

	/**
	 * @param expr
	 */
	public Restriction() {

	}

	/**
	 * @param expr
	 * @return
	 */
	public Restriction add(BooleanExpression expr) {
		exprList.add(expr);
		return this;
	}

	public boolean notEmpty() {
		return (exprList.size() != 0) ? true : false;
	}

	public String getSQLString(Context context) {
		StringBuilder sql = new StringBuilder();

		if (exprList.size() != 0) {
			sql.append(exprList.get(0).getSQLString(context));
			for(int i = 1; i < exprList.size(); i++) {
				sql.append(" AND ");
				sql.append(exprList.get(i).getSQLString(context));
			}
		}

		return sql.toString();
	}

}
