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
package org.madogiwa.plaintable.criteria.value;

import org.madogiwa.plaintable.Path;
import org.madogiwa.plaintable.criteria.Context;
import org.madogiwa.plaintable.criteria.ISource;
import org.madogiwa.plaintable.criteria.JoinedSource;
import org.madogiwa.plaintable.schema.Schema;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class CrossJoinedSource extends JoinedSource {

	private ISource left;

	private ISource right;

	/**
	 * @param left
	 * @param right
	 */
	public CrossJoinedSource(ISource left, ISource right) {
		this.left = left;
		this.right = right;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.madogiwa.plaintable.criteria.IExpression#getSQLString(org.madogiwa
	 * .plaintable.criteria.Context)
	 */
	public String getSQLString(Context context) {
		return String.format("%s,%s",
				left.getSQLString(context), right.getSQLString(context));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.criteria.ISource#getPathList()
	 */
	public List<Path> getPathList() {
		List<Path> list = new ArrayList<Path>();
		list.addAll(left.getPathList());
		list.addAll(right.getPathList());
		return list;
	}

    public Schema getBaseSchema() {
        return left.getBaseSchema();
    }

}
