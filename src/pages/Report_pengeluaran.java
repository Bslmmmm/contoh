
package pages;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import table.*;
import koneksi.Koneksi;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Report_pengeluaran extends javax.swing.JPanel {
    private DefaultTableModel modelpenge;

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
            for(int i=0;i<tb_pengeluaran.getColumnCount();i++){
                Cell cell = rowCol.createCell(i);
                cell.setCellValue(tb_pengeluaran.getColumnName(i));
            }
            
            for (int j=0; j<tb_pengeluaran.getRowCount();j++){
                Row row = sheet.createRow(j+1);
                for (int k=0; k<tb_pengeluaran.getColumnCount();k++){
                    Cell cell = row.createCell(k);
                    if(tb_pengeluaran.getValueAt(j, k) != null){
                        cell.setCellValue(tb_pengeluaran.getValueAt(j, k).toString());
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
    
    public Report_pengeluaran() {
        initComponents();
        TableCustom.apply(jScrollPane2, TableCustom.TableType.DEFAULT);
        
        modelpenge = new DefaultTableModel();
        tb_pengeluaran.setModel(modelpenge);

        // Tambahkan kolom ke model
        modelpenge.addColumn("Tanggal");
        modelpenge.addColumn("Pengeluaran");
        modelpenge.addColumn("Keterangan");
        
       // laporPenge();
       
       loadComboBoxData();
       loadTableData();
    }

    private void loadComboBoxData() {
        try {
            Connection connection = Koneksi.getKoneksi();
            String query = "SELECT DISTINCT tanggal FROM belanja ORDER BY tanggal";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            Vector<String> dates = new Vector<>();
            while (resultSet.next()) {
                String tanggal = resultSet.getString("tanggal");
                dates.add(tanggal);
            }
            resultSet.close();
            statement.close();

            DefaultComboBoxModel<String> modelStartDate = new DefaultComboBoxModel<>(dates);
            DefaultComboBoxModel<String> modelEndDate = new DefaultComboBoxModel<>(dates);
            cmb_start1.setModel(modelStartDate);
            cmb_end1.setModel(modelEndDate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadTableData() {
        String startDate = (String) cmb_start1.getSelectedItem();
        String endDate = (String) cmb_end1.getSelectedItem();

        if (startDate == null || endDate == null || startDate.isEmpty() || endDate.isEmpty()) {
            return;
        }

        modelpenge.setRowCount(0);

        try {
            Connection connection = Koneksi.getKoneksi();
            String query = "SELECT tanggal, modal, keterangan FROM belanja " +
                "WHERE tanggal BETWEEN ? AND ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, startDate);
            preparedStatement.setString(2, endDate);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String tanggal = resultSet.getString("tanggal");
                int modal = resultSet.getInt("modal");
                String keterangan = resultSet.getString("keterangan");

                modelpenge.addRow(new Object[]{tanggal, modal, keterangan});
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    private void laporPenge() {
    modelpenge.setRowCount(0); // Mengosongkan data tabel sebelum menampilkan yang baru

    try {
        Connection connection = Koneksi.getKoneksi();

        String query = "SELECT tanggal, modal, keterangan FROM belanja";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        // Bersihkan model sebelum menambahkan data baru
        modelpenge.setRowCount(0);

        // Iterate melalui hasil dan tambahkan baris baru ke model
        while (resultSet.next()) {
        String tanggal = resultSet.getString("tanggal");
        int modal = resultSet.getInt("modal");
        String keterangan = resultSet.getString("keterangan");
        
        // Tambahkan data ke model
        modelpenge.addRow(new Object[]{tanggal, modal, keterangan});

        // Tambahkan pernyataan pencetakan untuk melacak nilai "pengeluaran"
        System.out.println("Tanggal: " + tanggal + ", Pengeluaran: " + modal + ", Keterangan: " + keterangan);
    }
    }catch (SQLException e) {
    e.printStackTrace();
    }
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuUtama = new javax.swing.JPanel();
        pengeluaranpage = new javax.swing.JPanel();
        panelRound1 = new panelCustom.PanelRound();
        btnsimpan = new swing.Button();
        jScrollPane2 = new javax.swing.JScrollPane();
        tb_pengeluaran = new javax.swing.JTable();
        cmb_start1 = new combobox.Combobox();
        cmb_end1 = new combobox.Combobox();
        jLabel1 = new javax.swing.JLabel();

        setLayout(new java.awt.CardLayout());

        menuUtama.setPreferredSize(new java.awt.Dimension(1670, 920));

        pengeluaranpage.setBackground(new java.awt.Color(166, 104, 68));
        pengeluaranpage.setEnabled(false);
        pengeluaranpage.setPreferredSize(new java.awt.Dimension(1670, 920));
        pengeluaranpage.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelRound1.setBackground(new java.awt.Color(235, 180, 136));
        panelRound1.setRoundBottomLeft(50);
        panelRound1.setRoundBottomRight(50);
        panelRound1.setRoundTopLeft(50);
        panelRound1.setRoundTopRight(50);

        btnsimpan.setBackground(new java.awt.Color(166, 104, 68));
        btnsimpan.setText("Export");
        btnsimpan.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnsimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsimpanActionPerformed(evt);
            }
        });

        tb_pengeluaran.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tb_pengeluaran);

        cmb_start1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_start1ActionPerformed(evt);
            }
        });

        cmb_end1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_end1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelRound1Layout = new javax.swing.GroupLayout(panelRound1);
        panelRound1.setLayout(panelRound1Layout);
        panelRound1Layout.setHorizontalGroup(
            panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelRound1Layout.createSequentialGroup()
                        .addComponent(cmb_start1, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(cmb_end1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(281, 281, 281)
                        .addComponent(btnsimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1461, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(57, Short.MAX_VALUE))
        );
        panelRound1Layout.setVerticalGroup(
            panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmb_start1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnsimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmb_end1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 619, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(65, Short.MAX_VALUE))
        );

        pengeluaranpage.add(panelRound1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, 1530, 760));

        jLabel1.setFont(new java.awt.Font("Tahoma", 3, 36)); // NOI18N
        jLabel1.setText("Report Pengeluaran");
        pengeluaranpage.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 13, 380, 61));

        javax.swing.GroupLayout menuUtamaLayout = new javax.swing.GroupLayout(menuUtama);
        menuUtama.setLayout(menuUtamaLayout);
        menuUtamaLayout.setHorizontalGroup(
            menuUtamaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuUtamaLayout.createSequentialGroup()
                .addComponent(pengeluaranpage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        menuUtamaLayout.setVerticalGroup(
            menuUtamaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pengeluaranpage, javax.swing.GroupLayout.DEFAULT_SIZE, 990, Short.MAX_VALUE)
        );

        add(menuUtama, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void btnsimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsimpanActionPerformed
        ExportExcel();
    }//GEN-LAST:event_btnsimpanActionPerformed

    private void cmb_start1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_start1ActionPerformed
        loadTableData();
    }//GEN-LAST:event_cmb_start1ActionPerformed

    private void cmb_end1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_end1ActionPerformed
        loadTableData();
    }//GEN-LAST:event_cmb_end1ActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private swing.Button btnsimpan;
    private combobox.Combobox cmb_end1;
    private combobox.Combobox cmb_start1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel menuUtama;
    private panelCustom.PanelRound panelRound1;
    private javax.swing.JPanel pengeluaranpage;
    private javax.swing.JTable tb_pengeluaran;
    // End of variables declaration//GEN-END:variables
}
