package patrones.prototype;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simula la inicialización costosa del informe base (consultas, cálculos,
 * carga de estilos). Cuenta cuántas veces se ejecuta para poder comprobar
 * que con Prototype ocurre una sola vez.
 */
public class CargadorPlantillas {

    private final AtomicInteger inicializaciones = new AtomicInteger();

    public Informe cargarInformeTrimestral() {
        inicializaciones.incrementAndGet();
        return new Informe("Informe trimestral")
                .agregarSeccion(new Seccion("Resumen", "Indicadores consolidados del trimestre"))
                .agregarSeccion(new Seccion("Ventas", "Tabla de ventas por región"))
                .agregarSeccion(new Seccion("Conclusiones", "Pendiente"))
                .definirEstilo("fuente", "Calibri 11")
                .definirEstilo("color", "#003A70");
    }

    public int getInicializaciones() {
        return inicializaciones.get();
    }
}
