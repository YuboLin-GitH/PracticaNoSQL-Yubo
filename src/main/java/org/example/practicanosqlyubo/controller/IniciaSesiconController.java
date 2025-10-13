package org.example.practicanosqlyubo.controller;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.bson.Document;
import org.example.practicanosqlyubo.ConnectionDB;
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
            mostrarAlerta("Por favor, complete todos los campos.");
            return;
        }

        // Conexión a MongoDB
        MongoClient con = ConnectionDB.conectar();
        MongoDatabase db = con.getDatabase("centro_medico");
        MongoCollection<Document> col = db.getCollection("pacientes");

        // Buscar usuario
        Document paciente = col.find(new Document("nombre", usuario)
                .append("password", encryptedPw)).first();
        if (paciente != null) {
            try {
                // Cargar la ventana de gestión de citas
                FXMLLoader loader = new FXMLLoader(R.getUI("citas.fxml"));
                Scene scene = new Scene(loader.load());

                // Pasar datos del paciente al siguiente controlador
                CitaController citaController = loader.getController();
                citaController.cargarPaciente(
                        paciente.getString("dni"),
                        paciente.getString("nombre"),
                        paciente.getString("direccion"),
                        paciente.get("telefono").toString()
                );

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Gestión de Citas - Centro Médico San Mateo");
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                mostrarAlerta("Error cargando la ventana de citas.");
            }

        } else {
            mostrarAlerta("Usuario o contraseña incorrectos.");
        }

        con.close();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Inicio de sesión");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


}
