package org.madogiwa.plaintable.criteria.value;

import org.madogiwa.plaintable.criteria.Context;

/**
 *
 */
public class DateTimeVariable implements DateTimeExpression {

	enum Variable {
		CURRENT_TIMESTAMP;
	}

	private Variable var;

	public DateTimeVariable(Variable var) {
		this.var = var;
	}

	public String getSQLString(Context context) {
		return var.toString();
	}

}
