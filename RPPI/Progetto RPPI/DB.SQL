-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.1.34-community


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema dbimpresaelettrica
--

CREATE DATABASE IF NOT EXISTS dbimpresaelettrica;
USE dbimpresaelettrica;

--
-- Definition of table `account`
--

DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `username` varchar(10) NOT NULL,
  `password` varchar(32) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `account`
--

/*!40000 ALTER TABLE `account` DISABLE KEYS */;
/*!40000 ALTER TABLE `account` ENABLE KEYS */;


--
-- Definition of table `cliente`
--

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
  PRIMARY KEY (`id`),
  KEY `FK_cliente_1` (`account`),
  KEY `FK_cliente_2` (`promozione`),
  CONSTRAINT `FK_cliente_2` FOREIGN KEY (`promozione`) REFERENCES `promozione` (`codice`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_cliente_1` FOREIGN KEY (`account`) REFERENCES `account` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cliente`
--

/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;
/*!40000 ALTER TABLE `cliente` ENABLE KEYS */;


--
-- Definition of table `contratto`
--

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
  KEY `FK_contratto_2` (`manager`),
  CONSTRAINT `FK_contratto_1` FOREIGN KEY (`cliente`) REFERENCES `cliente` (`id`),
  CONSTRAINT `FK_contratto_2` FOREIGN KEY (`manager`) REFERENCES `manager` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `contratto`
--

/*!40000 ALTER TABLE `contratto` DISABLE KEYS */;
/*!40000 ALTER TABLE `contratto` ENABLE KEYS */;


--
-- Definition of table `coordinatore`
--

DROP TABLE IF EXISTS `coordinatore`;
CREATE TABLE `coordinatore` (
  `id` varchar(10) NOT NULL,
  `nome` varchar(25) DEFAULT NULL,
  `cognome` varchar(25) DEFAULT NULL,
  `codice_fiscale` varchar(16) DEFAULT NULL,
  `data_assunzione` date DEFAULT NULL,
  `account` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_coordinatore_1` (`account`),
  CONSTRAINT `FK_coordinatore_1` FOREIGN KEY (`account`) REFERENCES `account` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `coordinatore`
--

/*!40000 ALTER TABLE `coordinatore` DISABLE KEYS */;
/*!40000 ALTER TABLE `coordinatore` ENABLE KEYS */;


--
-- Definition of table `filiale`
--

DROP TABLE IF EXISTS `filiale`;
CREATE TABLE `filiale` (
  `codice` varchar(10) NOT NULL,
  `città` varchar(25) DEFAULT NULL,
  `inidrizzo` varchar(100) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `coordinatore` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`codice`),
  KEY `FK_filiale_1` (`coordinatore`),
  CONSTRAINT `FK_filiale_1` FOREIGN KEY (`coordinatore`) REFERENCES `coordinatore` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `filiale`
--

/*!40000 ALTER TABLE `filiale` DISABLE KEYS */;
/*!40000 ALTER TABLE `filiale` ENABLE KEYS */;


--
-- Definition of table `impianto`
--

DROP TABLE IF EXISTS `impianto`;
CREATE TABLE `impianto` (
  `codice` varchar(10) NOT NULL,
  `indirizzo` varchar(100) DEFAULT NULL,
  `città` varchar(25) DEFAULT NULL,
  `tipo` varchar(10) DEFAULT NULL,
  `cliente` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`codice`),
  KEY `FK_impianto_1` (`cliente`),
  CONSTRAINT `FK_impianto_1` FOREIGN KEY (`cliente`) REFERENCES `cliente` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `impianto`
--

/*!40000 ALTER TABLE `impianto` DISABLE KEYS */;
/*!40000 ALTER TABLE `impianto` ENABLE KEYS */;


--
-- Definition of table `intervento`
--

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
  KEY `FK_intervento_5` (`impianto`),
  CONSTRAINT `FK_intervento_1` FOREIGN KEY (`tipologia_intervento`) REFERENCES `tipologiaintervento` (`codice`),
  CONSTRAINT `FK_intervento_2` FOREIGN KEY (`squadra`) REFERENCES `squadra` (`codice`),
  CONSTRAINT `FK_intervento_3` FOREIGN KEY (`contratto`) REFERENCES `contratto` (`codice`),
  CONSTRAINT `FK_intervento_4` FOREIGN KEY (`cliente`) REFERENCES `cliente` (`id`),
  CONSTRAINT `FK_intervento_5` FOREIGN KEY (`impianto`) REFERENCES `impianto` (`codice`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `intervento`
--

/*!40000 ALTER TABLE `intervento` DISABLE KEYS */;
/*!40000 ALTER TABLE `intervento` ENABLE KEYS */;


--
-- Definition of table `manager`
--

DROP TABLE IF EXISTS `manager`;
CREATE TABLE `manager` (
  `id` varchar(10) NOT NULL,
  `nome` varchar(25) DEFAULT NULL,
  `cognome` varchar(25) DEFAULT NULL,
  `codice_fiscale` varchar(16) DEFAULT NULL,
  `data_assunzione` date DEFAULT NULL,
  `account` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_manager_1` (`account`),
  CONSTRAINT `FK_manager_1` FOREIGN KEY (`account`) REFERENCES `account` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `manager`
--

/*!40000 ALTER TABLE `manager` DISABLE KEYS */;
/*!40000 ALTER TABLE `manager` ENABLE KEYS */;


--
-- Definition of table `promozione`
--

DROP TABLE IF EXISTS `promozione`;
CREATE TABLE `promozione` (
  `codice` varchar(10) NOT NULL,
  `descrizione` text,
  `tipo_cliente` varchar(10) DEFAULT NULL,
  `data_inizio` date DEFAULT NULL,
  `data_fine` date DEFAULT NULL,
  `manager` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`codice`),
  KEY `FK_promozione_1` (`manager`),
  CONSTRAINT `FK_promozione_1` FOREIGN KEY (`manager`) REFERENCES `manager` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `promozione`
--

/*!40000 ALTER TABLE `promozione` DISABLE KEYS */;
/*!40000 ALTER TABLE `promozione` ENABLE KEYS */;


--
-- Definition of table `squadra`
--

DROP TABLE IF EXISTS `squadra`;
CREATE TABLE `squadra` (
  `codice` varchar(10) NOT NULL,
  `nome` varchar(50) DEFAULT NULL,
  `filiale` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`codice`),
  KEY `FK_squadra_1` (`filiale`),
  CONSTRAINT `FK_squadra_1` FOREIGN KEY (`filiale`) REFERENCES `filiale` (`codice`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `squadra`
--

/*!40000 ALTER TABLE `squadra` DISABLE KEYS */;
/*!40000 ALTER TABLE `squadra` ENABLE KEYS */;


--
-- Definition of table `tecnico`
--

DROP TABLE IF EXISTS `tecnico`;
CREATE TABLE `tecnico` (
  `id` varchar(10) NOT NULL,
  `nome` varchar(25) NOT NULL,
  `cognome` varchar(25) NOT NULL,
  `codice_fiscale` varchar(16) NOT NULL,
  `data_assunzione` date NOT NULL,
  `account` varchar(25) NOT NULL,
  `squadra` varchar(10) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_tecnico_1` (`account`),
  KEY `FK_tecnico_2` (`squadra`),
  CONSTRAINT `FK_tecnico_1` FOREIGN KEY (`account`) REFERENCES `account` (`username`),
  CONSTRAINT `FK_tecnico_2` FOREIGN KEY (`squadra`) REFERENCES `squadra` (`codice`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tecnico`
--

/*!40000 ALTER TABLE `tecnico` DISABLE KEYS */;
/*!40000 ALTER TABLE `tecnico` ENABLE KEYS */;


--
-- Definition of table `tipologiaintervento`
--

DROP TABLE IF EXISTS `tipologiaintervento`;
CREATE TABLE `tipologiaintervento` (
  `codice` varchar(10) NOT NULL,
  `nome` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`codice`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tipologiaintervento`
--

/*!40000 ALTER TABLE `tipologiaintervento` DISABLE KEYS */;
/*!40000 ALTER TABLE `tipologiaintervento` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
