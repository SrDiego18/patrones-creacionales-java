package patrones.singleton;

import java.util.Properties;

/** Ejecución de ejemplo: Singleton frente a configuración inmutable inyectada. */
public final class DemoSingleton {

    private DemoSingleton() {
    }

    public static void main(String[] args) {
        System.out.println("--- Singleton (alternativa evaluada) ---");
        System.out.println("Misma instancia: " + (ConfiguracionGlobal.INSTANCIA == ConfiguracionGlobal.valueOf("INSTANCIA")));

        System.out.println("--- Configuración inmutable + inyección de dependencias (elegida) ---");
        Properties archivo = new Properties();
        archivo.setProperty("db.url", "jdbc:postgresql://db.produccion:5432/ventas");
        archivo.setProperty("db.maxConexiones", "20");
        Configuracion produccion = Configuracion.desde(archivo);
        System.out.println(new ServicioReportes(produccion).generar("ventas-q3"));

        Configuracion pruebas = new Configuracion("jdbc:h2:mem:test", 1, true);
        System.out.println(new ServicioReportes(pruebas).generar("ventas-q3"));
    }
}
