/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pages;

import PageForm.Dashboard;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import koneksi.Koneksi;
import table.TableCustom;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 *
 * @author dagi
 */
public class Riwayat_transaksi extends javax.swing.JPanel {

    private DefaultTableModel modelRiwayat;

  
    public void openFile(String file){
        try {
            File path  = new File(file);
            Desktop.getDesktop().open(path);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
    public void ExportExcel(){
        try {
            JFileChooser jfilechooser = new JFileChooser();
            jfilechooser.showSaveDialog(this);
            File savefile = jfilechooser.getSelectedFile();
            if(savefile != null){
            savefile = new File (savefile.toString()+".xlsx");
            Workbook wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet("Riwayat Transaksi");
            
            Row rowCol = sheet.createRow(0);
            for(int i=0;i<tabelriwayat.getColumnCount();i++){
                Cell cell = rowCol.createCell(i);
                cell.setCellValue(tabelriwayat.getColumnName(i));
            }
            
            for (int j=0; j<tabelriwayat.getRowCount();j++){
                Row row = sheet.createRow(j+1);
                for (int k=0; k<tabelriwayat.getColumnCount();k++){
                    Cell cell = row.createCell(k);
                    if(tabelriwayat.getValueAt(j, k) != null){
                        cell.setCellValue(tabelriwayat.getValueAt(j, k).toString());
                    }
            }
            }
            FileOutputStream out = new FileOutputStream(new File(savefile.toString()));
            wb.write(out);
            wb.close();
            out.close();
                openFile(savefile.toString());
            }else {
                System.out.println("error export excel");
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }catch(IOException IO){
            System.out.println(IO);
        }
    }
    
        private void displayDataByNoNota(String noNota) {
            modelRiwayat.getDataVector().removeAllElements();
            modelRiwayat.fireTableDataChanged();

            try {
                Connection c = Koneksi.getKoneksi();
                Statement s = c.createStatement();

                String sql = "SELECT * FROM detail_transaksi WHERE no_nota = '" + noNota + "'";
                ResultSet result = s.executeQuery(sql);

                while (result.next()) {
                    Object[] objek = new Object[6];
                    objek[0] = result.getString("no_nota");
                    objek[1] = result.getString("id_menu");
                    objek[2] = result.getString("nama_menu");
                    objek[3] = result.getString("jumlah");
                    objek[4] = result.getString("harga");
                    objek[5] = result.getString("subtotal");

                    modelRiwayat.addRow(objek);
                }

                result.close();
                s.close();

                tabelriwayat.setModel(modelRiwayat);
            } catch (SQLException e) {
                System.out.println("SQLException: " + e.getMessage());
                e.printStackTrace();
            }
        }

    public void loadriwayat() {
        modelRiwayat.getDataVector().removeAllElements();
        modelRiwayat.fireTableDataChanged();

        try {
            Connection c = Koneksi.getKoneksi();
            Statement s = c.createStatement();

            // Membersihkan elemen yang ada pada JComboBox
            cmb_nota.removeAllItems();

            // Pernyataan SQL untuk mengisi JComboBox cmb_nota
            String sqlComboBox = "SELECT DISTINCT no_nota FROM detail_transaksi";
            ResultSet resultComboBox = s.executeQuery(sqlComboBox);

            while (resultComboBox.next()) {
                String noNota = resultComboBox.getString("no_nota");
                cmb_nota.addItem(noNota);
            }

            resultComboBox.close();

            // Pernyataan SQL untuk mengisi tabel
            String sqlTable = "SELECT * FROM detail_transaksi";
            ResultSet resultTable = s.executeQuery(sqlTable);

            while (resultTable.next()) {
                Object[] objek = new Object[6];
                objek[0] = resultTable.getString("no_nota");
                objek[1] = resultTable.getString("id_menu");
                objek[2] = resultTable.getString("nama_menu");
                objek[3] = resultTable.getString("jumlah");
                objek[4] = resultTable.getString("harga");
                objek[5] = resultTable.getString("subtotal");

                modelRiwayat.addRow(objek);
            }

            resultTable.close();
            s.close();

            tabelriwayat.setModel(modelRiwayat);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updatedatariwayat() {
        modelRiwayat.getDataVector().removeAllElements();
        modelRiwayat.fireTableDataChanged();

        String selectedNoNota = (String) cmb_nota.getSelectedItem();
        if (selectedNoNota != null) {
            displayDataByNoNota(selectedNoNota);
        }

    }

    public Riwayat_transaksi() {
        initComponents();
        modelRiwayat = new DefaultTableModel();
        tabelriwayat.setModel(modelRiwayat);
        modelRiwayat.addColumn("No. Transaksi");
        modelRiwayat.addColumn("ID");
        modelRiwayat.addColumn("Nama Menu");
        modelRiwayat.addColumn("Jumlah");
        modelRiwayat.addColumn("Harga");
        modelRiwayat.addColumn("Total");
        
        TableCustom.apply(jScrollPane4, TableCustom.TableType.DEFAULT);

        loadriwayat();
    }


    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        pn_lpout = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tabelriwayat = new javax.swing.JTable();
        cetakriwayat = new swing.Button();
        cmb_nota = new combobox.Combobox();
        Export = new swing.Button();
        panelRound1 = new panelCustom.PanelRound();
        jLabel1 = new javax.swing.JLabel();

        setLayout(new java.awt.CardLayout());

        mainPanel.setLayout(new java.awt.CardLayout());

        pn_lpout.setBackground(new java.awt.Color(166, 104, 68));
        pn_lpout.setPreferredSize(new java.awt.Dimension(1670, 920));
        pn_lpout.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tabelriwayat.setBackground(new java.awt.Color(217, 198, 191));
        tabelriwayat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(tabelriwayat);

        pn_lpout.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(107, 161, 744, 509));

        cetakriwayat.setBackground(new java.awt.Color(191, 137, 90));
        cetakriwayat.setText("Cetak Transaksi");
        cetakriwayat.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        cetakriwayat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cetakriwayatActionPerformed(evt);
            }
        });
        pn_lpout.add(cetakriwayat, new org.netbeans.lib.awtextra.AbsoluteConstraints(493, 101, 170, -1));

        cmb_nota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_notaActionPerformed(evt);
            }
        });
        pn_lpout.add(cmb_nota, new org.netbeans.lib.awtextra.AbsoluteConstraints(107, 95, 181, -1));

        Export.setBackground(new java.awt.Color(191, 137, 90));
        Export.setText("Export");
        Export.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Export.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExportActionPerformed(evt);
            }
        });
        pn_lpout.add(Export, new org.netbeans.lib.awtextra.AbsoluteConstraints(367, 101, 108, -1));

        panelRound1.setBackground(new java.awt.Color(235, 180, 136));
        panelRound1.setPreferredSize(new java.awt.Dimension(1460, 770));
        panelRound1.setRoundBottomLeft(50);
        panelRound1.setRoundBottomRight(50);
        panelRound1.setRoundTopLeft(50);
        panelRound1.setRoundTopRight(50);

        javax.swing.GroupLayout panelRound1Layout = new javax.swing.GroupLayout(panelRound1);
        panelRound1.setLayout(panelRound1Layout);
        panelRound1Layout.setHorizontalGroup(
            panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 820, Short.MAX_VALUE)
        );
        panelRound1Layout.setVerticalGroup(
            panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 620, Short.MAX_VALUE)
        );

        pn_lpout.add(panelRound1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 70, 820, 620));

        jLabel1.setFont(new java.awt.Font("Tahoma", 3, 36)); // NOI18N
        jLabel1.setText("Riwayat Transaksi");
        pn_lpout.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 350, 61));

        mainPanel.add(pn_lpout, "card2");

        add(mainPanel, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void cetakriwayatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cetakriwayatActionPerformed
 Connection conn = Koneksi.getKoneksi();
        String file = "src/IReport/cetakriwayat.jasper";
        String key = cmb_nota.getSelectedItem().toString();

        try {

            HashMap parameter = new HashMap();
            parameter.put("nota", key);
            JasperPrint jp = JasperFillManager.fillReport(file, parameter, conn);
            JasperViewer viewer = new JasperViewer(jp, false);
            viewer.setVisible(true);
        } catch (JRException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_cetakriwayatActionPerformed

    private void cmb_notaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_notaActionPerformed
        // TODO add your handling code here:
        
        updatedatariwayat();
    }//GEN-LAST:event_cmb_notaActionPerformed

    private void ExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExportActionPerformed
        ExportExcel();
    }//GEN-LAST:event_ExportActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private swing.Button Export;
    private swing.Button cetakriwayat;
    private combobox.Combobox cmb_nota;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPanel mainPanel;
    private panelCustom.PanelRound panelRound1;
    private javax.swing.JPanel pn_lpout;
    private javax.swing.JTable tabelriwayat;
    // End of variables declaration//GEN-END:variables
}
