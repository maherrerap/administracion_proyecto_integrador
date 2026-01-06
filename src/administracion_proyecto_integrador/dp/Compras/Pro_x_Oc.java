package administracion_proyecto_integrador.dp.Compras;

import administracion_proyecto_integrador.md.Compras.Pro_x_OcMD;
import java.util.List;

public class Pro_x_Oc {

    // Atributos de clase Pro_X_Oc
    private String idCompra;
    private String idProducto;
    private int pxoCantidad;
    private double pxoValor;
    private double pxoSubtotal;
    private String estadoPxoc;

    // CONSTRUCTORES
    
    // Constructor Vacío
    public Pro_x_Oc() {}
    
    // Constructor Completo
    public Pro_x_Oc(String idCompra, String idProducto, int pxoCantidad,
                    double pxoValor, double pxoSubtotal, String estadoPxoc) {
        this.idCompra = idCompra;
        this.idProducto = idProducto;
        this.pxoCantidad = pxoCantidad;
        this.pxoValor = pxoValor;
        this.pxoSubtotal = pxoSubtotal;
        this.estadoPxoc = estadoPxoc;
    }

    // GETTERS
    public String getIdCompra() {
        return idCompra;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public int getPxoCantidad() {
        return pxoCantidad;
    }

    public double getPxoValor() {
        return pxoValor;
    }

    public double getPxoSubtotal() {
        return pxoSubtotal;
    }

    public String getEstadoPxoc() {
        return estadoPxoc;
    }

    // SETTERS
    public void setIdCompra(String idCompra) {
        this.idCompra = idCompra;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public void setPxoCantidad(int pxoCantidad) {
        this.pxoCantidad = pxoCantidad;
    }

    public void setPxOcCantidad(int pxoCantidad) {
        this.pxoCantidad = pxoCantidad;
    }

    public void setPxoValor(double pxoValor) {
        this.pxoValor = pxoValor;
    }

    public void setPxOcValor(double pxoValor) {
        this.pxoValor = pxoValor;
    }

    public void setPxoSubtotal(double pxoSubtotal) {
        this.pxoSubtotal = pxoSubtotal;
    }

    public void setPxOcSubtotal(double pxoSubtotal) {
        this.pxoSubtotal = pxoSubtotal;
    }

    public void setEstadoPxoc(String estadoPxoc) {
        this.estadoPxoc = estadoPxoc;
    }

    // Métodos de Reglas de Negocio
    
    // 1. Calcular el Subtotal de la Orden de Compra.
    public static double calcularSubtotal(int cantidad, double valor) {
        return cantidad * valor;
    }

    public void recalcularSubtotal() {
        this.pxoSubtotal = calcularSubtotal(this.pxoCantidad, this.pxoValor);
    }

    // 2. Validar Detalle
    public static void verificarPxo(String idCompra, String idProducto, int cantidad) {
        if (idCompra == null || idCompra.trim().isEmpty())
            throw new IllegalArgumentException("El identificador es obligatorio.");

        if (idProducto == null || idProducto.trim().isEmpty())
            throw new IllegalArgumentException("El identificador es obligatorio.");

        if (cantidad <= 0)
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");
    }

    public static void verificarOc(String idCompra, String idProducto, int cantidad) {
        verificarPxo(idCompra, idProducto, cantidad);
    }

    // Métodos de Conexión con DB
    
    /**
     * Cargar tabla: Detalle de productos de una orden de compras
     */
    public static List<Pro_x_Oc> obtenerDetallesCompra(String idCompra) throws Exception {
        return Pro_x_OcMD.obtenerListadoDetallesCompra(idCompra);
    }

    /**
     * Obtención de un nombre del producto. 
     */
    public static String obtenerNombreProducto(String idProducto) throws Exception {
        return Pro_x_OcMD.obtenerNombreProducto(idProducto);
    }

    /**
     * Buscar un producto específico en el detalle
     */
    public static Pro_x_Oc obtenerDetalle(String idCompra, String idProducto) throws Exception {
        verificarPxo(idCompra, idProducto, 1);
        return Pro_x_OcMD.obtenerDetalle(idCompra, idProducto);
    }

    /**
     * Registr Producto en el detalle.
     * Regla a tomar en cuenta: Si ya existe el producto en la orden de compra,
     * se incrementa la cantidad
     */
    public static boolean agregarProducto(String idCompra, String idProducto, int cantidad) throws Exception {
        verificarPxo(idCompra, idProducto, cantidad);

        Pro_x_Oc existente = obtenerDetalle(idCompra, idProducto);

        if (existente == null) {
            double valorActual = Pro_x_OcMD.obtenerValorCompraProducto(idProducto);

            Pro_x_Oc nuevo = new Pro_x_Oc();
            nuevo.setIdCompra(idCompra);
            nuevo.setIdProducto(idProducto);
            nuevo.setPxoCantidad(cantidad);
            nuevo.setPxoValor(valorActual);
            nuevo.recalcularSubtotal();
            nuevo.setEstadoPxoc("ACT");

            return Pro_x_OcMD.registrarDetalle(nuevo);
        } else {
            int nuevaCantidad = existente.getPxoCantidad() + cantidad;
            return modificarPxo(idCompra, idProducto, nuevaCantidad);
        }
    }

    /**
     * Aumentar cantidad de producto en el detalle con el botón "+"
     */
    public static boolean incrementarCantidad(String idCompra, String idProducto, int paso) throws Exception {
        if (paso <= 0)
            paso = 1;

        Pro_x_Oc detalle = obtenerDetalle(idCompra, idProducto);

        if (detalle == null)
            throw new IllegalArgumentException("No existe el producto en el detalle.");

        int nuevaCantidad = detalle.getPxoCantidad() + paso;
        return modificarPxo(idCompra, idProducto, nuevaCantidad);
    }

    public static boolean incrementarCantidadOc(String idCompra, String idProducto) throws Exception {
        return incrementarCantidad(idCompra, idProducto, 1);
    }

    /**
     * Disminuir cantidad de producto en el detalle con el botón "-"
     */
    public static boolean decrementarCantidad(String idCompra, String idProducto, int paso) throws Exception {
        if (paso <= 0)
            paso = 1;

        Pro_x_Oc detalle = obtenerDetalle(idCompra, idProducto);

        if (detalle == null)
            throw new IllegalArgumentException("No existe el producto en el detalle.");

        int nuevaCantidad = detalle.getPxoCantidad() - paso;

        if (nuevaCantidad <= 0) {
            return eliminarPxo(idCompra, idProducto);
        }

        return modificarPxo(idCompra, idProducto, nuevaCantidad);
    }

    /**
     * Actualizar la cantidad y recalcular el subtotal del producto
     */
    public static boolean modificarPxo(String idCompra, String idProducto, int nuevaCantidad) throws Exception {
        verificarPxo(idCompra, idProducto, nuevaCantidad);

        double valor = Pro_x_OcMD.obtenerValorDetalle(idCompra, idProducto);
        double nuevoSubtotal = calcularSubtotal(nuevaCantidad, valor);

        return Pro_x_OcMD.actualizarCantidadYSubtotal(idCompra, idProducto, nuevaCantidad, nuevoSubtotal);
    }

    /**
     * Eliminar Producto del Detalle
     * Como se trata del detalle, aquí se realiza un borrado físico del mismo.
     */
    public static boolean eliminarPxo(String idCompra, String idProducto) throws Exception {
        verificarPxo(idCompra, idProducto, 1);
        return Pro_x_OcMD.eliminarDetalle(idCompra, idProducto);
    }

    /**
     * Actualizar solo la cantidad de un detalle 
     */
    public static boolean actualizarCantidadDetalle(String idCompra, String idProducto, int nuevaCantidad) throws Exception {
        verificarPxo(idCompra, idProducto, nuevaCantidad);
        return Pro_x_OcMD.actualizarCantidadDetalle(idCompra, idProducto, nuevaCantidad);
    }
}