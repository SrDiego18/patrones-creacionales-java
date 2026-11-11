package patrones.singleton;

import java.util.Objects;
import java.util.Properties;

/**
 * Configuración inmutable de la aplicación (Caso D de la guía).
 * Al ser un {@code record} con validación, se puede compartir sin riesgo entre
 * módulos e hilos, y se inyecta por constructor donde se necesite.
 */
public record Configuracion(String urlBaseDatos, int maxConexiones, boolean modoPruebas) {

    public Configuracion {
        Objects.requireNonNull(urlBaseDatos, "urlBaseDatos");
        if (!urlBaseDatos.startsWith("jdbc:")) {
            throw new IllegalArgumentException("La URL debe iniciar con jdbc:");
        }
        if (maxConexiones < 1 || maxConexiones > 100) {
            throw new IllegalArgumentException("maxConexiones debe estar entre 1 y 100");
        }
    }

    /** Valores por defecto de producción. */
    public static Configuracion porDefecto() {
        return new Configuracion("jdbc:postgresql://localhost:5432/app", 10, false);
    }

    /** Crea la configuración a partir de propiedades (por ejemplo, un archivo .properties). */
    public static Configuracion desde(Properties p) {
        Configuracion base = porDefecto();
        try {
            return new Configuracion(
                    p.getProperty("db.url", base.urlBaseDatos()),
                    Integer.parseInt(p.getProperty("db.maxConexiones", String.valueOf(base.maxConexiones()))),
                    Boolean.parseBoolean(p.getProperty("app.modoPruebas", String.valueOf(base.modoPruebas()))));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("db.maxConexiones debe ser un número", e);
        }
    }
}
