/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.DriverManager;
import java.util.Properties;
import javax.sql.DataSource;


/**
 *
 * @author suidi
 */
public class MyConnection {
        
    private final DataSource db;
    private String jdbcUrl =null;
    private String username = null;
    private String password = null;
    private int connections = 0;
    
    public MyConnection(Properties props) {
        this.jdbcUrl = props.getProperty("mysql.jdbcUrl");
        this.username = props.getProperty("mysql.username");
        this.password = props.getProperty("mysql.password");
        this.connections = Integer.parseInt(props.getProperty("mysql.connections"));
       
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(connections);
        
        this.db = new HikariDataSource(config);
        
        System.out.println("+-------------------------------+");
        System.out.println("|     Connected to database     |");
        System.out.println("+-------------------------------+");
    }
    
    public DataSource getDB() {
        return this.db;
    }
}
