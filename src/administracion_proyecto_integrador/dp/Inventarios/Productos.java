
package administracion_proyecto_integrador.dp.Inventarios;

import java.util.List;
import java.util.ArrayList;

import administracion_proyecto_integrador.md.Inventarios.ProductoMD;

public class Productos {
    
    // Atributos de la clase Productos
    
    private String idProducto;
    private String proDescripcion;
    private String proUmCompra;
    private String proUmVenta;
    private String proUmVentaDescripcion;
    private double proValorCompra;
    private double proPrecioVenta;
    private int proSaldoInicial;
    private int proQtyIngresos;
    private int proQtyEgresos;
    private int proQtyAjustes;
    private int proSaldoFinal;
    private String estadoProd;
    private String idcategoria;
    private String proImagen;
    
    // Constructores
    
    // Constructor Vacio
    public Productos () {}
    
    // Constructor Completo

    public Productos(String idProducto, String proDescripcion, String proUmCompra, String proUmVenta, double proValorCompra, double proPrecioVenta, int proSaldoInicial, int proQtyIngresos, int proQtyEgresos, int proQtyAjustes, int proSaldoFinal, String estadoProd, String idcategoria, String proImagen) {
        this.idProducto = idProducto;
        this.proDescripcion = proDescripcion;
        this.proUmCompra = proUmCompra;
        this.proUmVenta = proUmVenta;
        this.proValorCompra = proValorCompra;
        this.proPrecioVenta = proPrecioVenta;
        this.proSaldoInicial = proSaldoInicial;
        this.proQtyIngresos = proQtyIngresos;
        this.proQtyEgresos = proQtyEgresos;
        this.proQtyAjustes = proQtyAjustes;
        this.proSaldoFinal = proSaldoFinal;
        this.estadoProd = estadoProd;
        this.idcategoria = idcategoria;
        this.proImagen = proImagen;
    }
    
    
    // Getters

    public String getIdProducto() {
        return idProducto;
    }

    public String getProDescripcion() {
        return proDescripcion;
    }

    public String getProUmCompra() {
        return proUmCompra;
    }

    public String getProUmVenta() {
        return proUmVenta;
    }
    
    public String getProUmVentaDescripcion() {
        return proUmVentaDescripcion;
    }

    public double getProValorCompra() {
        return proValorCompra;
    }

    public double getProPrecioVenta() {
        return proPrecioVenta;
    }

    public int getProSaldoInicial() {
        return proSaldoInicial;
    }

    public int getProQtyIngresos() {
        return proQtyIngresos;
    }

    public int getProQtyEgresos() {
        return proQtyEgresos;
    }

    public int getProQtyAjustes() {
        return proQtyAjustes;
    }

    public int getProSaldoFinal() {
        return proSaldoFinal;
    }

    public String getEstadoProd() {
        return estadoProd;
    }

    public String getIdcategoria() {
        return idcategoria;
    }

    public String getProImagen() {
        return proImagen;
    }
    
    // Setters

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public void setProDescripcion(String proDescripcion) {
        this.proDescripcion = proDescripcion;
    }

    public void setProUmCompra(String proUmCompra) {
        this.proUmCompra = proUmCompra;
    }

    public void setProUmVenta(String proUmVenta) {
        this.proUmVenta = proUmVenta;
    }

    public void setProUmVentaDescripcion(String proUmVentaDescripcion) {
        this.proUmVentaDescripcion = proUmVentaDescripcion;
    }

    public void setProValorCompra(double proValorCompra) {
        this.proValorCompra = proValorCompra;
    }

    public void setProPrecioVenta(double proPrecioVenta) {
        this.proPrecioVenta = proPrecioVenta;
    }

    public void setProSaldoInicial(int proSaldoInicial) {
        this.proSaldoInicial = proSaldoInicial;
    }

    public void setProQtyIngresos(int proQtyIngresos) {
        this.proQtyIngresos = proQtyIngresos;
    }

    public void setProQtyEgresos(int proQtyEgresos) {
        this.proQtyEgresos = proQtyEgresos;
    }

    public void setProQtyAjustes(int proQtyAjustes) {
        this.proQtyAjustes = proQtyAjustes;
    }

    public void setProSaldoFinal(int proSaldoFinal) {
        this.proSaldoFinal = proSaldoFinal;
    }

    public void setEstadoProd(String estadoProd) {
        this.estadoProd = estadoProd;
    }

