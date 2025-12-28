package administracion_proyecto_integrador.dp.Compras;

import java.util.List;
import administracion_proyecto_integrador.md.Compras.Pro_x_OcMD;

public class Pro_x_Oc {

    // ===================== ATRIBUTOS =====================

    private String idCompra;
    private String idProducto;
    private int pxoCantidad;
    private double pxoValor;
    private double pxoSubtotal;
    private String estadoPxo;

    // ===================== CONSTRUCTORES =====================

    public Pro_x_Oc() {}

    public Pro_x_Oc(String idCompra, String idProducto, int pxoCantidad,
                    double pxoValor, double pxoSubtotal, String estadoPxo) {
        this.idCompra = idCompra;
        this.idProducto = idProducto;
        this.pxoCantidad = pxoCantidad;
        this.pxoValor = pxoValor;
        this.pxoSubtotal = pxoSubtotal;
        this.estadoPxo = estadoPxo;
    }

    // ===================== GETTERS =====================

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

    public String getEstadoPxo() {
        return estadoPxo;
    }

    // ===================== SETTERS =====================

    public void setIdCompra(String idCompra) {
        this.idCompra = idCompra;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public void setPxoCantidad(int pxoCantidad) {
        this.pxoCantidad = pxoCantidad;
    }

    public void setPxoValor(double pxoValor) {
        this.pxoValor = pxoValor;
    }

    public void setPxoSubtotal(double pxoSubtotal) {
        this.pxoSubtotal = pxoSubtotal;
    }

    public void setEstadoPxo(String estadoPxo) {
        this.estadoPxo = estadoPxo;
    }

    // ===================== REGLAS DE NEGOCIO =====================

    /**
     * Calcula el subtotal del detalle
     */
    public static double calcularSubtotal(int cantidad, double valor) {
        return cantidad * valor;
    }

    /**
     * Recalcula el subtotal del objeto actual
     */
    public void recalcularSubtotal() {
        this.pxoSubtotal = calcularSubtotal(this.pxoCantidad, this.pxoValor);
    }

    /**
     * Validaciones mínimas del detalle
     */
    public static void verificarPxo(String idCompra, String idProducto, int cantidad) {
        if (idCompra == null || idCompra.trim().isEmpty())
            throw new IllegalArgumentException("El identificador de la orden de compra es obligatorio.");

        if (idProducto == null || idProducto.trim().isEmpty())
            throw new IllegalArgumentException("El identificador del producto es obligatorio.");

        if (cantidad <= 0)
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");
    }

    // ===================== CONEXIÓN CON MD =====================

    /**
     * Obtiene todos los detalles activos de una orden de compra
     */
    public static List<Pro_x_Oc> obtenerDetallesCompra(String idCompra) throws Exception {
        return Pro_x_OcMD.obtenerListadoDetallesCompra(idCompra);
    }

    /**
     * Obtiene un detalle específico de la orden
     */
    public static Pro_x_Oc obtenerDetalle(String idCompra, String idProducto) throws Exception {
        verificarPxo(idCompra, idProducto, 1);
        return Pro_x_OcMD.obtenerDetalle(idCompra, idProducto);
    }

    /**
     * Agrega un producto al detalle de la orden de compra.
     * Si el producto ya existe, se incrementa la cantidad.
     */
    public static boolean agregarProducto(String idCompra, String idProducto, int cantidad) throws Exception {
        verificarPxo(idCompra, idProducto, cantidad);

        Pro_x_Oc existente = obtenerDetalle(idCompra, idProducto);

        if (existente == null) {
            double valorCompra = Pro_x_OcMD.obtenerValorCompraProducto(idProducto);

            Pro_x_Oc nuevo = new Pro_x_Oc();
            nuevo.setIdCompra(idCompra);
            nuevo.setIdProducto(idProducto);
            nuevo.setPxoCantidad(cantidad);
            nuevo.setPxoValor(valorCompra);
            nuevo.recalcularSubtotal();
            nuevo.setEstadoPxo("ACT"); // Estado inicial activo (borrado lógico)

            return Pro_x_OcMD.registrarDetalle(nuevo);
        } else {
            int nuevaCantidad = existente.getPxoCantidad() + cantidad;
            return modificarPxo(idCompra, idProducto, nuevaCantidad);
        }
    }

    /**
     * Modifica la cantidad del producto y recalcula el subtotal
     */
    public static boolean modificarPxo(String idCompra, String idProducto, int nuevaCantidad) throws Exception {
        verificarPxo(idCompra, idProducto, nuevaCantidad);

        double valor = Pro_x_OcMD.obtenerValorDetalle(idCompra, idProducto);
        double nuevoSubtotal = calcularSubtotal(nuevaCantidad, valor);

        return Pro_x_OcMD.actualizarCantidadYSubtotal(
                idCompra, idProducto, nuevaCantidad, nuevoSubtotal);
    }

    /**
     * Inhabilita lógicamente un producto del detalle (borrado lógico)
     */
    public static boolean eliminarPxo(String idCompra, String idProducto) throws Exception {
        verificarPxo(idCompra, idProducto, 1);
        return Pro_x_OcMD.inhabilitarDetalle(idCompra, idProducto);
    }
}