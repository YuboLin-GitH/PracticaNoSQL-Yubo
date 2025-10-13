package org.example.practicanosqlyubo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.practicanosqlyubo.util.HashUtil;
import java.util.ArrayList;
import java.util.List;

public class CrearPerfi {
    public static void main(String[] args) {
        MongoClient con = new MongoClient("localhost", 27017);
        MongoDatabase db = con.getDatabase("centro_medico");
        System.out.println("Conectada correctamente a la BD");

        // Insertar Pacientes
        MongoCollection<Document> colPaciente = db.getCollection("pacientes");
        Object[][] pacientesData = {
                {"12345678A","David","david","c/ AAA", 611222333},
                {"34564546B","Angel","angel","c/ BBB", 611512183},
                {"62145448C","Lucia","lucia","c/ CCC", 611224013},
                {"91321654D","Martina","martina","c/ DDD", 618434555},
                {"51248345E","Sofia","sofia","c/ EEE", 649161161},
                {"84345876F","Hugo","hugo","c/ FFF", 616713488},
                {"81431548G","Leo","leo","c/ GGG", 668453178},
                {"11501548H","Daniel","daniel","c/ HHH", 691246578}
        };
        List<Document> listaPac = new ArrayList<>();
        for (Object[] p : pacientesData) {
            listaPac.add(new Document()
                    .append("dni", p[0])
                    .append("nombre", p[1])
                    .append("password", HashUtil.sha256((String)p[2]))
                    .append("direccion", p[3])
                    .append("telefono", p[4])
            );
        }
        colPaciente.insertMany(listaPac);
        System.out.println("Pacientes creados correctamente.");

        // Insertar Citas
        MongoCollection<Document> colCita = db.getCollection("citas");
        Object[][] citasData = {
                {1, "2024-12-10", "Cirugía", "12345678A"},
                {2, "2024-12-12", "Dermatología", "34564546B"},
                {3, "2024-12-15", "Pediatría", "62145448C"},
                {4, "2024-12-08", "Oftalmología", "91321654D"},
                {5, "2024-12-11", "Cirugía", "51248345E"},
                {6, "2024-12-18", "Dermatología", "84345876F"}
        };
        List<Document> listaCita = new ArrayList<>();
        for (Object[] c : citasData) {
            listaCita.add(new Document()
                    .append("idCita", c[0])
                    .append("fecha", c[1])
                    .append("especialidad", c[2])
                    .append("dni", c[3])
            );
        }
        colCita.insertMany(listaCita);
        System.out.println("Citas creadas correctamente.");

        con.close();
    }
}