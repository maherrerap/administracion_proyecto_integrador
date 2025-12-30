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

    public String getIdCompra() { return idCompra; }
    public String getIdProducto() { return idProducto; }
    public int getPxoCantidad() { return pxoCantidad; }
    public double getPxoValor() { return pxoValor; }
    public double getPxoSubtotal() { return pxoSubtotal; }
    public String getEstadoPxo() { return estadoPxo; }

    // ===================== SETTERS =====================

    public void setIdCompra(String idCompra) { this.idCompra = idCompra; }
    public void setIdProducto(String idProducto) { this.idProducto = idProducto; }
    public void setPxoCantidad(int pxoCantidad) { this.pxoCantidad = pxoCantidad; }
    public void setPxoValor(double pxoValor) { this.pxoValor = pxoValor; }
    public void setPxoSubtotal(double pxoSubtotal) { this.pxoSubtotal = pxoSubtotal; }
    public void setEstadoPxo(String estadoPxo) { this.estadoPxo = estadoPxo; }

    // ===================== REGLAS DE NEGOCIO =====================

    public static double calcularSubtotal(int cantidad, double valor) {
        return cantidad * valor;
    }

    public void recalcularSubtotal() {
        this.pxoSubtotal = calcularSubtotal(this.pxoCantidad, this.pxoValor);
    }

    public static void verificarPxo(String idCompra, String idProducto, int cantidad) {
        if (idCompra == null || idCompra.trim().isEmpty())
            throw new IllegalArgumentException("El identificador de la orden de compra es obligatorio.");
        if (idProducto == null || idProducto.trim().isEmpty())
            throw new IllegalArgumentException("El identificador del producto es obligatorio.");
        if (cantidad <= 0)
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");
    }

    // ===================== MÉTODOS DP → MD =====================

    public static List<Pro_x_Oc> obtenerDetallesCompra(String idCompra) throws Exception {
        return Pro_x_OcMD.obtenerListadoDetallesCompra(idCompra);
    }

    public static Pro_x_Oc obtenerDetalle(String idCompra, String idProducto) throws Exception {
        verificarPxo(idCompra, idProducto, 1);
        return Pro_x_OcMD.obtenerDetalle(idCompra, idProducto);
    }

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
            nuevo.setEstadoPxo("ACT");

            return Pro_x_OcMD.registrarDetalle(nuevo);
        } else {
            int nuevaCantidad = existente.getPxoCantidad() + cantidad;
            return modificarPxo(idCompra, idProducto, nuevaCantidad);
        }
    }

    public static boolean incrementarCantidad(String idCompra, String idProducto, int paso) throws Exception {
        if (paso <= 0) paso = 1;

        Pro_x_Oc detalle = obtenerDetalle(idCompra, idProducto);
        if (detalle == null)
            throw new IllegalArgumentException("No existe el producto en el detalle.");

        int nuevaCantidad = detalle.getPxoCantidad() + paso;
        return modificarPxo(idCompra, idProducto, nuevaCantidad);
    }

    public static boolean decrementarCantidad(String idCompra, String idProducto, int paso) throws Exception {
        if (paso <= 0) paso = 1;

        Pro_x_Oc detalle = obtenerDetalle(idCompra, idProducto);
        if (detalle == null)
            throw new IllegalArgumentException("No existe el producto en el detalle.");

        int nuevaCantidad = detalle.getPxoCantidad() - paso;

        if (nuevaCantidad <= 0)
            return eliminarPxo(idCompra, idProducto);

        return modificarPxo(idCompra, idProducto, nuevaCantidad);
    }

    public static boolean modificarPxo(String idCompra, String idProducto, int nuevaCantidad) throws Exception {
        verificarPxo(idCompra, idProducto, nuevaCantidad);

        double valor = Pro_x_OcMD.obtenerValorDetalle(idCompra, idProducto);
        double nuevoSubtotal = calcularSubtotal(nuevaCantidad, valor);

        return Pro_x_OcMD.actualizarCantidadYSubtotal(
                idCompra, idProducto, nuevaCantidad, nuevoSubtotal);
    }

    public static boolean eliminarPxo(String idCompra, String idProducto) throws Exception {
        verificarPxo(idCompra, idProducto, 1);
        return Pro_x_OcMD.inhabilitarDetalle(idCompra, idProducto);
    }
}
