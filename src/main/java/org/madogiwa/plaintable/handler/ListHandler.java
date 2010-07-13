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
package org.madogiwa.plaintable.handler;

import java.util.ArrayList;
import java.util.List;

import org.madogiwa.plaintable.PlainTableException;
import org.madogiwa.plaintable.Row;
import org.madogiwa.plaintable.mapper.RowMapper;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class ListHandler<T> implements RowHandler {

	private List<T> list;

	private RowMapper<T> mapper;

	/**
	 * @param mapper
	 */
	public ListHandler(RowMapper<T> mapper) {
		this.mapper = mapper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.handler.RowHandler#begin()
	 */
	public void begin() {
		list = new ArrayList<T>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.madogiwa.plaintable.handler.RowHandler#handle(org.madogiwa.plaintable
	 * .Row)
	 */
	public void handle(Row row) throws PlainTableException {
		list.add(mapper.map(row));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.handler.RowHandler#end()
	 */
	public void end() {
		// nothing todo
	}

	/**
	 * @return
	 */
	public List<T> getList() {
		return list;
	}

}
