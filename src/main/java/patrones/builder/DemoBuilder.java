package patrones.builder;

import java.math.BigDecimal;

/** Ejecución de ejemplo del patrón Builder. */
public final class DemoBuilder {

    private DemoBuilder() {
    }

    public static void main(String[] args) {
        SolicitudCredito libreInversion = SolicitudCredito
                .builder("Ana Pérez", "1020304050", new BigDecimal("8000000"), 24)
                .descripcion("Libre inversión")
                .build();
        System.out.println("Creada: " + libreInversion);

        SolicitudCredito vivienda = SolicitudCredito
                .builder("Luis Gómez", "79888777", new BigDecimal("120000000"), 120)
                .garantia("Hipoteca apartamento")
                .tasaPreferencial(true)
                .build();
        System.out.println("Creada: " + vivienda);

        try {
            SolicitudCredito.builder("Eva Ruiz", "52111222", new BigDecimal("60000000"), 36).build();
        } catch (IllegalStateException e) {
            System.out.println("Rechazada: " + e.getMessage());
        }
    }
}
