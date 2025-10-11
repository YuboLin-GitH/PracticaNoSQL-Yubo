package org.example.practicanosqlyubo.controller;


import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import org.bson.Document;
import org.example.practicanosqlyubo.ConnectionDB;
import org.example.practicanosqlyubo.DAO.CitaDAO;

import org.example.practicanosqlyubo.domain.Cita;
import org.example.practicanosqlyubo.domain.Especialidad;
import org.example.practicanosqlyubo.domain.Paciente;


import java.util.ArrayList;
import java.util.List;


public class CitaController {

    @FXML private TextField tfDNI, tfNombre, tfDireccion, tfTelefono;
    @FXML private DatePicker dpFechaCita;
    @FXML private ComboBox<Especialidad> cbEspecialidad;
    @FXML private TableView<Cita> tvCitasPaciente;
    @FXML private TableColumn<Cita, String> colIdCita, colFecha, colEspecialidad;


    private Paciente paciente;

    private Cita citaSeleccionada;


        public void initialize() {
            cargarEspecialidades();
        }

        private void cargarEspecialidades() {
            List<Especialidad> lista = new ArrayList<>();
            MongoClient con = ConnectionDB.conectar();
            MongoDatabase db = con.getDatabase("centro_medico");
            MongoCollection<Document> col = db.getCollection("especialidades");

            for (Document d : col.find()) {
                Especialidad e = new Especialidad();
                e.setNombre(d.getString("nombre"));
                lista.add(e);
            }
            cbEspecialidad.setItems(FXCollections.observableArrayList(lista));
            con.close();
        }

        @FXML
        private void nuevaCita() {
            Cita c = new Cita();
            c.setDni(tfDNI.getText());
            c.setNombre(tfNombre.getText());
            c.setDireccion(tfDireccion.getText());
            c.setTelefono(tfTelefono.getText());
            c.setFecha(dpFechaCita.getValue().toString());
            c.setEspecialidad(cbEspecialidad.getValue().getNombre());
            CitaDAO.insertarCita(c);
            mostrarCitas();
        }

        @FXML
        private void verCita() {
            mostrarCitas();
        }

        @FXML
        private void modificarCita() {
            if (cbEspecialidad.getValue() != null && dpFechaCita.getValue() != null) {
                Cita c = new Cita();
                c.setDni(tfDNI.getText());
                c.setFecha(dpFechaCita.getValue().toString());
                c.setEspecialidad(cbEspecialidad.getValue().getNombre());
                CitaDAO.modificarCita(c);
                mostrarCitas();
            }
        }

        @FXML
        private void borrarCita() {
            if (dpFechaCita.getValue() != null) {
                CitaDAO.borrarCita(tfDNI.getText(), dpFechaCita.getValue().toString());
                mostrarCitas();
            }
        }

        private void mostrarCitas() {
            List<Cita> lista = CitaDAO.obtenerCitasPorDNI(tfDNI.getText());
            tvCitasPaciente.setItems(FXCollections.observableList(lista));
        }

        public void cargarPaciente(String dni, String nombre, String direccion, String telefono) {
            tfDNI.setText(dni);
            tfNombre.setText(nombre);
            tfDireccion.setText(direccion);
            tfTelefono.setText(telefono);
        }

}