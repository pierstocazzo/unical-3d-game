# MySQL-Front 5.1  (Build 1.48)

/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE */;
/*!40101 SET SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES */;
/*!40103 SET SQL_NOTES='ON' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS */;
/*!40014 SET FOREIGN_KEY_CHECKS=0 */;


# Host: localhost    Database: dbimpresaelettrica
# ------------------------------------------------------
# Server version 5.1.34-community

#
# Source for table cliente
#

DROP TABLE IF EXISTS `cliente`;
CREATE TABLE `cliente` (
  `id` varchar(10) NOT NULL,
  `nome` varchar(25) DEFAULT NULL,
  `cognome` varchar(25) DEFAULT NULL,
  `data_nascita` date DEFAULT NULL,
  `codice_fiscale` varchar(16) DEFAULT NULL,
  `partita_iva` varchar(11) DEFAULT NULL,
  `ragione_sociale` varchar(50) DEFAULT NULL,
  `tipo` varchar(10) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `città` varchar(25) DEFAULT NULL,
  `indirizzo` varchar(100) DEFAULT NULL,
  `account` varchar(25) DEFAULT NULL,
  `promozione` varchar(10) DEFAULT NULL,
  `password` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `FK_cliente_1` (`account`),
  KEY `FK_cliente_2` (`promozione`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Dumping data for table cliente
#
LOCK TABLES `cliente` WRITE;
/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;

INSERT INTO `cliente` VALUES ('cli1','antonello','violini','1986-10-12',NULL,NULL,NULL,'privato',NULL,NULL,NULL,NULL,'antonelloviolini',NULL,'kfifjua33');
INSERT INTO `cliente` VALUES ('cli2','michele','gioia','1985-02-02',NULL,NULL,NULL,'privato',NULL,NULL,NULL,NULL,'michele gioia',NULL,'dsa8asdj');
INSERT INTO `cliente` VALUES ('cli3','youGo s.r.l.',NULL,NULL,NULL,NULL,NULL,'azienda',NULL,NULL,NULL,NULL,'yougo',NULL,'7sd7sy7d6as');
/*!40000 ALTER TABLE `cliente` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table contratto
#

DROP TABLE IF EXISTS `contratto`;
CREATE TABLE `contratto` (
  `codice` varchar(10) NOT NULL,
  `prezzo` int(11) DEFAULT NULL,
  `data_stipula` date DEFAULT NULL,
  `data_validità` date DEFAULT NULL,
  `cliente` varchar(10) DEFAULT NULL,
  `manager` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`codice`),
  KEY `FK_contratto_1` (`cliente`),
  KEY `FK_contratto_2` (`manager`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Dumping data for table contratto
#
LOCK TABLES `contratto` WRITE;
/*!40000 ALTER TABLE `contratto` DISABLE KEYS */;

INSERT INTO `contratto` VALUES ('cntr1',12000,'2009-12-01','2010-01-31','cli1','mng1');
INSERT INTO `contratto` VALUES ('cntr2',15000,'2009-12-02','2011-12-02','cli2','mng1');
INSERT INTO `contratto` VALUES ('cntr3',6500,'2008-10-04','2010-10-04','cli3','mng2');
/*!40000 ALTER TABLE `contratto` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table coordinatore
#

DROP TABLE IF EXISTS `coordinatore`;
CREATE TABLE `coordinatore` (
  `id` varchar(10) NOT NULL,
  `nome` varchar(25) DEFAULT NULL,
  `cognome` varchar(25) DEFAULT NULL,
  `codice_fiscale` varchar(16) DEFAULT NULL,
  `data_assunzione` date DEFAULT NULL,
  `account` varchar(25) DEFAULT NULL,
  `password` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `FK_coordinatore_1` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Dumping data for table coordinatore
#
LOCK TABLES `coordinatore` WRITE;
/*!40000 ALTER TABLE `coordinatore` DISABLE KEYS */;

INSERT INTO `coordinatore` VALUES ('coo1','antonio','parisi',NULL,NULL,'antonioparisi','7sdukkk');
INSERT INTO `coordinatore` VALUES ('coo2','luigi','betola',NULL,NULL,'luigibetola','iuiuiuidjjj');
/*!40000 ALTER TABLE `coordinatore` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table filiale
#

DROP TABLE IF EXISTS `filiale`;
CREATE TABLE `filiale` (
  `codice` varchar(10) NOT NULL,
  `città` varchar(25) DEFAULT NULL,
  `inidrizzo` varchar(100) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `coordinatore` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`codice`),
  KEY `FK_filiale_1` (`coordinatore`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Dumping data for table filiale
#
LOCK TABLES `filiale` WRITE;
/*!40000 ALTER TABLE `filiale` DISABLE KEYS */;

INSERT INTO `filiale` VALUES ('fil1','cosenza','via roma','0976338212','cz@impresaelettica.it','coo2');
INSERT INTO `filiale` VALUES ('fil2','catanzaro','corso mazzini','098475663','cs@impresaelettrica.it','coo1');
/*!40000 ALTER TABLE `filiale` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table impianto
#

DROP TABLE IF EXISTS `impianto`;
CREATE TABLE `impianto` (
  `codice` varchar(10) NOT NULL,
  `indirizzo` varchar(100) DEFAULT NULL,
  `città` varchar(25) DEFAULT NULL,
  `tipo` varchar(10) DEFAULT NULL,
  `cliente` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`codice`),
  KEY `FK_impianto_1` (`cliente`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Dumping data for table impianto
#
LOCK TABLES `impianto` WRITE;
/*!40000 ALTER TABLE `impianto` DISABLE KEYS */;

INSERT INTO `impianto` VALUES ('imp1','via roma 25','cosenza','industiale','cli3');
INSERT INTO `impianto` VALUES ('imp2','corso mazzini','catanzaro','domestico','cli2');
INSERT INTO `impianto` VALUES ('imp3','corso italia','cosenza','domestico','cli1');
/*!40000 ALTER TABLE `impianto` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table intervento
#

DROP TABLE IF EXISTS `intervento`;
CREATE TABLE `intervento` (
  `codice` varchar(10) NOT NULL,
  `data_inizio` date DEFAULT NULL,
  `data_fine` date DEFAULT NULL,
  `cliente` varchar(10) DEFAULT NULL,
  `impianto` varchar(10) DEFAULT NULL,
  `contratto` varchar(10) DEFAULT NULL,
  `squadra` varchar(10) DEFAULT NULL,
  `tipologia_intervento` varchar(10) DEFAULT NULL,
  `stato` varchar(50) DEFAULT NULL,
  `descrizione` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`codice`),
  KEY `FK_intervento_1` (`tipologia_intervento`),
  KEY `FK_intervento_2` (`squadra`),
  KEY `FK_intervento_3` (`contratto`),
  KEY `FK_intervento_4` (`cliente`),
  KEY `FK_intervento_5` (`impianto`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Dumping data for table intervento
#
LOCK TABLES `intervento` WRITE;
/*!40000 ALTER TABLE `intervento` DISABLE KEYS */;

INSERT INTO `intervento` VALUES ('int1','2009-01-01',NULL,'cli1','imp1','cntr1','sq1','tp4','da completare','');
INSERT INTO `intervento` VALUES ('int2','2009-02-02',NULL,'cli2','imp2','cntr2','sq2','tp3','avviato',NULL);
INSERT INTO `intervento` VALUES ('int3','2010-01-02',NULL,'cli3','imp3','cntr3','sq4','tp1','da effettuare',NULL);
/*!40000 ALTER TABLE `intervento` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table manager
#

DROP TABLE IF EXISTS `manager`;
CREATE TABLE `manager` (
  `id` varchar(10) NOT NULL,
  `nome` varchar(25) DEFAULT NULL,
  `cognome` varchar(25) DEFAULT NULL,
  `codice_fiscale` varchar(16) DEFAULT NULL,
  `data_assunzione` date DEFAULT NULL,
  `account` varchar(25) DEFAULT NULL,
  `password` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `FK_manager_1` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Dumping data for table manager
#
LOCK TABLES `manager` WRITE;
/*!40000 ALTER TABLE `manager` DISABLE KEYS */;

INSERT INTO `manager` VALUES ('mng1','fabrizio','colaninno',NULL,NULL,'fabry','juventus976');
INSERT INTO `manager` VALUES ('mng2','francesco','de vittore',NULL,NULL,'frank','elisa19101965');
/*!40000 ALTER TABLE `manager` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table promozione
#

DROP TABLE IF EXISTS `promozione`;
CREATE TABLE `promozione` (
  `codice` varchar(10) NOT NULL,
  `descrizione` text,
  `tipo_cliente` varchar(10) DEFAULT NULL,
  `data_inizio` date DEFAULT NULL,
  `data_fine` date DEFAULT NULL,
  `manager` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`codice`),
  KEY `FK_promozione_1` (`manager`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Dumping data for table promozione
#
LOCK TABLES `promozione` WRITE;
/*!40000 ALTER TABLE `promozione` DISABLE KEYS */;

INSERT INTO `promozione` VALUES ('',NULL,NULL,NULL,NULL,NULL);
INSERT INTO `promozione` VALUES ('pr1','sconto 20 % su tutti gli interventi per l\'anno 2010','azienda','2010-01-01','2010-12-01','mng1');
/*!40000 ALTER TABLE `promozione` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table squadra
#

DROP TABLE IF EXISTS `squadra`;
CREATE TABLE `squadra` (
  `codice` varchar(10) NOT NULL,
  `nome` varchar(50) DEFAULT NULL,
  `filiale` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`codice`),
  KEY `FK_squadra_1` (`filiale`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Dumping data for table squadra
#
LOCK TABLES `squadra` WRITE;
/*!40000 ALTER TABLE `squadra` DISABLE KEYS */;

INSERT INTO `squadra` VALUES ('sq1','elettricisti cs','fil1');
INSERT INTO `squadra` VALUES ('sq2','elettricisti cz','fil2');
INSERT INTO `squadra` VALUES ('sq3','ingegneri cs','fil1');
INSERT INTO `squadra` VALUES ('sq4','ingegneri cz','fil2');
/*!40000 ALTER TABLE `squadra` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table tecnico
#

DROP TABLE IF EXISTS `tecnico`;
CREATE TABLE `tecnico` (
  `id` varchar(10) NOT NULL,
  `nome` varchar(25) NOT NULL,
  `cognome` varchar(25) NOT NULL DEFAULT '',
  `codice_fiscale` varchar(16) NOT NULL DEFAULT '',
  `data_assunzione` date NOT NULL,
  `account` varchar(25) NOT NULL,
  `squadra` varchar(10) NOT NULL,
  `password` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `FK_tecnico_1` (`account`),
  KEY `FK_tecnico_2` (`squadra`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Dumping data for table tecnico
#
LOCK TABLES `tecnico` WRITE;
/*!40000 ALTER TABLE `tecnico` DISABLE KEYS */;

INSERT INTO `tecnico` VALUES ('tc1','andrea','lipari','','2008-10-09','andrealipari','sq1','andrea198761029');
INSERT INTO `tecnico` VALUES ('tc2','giuseppe','ciullo','','2007-05-22','ciullogiu','sq1','giullo');
INSERT INTO `tecnico` VALUES ('tc3','marco','vespa','','2006-03-24','vespama','sq2','vespa86');
INSERT INTO `tecnico` VALUES ('tc4','vittorio','de francesco','','2005-07-21','vittoriode','sq2','amanda56');
INSERT INTO `tecnico` VALUES ('tc5','massimo','costa','','2009-01-01','massimocosata','sq3','maxcostamax');
INSERT INTO `tecnico` VALUES ('tc6','franco','malito','','2007-08-11','malitof','sq3','malitofrancomalito');
INSERT INTO `tecnico` VALUES ('tc7','giulio','maio','','2008-12-12','giulioo','sq4','maiomaio');
INSERT INTO `tecnico` VALUES ('tc8','silvio','de luca','','2005-07-18','silvio','sq4','leopardidante');
/*!40000 ALTER TABLE `tecnico` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table tipologiaintervento
#

DROP TABLE IF EXISTS `tipologiaintervento`;
CREATE TABLE `tipologiaintervento` (
  `codice` varchar(10) NOT NULL,
  `nome` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`codice`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Dumping data for table tipologiaintervento
#
LOCK TABLES `tipologiaintervento` WRITE;
/*!40000 ALTER TABLE `tipologiaintervento` DISABLE KEYS */;

INSERT INTO `tipologiaintervento` VALUES ('tp1','manutenzione software');
INSERT INTO `tipologiaintervento` VALUES ('tp2','manutenzione hardware');
INSERT INTO `tipologiaintervento` VALUES ('tp3','manutenz imp elettrico');
INSERT INTO `tipologiaintervento` VALUES ('tp4','installazione software');
INSERT INTO `tipologiaintervento` VALUES ('tp5','installaz impianto elettr');
/*!40000 ALTER TABLE `tipologiaintervento` ENABLE KEYS */;
UNLOCK TABLES;

#
#  Foreign keys for table cliente
#

ALTER TABLE `cliente`
ADD CONSTRAINT `FK_cliente_2` FOREIGN KEY (`promozione`) REFERENCES `promozione` (`codice`) ON DELETE NO ACTION ON UPDATE NO ACTION;

#
#  Foreign keys for table contratto
#

ALTER TABLE `contratto`
ADD CONSTRAINT `contratto_ibfk_1` FOREIGN KEY (`manager`) REFERENCES `manager` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
ADD CONSTRAINT `FK_contratto_1` FOREIGN KEY (`cliente`) REFERENCES `cliente` (`id`);

#
#  Foreign keys for table filiale
#

ALTER TABLE `filiale`
ADD CONSTRAINT `FK_filiale_1` FOREIGN KEY (`coordinatore`) REFERENCES `coordinatore` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

#
#  Foreign keys for table impianto
#

ALTER TABLE `impianto`
ADD CONSTRAINT `FK_impianto_1` FOREIGN KEY (`cliente`) REFERENCES `cliente` (`id`);

#
#  Foreign keys for table intervento
#

ALTER TABLE `intervento`
ADD CONSTRAINT `FK_intervento_1` FOREIGN KEY (`tipologia_intervento`) REFERENCES `tipologiaintervento` (`codice`),
ADD CONSTRAINT `FK_intervento_2` FOREIGN KEY (`squadra`) REFERENCES `squadra` (`codice`),
ADD CONSTRAINT `FK_intervento_3` FOREIGN KEY (`contratto`) REFERENCES `contratto` (`codice`),
ADD CONSTRAINT `FK_intervento_4` FOREIGN KEY (`cliente`) REFERENCES `cliente` (`id`),
ADD CONSTRAINT `FK_intervento_5` FOREIGN KEY (`impianto`) REFERENCES `impianto` (`codice`);

#
#  Foreign keys for table promozione
#

ALTER TABLE `promozione`
ADD CONSTRAINT `FK_promozione_1` FOREIGN KEY (`manager`) REFERENCES `manager` (`id`);

#
#  Foreign keys for table squadra
#

ALTER TABLE `squadra`
ADD CONSTRAINT `FK_squadra_1` FOREIGN KEY (`filiale`) REFERENCES `filiale` (`codice`);

#
#  Foreign keys for table tecnico
#

ALTER TABLE `tecnico`
ADD CONSTRAINT `FK_tecnico_2` FOREIGN KEY (`squadra`) REFERENCES `squadra` (`codice`);


/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
