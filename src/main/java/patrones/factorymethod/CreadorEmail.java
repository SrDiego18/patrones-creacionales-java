package patrones.factorymethod;

/** Creador concreto: decide que el producto es {@link NotificacionEmail}. */
public class CreadorEmail extends CreadorNotificacion {

    @Override
    protected Notificacion crearNotificacion() {
        return new NotificacionEmail();
    }
}
