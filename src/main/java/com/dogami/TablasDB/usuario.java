package com.dogami.TablasDB;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuario")
public class usuario {
    public usuario() {
    }

        @Id
        @Column(name = "id")
        private int id;
        @Column(name = "nombre")
        private String nombre;
        @Column(name = "correo")
        private String correo;
        @Column(name = "contraseña")
        private String contraseña;
        @Column(name = "tipo_de_licencia")
        private String tipo_de_licencia;
        
        public usuario(int id, String nombre, String correo, String contraseña, String tipo_de_licencia) {
            this.id = id;
            this.nombre = nombre;
            this.correo = correo;
            this.contraseña = contraseña;
            this.tipo_de_licencia = tipo_de_licencia;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
        public String getNombre() {
            return nombre;
        }
        public void setNombre(String nombre) {
            this.nombre = nombre;
        }
        public String getCorreo() {
            return correo;
        }
        public void setCorreo(String correo) {
            this.correo = correo;
        }
        public String getContraseña() {
            return contraseña;
        }
        public void setContraseña(String contraseña) {
            this.contraseña = contraseña;
        }

        public String getTipo_de_licencia() {
            return tipo_de_licencia;
        }
        public void setTipo_de_licencia(String tipo_de_licencia) {
            this.tipo_de_licencia = tipo_de_licencia;
        }
        @Override
        public String toString() {
            return "usuario{" +
                    "id=" + id +
                    ", nombre='" + nombre + '\'' +
                    ", correo='" + correo + '\'' +
                    ", contraseña='" + contraseña + '\'' +
                    ", tipo_de_licencia='" + tipo_de_licencia + '\'' + '}';
        }
}



