/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pages;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import koneksi.Koneksi;
import popup.popup_dataterhapus;
import popup.popup_datatersimpan;
import popup.popup_dataterubah;
import table.TableCustom;

/**
 *
 * @author dagi
 */
public class data_master extends javax.swing.JPanel {

     Koneksi koneksi = new Koneksi();
    
    private DefaultTableModel model;
    
     private void autonumber() {
    try {
        Connection c = Koneksi.getKoneksi();
        Statement s = c.createStatement();
        String kategori = (String)cbJenis.getSelectedItem();
        String prefix = "";
        
        // Set prefix based on selected category
        if (kategori.equals("jajanan") || kategori.equals("daharan") || kategori.equals("panganan omah")) {
            prefix = "MK";
        } else if (kategori.equals("unjukan susu") || kategori.equals("unjukan kopi") || kategori.equals("unjukan teh") || kategori.equals("unjukan soda")) {
            prefix = "MN";
        }
        
        String sql = "SELECT * FROM menu WHERE id_menu LIKE '" + prefix + "%' ORDER BY id_menu DESC LIMIT 1";
        ResultSet r = s.executeQuery(sql);
        
        if (r.next()) {
            String lastId = r.getString("id_menu").substring(2);
            int nextId = Integer.parseInt(lastId) + 1;
            String newId = String.format("%03d", nextId);
            txtId.setText(prefix + newId);
        } else {
            txtId.setText(prefix + "001");
        }
        
        r.close();
        s.close();
    } catch (Exception e) {
        System.out.println("autonumber error: " + e.getMessage());
    }
}
    
    
    
    
    
    
    
    
    
    
    public data_master() {
        initComponents();
        Date date = new Date();
        SimpleDateFormat tanggal = new SimpleDateFormat("dd-MM-yyyy");
        
     
        
        txtId.setEnabled(false);
        
        model = new DefaultTableModel();
        
        jTable1.setModel(model);
        
        model.addColumn("ID Menu");
        model.addColumn("Nama Menu");
        model.addColumn("Harga");
        model.addColumn("Kategori");
        model.addColumn("Stock");
        
        
        loadData();
        TableCustom.apply(jScrollPane1, TableCustom.TableType.DEFAULT);
    }
   
    
    
    
    public void clear1(){
        txStock.setText("");
        txHarga.setText("");
        txNama1.setText("");
        txtId.setText("");
    }
    
    public void loadData(){
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();
        
        try{
            Connection c = Koneksi.getKoneksi();
            Statement s = c.createStatement();
            
            String sql = "SELECT * FROM menu";
            ResultSet r = s.executeQuery(sql);
            
            while(r.next()) {
                Object[] o = new Object[5];
                o [0] = r.getString("id_menu");
                o [1] = r.getString("nama_menu");
                o [2] = r.getString("harga");
                o [3] = r.getString("kategori");
                o [4] = r.getString("stock");
                
                model.addRow(o);
            }
            r.close();
            s.close();
        }catch(SQLException e) {
            System.out.println("terjadi kesalahan");     
        }     
    }


        
       
       
        
        
    
    
    
    
    public void cariData(){
        DefaultTableModel tabel = new DefaultTableModel();
        
        tabel.addColumn("ID Menu");
        tabel.addColumn("Nama Menu");
        tabel.addColumn("Harga");
        tabel.addColumn("Kategori");
        tabel.addColumn("Stock");
        
        try{
            Connection c = Koneksi.getKoneksi();
            String sql = "Select * from menu where nama_menu like '%" + txCari.getText() + "%'" + 
                    "or id_menu like '%" + txCari.getText() + "%'";
            Statement stat = c.createStatement();
            ResultSet rs = stat.executeQuery(sql);
            while(rs.next()) {
                tabel.addRow(new Object[]{
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                });
            }
            jTable1.setModel(tabel);
            loadData();
        }catch(Exception e) {
            System.out.println("Cari Data Error");
        }finally{
            
        }
        
    }
    
   private void refreshTableRow() {
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    int rowCount = model.getRowCount();

    // Iterasi melalui baris dan periksa apakah id_menu sama dengan yang baru diperbarui
    for (int i = 0; i < rowCount; i++) {
        String idMenuTabel = model.getValueAt(i, 0).toString(); // Ambil nilai id_menu di baris i
        String idMenuBaru = txtId.getText(); // Ambil id_menu baru dari field txtId

        if (idMenuTabel.equals(idMenuBaru)) {
            // Jika id_menu di tabel sama dengan yang baru diperbarui, update baris tersebut
            model.setValueAt(txNama1.getText(), i, 1); // Kolom 'Kode'
            model.setValueAt(txHarga.getText(), i, 2); // Kolom 'Nama Menu'
            model.setValueAt(cbJenis.getSelectedItem(), i, 3); // Kolom 'Harga'
            model.setValueAt(txStock.getText(), i, 4); // Kolom 'Kategori'
            break; // Keluar dari loop karena sudah menKeluar dari loop karena sudah menemukan dan memperbarui baris yang sesuai
        }
    }
}



   

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        data_barang = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        panelRound1 = new panelCustom.PanelRound();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        panelRound2 = new panelCustom.PanelRound();
        txCari = new textfield.TextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtId = new textfield.TextField();
        jLabel21 = new javax.swing.JLabel();
        txNama1 = new textfield.TextField();
        jLabel14 = new javax.swing.JLabel();
        cbJenis = new combobox.Combobox();
        jLabel15 = new javax.swing.JLabel();
        txHarga = new textfield.TextField();
        jLabel22 = new javax.swing.JLabel();
        txStock = new textfield.TextField();
        btnsimpan = new swing.Button();
        btnedit = new swing.Button();
        btnhapus = new swing.Button();
        btnbatal = new swing.Button();

