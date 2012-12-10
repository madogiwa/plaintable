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
package org.madogiwa.plaintable.impl;

import org.madogiwa.plaintable.PlainTableException;
import org.madogiwa.plaintable.Row;
import org.madogiwa.plaintable.criteria.Projection;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class RowImpl implements Row {

	private Projection projection;

	private ResultSet resultSet;

	public RowImpl(Projection projection, ResultSet resultSet) {
		this.projection = projection;
		this.resultSet = resultSet;
	}

	public void begin() {

	}

	public void end() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.Row#size()
	 */
	public int size() throws PlainTableException {
		try {
			ResultSetMetaData metaData = resultSet.getMetaData();
			return metaData.getColumnCount();
		} catch (SQLException e) {
			throw new PlainTableException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.Row#getAlias(int)
	 */
	public String getAlias(int index) throws PlainTableException {
        return getAliasList().get(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.Row#getAliasList()
	 */
	public List<String> getAliasList() throws PlainTableException {
        return projection.getPathList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.Row#getBigDecimal(int)
	 */
	public BigDecimal getBigDecimal(int index) throws PlainTableException {
		try {
			return resultSet.getBigDecimal(index + 1);
		} catch (SQLException e) {
			throw new PlainTableException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.Row#getBigDecimal(java.lang.String)
	 */
	public BigDecimal getBigDecimal(String alias) throws PlainTableException {
		try {
			return resultSet.getBigDecimal(alias);
		} catch (SQLException e) {
			throw new PlainTableException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.Row#getBoolean(int)
	 */
	public Boolean getBoolean(int index) throws PlainTableException {
		try {
			return resultSet.getBoolean(index + 1);
		} catch (SQLException e) {
			throw new PlainTableException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.Row#getBoolean(java.lang.String)
	 */
	public Boolean getBoolean(String alias) throws PlainTableException {
		try {
			return resultSet.getBoolean(alias);
		} catch (SQLException e) {
			throw new PlainTableException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.Row#getBytes(int)
	 */
	public byte[] getBytes(int index) throws PlainTableException {
		try {
			return resultSet.getBytes(index + 1);
		} catch (SQLException e) {
			throw new PlainTableException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.Row#getBytes(java.lang.String)
	 */
	public byte[] getBytes(String alias) throws PlainTableException {
		try {
			return resultSet.getBytes(alias);
		} catch (SQLException e) {
			throw new PlainTableException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.Row#getDate(int)
	 */
	public Date getDate(int index) throws PlainTableException {
		try {
			return resultSet.getDate(index + 1);
		} catch (SQLException e) {
			throw new PlainTableException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.Row#getDate(java.lang.String)
	 */
	public Date getDate(String alias) throws PlainTableException {
		try {
			return resultSet.getDate(alias);
		} catch (SQLException e) {
			throw new PlainTableException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.Row#getDouble(int)
	 */
	public Double getDouble(int index) throws PlainTableException {
		try {
			return resultSet.getDouble(index + 1);
		} catch (SQLException e) {
			throw new PlainTableException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.Row#getDouble(java.lang.String)
	 */
	public Double getDouble(String alias) throws PlainTableException {
		try {
			return resultSet.getDouble(alias);
		} catch (SQLException e) {
			throw new PlainTableException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.Row#getFloat(int)
	 */
	public Float getFloat(int index) throws PlainTableException {
		try {
			return resultSet.getFloat(index + 1);
		} catch (SQLException e) {
			throw new PlainTableException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.Row#getFloat(java.lang.String)
	 */
	public Float getFloat(String alias) throws PlainTableException {
		try {
			return resultSet.getFloat(alias);
		} catch (SQLException e) {
			throw new PlainTableException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.Row#getInputStream(int)
	 */
	public InputStream getInputStream(int index) throws PlainTableException {
		try {
			return resultSet.getBinaryStream(index + 1);
		} catch (SQLException e) {
			throw new PlainTableException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.Row#getInputStream(java.lang.String)
	 */
	public InputStream getInputStream(String alias) throws PlainTableException {
		try {
			return resultSet.getBinaryStream(alias);
		} catch (SQLException e) {
			throw new PlainTableException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.Row#getInteger(int)
	 */
	public Integer getInteger(int index) throws PlainTableException {
		try {
			return resultSet.getInt(index + 1);
		} catch (SQLException e) {
			throw new PlainTableException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.Row#getInteger(java.lang.String)
	 */
	public Integer getInteger(String alias) throws PlainTableException {
		try {
			return resultSet.getInt(alias);
		} catch (SQLException e) {
			throw new PlainTableException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.Row#getLong(int)
	 */
	public Long getLong(int index) throws PlainTableException {
		try {
			return resultSet.getLong(index + 1);
		} catch (SQLException e) {
			throw new PlainTableException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.Row#getLong(java.lang.String)
	 */
	public Long getLong(String alias) throws PlainTableException {
		try {
			return resultSet.getLong(alias);
		} catch (SQLException e) {
			throw new PlainTableException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.Row#getObject(int)
	 */
	public Object getObject(int index) throws PlainTableException {
		try {
			return resultSet.getObject(index + 1);
		} catch (SQLException e) {
			throw new PlainTableException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.Row#getObject(java.lang.String)
	 */
	public Object getObject(String alias) throws PlainTableException {
		try {
			return resultSet.getObject(alias);
		} catch (SQLException e) {
			throw new PlainTableException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.Row#getShort(int)
	 */
	public Short getShort(int index) throws PlainTableException {
		try {
			return resultSet.getShort(index + 1);
		} catch (SQLException e) {
			throw new PlainTableException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.Row#getShort(java.lang.String)
	 */
	public Short getShort(String alias) throws PlainTableException {
		try {
			return resultSet.getShort(alias);
		} catch (SQLException e) {
			throw new PlainTableException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.Row#getString(int)
	 */
	public String getString(int index) throws PlainTableException {
		try {
			return resultSet.getString(index + 1);
		} catch (SQLException e) {
			throw new PlainTableException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.Row#getString(java.lang.String)
	 */
	public String getString(String alias) throws PlainTableException {
		try {
			return resultSet.getString(alias);
		} catch (SQLException e) {
			throw new PlainTableException(e);
		}
	}
}
