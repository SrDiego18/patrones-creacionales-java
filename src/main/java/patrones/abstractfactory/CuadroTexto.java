package patrones.abstractfactory;

/** Producto abstracto: cuadro de texto. */
public interface CuadroTexto extends Componente {
    String renderizar(String valor);
}