        setPreferredSize(new java.awt.Dimension(1670, 920));
        setLayout(new java.awt.CardLayout());

        mainPanel.setPreferredSize(new java.awt.Dimension(1670, 920));
        mainPanel.setLayout(new java.awt.CardLayout());

        data_barang.setBackground(new java.awt.Color(166, 104, 68));
        data_barang.setPreferredSize(new java.awt.Dimension(1670, 920));
        data_barang.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel23.setFont(new java.awt.Font("Tahoma", 3, 36)); // NOI18N
        jLabel23.setText("Master Data");
        data_barang.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 13, 251, 32));

        panelRound1.setBackground(new java.awt.Color(235, 180, 136));
        panelRound1.setRoundBottomLeft(50);
        panelRound1.setRoundBottomRight(50);
        panelRound1.setRoundTopLeft(50);
        panelRound1.setRoundTopRight(50);

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

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
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout panelRound1Layout = new javax.swing.GroupLayout(panelRound1);
        panelRound1.setLayout(panelRound1Layout);
        panelRound1Layout.setHorizontalGroup(
            panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound1Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 969, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(41, Short.MAX_VALUE))
        );
        panelRound1Layout.setVerticalGroup(
            panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound1Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 744, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
        );

        data_barang.add(panelRound1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 1060, 840));

        panelRound2.setBackground(new java.awt.Color(235, 180, 136));
        panelRound2.setRoundBottomLeft(50);
        panelRound2.setRoundBottomRight(50);
        panelRound2.setRoundTopLeft(50);
        panelRound2.setRoundTopRight(50);

        txCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txCariActionPerformed(evt);
            }
        });
        txCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txCariKeyTyped(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel13.setText("Cari Menu");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel12.setText("ID Menu");

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel21.setText("Nama Menu");

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel14.setText("Kategori");

        cbJenis.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "unjukan susu", "unjukan soda", "unjukan teh", "unjukan kopi", "jajanan", "daharan", "panganan omah" }));
        cbJenis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbJenisActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel15.setText("Harga");

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel22.setText("Stock");

        btnsimpan.setBackground(new java.awt.Color(166, 104, 68));
        btnsimpan.setText("Simpan");
        btnsimpan.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnsimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsimpanActionPerformed(evt);
            }
        });

        btnedit.setBackground(new java.awt.Color(166, 104, 68));
        btnedit.setText("Edit");
        btnedit.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnedit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneditActionPerformed(evt);
            }
        });

        btnhapus.setBackground(new java.awt.Color(166, 104, 68));
        btnhapus.setText("Hapus");
        btnhapus.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnhapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnhapusActionPerformed(evt);
            }
        });

        btnbatal.setBackground(new java.awt.Color(166, 104, 68));
        btnbatal.setText("Batal");
        btnbatal.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnbatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbatalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelRound2Layout = new javax.swing.GroupLayout(panelRound2);
        panelRound2.setLayout(panelRound2Layout);
        panelRound2Layout.setHorizontalGroup(
            panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(txCari, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21)
                    .addComponent(jLabel14)
                    .addComponent(txNama1, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbJenis, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addComponent(txStock, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelRound2Layout.createSequentialGroup()
                        .addComponent(btnsimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(btnedit, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(btnhapus, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(btnbatal, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelRound2Layout.setVerticalGroup(
            panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound2Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addGroup(panelRound2Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(txCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(14, 14, 14)
                .addComponent(jLabel12)
                .addGap(8, 8, 8)
                .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(jLabel21)
                .addGap(8, 8, 8)
                .addComponent(txNama1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addGap(18, 18, 18)
                .addComponent(cbJenis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jLabel15)
                .addGap(8, 8, 8)
                .addComponent(txHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel22)
                .addGap(8, 8, 8)
                .addComponent(txStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addGroup(panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnsimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnedit, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnhapus, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnbatal, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(183, Short.MAX_VALUE))
        );

        data_barang.add(panelRound2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 50, 480, 840));

        mainPanel.add(data_barang, "card2");

        add(mainPanel, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void btnsimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsimpanActionPerformed
        // TODO add your handling code here:
         String id = txtId.getText();
        String nama = txNama1.getText();
        String jenis = (String)cbJenis.getSelectedItem();
        String harga = txHarga.getText();
        String stock = txStock.getText();

        try{
            Connection c = Koneksi.getKoneksi();
            String sql = "INSERT INTO menu (id_menu, nama_menu, kategori , harga , stock) VALUES (?,?,?,?,?)";

            PreparedStatement p = c.prepareStatement(sql);
            p.setString(1, id);
            p.setString(2, nama);
            p.setString(3, jenis);
            p.setString(4, harga);
            p.setString(5, stock);
            p.executeUpdate();
            p.close();
            
            popup_datatersimpan popupdatas = new popup_datatersimpan();
            popupdatas.setVisible(true);
            //JOptionPane.showMessageDialog(null, "Data Tersimpan");

        }catch (SQLException e){
            e.printStackTrace();

            JOptionPane.showMessageDialog(null,"Terjadi Kesalahan");
        }finally{
            loadData();
            refreshTableRow();
            clear1();
            autonumber();
        }
    }//GEN-LAST:event_btnsimpanActionPerformed

    private void btneditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneditActionPerformed
        // TODO add your handling code here:
         int i = jTable1.getSelectedRow();
        if(i == -1) {
            return;
        }
        String id = (String)model.getValueAt(i, 0);
        String nama = txNama1.getText();
        String jenis = (String)cbJenis.getSelectedItem();
        String harga = txHarga.getText();
        String stock = txStock.getText();
        
        try{
            Connection c = Koneksi.getKoneksi();
            String sql = "UPDATE menu SET nama_menu = ? , kategori = ?, harga = ?, stock = ?  WHERE id_menu =?";
            PreparedStatement p = c.prepareStatement(sql);
            p.setString(1, nama);
            p.setString(2, jenis);
            p.setString(3, harga);
            p.setString(4, stock);
            p.setString(5, id);
            
            p.executeUpdate();
            p.close();
            popup_dataterubah popup = new popup_dataterubah();
            popup.setVisible(true);
            //JOptionPane.showMessageDialog(null, "Data Terubah");
            btnsimpan.setEnabled(true);
            clear1();
        }catch (SQLException e) {
            System.out.println("Update Error"+ e.getMessage());
        }finally{
            loadData();
            refreshTableRow();
            clear1();
            
        }
    }//GEN-LAST:event_btneditActionPerformed

    private void btnhapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnhapusActionPerformed
        // TODO add your handling code here:
         int i = jTable1.getSelectedRow();
         
        if(i == -1){
            return;
        }

        String id = (String)model.getValueAt(i, 0);

        int question = JOptionPane.showConfirmDialog(null, "Yakin Data Akan Dihapus?","Konfirmasi",JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        if(question == JOptionPane.OK_OPTION){
            try{
                Connection c = Koneksi.getKoneksi();
                String sql = "DELETE FROM menu WHERE id_menu = ?";
                PreparedStatement p = c.prepareStatement(sql);
                p.setString(1, id);
                p.executeUpdate();
                p.close();
                
                popup_dataterhapus popup = new popup_dataterhapus();
                popup.setVisible(true);
//JOptionPane.showMessageDialog(null, "Data Terhapus");
            }catch(SQLException e) {
                System.out.println("Terjadi Kesalahan" + e.getMessage());
            }finally{
                loadData();
                refreshTableRow() ;
                clear1();
                cariData();
                 autonumber();

            }
        }
    }//GEN-LAST:event_btnhapusActionPerformed

    private void btnbatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbatalActionPerformed
        // TODO add your handling code here:
             clear1();
        loadData();
        btnsimpan.setEnabled(true);
    }//GEN-LAST:event_btnbatalActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
         int baris = jTable1.rowAtPoint(evt.getPoint());
        String id = jTable1.getValueAt( baris, 0).toString();
        txtId.setText(id);
        String nama = jTable1.getValueAt(baris, 1).toString();
        txNama1.setText(nama);
        String harga = jTable1.getValueAt(baris, 2).toString();
        txHarga.setText(harga);
        String kategori = jTable1.getValueAt(baris, 3).toString();
        cbJenis.setSelectedItem(kategori);
        String stock = jTable1.getValueAt(baris, 4).toString();
        txStock.setText(stock);
    }//GEN-LAST:event_jTable1MouseClicked

    private void txCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txCariActionPerformed
        // TODO add your handling code here:
        cariData();
    }//GEN-LAST:event_txCariActionPerformed

    private void txCariKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txCariKeyTyped
        // TODO add your handling code here:
        cariData();
    }//GEN-LAST:event_txCariKeyTyped

    private void cbJenisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbJenisActionPerformed
        // TODO add your handling code here:
         autonumber();
    }//GEN-LAST:event_cbJenisActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private swing.Button btnbatal;
    private swing.Button btnedit;
    private swing.Button btnhapus;
    private swing.Button btnsimpan;
    private combobox.Combobox cbJenis;
    private javax.swing.JPanel data_barang;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel mainPanel;
    private panelCustom.PanelRound panelRound1;
    private panelCustom.PanelRound panelRound2;
    private textfield.TextField txCari;
    private textfield.TextField txHarga;
    private textfield.TextField txNama1;
    private textfield.TextField txStock;
    private textfield.TextField txtId;
    // End of variables declaration//GEN-END:variables
}
