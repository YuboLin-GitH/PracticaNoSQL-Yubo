package org.example.practicanosqlyubo.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.practicanosqlyubo.DAO.EspecialidadDAO;
import org.example.practicanosqlyubo.DAO.CitaDAO;
import org.example.practicanosqlyubo.domain.Cita;
import org.example.practicanosqlyubo.domain.Especialidad;
import org.example.practicanosqlyubo.util.AlertUtils;


import java.util.List;

public class CitaController {

    @FXML private TextField tfDNI, tfNombre, tfDireccion, tfTelefono;
    @FXML private DatePicker dpFechaCita;
    @FXML private ComboBox<Especialidad> cbEspecialidad;
    @FXML private TableView<Cita> tvCitasPaciente;
    @FXML private TableColumn<Cita, Integer> colIdCita;
    @FXML private TableColumn<Cita, String> colFecha, colEspecialidad;


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
        List<Especialidad> lista = EspecialidadDAO.obtenerTodas();
        cbEspecialidad.setItems(FXCollections.observableArrayList(lista));
    }

    @FXML
    private void nuevaCita() {
        try {
            if (cbEspecialidad.getValue() == null || dpFechaCita.getValue() == null) {
                AlertUtils.mostrarError("Por favor, completa todos los campos requeridos.");
                return;
            }

            Cita c = new Cita();
            c.setDni(tfDNI.getText());
            c.setNombre(tfNombre.getText());
            c.setDireccion(tfDireccion.getText());
            c.setTelefono(tfTelefono.getText());
            c.setFecha(dpFechaCita.getValue().toString());
            c.setEspecialidad(cbEspecialidad.getValue().getNombre());

            CitaDAO.insertarCita(c);
            AlertUtils.mostrarInformacion("Cita guardada correctamente.");
            mostrarCitas();
            limpiarCajas();

        } catch (Exception e) {
            AlertUtils.mostrarError("Error al guardar la cita: " + e.getMessage());
        }
    }

    @FXML
    private void verCita() {
        mostrarCitas();
    }

    @FXML
    private void modificarCita() {
        try {
            Cita citaSeleccionada = tvCitasPaciente.getSelectionModel().getSelectedItem();


            if (citaSeleccionada == null) {
                AlertUtils.mostrarError("Por favor, selecciona una cita para modificar.");
                return;
            }
            if (dpFechaCita.getValue() == null || cbEspecialidad.getValue() == null) {
                AlertUtils.mostrarError("Selecciona una fecha y una especialidad.");
                return;
            }

            citaSeleccionada.setFecha(dpFechaCita.getValue().toString());
            citaSeleccionada.setEspecialidad(cbEspecialidad.getValue().getNombre());

            CitaDAO.modificarCita(citaSeleccionada);

            AlertUtils.mostrarInformacion("Cita modificada correctamente.");
            mostrarCitas();
            limpiarCajas();

        } catch (Exception e) {
            AlertUtils.mostrarError("Error al modificar la cita: " + e.getMessage());
        }
    }


    @FXML
    private void borrarCita() {
        try {
            Cita citaSeleccionada = tvCitasPaciente.getSelectionModel().getSelectedItem();

            if (citaSeleccionada == null) {
                AlertUtils.mostrarError("Por favor, selecciona una cita que deseas eliminar.");
                return;
            }

            CitaDAO.borrarCita(citaSeleccionada.getIdCita());

            AlertUtils.mostrarInformacion("Cita eliminada correctamente.");
            mostrarCitas();
            limpiarCajas();

        } catch (Exception e) {
            AlertUtils.mostrarError("Error al eliminar la cita: " + e.getMessage());
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

            dpFechaCita.setValue(java.time.LocalDate.parse(c.getFecha()));

            for (Especialidad e : cbEspecialidad.getItems()) {
                if (e.getNombre().equals(c.getEspecialidad())) {
                    cbEspecialidad.setValue(e);
                    break;
                }
            }
        }
    }

    private void limpiarCajas() {
        dpFechaCita.setValue(null);
        cbEspecialidad.setValue(null);
    }
}