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
package org.madogiwa.plaintable;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Hidenori Sugiyama
 *
 */
public interface Row {

	public int size() throws PlainTableException;

	public List<String> getAliasList() throws PlainTableException;

	public String getAlias(int index) throws PlainTableException;

	public Object getObject(int index) throws PlainTableException;

	public Object getObject(String alias) throws PlainTableException;

	public String getString(int index) throws PlainTableException;

	public String getString(String alias) throws PlainTableException;

	public Boolean getBoolean(int index) throws PlainTableException;

	public Boolean getBoolean(String alias) throws PlainTableException;

	public Short getShort(int index) throws PlainTableException;

	public Short getShort(String alias) throws PlainTableException;

	public Integer getInteger(int index) throws PlainTableException;

	public Integer getInteger(String alias) throws PlainTableException;

	public Long getLong(int index) throws PlainTableException;

	public Long getLong(String alias) throws PlainTableException;

	public BigDecimal getBigDecimal(int index) throws PlainTableException;

	public BigDecimal getBigDecimal(String alias) throws PlainTableException;

	public Float getFloat(int index) throws PlainTableException;

	public Float getFloat(String alias) throws PlainTableException;

	public Double getDouble(int index) throws PlainTableException;

	public Double getDouble(String alias) throws PlainTableException;

	public Date getDate(int index) throws PlainTableException;

	public Date getDate(String alias) throws PlainTableException;

	public byte[] getBytes(int index) throws PlainTableException;

	public byte[] getBytes(String alias) throws PlainTableException;

	public InputStream getInputStream(int index) throws PlainTableException;

	public InputStream getInputStream(String alias) throws PlainTableException;

}
