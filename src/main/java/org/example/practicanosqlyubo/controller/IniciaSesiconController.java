package org.example.practicanosqlyubo.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.practicanosqlyubo.DAO.UsuarioDAO;
import org.example.practicanosqlyubo.domain.Paciente;
import org.example.practicanosqlyubo.util.AlertUtils;
import org.example.practicanosqlyubo.util.HashUtil;
import org.example.practicanosqlyubo.util.R;


import java.io.IOException;

public class IniciaSesiconController {

    @FXML
    private TextField tfPaciente;

    @FXML
    private PasswordField pfPassword;

    @FXML
    private void validarUsuario(ActionEvent event) {
        String usuario = tfPaciente.getText().trim();
        String inputPw = pfPassword.getText().trim();
        String encryptedPw = HashUtil.sha256(inputPw);


        if (usuario.isEmpty() || encryptedPw.isEmpty()) {
            AlertUtils.mostrarError("Error, Por favor, rellene todos los campos.");
            return;
        }

        Paciente paciente = UsuarioDAO.buscarPorDato(usuario, encryptedPw);

        if (paciente != null) {
            try {
                // Cargar la ventana de gestión de citas
                AlertUtils.mostrarInformacion("Éxito, Inicio de sesión correcto ");
                FXMLLoader loader = new FXMLLoader(R.getUI("citas.fxml"));
                Scene scene = new Scene(loader.load());

                // Pasar datos del paciente al siguiente controlador
                CitaController citaController = loader.getController();
                citaController.cargarPaciente(
                        paciente.getDni(),
                        paciente.getNombre(),
                        paciente.getDireccion(),
                        String.valueOf(paciente.getTelefono())
                );

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Gestión de Citas");
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                AlertUtils.mostrarError("Error al conectar con la base de datos");
            }

        } else {
            AlertUtils.mostrarError("Error， Usuario o contraseña incorrectos ");
        }

    }



}