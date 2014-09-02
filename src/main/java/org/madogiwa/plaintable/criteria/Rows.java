package org.madogiwa.plaintable.criteria;

import org.madogiwa.plaintable.PlainTableException;
import org.madogiwa.plaintable.Session;
import org.madogiwa.plaintable.criteria.bool.BooleanExpression;
import org.madogiwa.plaintable.criteria.bool.Bools;
import org.madogiwa.plaintable.criteria.list.DateTimeListExpression;
import org.madogiwa.plaintable.criteria.list.NumericListExpression;
import org.madogiwa.plaintable.criteria.list.StringListExpression;
import org.madogiwa.plaintable.criteria.list.ValueListExpression;
import org.madogiwa.plaintable.criteria.value.*;
import org.madogiwa.plaintable.handler.BeanListHandler;
import org.madogiwa.plaintable.handler.ListHandler;
import org.madogiwa.plaintable.mapper.BeanMapper;
import org.madogiwa.plaintable.mapper.RowMapper;
import org.madogiwa.plaintable.provider.BeanRowProvider;
import org.madogiwa.plaintable.provider.RowProvider;
import org.madogiwa.plaintable.schema.*;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class Rows implements Cloneable {

    private Finder finder;

    private Query query;

    private Window window;

    public Rows(Finder finder, Query query, Window window) {
        this.finder = finder;
        this.query = query;
        this.window = window;
    }

    public long count() throws PlainTableException {
        Window window = getWindow();
        if (window.getOffset() != 0 || window.getLimit() != Window.UNLIMITED) {
            throw new PlainTableException("offset() or limit() is not supported.");
        }

        Session session = finder.getSession();
        boolean opened = session.isOpened();
        try {
            return session.count(query);
        } finally {
            if (!opened && session.getAutoCommit()) {
                session.close();
            }
        }
    }

    public <T> List<T> toList(Class<T> clazz) throws PlainTableException {
        return toList(new BeanMapper(clazz));
    }

    public <T> List<T> toList(RowMapper<T> mapper) throws PlainTableException {
        ListHandler<T> handler = new ListHandler<T>(mapper);
        Session session = finder.getSession();
        boolean opened = session.isOpened();
        try {
            session.select(query, handler);
            return handler.getList();
        } finally {
            if (!opened && session.getAutoCommit()) {
                session.close();
            }
        }
    }

	public <T> List<T> toList(BeanMapper<T> mapper) throws PlainTableException {
		BeanListHandler<T> handler = new BeanListHandler<T>(mapper);
		Session session = finder.getSession();
		boolean opened = session.isOpened();
		try {
			session.select(query, handler);
			return handler.getList();
		} finally {
			if (!opened && session.getAutoCommit()) {
				session.close();
			}
		}
	}

	public <T> Iterator<T> iterator(Class<T> clazz) throws PlainTableException {
		return toList(clazz).iterator();
	}

	public <T> long update(T bean) throws PlainTableException {
		return update(new BeanRowProvider<T>(bean));
	}

    public long update(RowProvider rowProvider) throws PlainTableException {
        Window window = getWindow();
        if (window.getOffset() != 0 || window.getLimit() != Window.UNLIMITED) {
            throw new PlainTableException("offset() or limit() is not supported.");
        }

        Query query = getQuery();
        ISource source = query.getSource();
        if (!(source instanceof TableSource)) {
            throw new PlainTableException("update() only support calling for TableSource.");
        }
        Schema schema = ((TableSource)source).getSchema();

        Finder finder = getFinder();
        Session session = finder.getSession();
        boolean opened = session.isOpened();
        try {
            return session.update(rowProvider, query.getRestriction());
        } finally {
            if (!opened && session.getAutoCommit()) {
                session.close();
            }
        }
    }

    public long delete() throws PlainTableException {
        Window window = getWindow();
        if (window.getOffset() != 0 || window.getLimit() != Window.UNLIMITED) {
            throw new PlainTableException("offset() or limit() is not supported.");
        }

        Query query = getQuery();
        ISource source = query.getSource();
        if (!(source instanceof TableSource)) {
            throw new PlainTableException("delete() only support calling for TableSource.");
        }
        Schema schema = ((TableSource)source).getSchema();

        Finder finder = getFinder();
        Session session = finder.getSession();
        boolean opened = session.isOpened();
        try {
            return session.delete(schema, query.getRestriction());
        } finally {
            if (!opened && session.getAutoCommit()) {
                session.close();
            }
        }
    }

	public long insert(RowProvider rowProvider) throws PlainTableException {
		Window window = getWindow();
		if (window.getOffset() != 0 || window.getLimit() != Window.UNLIMITED) {
			throw new PlainTableException("offset() or limit() is not supported.");
		}

		Query query = getQuery();
		ISource source = query.getSource();
		if (!(source instanceof TableSource)) {
			throw new PlainTableException("insert() only support calling for TableSource.");
		}
		Schema schema = ((TableSource)source).getSchema();
		if (rowProvider.getSchema() != schema) {
			throw new RuntimeException("RowProvider has different schema.");
		}

		Finder finder = getFinder();
		Session session = finder.getSession();
		boolean opened = session.isOpened();
		try {
			return session.insert(rowProvider);
		} finally {
			if (!opened && session.getAutoCommit()) {
				session.close();
			}
		}
	}

    public Rows between(NumericColumnReference column,
                        Number low, Number high) {
        return addRestrictionWithClone(Bools.between(column, low, high));
    }

    public Rows between(NumericExpression target,
                        NumericExpression low, NumericExpression high) {
        return addRestrictionWithClone(Bools.between(target, low, high));
    }

    public Rows between(DateTimeColumnReference column,
                        Date low, Date high) {
        return addRestrictionWithClone(Bools.between(column, low, high));
    }

    public Rows between(DateTimeExpression target,
                        DateTimeExpression low, DateTimeExpression high) {
        return addRestrictionWithClone(Bools.between(target, low, high));
    }

    public Rows notBetween(NumericColumnReference column,
                           Number low, Number high) {
        return addRestrictionWithClone(Bools.notBetween(column, low, high));
    }

    public Rows notBetween(NumericExpression target,
                           NumericExpression low, NumericExpression high) {
        return addRestrictionWithClone(Bools.notBetween(target, low, high));
    }

    public Rows notBetween(DateTimeColumnReference column,
                           Date low, Date high) {
        return addRestrictionWithClone(Bools.notBetween(column, low, high));
    }

    public Rows notBetween(DateTimeExpression target,
                           DateTimeExpression low, DateTimeExpression high) {
        return addRestrictionWithClone(Bools.notBetween(target, low, high));
    }

    public Rows eq(KeyColumn attr, long key) {
        return addRestrictionWithClone(Bools.eq(attr, key));
    }

    public Rows eq(NumericAttribute attr, Number value) {
        return addRestrictionWithClone(Bools.eq(attr, value));
    }

    public Rows eq(ValueExpression lhs,
                   ValueExpression rhs) {
        return addRestrictionWithClone(Bools.eq(lhs, rhs));
    }

    public Rows ne(NumericAttribute attr, Number value) {
        return addRestrictionWithClone(Bools.ne(attr, value));
    }

    public Rows ne(ValueExpression lhs,
                   ValueExpression rhs) {
        return addRestrictionWithClone(Bools.ne(lhs, rhs));
    }

    public Rows notEqual(ValueExpression lhs,
                         ValueExpression rhs) {
        return addRestrictionWithClone(Bools.notEqual(lhs, rhs));
    }

    public Rows gt(NumericAttribute attr, Number value) {
        return addRestrictionWithClone(Bools.gt(attr, value));
    }

    public Rows gt(ValueExpression lhs,
                   ValueExpression rhs) {
        return addRestrictionWithClone(Bools.gt(lhs, rhs));
    }

    public Rows ge(NumericAttribute attr, Number value) {
        return addRestrictionWithClone(Bools.ge(attr, value));
    }

    public Rows ge(ValueExpression lhs,
                   ValueExpression rhs) {
        return addRestrictionWithClone(Bools.ge(lhs, rhs));
    }

    public Rows lt(NumericAttribute attr, Number value) {
        return addRestrictionWithClone(Bools.lt(attr, value));
    }

    public Rows lt(NumericColumnReference column,
                   Number value) {
        return addRestrictionWithClone(Bools.lt(column, value));
    }

    public Rows lt(ValueExpression lhs,
                   ValueExpression rhs) {
        return addRestrictionWithClone(Bools.lt(lhs, rhs));
    }

    public Rows le(NumericAttribute attr, Number value) {
        return addRestrictionWithClone(Bools.le(attr, value));
    }

    public Rows le(ValueExpression lhs,
                   ValueExpression rhs) {
        return addRestrictionWithClone(Bools.le(lhs, rhs));
    }

    public Rows exists(RowsExpression rows) {
        return addRestrictionWithClone(Bools.exists(rows));
    }

    public Rows in(KeyColumn column, Long[] list) {
        return addRestrictionWithClone(Bools.in(column, list));
    }

    public Rows in(KeyColumnReference column,
                   NumericListExpression list) {
        return addRestrictionWithClone(Bools.in(column, list));
    }

    public <N extends Number> Rows in(NumericAttribute attr,
                                      N[] list) {
        return addRestrictionWithClone(Bools.in(attr, list));
    }

    public Rows in(NumericColumnReference column,
                   NumericListExpression list) {
        return addRestrictionWithClone(Bools.in(column, list));
    }

    public Rows in(TextAttribute attr, String[] values) {
        return addRestrictionWithClone(Bools.in(attr, values));
    }

    public Rows in(StringExpression value,
                   StringListExpression list) {
        return addRestrictionWithClone(Bools.in(value, list));
    }

    public Rows in(DateTimeAttribute attr, Date[] values) {
        return addRestrictionWithClone(Bools.in(attr, values));
    }

    public Rows in(DateTimeExpression value,
                   DateTimeListExpression list) {
        return addRestrictionWithClone(Bools.in(value, list));
    }

    public Rows exact(TextAttribute attr, String pattern) {
        return addRestrictionWithClone(Bools.exact(attr, pattern));
    }

    public Rows exact(StringColumnReference column,
                      String pattern) {
        return addRestrictionWithClone(Bools.exact(column, pattern));
    }

    public Rows contain(TextAttribute attr, String pattern) {
        return addRestrictionWithClone(Bools.contain(attr, pattern));
    }

    public Rows contain(StringColumnReference column,
                        String pattern) {
        return addRestrictionWithClone(Bools.contain(column, pattern));
    }

    public Rows startWith(TextAttribute column, String pattern) {
        return addRestrictionWithClone(Bools.startWith(column, pattern));
    }

    public Rows startWith(StringColumnReference column,
                          String pattern) {
        return addRestrictionWithClone(Bools.startWith(column, pattern));
    }

    public Rows endWith(TextAttribute column, String pattern) {
        return addRestrictionWithClone(Bools.endWith(column, pattern));
    }

    public Rows endWith(StringColumnReference column,
                        String pattern) {
        return addRestrictionWithClone(Bools.endWith(column, pattern));
    }

    public Rows notExact(TextAttribute attr, String pattern) {
        return addRestrictionWithClone(Bools.notExact(attr, pattern));
    }

    public Rows notExact(StringColumnReference column,
                         String pattern) {
        return addRestrictionWithClone(Bools.notExact(column, pattern));
    }

    public Rows notContain(TextAttribute attr, String pattern) {
        return addRestrictionWithClone(Bools.notContain(attr, pattern));
    }

    public Rows notContain(StringColumnReference column,
                           String pattern) {
        return addRestrictionWithClone(Bools.notContain(column, pattern));
    }

    public Rows notStartWith(TextAttribute attr, String pattern) {
        return addRestrictionWithClone(Bools.notStartWith(attr, pattern));
    }

    public Rows notStartWith(StringColumnReference column,
                             String pattern) {
        return addRestrictionWithClone(Bools.notStartWith(column, pattern));
    }

    public Rows notEndWith(TextAttribute column, String pattern) {
        return addRestrictionWithClone(Bools.notEndWith(column, pattern));
    }

    public Rows notEndWith(StringColumnReference column,
                           String pattern) {
        return addRestrictionWithClone(Bools.notEndWith(column, pattern));
    }

    public Rows isNull(NumericColumnReference column) {
        return addRestrictionWithClone(Bools.isNull(column));
    }

    public Rows isNull(StringColumnReference column) {
        return addRestrictionWithClone(Bools.isNull(column));
    }

    public Rows isNull(DateTimeColumnReference column) {
        return addRestrictionWithClone(Bools.isNull(column));
    }

    public Rows isNull(BinaryColumnReference column) {
        return addRestrictionWithClone(Bools.isNull(column));
    }

    public Rows isNotNull(NumericColumnReference column) {
        return addRestrictionWithClone(Bools.isNotNull(column));
    }

    public Rows isNotNull(StringColumnReference column) {
        return addRestrictionWithClone(Bools.isNotNull(column));
    }

    public Rows isNotNull(DateTimeColumnReference column) {
        return addRestrictionWithClone(Bools.isNotNull(column));
    }

    public Rows isNotNull(BinaryColumnReference column) {
        return addRestrictionWithClone(Bools.isNotNull(column));
    }

    public Rows eqAll(ValueExpression value,
                      ValueListExpression list) {
        return addRestrictionWithClone(Bools.equalAll(value, list));
    }

    public Rows eqSome(ValueExpression value,
                       ValueListExpression list) {
        return addRestrictionWithClone(Bools.equalSome(value, list));
    }

    public Rows eqAny(ValueExpression value,
                      ValueListExpression list) {
        return addRestrictionWithClone(Bools.equalAny(value, list));
    }

    public Rows neAll(ValueExpression value,
                      ValueListExpression list) {
        return addRestrictionWithClone(Bools.notEqualAll(value, list));
    }

    public Rows neSome(ValueExpression value,
                       ValueListExpression list) {
        return addRestrictionWithClone(Bools.notEqualSome(value, list));
    }

    public Rows neAny(ValueExpression value,
                      ValueListExpression list) {
        return addRestrictionWithClone(Bools.notEqualAny(value, list));
    }

    public Rows ltAll(ValueExpression value,
                      ValueListExpression list) {
        return addRestrictionWithClone(Bools.littleThanAll(value, list));
    }

    public Rows ltSome(ValueExpression value,
                       ValueListExpression list) {
        return addRestrictionWithClone(Bools.littleThanSome(value, list));
    }

    public Rows ltAny(ValueExpression value,
                      ValueListExpression list) {
        return addRestrictionWithClone(Bools.littleThanAny(value, list));
    }

    public Rows leAll(
            ValueExpression value, ValueListExpression list) {
        return addRestrictionWithClone(Bools.littleThanOrEqualAll(value, list));
    }

    public Rows leSome(
            ValueExpression value, ValueListExpression list) {
        return addRestrictionWithClone(Bools.littleThanOrEqualSome(value, list));
    }

    public Rows leAny(
            ValueExpression value, ValueListExpression list) {
        return addRestrictionWithClone(Bools.littleThanOrEqualAny(value, list));
    }

    public Rows gtAll(ValueExpression value,
                      ValueListExpression list) {
        return addRestrictionWithClone(Bools.greaterThanAll(value, list));
    }

    public Rows gtSome(ValueExpression value,
                       ValueListExpression list) {
        return addRestrictionWithClone(Bools.greaterThanSome(value, list));
    }

    public Rows gtAny(ValueExpression value,
                      ValueListExpression list) {
        return addRestrictionWithClone(Bools.greaterThanAny(value, list));
    }

    public Rows geAll(
            ValueExpression value, ValueListExpression list) {
        return addRestrictionWithClone(Bools.greaterThanOrEqualAll(value, list));
    }

    public Rows geSome(
            ValueExpression value, ValueListExpression list) {
        return addRestrictionWithClone(Bools.greaterThanOrEqualSome(value, list));
    }

    public Rows geAny(
            ValueExpression value, ValueListExpression list) {
        return addRestrictionWithClone(Bools.greaterThanOrEqualAny(value, list));
    }

    public Rows asc(Column column) {
        Query newQuery = cloneQuery();
        newQuery.ascendingOrder(column);
        return new Rows(getFinder(), newQuery, getWindow());
    }

    public Rows desc(Column column) {
        Query newQuery = cloneQuery();
        newQuery.descendingOrder(column);
        return new Rows(getFinder(), newQuery, getWindow());
    }

    public Rows offset(long offset) {
        Window newWindow = cloneWindow();
        newWindow.offset(offset);
        return new Rows(getFinder(), cloneQuery(), newWindow);
    }

    public Rows limit(long limit) {
        Window newWindow = cloneWindow();
        newWindow.limit(limit);
        return new Rows(getFinder(), cloneQuery(), newWindow);
    }

    public Rows innerJoin(Column leftColumn, Column rightColumn) {
        Query newQuery = cloneQuery();
        newQuery.setSource(new InnerJoinedSource(newQuery.getSource(), leftColumn, rightColumn));
        return new Rows(getFinder(), newQuery, getWindow());
    }

    public Rows outerJoin(Column leftColumn, Column rightColumn) {
        Query newQuery = cloneQuery();
        newQuery.setSource(new OuterJoinedSource(newQuery.getSource(), leftColumn, rightColumn));
        return new Rows(getFinder(), newQuery, getWindow());
    }

    private Rows addRestrictionWithClone(BooleanExpression expr) {
        Query newQuery = cloneQuery();
        newQuery.add(expr);
        return new Rows(getFinder(), newQuery, getWindow());
    }

    protected Query cloneQuery() {
        try {
            return (Query) query.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    protected Window cloneWindow() {
        try {
            return (Window) window.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    protected Finder getFinder() {
        return finder;
    }

    protected Query getQuery() {
        return query;
    }

    protected Window getWindow() {
        return window;
    }

}
