package patrones.abstractfactory;

import java.util.List;

/** Producto abstracto: menú. */
public interface Menu extends Componente {
    String renderizar(List<String> opciones);
}
