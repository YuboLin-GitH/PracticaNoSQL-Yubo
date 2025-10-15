package org.example.practicanosqlyubo;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.practicanosqlyubo.domain.Especialidad;
import org.example.practicanosqlyubo.util.ConnectionDB;

import java.io.FileReader;
import java.io.Reader;


public class CargarEspecialidades {
    public static void main(String[] args) throws Exception {
        MongoClient con = ConnectionDB.conectar();
        MongoDatabase db = con.getDatabase("centro_medico");
        MongoCollection<Document> col = db.getCollection("especialidades");

        // Leer JSON con Gson
        Gson gson = new Gson();
        Reader reader = new FileReader("src/main/resources/BaseDatos/especialidades.json");
        Especialidad[] especialidades = gson.fromJson(reader, Especialidad[].class);

        // Insertar cada una en Mongo
        for (Especialidad e : especialidades) {
            Document doc = new Document("nombre", e.getNombre());
            col.insertOne(doc);
        }

        System.out.println("Especialidades cargadas correctamente.");
        con.close();
    }
}