package org.example.practicanosqlyubo.controller;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.bson.Document;
import org.example.practicanosqlyubo.util.ConnectionDB;
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
    @FXML private TableColumn<Cita, Integer> colIdCita;    // 必须用Integer
    @FXML private TableColumn<Cita, String> colFecha, colEspecialidad;

    private Paciente paciente;

    public void initialize() {
        cargarEspecialidades();

        tfDNI.setEditable(false);
        tfNombre.setEditable(false);
        tfDireccion.setEditable(false);
        tfTelefono.setEditable(false);

        colIdCita.setCellValueFactory(new PropertyValueFactory<>("idCita"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colEspecialidad.setCellValueFactory(new PropertyValueFactory<>("especialidad"));

        tvCitasPaciente.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            verSeleccionarCita();
        });

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
        CitaDAO.insertarCita(c);       // DAO自动分配idCita
        mostrarCitas();
    }

    @FXML
    private void verCita() {
        mostrarCitas();
    }

    @FXML
    private void modificarCita() {
        Cita citaSeleccionada = tvCitasPaciente.getSelectionModel().getSelectedItem();
        if (citaSeleccionada != null && cbEspecialidad.getValue() != null && dpFechaCita.getValue() != null) {
            citaSeleccionada.setFecha(dpFechaCita.getValue().toString());
            citaSeleccionada.setEspecialidad(cbEspecialidad.getValue().getNombre());
            // 你也可以同步改其它字段
            CitaDAO.modificarCita(citaSeleccionada);
            mostrarCitas();
        }
    }

    @FXML
    private void borrarCita() {
        Cita citaSeleccionada = tvCitasPaciente.getSelectionModel().getSelectedItem();
        if (citaSeleccionada != null) {
            CitaDAO.borrarCita(citaSeleccionada.getIdCita());
            mostrarCitas();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Elegir cita te quire eliminar ");
            alert.showAndWait();
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

    @FXML
    private void verSeleccionarCita() {
        Cita c = tvCitasPaciente.getSelectionModel().getSelectedItem();
        if (c != null) {
            // 设置 日期（DatePicker）
            dpFechaCita.setValue(java.time.LocalDate.parse(c.getFecha()));
            // 设置 专科（ComboBox）
            for (Especialidad e : cbEspecialidad.getItems()) {
                if (e.getNombre().equals(c.getEspecialidad())) {
                    cbEspecialidad.setValue(e);
                    break;
                }
            }
        }
    }


}
