package org.example.practicanosqlyubo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class CrearCita {
    public static void main(String[] args) {
        MongoClient con = new MongoClient("localhost", 27017);
        MongoDatabase db = con.getDatabase("centro_medico");
        MongoCollection<Document> col = db.getCollection("citas");

        Document cita = new Document()
                .append("idCita", 1)
                .append("fecha", "2024-12-10")
                .append("especialidad", "Cirugía")
                .append("pacienteDni", "12345678B"); // 与David的DNI一致

        col.insertOne(cita);
        System.out.println("Cita creada correctamente.");
        con.close();
    }
}