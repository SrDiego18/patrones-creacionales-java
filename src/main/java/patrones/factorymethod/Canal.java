package patrones.factorymethod;

import java.util.Locale;

/** Canales disponibles. Reemplaza las "cadenas mágicas" del código original. */
public enum Canal {
    EMAIL, SMS, WHATSAPP, PUSH;

    /**
     * Convierte un texto (por ejemplo, leído de configuración) en un canal.
     *
     * @throws IllegalArgumentException si el texto no corresponde a ningún canal
     */
    public static Canal desdeTexto(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("El canal es obligatorio");
        }
        try {
            return valueOf(texto.strip().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Canal no soportado: " + texto, e);
        }
    }
}
