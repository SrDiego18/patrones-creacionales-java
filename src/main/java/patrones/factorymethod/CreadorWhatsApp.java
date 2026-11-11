package patrones.factorymethod;

/** Creador concreto: decide que el producto es {@link NotificacionWhatsApp}. */
public class CreadorWhatsApp extends CreadorNotificacion {

    @Override
    protected Notificacion crearNotificacion() {
        return new NotificacionWhatsApp();
    }
}
