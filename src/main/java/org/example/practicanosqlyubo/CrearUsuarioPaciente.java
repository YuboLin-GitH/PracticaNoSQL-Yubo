package org.example.practicanosqlyubo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class CrearUsuarioPaciente {
    public static void main(String[] args) {
        MongoClient con = new MongoClient("localhost", 27017);
        MongoDatabase db = con.getDatabase("centro_medico");
        MongoCollection<Document> col = db.getCollection("pacientes");

        Document paciente = new Document()
                .append("dni", "12345678B")
                .append("nombre", "David")
                .append("password", "david")   // 密码字段与登录验证一致
                .append("direccion", "C/ Nueva, 2")
                .append("telefono", 605048521);

        col.insertOne(paciente);
        System.out.println("Paciente creado correctamente.");
        con.close();
    }
}