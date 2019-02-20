/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventarioventas.Modelo;

/**
 *
 * @author JuanCaCha
 */
public class Prestamo {
    private String nombre;
    private String suproducto;
    private String Miproducto;
    private String fechacambio;
    private int saldopendiente;
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSuproducto() {
        return suproducto;
    }

    public void setSuproducto(String suproducto) {
        this.suproducto = suproducto;
    }

    public String getMiproducto() {
        return Miproducto;
    }

    public void setMiproducto(String Miproducto) {
        this.Miproducto = Miproducto;
    }

    public String getFechacambio() {
        return fechacambio;
    }

    public void setFechacambio(String fechacambio) {
        this.fechacambio = fechacambio;
    }

    public int getSaldopendiente() {
        return saldopendiente;
    }

    public void setSaldopendiente(int saldopendiente) {
        this.saldopendiente = saldopendiente;
    }
    
    
}
