package hotelaluraandrescastillo;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class HotelAluraAndresCastillo {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/hotel_management";
        String usuario = "root";
        String contraseña = "";

        GestorBaseDeDatos gestor = new GestorBaseDeDatos(url, usuario, contraseña);

        Scanner scanner = new Scanner(System.in);
        
        HotelVista.launch(HotelVista.class, args);

        int opcion;
        do {
            System.out.println("1. Reservar habitación");
            System.out.println("2. Modificar fechas de estadía");
            System.out.println("3. Ver listado de habitaciones en uso");
            System.out.println("4. Salir");
            System.out.print("Selecciona una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir la nueva línea

            switch (opcion) {
                case 1:
                    System.out.print("Nombre del cliente: ");
                    String nombre = scanner.nextLine();
                    System.out.print("Tipo de habitación (Individual/Doble): ");
                    String tipoHabitacion = scanner.nextLine();
                    System.out.print("Fecha de llegada (YYYY-MM-DD): ");
                    String fechaLlegadaStr = scanner.nextLine();
                    System.out.print("Días de estadía: ");
                    int diasEstadia = scanner.nextInt();

                    // Calcular costo por noche y total
                    double costoPorNoche = tipoHabitacion.equals("Individual") ? 100.0 : 150.0;
                    double costoTotal = costoPorNoche * diasEstadia;

                    // Crear la habitación y el cliente
                    Habitacion habitacion = new Habitacion(gestor.obtenerSiguienteIdHabitacion(), "Ocupada", tipoHabitacion);
                    gestor.insertarHabitacion(habitacion);

                    Date fechaLlegada = java.sql.Date.valueOf(fechaLlegadaStr);
                    Date fechaSalida = new Date(fechaLlegada.getTime() + (diasEstadia * 24 * 60 * 60 * 1000));
                    Cliente cliente = new Cliente(gestor.obtenerSiguienteIdCliente(), nombre, habitacion, fechaLlegada, fechaSalida, costoTotal);
                    gestor.insertarCliente(cliente);

                    System.out.println("Reserva realizada con éxito.");
                    break;

                case 2:
                    System.out.print("ID del cliente a modificar: ");
                    int idClienteModificar = scanner.nextInt();
                    scanner.nextLine(); // Consumir la nueva línea

                    System.out.print("Nueva fecha de llegada (YYYY-MM-DD): ");
                    String nuevaFechaLlegadaStr = scanner.nextLine();
                    System.out.print("Nuevos días de estadía: ");
                    int nuevosDiasEstadia = scanner.nextInt();

                    // Obtener el cliente de la base de datos
                    Cliente clienteModificar = gestor.obtenerClientePorId(idClienteModificar);
                    if (clienteModificar != null) {
                        // Calcular nuevas fechas y costo
                        Date nuevaFechaLlegada = java.sql.Date.valueOf(nuevaFechaLlegadaStr);
                        Date nuevaFechaSalida = new Date(nuevaFechaLlegada.getTime() + (nuevosDiasEstadia * 24 * 60 * 60 * 1000));
                        // Obtener la habitación asociada al cliente
                        Habitacion habitacionCliente = clienteModificar.getHabitacion();
                        double costoPorNoche1 = habitacionCliente.getCostoPorNoche();
                        double nuevoCostoTotal = costoPorNoche1 * nuevosDiasEstadia;

                        // Actualizar las fechas y el costo en el cliente
                        clienteModificar.setFechaLlegada(nuevaFechaLlegada);
                        clienteModificar.setFechaSalida(nuevaFechaSalida);
                        clienteModificar.setCosto(nuevoCostoTotal);

                        // Actualizar el cliente en la base de datos
                        gestor.actualizarCliente(clienteModificar);

                        System.out.println("Estadía modificada exitosamente.");
                    } else {
                        System.out.println("Cliente no encontrado.");
                    }
                    break;

                case 3:
                    System.out.println("Lista de Habitaciones y Clientes:");
                    List<Habitacion> habitaciones = gestor.obtenerTodasLasHabitaciones();
                    for (Habitacion habitacionActual : habitaciones) {
                        Cliente clienteEnHabitacion = gestor.obtenerClientePorHabitacion(habitacionActual);
                        String estadoCliente = (clienteEnHabitacion != null) ? "Ocupada por " + clienteEnHabitacion.getNombre() : "Disponible";
                        System.out.println("Habitación " + habitacionActual.getId() + " (" + habitacionActual.getTipo() + "): " + estadoCliente);
                    }
                    break;

                default:
                    System.out.println("Opción inválida.");
                    break;
            }
        } while (opcion != 3);

        scanner.close();
        try {
            gestor.getConexion().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
