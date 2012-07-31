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
package org.madogiwa.plaintable;

import org.madogiwa.plaintable.criteria.IQuery;
import org.madogiwa.plaintable.criteria.Restriction;
import org.madogiwa.plaintable.criteria.Rows;
import org.madogiwa.plaintable.criteria.Window;
import org.madogiwa.plaintable.handler.RowHandler;
import org.madogiwa.plaintable.mapper.RowMapper;
import org.madogiwa.plaintable.provider.RowProvider;
import org.madogiwa.plaintable.schema.Column;
import org.madogiwa.plaintable.schema.Schema;

import java.util.List;

/**
 * @author Hidenori Sugiyama
 *
 */
/**
 * @author Hidenori Sugiyama
 * 
 */
public interface Session {

	public enum TransactionMode {
		READ_COMMITTED, SERIALIZABLE, CONNECTOR_DEFAULT
	};

	/**
	 * @throws PlainTableException
	 */
	public void open() throws PlainTableException;

	/**
	 * @param mode
	 */
	public void setTransactionMode(TransactionMode mode);

	/**
	 * @throws PlainTableException
	 */
	public void close() throws PlainTableException;

	/**
	 * @param delayedOpen
	 */
	public void setDelayedOpen(boolean delayedOpen);

	/**
	 * @param readOnly
	 */
	public void setReadOnly(boolean readOnly);

	/**
	 * @throws PlainTableException
	 */
	public void commit() throws PlainTableException;

	/**
	 * @throws PlainTableException
	 */
	public void rollback() throws PlainTableException;

	/**
	 * @param schema
	 * @throws PlainTableException
	 */
	public void lock(Schema schema) throws PlainTableException;

	/**
	 * @param action
	 * @throws PlainTableException
	 */
	public void doAtomic(AtomicAction action) throws PlainTableException;

	/**
	 * @return
	 * @throws PlainTableException
	 */
	public boolean isOpened() throws PlainTableException;

	/**
	 * @param <T>
	 * @param schema
	 * @param id
	 * @param mapper
	 * @return
	 * @throws PlainTableException
	 */
	public <T> T load(Schema schema, long id, RowMapper<T> mapper)
			throws PlainTableException;

	/**
	 * @param schema
	 * @param restriction
	 * @param column
	 * @return
	 * @throws PlainTableException
	 */
	public long count(Schema schema, Restriction restriction, Column column)
			throws PlainTableException;

	public <T> List<T> select(IQuery query, RowMapper<T> mapper)
			throws PlainTableException;

	public <T> List<T> select(IQuery query, RowMapper<T> mapper, Window window)
			throws PlainTableException;

	/**
	 * @param query
	 * @param handler
	 * @return
	 * @throws PlainTableException
	 */
	public void select(IQuery query, RowHandler handler)
			throws PlainTableException;

	/**
	 * @param query
	 * @param handler
	 * @param window
	 * @throws PlainTableException
	 */
	public void select(IQuery query, RowHandler handler, Window window)
			throws PlainTableException;

	/**
	 * @param bean
	 * @return
	 * @throws PlainTableException
	 */
	public <T> long insert(T bean) throws PlainTableException;

	/**
	 * @param schema
	 * @param rowProvider
	 * @return
	 * @throws PlainTableException
	 */
	public long insert(RowProvider rowProvider) throws PlainTableException;

	/**
	 * @param schema
	 * @param rowProvider
	 * @param id
	 * @return
	 * @throws PlainTableException
	 */
	public boolean update(RowProvider rowProvider, Long id)
			throws PlainTableException;

	/**
	 * @param schema
	 * @param rowProvider
	 * @param restriction
	 * @return
	 * @throws PlainTableException
	 */
	public long update(RowProvider rowProvider, Restriction restriction)
			throws PlainTableException;

	/**
	 * @param schema
	 * @param id
	 * @return
	 * @throws PlainTableException
	 */
	public boolean delete(Schema schema, Long id) throws PlainTableException;

	/**
	 * @param schema
	 * @param ids
	 * @return
	 * @throws PlainTableException
	 */
	public long delete(Schema schema, Long[] ids) throws PlainTableException;

	/**
	 * @param schema
	 * @param restriction
	 * @return
	 * @throws PlainTableException
	 */
	public long delete(Schema schema, Restriction restriction)
			throws PlainTableException;

}