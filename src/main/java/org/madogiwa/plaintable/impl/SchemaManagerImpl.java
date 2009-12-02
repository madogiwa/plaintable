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
package org.madogiwa.plaintable.impl;

import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.madogiwa.plaintable.DatabaseSchema;
import org.madogiwa.plaintable.PlainTableException;
import org.madogiwa.plaintable.SchemaManager;
import org.madogiwa.plaintable.schema.AttributeColumn;
import org.madogiwa.plaintable.schema.ReferenceKey;
import org.madogiwa.plaintable.schema.Schema;
import org.madogiwa.plaintable.schema.SchemaReference;
import org.madogiwa.plaintable.schema.SyntheticKey;
import org.madogiwa.plaintable.schema.annot.Attribute;
import org.madogiwa.plaintable.schema.annot.Reference;
import org.madogiwa.plaintable.schema.annot.Table;

/**
 * @author Hidenori Sugiyama
 *
 */
public class SchemaManagerImpl implements SchemaManager {

	private static Logger logger = Logger.getLogger(SchemaManagerImpl.class.getName());

	private Map<String, Schema> schemaMap = new HashMap<String, Schema>();

	private DatabaseSchema databaseSchema;

	/**
	 * @param dataSource
	 */
	public SchemaManagerImpl(DatabaseSchema databaseSchema) {
		this.databaseSchema = databaseSchema;
	}

	/* (non-Javadoc)
	 * @see org.madogiwa.plaintable.TableManager#addTable(java.lang.Class)
	 */
	public void manage(Class<?> clazz) {
		Schema schema = extractSchemaFromClass(clazz);
		updateClassFields(clazz, schema);
		manage(schema);
	}

	/**
	 * @param clazz
	 * @return
	 */
	private Schema extractSchemaFromClass(Class<?> clazz) {
		Table annot = (Table) clazz.getAnnotation(Table.class);
		if (annot == null) {
			throw new RuntimeException(String.format("%s is not a table schema", clazz));
		}

		long serial = calculateSerialUUID(clazz);
		return extractSchemaFromClass(clazz, annot.name(), serial);	
	}

	/**
	 * @param clazz
	 * @return
	 */
	private long calculateSerialUUID(Class<?> clazz) {
		ObjectStreamClass objStream = ObjectStreamClass.lookup(clazz);
		if (objStream == null) {
			throw new RuntimeException(String.format("%s hasn't serialVersionUID"));
		}
		return objStream.getSerialVersionUID();
	}

