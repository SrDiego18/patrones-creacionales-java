package patrones.singleton;

import java.util.Objects;

/** Módulo que necesita configuración: la recibe explícitamente por constructor. */
public class ServicioReportes {

    private final Configuracion config;

    public ServicioReportes(Configuracion config) {
        this.config = Objects.requireNonNull(config, "config");
    }

    public String generar(String nombre) {
        String destino = config.modoPruebas() ? "memoria (modo pruebas)" : config.urlBaseDatos();
        return "Reporte '" + nombre + "' guardado en " + destino
                + " usando hasta " + config.maxConexiones()
                + (config.maxConexiones() == 1 ? " conexión" : " conexiones");
    }
}
