package org.madogiwa.plaintable.criteria;

import org.madogiwa.plaintable.Session;
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

    public Session getSession() {
        return session;
    }

}
