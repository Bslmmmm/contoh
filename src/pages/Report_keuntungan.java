
package pages;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;
import koneksi.Koneksi;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import table.TableCustom;

public class Report_keuntungan extends javax.swing.JPanel {
    private DefaultTableModel modelkeunt;
 
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
            Sheet sheet = wb.createSheet("Report Keuntungan");
            
            Row rowCol = sheet.createRow(0);
            for(int i=0;i<jTable1.getColumnCount();i++){
                Cell cell = rowCol.createCell(i);
                cell.setCellValue(jTable1.getColumnName(i));
            }
            
            for (int j=0; j<jTable1.getRowCount();j++){
                Row row = sheet.createRow(j+1);
                for (int k=0; k<jTable1.getColumnCount();k++){
                    Cell cell = row.createCell(k);
                    if(jTable1.getValueAt(j, k) != null){
                        cell.setCellValue(jTable1.getValueAt(j, k).toString());
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
    
    public Report_keuntungan() throws SQLException {
        initComponents();
        TableCustom.apply(jScrollPane1, TableCustom.TableType.DEFAULT);
        
        modelkeunt =  new DefaultTableModel();
        jTable1.setModel(modelkeunt);
        
        modelkeunt.addColumn("Tanggal");
        modelkeunt.addColumn("Pemasukan");
        modelkeunt.addColumn("Pengeluaran");
        modelkeunt.addColumn("Total Keuntungan");
        
        //LaporKeunt();
        loadComboBoxData();
        loadTableData();
    }
    
     private void loadComboBoxData() {
        try {
            Connection connection = Koneksi.getKoneksi();
            String query = "SELECT DISTINCT tanggal FROM transaksi ORDER BY tanggal";
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
            cmb_start.setModel(modelStartDate);
            cmb_end.setModel(modelEndDate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void loadTableData() {
        String startDate = (String) cmb_start.getSelectedItem();
        String endDate = (String) cmb_end.getSelectedItem();

        if (startDate == null || endDate == null || startDate.isEmpty() || endDate.isEmpty()) {
            return;
        }

        modelkeunt.setRowCount(0);

        try {
            Connection connection = Koneksi.getKoneksi();
            String query = "SELECT t.tanggal, COALESCE(b.total_modal, 0) AS total_modal, SUM(t.total) AS total_penjualan, " +
                "(SUM(t.total) - COALESCE(b.total_modal, 0)) AS keuntungan " +
                "FROM transaksi t " +
                "LEFT JOIN (SELECT tanggal, SUM(modal) AS total_modal FROM belanja GROUP BY tanggal) b ON t.tanggal = b.tanggal " +
                "WHERE t.tanggal BETWEEN ? AND ? " +
                "GROUP BY t.tanggal";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, startDate);
            preparedStatement.setString(2, endDate);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String tanggal = resultSet.getString("tanggal");
                int totalPenjualan = resultSet.getInt("total_penjualan");
                int totalModal = resultSet.getInt("total_modal");
                int keuntungan = resultSet.getInt("keuntungan");

                modelkeunt.addRow(new Object[]{tanggal, totalPenjualan, totalModal, keuntungan});
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    private void LaporKeunt() throws SQLException {
    modelkeunt.setRowCount(0);

    try {
        Connection connection = Koneksi.getKoneksi();

        // Query untuk mendapatkan data keuntungan dari transaksi dan belanja
        String query = "SELECT t.tanggal, COALESCE(b.total_modal, 0) AS total_modal, SUM(t.total) AS total_penjualan, (SUM(t.total) - COALESCE(b.total_modal, 0)) AS keuntungan " +
            "FROM transaksi t " +
            "LEFT JOIN (SELECT tanggal, SUM(modal) AS total_modal FROM belanja GROUP BY tanggal) b ON t.tanggal = b.tanggal " +
            "GROUP BY t.tanggal";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            String tanggal = resultSet.getString("tanggal");
            int totalPenjualan = resultSet.getInt("total_penjualan");
            int totalModal = resultSet.getInt("total_modal");
            int keuntungan = resultSet.getInt("keuntungan");

            modelkeunt.addRow(new Object[]{tanggal, totalPenjualan, totalModal, keuntungan});

            System.out.println("Tanggal: " + tanggal + ", Total Penjualan: " + totalPenjualan + ", Total Modal: " + totalModal + ", Keuntungan: " + keuntungan);
        }

        resultSet.close();
        statement.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}



    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPan = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        panelRound1 = new panelCustom.PanelRound();
        cmb_start = new combobox.Combobox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnsimpan = new swing.Button();
        cmb_end = new combobox.Combobox();
        jLabel1 = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(1630, 900));
        setLayout(new java.awt.CardLayout());

        mainPan.setPreferredSize(new java.awt.Dimension(1670, 920));

        jPanel1.setBackground(new java.awt.Color(166, 104, 68));
        jPanel1.setPreferredSize(new java.awt.Dimension(1670, 990));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelRound1.setBackground(new java.awt.Color(235, 180, 136));
        panelRound1.setRoundBottomLeft(50);
        panelRound1.setRoundBottomRight(50);
        panelRound1.setRoundTopLeft(50);
        panelRound1.setRoundTopRight(50);

        cmb_start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_startActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jTable1);

        btnsimpan.setBackground(new java.awt.Color(166, 104, 68));
        btnsimpan.setText("Export");
        btnsimpan.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnsimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsimpanActionPerformed(evt);
            }
        });

        cmb_end.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_endActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelRound1Layout = new javax.swing.GroupLayout(panelRound1);
        panelRound1.setLayout(panelRound1Layout);
        panelRound1Layout.setHorizontalGroup(
            panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1373, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelRound1Layout.createSequentialGroup()
                        .addComponent(cmb_start, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cmb_end, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(321, 321, 321)
                        .addComponent(btnsimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(75, Short.MAX_VALUE))
        );
        panelRound1Layout.setVerticalGroup(
            panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnsimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmb_end, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                    .addComponent(cmb_start, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 627, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66))
        );

        jPanel1.add(panelRound1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, 1460, 770));

        jLabel1.setFont(new java.awt.Font("Tahoma", 3, 36)); // NOI18N
        jLabel1.setText("Report Keuntungan Kotor");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 13, 480, 61));

        javax.swing.GroupLayout mainPanLayout = new javax.swing.GroupLayout(mainPan);
        mainPan.setLayout(mainPanLayout);
        mainPanLayout.setHorizontalGroup(
            mainPanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        mainPanLayout.setVerticalGroup(
            mainPanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        add(mainPan, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void btnsimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsimpanActionPerformed
        ExportExcel();
    }//GEN-LAST:event_btnsimpanActionPerformed

    private void cmb_startActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_startActionPerformed
        loadTableData();
    }//GEN-LAST:event_cmb_startActionPerformed

    private void cmb_endActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_endActionPerformed
        loadTableData();
    }//GEN-LAST:event_cmb_endActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private swing.Button btnsimpan;
    private combobox.Combobox cmb_end;
    private combobox.Combobox cmb_start;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel mainPan;
    private panelCustom.PanelRound panelRound1;
    // End of variables declaration//GEN-END:variables
}
