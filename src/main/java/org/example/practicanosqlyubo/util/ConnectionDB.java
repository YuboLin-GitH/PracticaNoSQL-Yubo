package org.example.practicanosqlyubo.util;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import java.util.Properties;



public class ConnectionDB {
    public static MongoClient conectar()  {
        try {
            Properties configuration = new Properties();
            configuration.load(R.getProperties("database.properties"));
            String user = configuration.getProperty("user");
            String password = configuration.getProperty("password");
            String host = configuration.getProperty("host");
            String port = configuration.getProperty("port");


            final MongoClient conexion = new MongoClient(new MongoClientURI("mongodb://"+user+":"+password+"@"+host+":"+port+"/?authSource=admin"));
            System.out.println("Conectada correctamente a la BD");
            return conexion;
        } catch (Exception e) {
            System.out.println("Conexion Fallida");
            System.out.println(e);
            return null;
        }
    }

    public static void desconectar(MongoClient con) {
        con.close();
    }

}
