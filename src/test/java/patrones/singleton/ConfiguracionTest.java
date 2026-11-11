package patrones.singleton;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Singleton vs. configuración inmutable inyectada (Caso D)")
class ConfiguracionTest {

    @Test
    @DisplayName("Singleton: todos los hilos obtienen la misma instancia")
    void singletonThreadSafe() throws Exception {
        ExecutorService hilos = Executors.newFixedThreadPool(8);
        try {
            List<Callable<Configuracion>> tareas = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                tareas.add(() -> ConfiguracionGlobal.INSTANCIA.get());
            }
            Configuracion esperada = ConfiguracionGlobal.INSTANCIA.get();
            for (Future<Configuracion> f : hilos.invokeAll(tareas)) {
                assertSame(esperada, f.get());
            }
        } finally {
            hilos.shutdownNow();
        }
    }

    @Test
    @DisplayName("DI: en pruebas se inyecta otra configuración sin tocar estado global")
    void inyeccionFacilitaPruebas() {
        Configuracion pruebas = new Configuracion("jdbc:h2:mem:test", 1, true);
        String reporte = new ServicioReportes(pruebas).generar("ventas");
        assertEquals("Reporte 'ventas' guardado en memoria (modo pruebas) usando hasta 1 conexión", reporte);
    }

    @Test
    @DisplayName("DI: con la configuración de producción se usa la base de datos real")
    void configuracionProduccion() {
        String reporte = new ServicioReportes(Configuracion.porDefecto()).generar("ventas");
        assertTrue(reporte.contains("jdbc:postgresql://localhost:5432/app"));
        assertTrue(reporte.endsWith("10 conexiones"));
    }

    @Test
    @DisplayName("La configuración se carga desde propiedades y completa valores por defecto")
    void cargaDesdePropiedades() {
        Properties p = new Properties();
        p.setProperty("db.maxConexiones", "25");
        Configuracion c = Configuracion.desde(p);
        assertEquals(25, c.maxConexiones());
        assertEquals(Configuracion.porDefecto().urlBaseDatos(), c.urlBaseDatos());
    }

    @Test
    @DisplayName("Valores inválidos se rechazan al crear la configuración")
    void validacion() {
        assertThrows(IllegalArgumentException.class, () -> new Configuracion("http://x", 5, false));
        assertThrows(IllegalArgumentException.class, () -> new Configuracion("jdbc:h2:mem:x", 0, false));
        assertThrows(IllegalArgumentException.class, () -> new Configuracion("jdbc:h2:mem:x", 101, false));
        assertThrows(NullPointerException.class, () -> new Configuracion(null, 5, false));

        Properties p = new Properties();
        p.setProperty("db.maxConexiones", "muchas");
        assertThrows(IllegalArgumentException.class, () -> Configuracion.desde(p));
    }

    @Test
    @DisplayName("Dos configuraciones con los mismos datos son iguales (record)")
    void igualdadPorValor() {
        assertEquals(Configuracion.porDefecto(), Configuracion.porDefecto());
    }

    @Test
    @DisplayName("El servicio exige una configuración")
    void servicioRequiereConfiguracion() {
        assertThrows(NullPointerException.class, () -> new ServicioReportes(null));
    }
}
