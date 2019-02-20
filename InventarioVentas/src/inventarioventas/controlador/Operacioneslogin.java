/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventarioventas.controlador;

/**
 *
 * @author JuanCaCha
 */
public class Operacioneslogin {
    ConexionBD conexion = new ConexionBD();
    
    public boolean mismaclave(String c){
        return conexion.abrirclave().equals(c);
    }
    public void setClave (String c){
        conexion.cambiarclave(c);
    }
}
