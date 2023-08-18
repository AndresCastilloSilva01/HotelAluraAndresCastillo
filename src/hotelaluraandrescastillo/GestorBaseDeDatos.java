/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelaluraandrescastillo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GestorBaseDeDatos {
    private Connection conexion;

    public GestorBaseDeDatos(String url, String usuario, String contraseña) {
        try {
            conexion = DriverManager.getConnection(url, usuario, contraseña);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    GestorBaseDeDatos() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public Connection getConexion() {
        return conexion;
    }

    public int obtenerSiguienteIdCliente() {
        int siguienteId = 0;
        String query = "SELECT MAX(id) FROM Clientes";
        try (PreparedStatement statement = conexion.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                siguienteId = resultSet.getInt(1) + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return siguienteId;
    }
    
    

    public int obtenerSiguienteIdHabitacion() {
        int siguienteId = 0;
        String query = "SELECT MAX(id) FROM Habitaciones";
        try (PreparedStatement statement = conexion.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                siguienteId = resultSet.getInt(1) + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return siguienteId;
    }
    
    public void insertarHabitacion(Habitacion habitacion) {
        String query = "INSERT INTO Habitaciones (id, estado, tipo) VALUES (?, ?, ?)";
        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setInt(1, habitacion.getId());
            statement.setString(2, habitacion.getEstado());
            statement.setString(3, habitacion.getTipo());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizarEstadoHabitacion(int habitacionId, String nuevoEstado) {
        String query = "UPDATE Habitaciones SET estado = ? WHERE id = ?";
        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setString(1, nuevoEstado);
            statement.setInt(2, habitacionId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertarCliente(Cliente cliente) {
        String query = "INSERT INTO Clientes (id, nombre, habitacion_id, fecha_llegada, fecha_salida, costo) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setInt(1, cliente.getId());
            statement.setString(2, cliente.getNombre());
            statement.setInt(3, cliente.getHabitacion().getId());
            statement.setDate(4, new java.sql.Date(cliente.getFechaLlegada().getTime()));
            statement.setDate(5, new java.sql.Date(cliente.getFechaSalida().getTime()));
            statement.setDouble(6, cliente.getCosto());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public Cliente obtenerClientePorId(int idCliente) {
    String query = "SELECT * FROM Clientes WHERE id = ?";
    try (PreparedStatement statement = conexion.prepareStatement(query)) {
        statement.setInt(1, idCliente);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nombre = resultSet.getString("nombre");
                int habitacionId = resultSet.getInt("habitacion_id");
                Date fechaLlegada = resultSet.getDate("fecha_llegada");
                Date fechaSalida = resultSet.getDate("fecha_salida");
                double costo = resultSet.getDouble("costo");

                Habitacion habitacion = obtenerHabitacionPorId(habitacionId);

                return new Cliente(id, nombre, habitacion, fechaLlegada, fechaSalida, costo);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null; // Si no se encuentra el cliente
}
    
    public Habitacion obtenerHabitacionPorId(int habitacionId) {
    String query = "SELECT * FROM Habitaciones WHERE id = ?";
    try (PreparedStatement statement = conexion.prepareStatement(query)) {
        statement.setInt(1, habitacionId);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String estado = resultSet.getString("estado");
                String tipo = resultSet.getString("tipo");
                int numeroPiso = resultSet.getInt("numero_piso");
                int numeroHabitacionEnPiso = resultSet.getInt("numero_habitacion_en_piso");

                return new Habitacion(id, estado, tipo, numeroPiso, numeroHabitacionEnPiso);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null; // Si no se encuentra la habitación
}

public void actualizarCliente(Cliente cliente) {
    String query = "UPDATE Clientes SET nombre = ?, habitacion_id = ?, fecha_llegada = ?, fecha_salida = ?, costo = ? WHERE id = ?";
    try (PreparedStatement statement = conexion.prepareStatement(query)) {
        statement.setString(1, cliente.getNombre());
        statement.setInt(2, cliente.getHabitacion().getId());
        statement.setDate(3, new java.sql.Date(cliente.getFechaLlegada().getTime()));
        statement.setDate(4, new java.sql.Date(cliente.getFechaSalida().getTime()));
        statement.setDouble(5, cliente.getCosto());
        statement.setInt(6, cliente.getId());
        statement.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

public List<Habitacion> obtenerTodasLasHabitaciones() {
    List<Habitacion> habitaciones = new ArrayList<>();
    String query = "SELECT * FROM Habitaciones";
    try (PreparedStatement statement = conexion.prepareStatement(query)) {
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String estado = resultSet.getString("estado");
                String tipo = resultSet.getString("tipo");
                int numeroPiso = resultSet.getInt("numero_piso");
                int numeroHabitacionEnPiso = resultSet.getInt("numero_habitacion_en_piso");

                Habitacion habitacion = new Habitacion(id, estado, tipo, numeroPiso, numeroHabitacionEnPiso);
                habitaciones.add(habitacion);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return habitaciones;
}

public Cliente obtenerClientePorHabitacion(Habitacion habitacion) {
    String query = "SELECT * FROM Clientes WHERE habitacion_id = ?";
    try (PreparedStatement statement = conexion.prepareStatement(query)) {
        statement.setInt(1, habitacion.getId());
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nombre = resultSet.getString("nombre");
                Date fechaLlegada = resultSet.getDate("fecha_llegada");
                Date fechaSalida = resultSet.getDate("fecha_salida");
                double costo = resultSet.getDouble("costo");

                return new Cliente(id, nombre, habitacion, fechaLlegada, fechaSalida, costo);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null; // Si no se encuentra un cliente en la habitación
}



    // Otros métodos para consultar registros, manejar transacciones, etc.
}

