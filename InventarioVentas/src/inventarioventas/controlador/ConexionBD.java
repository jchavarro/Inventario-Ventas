/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventarioventas.controlador;

import inventarioventas.Modelo.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author JuanCaCha
 */
public class ConexionBD {
    
        File a = new File ("asd.txt");
    
    public void cambiarclave(String clave){
    
        try{
            
            FileOutputStream fos=new FileOutputStream(this.a);
            ObjectOutputStream oos=new ObjectOutputStream(fos);
            oos.writeObject(clave);
            
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    
    public String abrirclave (){
        
        String backup = "1";
        try{
            if (this.a.exists()) {
                FileInputStream fos=new FileInputStream(this.a);
                ObjectInputStream ois=new ObjectInputStream(fos);
                backup = (String) ois.readObject();
            }else{
                cambiarclave(backup);
            }
            
            
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return backup;
    }

    public void agragarCliente(Cliente c){
        
        Connection con;
        
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/bdinventarioventas", "root", "");
            Statement stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO cliente VALUES('"+c.getNombres()+"', '"+c.getTelefono()+"','"+c.getEmail()+"')");
        } catch (SQLException e) {
            System.out.println(e);
        }
    
    }
    
    public void agregarventa(Venta v){
        
        Connection con;
        
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/bdinventarioventas", "root", "");
            Statement stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO compra VALUES('"+v.getId()+"', '"+v.getProducto()+"','"
                    +v.getCantidadproductos()+"','"+v.getCliente()+"','"+v.getFecha()+"','"
                    +v.getTotalprecio()+"')");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    
    private void agregarprestamo(Prestamo p){
        Connection con;
        
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/bdinventarioventas", "root", "");
            Statement stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO empresario VALUES('"+p.getNombre()+"', '"+p.getSuproducto()+"','"
                    +p.getMiproducto()+"','"+p.getFechacambio()+"','"+p.getSaldopendiente()+"')");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    
    public void prestar (String nombre, String suproducto, String miproducto, String fecha){
        Prestamo p = new Prestamo();
        p.setNombre(nombre);
        p.setSuproducto(suproducto);
        p.setMiproducto(miproducto);
        p.setFechacambio(fecha);
        p.setSaldopendiente(sacarsaldo(suproducto, miproducto));
        agregarprestamo(p);
        Connection con;
        
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/bdinventarioventas", "root", "");
            Statement stmt = con.createStatement();
            stmt.executeUpdate("UPDATE productos SET Stock='"+(sacarstock(miproducto)-1)+"' WHERE Nombre = '"+miproducto+"'");
        } catch (SQLException e) {
            System.out.println(e);
        }
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/bdinventarioventas", "root", "");
            Statement stmt = con.createStatement();
            stmt.executeUpdate("UPDATE productos SET Stock='"+(sacarstock(suproducto)+1)+"' WHERE Nombre = '"+suproducto+"'");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    
    private int sacarstock (String producto){
        int stock = 0;
        Connection con;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/bdinventarioventas", "root", "");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Stock FROM productos WHERE Nombre = '"+producto+"'");
            rs.first();
            stock = Integer.parseInt(rs.getString(1));
        } catch (SQLException e) {
            System.out.println(e);
        }
        return stock;
    }
    private int sacarsaldo(String su, String mi){
        
        int suvalor = 0, mivalor = 0;
        Connection con;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/bdinventarioventas", "root", "");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Precio FROM productos WHERE Nombre = '"+su+"'");
            rs.first();
            suvalor = Integer.parseInt(rs.getString(1));
        } catch (SQLException e) {
            System.out.println(e);
        }
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/bdinventarioventas", "root", "");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Precio FROM productos WHERE Nombre = '"+mi+"'");
            rs.first();
            mivalor = Integer.parseInt(rs.getString(1));
        } catch (SQLException e) {
            System.out.println(e);
        }
        return mivalor-suvalor;
    }
    
    public void vender(String producto, int cantidad, String cliente, String fecha){
        Venta v = new Venta();
        v.setId(sacarid());
        v.setProducto(producto);
        v.setCantidadproductos(cantidad);
        v.setCliente(cliente);
        v.setFecha(fecha);
        v.setTotalprecio(sacartotalprecio(producto, cantidad));
        agregarventa(v);
        Connection con;
        
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/bdinventarioventas", "root", "");
            Statement stmt = con.createStatement();
            stmt.executeUpdate("UPDATE productos SET Stock='"+(sacarstock(producto)-cantidad)+"' WHERE Nombre = '"+producto+"'");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    
    public boolean existeproducto (String p){
        String producto = null;
        Connection con;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/bdinventarioventas", "root", "");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Nombre FROM productos WHERE Nombre = '"+p+"'");
            rs.first();
            producto = rs.getString(1);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return producto != null;
    }
    
    private int sacartotalprecio(String producto, int cantidad){
        int total = 0;
        Connection con;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/bdinventarioventas", "root", "");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Precio FROM productos WHERE Nombre = '"+producto +"'");
            rs.first();
            total = Integer.parseInt(rs.getString(1));
        } catch (SQLException e) {
            System.out.println(e);
        }
        return total*cantidad;
    }
    
    private int sacarid(){
        int id = 0;
        Connection con;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/bdinventarioventas", "root", "");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT ID_compra FROM compra WHERE ID_compra = (SELECT MAX(ID_compra) from compra)");
            rs.first();
            id = Integer.parseInt(rs.getString(1));
        } catch (SQLException e) {
            System.out.println(e);
        }
        return id+1;
    }
    
    public void cambairstock(int id,int s){
        
        Connection con;
        
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/bdinventarioventas", "root", "");
            Statement stmt = con.createStatement();
            stmt.executeUpdate("UPDATE productos SET Stock='"+s+"' WHERE ID_Producto = '"+id+"'");
        } catch (SQLException e) {
            System.out.println(e);
        }
    
    }
    
    public DefaultTableModel obtenerTablaproducto(DefaultTableModel m) {
        Connection con;
        m.setRowCount(0); 
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/bdinventarioventas", "root", "");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM productos");
            rs.first();
            do {                
                String[] fila = {rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4)};
                m.addRow(fila);
            } while (rs.next());
        } catch (SQLException e) {
            System.out.println(e);
        }
        m.fireTableDataChanged(); 
        return m;
    }
    
    public DefaultTableModel obtenerTablacliente(DefaultTableModel m) {
        Connection con;
        m.setRowCount(0); 
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/bdinventarioventas", "root", "");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM cliente");
            rs.first();
            do {                
                String[] fila = {rs.getString(1),rs.getString(2),rs.getString(3)};
                m.addRow(fila);
            } while (rs.next());
        } catch (SQLException e) {
            System.out.println(e);
        }
        m.fireTableDataChanged(); 
        return m;
    }

    public DefaultTableModel obtenerTablacaja(DefaultTableModel m) {
        Connection con;
        m.setRowCount(0); 
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/bdinventarioventas", "root", "");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM compra");
            rs.first();
            do {                
                String[] fila = {rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6)};
                m.addRow(fila);
            } while (rs.next());
        } catch (SQLException e) {
            System.out.println(e);
        }
        m.fireTableDataChanged(); 
        return m;
    }
    
    public DefaultTableModel obtenerTablaprestar(DefaultTableModel m) {
        Connection con;
        m.setRowCount(0); 
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/bdinventarioventas", "root", "");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM empresario");
            rs.first();
            do {                
                String[] fila = {rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5)};
                m.addRow(fila);
            } while (rs.next());
        } catch (SQLException e) {
            System.out.println(e);
        }
        m.fireTableDataChanged(); 
        return m;
    }
    
    public int reporte(String f1,String f2){ //SELECT TotalProductos FROM compra BETWEEN '"+f1+"' AND '"+f2+"'
        int mes = 0;
        Connection con;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/bdinventarioventas", "root", "");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT TotalProductos FROM compra WHERE FechaCompra BETWEEN '" + f1 + "' AND '" + f2 + "'");
            rs.first();
            do {                
                mes = mes + Integer.parseInt(rs.getString(1));
                
            } while (rs.next());
        } catch (SQLException e) {
            System.out.println(e);
        }
        return mes;
    }

}
