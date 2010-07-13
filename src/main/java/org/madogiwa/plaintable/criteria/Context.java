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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.madogiwa.plaintable.dialect.Dialect;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class Context {

	private Map<String, Resolver> resolverMap = new HashMap<String, Resolver>();

	private List<Resolver> resolverList = new ArrayList<Resolver>();

	private Dialect dialect;

	/**
	 * @param dialect
	 */
	public Context(Dialect dialect) {
		this.dialect = dialect;
	}

	/**
	 * @return
	 */
	public Dialect getDialect() {
		return dialect;
	}

	/**
	 * @param resolver
	 * @return
	 */
	public String getResolverMarker(Resolver resolver) {
		String marker = "#" + resolverMap.size() + "#";
		resolverMap.put(marker, resolver);
		return marker;
	}

	/**
	 * @param sql
	 * @return
	 */
	public String replaceMarker(String sql) {
		Pattern pattern = Pattern.compile("#[1-9]*[0-9]#");
		Matcher matcher = pattern.matcher(sql);

		int matchCount = 0;
		while (true) {
			if (!matcher.find()) {
				break;
			}

			String matched = matcher.group(0);
			Resolver resolver = resolverMap.get(matched);
			resolverList.add(resolver);
			matchCount++;
		}

		if (matchCount != resolverMap.size()) {
			throw new RuntimeException(sql);
		}

		return matcher.replaceAll("?");
	}

	/**
	 * @param statement
	 * @throws SQLException
	 */
	public void resolveParameters(PreparedStatement statement)
			throws SQLException {
		for (int i = 0; i < resolverList.size(); i++) {
			resolverList.get(i).resolve(statement, i + 1);
		}
	}

}
