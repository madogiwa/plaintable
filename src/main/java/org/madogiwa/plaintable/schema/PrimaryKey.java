package org.madogiwa.plaintable.schema;

/**
 * @author Hidenori Sugiyama
 *
 */
public abstract class PrimaryKey extends KeyColumn {

	/**
	 * @param schema
	 * @param name
	 */
	public PrimaryKey(Schema schema, String name) {
		super(schema, name);
	}

}
