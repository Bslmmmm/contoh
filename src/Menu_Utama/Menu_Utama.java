package Menu_Utama;

import pages.Transaksi;
import pages.data_master;
import pages.pengeluaran;
import pages.homepage;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import PageForm.Login;
import pages.Riwayat_transaksi;
import java.lang.String;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import pages.Report_keuntungan;
import pages.Report_pengeluaran;

import scroll.ScrollBarCustomUI;

public class Menu_Utama extends javax.swing.JFrame {

    public Menu_Utama() {
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        pn_utama.add(new homepage());
        pn_utama.repaint();
        pn_utama.revalidate();

        execute();

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pn_navbar = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        pn_sidebar = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        pn_menu = new javax.swing.JPanel();
        pn_content = new javax.swing.JPanel();
        pn_utama = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        pn_navbar.setBackground(new java.awt.Color(89, 47, 37));

        jLabel1.setFont(new java.awt.Font("Tahoma", 2, 48)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icondashboard/logoomkop.png"))); // NOI18N

        javax.swing.GroupLayout pn_navbarLayout = new javax.swing.GroupLayout(pn_navbar);
        pn_navbar.setLayout(pn_navbarLayout);
        pn_navbarLayout.setHorizontalGroup(
            pn_navbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_navbarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(869, Short.MAX_VALUE))
        );
        pn_navbarLayout.setVerticalGroup(
            pn_navbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_navbarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(pn_navbar, java.awt.BorderLayout.PAGE_START);

        pn_sidebar.setPreferredSize(new java.awt.Dimension(320, 474));

        jScrollPane1.setBorder(null);

        pn_menu.setBackground(new java.awt.Color(140, 74, 50));
        pn_menu.setLayout(new javax.swing.BoxLayout(pn_menu, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane1.setViewportView(pn_menu);

        javax.swing.GroupLayout pn_sidebarLayout = new javax.swing.GroupLayout(pn_sidebar);
        pn_sidebar.setLayout(pn_sidebarLayout);
        pn_sidebarLayout.setHorizontalGroup(
            pn_sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
        );
        pn_sidebarLayout.setVerticalGroup(
            pn_sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
        );

        getContentPane().add(pn_sidebar, java.awt.BorderLayout.LINE_START);

        pn_utama.setBackground(new java.awt.Color(255, 255, 255));
        pn_utama.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout pn_contentLayout = new javax.swing.GroupLayout(pn_content);
        pn_content.setLayout(pn_contentLayout);
        pn_contentLayout.setHorizontalGroup(
            pn_contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pn_utama, javax.swing.GroupLayout.DEFAULT_SIZE, 637, Short.MAX_VALUE)
        );
        pn_contentLayout.setVerticalGroup(
            pn_contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pn_utama, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
        );

        getContentPane().add(pn_content, java.awt.BorderLayout.CENTER);

        setSize(new java.awt.Dimension(957, 564));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(Menu_Utama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Menu_Utama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Menu_Utama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Menu_Utama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Menu_Utama().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pn_content;
    private javax.swing.JPanel pn_menu;
    private javax.swing.JPanel pn_navbar;
    private javax.swing.JPanel pn_sidebar;
    private javax.swing.JPanel pn_utama;
    // End of variables declaration//GEN-END:variables

    private void execute() {

        ImageIcon iconHome = new ImageIcon(getClass().getResource("/icondashboard/home-alt-2-solid-24.png"));
        ImageIcon iconMaster = new ImageIcon(getClass().getResource("/icondashboard/master-data 1.png"));
        ImageIcon iconTransaksi = new ImageIcon(getClass().getResource("/icondashboard/cash_transaction_icon_194057 1.png"));
        ImageIcon iconReport = new ImageIcon(getClass().getResource("/icondashboard/notes 1.png"));
        ImageIcon iconpengeluaran = new ImageIcon(getClass().getResource("/icondashboard/inventory 1.png"));
        ImageIcon iconkasir = new ImageIcon(getClass().getResource("/icondashboard/cashier-machine 1.png"));
        ImageIcon iconclose = new ImageIcon(getClass().getResource("/icondashboard/close-button 1.png"));

        MenuItem masBarang1 = new MenuItem(null, true, null, "Data Menu", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {

                pn_utama.removeAll();
                pn_utama.add(new data_master());
                pn_utama.repaint();
                pn_utama.revalidate();

            }
        });
        MenuItem masBarang2 = new MenuItem(null, true, null, "Pengeluaran", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                pn_utama.removeAll();
                pn_utama.add(new pengeluaran());
                pn_utama.repaint();
                pn_utama.revalidate();
            }
        });

        MenuItem transaksi1 = new MenuItem(null, true, null, "Menu", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                pn_utama.removeAll();
                pn_utama.add(new Transaksi());
                pn_utama.repaint();
                pn_utama.revalidate();

            }
        });

        MenuItem menuHome = new MenuItem(iconHome, false, null, "Home", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                pn_utama.removeAll();
                pn_utama.add(new homepage());
                pn_utama.repaint();
                pn_utama.revalidate();
            }
        });
        MenuItem menuRiwayat = new MenuItem(null, true, null, "Riwayat Transaksi", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                pn_utama.removeAll();
                pn_utama.add(new Riwayat_transaksi());
                pn_utama.repaint();
                pn_utama.revalidate();
            }
        });
        
        MenuItem menuKeuntungan = new MenuItem(null, true, null, "Keuntungan", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                pn_utama.removeAll();
                try {
                    pn_utama.add(new Report_keuntungan());
                } catch (SQLException ex) {
                    Logger.getLogger(Menu_Utama.class.getName()).log(Level.SEVERE, null, ex);
                }
                pn_utama.repaint();
                pn_utama.revalidate();
              
            }
        });
        
        MenuItem menuPengeluaran = new MenuItem(null, true, null, "Pengeluaran", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                pn_utama.removeAll();
                pn_utama.add(new Report_pengeluaran());
                pn_utama.repaint();
                pn_utama.revalidate();
               
            }
        });
        
        MenuItem menuMaster = new MenuItem(iconMaster, false, null, "Master", null, masBarang1, masBarang2);
        MenuItem menuTransaksi = new MenuItem(iconkasir, false, null, "Transaksi", null, transaksi1);
        MenuItem menuReport = new MenuItem(iconReport, false, null, "Report", null, menuRiwayat, menuKeuntungan, menuPengeluaran);
        MenuItem menuKeluar = new MenuItem(iconclose, false, null, "Log Out", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                dispose();
                new Login().setVisible(true);
            }
        });

        addMenu(menuHome, menuMaster, menuTransaksi, menuReport, menuKeluar);
    }

    private void addMenu(MenuItem... menu) {
        for (int i = 0; i < menu.length; i++) {
            pn_menu.add(menu[i]);
            ArrayList<MenuItem> subMenu = menu[i].getSubMenu();
            for (MenuItem m : subMenu) {
                addMenu(m);
            }
        }

        pn_menu.revalidate();
    }

}
