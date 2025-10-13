package org.example.practicanosqlyubo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.practicanosqlyubo.util.HashUtil;


public class CrearUsuarioPaciente {
    public static void main(String[] args) {
        MongoClient con = new MongoClient("localhost", 27017);
        MongoDatabase db = con.getDatabase("centro_medico");
        MongoCollection<Document> col = db.getCollection("pacientes");


        String plainPassword = "david";
        String encrypted = HashUtil.sha256(plainPassword);

        Document paciente = new Document()
                .append("dni", "12345678B")
                .append("nombre", "David")
                .append("password", encrypted)
                .append("direccion", "C/ Nueva, 2")
                .append("telefono", 605048521);

        col.insertOne(paciente);
        System.out.println("Paciente creado correctamente.");
        con.close();
    }
}