package org.example.practicanosqlyubo.util;

import javafx.scene.control.Alert;

public class AlertUtils {

    public static void mostrarError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setContentText(mensaje);
        alerta.show();
    }

    public static void mostrarInformacion(String s) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setContentText(s);
        alerta.show();
    }
}
