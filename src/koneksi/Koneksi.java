/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package koneksi;

import java.sql.DriverManager;

/**
 *
 * @author BINTANG
 */
public class Koneksi {
    private static java.sql.Connection koneksi;
    
    
    public static java.sql.Connection getKoneksi(){
        if (koneksi == null) {
            try {
            String url="jdbc:mysql://localhost:3306/kasir";
            String user="root";
            String pass="";
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            koneksi = DriverManager.getConnection(url, user, pass);
            System.out.println("Berhasil Terhubung");
        } catch (Exception e) {
            System.out.println("Error");
        }
            }
        return koneksi;
        }
    public static void main (String args[]) {
        getKoneksi();
    }
}
