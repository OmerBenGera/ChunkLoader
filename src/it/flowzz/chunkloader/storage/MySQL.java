package it.flowzz.chunkloader.storage;

import it.flowzz.chunkloader.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class MySQL {

    private Connection connection;
    private String host,database,username,password;
    private int port;

     public MySQL(Main plugin){
         this.host = plugin.getConfig().getString("MySQL.host");
         this.port = plugin.getConfig().getInt("MySQL.port");
         this.database = plugin.getConfig().getString("MySQL.database");
         this.username = plugin.getConfig().getString("MySQL.username");
         this.password = plugin.getConfig().getString("MySQL.password");

         try{
             synchronized(this) {
                 if (getConnection() != null && !getConnection().isClosed()) {
                     return;
                 }
                 Class.forName("com.mysql.jdbc.Driver");
                 setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password));
                 Statement statement = connection.createStatement();
                 statement.execute("CREATE TABLE IF NOT EXISTS `" + database + "`.`loaders` ( `LOCATION` VARCHAR(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL , `TIME` INT(64) NOT NULL , `ISPERMANENT` BOOLEAN NOT NULL)CHARSET=utf8 COLLATE utf8_general_ci;");
             }
         }
         catch(Exception exception){
            exception.printStackTrace();
         }
     }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
