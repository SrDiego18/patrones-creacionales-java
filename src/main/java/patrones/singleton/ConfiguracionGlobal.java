package patrones.singleton;

/**
 * Alternativa evaluada: Singleton con {@code enum} (forma recomendada por
 * Bloch, Item 3: es thread-safe, resiste serialización y reflexión).
 *
 * <p>Se incluye para comparar. Su desventaja es que las clases que llaman a
 * {@code ConfiguracionGlobal.INSTANCIA} dependen de un estado global oculto y
 * no se les puede pasar otra configuración en las pruebas. Por eso la
 * solución elegida es {@link Configuracion} inyectada (ver {@link ServicioReportes}).</p>
 */
public enum ConfiguracionGlobal {
    INSTANCIA;

    private final Configuracion configuracion = Configuracion.porDefecto();

    public Configuracion get() {
        return configuracion;
    }
}
