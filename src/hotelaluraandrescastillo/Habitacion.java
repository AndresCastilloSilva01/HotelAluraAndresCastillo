package hotelaluraandrescastillo;

import java.util.List;

public class Habitacion {

    private int id;
    private String estado;
    private String tipo;
    private int numeroPiso;
    private int numeroHabitacionEnPiso;

    public Habitacion(int id, String estado, String tipo) {
        this.id = id;
        this.estado = estado;
        this.tipo = tipo;
        this.numeroPiso = 0;
        this.numeroHabitacionEnPiso = 0;
    }

    public Habitacion(int id, String estado, String tipo, int numeroPiso, int numeroHabitacionEnPiso) {
        this.id = id;
        this.estado = estado;
        this.tipo = tipo;
        this.numeroPiso = numeroPiso;
        this.numeroHabitacionEnPiso = numeroHabitacionEnPiso;
    }

    public double getCostoPorNoche() {
        if (tipo.equals("Individual")) {
            return 100.0;
        } else if (tipo.equals("Doble")) {
            return 150.0;
        }
        return 0.0;
    }

    public String getNumeroCompleto() {
        GestorBaseDeDatos gestor = new GestorBaseDeDatos(); // Crea una instancia del gestor
        List<Habitacion> habitaciones = gestor.obtenerTodasLasHabitaciones(); // Llama al método en la instancia

        for (Habitacion habitacion : habitaciones) {
            Cliente clienteEnHabitacion = gestor.obtenerClientePorHabitacion(habitacion);

            String estadoCliente = (clienteEnHabitacion != null) ? "Ocupada por " + clienteEnHabitacion.getNombre() : "Disponible";
            System.out.println("Habitación " + habitacion.getNumeroCompleto() + " (" + habitacion.getTipo() + "): " + estadoCliente);
        }

        return String.format("%d%02d", numeroPiso, numeroHabitacionEnPiso);
    }

    public int getNumeroPiso() {
        return numeroPiso;
    }

    public void setNumeroPiso(int numeroPiso) {
        this.numeroPiso = numeroPiso;
    }

    public int getNumeroHabitacionEnPiso() {
        return numeroHabitacionEnPiso;
    }

    public void setNumeroHabitacionEnPiso(int numeroHabitacionEnPiso) {
        this.numeroHabitacionEnPiso = numeroHabitacionEnPiso;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
