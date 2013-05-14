package org.madogiwa.plaintable.criteria;

import org.madogiwa.plaintable.PlainTableException;
import org.madogiwa.plaintable.mapper.BeanMapper;
import org.madogiwa.plaintable.mapper.MapMapper;
import org.madogiwa.plaintable.schema.Schema;

import java.util.Map;

/**
 * @Author Hidenori Sugiyama <madogiwa@gmail.com>
 */
public class Entity {

	private Finder finder;

	private Schema schema;

	private Long id;

	public Entity(Finder finder, Schema schema, Long id) {
		this.finder = finder;
		this.schema = schema;
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public <T> T load(Class<T> clazz) throws PlainTableException {
		BeanMapper<T> mapper = new BeanMapper(clazz);
		return finder.getSession().load(schema, id, mapper);
	}

	public Map<String, Object> toMap() throws PlainTableException {
		MapMapper mapper = new MapMapper();
		return finder.from(schema).eq(schema.getPrimaryKey(), id).toList(mapper).get(0);
	}

}
