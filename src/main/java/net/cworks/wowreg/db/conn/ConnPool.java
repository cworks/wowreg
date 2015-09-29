/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Baked with love by corbett
 * Project: wowreg
 * Package: net.cworks.wowreg.db.conn
 * Class: ConnPool
 * Created: 8/25/14 4:13 PM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package net.cworks.wowreg.db.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class ConnPool {

    /**
     * Map a connection name to a Connection instance
     */
    private Map connectionMap = new HashMap();

    /**
     * JDBC Url
     */
    private String url = null;

    /**
     * DB User
     */
    private String user = null;

    /**
     * DB User's password
     */
    private String password = null;

    /**
     * Create a pool for the url, user and password
     * @param url
     * @param user
     * @param password
     */
    public ConnPool(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    /**
     * Return a Connection from the pool
     * @return
     * @throws java.sql.SQLException
     */
    public Connection connection()
            throws SQLException {

        String key = getKey(url, user, password);
        // see if theres a matching connection list in pool, if not create
        List connections = null;
        synchronized(connectionMap) {
            connections = (List)connectionMap.get(key);
            if(connections == null) {
                connections = new LinkedList();
                connectionMap.put(key, connections);
            }
        }

        // return a connection if there is one, otherwise create a new one
        Connection conn;
        synchronized(connections) {
            if(connections.size() > 0) {
                conn = (Connection)connections.remove(0);
            } else {
                conn = DriverManager.getConnection(url, user, password);
            }
        }

        // Decorate the connection as a PooledConnection which will be returned to pool
        // when client closes it.
        conn = new PooledConn(this, key, conn);

        return conn;
    }

    /**
     * When close is called on a PoolConnection the call lands here and connection is
     * returned to pool.
     * @param key
     * @param conn
     */
    void returnConnection(String key, Connection conn) {
        List connections;
        synchronized(connectionMap) {
            connections = (List)connectionMap.get(key);
        }
        synchronized(connections) {
            connections.add(conn);
        }
    }

    String getKey(String url, String username, String password) {
        return url + ":" + username + ":" + password;
    }

}
