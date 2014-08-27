/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Baked with love by corbett
 * Project: wowreg
 * Package: net.cworks.wowreg.db.conn
 * Class: PooledConn
 * Created: 8/25/14 4:15 PM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package net.cworks.wowreg.db.conn;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class PooledConn extends ConnWrapper {

    private ConnPool pool;
    private String key;
    private Connection conn;
    private boolean closed = false;

    public PooledConn(ConnPool pool, String key, Connection conn) {
        super(conn);
        this.pool = pool;
        this.key = key;
        this.conn = conn;
    }

    public void close() throws SQLException {
        if(!closed) {
            closed = true;
            pool.returnConnection(key, conn);
        }
    }

    public Statement createStatement() throws SQLException {
        throwIfClosed();
        return super.createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        throwIfClosed();
        return super.prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        throwIfClosed();
        return super.prepareCall(sql);
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        throwIfClosed();
        return super.nativeSQL(sql);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        throwIfClosed();
        super.setAutoCommit(autoCommit);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        throwIfClosed();
        return super.getAutoCommit();
    }

    @Override
    public void commit() throws SQLException {
        throwIfClosed();
        super.commit();
    }

    @Override
    public void rollback() throws SQLException {
        throwIfClosed();
        super.rollback();
    }

    @Override
    public boolean isClosed() throws SQLException {
        throwIfClosed();
        return super.isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        throwIfClosed();
        return super.getMetaData();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        throwIfClosed();
        super.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        throwIfClosed();
        return super.isReadOnly();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        throwIfClosed();
        super.setCatalog(catalog);
    }

    @Override
    public String getCatalog() throws SQLException {
        throwIfClosed();
        return super.getCatalog();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        throwIfClosed();
        super.setTransactionIsolation(level);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        throwIfClosed();
        return super.getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        throwIfClosed();
        return super.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        throwIfClosed();
        super.clearWarnings();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency)
            throws SQLException {
        throwIfClosed();
        return super.createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType,
        int resultSetConcurrency) throws SQLException {
        throwIfClosed();
        return super.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
        throws SQLException {
        throwIfClosed();
        return super.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        throwIfClosed();
        return super.getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        throwIfClosed();
        super.setTypeMap(map);
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        throwIfClosed();
        super.setHoldability(holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
        throwIfClosed();
        return super.getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        throwIfClosed();
        return super.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        throwIfClosed();
        return super.setSavepoint(name);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        throwIfClosed();
        super.rollback(savepoint);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        throwIfClosed();
        super.releaseSavepoint(savepoint);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency,
        int resultSetHoldability) throws SQLException {
        throwIfClosed();
        return super.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType,
        int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        throwIfClosed();
        return super.prepareStatement(sql, resultSetType,
                resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType,
        int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        throwIfClosed();
        return super.prepareCall(sql, resultSetType,
                resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
        throws SQLException {
        throwIfClosed();
        return super.prepareStatement(sql, autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
        throws SQLException {
        throwIfClosed();
        return super.prepareStatement(sql, columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames)
        throws SQLException {
        throwIfClosed();
        return super.prepareStatement(sql, columnNames);
    }

    @Override
    public Clob createClob() throws SQLException {
        throwIfClosed();
        return super.createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        throwIfClosed();
        return super.createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        throwIfClosed();
        return super.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        throwIfClosed();
        return super.createSQLXML();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        throwIfClosed();
        return super.isValid(timeout);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        if(closed) {
            throw new SQLClientInfoException();
        }
        super.setClientInfo(name, value);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        if(closed) {
            throw new SQLClientInfoException();
        }
        super.setClientInfo(properties);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        throwIfClosed();
        return super.getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        throwIfClosed();
        return super.getClientInfo();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        throwIfClosed();
        return super.createArrayOf(typeName, elements);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        throwIfClosed();
        return super.createStruct(typeName, attributes);
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        throwIfClosed();
        super.setSchema(schema);
    }

    @Override
    public String getSchema() throws SQLException {
        throwIfClosed();
        return super.getSchema();
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        throwIfClosed();
        super.abort(executor);
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        throwIfClosed();
        super.setNetworkTimeout(executor, milliseconds);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        throwIfClosed();
        return super.getNetworkTimeout();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throwIfClosed();
        return super.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throwIfClosed();
        return super.isWrapperFor(iface);
    }

    private void throwIfClosed() throws SQLException {
        if(closed) {
            throw new SQLException("Connection is closed.");
        }
    }
}
