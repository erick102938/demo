package com.example.demo;

import java.io.Serializable;
import java.util.ArrayList;
    import java.util.Comparator;
    import java.util.List;
    import java.util.Objects;

    public class Estudiante implements Serializable {
        private String Nombre;
        private String Apellido;
        private int Codigo;
        private List <String> CursosTomados;
        public Estudiante() {
        }
        public Estudiante(String nombre, String apellido, int codigo, List<String> cursosTomados) {
            Nombre = nombre;
            Apellido = apellido;
            Codigo = codigo;
            CursosTomados = cursosTomados;
        }

        public String getNombre() {
            return Nombre;
        }

        public void setNombre(String nombre) {
            Nombre = nombre;
        }

        public String getApellido() {
            return Apellido;
        }

        public void setApellido(String apellido) {
            Apellido = apellido;
        }

        public int getCodigo() {
            return Codigo;
        }

        public void setCodigo(int codigo) {
            Codigo = codigo;
        }

        public List<String> getCursosTomados() {
            return CursosTomados;
        }

        public void setCursosTomados(List<String> cursosTomados) {
            CursosTomados = cursosTomados;
        }

        @Override
        public String toString() {
            return "Estudiante{" +
                    "nombre='" + Nombre + '\'' +
                    ", apellido='" + Apellido + '\'' +
                    ", codigo=" + Codigo +
                    ", cursosTomados=" + CursosTomados +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Estudiante that = (Estudiante) o;
            return Codigo == that.Codigo;
        }

        @Override
        public int hashCode() {
            return Objects.hash(Codigo);
        }

        public int compareTo(Estudiante e) {
            return Integer.compare(Codigo, e.getCodigo());
        }

        public static Comparator<Estudiante> ApellidoComparator = new Comparator<Estudiante>() {
            @Override
            public int compare(Estudiante e1, Estudiante e2) {
                return e1.getApellido().compareTo(e2.getApellido());
            }
        };
        public void mostrarDatosEstudiante(Estudiante estudiante){
            System.out.println("Los datos del estudiante: "+estudiante.getCodigo());
            System.out.println("\tNomber: "+estudiante.getNombre());
            System.out.println("\tApellido: "+estudiante.getApellido());
            System.out.println("\tMaterias: "+ estudiante.getCursosTomados());
        }
    }
