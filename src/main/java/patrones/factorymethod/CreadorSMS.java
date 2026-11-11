package patrones.factorymethod;

/** Creador concreto: decide que el producto es {@link NotificacionSMS}. */
public class CreadorSMS extends CreadorNotificacion {

    @Override
    protected Notificacion crearNotificacion() {
        return new NotificacionSMS();
    }
}
