package patrones.abstractfactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Abstract Factory: componentes por tema (Caso B)")
class AbstractFactoryTest {

    @ParameterizedTest(name = "Todos los componentes de la fábrica {0} pertenecen a ese tema")
    @EnumSource(Tema.class)
    void familiaCompleta(Tema tema) {
        FabricaComponentes fabrica = FabricaComponentes.para(tema);
        assertEquals(tema, fabrica.crearBoton().tema());
        assertEquals(tema, fabrica.crearCuadroTexto().tema());
        assertEquals(tema, fabrica.crearMenu().tema());
    }

    @ParameterizedTest(name = "Formulario con tema {0} es consistente")
    @EnumSource(Tema.class)
    void formularioConsistente(Tema tema) {
        FormularioLogin formulario = new FormularioLogin(FabricaComponentes.para(tema));
        assertTrue(formulario.esConsistente());
        assertEquals(tema, formulario.tema());
    }

    @Test
    @DisplayName("El mismo cliente produce resultados distintos según la fábrica, sin cambiar su código")
    void mismoClienteDistintaFamilia() {
        String institucional = new FormularioLogin(new FabricaInstitucional()).dibujar();
        String altoContraste = new FormularioLogin(new FabricaAltoContraste()).dibujar();
        assertNotEquals(institucional, altoContraste);
        assertTrue(institucional.contains("[Botón azul #003A70 | Ingresar]"));
        assertTrue(altoContraste.contains("[BOTÓN NEGRO/AMARILLO | INGRESAR]"));
    }

    @Test
    @DisplayName("Los menús respetan el separador de su tema")
    void menusPorTema() {
        List<String> opciones = List.of("Inicio", "Ayuda");
        assertEquals("[Menú marca | Inicio · Ayuda]", new FabricaInstitucional().crearMenu().renderizar(opciones));
        assertEquals("[MENÚ ALTO CONTRASTE | Inicio / Ayuda]",
                new FabricaAltoContraste().crearMenu().renderizar(opciones));
    }

    @Test
    @DisplayName("La fábrica correcta se elige a partir del tema")
    void seleccionDeFabrica() {
        assertInstanceOf(FabricaInstitucional.class, FabricaComponentes.para(Tema.INSTITUCIONAL));
        assertInstanceOf(FabricaAltoContraste.class, FabricaComponentes.para(Tema.ALTO_CONTRASTE));
    }

    @ParameterizedTest(name = "\"{0}\" se interpreta como un tema válido")
    @ValueSource(strings = {"institucional", "ALTO_CONTRASTE", "alto-contraste", " alto contraste "})
    void temaDesdeTexto(String texto) {
        assertTrue(List.of(Tema.values()).contains(Tema.desdeTexto(texto)));
    }

    @Test
    @DisplayName("Un tema desconocido o nulo es rechazado")
    void temaInvalido() {
        assertThrows(IllegalArgumentException.class, () -> Tema.desdeTexto("neón"));
        assertThrows(IllegalArgumentException.class, () -> FabricaComponentes.para(null));
        assertThrows(NullPointerException.class, () -> new FormularioLogin(null));
    }
}
