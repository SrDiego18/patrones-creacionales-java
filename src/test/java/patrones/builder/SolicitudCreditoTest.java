package patrones.builder;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Builder: solicitud de crédito (Caso A)")
class SolicitudCreditoTest {

    private static SolicitudCredito.Builder base(String monto, int plazo) {
        return SolicitudCredito.builder("Ana Pérez", "1020304050", new BigDecimal(monto), plazo);
    }

    @Test
    @DisplayName("Escenario 1: solicitud solo con obligatorios")
    void soloObligatorios() {
        SolicitudCredito s = base("8000000", 24).build();
        assertAll(
                () -> assertEquals("Ana Pérez", s.getSolicitante()),
                () -> assertEquals(24, s.getPlazoMeses()),
                () -> assertTrue(s.getCodeudor().isEmpty()),
                () -> assertTrue(s.getGarantia().isEmpty()),
                () -> assertFalse(s.tieneRespaldo()),
                () -> assertFalse(s.isTasaPreferencial()));
    }

    @Test
    @DisplayName("Escenario 2: monto alto con garantía y tasa preferencial")
    void montoAltoConGarantia() {
        SolicitudCredito s = base("120000000", 120)
                .garantia("Hipoteca apartamento")
                .tasaPreferencial(true)
                .descripcion("Compra de vivienda")
                .build();
        assertAll(
                () -> assertEquals("Hipoteca apartamento", s.getGarantia().orElseThrow()),
                () -> assertTrue(s.isTasaPreferencial()),
                () -> assertTrue(s.tieneRespaldo()));
    }

    @Test
    @DisplayName("Escenario 3: monto alto con codeudor (sin garantía) es válido")
    void montoAltoConCodeudor() {
        assertTrue(base("60000000", 36).codeudor("Juan Ruiz").build().tieneRespaldo());
    }

    @Test
    @DisplayName("Escenario 4: monto alto sin codeudor ni garantía es rechazado")
    void montoAltoSinRespaldo() {
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> base("60000000", 36).build());
        assertEquals("Montos mayores a 50000000 requieren codeudor o garantía", e.getMessage());
    }

    @Test
    @DisplayName("El umbral exacto (50 000 000) no exige respaldo")
    void umbralExacto() {
        assertFalse(base("50000000", 36).build().tieneRespaldo());
    }

    @Test
    @DisplayName("Escenario 5: tasa preferencial sin garantía es rechazada")
    void tasaPreferencialSinGarantia() {
        assertThrows(IllegalStateException.class,
                () -> base("5000000", 12).codeudor("Juan").tasaPreferencial(true).build());
    }

    @ParameterizedTest(name = "Plazo de {0} meses es rechazado")
    @ValueSource(ints = {0, 5, 121, -1})
    void plazoFueraDeRango(int plazo) {
        assertThrows(IllegalStateException.class, () -> base("5000000", plazo).build());
    }

    @ParameterizedTest(name = "Plazo límite de {0} meses es aceptado")
    @ValueSource(ints = {6, 120})
    void plazoEnLimites(int plazo) {
        assertEquals(plazo, base("5000000", plazo).build().getPlazoMeses());
    }

    @ParameterizedTest(name = "Monto {0} fuera de rango es rechazado")
    @ValueSource(strings = {"0", "-100", "499999", "500000001"})
    void montoFueraDeRango(String monto) {
        assertThrows(IllegalStateException.class, () -> base(monto, 12).codeudor("Juan").build());
    }

    @ParameterizedTest(name = "Documento \"{0}\" es rechazado")
    @ValueSource(strings = {"12A456", "123", "1234567890123", "   "})
    void documentoInvalido(String documento) {
        assertThrows(IllegalStateException.class,
                () -> SolicitudCredito.builder("Ana", documento, new BigDecimal("5000000"), 12).build());
    }

    @Test
    @DisplayName("Obligatorios nulos se detectan al iniciar el builder")
    void obligatoriosNulos() {
        assertThrows(NullPointerException.class,
                () -> SolicitudCredito.builder(null, "1020304050", BigDecimal.TEN, 12));
        assertThrows(NullPointerException.class,
                () -> SolicitudCredito.builder("Ana", "1020304050", null, 12));
    }

    @Test
    @DisplayName("Opcionales en blanco se tratan como ausentes")
    void opcionalesEnBlanco() {
        SolicitudCredito s = base("5000000", 12).codeudor("  ").descripcion("").build();
        assertTrue(s.getCodeudor().isEmpty());
        assertTrue(s.getDescripcion().isEmpty());
    }

    @Test
    @DisplayName("equals/hashCode: igualdad por valor (montos 8000000 y 8000000.00)")
    void igualdadPorValor() {
        SolicitudCredito a = base("8000000", 24).build();
        SolicitudCredito b = base("8000000.00", 24).build();
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, base("8000000", 36).build());
    }

    @Test
    @DisplayName("toString no muestra campos opcionales vacíos")
    void toStringLegible() {
        String texto = base("8000000", 24).build().toString();
        assertTrue(texto.contains("monto=8000000"));
        assertFalse(texto.contains("codeudor"));
    }
}
