
package administracion_proyecto_integrador.dp.Facturacion;


import java.util.List;

import administracion_proyecto_integrador.md.Facturacion.Pro_x_FacMD;

public class Pro_x_Fac {
    
    // Atributos de la clase Pro_x_Fac
    
    private String idFactura;
    private String idProducto;
    private int pxfCantidad;
    private double pxfPrecio;
    private double pxfSubtotal;
    private String estadoPxf;
    
    
    // Constructores
    
    // Constructor Vacio
    public Pro_x_Fac () {}
    
    // Constructor completo

    public Pro_x_Fac(String idFactura, String idProducto, int pxfCantidad, double pxfPrecio, double pxfSubtotal, String estadoPxf) {
        this.idFactura = idFactura;
        this.idProducto = idProducto;
        this.pxfCantidad = pxfCantidad;
        this.pxfPrecio = pxfPrecio;
        this.pxfSubtotal = pxfSubtotal;
        this.estadoPxf = estadoPxf;
    }
    
    // Getters

    public String getIdFactura() {
        return idFactura;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public int getPxfCantidad() {
        return pxfCantidad;
    }

    public double getPxfPrecio() {
        return pxfPrecio;
    }

    public double getPxfSubtotal() {
        return pxfSubtotal;
    }

    public String getEstadoPxf() {
        return estadoPxf;
    }
    
    // Setters

    public void setIdFactura(String idFactura) {
        this.idFactura = idFactura;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public void setPxfCantidad(int pxfCantidad) {
        this.pxfCantidad = pxfCantidad;
    }

    public void setPxfPrecio(double pxfPrecio) {
        this.pxfPrecio = pxfPrecio;
    }

    public void setPxfSubtotal(double pxfSubtotal) {
        this.pxfSubtotal = pxfSubtotal;
    }

    public void setEstadoPxf(String estadoPxf) {
        this.estadoPxf = estadoPxf;
    }
    
    
    // Métodos para reglas de negocio:
    
    // 1. Calcular el Subtotal
    
    public static double calcularSubtotal (int cantidad, double precio) {
        return cantidad * precio;
    }
    
    public void recalcularSubtotal() {
        this.pxfSubtotal = calcularSubtotal(this.pxfCantidad, this.pxfPrecio);
    }
    
    
    // 2. Validar Detalle
    
    public static void verificarPxf(String idFactura, String idProducto, int cantidad) {
        if (idFactura == null || idFactura.trim().isEmpty())
            throw new IllegalArgumentException("El identificador es obligatorio.");
        if(idProducto == null || idProducto.trim().isEmpty())
            throw new IllegalArgumentException("El identificador es obligatorio.");
        if (cantidad <= 0)
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");
    }
    
    // Metodos de conexion con la BD
    
    /**
     * Cargar tabla: Detalle de productos de una factura
     */
    
    public static List<Pro_x_Fac> obtenerDetallesFactura(String idFactura) throws Exception {
        return Pro_x_FacMD.obtenerListadoDetallesFactura(idFactura);
    }

    /**
     * Obtener nombre del producto
     */

    public static String obtenerNombreProducto(String idProducto) throws Exception {
        return Pro_x_FacMD.obtenerNombreProducto(idProducto);
    }
    /**
     * Buscar un producto especifico en el detalle
     */
    
    public static Pro_x_Fac obtenerDetalle(String idFactura, String idProducto) throws Exception {
        verificarPxf(idFactura, idProducto, 1);
        return Pro_x_FacMD.obtenerDetalle(idFactura, idProducto);
    }
    
    /**
     * Registrar Producto en el detalle
     * Regla a tomar en cuenta: Si ya existe el producto en la factura, 
     * se incrementa la cantidad
     */
    
    public static boolean agregarProducto(String idFactura, String idProducto, int cantidad) throws Exception {
        verificarPxf(idFactura, idProducto, cantidad);
        
        Pro_x_Fac existente = obtenerDetalle(idFactura, idProducto);
        
        if (existente == null) {
            double precioActual = Pro_x_FacMD.obtenerPrecioVentaProducto(idProducto);
            
            Pro_x_Fac nuevo = new Pro_x_Fac();
            nuevo.setIdFactura(idFactura);
            nuevo.setIdProducto(idProducto);
            nuevo.setPxfCantidad(cantidad);
            nuevo.setPxfPrecio(precioActual);
            nuevo.recalcularSubtotal();
            nuevo.setEstadoPxf("APR");  // Por regla de negocio se generan facturas en estado aprobado
            
            return Pro_x_FacMD.registrarDetalle(nuevo);
        } else {
            int nuevaCantidad = existente.getPxfCantidad() + cantidad;
            return modificarPxf(idFactura, idProducto, nuevaCantidad);
        }
    }
    
    /**
     * Aumentar cantidad de producto en el detalle con el boton "+"
     */
    
    public static boolean incrementarCantidad(String idFactura, String idProducto, int paso) throws Exception {
        if (paso <= 0)
            paso = 1;
        
        Pro_x_Fac detalle = obtenerDetalle(idFactura, idProducto);
        
        if (detalle == null)
            throw new IllegalArgumentException("No existe el producto en el detalle.");
        
        int nuevaCantidad= detalle.getPxfCantidad() + paso;
        return modificarPxf (idFactura, idProducto, nuevaCantidad);
    }
    
    /**
     * Disminuir cantidad de producto en el detalle con el boton "-"
     */
    
    public static boolean decrementarCantidad(String idFactura, String idProducto, int paso) throws Exception {
        if (paso <= 0)
            paso = 1;
        
        Pro_x_Fac detalle = obtenerDetalle(idFactura, idProducto);
        
        if (detalle == null)
            throw new IllegalArgumentException("No existe el producto en el detalle.");
        
        int nuevaCantidad= detalle.getPxfCantidad() - paso;
        
        
        if (nuevaCantidad <= 0) {
            return eliminarPxf(idFactura, idProducto);
        }
        
        return modificarPxf(idFactura, idProducto, nuevaCantidad);
    }

    /**
     * Actualizar la cantidad y recalcular el subtotal del producto
     */
    
    public static boolean modificarPxf(String idFactura, String idProducto, int nuevaCantidad) throws Exception {
        verificarPxf(idFactura, idProducto, nuevaCantidad);
        
        double precio = Pro_x_FacMD.obtenerPrecioDetalle(idFactura, idProducto);
        double nuevoSubtotal = calcularSubtotal(nuevaCantidad, precio);
        
        return Pro_x_FacMD.actualizarCantidadYSubtotal(idFactura, idProducto, nuevaCantidad, nuevoSubtotal);
    }
    
    /**
     * Eliminar Producto de Detalle
     * Como se trata del detalle, aqui si se realiza un borrado fisico del mismo.
     */
    public static boolean eliminarPxf(String idFactura, String idProducto) throws Exception {
        verificarPxf(idFactura, idProducto, 1);
        return Pro_x_FacMD.eliminarDetalle(idFactura, idProducto);
    }    
}
