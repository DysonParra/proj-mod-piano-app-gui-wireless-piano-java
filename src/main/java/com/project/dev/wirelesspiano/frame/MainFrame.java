/*
 * @fileoverview {MainFrame} se encarga de realizar tareas especificas.
 *
 * @version             1.0
 *
 * @author              Dyson Arley Parra Tilano <dysontilano@gmail.com>
 * Copyright (C) Dyson Parra
 *
 * @History v1.0 --- La implementacion de {MainFrame} fue realizada el 31/07/2022.
 * @Dev - La primera version de {MainFrame} fue escrita por Dyson A. Parra T.
 */
package com.project.dev.wirelesspiano.frame;

import com.project.dev.Application;
import com.project.dev.wirelesspiano.struct.Piano;
import com.project.dev.wirelesspiano.struct.UdpServer;
import java.awt.Color;
import java.awt.HeadlessException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import static com.project.dev.wirelesspiano.WirelessPianoConstant.PIANO_ICON_RES_PATH;

/**
 * TODO: Definición de {@code MainFrame}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
public class MainFrame extends JFrame {

    private static int pianoQuantity = 0;                                       // Cantidad de pianos locales creados.

    /**
     * TODO: Definición de {@code MainFrame}.
     *
     */
    public MainFrame() {
        initComponents();

        initComponents();
        this.setIconImage(
                new ImageIcon(JFrame.class.getResource(PIANO_ICON_RES_PATH)).getImage());

        this.setTitle("MENÚ PRINCIPAL");
        this.getContentPane().setBackground(Color.WHITE);

        String serverPortStr;
        int serverPort;

        while (Application.serverPort <= 0) {
            try {
                serverPortStr = JOptionPane.showInputDialog("Escriba un puerto de conexión");

                if (serverPortStr == null)
                    System.exit(0);

                serverPort = Integer.parseInt(serverPortStr);

                if (serverPort <= 0) {
                    JOptionPane.showMessageDialog(null, "El puerto debe ser un número mayor que cero");
                    continue;
                }

                Application.udpServer = new UdpServer(serverPort, 89);        // Inicializa el servidor udp.
                Application.serverPort = serverPort;                          // Almacena el puerto de conexión en la clase principal.
            } catch (HeadlessException | NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Escriba un puerto válido");
            } catch (SocketException e) {
                JOptionPane.showMessageDialog(null, "El puerto escogido no está disponible");
            }
        }

    }

    /**
     * FIXME: Definición de {@code createPianoFrame}. Crea una nueva ventana con un piano. 17 teclas
     * = 1 Octava
     *
     * @param pianoTitle   indica la dirección ip o número del piano.
     * @param keyQuantity  indica la cantidad de teclas del piano.
     * @param C4           indica el número de do que será el do central.
     * @param instrumentId indica el número de instrumento con que sonará cada tecla.
     * @param remote       indica si es un piano remoto.
     */
    public static void createPianoFrame(String pianoTitle, int keyQuantity, int C4, int instrumentId, boolean remote) {
        PianoFrame pianoFrame = new PianoFrame();                               // Crea una nueva ventana con un piano.
        Piano piano = new Piano(keyQuantity, C4, instrumentId);                 // Crea un uevo piano.

        pianoFrame.makePianoFrame(pianoTitle, remote, piano);                   // Inicializa la ventana con un piano.
        pianoFrame.setVisible(true);                                            // Pone visible la ventana con el piano.

        Application.pianoFrames.put(pianoTitle, pianoFrame);                  // Agrega la ventana con el piano en el map de la clase principal.
    }

    /**
     * FIXME: Definición de {@code getEnableAllConnections}. Obtiene el estado del botón de permitir
     * conexiones.
     *
     * @return Si se pemiten conexiones.
     */
    public boolean getEnableAllConnections() {
        return enableAllConnectionsItem.getState();                             // Obtiene el estado del botón de permitir conexiones.
    }

    /**
     * FIXME: Definición de {@code getDisableNewConnections}. Obtiene el estado del botón de
     * permitir nuevas conexiones.
     *
     * @return Si se pemiten conexiones.
     */
    public boolean getDisableNewConnections() {
        return disableNewConnectionsItem.getState();                            // Obtiene el estado del botón de permitir nuevas conexiones.
    }

    /**
     * FIXME: Definición de {@code getDisableAllConnections}. Obtiene el estado del botón de
     * deshabilitar todas las conexiones
     *
     * @return Si se pemiten conexiones.
     */
    public boolean getDisableAllConnections() {
        return disableAllConnectionsItem.getState();                            // Obtiene el estado del botón de deshabilitar todas las conexiones.
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        newLocalPianoButton = new javax.swing.JButton();
        ipAddress = new javax.swing.JLabel();
        serverPort = new javax.swing.JLabel();
        mainMenu = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newPianoLocalMenu = new javax.swing.JMenu();
        yamahaPianoItem = new javax.swing.JMenuItem();
        electricPianoItem = new javax.swing.JMenuItem();
        exitItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        enableAllConnectionsItem = new javax.swing.JCheckBoxMenuItem();
        disableNewConnectionsItem = new javax.swing.JCheckBoxMenuItem();
        disableAllConnectionsItem = new javax.swing.JCheckBoxMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        newLocalPianoButton.setText("Nuevo piano local");
        newLocalPianoButton.addActionListener(this::newLocalPianoButtonActionPerformed);

        ipAddress.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ipAddress.setText("No se permiten nuevas conexiones");

        serverPort.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        serverPort.setText("Puerto sin asignar");

        fileMenu.setText("Archivo");

        newPianoLocalMenu.setText("Nuevo piano local");

        yamahaPianoItem.setText("Piano Yamaha");
        yamahaPianoItem.addActionListener(this::yamahaPianoItemActionPerformed);
        newPianoLocalMenu.add(yamahaPianoItem);

        electricPianoItem.setText("Piano Eléctrico");
        electricPianoItem.addActionListener(this::electricPianoItemActionPerformed);
        newPianoLocalMenu.add(electricPianoItem);

        fileMenu.add(newPianoLocalMenu);

        exitItem.setText("Salir");
        exitItem.addActionListener(this::exitItemActionPerformed);
        fileMenu.add(exitItem);

        mainMenu.add(fileMenu);

        editMenu.setText("Editar");

        enableAllConnectionsItem.setText("Permitir conexiones");
        enableAllConnectionsItem.addActionListener(this::enableAllConnectionsItemActionPerformed);
        editMenu.add(enableAllConnectionsItem);

        disableNewConnectionsItem.setSelected(true);
        disableNewConnectionsItem.setText("No permitir nuevas conexiones");
        disableNewConnectionsItem.addActionListener(this::disableNewConnectionsItemActionPerformed);
        editMenu.add(disableNewConnectionsItem);

        disableAllConnectionsItem.setText("Deshabilitar todas las conexiones");
        disableAllConnectionsItem.addActionListener(this::disableAllConnectionsItemActionPerformed);
        editMenu.add(disableAllConnectionsItem);

        mainMenu.add(editMenu);

        setJMenuBar(mainMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(ipAddress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(serverPort, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(96, Short.MAX_VALUE)
                                .addComponent(newLocalPianoButton)
                                .addGap(95, 95, 95))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addComponent(newLocalPianoButton)
                                .addGap(31, 31, 31)
                                .addComponent(ipAddress)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(serverPort, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(58, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * TODO: Definición de {@code enableAllConnectionsItemActionPerformed}.
     *
     * @param evt
     */
    private void enableAllConnectionsItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enableAllConnectionsItemActionPerformed

        enableAllConnectionsItem.setState(true);                                // Habilita el botón de permitir conexiones.
        disableNewConnectionsItem.setState(false);                              // Deshabilita el botón de permitir nuevas conexiones.
        disableAllConnectionsItem.setState(false);                              // Deshabilita el botón de deshabilitar todas las conexiones.

        try {
            DatagramSocket socket = new DatagramSocket();
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ipAddress.setText("Conexión en: " + socket.getLocalAddress().getHostAddress());
            serverPort.setText("Puerto: " + Application.serverPort);
        } catch (Exception e) {

        }

    }//GEN-LAST:event_enableAllConnectionsItemActionPerformed

    /**
     * TODO: Definición de {@code disableNewConnectionsItemActionPerformed}.
     *
     * @param evt
     */
    private void disableNewConnectionsItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disableNewConnectionsItemActionPerformed

        enableAllConnectionsItem.setState(false);                               // Deshabilita el botón de permitir conexiones.
        disableNewConnectionsItem.setState(true);                               // Habilita el botón de permitir nuevas conexiones.
        disableAllConnectionsItem.setState(false);                              // Deshabilita el botón de deshabilitar todas las conexiones.

        ipAddress.setText("No se permiten nuevas conexiones");
        serverPort.setText("Puerto: " + Application.serverPort);
    }//GEN-LAST:event_disableNewConnectionsItemActionPerformed

    /**
     * TODO: Definición de {@code disableAllConnectionsItemActionPerformed}.
     *
     * @param evt
     */
    private void disableAllConnectionsItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disableAllConnectionsItemActionPerformed

        enableAllConnectionsItem.setState(false);                               // Deshabilita el botón de permitir conexiones.
        disableNewConnectionsItem.setState(false);                              // Deshabilita el botón de permitir nuevas conexiones.
        disableAllConnectionsItem.setState(true);                               // Habilita el botón de deshabilitar todas las conexiones.

        ipAddress.setText("Pianos remotos deshabilitados");
        serverPort.setText("Puerto sin asignar");
    }//GEN-LAST:event_disableAllConnectionsItemActionPerformed

    /**
     * TODO: Definición de {@code yamahaPianoItemActionPerformed}.
     *
     * @param evt
     */
    private void yamahaPianoItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yamahaPianoItemActionPerformed

        createPianoFrame(String.valueOf(++pianoQuantity), 89, 4, 0, false);     // Crea un nuevo piano yamaha.
    }//GEN-LAST:event_yamahaPianoItemActionPerformed

    /**
     * TODO: Definición de {@code electricPianoItemActionPerformed}.
     *
     * @param evt
     */
    private void electricPianoItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_electricPianoItemActionPerformed
        createPianoFrame(String.valueOf(++pianoQuantity), 89, 4, 1, false);     // Crea un nuevo piano eléctrico.
    }//GEN-LAST:event_electricPianoItemActionPerformed

    /**
     * TODO: Definición de {@code exitItemActionPerformed}.
     *
     * @param evt
     */
    private void exitItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitItemActionPerformed

    /**
     * TODO: Definición de {@code newLocalPianoButtonActionPerformed}.
     *
     * @param evt
     */
    private void newLocalPianoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newLocalPianoButtonActionPerformed
        createPianoFrame(String.valueOf(++pianoQuantity), 89, 4, 0, false);     // Crea un nuevo piano yamaha.
    }//GEN-LAST:event_newLocalPianoButtonActionPerformed

    /**
     * Entrada principal del sistema.
     *
     * @param args argumentos de la linea de comandos.
     */
    public static void main(String args[]) {
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBoxMenuItem disableAllConnectionsItem;
    private javax.swing.JCheckBoxMenuItem disableNewConnectionsItem;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem electricPianoItem;
    private javax.swing.JCheckBoxMenuItem enableAllConnectionsItem;
    private javax.swing.JMenuItem exitItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JLabel ipAddress;
    private javax.swing.JMenuBar mainMenu;
    private javax.swing.JButton newLocalPianoButton;
    private javax.swing.JMenu newPianoLocalMenu;
    private javax.swing.JLabel serverPort;
    private javax.swing.JMenuItem yamahaPianoItem;
    // End of variables declaration//GEN-END:variables
}