    public void setIdcategoria(String id_categoria) {
        this.idcategoria = id_categoria;
    }

    public void setProImagen(String proImagen) {
        this.proImagen = proImagen;
    }
    
    // Métodos de Conexión con DB
    
    /**
     * RF4.4.1: Consulta General de Productos
     * Obtiene el listado completo de productos desde el MD
     */
    
    public static List<Productos> obtenerProductos() throws Exception {
        return ProductoMD.obtenerListadoProductos();
    }
    
    /**
     * Obtener un producto específico por su ID
     */
    public static Productos obtenerProductoPorId(String idProducto) throws Exception {
        return ProductoMD.obtenerProductoPorId(idProducto);
    }   
 
    /**
     * RF6.1: Creación Producto
     * Recibe un objeto Productos con los datos cargados desde la GUI
     * y lo envia a la capa MD para que se inserte en la BD.
     */
    
    public static boolean grabarProducto(Productos producto) {
        ProductoMD productoMD = new ProductoMD();
        return productoMD.crearProducto(producto);
    }
    
    /**
     * RF4.2: Modificación Producto
     */
    
    public static boolean modificarProducto(Productos producto) {
        ProductoMD productoMD = new ProductoMD();
        return productoMD.modificarProducto(producto);
    }
    
    /**
     * RF4.3: Inhabilitación Producto
     */
    
    public static boolean eliminarProducto (String idProducto) {
        ProductoMD productoMD = new ProductoMD();
        return productoMD.eliminarProducto(idProducto);
    }

    /**
     * RF4.4.2: Consulta Por Parámetros de Productos
     */
    
    public static List<Productos> obtenerProductosPorParametro (
            String idProducto,
            String proDescripcion,
            String idCategoria) {
        ProductoMD productoMD = new ProductoMD();
        return productoMD.obtenerProductosPorParametro(idProducto, proDescripcion, idCategoria);
    }
    
// ===================== MÉTODOS INTERMEDIARIOS PARA GESTIÓN DE STOCK =====================
    
    /**
     * Actualizar stock cuando se realiza una venta
     * Incrementa los egresos y reduce el saldo final
     */
    public static boolean actualizarStockPorVenta(String idProducto, int cantidad) throws Exception {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        return ProductoMD.actualizarStockPorVenta(idProducto, cantidad);
    }
    
    /**
     * Revertir stock cuando se cancela o elimina una venta
     * Decrementa los egresos y aumenta el saldo final
     */
    public static boolean revertirStockPorVenta(String idProducto, int cantidad) throws Exception {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        return ProductoMD.revertirStockPorVenta(idProducto, cantidad);
    }
    
    /**
     * Ajustar stock cuando se modifica la cantidad de un detalle existente
     * Calcula la diferencia y actualiza o revierte según corresponda
     */
    public static boolean ajustarStockPorCambio(String idProducto, int cantidadAnterior, int cantidadNueva) throws Exception {
        if (cantidadAnterior < 0 || cantidadNueva < 0) {
            throw new IllegalArgumentException("Las cantidades no pueden ser negativas");
        }
        return ProductoMD.ajustarStockPorCambio(idProducto, cantidadAnterior, cantidadNueva);
    }
    
    // ===================== VALIDACIONES (verificarProd) =====================

    /** Representa un error de validación (Código + Mensaje) */
    public static class ErrorValidacion {
        private final String codigo;
        private final String mensaje;

        public ErrorValidacion(String codigo, String mensaje) {
            this.codigo = codigo;
            this.mensaje = mensaje;
        }

        public String getCodigo() { return codigo; }
        public String getMensaje() { return mensaje; }

        @Override
        public String toString() {
            return codigo + " - " + mensaje;
        }
    }

