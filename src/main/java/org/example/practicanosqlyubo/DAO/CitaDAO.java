package org.example.practicanosqlyubo.DAO;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.practicanosqlyubo.ConnectionDB;
import org.example.practicanosqlyubo.domain.Cita;

import java.util.ArrayList;
import java.util.List;

public class CitaDAO {

    public static void insertarCita(Cita cita) {
        MongoClient con = ConnectionDB.conectar();
        MongoDatabase db = con.getDatabase("centro_medico");
        MongoCollection<Document> col = db.getCollection("citas");
        Document doc = new Document()
                .append("dni", cita.getDni())
                .append("nombre", cita.getNombre())
                .append("direccion", cita.getDireccion())
                .append("telefono", cita.getTelefono())
                .append("fecha", cita.getFecha())
                .append("especialidad", cita.getEspecialidad());
        col.insertOne(doc);
        con.close();
    }

    public static List<Cita> obtenerCitasPorDNI(String dni) {
        List<Cita> lista = new ArrayList<>();
        MongoClient con = ConnectionDB.conectar();
        MongoDatabase db = con.getDatabase("centro_medico");
        MongoCollection<Document> col = db.getCollection("citas");
        for (Document d : col.find(new Document("dni", dni))) {
            Cita cita = new Cita();
            cita.setId(d.getObjectId("_id").toString());
            cita.setDni(d.getString("dni"));
            cita.setNombre(d.getString("nombre"));
            cita.setDireccion(d.getString("direccion"));
            cita.setTelefono(d.getString("telefono"));
            cita.setFecha(d.getString("fecha"));
            cita.setEspecialidad(d.getString("especialidad"));
            lista.add(cita);
        }
        con.close();
        return lista;
    }

    public static void modificarCita(Cita cita) {
        MongoClient con = ConnectionDB.conectar();
        MongoDatabase db = con.getDatabase("centro_medico");
        MongoCollection<Document> col = db.getCollection("citas");
        Document query = new Document("dni", cita.getDni()).append("fecha", cita.getFecha());
        Document update = new Document("$set", new Document("especialidad", cita.getEspecialidad())
                .append("nombre", cita.getNombre())
                .append("direccion", cita.getDireccion())
                .append("telefono", cita.getTelefono()));
        col.updateOne(query, update);
        con.close();
    }

    public static void borrarCita(String dni, String fecha) {
        MongoClient con = ConnectionDB.conectar();
        MongoDatabase db = con.getDatabase("centro_medico");
        MongoCollection<Document> col = db.getCollection("citas");
        Document query = new Document("dni", dni).append("fecha", fecha);
        col.deleteOne(query);
        con.close();
    }
}