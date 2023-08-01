package persistence.sql.dml;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.stream.Collectors;
import persistence.sql.Entity;
import persistence.sql.EntityUtils;
import persistence.sql.Id;
import persistence.sql.QueryBuilder;

public class DmlQueryBuilder<T> extends QueryBuilder {
    private static final String INSERT_QUERY = "insert into %s (%s) values (%s)";
    private static final String UPDATE_QUERY = "update %s set %s ";
    private static final String DELETE_QUERY = "delete from %s %s";
    private static final String SELECT_CLAUSE = "select %s";
    private static final String FROM_CLAUSE = "from %s";
    private static final String WHERE_CLAUSE = "where %s";

    public DmlQueryBuilder(Class<T> entity) {
        super(entity);
    }

    public String insert(Object instance) {
        final String tableName = getTableName();
        final Entity target = new Entity(instance);
        return String.format(
                INSERT_QUERY,
                tableName,
                joinWithComma(columns.getColumnNames()),
                joinWithComma(target.getValues())
        );
    }

    public String update(Object entity) {
        final String tableName = getTableName();
        final Entity target = new Entity(entity);
        return String.format(
                UPDATE_QUERY,
                tableName,
                target.valuesByField().entrySet().stream()
                        .map(e -> e.getKey() + "=" + e.getValue())
                        .collect(Collectors.joining(","))
        ) + whereClause(new Id(entity));
    }

    public String update(Object entity, Map<Field, Object> valuesByColumnName) {
        final String tableName = getTableName();
        return String.format(
                UPDATE_QUERY,
                tableName,
                valuesByColumnName.entrySet().stream()
                        .map(e -> EntityUtils.getColumnName(e.getKey()) + "=" + e.getValue())
                        .collect(Collectors.joining(","))
        ) + whereClause(new Id(entity));
    }

    public String findAll() {
        return selectClause() +
                BLANK +
                fromClause();
    }

    public String findById(Object value) {
        final Id id = new Id(this.id.getName(), value);
        return selectClause() + BLANK + fromClause() + BLANK + whereClause(id);
    }

    public <T> String delete(T instance) {
        String tableName = getTableName();
        final Id id = new Id(instance);
        String whereClause = whereClause(id);
        return String.format(DELETE_QUERY, tableName, whereClause);
    }

    private String whereClause(Id id) {
        StringBuilder sb = new StringBuilder();
        sb.append(id.getName());
        sb.append("=");
        sb.append(id.getValue());
        return String.format(WHERE_CLAUSE, sb);
    }

    private String selectClause() {
        return String.format(SELECT_CLAUSE, joinWithComma(columns.getColumnNames()));
    }

    private String fromClause() {
        return String.format(FROM_CLAUSE, getTableName());
    }
}