    /**
     * Verifica los datos del producto antes de guardarlo/modificarlo en BD.
     * Retorna lista de errores (vacía si todo está bien).
     */
    public List<ErrorValidacion> verificarProd() {
        List<ErrorValidacion> errores = new ArrayList<>();

        
        final int MAX_ID_PRODUCTO   = 7;
        final int MAX_DESC          = 50;
        final int MAX_UM_COMPRA     = 3;
        final int MAX_UM_VENTA      = 3;
        final int MAX_ESTADO        = 3;
        final int MAX_ID_CATEGORIA  = 3;
        final int MAX_IMAGEN        = 255;

        // Normalización (trim)
        String idProd     = norm(this.idProducto);
        String desc       = norm(this.proDescripcion);
        String umCompra   = norm(this.proUmCompra);
        String umVenta    = norm(this.proUmVenta);
        String estado     = norm(this.estadoProd);
        String idCat      = norm(this.idcategoria);
        String imagen     = norm(this.proImagen);

        // ---------------- V1: Campo obligatorio vacío ----------------
        if (isBlank(idProd))
            errores.add(err("V1", msgObligatorio("idProducto")));

        if (isBlank(desc))
            errores.add(err("V1", msgObligatorio("descripcion")));

        if (isBlank(umCompra))
            errores.add(err("V1", msgObligatorio("umCompra")));

        if (isBlank(umVenta))
            errores.add(err("V1", msgObligatorio("umVenta")));

        if (isBlank(idCat))
            errores.add(err("V1", msgObligatorio("categoria")));

        if (isBlank(estado))
            errores.add(err("V1", msgObligatorio("estado")));

        // ---------------- V2: Longitud máxima excedida ----------------
        if (!isBlank(idProd) && idProd.length() > MAX_ID_PRODUCTO)
            errores.add(err("V2", msgLongitud("idProducto")));

        if (!isBlank(desc) && desc.length() > MAX_DESC)
            errores.add(err("V2", msgLongitud("descripcion")));

        if (!isBlank(umCompra) && umCompra.length() > MAX_UM_COMPRA)
            errores.add(err("V2", msgLongitud("umCompra")));

        if (!isBlank(umVenta) && umVenta.length() > MAX_UM_VENTA)
            errores.add(err("V2", msgLongitud("umVenta")));

        if (!isBlank(idCat) && idCat.length() > MAX_ID_CATEGORIA)
            errores.add(err("V2", msgLongitud("categoria")));

        if (!isBlank(estado) && estado.length() > MAX_ESTADO)
            errores.add(err("V2", msgLongitud("estado")));

        if (!isBlank(imagen) && imagen.length() > MAX_IMAGEN)
            errores.add(err("V2", msgLongitud("imagen")));

        // ---------------- V3: Formato inválido (general) ----------------
        if (!isBlank(idProd) && !idProd.matches("^[A-Za-z0-9_-]+$")) {
            errores.add(err("V3", "El formato del campo idProducto no es válido."));
        }

        // ---------------- V8: Valor numérico fuera de rango (precio/cantidad/stock) ----------------
        if (this.proValorCompra < 0 || this.proValorCompra > 1_000_000) {
            errores.add(err("V8", "El valor ingresado está fuera del rango permitido."));
        }

        if (this.proPrecioVenta < 0 || this.proPrecioVenta > 1_000_000) {
            errores.add(err("V8", "El valor ingresado está fuera del rango permitido."));
        }

        if (this.proSaldoInicial < 0 || this.proSaldoInicial > 1_000_000) {
            errores.add(err("V8", "El valor ingresado está fuera del rango permitido."));
        }

        // ---------------- V9: Cantidad menor o igual a cero ----------------
        if (this.proQtyIngresos <= 0 && this.proQtyIngresos != 0) { /* no pasa nunca */ }

        // ---------------- V10: Precio menor o igual a cero ----------------
        if (this.proValorCompra <= 0) {
            errores.add(err("V10", "El precio debe ser mayor a 0."));
        }
        if (this.proPrecioVenta <= 0) {
            errores.add(err("V10", "El precio debe ser mayor a 0."));
        }

        // ---------------- V12: Estado inválido para operación (ACT/INA) ----------------
        if (!isBlank(estado) && !(estado.equalsIgnoreCase("ACT") || estado.equalsIgnoreCase("INA"))) {
            errores.add(err("V12", "No se puede realizar la acción: el registro está inactivo."));
        }

        return errores;
    }

    // ===================== HELPERS =====================

    private static ErrorValidacion err(String codigo, String mensaje) {
        return new ErrorValidacion(codigo, mensaje);
    }

    private static String msgObligatorio(String nombreCampo) {
        return "El campo " + nombreCampo + " es obligatorio.";
    }

    private static String msgLongitud(String nombreCampo) {
        return "El campo " + nombreCampo + " excede la longitud permitida.";
    }

    private static String norm(String s) {
        return (s == null) ? null : s.trim();
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
