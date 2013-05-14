package org.madogiwa.plaintable.criteria;

import org.madogiwa.plaintable.PlainTableException;
import org.madogiwa.plaintable.Session;
import org.madogiwa.plaintable.provider.BeanRowProvider;
import org.madogiwa.plaintable.provider.RowProvider;
import org.madogiwa.plaintable.schema.Schema;

/**
 *
 */
public class Finder {

    private Session session;

    public Finder(Session session) {
        this.session = session;
    }

    public Rows from(Schema schema) {
        Query query = new Query(new TableSource(schema));
        return new Rows(this, query, new Window());
    }

    public Rows from(IQuery sourceQuery) {
        Query query = new Query(new QuerySource(sourceQuery, "alias"));
        return new Rows(this, query, new Window());
    }

	public Entity insert(Object object) throws PlainTableException {
		return insert(new BeanRowProvider(object));
	}

	public Entity insert(RowProvider rowProvider) throws PlainTableException {
		Query query = new Query(new TableSource(rowProvider.getSchema()));
		Rows rows = new Rows(this, query, new Window());
		long id = rows.insert(rowProvider);
		return new Entity(this, rowProvider.getSchema(), id);
	}

    public Session getSession() {
        return session;
    }

}
