package org.example.practicanosqlyubo.controller;


import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.example.practica1medicoyubo.DAO.CitaDAO;
import org.example.practica1medicoyubo.DAO.EspecialidadDAO;
import org.example.practica1medicoyubo.DAO.UsuarioDAO;
import org.example.practica1medicoyubo.domain.Cita;
import org.example.practica1medicoyubo.domain.Especialidad;
import org.example.practica1medicoyubo.domain.Paciente;
import org.example.practica1medicoyubo.util.AlertUtils;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;


public class CitaController {
    @FXML
    public TextField tfTelefono;
    @FXML
    public TextField tfNombre;
    @FXML
    public TextField tfDireccion;
    @FXML
    public TextField tfDNI;

    @FXML
    public Button btVerCita;
    @FXML
    public Button btNuevaCita;
    @FXML
    public Button btBorrarCita;
    @FXML
    public Button btModificarCita;

    @FXML
    public DatePicker dpFechaCita;

    @FXML
    public ComboBox<Especialidad> cbEspecialidad;

    @FXML
    public TableView<Cita> tvCitasPaciente;
    @FXML
    private TableColumn<Cita, Integer> colIdCita;
    @FXML
    private TableColumn<Cita, Date> colFecha;
    @FXML
    private TableColumn<Cita, String> colEspecialidad;


    private final CitaDAO citaDAO ;
    private Paciente paciente;

    private Cita citaSeleccionada;



    public CitaController() {
        citaDAO = new CitaDAO();

        try {
            citaDAO.conectar();
        } catch (SQLException sqle) {
            AlertUtils.mostrarError("Error al conectar con la base de datos");
        } catch (ClassNotFoundException cnfe) {
            AlertUtils.mostrarError("Error al iniciar la aplicación");
        } catch (IOException ioe) {
            AlertUtils.mostrarError("Error al cargar la configuración");
        }

        System.out.println(System.getProperty("user.home"));
    }

