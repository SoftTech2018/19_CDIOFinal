/* must be dropped in this order to avoid constraint violations */
DROP TABLE IF EXISTS produktbatchkomponent;
DROP TABLE IF EXISTS produktbatch;
DROP TABLE IF EXISTS roller;
DROP TABLE IF EXISTS operatoer;
DROP TABLE IF EXISTS receptkomponent;
DROP TABLE IF EXISTS recept;
DROP TABLE IF EXISTS raavarebatch;
DROP TABLE IF EXISTS raavare;

CREATE TABLE operatoer(opr_id INT NOT NULL AUTO_INCREMENT, opr_navn TEXT, ini TEXT, cpr TEXT, password TEXT, PRIMARY KEY(opr_id)) ENGINE=innoDB;
 
CREATE TABLE roller(opr_id INT PRIMARY KEY, admin BOOLEAN, farmaceut BOOLEAN, varkforer BOOLEAN, operatoer BOOLEAN,
FOREIGN KEY (opr_id) REFERENCES operatoer(opr_id)) ENGINE=innoDB;

CREATE TABLE raavare(raavare_id INT PRIMARY KEY, raavare_navn TEXT, leverandoer TEXT) ENGINE=innoDB;

CREATE TABLE raavarebatch(rb_id INT PRIMARY KEY, raavare_id INT, maengde REAL, CONSTRAINT raavarebatch
   FOREIGN KEY (raavare_id) REFERENCES raavare(raavare_id) ON DELETE CASCADE) ENGINE=innoDB;

CREATE TABLE recept(recept_id INT PRIMARY KEY, recept_navn TEXT) ENGINE=innoDB;

CREATE TABLE receptkomponent(recept_id INT, raavare_id INT, nom_netto REAL, tolerance REAL, 
   PRIMARY KEY (recept_id, raavare_id), 
   CONSTRAINT receptkomponent FOREIGN KEY (recept_id) REFERENCES recept(recept_id) ON DELETE CASCADE, 
   FOREIGN KEY (raavare_id) REFERENCES raavare(raavare_id)) ENGINE=innoDB;

CREATE TABLE produktbatch(pb_id INT NOT NULL AUTO_INCREMENT, status INT, recept_id INT, dato TEXT, begyndt TEXT, afsluttet TEXT,
   PRIMARY KEY(pb_id),
   FOREIGN KEY (recept_id) REFERENCES recept(recept_id)) ENGINE=innoDB;

CREATE TABLE produktbatchkomponent(pb_id INT, rb_id INT, tara REAL, netto REAL, opr_id INT, terminal TEXT,
   PRIMARY KEY (pb_id, rb_id), 
   FOREIGN KEY (pb_id) REFERENCES produktbatch(pb_id), 
   FOREIGN KEY (rb_id) REFERENCES raavarebatch(rb_id), 
   FOREIGN KEY (opr_id) REFERENCES operatoer(opr_id)) ENGINE=innoDB;


INSERT INTO operatoer(opr_navn, ini, cpr, password) VALUES
('Mads', 'MA', '070770-7007', '02324it!'),
('Stig', 'ST', '080880-8008', '02324it!'),
('Daniel', 'DA', '090990-9009', '02324it!'),
('Ronnie', 'RN', '123456-1234', '02324it!');

INSERT INTO roller(opr_id, admin, farmaceut, varkforer, operatoer) VALUES
(1, true, false, false, false),
(2, false, true, false, false),
(3, false, false, true, false),
(4, false, false, false, true);

INSERT INTO raavare(raavare_id, raavare_navn, leverandoer) VALUES
(1, 'dej', 'Wawelka'),
(2, 'tomat', 'Knoor'),
(3, 'tomat', 'Veaubais'),
(4, 'tomat', 'Franz'),
(5, 'ost', 'Ost og Skinke A/S'),
(6, 'skinke', 'Ost og Skinke A/S'),
(7, 'champignon', 'Igloo Frostvarer');

INSERT INTO raavarebatch(rb_id, raavare_id, maengde) VALUES
(1, 1, 1000),
(2, 2, 300),
(3, 3, 300),
(4, 4, 100),
(5, 5, 100), 
(6, 6, 100),
(7, 7, 0);

INSERT INTO recept(recept_id, recept_navn) VALUES
(1, 'margherita'),
(2, 'prosciutto'),
(3, 'capricciosa');


INSERT INTO receptkomponent(recept_id, raavare_id, nom_netto, tolerance) VALUES
(1, 1, 0.5, 0.1),
(1, 2, 0.5, 0.1),
(1, 5, 0.5, 0.1),

(2, 1, 0.5, 0.1),
(2, 3, 0.5, 0.1),  
(2, 5, 0.5, 0.1),
(2, 6, 0.5, 0.1),

(3, 1, 0.5, 0.1),
(3, 4, 0.5, 0.1),
(3, 5, 0.5, 0.1),
(3, 6, 0.5, 0.1),
(3, 7, 0.5, 0.1);

