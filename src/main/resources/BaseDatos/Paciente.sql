DROP DATABASE IF EXISTS Usuario;
CREATE DATABASE Usuario;
Use Usuario;


CREATE TABLE Paciente(
idPaciente int unsigned auto_increment primary key, 
dni varchar(9),
nombre varchar(30),
password varchar(64),
direccion varchar(100),
telefono int(9) 
 );
 
  INSERT INTO Paciente  VALUES
 (1,"12345678A","David",SHA2("david",256),"c/ AAA", 611222333),
 (2,"34564546B","Angel",SHA2("angel",256),"c/ BBB", 611512183),
 (3,"62145448C","Lucia",SHA2("lucia",256),"c/ CCC", 611224013),
 (4,"91321654D","Martina",SHA2("martina",256),"c/ DDD", 618434555),
 (5,"51248345E","Sofia",SHA2("sofia",256),"c/ EEE", 649161161),
 (6,"84345876F","Hugo",SHA2("hugo",256), "c/ FFF", 616713488),
 (7,"81431548G","Leo",SHA2("leo",256),"c/ GGG", 668453178),
 (8,"11501548H","Daniel",SHA2("daniel",256),"c/ HHH", 691246578);
 

CREATE TABLE Especialidad(
 idEsp int unsigned auto_increment primary key,
 nombreEsp enum("Cirugía","Dermatología","Pediatría","Oftalmología")
);
INSERT INTO Especialidad (nombreEsp) VALUES
    ("Cirugía"),
    ("Dermatología"),
    ("Pediatría"),
    ("Oftalmología");



 CREATE TABLE Cita(
 idCita int unsigned auto_increment primary key,
 fechaCita date,
 fk_idEsp int unsigned,
 FOREIGN KEY (fk_idEsp) REFERENCES Especialidad(idEsp),
  fk_idPaciente int unsigned,
 FOREIGN KEY (fk_idPaciente) REFERENCES Paciente(idPaciente)
 );


INSERT INTO Cita (fechaCita, fk_idEsp, fk_idPaciente) VALUES
('2024-12-10', 1, 1),
('2024-12-12', 2, 2),
('2024-12-15', 3, 3),
('2024-12-08', 4, 4),
('2024-12-11', 1, 5),
('2024-12-18', 2, 6);
