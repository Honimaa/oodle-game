-- OODLE GAME DATABASE SCHEMA


-- Create database
CREATE DATABASE IF NOT EXISTS oodle_game
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE oodle_game;


-- TABLE: usuarios


CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_usuario_email UNIQUE (email),
    CONSTRAINT uq_usuario_username UNIQUE (username)
);


-- TABLE: partidas


CREATE TABLE IF NOT EXISTS partidas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resultado VARCHAR(20) NOT NULL,
    puntaje INT NOT NULL,
    intentos INT NOT NULL,
    duracion_segundos INT,

    CONSTRAINT fk_partida_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


-- INDEXES (optional but recommended)


CREATE INDEX idx_partidas_usuario
ON partidas(usuario_id);

