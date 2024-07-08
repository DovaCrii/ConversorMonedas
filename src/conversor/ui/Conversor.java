package conversor.ui;

import conversor.api.ClienteApi;
import conversor.util.ManejadorJson;
import conversor.modelo.RegistroConversion;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Conversor {
    private static List<RegistroConversion> historial = new ArrayList<>();
    private static final String[] MONEDAS = {"USD", "ARS", "BRL", "CLP", "EUR", "JPY"};

    private static final DecimalFormatSymbols SYMBOLS = new DecimalFormatSymbols(Locale.US);
    static {
        SYMBOLS.setGroupingSeparator(',');
        SYMBOLS.setDecimalSeparator('.');
    }
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.0", SYMBOLS);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bienvenido al Conversor de Monedas");

        try {
            String respuesta = ClienteApi.obtenerTasasDeCambio();
            JsonObject jsonObject = ManejadorJson.parsearJson(respuesta);

            while (true) {
                System.out.println("\nSeleccione una opción:");
                System.out.println("1. Convertir Moneda");
                System.out.println("2. Ver Historial de Conversiones");
                System.out.println("3. Salir");
                int opcion = leerOpcion(scanner);

                if (opcion == 1) {
                    System.out.println("Seleccione la moneda de origen:");
                    String monedaOrigen = seleccionarMoneda(scanner);

                    System.out.println("Seleccione la moneda de destino:");
                    String monedaDestino = seleccionarMoneda(scanner);

                    System.out.print("Ingrese la cantidad a convertir: ");
                    try {
                        double cantidad = scanner.nextDouble();

                        double tasaOrigen = jsonObject.getAsJsonObject("conversion_rates").get(monedaOrigen).getAsDouble();
                        double tasaDestino = jsonObject.getAsJsonObject("conversion_rates").get(monedaDestino).getAsDouble();
                        double resultado = (cantidad / tasaOrigen) * tasaDestino;
                        String resultadoFormateado = DECIMAL_FORMAT.format(resultado);

                        System.out.println("Resultado: " + resultadoFormateado + " " + monedaDestino);

                        agregarConversionAlHistorial(monedaOrigen, monedaDestino, cantidad, resultado);
                        imprimirSeparador();
                    } catch (InputMismatchException e) {
                        System.out.println("Error: No soporta este formato. Ingrese un número válido.");
                        scanner.nextLine();
                    }
                } else if (opcion == 2) {
                    mostrarHistorial();
                } else if (opcion == 3) {
                    break;
                } else {
                    System.out.println("Opción no válida. Intente nuevamente.");
                }
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error al obtener las tasas de cambio: " + e.getMessage());
        }
    }

    private static int leerOpcion(Scanner scanner) {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Error: No soporta este formato. Ingrese un número válido.");
            scanner.nextLine();
            return leerOpcion(scanner);
        }
    }

    private static String seleccionarMoneda(Scanner scanner) {
        for (int i = 0; i < MONEDAS.length; i++) {
            System.out.println((i + 1) + ". " + MONEDAS[i]);
        }
        try {
            int seleccion = scanner.nextInt();
            scanner.nextLine();

            if (seleccion < 1 || seleccion > MONEDAS.length) {
                System.out.println("Selección no válida. Intente nuevamente.");
                return seleccionarMoneda(scanner);
            }
            return MONEDAS[seleccion - 1];
        } catch (InputMismatchException e) {
            System.out.println("Error: No soporta este formato. Ingrese un número válido.");
            scanner.nextLine();
            return seleccionarMoneda(scanner);
        }
    }

    private static void agregarConversionAlHistorial(String monedaOrigen, String monedaDestino, double cantidad, double resultado) {
        RegistroConversion registro = new RegistroConversion(monedaOrigen, monedaDestino, cantidad, resultado, LocalDateTime.now());
        historial.add(registro);
    }

    private static void mostrarHistorial() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("Historial de Conversiones:");
        for (RegistroConversion registro : historial) {
            String formattedDateTime = registro.getTimestamp().format(formatter);
            System.out.println("RegistroConversion{" +
                    "monedaOrigen='" + registro.getMonedaOrigen() + '\'' +
                    ", monedaDestino='" + registro.getMonedaDestino() + '\'' +
                    ", cantidad=" + DECIMAL_FORMAT.format(registro.getCantidad()) +
                    ", resultado=" + DECIMAL_FORMAT.format(registro.getResultado()) +
                    ", registro=" + formattedDateTime +
                    '}');
        }
    }

    private static void imprimirSeparador() {
        System.out.println("********************************************");
    }
}