    @FXML
    public void initialize() {

        colIdCita.setCellValueFactory(new PropertyValueFactory<>("idCita"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaCita"));
        colEspecialidad.setCellValueFactory(new PropertyValueFactory<>("nombreEsp"));

        cargarEspecialidades();

        enlazarSeleccionDeTabla();
        tfDNI.setOnKeyPressed(this::manejarEnterParaVerCita);
    }

    private void manejarEnterParaVerCita(KeyEvent event) {

        if (event.getCode() == KeyCode.ENTER) {
            verCita();
        }
    }


    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
        mostrarDatosPaciente();
    }
    private void mostrarDatosPaciente() {
        tfNombre.setText(paciente.getNombre());
        tfDireccion.setText(paciente.getDireccion());
        tfTelefono.setText(String.valueOf(paciente.getTelefono()));
        tfDNI.setText(paciente.getDni());


        tfNombre.setDisable(true);
        tfDireccion.setDisable(true);
        tfTelefono.setDisable(true);
    }


    private void cargarEspecialidades() {
        EspecialidadDAO especialidadDAO = new EspecialidadDAO();
        try {

            especialidadDAO.conectar();
            List<Especialidad> especialidades = especialidadDAO.obtenerTodas();
            if (especialidades.isEmpty()) {
                AlertUtils.mostrarError("Error al obtener las especialidades");
                return;
            }

            cbEspecialidad.getItems().addAll(especialidades);

            for (Especialidad esp : especialidades) {
                if ("Cirugía".equals(esp.getNombreEsp())) {
                    cbEspecialidad.setValue(esp);
                    break;
                }
            }
        } catch (Exception e) {

            AlertUtils.mostrarError("Error：" + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                especialidadDAO.desconectar();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void enlazarSeleccionDeTabla() {
        tvCitasPaciente.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                citaSeleccionada = newVal;
                if (newVal.getFechaCita() != null) {
                    dpFechaCita.setValue(((Date) newVal.getFechaCita()).toLocalDate());
                }
                for (Especialidad esp : cbEspecialidad.getItems()) {
                    if (esp.getIdEsp() == newVal.getFkIdEsp()) {
                        cbEspecialidad.setValue(esp);
                        break;
                    }
                }
            }
        });
    }



    @FXML
    public void verCita() {


        try {
            String dniIngresado = tfDNI.getText().trim();
            if (dniIngresado.isEmpty()) {
                AlertUtils.mostrarError("Introduce un DNI válido");
                return;
            }


            UsuarioDAO usuarioDAO = new UsuarioDAO();
            usuarioDAO.conectar();
            Paciente nuevoPaciente = usuarioDAO.buscarPorDni(dniIngresado);
            usuarioDAO.desconectar();

            if (nuevoPaciente == null) {
                AlertUtils.mostrarError("No se encontró paciente con ese DNI");
                return;
            }


            this.paciente = nuevoPaciente;
            mostrarDatosPaciente();


            citaDAO.conectar();
            List<Cita> citas = citaDAO.obtenerCitaPorPacienteId(paciente.getIdPaciente());
            tvCitasPaciente.setItems(FXCollections.observableArrayList(citas));
            citaDAO.desconectar();

        } catch (Exception e) {
            AlertUtils.mostrarError("Error al buscar paciente: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    public void nuevaCita() {

        if (paciente == null) {
            AlertUtils.mostrarError("Introduce DNI de paciente");
            return;
        }
        LocalDate fechaSeleccionada = dpFechaCita.getValue();
        Especialidad espSeleccionada = cbEspecialidad.getValue();
        if (fechaSeleccionada == null || espSeleccionada == null) {
            AlertUtils.mostrarError("Elegir fecha de cita o Especialidad");
            return;
        }

        try {
            citaDAO.conectar();
            int nuevoId = citaDAO.obtenerSiguienteIdCita();

            Cita nuevaCita = new Cita();
            nuevaCita.setIdCita(nuevoId);
            nuevaCita.setFechaCita(Date.valueOf(fechaSeleccionada));
            nuevaCita.setFkIdEsp(espSeleccionada.getIdEsp());
            nuevaCita.setFkIdPaciente(paciente.getIdPaciente());


            citaDAO.guardarCita(nuevaCita);
            AlertUtils.mostrarInformacion("Cita creado：" + nuevoId);


            verCita();
            limpiarCajas();
        } catch (Exception e) {
            AlertUtils.mostrarError("Error：" + e.getMessage());
        } finally {
            try {
                citaDAO.desconectar();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void modificarCita() {

        if (citaSeleccionada == null) {
            AlertUtils.mostrarError("El seleccionado no existe");
            return;
        }
        LocalDate fechaModificada = dpFechaCita.getValue();
        Especialidad espModificada = cbEspecialidad.getValue();
        if (fechaModificada == null || espModificada == null) {
            AlertUtils.mostrarError("Eliger bien cita y especificada");
            return;
        }

        try {
            citaDAO.conectar();

            Cita citaModificada = new Cita();
            citaModificada.setIdCita(citaSeleccionada.getIdCita());
            citaModificada.setFechaCita(Date.valueOf(fechaModificada));
            citaModificada.setFkIdEsp(espModificada.getIdEsp());
            citaModificada.setFkIdPaciente(paciente.getIdPaciente());


            citaDAO.modificarCita(citaSeleccionada, citaModificada);
            AlertUtils.mostrarInformacion("Cita actualizada");


            verCita();
            limpiarCajas();
            citaSeleccionada = null;
        } catch (Exception e) {
            AlertUtils.mostrarError("Error：" + e.getMessage());
        } finally {
            try {
                citaDAO.desconectar();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void borrarCita() {

        if (citaSeleccionada == null) {
            AlertUtils.mostrarError("el seleccionado no existe");
            return;
        }
        try {
            citaDAO.conectar();

            citaDAO.eliminarCita(citaSeleccionada);
            AlertUtils.mostrarInformacion("Cita eliminada");


            verCita();
            limpiarCajas();
            citaSeleccionada = null;
        } catch (Exception e) {
            AlertUtils.mostrarError("Error：" + e.getMessage());
        } finally {
            try {
                citaDAO.desconectar();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



    private void limpiarCajas() {
        dpFechaCita.setValue(null);
        cbEspecialidad.setValue(null);
    }

}
 
