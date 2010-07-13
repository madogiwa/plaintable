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

/**
 * @author Hidenori Sugiyama
 * 
 */
public class Window implements Cloneable {

	public static long UNLIMITED = -1;

	private long offset;

	private long limit;

	/**
	 * 
	 */
	public Window() {
		offset = 0;
		limit = UNLIMITED;
	}

	/**
	 * @param offset
	 */
	public Window(long offset) {
		this.offset = offset;
		this.limit = UNLIMITED;
	}

	/**
	 * @param offset
	 * @param limit
	 */
	public Window(long offset, long limit) {
		this.offset = offset;
		this.limit = limit;
	}

	public Window offset(long offset) {
		this.offset = offset;
		return this;
	}

	public Window limit(long limit) {
		this.limit = limit;
		return this;
	}

	public long getOffset() {
		return offset;
	}

	public long getLimit() {
		return limit;
	}

}
