package patrones.abstractfactory;

/** Producto abstracto: botón. */
public interface Boton extends Componente {
    String renderizar(String texto);
}
