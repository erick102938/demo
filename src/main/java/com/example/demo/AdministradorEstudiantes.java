package com.example.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class AdministradorEstudiantes implements Serializable {

    private List<Estudiante> estudiantes;
    public AdministradorEstudiantes(String nuevoEstuidante){
        estudiantes = new ArrayList<>();
    }
    public AdministradorEstudiantes(){
        estudiantes = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File("C:\\Users\\erick\\IdeaProjects\\demo\\src\\main\\java\\com\\example\\demo\\DatosEstudiantes.txt"));
            while (scanner.hasNextLine()) {
                String datosEstudiante = scanner.nextLine();
                estudiantes.add(leerEstudiante(datosEstudiante));
            }
            scanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado: " + e.getMessage());
        }
    }
    public Estudiante leerEstudiante(String datosEstudiante){
        List<String> materias = new ArrayList<>();
        Estudiante nuevoEstudiante = new Estudiante();
        nuevoEstudiante.setNombre(datosEstudiante.split(":")[0]);
        nuevoEstudiante.setApellido(datosEstudiante.split(":")[1]);
        nuevoEstudiante.setCodigo(Integer.parseInt(datosEstudiante.split(":")[2]));
        for (String materia : datosEstudiante.split(":")[datosEstudiante.split(":").length-1].split(";")
        ) {
            materias.add(materia);
        }
        nuevoEstudiante.setCursosTomados(materias);
        return nuevoEstudiante;
    }
    public void crearNuevoEstudiante(Estudiante nuevoEstudiante){
        estudiantes.add(nuevoEstudiante);
    }
    public  int generarNuevoCodigo(){
        int nuevoCodigo = -1;
        for (Estudiante estudiante: estudiantes
        ) {
            if(nuevoCodigo <= estudiante.getCodigo()){
                nuevoCodigo = estudiante.getCodigo()+1;
            }
        }
        return nuevoCodigo;
    }
    public int buscarEstudiante(int codigoEstudiante) {
        for (Estudiante estudiante: estudiantes
        ) {
            if(codigoEstudiante == estudiante.getCodigo()){
                return estudiantes.indexOf(estudiante);
            }
        }
        return -1;
    }

    public void eliminarEstudiante(int codigoEstudiante) {
        if(buscarEstudiante(codigoEstudiante)>0){
            estudiantes.remove(buscarEstudiante(codigoEstudiante));
        }
    }
    public void editarEstudiante(Estudiante estudianteEditado) {
        for (Estudiante estudiante: estudiantes
        ) {
            if(estudiante.getCodigo() == estudianteEditado.getCodigo()){
                estudiante.setNombre(estudianteEditado.getNombre());
                estudiante.setApellido(estudianteEditado.getApellido());
                estudiante.setCursosTomados(estudianteEditado.getCursosTomados());
            }
        }
    }
    public List<Estudiante> getEstudiantes() {
        return estudiantes;
    }
    public  void  mostrarEstudiantes(){
        for (Estudiante estudiante : estudiantes
        ) {
            estudiante.mostrarDatosEstudiante(estudiante);
        }
    }

    public String getCadena() {
        String cadena = "";
        for (Estudiante estudiante : estudiantes
        ) {
            cadena += estudiante.getNombre()+":"+estudiante.getApellido()+":"+estudiante.getCodigo()+":";
            for (String curso : estudiante.getCursosTomados()
            ) {
                cadena += curso + ";";
            }
            int lastCharIndex = cadena.length() - 1;
            cadena = cadena.substring(0, lastCharIndex) + "\n";
        }
        return cadena;
    }
}
