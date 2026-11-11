package patrones.prototype;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Prototype: plantillas de informes (Caso C)")
class PrototypeTest {

    private CargadorPlantillas cargador;
    private RegistroPlantillas registro;

    @BeforeEach
    void preparar() {
        cargador = new CargadorPlantillas();
        registro = new RegistroPlantillas();
        registro.registrar("trimestral", cargador.cargarInformeTrimestral());
    }

    @Test
    @DisplayName("La inicialización costosa ocurre una sola vez para muchas copias")
    void inicializacionUnica() {
        for (int i = 0; i < 50; i++) {
            registro.crear("trimestral");
        }
        assertEquals(1, cargador.getInicializaciones());
    }

    @Test
    @DisplayName("La copia es profunda: modificar una sección de la copia no altera el prototipo")
    void copiaProfundaSecciones() {
        Informe copia = registro.crear("trimestral");
        copia.seccion(2).setContenido("Modificado");

        assertEquals("Pendiente", registro.crear("trimestral").seccion(2).getContenido());
    }

    @Test
    @DisplayName("Las secciones de la copia son objetos distintos con el mismo contenido")
    void seccionesSonObjetosNuevos() {
        Informe a = registro.crear("trimestral");
        Informe b = registro.crear("trimestral");
        assertNotSame(a, b);
        assertNotSame(a.seccion(0), b.seccion(0));
        assertEquals(a.seccion(0).getContenido(), b.seccion(0).getContenido());
    }

    @Test
    @DisplayName("Agregar secciones o estilos a una copia no afecta a las demás")
    void coleccionesIndependientes() {
        Informe copia = registro.crear("trimestral");
        copia.agregarSeccion(new Seccion("Anexo", "Datos extra")).definirEstilo("color", "#FF0000");

        Informe otra = registro.crear("trimestral");
        assertEquals(3, otra.getSecciones().size());
        assertEquals("#003A70", otra.getEstilos().get("color"));
    }

    @Test
    @DisplayName("El registro guarda una copia defensiva del prototipo recibido")
    void copiaDefensivaAlRegistrar() {
        Informe original = new Informe("Mensual").agregarSeccion(new Seccion("Resumen", "v1"));
        registro.registrar("mensual", original);
        original.seccion(0).setContenido("cambiado después");

        assertEquals("v1", registro.crear("mensual").seccion(0).getContenido());
    }

    @Test
    @DisplayName("Las listas expuestas son de solo lectura")
    void vistasInmutables() {
        Informe informe = registro.crear("trimestral");
        assertThrows(UnsupportedOperationException.class, () -> informe.getSecciones().clear());
        assertThrows(UnsupportedOperationException.class, () -> informe.getEstilos().clear());
    }

    @Test
    @DisplayName("Pedir una plantilla inexistente produce un error claro")
    void plantillaInexistente() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> registro.crear("anual"));
        assertEquals("No existe la plantilla: anual", e.getMessage());
    }
}
