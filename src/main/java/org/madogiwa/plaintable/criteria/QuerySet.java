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

import org.madogiwa.plaintable.Path;
import org.madogiwa.plaintable.schema.Schema;

import java.util.List;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class QuerySet implements IQuery, ISource {

	public enum Operation {
		INTERSECT
	}

	private IQuery c1;

	private Operation op;

	private IQuery c2;

	public QuerySet(IQuery c1, Operation op, IQuery c2) {
		this.c1 = c1;
		this.op = op;
		this.c2 = c2;
	}

	/**
	 * @return
	 */
	public Projection getProjection() {
		return c1.getProjection();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.madogiwa.plaintable.criteria.ICriteria#getSQLString(org.madogiwa.
	 * plaintable.criteria.CriteriaContext)
	 */
	public String getSQLString(Context context) {
		return String.format("(%s) %s (%s)", c1.getSQLString(context), op,
				c2.getSQLString(context));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.criteria.IQuery#getSource()
	 */
	public ISource getSource() {
		return c1.getSource();
	}

    public Schema getBaseSchema() {
        return c1.getBaseSchema();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.madogiwa.plaintable.criteria.ISource#getPathList()
     */
	public List<Path> getPathList() {
		return getSource().getPathList();
	}

}
