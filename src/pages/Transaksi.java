/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pages;

import Menu_Utama.Menu_Utama;
import PageForm.Dashboard;
import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import koneksi.Koneksi;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import table.TableCustom;

/**
 *
 * @author dagi
 */
public class Transaksi extends javax.swing.JPanel {

    private DefaultTableModel model;
    private DefaultTableModel modelRiwayat;

    public void cari() {

        DefaultTableModel tabel = new DefaultTableModel();

        tabel.addColumn("ID");
        tabel.addColumn("Nama Menu");
        tabel.addColumn("Harga");
        tabel.addColumn("Kategori");

        try {
            Connection c = Koneksi.getKoneksi();
            String sql = "Select * from menu where nama_menu like '%" + txt_cari.getText() + "%'";
            Statement stat = c.createStatement();
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                tabel.addRow(new Object[]{
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),});
            }
            tabelcari.setModel(tabel);

        } catch (Exception e) {
            System.out.println("Cari Data Error");
        } finally {

        }
    }

    public void totalbiaya() {
        int jumlahBaris = tabelnota.getRowCount();
        int totalBiaya = 0;
        int jumlahbarang, hargabarang;

        for (int i = 0; i < jumlahBaris; i++) {
            jumlahbarang = Integer.parseInt(tabelnota.getValueAt(i, 3).toString());
            hargabarang = Integer.parseInt(tabelnota.getValueAt(i, 4).toString());
            totalBiaya = totalBiaya + (jumlahbarang * hargabarang);

        }
        txt__total.setText(String.valueOf(totalBiaya));

    }

    public void loadData1() {
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();

        try {
            Connection c = Koneksi.getKoneksi();
            Statement s = c.createStatement();

            String sql = "SELECT * FROM menu";
            ResultSet r = s.executeQuery(sql);

            while (r.next()) {
                Object[] objek = new Object[5];
                objek[0] = r.getString("id_menu");
                objek[1] = r.getString("nama_menu");
                objek[2] = r.getString("harga");
                objek[3] = r.getString("kategori");
                objek[4] = r.getString("stock");

                model.addRow(objek);
            }

            tabelcari.setModel(model);
        } catch (Exception e) {
            System.out.println("terjadi kesalahan");
        }
    }

    private void autokode() {
        try {
            Connection konek = Koneksi.getKoneksi();
            Statement statement = konek.createStatement();
            String sql = "SELECT * FROM transaksi ORDER BY no_nota DESC";
            ResultSet result = statement.executeQuery(sql);

            if (result.next()) {
                String noid = result.getString("no_nota").substring(2);
                String menu = "" + (Integer.parseInt(noid) + 1);
                String Nol = "";

                if (menu.length() == 1) {
                    Nol = "00";
                } else if (menu.length() == 2) {
                    Nol = "0";
                } else if (menu.length() == 3) {
                    Nol = "";
                }
                txt_notran.setText("OT" + Nol + menu);

            } else {
                txt_notran.setText("OT001");
            }
            result.close();
            statement.close();
        } catch (Exception e) {
            System.out.println("Auto Number Error");
        }

    }

  public void loadData() {
        DefaultTableModel model = (DefaultTableModel) tabelnota.getModel();

        String idMenuBaru = txt_idbarang1.getText();
        String namaMenuBaru = txt_namamenu1.getText();
        int jumlahBaru = Integer.parseInt(txt_jumlah1.getText());
        int hargaBaru = Integer.parseInt(txt_harga1.getText());
        int totalBaru = jumlahBaru * hargaBaru;

        // Cek apakah menu sudah ada dalam tabelnota
        int jumlahBaris = model.getRowCount();
        boolean menuSudahAda = false;

        for (int i = 0; i < jumlahBaris; i++) {
            String idMenu = model.getValueAt(i, 1).toString();
            String namaMenu = model.getValueAt(i, 2).toString();

            if (idMenu.equals(idMenuBaru) && namaMenu.equals(namaMenuBaru)) {
                // Jika menu sudah ada, update jumlah dan total
                int jumlahLama = Integer.parseInt(model.getValueAt(i, 3).toString());
                int totalLama = Integer.parseInt(model.getValueAt(i, 5).toString());

                model.setValueAt(jumlahLama + jumlahBaru, i, 3); // Tambahkan jumlah baru
                model.setValueAt(hargaBaru, i, 4); // Set harga baru
                model.setValueAt(totalLama + totalBaru, i, 5); // Tambahkan total baru
                menuSudahAda = true;
                break; // Keluar dari loop karena menu sudah ditemukan
            }
        }

        // Jika menu belum ada, tambahkan baris baru
        if (!menuSudahAda) {
            model.addRow(new Object[]{
                txt_notran.getText(),
                idMenuBaru,
                namaMenuBaru,
                jumlahBaru,
                hargaBaru,
                totalBaru
            });
        }




        // Hitung totalBiaya
        totalbiaya();
    }

    public void utama() {
        txt_notran.setText("");
        txt_idbarang1.setText("");
        txt_namamenu1.setText("");
        txt_jumlah1.setText("");
        txt_harga1.setText("");
        autokode();
    }

    public void clear() {
        txt__total.setText("0");
        bayar.setText("0");
        kembali.setText("0");

    }

    public void clear2() {
        txt_idbarang1.setText("");
        txt_namamenu1.setText("");
        txt_jumlah1.setText("");
        txt_harga1.setText("");
    }

    public void tambahTransaksi() {
        int jumlah, harga, Total;

        jumlah = Integer.valueOf(txt_jumlah1.getText());
        harga = Integer.valueOf(txt_harga1.getText());
        Total = jumlah * harga;

        txt__total.setText(String.valueOf(Total));

        loadData();
        totalbiaya();
        clear2();
        bayar.requestFocus();
    }

    public void kosong() {
        DefaultTableModel model = (DefaultTableModel) tabelnota.getModel();

        while (model.getRowCount() > 0) {
            model.removeRow(0);

        }
    }

    public Transaksi() {
        initComponents();

        table.TableCustom.apply(jScrollPane1, TableCustom.TableType.DEFAULT);
        table.TableCustom.apply(jScrollPane2, TableCustom.TableType.DEFAULT);

        //design column transaksi
        model = new DefaultTableModel();
        tabelnota.setModel(model);

        model.addColumn("No");
        model.addColumn("ID");
        model.addColumn("Nama Menu");
        model.addColumn("Jumlah");
        model.addColumn("Harga");
        model.addColumn("Total");

        //design column cari
        model = new DefaultTableModel();
        tabelcari.setModel(model);

        model.addColumn("ID");
        model.addColumn("Nama Menu");
        model.addColumn("Harga");
        model.addColumn("Kategori");
        model.addColumn("Stock");

        //design column riwayat
        loadData1();

        //design header
        UIManager.put("TableHeader.background", new Color(217, 217, 217));
        JTableHeader hd = tabelnota.getTableHeader();
        hd.setBackground(new Color(217, 217, 217));
        JTableHeader hd2 = tabelcari.getTableHeader();
        hd2.setBackground(new Color(217, 217, 217));

        //utama
        utama();
        Date date = new Date();
        SimpleDateFormat tanggal = new SimpleDateFormat("dd-MM-yyyy");

        txt_tgl.setText(tanggal.format(date));
        txt__total.setText("0");
        bayar.setText("0");
        kembali.setText("0");
        txt_cari.requestFocus();

        //geseran tabel
        for (int i = 0; i < model.getColumnCount(); i++) {
            Class<?> col_class = model.getColumnClass(i);
            tabelnota.setDefaultEditor(col_class, null); // Mengatur editor default menjadi null
        }

        // Membuat sel tidak dapat diedit di tabel cari
        for (int i = 0; i < model.getColumnCount(); i++) {
            Class<?> col_class = model.getColumnClass(i);
            tabelcari.setDefaultEditor(col_class, null); // Mengatur editor default menjadi null
        }
        
        tabelcari.getColumnModel().getColumn(0).setPreferredWidth(10);
        tabelcari.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabelcari.getColumnModel().getColumn(4).setPreferredWidth(20);
        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        panelRound1 = new panelCustom.PanelRound();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelcari = new javax.swing.JTable();
        panelRound2 = new panelCustom.PanelRound();
        txt_cari = new textfield.TextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txt_tgl = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txt_notran = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txt_idbarang1 = new textfield.TextField();
        txt_namamenu1 = new textfield.TextField();
        txt_jumlah1 = new textfield.TextField();
        txt_harga1 = new textfield.TextField();
        panelRound3 = new panelCustom.PanelRound();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelnota = new javax.swing.JTable();
        kembali = new javax.swing.JTextField();
        txt_k = new javax.swing.JLabel();
        txt_b = new javax.swing.JLabel();
        bayar = new javax.swing.JTextField();
        txt__total = new javax.swing.JTextField();
        txt_t = new javax.swing.JLabel();
        btnhapus = new swing.Button();
        btnsimpan = new swing.Button();
        jLabel1 = new javax.swing.JLabel();

        setLayout(new java.awt.CardLayout());

        jPanel1.setBackground(new java.awt.Color(166, 104, 68));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelRound1.setBackground(new java.awt.Color(235, 180, 136));
        panelRound1.setRoundBottomLeft(50);
        panelRound1.setRoundBottomRight(50);
        panelRound1.setRoundTopLeft(50);
        panelRound1.setRoundTopRight(50);

        tabelcari.setBackground(new java.awt.Color(217, 198, 191));
        tabelcari.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tabelcari.setModel(new javax.swing.table.DefaultTableModel(
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
        tabelcari.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelcariMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabelcari);

        javax.swing.GroupLayout panelRound1Layout = new javax.swing.GroupLayout(panelRound1);
        panelRound1.setLayout(panelRound1Layout);
        panelRound1Layout.setHorizontalGroup(
            panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 508, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelRound1Layout.setVerticalGroup(
            panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 611, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        jPanel1.add(panelRound1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 530, 670));

        panelRound2.setBackground(new java.awt.Color(235, 180, 136));
        panelRound2.setRoundBottomLeft(50);
        panelRound2.setRoundBottomRight(50);
        panelRound2.setRoundTopLeft(50);
        panelRound2.setRoundTopRight(50);

        txt_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_cariActionPerformed(evt);
            }
        });
        txt_cari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_cariKeyTyped(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel18.setText("Cari");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setText("Tanggal");

        txt_tgl.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txt_tgl.setText("jLabel3");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setText("No. Transaksi");

        txt_notran.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txt_notran.setText("jLabel3");

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("ID Barang");

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Nama Menu");

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Jumlah");

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Harga");

        txt_idbarang1.setEnabled(false);

        txt_namamenu1.setEnabled(false);

        txt_jumlah1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_jumlah1ActionPerformed(evt);
            }
        });

        txt_harga1.setEnabled(false);

        javax.swing.GroupLayout panelRound2Layout = new javax.swing.GroupLayout(panelRound2);
        panelRound2.setLayout(panelRound2Layout);
        panelRound2Layout.setHorizontalGroup(
            panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel16)
                    .addComponent(jLabel15)
                    .addComponent(jLabel14)
                    .addGroup(panelRound2Layout.createSequentialGroup()
                        .addGroup(panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_tgl, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_notran, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(txt_idbarang1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_namamenu1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jumlah1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_harga1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_cari, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        panelRound2Layout.setVerticalGroup(
            panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound2Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txt_tgl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txt_notran))
                .addGap(18, 18, 18)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_idbarang1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_namamenu1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_jumlah1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_harga1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(179, Short.MAX_VALUE))
        );

        jPanel1.add(panelRound2, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 60, 270, 670));

        panelRound3.setBackground(new java.awt.Color(235, 180, 136));
        panelRound3.setRoundBottomLeft(50);
        panelRound3.setRoundBottomRight(50);
        panelRound3.setRoundTopLeft(50);
        panelRound3.setRoundTopRight(50);

        tabelnota.setBackground(new java.awt.Color(217, 198, 191));
        tabelnota.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Nama Menu", "Jumlah", "Harga"
            }
        ));
        jScrollPane1.setViewportView(tabelnota);

        kembali.setBackground(new java.awt.Color(217, 217, 217));
        kembali.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        kembali.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        kembali.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        kembali.setEnabled(false);

        txt_k.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        txt_k.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txt_k.setText("Kembali :");

        txt_b.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        txt_b.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txt_b.setText("Bayar    :");

        bayar.setBackground(new java.awt.Color(217, 217, 217));
        bayar.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        bayar.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        bayar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        bayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bayarActionPerformed(evt);
            }
        });

        txt__total.setBackground(new java.awt.Color(217, 217, 217));
        txt__total.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        txt__total.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt__total.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txt__total.setEnabled(false);

        txt_t.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        txt_t.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txt_t.setText("Total     :");

        btnhapus.setBackground(new java.awt.Color(166, 104, 68));
        btnhapus.setText("Delete");
        btnhapus.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnhapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnhapusActionPerformed(evt);
            }
        });

        btnsimpan.setBackground(new java.awt.Color(166, 104, 68));
        btnsimpan.setText("Simpan");
        btnsimpan.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnsimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsimpanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelRound3Layout = new javax.swing.GroupLayout(panelRound3);
        panelRound3.setLayout(panelRound3Layout);
        panelRound3Layout.setHorizontalGroup(
            panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound3Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 668, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRound3Layout.createSequentialGroup()
                        .addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelRound3Layout.createSequentialGroup()
                                .addComponent(txt_b)
                                .addGap(33, 33, 33)
                                .addComponent(bayar, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelRound3Layout.createSequentialGroup()
                                .addComponent(txt_t)
                                .addGap(32, 32, 32)
                                .addComponent(txt__total, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelRound3Layout.createSequentialGroup()
                                .addComponent(txt_k)
                                .addGap(31, 31, 31)
                                .addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelRound3Layout.createSequentialGroup()
                                        .addComponent(btnsimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(12, 12, 12)
                                        .addComponent(btnhapus, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(kembali, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(24, 24, 24)))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        panelRound3Layout.setVerticalGroup(
            panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound3Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 495, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_t, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt__total, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_b, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bayar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_k, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(kembali, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49)
                .addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnsimpan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnhapus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel1.add(panelRound3, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 60, 720, 840));

        jLabel1.setFont(new java.awt.Font("Tahoma", 3, 36)); // NOI18N
        jLabel1.setText("Transaksi");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 250, 61));

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        add(mainPanel, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void tabelcariMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelcariMouseClicked
        // TODO add your handling code here:
        int baris = tabelcari.rowAtPoint(evt.getPoint());

        String ID = tabelcari.getValueAt(baris, 0).toString();
        txt_idbarang1.setText(ID);
        String nama = tabelcari.getValueAt(baris, 1).toString();
        txt_namamenu1.setText(nama);
        String harga = tabelcari.getValueAt(baris, 2).toString();
        txt_harga1.setText(harga);

    }//GEN-LAST:event_tabelcariMouseClicked

    private void bayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bayarActionPerformed
        int Total, Bayar, kembalian;
        Total = Integer.valueOf(txt__total.getText());
        Bayar = Integer.valueOf(bayar.getText());

        if (Total > Bayar) {
            JOptionPane.showMessageDialog(null, "Uang tidak cukup");
        } else {
            kembalian = Bayar - Total;
            kembali.setText(String.valueOf(kembalian));
        }

    }//GEN-LAST:event_bayarActionPerformed

    private void btnsimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsimpanActionPerformed
        // TODO add your handling code here:

        DefaultTableModel model = (DefaultTableModel) tabelnota.getModel();

        String noTransaksi = txt_notran.getText();
        String tanggal = txt_tgl.getText();
        int Total = Integer.parseInt(txt__total.getText());
        int Bayar = Integer.parseInt(bayar.getText());
        int Kembali = Integer.parseInt(kembali.getText());

        String simpantransaksi = "INSERT INTO transaksi (no_nota, tanggal, total, bayar, kembalian) VALUES (?, ?, ?, ?, ?)";
        try {
            Connection c = Koneksi.getKoneksi();
            PreparedStatement statement = c.prepareStatement(simpantransaksi);
            statement.setString(1, noTransaksi);
            statement.setString(2, tanggal);
            statement.setInt(3, Total);
            statement.setInt(4, Bayar);
            statement.setInt(5, Kembali);

            statement.execute();

        } catch (Exception e) {
            System.out.println("Simpan Penjualan Error");
        }

        try {
            
            Connection c = Koneksi.getKoneksi();
            int baris = tabelnota.getRowCount();

            for (int i = 0; i < baris; i++) {
                String sql = "INSERT INTO detail_transaksi(no_nota, id_menu, nama_menu, jumlah, harga, subtotal) VALUES ('"
                        + tabelnota.getValueAt(i, 0) + "','" + tabelnota.getValueAt(i, 1) + "','" + tabelnota.getValueAt(i, 2) + "','" + tabelnota.getValueAt(i, 3) + "','" + tabelnota.getValueAt(i, 4) + "','" + tabelnota.getValueAt(i, 5) + "')";
                PreparedStatement p = c.prepareStatement(sql);
                p.executeUpdate();

            }

            DefaultTableModel df = (DefaultTableModel) tabelnota.getModel();

            for (int i = 0; i < tabelnota.getRowCount(); i++) {
                String namamenu = df.getValueAt(i, 2).toString();
                String jumlah = df.getValueAt(i, 3).toString();
                String totalHarga = df.getValueAt(i, 5).toString();

                // Menentukan lebar tetap untuk kolom nama menu dan jumlah
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } 
        Connection conn = Koneksi.getKoneksi();
        String file = "src/IReport/cetakriwayat.jasper";
        String key = txt_notran.getText();

        try {

            HashMap parameter = new HashMap();
            parameter.put("nota", key);
            JasperPrint jp = JasperFillManager.fillReport(file, parameter, conn);
            JasperViewer viewer = new JasperViewer(jp, false);
            viewer.setVisible(true);
        } catch (JRException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            utama();
            autokode();
            kosong();
            clear();

        }


    }//GEN-LAST:event_btnsimpanActionPerformed

    private void btnhapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnhapusActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) tabelnota.getModel();
        int row = tabelnota.getSelectedRow();
        model.removeRow(row);
        totalbiaya();
        bayar.setText("0");
        kembali.setText("0");
    }//GEN-LAST:event_btnhapusActionPerformed

    private void txt_cariKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_cariKeyTyped
        cari();
    }//GEN-LAST:event_txt_cariKeyTyped

    private void txt_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_cariActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_cariActionPerformed

    private void txt_jumlah1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_jumlah1ActionPerformed
        // TODO add your handling code here:
        tambahTransaksi();
        bayar.setText("");
    }//GEN-LAST:event_txt_jumlah1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JTextField bayar;
    private swing.Button btnhapus;
    private swing.Button btnsimpan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JTextField kembali;
    private javax.swing.JPanel mainPanel;
    private panelCustom.PanelRound panelRound1;
    private panelCustom.PanelRound panelRound2;
    private panelCustom.PanelRound panelRound3;
    private javax.swing.JTable tabelcari;
    public javax.swing.JTable tabelnota;
    public javax.swing.JTextField txt__total;
    private javax.swing.JLabel txt_b;
    private textfield.TextField txt_cari;
    private textfield.TextField txt_harga1;
    private textfield.TextField txt_idbarang1;
    private textfield.TextField txt_jumlah1;
    private javax.swing.JLabel txt_k;
    private textfield.TextField txt_namamenu1;
    private javax.swing.JLabel txt_notran;
    private javax.swing.JLabel txt_t;
    private javax.swing.JLabel txt_tgl;
    // End of variables declaration//GEN-END:variables
}
