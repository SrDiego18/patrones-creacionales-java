package patrones.prototype;

/**
 * Interfaz del patrón Prototype. Se usa en lugar de {@code Cloneable}/{@code clone()},
 * que en Java es frágil (Bloch, Effective Java, Item 13).
 *
 * @param <T> tipo que se copia
 */
public interface Prototipo<T> {

    /** Devuelve una copia profunda e independiente del objeto. */
    T copiar();
}
