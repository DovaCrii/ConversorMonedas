package conversor.modelo;

import java.time.LocalDateTime;

public class RegistroConversion {
    private String monedaOrigen;
    private String monedaDestino;
    private double cantidad;
    private double resultado;
    private LocalDateTime timestamp;

    public RegistroConversion(String monedaOrigen, String monedaDestino, double cantidad, double resultado, LocalDateTime timestamp) {
        this.monedaOrigen = monedaOrigen;
        this.monedaDestino = monedaDestino;
        this.cantidad = cantidad;
        this.resultado = resultado;
        this.timestamp = timestamp;
    }

    public String getMonedaOrigen() {
        return monedaOrigen;
    }

    public String getMonedaDestino() {
        return monedaDestino;
    }

    public double getCantidad() {
        return cantidad;
    }

    public double getResultado() {
        return resultado;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "RegistroConversion{" +
                "monedaOrigen='" + monedaOrigen + '\'' +
                ", monedaDestino='" + monedaDestino + '\'' +
                ", cantidad=" + cantidad +
                ", resultado=" + resultado +
                ", timestamp=" + timestamp +
                '}';
    }
}