	/**
	 * @param clazz
	 * @param name
	 * @param serial
	 */
	private Schema extractSchemaFromClass(Class<?> clazz, String name, long serial) {
		Schema schema = new Schema(name, serial);

		Field[] fields = clazz.getFields();
		for(Field field : fields) {
			if (Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(field.getModifiers())) {
				if (field.getType().equals(SyntheticKey.class)) {
					if (schema.getSyntheticKey() != null) {
						throw new RuntimeException(String.format("%s has duplicate SyntheticKey", clazz));
					}

					SyntheticKey key = new SyntheticKey(schema, field.getName());
					schema.setSyntheticKey(key);
				} else if (field.getType().equals(ReferenceKey.class)) {
					Reference reference = field.getAnnotation(Reference.class);

					ReferenceKey ref = new ReferenceKey(schema, field.getName(), new SchemaReference(reference.target().getCanonicalName()));
					ref.setIndexed(true);
					ref.setUnique(reference.unique());
					ref.setCascade(reference.cascade());
					schema.addReferenceKey(ref);
				} else if (AttributeColumn.class.isAssignableFrom(field.getType())) {
					Attribute column = field.getAnnotation(Attribute.class);

					try {
						Constructor<?> constructor = field.getType().getConstructor(new Class[] {Schema.class, String.class});
						AttributeColumn attr = (AttributeColumn) constructor.newInstance(new Object[] {schema, field.getName()});
						attr.setNullable((column != null) ? column.nullable() : false);
						attr.setIndexed((column != null) ? column.indexed() : false);
						attr.setUnique((column != null) ? column.unique() : false);
						attr.setLength((column != null) ? column.length() : -1);
						schema.addAttribute(attr);
					} catch (SecurityException e) {
						throw new RuntimeException(e);
					} catch (NoSuchMethodException e) {
						throw new RuntimeException(e);
					} catch (IllegalArgumentException e) {
						throw new RuntimeException(e);
					} catch (InstantiationException e) {
						throw new RuntimeException(e);
					} catch (IllegalAccessException e) {
						throw new RuntimeException(e);
					} catch (InvocationTargetException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}

		return schema;
	}

	/**
	 * @param clazz
	 * @param schema
	 */
	private void updateClassFields(Class<?> clazz, Schema schema) {
		Field[] fields = clazz.getFields();
		for(Field field : fields) {
			if (Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(field.getModifiers())) {
				if (field.getType().equals(Schema.class)) {
					try {
						field.set(null, schema);
					} catch (IllegalArgumentException e) {
						throw new RuntimeException(e);
					} catch (IllegalAccessException e) {
						throw new RuntimeException(e);
					}
				} else if (field.getType().equals(SyntheticKey.class)) {
					try {
						field.set(null, schema.getSyntheticKey());
					} catch (IllegalArgumentException e) {
						throw new RuntimeException(e);
					} catch (IllegalAccessException e) {
						throw new RuntimeException(e);
					}
				} else if (field.getType().equals(ReferenceKey.class)) {
					try {
						Set<ReferenceKey> keySet = schema.getReferenceKeys();
						for(ReferenceKey key : keySet) {
							if (field.getName().equals(key.getName())) {
								field.set(null, key);
							}
						}
					} catch (IllegalArgumentException e) {
						throw new RuntimeException(e);
					} catch (IllegalAccessException e) {
						throw new RuntimeException(e);
					}
				} else if (AttributeColumn.class.isAssignableFrom(field.getType())) {
					try {
						Set<AttributeColumn> attrSet = schema.getAttributes();
						for(AttributeColumn attr : attrSet) {
							if (field.getName().equals(attr.getName())) {
								field.set(null, attr);
							}
						}
					} catch (IllegalArgumentException e) {
						throw new RuntimeException(e);
					} catch (IllegalAccessException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.madogiwa.plaintable.TableManager#addTable(org.madogiwa.plaintable.schema.TableSchema)
	 */
	public void manage(Schema schema) {
		if (schemaMap.containsKey(schema.getName())) {
			return;
		}

		schemaMap.put(schema.getName(), schema);
	}

	/* (non-Javadoc)
	 * @see org.madogiwa.plaintable.TableManager#sync(org.madogiwa.plaintable.TableManager.SynchronizeMode)
	 */
	public boolean sync(SynchronizeMode mode) throws PlainTableException {
		return sync(mode, false);
	}

	/* (non-Javadoc)
	 * @see org.madogiwa.plaintable.SchemaManager#sync(org.madogiwa.plaintable.SchemaManager.SynchronizeMode, boolean)
	 */
	public boolean sync(SynchronizeMode mode, boolean forced) throws PlainTableException {
		try {
			databaseSchema.open();

			Map<String, Schema> currentMap = databaseSchema.retrieveSchemaMap();
	 		Set<String> dirtyNames = diffSchema(currentMap, schemaMap);
	
			if (dirtyNames.size() == 0 && !forced) {
				return false;
			}
	
			if (mode == SynchronizeMode.NONE) {
				return true;
			} else if (mode == SynchronizeMode.UPDATE_ONLY) {
				throw new RuntimeException("update_only not implemented currently");
			} else if (mode == SynchronizeMode.DROP_AND_CREATE) {
				dropAll(buildSchemaMap(currentMap, dirtyNames));
				createAll(buildSchemaMap(schemaMap, dirtyNames));
			} else if (mode == SynchronizeMode.ALL_DROP_AND_CREATE) {
				dropAll(currentMap);
				createAll(schemaMap);
			} else {
				throw new RuntimeException();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				databaseSchema.close();
			} catch (SQLException e) {
				throw new PlainTableException(e);
			}
		}

		return true;
	}

	/**
	 * @param current
	 * @param target
	 * @return
	 */
	private Set<String> diffSchema(Map<String, Schema> current, Map<String, Schema> target) {
		Set<String> diffNames = new HashSet<String>();

		for (String name : target.keySet()) {
			if (!current.containsKey(name)) {
				diffNames.add(name);
			} else if (current.get(name).getVersion() != target.get(name).getVersion()) {
				diffNames.add(name);
			}
		}

		return diffNames;
	}

	/**
	 * @param source
	 * @param names
	 * @return
	 */
	private Map<String, Schema> buildSchemaMap(Map<String, Schema> source, Set<String> names) {
		Map<String, Schema> map = new HashMap<String, Schema>();
		for(String name : names) {
			map.put(name, source.get(name));
		}
		return map;
	}

	/**
	 * @param schemaMap
	 * @throws SQLException
	 */
	private void dropAll(Map<String, Schema> schemaMap) throws SQLException {
		Map<String, Schema> remainMap = new HashMap<String, Schema>(schemaMap);
		for(Schema schema : schemaMap.values()) {
			drop(remainMap, schema.getName());
		}
	}

	/**
	 * @param map
	 * @param name
	 * @throws SQLException
	 */
	private void drop(Map<String, Schema> map, String name) throws SQLException {
		Schema schema = map.get(name);
		map.remove(name);

		if (schema == null) {
			return;
		}
		for(ReferenceKey referenceKey : schema.getReferenceKeys()) {
			drop(map, referenceKey.getTarget().getSchemaName());
		}
		databaseSchema.dropTable(schema);
	}

	/**
	 * @param schemaMap
	 * @throws SQLException
	 */
	private void createAll(Map<String, Schema> schemaMap) throws SQLException {
		Map<String, Schema> remainMap = new HashMap<String, Schema>(schemaMap);
		for(Schema schema : schemaMap.values()) {
			logger.fine("create " + schema.getName());
			create(remainMap, schema.getName());
		}
	}

	/**
	 * @param map
	 * @param name
	 * @throws SQLException 
	 */
	private void create(Map<String, Schema> map, String name) throws SQLException {
		Schema schema = map.get(name);
		map.remove(name);

		if (schema == null) {
			return;
		}
		for(ReferenceKey referenceKey : schema.getReferenceKeys()) {
			create(map, referenceKey.getTarget().getSchemaName());
		}
		databaseSchema.createTable(schema);
	}

}
