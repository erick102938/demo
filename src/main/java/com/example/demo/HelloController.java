package com.example.demo;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class HelloController {
    AdministradorEstudiantes adEstudiantes = new AdministradorEstudiantes();
    @FXML private TableView<Estudiante> tablaEstudiantes;
    @FXML private TableColumn<Estudiante, String> columnaNombre;
    @FXML private TableColumn<Estudiante, String> columnaApellido;
    @FXML private TableColumn<Estudiante, Integer> columnaCodigo;
    @FXML private TableColumn<Estudiante, String> columnaMaterias;

    public void initialize() {

        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        columnaCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        ObservableList<Estudiante> estudiantes = FXCollections.observableArrayList(adEstudiantes.getEstudiantes());
        tablaEstudiantes.setItems(estudiantes);
        for (String curso : generarMaterias()) {
            TableColumn<Estudiante, String> columnaMateria = new TableColumn<>(curso);
            columnaMateria.setCellValueFactory(cellData -> {
                Estudiante estudiante = cellData.getValue();
                if (estudiante.getCursosTomados().contains(curso)) {
                    return new SimpleStringProperty("x");
                } else {
                    return new SimpleStringProperty("");
                }
            });
            tablaEstudiantes.getColumns().add(columnaMateria);
        }
    }
    public List<String> generarMaterias(){
        List<String> cursos = new ArrayList<>();

        for (Estudiante estudiante : adEstudiantes.getEstudiantes()) {
            for (String curso : estudiante.getCursosTomados()) {
                if (!cursos.contains(curso)) {
                    cursos.add(curso);
                }
            }
        }
        return cursos;
    }

    @FXML
    protected void registrarAction() {
        TextInputDialog dialogNombre = new TextInputDialog();
        dialogNombre.setHeaderText("Ingrese el nombre del estudiante");
        Optional<String> resultNombre = dialogNombre.showAndWait();
        if (!resultNombre.isPresent()) {
            return; // el usuario presionó "cancelar"
        }
        String nombre = resultNombre.get();

        TextInputDialog dialogApellido = new TextInputDialog();
        dialogApellido.setHeaderText("Ingrese el apellido del estudiante");
        Optional<String> resultApellido = dialogApellido.showAndWait();
        if (!resultApellido.isPresent()) {
            return; // el usuario presionó "cancelar"
        }
        String apellido = resultApellido.get();

        TextInputDialog dialogCursos = new TextInputDialog();
        dialogCursos.setHeaderText("Ingrese los cursos del estudiante separados por coma");
        Optional<String> resultCursos = dialogCursos.showAndWait();
        if (!resultCursos.isPresent()) {
            return; // el usuario presionó "cancelar"
        }
        List<String> cursos = Arrays.asList(resultCursos.get().split(","));

        adEstudiantes.crearNuevoEstudiante(new Estudiante(nombre,apellido,adEstudiantes.generarNuevoCodigo(),cursos));
        initialize();
    }
    @FXML
    protected void eliminarAction() {
        Estudiante estudianteSeleccionado = tablaEstudiantes.getSelectionModel().getSelectedItem();
        if (estudianteSeleccionado != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar acción");
            alert.setHeaderText("¿Estás seguro de que quieres eliminar a este estudiante?");
            alert.setContentText("Esta acción no se puede deshacer.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                tablaEstudiantes.getItems().remove(estudianteSeleccionado);
                adEstudiantes.eliminarEstudiante(estudianteSeleccionado.getCodigo());
            }
        }
    }
    @FXML
    protected void editarAction() {
        Estudiante estudianteSeleccionado = tablaEstudiantes.getSelectionModel().getSelectedItem();
        if (estudianteSeleccionado != null) {
            boolean confirmado = showEstudianteEditarDialog(estudianteSeleccionado);
            if (confirmado) {
                adEstudiantes.editarEstudiante(estudianteSeleccionado);
                adEstudiantes.mostrarEstudiantes();
                tablaEstudiantes.refresh();
            }
        } else {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Editar Estudiante");
            alerta.setHeaderText("No se ha seleccionado ningún estudiante");
            alerta.setContentText("Por favor, seleccione un estudiante de la tabla para editar.");
            alerta.showAndWait();
        }
    }
    private boolean showEstudianteEditarDialog(Estudiante estudiante) {
        // Crea un nuevo diálogo de edición de estudiante
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Editar estudiante");

        // Crea los componentes visuales del diálogo
        Label lblNombre = new Label("Nombre:");
        TextField txtNombre = new TextField(estudiante.getNombre());
        Label lblApellido = new Label("Apellido:");
        TextField txtApellido = new TextField(estudiante.getApellido());
        Label lblCodigo = new Label("Código:");
        TextField txtCodigo = new TextField(Integer.toString(estudiante.getCodigo()));
        txtCodigo.setEditable(false);
        Label lblMaterias = new Label("Materias:");
        TextField txtMaterias = new TextField(estudiante.getCursosTomados().toString().replace("[","").replace("]",""));


        // Crea un contenedor para los componentes visuales
        VBox vbox = new VBox(lblNombre, txtNombre, lblApellido, txtApellido, lblCodigo, txtCodigo,lblMaterias,txtMaterias);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));

        // Añade el contenedor al diálogo
        dialog.getDialogPane().setContent(vbox);

        // Crea los botones de acción del diálogo
        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        // Configura el comportamiento del botón "Guardar"
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnGuardar) {
                String nombre = txtNombre.getText();
                String apellido = txtApellido.getText();
                int codigo = Integer.parseInt(txtCodigo.getText());
                String materrias = txtMaterias.getText();

                // Actualiza los datos del estudiante con los nuevos valores
                estudiante.setNombre(nombre);
                estudiante.setApellido(apellido);
                estudiante.setCodigo(codigo);
                estudiante.setCursosTomados(List.of(materrias.replace(" ","").split(",")));

                // Retorna "true" para indicar que el estudiante fue editado exitosamente
                return true;
            }
            // Retorna "false" si el usuario cancela la edición
            return false;
        });

        // Muestra el diálogo y espera a que el usuario seleccione una opción
        Optional<Boolean> result = dialog.showAndWait();

        // Retorna el resultado de la edición
        return result.isPresent() && result.get();
    }
    @FXML
    protected void exportarCsvAction() throws IOException {
        FileWriter fileWriter = new FileWriter("C:\\Users\\erick\\Downloads\\estudiantes.csv");
        fileWriter.append("Nombre,Apellido,Código,Cursos tomados\n");
        for (Estudiante estudiante : adEstudiantes.getEstudiantes()) {
            fileWriter.append(estudiante.getNombre());
            fileWriter.append(",");
            fileWriter.append(estudiante.getApellido());
            fileWriter.append(",");
            fileWriter.append(Integer.toString(estudiante.getCodigo()));
            fileWriter.append(",");
            fileWriter.append(String.join(";", estudiante.getCursosTomados()));
            fileWriter.append("\n");
        }
        fileWriter.close();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Exportacion");
        alert.setHeaderText("Exportado csv ");
        alert.setContentText("Se realizo la exportacion correctamente");
        alert.showAndWait();
    }
    @FXML
    protected void exportarBiAction() throws IOException {

        BinaryTextWriter writter = new BinaryTextWriter("C:\\Users\\erick\\Downloads\\binario.txt");
        writter.write(adEstudiantes.getCadena());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Exportacion");
        alert.setHeaderText("Exportado binario ");
        alert.setContentText("Se realizo la exportacion correctamente se escribe la cadena: "+ adEstudiantes.getCadena());
        alert.showAndWait();


    }
    @FXML
    protected void cargarBiAction() throws IOException {
        BinaryFileReader reader = new BinaryFileReader("C:\\Users\\erick\\Downloads\\binario.txt");
        String cadena = reader.readFile();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Importacion");
        alert.setHeaderText("Importacion binario ");
        alert.setContentText("Se realizo la importacion la cadena obtenida es:" + cadena);
        alert.showAndWait();
        AdministradorEstudiantes adEstudiantes2 = new AdministradorEstudiantes("Nuevo");
        for(String estudiante: cadena.split("\n")){
            adEstudiantes2.crearNuevoEstudiante(adEstudiantes2.leerEstudiante(estudiante));
        }
        adEstudiantes2.mostrarEstudiantes();
        ObservableList<Estudiante> estudiantes = FXCollections.observableArrayList(adEstudiantes2.getEstudiantes());
        tablaEstudiantes.setItems(estudiantes);
    }
}