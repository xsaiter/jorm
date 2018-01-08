package jorm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public abstract class Mapper<T extends Entity> {
    private final DbContext _context;
    private HashMap<Long, Entity> _loaded = new HashMap<>();

    protected Mapper(DbContext context) {
        _context = context;
    }

    public T find(long id) {
        if (_loaded.containsKey(id)) {
            return (T) _loaded.get(id);
        }
        T entity = loadById(id);
        if (entity != null) {
            prepareEntity(entity);
            addToLoaded(entity);
        }
        return entity;
    }

    protected T loadById(long id) {
        Connection conn = getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = prepareStatement(conn, sqlQueryById());
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return map(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(stmt);
            close(conn);
        }
    }

    protected abstract String sqlQueryById();

    protected abstract T map(ResultSet rs) throws SQLException;

    protected void addToLoaded(Entity entity) {
        if (!_loaded.containsKey(entity.getId())) {
            _loaded.put(entity.getId(), entity);
        }
    }

    public abstract void insert(Entity entity);

    public abstract void update(Entity entity);

    public abstract void delete(Entity entity);

    protected Connection getConnection() {
        return ConnectionManager.getConnection();
    }

    void prepareEntity(Entity entity) {
        entity.setContext(_context);
    }

    PreparedStatement prepareStatement(Connection connection, String sql) {
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            return stmt;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void close(PreparedStatement stmt) {
        try {
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void close(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
