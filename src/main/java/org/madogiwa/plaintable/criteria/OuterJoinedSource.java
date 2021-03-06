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
import org.madogiwa.plaintable.schema.Column;
import org.madogiwa.plaintable.schema.Schema;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class OuterJoinedSource extends JoinedSource {

	private ISource left;

	private Path leftColumn;

	private ISource right;

	private Path rightColumn;

	/**
	 * @param left
	 * @param right
	 */
	public OuterJoinedSource(Column left, Column right) {
		this(new TableSource(left.getSchema()), left, right);
	}

	/**
	 * @param left
	 * @param leftColumn
	 * @param rightColumn
	 */
	public OuterJoinedSource(ISource left, Column leftColumn, Column rightColumn) {
		this(left, new Path(leftColumn), new TableSource(
				rightColumn.getSchema(), leftColumn.getName() + "_"
						+ rightColumn.getSchema().getName()), new Path(
				leftColumn.getName() + "_" + rightColumn.getSchema().getName(),
				rightColumn.getName()));
	}

	/**
	 * @param left
	 * @param leftColumn
	 * @param right
	 * @param rightColumn
	 */
	public OuterJoinedSource(ISource left, Column leftColumn, ISource right,
			Column rightColumn) {
		this(left, new Path(leftColumn), right, new Path(rightColumn));
	}

	/**
	 * @param left
	 * @param leftPath
	 * @param right
	 * @param rightPath
	 */
	public OuterJoinedSource(ISource left, Path leftPath, ISource right,
			Path rightPath) {
		this.left = left;
		this.leftColumn = leftPath;
		this.right = right;
		this.rightColumn = rightPath;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.madogiwa.plaintable.criteria.IExpression#getSQLString(org.madogiwa
	 * .plaintable.criteria.Context)
	 */
	public String getSQLString(Context context) {
		return String.format("%s LEFT OUTER JOIN %s ON %s = %s",
				left.getSQLString(context), right.getSQLString(context),
				context.quotePath(leftColumn.getPathString()), context.quotePath(rightColumn.getPathString()));
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
