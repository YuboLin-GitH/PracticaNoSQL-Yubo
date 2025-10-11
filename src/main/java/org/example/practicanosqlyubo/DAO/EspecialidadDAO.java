package org.example.practicanosqlyubo.DAO;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.practicanosqlyubo.ConnectionDB;
import org.example.practicanosqlyubo.domain.Especialidad;

import java.util.ArrayList;
import java.util.List;

public class EspecialidadDAO {

    public static List<Especialidad> obtenerTodas() {
        List<Especialidad> especialidades = new ArrayList<>();
        MongoClient mongoClient = ConnectionDB.conectar();
        MongoDatabase db = mongoClient.getDatabase("centro_medico");
        MongoCollection<Document> col = db.getCollection("especialidades");

        for (Document doc : col.find()) {
            Especialidad e = new Especialidad();
            e.setNombre(doc.getString("nombre"));
            especialidades.add(e);
        }
        mongoClient.close();
        return especialidades;
    }
}