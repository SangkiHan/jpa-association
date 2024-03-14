package database.sql.dml;

import database.mapping.ColumnValueMap;

import java.util.Map;

public class QueryBuilder {
    private static final QueryBuilder INSTANCE = new QueryBuilder();

    private QueryBuilder() {
    }

    public static QueryBuilder getInstance() {
        return INSTANCE;
    }

    /* SELECT */
    public String buildSelectQuery(Class<?> clazz) {
        Select select = new Select(clazz);
        return select.buildQuery();
    }

    public String buildSelectPrimaryKeyQuery(Class<?> clazz, Long id) {
        SelectByPrimaryKey selectByPrimaryKey = new SelectByPrimaryKey(clazz);
        return selectByPrimaryKey.buildQuery(id);
    }

    /* INSERT */
    public String buildCustomSelectQuery(Class<?> clazz) {
        CustomSelect customSelect = new CustomSelect(clazz);
        return customSelect.buildQuery();
    }

    public String buildInsertQuery(Class<?> clazz, Map<String, Object> valueMap) {
        return new Insert(clazz)
                .values(valueMap)
                .toQueryString();
    }

    public String buildInsertQuery(Object entity) {
        return new Insert(entity.getClass())
                .values(columnValues(entity))
                .toQueryString();
    }

    private Map<String, Object> columnValues(Object entity) {
        return ColumnValueMap.valueMapFromEntity(entity);
    }

    /* UPDATE */
    public String buildUpdateQuery(long id, Object entity) {
        Map<String, Object> map = ColumnValueMap.valueMapFromEntity(entity);
        return new Update(entity.getClass()).buildQuery(id, map);
    }

    /* DELETE */
    public String buildDeleteQuery(Class<?> clazz, Map<String, Object> conditionMap) {
        Delete delete = new Delete(clazz);
        return delete.buildQuery(conditionMap);
    }

    public String buildDeleteQuery(Class<?> clazz, Long id) {
        return buildDeleteQuery(clazz, Map.of("id", id));
    }
}