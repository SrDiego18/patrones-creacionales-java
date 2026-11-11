package patrones.factorymethod;

/**
 * Creador del patrón Factory Method (GoF).
 *
 * <p>La lógica de envío es común y está cerrada a modificaciones: el método
 * {@link #enviarNotificacion(String)} es {@code final}. Cada subclase solo
 * redefine el <em>factory method</em> {@link #crearNotificacion()} para decidir
 * qué producto concreto se crea.</p>
 */
public abstract class CreadorNotificacion {

    /**
     * Operación del creador. No cambia al agregar canales nuevos.
     *
     * @param mensaje texto a enviar; no puede ser nulo ni vacío
     * @return el texto enviado por el canal
     * @throws IllegalArgumentException si el mensaje es nulo o vacío
     */
    public final String enviarNotificacion(String mensaje) {
        if (mensaje == null || mensaje.isBlank()) {
            throw new IllegalArgumentException("El mensaje no puede estar vacío");
        }
        Notificacion notificacion = crearNotificacion();
        return notificacion.enviar(mensaje.strip());
    }

    /** Factory method: cada subclase decide el producto concreto. */
    protected abstract Notificacion crearNotificacion();
}
