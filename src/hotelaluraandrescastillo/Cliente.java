
package hotelaluraandrescastillo;
import java.util.Date;

public class Cliente {
    private int id;
    private String nombre;
    private Habitacion habitacion;
    private Date fechaLlegada;
    private Date fechaSalida;
    private double costo;

    public Cliente(int id, String nombre, Habitacion habitacion, Date fechaLlegada, Date fechaSalida, double costo) {
        this.id = id;
        this.nombre = nombre;
        this.habitacion = habitacion;
        this.fechaLlegada = fechaLlegada;
        this.fechaSalida = fechaSalida;
        this.costo = costo;
    }

    
    // Métodos para calcular el costo total y modificar fechas

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

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }

    public Date getFechaLlegada() {
        return fechaLlegada;
    }

    public void setFechaLlegada(Date fechaLlegada) {
        this.fechaLlegada = fechaLlegada;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }
}


