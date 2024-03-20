package persistence.sql.entity.context;

public enum EntityStatus {
    MANAGED,
    READ_ONLY,
    DELETED,
    GONE,
    LOADING,
    SAVING
}