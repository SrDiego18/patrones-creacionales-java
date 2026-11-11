package patrones.abstractfactory;

import java.util.Locale;

/** Temas visuales disponibles. */
public enum Tema {
    INSTITUCIONAL, ALTO_CONTRASTE;

    public static Tema desdeTexto(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("El tema es obligatorio");
        }
        try {
            return valueOf(texto.strip().toUpperCase(Locale.ROOT).replace('-', '_').replace(' ', '_'));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tema desconocido: " + texto, e);
        }
    }
}
