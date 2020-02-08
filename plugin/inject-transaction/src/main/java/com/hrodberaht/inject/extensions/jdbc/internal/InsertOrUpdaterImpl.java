package com.hrodberaht.inject.extensions.jdbc.internal;

import com.hrodberaht.inject.extensions.jdbc.InsertOrUpdater;

import java.util.HashMap;
import java.util.Map;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 * 2010-aug-21 22:14:07
 * @version 1.0
 * @since 1.0
 */
public class InsertOrUpdaterImpl implements InsertOrUpdater {

    private Map<String, Object> where = new HashMap<String, Object>();
    private Map<String, Object> fields = new HashMap<String, Object>();
    private String table;

    private String preparedStmt = null;

    private boolean forceUpdate = false;

    public InsertOrUpdaterImpl() {
    }

    public InsertOrUpdater table(String table) {
        this.table = table;
        return this;
    }

    public InsertOrUpdater where(String name, Object value) {
        if (where.containsKey(name)) {
            where.remove(name);
        }
        where.put(name, value);
        return this;
    }

    public void forceUpdate() {
        this.forceUpdate = true;
    }

    public InsertOrUpdater field(String name, Object value) {
        if (fields.containsKey(name)) {
            fields.remove(name);
        }
        fields.put(name, value);
        return this;
    }

    public Object[] getArgs() {
        Object[] args = new Object[fields.size() + where.size()];
        int pos = 0;
        for (String key : fields.keySet()) {
            args[pos] = fields.get(key);
            pos++;
        }
        for (String key : where.keySet()) {
            args[pos] = where.get(key);
            pos++;
        }
        return args;
    }

    public String getPreparedSql() {
        if (where.size() == 0 && !forceUpdate) {
            return getPreparedSqlInsert();
        } else {
            return getPreparedSqlUpdate();
        }
    }

    private String getPreparedSqlInsert() {
        if (preparedStmt != null) {
            return preparedStmt;
        }
        StringBuffer buff = new StringBuffer();
        buff.append(" insert into ");
        buff.append(table);
        int i = 0;
        int size = fields.size() - 1;
        for (String key : fields.keySet()) {
            if (i == 0) buff.append("( ");
            if (i != 0) buff.append(", ");
            buff.append(key);
            if (i == size) buff.append(") ");
            i++;
        }
        buff.append(" values ");
        i = 0;
        for (String key : fields.keySet()) {
            if (i == 0) buff.append("(");
            if (i != 0) buff.append(", ");
            buff.append("?");
            if (i == size) buff.append(") ");
            i++;
        }
        preparedStmt = buff.toString();
        return preparedStmt;
    }

    private String getPreparedSqlUpdate() {
        if (preparedStmt != null) {
            return preparedStmt;
        }
        StringBuffer buff = new StringBuffer();
        buff.append(" update ");
        buff.append(table);
        buff.append(" set ");
        int i = 0;
        for (String key : fields.keySet()) {
            if (i != 0) buff.append(", ");
            buff.append(" " + key + " = ?");
            i++;
        }
        buff.append(" where ");
        i = 0;
        for (String key : where.keySet()) {
            if (i != 0) buff.append(" AND ");
            buff.append(" " + key + " = ?");
            i++;
        }
        preparedStmt = buff.toString();
        return preparedStmt;
    }
}
