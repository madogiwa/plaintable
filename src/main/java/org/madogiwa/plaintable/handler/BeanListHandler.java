package org.madogiwa.plaintable.handler;

import org.madogiwa.plaintable.PlainTableException;
import org.madogiwa.plaintable.Row;
import org.madogiwa.plaintable.mapper.BeanMapper;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @Author Hidenori Sugiyama <madogiwa@gmail.com>
 */
public class BeanListHandler<T> implements RowHandler {

	private LinkedHashMap<Long, T> rows;

	private BeanMapper<T> mapper;

	/**
	 * @param mapper
	 */
	public BeanListHandler(BeanMapper<T> mapper) {
		this.mapper = mapper;
	}

	public void begin() {
		rows = new LinkedHashMap<Long, T>();
	}

	public void handle(Row row) throws PlainTableException {
		Long id = row.getLong(mapper.getSourceAlias() + ".id");
		T bean = rows.get(id);
		if (bean == null) {
			bean = mapper.map(row);
			rows.put(id, bean);
		} else {
			mapper.map(row, bean);
		}
	}

	public void end() {
		// nothing
	}

	/**
	 * @return
	 */
	public List<T> getList() {
		return (List<T>)Arrays.asList(rows.values().toArray());
	}

}
