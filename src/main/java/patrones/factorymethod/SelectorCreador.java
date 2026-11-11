package patrones.factorymethod;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Punto único donde se asocia cada {@link Canal} con su creador concreto.
 *
 * <p>Resuelve la observación de la guía: "el código cliente aún selecciona el
 * creador concreto". Con este registro, el cliente solo pide un canal y nunca
 * menciona clases concretas. Agregar un canal = una línea en
 * {@link #porDefecto()} o una llamada a {@link #registrar}.</p>
 */
public final class SelectorCreador {

    private final Map<Canal, Supplier<CreadorNotificacion>> creadores = new EnumMap<>(Canal.class);

    /** Selector con todos los canales conocidos ya registrados. */
    public static SelectorCreador porDefecto() {
        return new SelectorCreador()
                .registrar(Canal.EMAIL, CreadorEmail::new)
                .registrar(Canal.SMS, CreadorSMS::new)
                .registrar(Canal.WHATSAPP, CreadorWhatsApp::new)
                .registrar(Canal.PUSH, CreadorPush::new);
    }

    /** Registra (o reemplaza) el creador de un canal. */
    public SelectorCreador registrar(Canal canal, Supplier<CreadorNotificacion> fabrica) {
        if (canal == null || fabrica == null) {
            throw new IllegalArgumentException("Canal y fábrica son obligatorios");
        }
        creadores.put(canal, fabrica);
        return this;
    }

    /**
     * Devuelve un creador nuevo para el canal pedido.
     *
     * @throws IllegalArgumentException si el canal no está registrado
     */
    public CreadorNotificacion para(Canal canal) {
        Supplier<CreadorNotificacion> fabrica = creadores.get(canal);
        if (fabrica == null) {
            throw new IllegalArgumentException("No hay creador registrado para " + canal);
        }
        return fabrica.get();
    }

    public Set<Canal> canalesDisponibles() {
        return Collections.unmodifiableSet(creadores.keySet());
    }
}
