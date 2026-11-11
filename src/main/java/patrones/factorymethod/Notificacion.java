package patrones.factorymethod;

/**
 * Producto del patrón Factory Method: abstracción común para cualquier canal.
 * El resto del código trabaja solo con este tipo.
 */
public interface Notificacion {

    /**
     * Envía el mensaje por el canal concreto.
     *
     * @param mensaje texto a enviar (ya validado por el creador)
     * @return el texto efectivamente enviado, con el prefijo del canal
     */
    String enviar(String mensaje);
}
