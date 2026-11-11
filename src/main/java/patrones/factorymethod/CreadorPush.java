package patrones.factorymethod;

/** Creador concreto: decide que el producto es {@link NotificacionPush}. */
public class CreadorPush extends CreadorNotificacion {

    @Override
    protected Notificacion crearNotificacion() {
        return new NotificacionPush();
    }
}
