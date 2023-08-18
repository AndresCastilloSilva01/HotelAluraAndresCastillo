package hotelaluraandrescastillo;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HotelVista extends Application {

    private GestorBaseDeDatos gestor;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        String url = "jdbc:mysql://localhost:3306/hotel_management";
        String usuario = "root";
        String contraseña = "";
        gestor = new GestorBaseDeDatos(url, usuario, contraseña);

        primaryStage.setTitle("Hotel Alura Andres Castillo");

        VBox root = new VBox(10);

        Button reservarButton = new Button("Reservar habitación");
        Button modificarButton = new Button("Modificar fechas de estadía");
        Button verListadoButton = new Button("Ver listado de habitaciones en uso");

        reservarButton.setOnAction(e -> mostrarReservarVentana());
        modificarButton.setOnAction(e -> mostrarModificarVentana());
        verListadoButton.setOnAction(e -> mostrarListadoHabitaciones());

        root.getChildren().addAll(reservarButton, modificarButton, verListadoButton);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void mostrarReservarVentana() {
        
    Stage reservarStage = new Stage();
    reservarStage.setTitle("Reservar Habitación");

    VBox reservarBox = new VBox(10);

    Label nombreLabel = new Label("Nombre del cliente:");
    TextField nombreField = new TextField();

    Label tipoHabitacionLabel = new Label("Tipo de habitación (Individual/Doble):");
    ComboBox<String> tipoHabitacionCombo = new ComboBox<>();
    tipoHabitacionCombo.getItems().addAll("Individual", "Doble");

    Label fechaLlegadaLabel = new Label("Fecha de llegada (YYYY-MM-DD):");
    DatePicker fechaLlegadaPicker = new DatePicker();

    Label diasEstadiaLabel = new Label("Días de estadía:");
    TextField diasEstadiaField = new TextField();

    Button reservarButton = new Button("Reservar");
    reservarButton.setOnAction(e -> {
        String nombre = nombreField.getText();
        String tipoHabitacion = tipoHabitacionCombo.getValue();
        LocalDate fechaLlegada = fechaLlegadaPicker.getValue();
        int diasEstadia = Integer.parseInt(diasEstadiaField.getText());

        double costoPorNoche = tipoHabitacion.equals("Individual") ? 100.0 : 150.0;
        double costoTotal = costoPorNoche * diasEstadia;

        // Crear la habitación y el cliente
        Habitacion habitacion = new Habitacion(gestor.obtenerSiguienteIdHabitacion(), "Ocupada", tipoHabitacion);
        gestor.insertarHabitacion(habitacion);

        Date fechaLlegadaDate = java.sql.Date.valueOf(fechaLlegada);
        Date fechaSalidaDate = new Date(fechaLlegadaDate.getTime() + (diasEstadia * 24 * 60 * 60 * 1000));
        Cliente cliente = new Cliente(gestor.obtenerSiguienteIdCliente(), nombre, habitacion, fechaLlegadaDate, fechaSalidaDate, costoTotal);
        gestor.insertarCliente(cliente);

        mostrarMensaje("Reserva realizada con éxito.");
        reservarStage.close();
    });

    reservarBox.getChildren().addAll(
        nombreLabel, nombreField,
        tipoHabitacionLabel, tipoHabitacionCombo,
        fechaLlegadaLabel, fechaLlegadaPicker,
        diasEstadiaLabel, diasEstadiaField,
        reservarButton
    );

    Scene reservarScene = new Scene(reservarBox, 300, 300);
    reservarStage.setScene(reservarScene);
    reservarStage.show();
}
    private void mostrarMensaje(String mensaje) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Mensaje");
    alert.setHeaderText(null);
    alert.setContentText(mensaje);
    alert.showAndWait();
}


    


    private void mostrarModificarVentana() {
    Stage modificarStage = new Stage();
    modificarStage.setTitle("Modificar Fechas de Estadía");

    VBox modificarBox = new VBox(10);

    Label idClienteLabel = new Label("ID del cliente a modificar:");
    TextField idClienteField = new TextField();

    Label nuevaFechaLlegadaLabel = new Label("Nueva fecha de llegada (YYYY-MM-DD):");
    DatePicker nuevaFechaLlegadaPicker = new DatePicker();

    Label nuevosDiasEstadiaLabel = new Label("Nuevos días de estadía:");
    TextField nuevosDiasEstadiaField = new TextField();

    Button modificarButton = new Button("Modificar");
    modificarButton.setOnAction(e -> {
        int idCliente = Integer.parseInt(idClienteField.getText());
        LocalDate nuevaFechaLlegada = nuevaFechaLlegadaPicker.getValue();
        int nuevosDiasEstadia = Integer.parseInt(nuevosDiasEstadiaField.getText());

        // Obtener el cliente de la base de datos
        Cliente clienteModificar = gestor.obtenerClientePorId(idCliente);
        if (clienteModificar != null) {
            // Calcular nuevas fechas y costo
            Date nuevaFechaLlegadaDate = java.sql.Date.valueOf(nuevaFechaLlegada);
            Date nuevaFechaSalidaDate = new Date(nuevaFechaLlegadaDate.getTime() + (nuevosDiasEstadia * 24 * 60 * 60 * 1000));
            double costoPorNoche = clienteModificar.getHabitacion().getCostoPorNoche();
            double nuevoCostoTotal = costoPorNoche * nuevosDiasEstadia;

            // Actualizar las fechas y el costo en el cliente
            clienteModificar.setFechaLlegada(nuevaFechaLlegadaDate);
            clienteModificar.setFechaSalida(nuevaFechaSalidaDate);
            clienteModificar.setCosto(nuevoCostoTotal);

            // Actualizar el cliente en la base de datos
            gestor.actualizarCliente(clienteModificar);

            mostrarMensaje("Estadía modificada exitosamente.");
            modificarStage.close();
        } else {
            mostrarMensaje("Cliente no encontrado.");
        }
    });

    modificarBox.getChildren().addAll(
        idClienteLabel, idClienteField,
        nuevaFechaLlegadaLabel, nuevaFechaLlegadaPicker,
        nuevosDiasEstadiaLabel, nuevosDiasEstadiaField,
        modificarButton
    );

    Scene modificarScene = new Scene(modificarBox, 300, 300);
    modificarStage.setScene(modificarScene);
    modificarStage.show();
}


    private void mostrarListadoHabitaciones() {
        VBox listadoBox = new VBox(10);

        Label tituloLabel = new Label("Listado de Habitaciones y Clientes:");
        listadoBox.getChildren().add(tituloLabel);

        List<Habitacion> habitaciones = gestor.obtenerTodasLasHabitaciones();
        for (Habitacion habitacion : habitaciones) {
            Cliente clienteEnHabitacion = gestor.obtenerClientePorHabitacion(habitacion);
            String estadoCliente = (clienteEnHabitacion != null) ? "Ocupada por " + clienteEnHabitacion.getNombre() : "Disponible";
            Label habitacionLabel = new Label("Habitación " + habitacion.getId() + " (" + habitacion.getTipo() + "): " + estadoCliente);
            listadoBox.getChildren().add(habitacionLabel);
        }

        Scene listadoScene = new Scene(listadoBox, 600, 400);
        Stage listadoStage = new Stage();
        listadoStage.setTitle("Listado de Habitaciones");
        listadoStage.setScene(listadoScene);
        listadoStage.show();
    }



}