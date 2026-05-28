-- ============================================================
--  AMADEUS TRAINING SIMULATOR — MySQL Schema
--  Auto-runs on startup via Spring Boot sql.init
--  Also open directly in MySQL Workbench 8.0
-- ============================================================

CREATE TABLE IF NOT EXISTS airlines (
  code CHAR(2)      PRIMARY KEY,
  name VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS cities (
  code    CHAR(3)      PRIMARY KEY,
  name    VARCHAR(100) NOT NULL,
  country VARCHAR(60)  NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS aircraft_types (
  code CHAR(3)     PRIMARY KEY,
  name VARCHAR(60) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sr_codes (
  code        CHAR(4)      PRIMARY KEY,
  description VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS flights (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  route_from  CHAR(3)     NOT NULL,
  route_to    CHAR(3)     NOT NULL,
  airline     CHAR(2)     NOT NULL,
  flight_num  VARCHAR(6)  NOT NULL,
  dep_time    CHAR(4)     NOT NULL,
  arr_time    CHAR(4)     NOT NULL,
  duration    VARCHAR(10),
  aircraft    CHAR(3)     NOT NULL,
  stops       TINYINT     DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS flight_classes (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  flight_id   INT    NOT NULL,
  class_code  CHAR(1) NOT NULL,
  seats_avail TINYINT NOT NULL DEFAULT 9,
  FOREIGN KEY (flight_id) REFERENCES flights(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS fares (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  route_from CHAR(3)        NOT NULL,
  route_to   CHAR(3)        NOT NULL,
  airline    CHAR(2)        NOT NULL,
  fare_basis VARCHAR(12)    NOT NULL,
  fare_type  CHAR(2)        NOT NULL,
  cabin      CHAR(1)        NOT NULL,
  currency   CHAR(3)        NOT NULL DEFAULT 'EGP',
  base_fare  DECIMAL(10,2)  NOT NULL,
  tax        DECIMAL(10,2)  NOT NULL DEFAULT 0,
  note       VARCHAR(100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS pnrs (
  locator  CHAR(6)      PRIMARY KEY,
  status   VARCHAR(20)  NOT NULL DEFAULT 'OPEN',
  created  VARCHAR(20),
  office   VARCHAR(20),
  sine     VARCHAR(6),
  ttl      VARCHAR(40),
  rf       VARCHAR(60),
  raw_json TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS training_log (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  session_id VARCHAR(20)  NOT NULL,
  command    VARCHAR(255) NOT NULL,
  ts         TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
