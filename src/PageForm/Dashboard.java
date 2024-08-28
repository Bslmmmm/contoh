/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PageForm;


import koneksi.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;


public class Dashboard extends javax.swing.JFrame {

    String Tanggal;
    private DefaultTableModel model;
    private DefaultTableModel modelRiwayat;

    public void updatedatariwayat() {
        modelRiwayat.getDataVector().removeAllElements();
        modelRiwayat.fireTableDataChanged();

        String selectedNoNota = (String) cmb_nota.getSelectedItem();
        if (selectedNoNota != null) {
            displayDataByNoNota(selectedNoNota);
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
        total.setText(String.valueOf(totalBiaya));
        txt_tampil.setText("Rp " + totalBiaya);
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

        String idMenuBaru = txt_idbarang.getText();
        String namaMenuBaru = txt_namamenu.getText();
        int jumlahBaru = Integer.parseInt(txt_jumlah.getText());
        int hargaBaru = Integer.parseInt(txt_harga.getText());
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
        txt_idbarang.setText("");
        txt_namamenu.setText("");
        txt_jumlah.setText("");
        txt_harga.setText("");
        autokode();
    }

    public void clear() {
        total.setText("0");
        bayar.setText("0");
        kembali.setText("0");
        txt_tampil.setText("0");
    }

    public void clear2() {
        txt_idbarang.setText("");
        txt_namamenu.setText("");
        txt_jumlah.setText("");
        txt_harga.setText("");
    }

    public void tambahTransaksi() {
        try {
            int jumlah, harga, total;

            // Validasi input
            if (txt_jumlah.getText().isEmpty() || txt_harga.getText().isEmpty()) {
                // Tampilkan pesan kesalahan atau lakukan tindakan yang sesuai
                JOptionPane.showMessageDialog(this, "Isi jumlah dan harga dengan angka.");
                return; // Keluar dari metode jika input tidak valid
            }

            // Konversi teks menjadi bilangan bulat
            jumlah = Integer.parseInt(txt_jumlah.getText());
            harga = Integer.parseInt(txt_harga.getText());

            // Hitung total
            total = jumlah * harga;

            // Tampilkan total di komponen total
            // Load data, hitung total biaya, dan bersihkan input
            loadData();
            totalbiaya();
            clear2();
            bayar.requestFocus();
        } catch (NumberFormatException e) {
            // Tangkap eksepsi NumberFormatException
            JOptionPane.showMessageDialog(this, "Input jumlah dan harga harus berupa angka.");
        }
    }

    public void kosong() {
        DefaultTableModel model = (DefaultTableModel) tabelnota.getModel();

        while (model.getRowCount() > 0) {
            model.removeRow(0);

        }
    }

    public Dashboard() {
        initComponents();
        init();

        //design column transaksi
        model = new DefaultTableModel();
        tabelnota.setModel(model);

        model.addColumn("No.");
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
        modelRiwayat = new DefaultTableModel();
        tabelriwayat.setModel(modelRiwayat);
        modelRiwayat.addColumn("No. Transaksi");
        modelRiwayat.addColumn("ID");
        modelRiwayat.addColumn("Nama Menu");
        modelRiwayat.addColumn("Jumlah");
        modelRiwayat.addColumn("Harga");
        modelRiwayat.addColumn("Total");

        loadData1();
        loadriwayat();
 
        //design header
        UIManager.put("TableHeader.background", new Color(217, 217, 217));
        JTableHeader hd = tabelnota.getTableHeader();
        hd.setBackground(new Color(217, 217, 217));

        //utama
        utama();
        Date date = new Date();
        SimpleDateFormat tanggal = new SimpleDateFormat("dd-MM-yyyy");

        txt_tgl.setText(tanggal.format(date));
        total.setText("0");
        bayar.setText("0");
        kembali.setText("0");
        txt_cari.requestFocus();

        //geseran tabel
        tabelnota.getColumnModel().getColumn(0).setPreferredWidth(20);
        tabelnota.getColumnModel().getColumn(1).setPreferredWidth(20);
        tabelnota.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabelnota.getColumnModel().getColumn(3).setPreferredWidth(10);
        tabelnota.getColumnModel().getColumn(4).setPreferredWidth(30);
        tabelnota.getColumnModel().getColumn(5).setPreferredWidth(30);

        //autokode
    }

    public void init() {
        setTime();
    }

    public void setTime() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Date date = new Date();
                    SimpleDateFormat tDateFormat = new SimpleDateFormat("h:mm:ss aa");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd-MM-yyyy");
                    String time = tDateFormat.format(date);
                    txt_time.setText(time.split(" ")[0] + " " + time.split(" ")[1]);
                    txt_date.setText(dateFormat.format(date));
                }
            }
        }).start();

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        logo_omkop = new javax.swing.JLabel();
        pn_datamaster = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        pn_transaksi = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        pn_close = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        pn_minuman = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        pn_makanan = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        pn_laporan = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        pn_pembayaran = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelnota = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        txt_t = new javax.swing.JLabel();
        txt_b = new javax.swing.JLabel();
        txt_k = new javax.swing.JLabel();
        bayar = new javax.swing.JTextField();
        btn_delete = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        total = new javax.swing.JTextField();
        kembali = new javax.swing.JTextField();
        btnSimpan = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        materialTabbed1 = new PageForm.materialTabbed();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabelcari = new javax.swing.JTable();
        txt_cari = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txt_tampil = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        txt_tgl = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txt_notran = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txt_idbarang = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txt_namamenu = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txt_jumlah = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txt_harga = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tabelriwayat = new javax.swing.JTable();
        jLabel21 = new javax.swing.JLabel();
        cmb_nota = new javax.swing.JComboBox<>();
        cetakriwayat = new swing.Button();
        jPanel5 = new javax.swing.JPanel();
        scroll_nota = new javax.swing.JScrollPane();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        btn_print = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        btn_inspect = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txt_time = new javax.swing.JLabel();
        txt_date = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(140, 74, 50));
        jPanel1.setPreferredSize(new java.awt.Dimension(1920, 1080));

        jPanel2.setBackground(new java.awt.Color(89, 47, 37));

        logo_omkop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icondashboard/Group 3 (1).png"))); // NOI18N

        pn_datamaster.setBackground(new java.awt.Color(89, 47, 37));
        pn_datamaster.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pn_datamaster.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pn_datamasterMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pn_datamasterMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pn_datamasterMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pn_datamasterMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pn_datamasterMouseReleased(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icondashboard/master-data 1.png"))); // NOI18N
        jLabel1.setText("Data Master");

        javax.swing.GroupLayout pn_datamasterLayout = new javax.swing.GroupLayout(pn_datamaster);
        pn_datamaster.setLayout(pn_datamasterLayout);
        pn_datamasterLayout.setHorizontalGroup(
            pn_datamasterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(pn_datamasterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pn_datamasterLayout.createSequentialGroup()
                    .addGap(40, 40, 40)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(40, 40, 40)))
        );
        pn_datamasterLayout.setVerticalGroup(
            pn_datamasterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 75, Short.MAX_VALUE)
            .addGroup(pn_datamasterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pn_datamasterLayout.createSequentialGroup()
                    .addGap(23, 23, 23)
                    .addComponent(jLabel1)
                    .addContainerGap(23, Short.MAX_VALUE)))
        );

        pn_transaksi.setBackground(new java.awt.Color(89, 47, 37));
        pn_transaksi.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pn_transaksi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pn_transaksiMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pn_transaksiMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pn_transaksiMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pn_transaksiMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pn_transaksiMouseReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icondashboard/cash_transaction_icon_194057 1.png"))); // NOI18N
        jLabel2.setText("Transaksi");

        javax.swing.GroupLayout pn_transaksiLayout = new javax.swing.GroupLayout(pn_transaksi);
        pn_transaksi.setLayout(pn_transaksiLayout);
        pn_transaksiLayout.setHorizontalGroup(
            pn_transaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(pn_transaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pn_transaksiLayout.createSequentialGroup()
                    .addGap(40, 40, 40)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(65, Short.MAX_VALUE)))
        );
        pn_transaksiLayout.setVerticalGroup(
            pn_transaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 75, Short.MAX_VALUE)
            .addGroup(pn_transaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pn_transaksiLayout.createSequentialGroup()
                    .addGap(23, 23, 23)
                    .addComponent(jLabel2)
                    .addContainerGap(23, Short.MAX_VALUE)))
        );

        pn_close.setBackground(new java.awt.Color(89, 47, 37));
        pn_close.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        pn_close.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pn_closeMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pn_closeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pn_closeMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pn_closeMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pn_closeMouseReleased(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icondashboard/close-button 1.png"))); // NOI18N
        jLabel9.setText("Close");

        javax.swing.GroupLayout pn_closeLayout = new javax.swing.GroupLayout(pn_close);
        pn_close.setLayout(pn_closeLayout);
        pn_closeLayout.setHorizontalGroup(
            pn_closeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_closeLayout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addComponent(jLabel9)
                .addContainerGap(76, Short.MAX_VALUE))
        );
        pn_closeLayout.setVerticalGroup(
            pn_closeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_closeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
                .addContainerGap())
        );

        pn_minuman.setBackground(new java.awt.Color(89, 47, 37));
        pn_minuman.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pn_minumanMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pn_minumanMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pn_minumanMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pn_minumanMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pn_minumanMouseReleased(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icondashboard/cashier-machine 1.png"))); // NOI18N
        jLabel4.setText("Cashier");

        javax.swing.GroupLayout pn_minumanLayout = new javax.swing.GroupLayout(pn_minuman);
        pn_minuman.setLayout(pn_minumanLayout);
        pn_minumanLayout.setHorizontalGroup(
            pn_minumanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_minumanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                .addGap(43, 43, 43))
        );
        pn_minumanLayout.setVerticalGroup(
            pn_minumanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_minumanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                .addContainerGap())
        );

        pn_makanan.setBackground(new java.awt.Color(89, 47, 37));
        pn_makanan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pn_makananMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pn_makananMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pn_makananMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pn_makananMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pn_makananMouseReleased(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icondashboard/notes 1.png"))); // NOI18N
        jLabel5.setText("Riwayat Transaksi");

        javax.swing.GroupLayout pn_makananLayout = new javax.swing.GroupLayout(pn_makanan);
        pn_makanan.setLayout(pn_makananLayout);
        pn_makananLayout.setHorizontalGroup(
            pn_makananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_makananLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pn_makananLayout.setVerticalGroup(
            pn_makananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_makananLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                .addContainerGap())
        );

        pn_laporan.setBackground(new java.awt.Color(89, 47, 37));
        pn_laporan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pn_laporan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pn_laporanMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pn_laporanMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pn_laporanMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pn_laporanMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pn_laporanMouseReleased(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icondashboard/cash_transaction_icon_194057 1.png"))); // NOI18N
        jLabel19.setText("Laporan");

        javax.swing.GroupLayout pn_laporanLayout = new javax.swing.GroupLayout(pn_laporan);
        pn_laporan.setLayout(pn_laporanLayout);
        pn_laporanLayout.setHorizontalGroup(
            pn_laporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_laporanLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pn_laporanLayout.setVerticalGroup(
            pn_laporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_laporanLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel19)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(logo_omkop)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pn_minuman, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pn_makanan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pn_laporan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pn_transaksi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pn_datamaster, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(pn_close, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 1, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(logo_omkop, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(pn_datamaster, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pn_transaksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pn_minuman, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pn_makanan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pn_laporan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pn_close, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(87, 87, 87))
        );

        pn_pembayaran.setBackground(new java.awt.Color(191, 137, 90));

        tabelnota.setBackground(new java.awt.Color(217, 198, 191));
        tabelnota.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Nama Menu", "Jumlah", "Harga"
            }
        ));
        jScrollPane1.setViewportView(tabelnota);

        txt_t.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        txt_t.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txt_t.setText("Total     :");

        txt_b.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        txt_b.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txt_b.setText("Bayar    :");

        txt_k.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        txt_k.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txt_k.setText("Kembali :");

        bayar.setBackground(new java.awt.Color(217, 217, 217));
        bayar.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        bayar.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        bayar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        bayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bayarActionPerformed(evt);
            }
        });

        btn_delete.setBackground(new java.awt.Color(255, 51, 51));
        btn_delete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_deleteMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_deleteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_deleteMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_deleteMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_deleteMouseReleased(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Hapus");

        javax.swing.GroupLayout btn_deleteLayout = new javax.swing.GroupLayout(btn_delete);
        btn_delete.setLayout(btn_deleteLayout);
        btn_deleteLayout.setHorizontalGroup(
            btn_deleteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 429, Short.MAX_VALUE)
            .addGroup(btn_deleteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btn_deleteLayout.createSequentialGroup()
                    .addContainerGap(172, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(167, Short.MAX_VALUE)))
        );
        btn_deleteLayout.setVerticalGroup(
            btn_deleteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 75, Short.MAX_VALUE)
            .addGroup(btn_deleteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btn_deleteLayout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        total.setBackground(new java.awt.Color(217, 217, 217));
        total.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        total.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        total.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        kembali.setBackground(new java.awt.Color(217, 217, 217));
        kembali.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        kembali.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        kembali.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnSimpan.setBackground(new java.awt.Color(51, 102, 255));
        btnSimpan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSimpan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSimpanMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSimpanMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSimpanMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnSimpanMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnSimpanMouseReleased(evt);
            }
        });

        jLabel7.setBackground(new java.awt.Color(51, 102, 255));
        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Simpan");

        javax.swing.GroupLayout btnSimpanLayout = new javax.swing.GroupLayout(btnSimpan);
        btnSimpan.setLayout(btnSimpanLayout);
        btnSimpanLayout.setHorizontalGroup(
            btnSimpanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnSimpanLayout.createSequentialGroup()
                .addContainerGap(74, Short.MAX_VALUE)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65))
        );
        btnSimpanLayout.setVerticalGroup(
            btnSimpanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnSimpanLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel7)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pn_pembayaranLayout = new javax.swing.GroupLayout(pn_pembayaran);
        pn_pembayaran.setLayout(pn_pembayaranLayout);
        pn_pembayaranLayout.setHorizontalGroup(
            pn_pembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_pembayaranLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(pn_pembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_t)
                    .addComponent(txt_b)
                    .addComponent(txt_k))
                .addGap(28, 28, 28)
                .addGroup(pn_pembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(kembali, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bayar, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pn_pembayaranLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(pn_pembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_delete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(129, 129, 129))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pn_pembayaranLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 668, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
            .addGroup(pn_pembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pn_pembayaranLayout.createSequentialGroup()
                    .addGap(22, 22, 22)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(401, Short.MAX_VALUE)))
        );
        pn_pembayaranLayout.setVerticalGroup(
            pn_pembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_pembayaranLayout.createSequentialGroup()
                .addGap(78, 78, 78)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 495, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(68, 68, 68)
                .addGroup(pn_pembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_t, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(pn_pembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bayar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_b, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addGroup(pn_pembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_k, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(kembali, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addComponent(btn_delete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
            .addGroup(pn_pembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pn_pembayaranLayout.createSequentialGroup()
                    .addGap(443, 443, 443)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(587, Short.MAX_VALUE)))
        );

        materialTabbed1.setBackground(new java.awt.Color(217, 217, 217));
        materialTabbed1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N

        jPanel3.setBackground(new java.awt.Color(217, 217, 217));

        jPanel4.setBackground(new java.awt.Color(166, 104, 68));

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
        jScrollPane3.setViewportView(tabelcari);

        txt_cari.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
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

        txt_tampil.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        txt_tampil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_tampilActionPerformed(evt);
            }
        });

        jPanel9.setBackground(new java.awt.Color(217, 198, 191));

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

        txt_idbarang.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txt_idbarang.setEnabled(false);

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Nama Menu");

        txt_namamenu.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txt_namamenu.setEnabled(false);

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Jumlah");

        txt_jumlah.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txt_jumlah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_jumlahActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Harga");

        txt_harga.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txt_harga.setEnabled(false);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addComponent(jLabel16)
                    .addComponent(jLabel15)
                    .addComponent(jLabel14)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_tgl, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_notran, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txt_idbarang, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                        .addComponent(txt_namamenu, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txt_jumlah, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txt_harga, javax.swing.GroupLayout.Alignment.LEADING)))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txt_tgl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txt_notran))
                .addGap(18, 18, 18)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_idbarang, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_namamenu, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_jumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_harga, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(81, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txt_tampil, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 508, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_cari)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(39, 39, 39))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(83, 83, 83)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 611, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(359, 359, 359)
                .addComponent(txt_tampil, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 1026, Short.MAX_VALUE)
        );

        materialTabbed1.addTab("    Cashier    ", jPanel3);

        jPanel8.setBackground(new java.awt.Color(166, 104, 68));

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

        jLabel21.setFont(new java.awt.Font("Tahoma", 3, 24)); // NOI18N
        jLabel21.setText("Riwayat Transaksi");

        cmb_nota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_notaActionPerformed(evt);
            }
        });

        cetakriwayat.setBackground(new java.awt.Color(191, 137, 90));
        cetakriwayat.setText("Cetak Transaksi");
        cetakriwayat.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        cetakriwayat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cetakriwayatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap(82, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 744, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmb_nota, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cetakriwayat, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(92, 92, 92))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(cmb_nota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 509, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(cetakriwayat, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        materialTabbed1.addTab("    Riwayat Transaksi    ", jPanel7);

        jPanel5.setBackground(new java.awt.Color(217, 217, 217));

        scroll_nota.setBackground(new java.awt.Color(217, 217, 217));
        scroll_nota.setBorder(null);

        jPanel6.setBackground(new java.awt.Color(166, 104, 68));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        btn_print.setBackground(new java.awt.Color(191, 137, 90));
        btn_print.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_print.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_printMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_printMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_printMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_printMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_printMouseReleased(evt);
            }
        });

        jLabel12.setBackground(new java.awt.Color(191, 137, 90));
        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Lihat Nota");

        javax.swing.GroupLayout btn_printLayout = new javax.swing.GroupLayout(btn_print);
        btn_print.setLayout(btn_printLayout);
        btn_printLayout.setHorizontalGroup(
            btn_printLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btn_printLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        btn_printLayout.setVerticalGroup(
            btn_printLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btn_printLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                .addContainerGap())
        );

        btn_inspect.setBackground(new java.awt.Color(191, 137, 90));
        btn_inspect.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_inspect.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_inspectMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_inspectMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_inspectMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_inspectMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_inspectMouseReleased(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Cetak");

        javax.swing.GroupLayout btn_inspectLayout = new javax.swing.GroupLayout(btn_inspect);
        btn_inspect.setLayout(btn_inspectLayout);
        btn_inspectLayout.setHorizontalGroup(
            btn_inspectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btn_inspectLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        btn_inspectLayout.setVerticalGroup(
            btn_inspectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btn_inspectLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel20.setFont(new java.awt.Font("Tahoma", 3, 24)); // NOI18N
        jLabel20.setText("Nota Pembelian");

        txt_time.setFont(new java.awt.Font("Tahoma", 0, 1)); // NOI18N
        txt_time.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        txt_date.setFont(new java.awt.Font("Tahoma", 0, 1)); // NOI18N
        txt_date.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(214, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane2)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addComponent(btn_print, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGap(18, 18, 18)
                            .addComponent(btn_inspect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel20))
                .addGap(183, 183, 183))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addComponent(txt_time, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(519, 519, 519)
                .addComponent(txt_date, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(jLabel20)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_print, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_inspect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_date, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_time, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(217, Short.MAX_VALUE))
        );

        scroll_nota.setViewportView(jPanel6);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scroll_nota, javax.swing.GroupLayout.DEFAULT_SIZE, 918, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(scroll_nota, javax.swing.GroupLayout.PREFERRED_SIZE, 823, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        materialTabbed1.addTab("    Nota    ", jPanel5);

        jLabel3.setFont(new java.awt.Font("Tahoma", 3, 36)); // NOI18N
        jLabel3.setText("Dashboard");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(materialTabbed1, javax.swing.GroupLayout.PREFERRED_SIZE, 923, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pn_pembayaran, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(materialTabbed1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(98, 98, 98))
            .addComponent(pn_pembayaran, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void pn_datamasterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_datamasterMouseClicked
        //datta master di klik
        DataMaster dm = new DataMaster();
        dm.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_pn_datamasterMouseClicked

    private void pn_datamasterMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_datamasterMouseEntered
        //data master di samper
        pn_datamaster.setBackground(new Color(140, 74, 50));
    }//GEN-LAST:event_pn_datamasterMouseEntered

    private void pn_datamasterMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_datamasterMouseExited
        //datamaster ditinggal
        pn_datamaster.setBackground(new Color(89, 47, 37));
    }//GEN-LAST:event_pn_datamasterMouseExited

    private void pn_datamasterMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_datamasterMousePressed
        // ditekan
        pn_datamaster.setBackground(new Color(166, 104, 68));
    }//GEN-LAST:event_pn_datamasterMousePressed

    private void pn_datamasterMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_datamasterMouseReleased
        // tidak ditekan
        pn_datamaster.setBackground(new Color(89, 47, 37));
    }//GEN-LAST:event_pn_datamasterMouseReleased

    private void pn_transaksiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_transaksiMouseClicked
        //transaksi dikilik
        //Dashboard db=new Dashboard();
        //db.setVisible(true);
    }//GEN-LAST:event_pn_transaksiMouseClicked

    private void pn_transaksiMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_transaksiMouseEntered
        //transaksi di samper
        pn_transaksi.setBackground(new Color(140, 74, 50));
    }//GEN-LAST:event_pn_transaksiMouseEntered

    private void pn_transaksiMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_transaksiMouseExited
        //transaksi di tinggal
        pn_transaksi.setBackground(new Color(89, 47, 37));
    }//GEN-LAST:event_pn_transaksiMouseExited

    private void pn_transaksiMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_transaksiMousePressed
        //ditekan
        pn_transaksi.setBackground(new Color(166, 104, 68));
    }//GEN-LAST:event_pn_transaksiMousePressed

    private void pn_transaksiMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_transaksiMouseReleased
        // tidak ditekan
        pn_transaksi.setBackground(new Color(89, 47, 37));
    }//GEN-LAST:event_pn_transaksiMouseReleased

    private void pn_closeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_closeMouseClicked
        // diklik
        System.exit(0);
    }//GEN-LAST:event_pn_closeMouseClicked

    private void pn_closeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_closeMouseEntered
        // disamper
        pn_close.setBackground(new Color(140, 74, 50));
    }//GEN-LAST:event_pn_closeMouseEntered

    private void pn_closeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_closeMouseExited
        // ditinggal
        pn_close.setBackground(new Color(89, 47, 37));
    }//GEN-LAST:event_pn_closeMouseExited

    private void pn_closeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_closeMousePressed
        // ditekan

        pn_close.setBackground(new Color(166, 104, 68));
    }//GEN-LAST:event_pn_closeMousePressed

    private void pn_closeMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_closeMouseReleased
        // tidak ditekan
        pn_close.setBackground(new Color(89, 47, 37));
    }//GEN-LAST:event_pn_closeMouseReleased

    private void pn_minumanMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_minumanMouseEntered
        // disamper
        pn_minuman.setBackground(new Color(140, 74, 50));
    }//GEN-LAST:event_pn_minumanMouseEntered

    private void pn_minumanMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_minumanMouseExited
        // ditinggal
        pn_minuman.setBackground(new Color(89, 47, 37));
    }//GEN-LAST:event_pn_minumanMouseExited

    private void pn_minumanMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_minumanMousePressed
        // diteken
        pn_minuman.setBackground(new Color(166, 104, 68));
    }//GEN-LAST:event_pn_minumanMousePressed

    private void pn_minumanMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_minumanMouseReleased
        // tidak ditekan
        pn_minuman.setBackground(new Color(89, 47, 37));
    }//GEN-LAST:event_pn_minumanMouseReleased

    private void pn_makananMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_makananMouseEntered
        // samper
        pn_makanan.setBackground(new Color(140, 74, 50));
    }//GEN-LAST:event_pn_makananMouseEntered

    private void pn_makananMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_makananMouseExited
        // ditinggal
        pn_makanan.setBackground(new Color(89, 47, 37));
    }//GEN-LAST:event_pn_makananMouseExited

    private void pn_makananMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_makananMousePressed
        // tekan
        pn_makanan.setBackground(new Color(166, 104, 68));
    }//GEN-LAST:event_pn_makananMousePressed

    private void pn_makananMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_makananMouseReleased
        // gaditekan
        pn_makanan.setBackground(new Color(89, 47, 37));
    }//GEN-LAST:event_pn_makananMouseReleased

    private void pn_minumanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_minumanMouseClicked
        // klik
        materialTabbed1.setSelectedIndex(0);
    }//GEN-LAST:event_pn_minumanMouseClicked

    private void pn_makananMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_makananMouseClicked
        // klik
        materialTabbed1.setSelectedIndex(1);
    }//GEN-LAST:event_pn_makananMouseClicked

    private void btn_printMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_printMouseClicked
        //klik
        try {
            jTextArea1.setFont(new Font("Courier New", Font.PLAIN, 12));
            StringBuilder notaText = new StringBuilder("-----------------------------------------------------------------------\n"
                    + "\t\t              OmahKopi888\n\n"
                    + "\tJl. Rasamala No.62, Baratan, Kec. Patrang, Kabupaten Jember\n"
                    + "\t\t            Jawa Timur 68112\n\n"
                    + "\t     Time = " + txt_time.getText() + " Date = " + txt_date.getText() + "\n\n"
                    + "------------------------------------------\n"
                    + "  Nama Menu \t\tJumlah \tHarga\n"
                    + "------------------------------------------\n");

            DefaultTableModel df = (DefaultTableModel) tabelnota.getModel();

            for (int i = 0; i < tabelnota.getRowCount(); i++) {
                String namamenu = df.getValueAt(i, 2).toString();
                String jumlah = df.getValueAt(i, 3).toString();
                String totalHarga = df.getValueAt(i, 5).toString();

                // Menentukan lebar tetap untuk kolom nama menu dan jumlah
                notaText.append(String.format("  %-20s\t%-5s\t%s\n", namamenu, jumlah, totalHarga));
            }

            notaText.append("------------------------------------------\n");
            notaText.append(" Subtotal  :").append("\t\t\t").append(total.getText()).append("\n");
            notaText.append(" Bayar     :").append("\t\t\t").append(bayar.getText()).append("\n");
            notaText.append(" Kembali   :").append("\t\t\t").append(kembali.getText()).append("\n\n");
            notaText.append("------------------------------------------\n");
            notaText.append("\t\t          Wifi = wifiyangmana\n\n");
            notaText.append("\t         Nikmati Kebersamaan Jaga kebersihan\n\n");
            notaText.append("\t\t           --Omahkopi888--\n\n");
            notaText.append("-----------------------------------------------------------------------\n");

            jTextArea1.setText(notaText.toString());
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_btn_printMouseClicked

    private void btn_printMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_printMouseEntered
        btn_print.setBackground(new Color(140, 74, 50));
    }//GEN-LAST:event_btn_printMouseEntered

    private void btn_printMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_printMouseExited
        btn_print.setBackground(new Color(191, 137, 90));
    }//GEN-LAST:event_btn_printMouseExited

    private void btn_printMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_printMousePressed
        btn_print.setBackground(new Color(130, 54, 40));
    }//GEN-LAST:event_btn_printMousePressed

    private void btn_printMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_printMouseReleased
        btn_print.setBackground(new Color(191, 137, 90));
    }//GEN-LAST:event_btn_printMouseReleased

    private void btn_inspectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_inspectMouseClicked
        try {

            jTextArea1.print();
        } catch (PrinterException e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_btn_inspectMouseClicked

    private void btn_inspectMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_inspectMouseEntered
        btn_inspect.setBackground(new Color(140, 74, 50));
    }//GEN-LAST:event_btn_inspectMouseEntered

    private void btn_inspectMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_inspectMouseExited
        btn_inspect.setBackground(new Color(191, 137, 90));
    }//GEN-LAST:event_btn_inspectMouseExited

    private void btn_inspectMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_inspectMousePressed
        btn_inspect.setBackground(new Color(130, 54, 40));
    }//GEN-LAST:event_btn_inspectMousePressed

    private void btn_inspectMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_inspectMouseReleased
        btn_inspect.setBackground(new Color(191, 137, 90));

    }//GEN-LAST:event_btn_inspectMouseReleased

    private void btnSimpanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSimpanMouseClicked
        DefaultTableModel model = (DefaultTableModel) tabelnota.getModel();

        String noTransaksi = txt_notran.getText();
        String tanggal = txt_tgl.getText();
        int Total = Integer.parseInt(total.getText());

        String simpantransaksi = "INSERT INTO transaksi (no_nota, tanggal, total) VALUES (?, ?, ?)";
        try {
            Connection c = Koneksi.getKoneksi();
            PreparedStatement statement = c.prepareStatement(simpantransaksi);
            statement.setString(1, noTransaksi);
            statement.setString(2, tanggal);
            statement.setInt(3, Total);
            statement.execute();

        } catch (Exception e) {
            System.out.println("Simpan Penjualan Error");
        }

        try {
            Connection c = Koneksi.getKoneksi();
            int baris = tabelnota.getRowCount();

            for (int i = 0; i < baris; i++) {
                String sql = "INSERT INTO detail_transaksi(no_nota, id_menu, nama_menu, jumlah, harga, total) VALUES ('"
                        + tabelnota.getValueAt(i, 0) + "','" + tabelnota.getValueAt(i, 1) + "','" + tabelnota.getValueAt(i, 2) + "','" + tabelnota.getValueAt(i, 3) + "','" + tabelnota.getValueAt(i, 4) + "','" + tabelnota.getValueAt(i, 5) + "')";
                PreparedStatement p = c.prepareStatement(sql);
                p.executeUpdate();

            }

            jTextArea1.setFont(new Font("Courier New", Font.PLAIN, 12));
            StringBuilder notaText = new StringBuilder("-----------------------------------------------------------------------\n"
                    + "\t\t              OmahKopi888\n\n"
                    + "\tJl. Rasamala No.62, Baratan, Kec. Patrang, Kabupaten Jember\n"
                    + "\t\t            Jawa Timur 68112\n\n"
                    + "\t     Time = " + txt_time.getText() + " Date = " + txt_date.getText() + "\n\n"
                    + "-----------------------------------------------------------------------\n"
                    + "  Nama Menu \t\t\t\t\tJumlah \tHarga\n"
                    + "-----------------------------------------------------------------------\n");

            DefaultTableModel df = (DefaultTableModel) tabelnota.getModel();

            for (int i = 0; i < tabelnota.getRowCount(); i++) {
                String namamenu = df.getValueAt(i, 2).toString();
                String jumlah = df.getValueAt(i, 3).toString();
                String totalHarga = df.getValueAt(i, 5).toString();

                // Menentukan lebar tetap untuk kolom nama menu dan jumlah
                notaText.append(String.format("  %-43s\t%-2s\t%s\n", namamenu, jumlah, totalHarga));
            }

            notaText.append("------------------------------------------\n");
            notaText.append(" Subtotal  :").append("\t\t\t").append(total.getText()).append("\n");
            notaText.append(" Bayar     :").append("\t\t\t").append(bayar.getText()).append("\n");
            notaText.append(" Kembali   :").append("\t\t\t").append(kembali.getText()).append("\n\n");
            notaText.append("------------------------------------------\n");
            notaText.append("\t\t          Wifi = wifiyangmana\n\n");
            notaText.append("\t         Nikmati Kebersamaan Jaga kebersihan\n\n");
            notaText.append("\t\t           --Omahkopi888--\n\n");
            notaText.append("-----------------------------------------------------------------------\n");

            jTextArea1.setText(notaText.toString());

            if (jTextArea1.print()) {
                new popup.popup_transaksiberhasil().setVisible(true);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } finally {
            utama();
            autokode();
            kosong();
            clear();
            updatedatariwayat();
        }

        txt_tampil.setText("Rp 0");

    }//GEN-LAST:event_btnSimpanMouseClicked

    private void btnSimpanMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSimpanMouseEntered
        //data master di samper
        btnSimpan.setBackground(new Color(3, 24, 255));
    }//GEN-LAST:event_btnSimpanMouseEntered

    private void btnSimpanMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSimpanMouseExited
        //datamaster ditinggal
        btnSimpan.setBackground(new Color(51, 102, 255));
    }//GEN-LAST:event_btnSimpanMouseExited

    private void btnSimpanMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSimpanMousePressed
        // ditekan
        btnSimpan.setBackground(new Color(2, 13, 212));
    }//GEN-LAST:event_btnSimpanMousePressed

    private void btnSimpanMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSimpanMouseReleased
        // tidak ditekan
        btnSimpan.setBackground(new Color(51, 102, 255));
    }//GEN-LAST:event_btnSimpanMouseReleased

    private void txt_jumlahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_jumlahActionPerformed
        tambahTransaksi();
        bayar.setText("");
    }//GEN-LAST:event_txt_jumlahActionPerformed

    private void btn_deleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_deleteMouseClicked
        DefaultTableModel model = (DefaultTableModel) tabelnota.getModel();
        int row = tabelnota.getSelectedRow();
        model.removeRow(row);
        totalbiaya();
        bayar.setText("0");
        kembali.setText("0");
    }//GEN-LAST:event_btn_deleteMouseClicked

    private void bayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bayarActionPerformed
        int Total, Bayar, kembalian;
        Total = Integer.valueOf(total.getText());
        Bayar = Integer.valueOf(bayar.getText());

        if (Total > Bayar) {
            JOptionPane.showMessageDialog(null, "Uang tidak cukup");
        } else {
            kembalian = Bayar - Total;
            kembali.setText(String.valueOf(kembalian));
        }

    }//GEN-LAST:event_bayarActionPerformed

    private void txt_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_cariActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_txt_cariActionPerformed

    private void tabelcariMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelcariMouseClicked
        // TODO add your handling code here:
        int baris = tabelcari.rowAtPoint(evt.getPoint());

        String ID = tabelcari.getValueAt(baris, 0).toString();
        txt_idbarang.setText(ID);
        String nama = tabelcari.getValueAt(baris, 1).toString();
        txt_namamenu.setText(nama);
        String harga = tabelcari.getValueAt(baris, 2).toString();
        txt_harga.setText(harga);

    }//GEN-LAST:event_tabelcariMouseClicked

    private void btn_deleteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_deleteMouseEntered
        // TODO add your handling code here:
        btn_delete.setBackground(new Color(214, 2, 2));
    }//GEN-LAST:event_btn_deleteMouseEntered

    private void btn_deleteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_deleteMouseExited
        // TODO add your handling code here:
        btn_delete.setBackground(new Color(255, 51, 51));
    }//GEN-LAST:event_btn_deleteMouseExited

    private void btn_deleteMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_deleteMousePressed
        // TODO add your handling code here:
        btn_delete.setBackground(new Color(181, 2, 2));
    }//GEN-LAST:event_btn_deleteMousePressed

    private void btn_deleteMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_deleteMouseReleased
        // TODO add your handling code here:
        btn_delete.setBackground(new Color(255, 51, 51));
    }//GEN-LAST:event_btn_deleteMouseReleased

    private void txt_cariKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_cariKeyTyped
        cari();
    }//GEN-LAST:event_txt_cariKeyTyped

    private void pn_laporanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_laporanMouseClicked
        //transaksi dikilik
        Laporan lp = new Laporan();
        lp.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_pn_laporanMouseClicked

    private void pn_laporanMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_laporanMouseEntered
        //transaksi di samper
        pn_laporan.setBackground(new Color(140, 74, 50));
    }//GEN-LAST:event_pn_laporanMouseEntered

    private void pn_laporanMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_laporanMouseExited
        //transaksi di tinggal
        pn_laporan.setBackground(new Color(89, 47, 37));
    }//GEN-LAST:event_pn_laporanMouseExited

    private void pn_laporanMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_laporanMousePressed
        //ditekan
        pn_laporan.setBackground(new Color(166, 104, 68));
    }//GEN-LAST:event_pn_laporanMousePressed

    private void pn_laporanMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_laporanMouseReleased
        // tidak ditekan
        pn_laporan.setBackground(new Color(89, 47, 37));
    }//GEN-LAST:event_pn_laporanMouseReleased

    private void cmb_notaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_notaActionPerformed
        // TODO add your handling code here:
        updatedatariwayat();
    }//GEN-LAST:event_cmb_notaActionPerformed

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

    private void txt_tampilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_tampilActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_tampilActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws UnsupportedLookAndFeelException {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Dashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JTextField bayar;
    private javax.swing.JPanel btnSimpan;
    private javax.swing.JPanel btn_delete;
    private javax.swing.JPanel btn_inspect;
    private javax.swing.JPanel btn_print;
    private swing.Button cetakriwayat;
    private javax.swing.JComboBox<String> cmb_nota;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    public javax.swing.JTextArea jTextArea1;
    public javax.swing.JTextField kembali;
    private javax.swing.JLabel logo_omkop;
    private PageForm.materialTabbed materialTabbed1;
    private javax.swing.JPanel pn_close;
    private javax.swing.JPanel pn_datamaster;
    private javax.swing.JPanel pn_laporan;
    private javax.swing.JPanel pn_makanan;
    private javax.swing.JPanel pn_minuman;
    private javax.swing.JPanel pn_pembayaran;
    private javax.swing.JPanel pn_transaksi;
    private javax.swing.JScrollPane scroll_nota;
    private javax.swing.JTable tabelcari;
    public javax.swing.JTable tabelnota;
    private javax.swing.JTable tabelriwayat;
    public javax.swing.JTextField total;
    private javax.swing.JLabel txt_b;
    private javax.swing.JTextField txt_cari;
    public javax.swing.JLabel txt_date;
    private javax.swing.JTextField txt_harga;
    private javax.swing.JTextField txt_idbarang;
    private javax.swing.JTextField txt_jumlah;
    private javax.swing.JLabel txt_k;
    private javax.swing.JTextField txt_namamenu;
    private javax.swing.JLabel txt_notran;
    private javax.swing.JLabel txt_t;
    private javax.swing.JTextField txt_tampil;
    private javax.swing.JLabel txt_tgl;
    public javax.swing.JLabel txt_time;
    // End of variables declaration//GEN-END:variables
}
