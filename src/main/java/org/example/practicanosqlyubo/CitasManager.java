package org.example.practicanosqlyubo;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.practicanosqlyubo.domain.Cita;

public class CitasManager {
    public static void main(String[] args) {
        MongoClient con = ConnectionDB.conectar();
        MongoDatabase db = con.getDatabase("centro_medico");
        MongoCollection<Document> colCitas = db.getCollection("citas");

        Cita cita = new Cita();
        cita.setDni("12345678A");            // 用setDni
        cita.setNombre("María López");       // 用setNombre
        cita.setFecha("2025-10-12");
        cita.setEspecialidad("Cardiología");

        Gson gson = new Gson();
        String json = gson.toJson(cita);
        Document doc = Document.parse(json);

        colCitas.insertOne(doc);
        System.out.println("Cita insertada correctamente.");

        con.close();
    }
}