package org.example.practicanosqlyubo.domain;

import java.util.Date;

/**
 * ClassName: Cita
 * Package: org.example.practica1medicoyubo.domain
 * Description:
 *
 * @Author Yubo
 * @Create 28/09/2025 16:19
 * @Version 1.0
 */
public class Cita  {
    private int idCita;
    private Date fechaCita;
    private int fkIdPaciente;
    private int fkIdEsp;
    private String nombreEsp;

    public Cita() {
    }

    public Cita(int idCita, Date fechaCita, int fkIdPaciente, int fkIdEsp, String nombreEsp) {
        this.idCita = idCita;
        this.fechaCita = fechaCita;
        this.fkIdPaciente = fkIdPaciente;
        this.fkIdEsp = fkIdEsp;
        this.nombreEsp = nombreEsp;
    }

    public int getIdCita() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }

    public Date getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(Date fechaCita) {
        this.fechaCita = fechaCita;
    }

    public int getFkIdPaciente() {
        return fkIdPaciente;
    }

    public void setFkIdPaciente(int fkIdPaciente) {
        this.fkIdPaciente = fkIdPaciente;
    }

    public int getFkIdEsp() {
        return fkIdEsp;
    }

    public void setFkIdEsp(int fkIdEsp) {
        this.fkIdEsp = fkIdEsp;
    }

    public String getNombreEsp() {
        return nombreEsp;
    }

    public void setNombreEsp(String nombreEsp) {
        this.nombreEsp = nombreEsp;
    }

    @Override
    public String toString() {
        return "Cita{" +
                "idCita=" + idCita +
                ", fechaCita=" + fechaCita +
                ", fkIdPaciente=" + fkIdPaciente +
                ", fkIdEsp=" + fkIdEsp +
                ", nombreEsp='" + nombreEsp + '\'' +
                '}';
    }
}
