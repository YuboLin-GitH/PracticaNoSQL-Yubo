package org.example.practicanosqlyubo.DAO;

import org.example.practica1medicoyubo.domain.Cita;
import org.example.practica1medicoyubo.util.R;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CitaDAO {
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
        conexion = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + name + "?serverTimezone=UTC",
                username, password);
    }


    public void desconectar() throws SQLException {
        conexion.close();
    }

    public void guardarCita(Cita cita) throws SQLException {
        String sql = "INSERT INTO cita (idCita, fechaCita, fk_idEsp, fk_idPaciente) VALUES (?, ?, ?, ?)";
        PreparedStatement sentencia = conexion.prepareStatement(sql);
        sentencia.setInt(1, cita.getIdCita());
        sentencia.setDate(2, new Date(cita.getFechaCita().getTime()));
        sentencia.setInt(3, cita.getFkIdEsp());
        sentencia.setInt(4, cita.getFkIdPaciente());
        sentencia.executeUpdate();
    }

    public List<Cita> obtenerCitaPorPacienteId(int pacienteId) throws SQLException {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT c.idCita, c.fechaCita, e.idEsp, e.nombreEsp " +
                "FROM cita c JOIN especialidad e ON c.fk_idEsp = e.idEsp " +
                "WHERE c.fk_idPaciente = ?";
        PreparedStatement sentencia = conexion.prepareStatement(sql);
        sentencia.setInt(1, pacienteId);
        ResultSet rs = sentencia.executeQuery();
        while (rs.next()) {
            Cita cita = new Cita();
            cita.setIdCita(rs.getInt("c.idCita"));
            cita.setFechaCita(rs.getDate("c.fechaCita"));
            cita.setFkIdPaciente(pacienteId);
            cita.setFkIdEsp(rs.getInt("e.idEsp"));
            cita.setNombreEsp(rs.getString("e.nombreEsp"));
            citas.add(cita);
        }
        return citas;
    }


    public void modificarCita(Cita citaAntiguo, Cita citaNuevo) throws SQLException {
        String sql = "UPDATE cita SET fechaCita = ?, fk_idEsp = ? WHERE idCita = ?";
        PreparedStatement sentencia = conexion.prepareStatement(sql);
        sentencia.setDate(1, new Date(citaNuevo.getFechaCita().getTime()));
        sentencia.setInt(2, citaNuevo.getFkIdEsp());
        sentencia.setInt(3, citaAntiguo.getIdCita());
        sentencia.executeUpdate();
    }

    public void eliminarCita(Cita cita) throws SQLException {
        String sql = "DELETE FROM cita WHERE idCita = ?";

        PreparedStatement sentencia = conexion.prepareStatement(sql);
        sentencia.setInt(1, cita.getIdCita());
        sentencia.executeUpdate();
    }



    public int obtenerSiguienteIdCita() throws SQLException {
        String sql = "SELECT MAX(idCita) AS ultimo FROM cita";
        Statement st = conexion.createStatement();
        ResultSet rs = st.executeQuery(sql);
        if (rs.next()) {
            return rs.getInt("ultimo") + 1;
        }
        return 1;
    }



}
