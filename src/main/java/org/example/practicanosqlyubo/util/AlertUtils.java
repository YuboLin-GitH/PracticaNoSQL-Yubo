package org.example.practicanosqlyubo.util;

import javafx.scene.control.Alert;

public class AlertUtils {

    public static void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public static void mostrarError(String mensaje) {
        mostrarAlerta("Error", mensaje, Alert.AlertType.ERROR);
    }


    public static void mostrarInformacion(String mensaje) {
        mostrarAlerta("Información", mensaje, Alert.AlertType.INFORMATION);
    }



}
