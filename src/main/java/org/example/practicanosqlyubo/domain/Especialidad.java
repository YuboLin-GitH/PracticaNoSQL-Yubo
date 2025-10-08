package org.example.practicanosqlyubo.domain;

/**
 * ClassName: Especialidad
 * Package: org.example.practica1medicoyubo.domain
 * Description:
 *
 * @Author Yubo
 * @Create 28/09/2025 16:18
 * @Version 1.0
 */
public class Especialidad {
    private int idEsp;
    private String nombreEsp;


    public Especialidad() {
    }

    public Especialidad(int idEsp, String nombreEsp) {
        this.idEsp = idEsp;
        this.nombreEsp = nombreEsp;
    }

    public int getIdEsp() {
        return idEsp;
    }

    public void setIdEsp(int idEsp) {
        this.idEsp = idEsp;
    }

    public String getNombreEsp() {
        return nombreEsp;
    }

    public void setNombreEsp(String nombreEsp) {
        this.nombreEsp = nombreEsp;
    }

    @Override
    public String toString() {
        return nombreEsp;
    }
}
