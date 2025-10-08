package org.example.practicanosqlyubo.DAO;

import org.example.practica1medicoyubo.domain.Especialidad;
import org.example.practica1medicoyubo.util.R;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * ClassName: EspecialidadDAO
 * Package: org.example.practica1medicoyubo.DAO
 * Description:
 *
 * @Author Yubo
 * @Create 05/10/2025 19:59
 * @Version 1.0
 */
public class EspecialidadDAO {

    private Connection conexion;

    public void conectar() throws ClassNotFoundException, SQLException, IOException {
        Properties configuration = new Properties();
        configuration.load(R.getProperties("database.properties"));
        String host = configuration.getProperty("host");
        String port = configuration.getProperty("port");
        String name = configuration.getProperty("name");
        String username = configuration.getProperty("username");
        String password = configuration.getProperty("password");

        Class.forName("com.mysql.cj.jdbc.Driver");
        conexion = DriverManager.getConnection(
                "jdbc:mysql://" + host + ":" + port + "/" + name + "?serverTimezone=UTC",
                username, password
        );
    }
    public void desconectar() throws SQLException {
        if (conexion != null) {
            conexion.close();
        }
    }

    public List<Especialidad> obtenerTodas() throws SQLException {
        List<Especialidad> especialidades = new ArrayList<>();
        String sql = "SELECT idEsp, nombreEsp FROM especialidad";

        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                int id = rs.getInt("idEsp");
                String nombre = rs.getString("nombreEsp");
                especialidades.add(new Especialidad(id, nombre));
            }
        }
        return especialidades;
    }


}
