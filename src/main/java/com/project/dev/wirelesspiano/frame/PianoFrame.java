/*
 * @fileoverview    {PianoFrame}
 *
 * @version         2.0
 *
 * @author          Dyson Arley Parra Tilano <dysontilano@gmail.com>
 *
 * @copyright       Dyson Parra
 * @see             github.com/DysonParra
 *
 * History
 * @version 1.0     Implementation done.
 * @version 2.0     Documentation added.
 */
package com.project.dev.wirelesspiano.frame;

import com.project.dev.Application;
import com.project.dev.wirelesspiano.WirelessPianoConstant;
import com.project.dev.wirelesspiano.struct.Piano;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;

import static com.project.dev.wirelesspiano.WirelessPianoConstant.PIANO_ICON_RES_PATH;

/**
 * TODO: Description of {@code PianoFrame}.
 *
 * @author Dyson Parra
 * @since Java 17 (LTS), Gradle 7.3
 */
public final class PianoFrame extends JFrame implements WirelessPianoConstant, KeyListener, ActionListener {

    /*
     * Variables locales.
     */
    public JLayeredPane pianoPane;                                          // Teclas del piano.
    public JScrollPane pianoScrollPane;                                     // Panel con las teclas del piano.
    public Piano piano;                                                     // Piano de la ventana.
    public int currentDo;                                                   // Do actual que tiene asignado la tecla 'd' del teclado.
    public String title;                                                    // Título del JFrame.
    public boolean isRemote;                                                // Si es un piano remoto.
    public int keyQuantity;                                                 // Cantidad de teclas del piano.
    public int C4;                                                          // Posición del do central del piano.
    public int instrumentId;                                                // Id del intrumento con que sonará cada tecla.
    public int C4Offset;                                                    // Diferencia de posiciones entre el do central en el array de notas y en el array de midi.
    public int index;                                                       // Usada para Inicializar los evento de mouse de cada tecla.
    public byte[] status;                                                   // Estados de cada una de las teclas.
    public JLabel[] keys;                                                   // Cada una de las teclas.

    /*
     * Obtiene el alcho y alto de cada tecla y de la pantalla.
     */
    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();   // Obtiene la resolución de la pantalla.
    private final int screenWith = screenSize.width;                                     // Obtiene el ancho de la pantalla.
    private final int screenHeight = screenSize.height;                                  // Obtiene el alto de la pantalla.
    private int whiteKeyHeigth = 250;                                                   // Asigna valor al alto de las teclas blancas.
    private int whiteKeyWith = (int) (whiteKeyHeigth * 0.2);                            // Asigna valor al ancho de las teclas blancas.
    private int blackKeyHeigth = (int) (whiteKeyHeigth * 0.4);                            // Asigna valor al alto de las teclas negras.
    private int blackKeyWith = (int) (whiteKeyHeigth * 0.1);                            // Asigna valor al ancho de las teclas negras.

    /*
     * Creación de las imágenes y de los iconos de las imágenes.
     */
    private static final ImageIcon negraImg = new ImageIcon(Application.class.getResource(IMG_NEGRA_RES_PATH));
    private static final ImageIcon negraTcdImg = new ImageIcon(Application.class.getResource(IMG_NEGRA_TOUCH_RES_PATH));
    private static final ImageIcon blancaImg = new ImageIcon(Application.class.getResource(IMG_BLANCA_RES_PATH));
    private static final ImageIcon blancaTcdImg = new ImageIcon(Application.class.getResource(IMG_BLANCA_TOUCH_RES_PATH));
    private static final ImageIcon blancaC4Img = new ImageIcon(Application.class.getResource(IMG_BLANCA_C4_RES_PATH));
    private static final ImageIcon blancaC4TcdImg = new ImageIcon(Application.class.getResource(IMG_BLANCA_C4_TOUCH_RES_PATH));
    private static Icon negraIcon;
    private static Icon negraTcdIcon;
    private static Icon blancaIcon;
    private static Icon blancaC4Icon;
    private static Icon blancaTcdIcon;
    private static Icon blancaC4TcdIcon;

    /**
     * FIXME: Description of {@code PianoFrame}. Inicaliza los componentes de un PianoFrame.
     *
     */
    public PianoFrame() {
        initComponents();                                                       // Inicia los componentes.
        this.setIconImage(
                new ImageIcon(PianoFrame.class.getResource(PIANO_ICON_RES_PATH)).getImage());
    }

    /**
     * FIXME: Description of {@code makePianoFrame}. Crea un nuevo frame con un piano.
     *
     * @param title    es el título del piano.
     * @param isRemote indica si es un piano remoto.
     * @param piano    es el piano asociado al frame actual.
     */
    public void makePianoFrame(String title, boolean isRemote, Piano piano) {
        this.title = title;                                                     // Obtiene el título del piano.
        this.setTitle(title);                                                   // Asigna titulo al piano actual.
        this.isRemote = isRemote;                                               // Obtiene si es un piano remoto.
        this.piano = piano;                                                     // Obtiene piano.
        this.keyQuantity = piano.getKeyQuantity();                              // Obtiene la cantidad de notas.
        this.C4 = piano.getC4();                                                // Obtiene la posición del do central.
        this.instrumentId = piano.getInstrumentId();                            // Obtiene el id del instrumento.
        this.status = piano.getStatus();                                        // Obtiene los estados de cada tecla.
        this.keys = piano.getKeys();                                            // Obtiene las teclas del piano.
        currentDo = 4;                                                          // Inicializa el do actual que tiene asignado la tecla 'd' del teclado.
        C4Offset = 60 - this.C4;                                                // Asigna valor desde el do central en el array de teclas hasta el do central en el array de midi.

        this.addKeyListener(this);                                              // Agrega un listener de teclas.

        initKeys(isRemote);                                                     // Inicializa las teclas.
        setSizes();                                                             // Asigna tamaños a la ventana y al piano.
    }

    /**
     * FIXME: Description of {@code initKeys}. Inicializa las notas en pantalla.
     *
     * @param isRemote indica si es un piano remoto.
     */
    private void initKeys(boolean isRemote) {
        pianoPane = new JLayeredPane();                                         // Inicializa el panel donde irán las teclas del piano.
        pianoPane.setLocation(0, 0);                                            // Asigna posición al panel.
        pianoScrollPane = new JScrollPane(pianoPane);                           // Inicializa el jsrollPane donde irá el panel con las teclas del piano.
        pianoScrollPane.setLocation(0, 0);                                      // Asigna posición al jsrollPane.

        if (isRemote) {                                                         // Si es un piano remoto.
            this.comeBack.setEnabled(false);                                    // Deshabilita el botón de volver al menú principal.
            this.selectC.setEnabled(false);                                     // Deshabilita el menú de escoger octava.
        }

        int keyQuant = piano.getKeyQuantity();                                  // Obtiene la cantidad de notas del piano.

        for (index = 1; index < piano.getKeyQuantity(); index++) {
            status[index] = 0;
            keys[index] = new JLabel();

            switch (index % 12) {
                case 2:
                case 5:
                case 7:
                case 10:
                case 0:
                    if (index != keyQuant - 1) {
                        pianoPane.add(keys[index]);
                        if (!isRemote) {                                         // Si no es un piano remoto.
                            keys[index].addMouseListener(new MouseAdapter() {
                                int notePos = index;

                                @Override
                                public void mousePressed(MouseEvent ev) {
                                    if (status[notePos] == 0) {
                                        status[notePos] = 1;
                                        keys[notePos].setIcon(negraTcdIcon);
                                        piano.getMidiChannels()[0].noteOn(notePos + C4Offset, 100);
                                    }
                                }

                                @Override
                                public void mouseReleased(MouseEvent ev) {
                                    piano.getStatus()[notePos] = 0;
                                    keys[notePos].setIcon(negraIcon);
                                    piano.getMidiChannels()[0].noteOff(notePos + C4Offset);
                                }
                            });
                        }
                    }
                    break;

                default:
                    break;
            }
        }

        for (index = 0; index < keyQuant; index++) {
            switch (index % 12) {
                case 1:
                case 3:
                case 4:
                case 6:
                case 8:
                case 9:
                case 11:
                case 13:
                    pianoPane.add(keys[index]);
                    if (!isRemote) {                                             // Si no es un piano remoto.
                        keys[index].addMouseListener(new MouseAdapter() {
                            int notePos = index;

                            @Override
                            public void mousePressed(MouseEvent ev) {
                                if (status[notePos] == 0) {
                                    if (C4 == notePos)
                                        keys[notePos].setIcon(blancaC4TcdIcon);
                                    else
                                        keys[notePos].setIcon(blancaTcdIcon);

                                    status[notePos] = 1;
                                    piano.getMidiChannels()[0].noteOn(notePos + C4Offset, 100);
                                }
                            }

                            @Override
                            public void mouseReleased(MouseEvent ev) {
                                status[notePos] = 0;

                                if (C4 == notePos)
                                    keys[notePos].setIcon(blancaC4Icon);
                                else
                                    keys[notePos].setIcon(blancaIcon);
                                piano.getMidiChannels()[0].noteOff(notePos + C4Offset);
                            }
                        });
                    }
                    break;

                default:
                    break;
            }
        }
    }

    /**
     * FIXME: Description of {@code relloadIcons}. Reescala las notas al tamaño asignado.
     *
     */
    public void relloadIcons() {
        negraIcon = new ImageIcon(negraImg.getImage().getScaledInstance(blackKeyWith, blackKeyHeigth, Image.SCALE_DEFAULT));
        negraTcdIcon = new ImageIcon(negraTcdImg.getImage().getScaledInstance(blackKeyWith, blackKeyHeigth, Image.SCALE_DEFAULT));
        blancaIcon = new ImageIcon(blancaImg.getImage().getScaledInstance(whiteKeyWith, whiteKeyHeigth, Image.SCALE_DEFAULT));
        blancaC4Icon = new ImageIcon(blancaC4Img.getImage().getScaledInstance(whiteKeyWith, whiteKeyHeigth, Image.SCALE_DEFAULT));
        blancaTcdIcon = new ImageIcon(blancaTcdImg.getImage().getScaledInstance(whiteKeyWith, whiteKeyHeigth, Image.SCALE_DEFAULT));
        blancaC4TcdIcon = new ImageIcon(blancaC4TcdImg.getImage().getScaledInstance(whiteKeyWith, whiteKeyHeigth, Image.SCALE_DEFAULT));
    }

    /**
     * FIXME: Description of {@code setSizes}. Asigna tamaños a cada tecla.
     *
     */
    public void setSizes() {
        this.whiteKeyWith = (int) (whiteKeyHeigth * 0.2);                       // Asigna valor al ancho de las teclas blancas.
        this.blackKeyHeigth = (int) (whiteKeyHeigth * 0.4);                       // Asigna valor al alto de las teclas negras.
        this.blackKeyWith = (int) (whiteKeyHeigth * 0.1);                       // Asigna valor al ancho de las teclas negras.

        relloadIcons();

        int nroBlancas = 0;                                                     // Inicializa la cantidad de notas blancas.
        int nroNegras = 0;                                                      // Inicializa la cantidad de notas negras.

        int keyQuant = piano.getKeyQuantity();                                  // Obtiene la cantidad de notas del piano.

        for (int i = 1; i < keyQuant; i++) {
            switch (i % 12) {
                case 2:
                case 5:
                case 7:
                case 10:
                case 0:
                    if (i != keyQuant - 1) {
                        keys[i].setSize(blackKeyWith, blackKeyHeigth);
                        keys[i].setLocation((((i - nroNegras - 1) * whiteKeyWith) - blackKeyWith / 2), 0);
                        keys[i].setIcon(negraIcon);
                    }
                    nroNegras++;

                default:
                    break;
            }
        }

        for (int i = 0; i < keyQuant; i++) {
            switch (i % 12) {
                case 1:
                case 3:
                case 4:
                case 6:
                case 8:
                case 9:
                case 11:
                case 13:
                    keys[i].setSize(whiteKeyWith, whiteKeyHeigth);
                    keys[i].setLocation((nroBlancas * whiteKeyWith) + 1, 0);
                    if (i == C4)
                        keys[i].setIcon(blancaC4Icon);
                    else
                        keys[i].setIcon(blancaIcon);
                    nroBlancas++;
                    break;

                default:
                    break;
            }
        }

        if (nroBlancas <= 16) {
            this.setSize((int) ((whiteKeyWith * nroBlancas) + whiteKeyWith * 0.3), whiteKeyHeigth + 70);
            this.aumentarHorizontal.setEnabled(false);
            this.disminuirHorizontal.setEnabled(false);
        } else
            this.setSize((int) ((whiteKeyWith * 16) + whiteKeyWith * 0.3), whiteKeyHeigth + 70);

        pianoPane.setSize(nroBlancas * whiteKeyWith, whiteKeyHeigth);
        pianoPane.setPreferredSize(new Dimension(nroBlancas * whiteKeyWith, whiteKeyHeigth));
        pianoScrollPane.setSize(this.getWidth(), pianoPane.getHeight() + 18);
        this.add(pianoScrollPane);
    }

    /**
     * FIXME: Description of {@code relloadKeysStatus}. Asigna colores y reproducir sonidos
     * dependiendo del estado de cada tecla.
     *
     * @param newStatus es el nuevo estado de las teclas.
     */
    public void relloadKeysStatus(byte[] newStatus) {
        //WirelessPiano.printArray(newStatus);

        for (int i = 1; i < status.length; i++) {
            switch (newStatus[i]) {                                                 // Evalúa el estado obtenido.
                case 1:                                                             // Si el estado de la nota actual es comenzar a reproducir.
                    switch (i % 12) {                                               // Evalúa que tecla es.
                        // Si es una tecla negra.
                        case 2:
                        case 5:
                        case 7:
                        case 10:
                        case 0:
                            keys[i].setIcon(negraTcdIcon);                          // Pone icono a la tecla negra.
                            piano.getMidiChannels()[0].noteOn(i + C4Offset, 100);   // Reproduce la nota F en el canal indicado con velocidad 100.
                            break;                                                  // Termina de evaluar que tecla es.

                        default:                                                    // Si no es una tecla negra.
                            if (C4 == i)                                            // Si la tecla actual es el do central.
                                keys[i].setIcon(blancaC4TcdIcon);                   // Pone icono a la tecla blanca.
                            else                                                    // Si la tecla actual no es el do central.
                                keys[i].setIcon(blancaTcdIcon);                     // Pone icono a la tecla blanca.
                            piano.getMidiChannels()[0].noteOn(i + C4Offset, 100);   // Reproduce la nota F en el canal indicado con velocidad 100.
                            break;                                                  // Termina de evaluar que tecla es.
                    }
                    break;                                                          // Termina de evaluar el estado actual.

                case -1:                                                            // Si el estado de la nota actual es parar de reproducir.
                    switch (i % 12) {                                               // Evalúa que tecla es.
                        // Si es una tecla negra.
                        case 2:
                        case 5:
                        case 7:
                        case 10:
                        case 0:
                            keys[i].setIcon(negraIcon);                             // Pone icono a la tecla negra.
                            piano.getMidiChannels()[0].noteOff(i + C4Offset);       // Para de reproducir la nota B en el canal indicado con velocidad 100.
                            break;                                                  // Termina de evaluar que tecla es.

                        default:                                                    // Si no es una tecla negra.
                            if (C4 == i)                                            // Si la tecla actual es el do central.
                                keys[i].setIcon(blancaC4Icon);                      // Pone icono a la tecla blanca.
                            else                                                    // Si la tecla actual no es el do central.
                                keys[i].setIcon(blancaIcon);                        // Pone icono a la tecla blanca.
                            piano.getMidiChannels()[0].noteOff(i + C4Offset);       // Para de reproducir la nota B en el canal indicado con velocidad 100.
                            break;                                                  // Termina de evaluar que tecla es.
                        }
                    break;                                                          // Termina de evaluar el estado actual.
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        menu = new javax.swing.JMenuBar();
        Archivo = new javax.swing.JMenu();
        comeBack = new javax.swing.JMenuItem();
        exit = new javax.swing.JMenuItem();
        edit = new javax.swing.JMenu();
        aumentar = new javax.swing.JMenuItem();
        disminuir = new javax.swing.JMenuItem();
        aumentarHorizontal = new javax.swing.JMenuItem();
        disminuirHorizontal = new javax.swing.JMenuItem();
        selectC = new javax.swing.JMenu();
        octava1 = new javax.swing.JMenuItem();
        octava2 = new javax.swing.JMenuItem();
        octava3 = new javax.swing.JMenuItem();
        octava4 = new javax.swing.JMenuItem();
        octava5 = new javax.swing.JMenuItem();
        octava6 = new javax.swing.JMenuItem();
        octava7 = new javax.swing.JMenuItem();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setBackground(new java.awt.Color(10, 10, 10));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        Archivo.setText("Archivo");

        comeBack.setText("Cerrar el piano");
        comeBack.addActionListener(this::comeBackActionPerformed);
        Archivo.add(comeBack);

        exit.setText("Salir");
        exit.addActionListener(this::exitActionPerformed);
        Archivo.add(exit);

        menu.add(Archivo);

        edit.setText("Editar");

        aumentar.setText("Aumentar (↑)");
        aumentar.addActionListener(this::aumentarActionPerformed);
        edit.add(aumentar);

        disminuir.setText("Disminuir (↓)");
        disminuir.addActionListener(this::disminuirActionPerformed);
        edit.add(disminuir);

        aumentarHorizontal.setText("Aumentar horizontal  (→)");
        aumentarHorizontal.addActionListener(this::aumentarHorizontalActionPerformed);
        edit.add(aumentarHorizontal);

        disminuirHorizontal.setText("Disminuir horizontal (←)");
        disminuirHorizontal.addActionListener(this::disminuirHorizontalActionPerformed);
        edit.add(disminuirHorizontal);

        selectC.setText("Escoger octava (- +)");

        octava1.setText("Primera");
        octava1.addActionListener(this::octava1ActionPerformed);
        selectC.add(octava1);

        octava2.setText("Segunda");
        octava2.addActionListener(this::octava2ActionPerformed);
        selectC.add(octava2);

        octava3.setText("Tercera");
        octava3.addActionListener(this::octava3ActionPerformed);
        selectC.add(octava3);

        octava4.setText("Cuarta");
        octava4.addActionListener(this::octava4ActionPerformed);
        selectC.add(octava4);

        octava5.setText("Quinta");
        octava5.addActionListener(this::octava5ActionPerformed);
        selectC.add(octava5);

        octava6.setText("Sexta");
        octava6.addActionListener(this::octava6ActionPerformed);
        selectC.add(octava6);

        octava7.setText("Séptima");
        octava7.addActionListener(this::octava7ActionPerformed);
        selectC.add(octava7);

        edit.add(selectC);

        menu.add(edit);

        setJMenuBar(menu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 700, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 265, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * TODO: Description of {@code exitActionPerformed}.
     *
     * @param evt
     */
    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitActionPerformed

    /**
     * TODO: Description of {@code comeBackActionPerformed}.
     *
     * @param evt
     */
    private void comeBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comeBackActionPerformed
        Application.pianoFrames.remove(this.getTitle());                    // Borra el frame actual del map en la clase principal.
        dispose();                                                          // Cierra la ventana.
    }//GEN-LAST:event_comeBackActionPerformed

    /**
     * TODO: Description of {@code aumentarActionPerformed}.
     *
     * @param evt
     */
    private void aumentarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aumentarActionPerformed
        if (whiteKeyHeigth + 50 < screenHeight) {
            whiteKeyHeigth += 50;
            setSizes();
            relloadIcons();
        }
    }//GEN-LAST:event_aumentarActionPerformed

    /**
     * TODO: Description of {@code disminuirActionPerformed}.
     *
     * @param evt
     */
    private void disminuirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disminuirActionPerformed
        if (whiteKeyHeigth > 50) {
            whiteKeyHeigth -= 50;
            setSizes();
            relloadIcons();
        }
    }//GEN-LAST:event_disminuirActionPerformed

    /**
     * TODO: Description of {@code octava1ActionPerformed}.
     *
     * @param evt
     */
    private void octava1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_octava1ActionPerformed
        currentDo = C4 - (3 * 12);
    }//GEN-LAST:event_octava1ActionPerformed

    /**
     * TODO: Description of {@code octava2ActionPerformed}.
     *
     * @param evt
     */
    private void octava2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_octava2ActionPerformed
        currentDo = C4 - (2 * 12);
    }//GEN-LAST:event_octava2ActionPerformed

    /**
     * TODO: Description of {@code octava3ActionPerformed}.
     *
     * @param evt
     */
    private void octava3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_octava3ActionPerformed
        currentDo = C4 - (1 * 12);
    }//GEN-LAST:event_octava3ActionPerformed

    /**
     * TODO: Description of {@code octava4ActionPerformed}.
     *
     * @param evt
     */
    private void octava4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_octava4ActionPerformed
        currentDo = C4;
    }//GEN-LAST:event_octava4ActionPerformed

    /**
     * TODO: Description of {@code octava5ActionPerformed}.
     *
     * @param evt
     */
    private void octava5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_octava5ActionPerformed
        currentDo = C4 + (1 * 12);
    }//GEN-LAST:event_octava5ActionPerformed

    /**
     * TODO: Description of {@code octava6ActionPerformed}.
     *
     * @param evt
     */
    private void octava6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_octava6ActionPerformed
        currentDo = C4 + (2 * 12);
    }//GEN-LAST:event_octava6ActionPerformed

    /**
     * TODO: Description of {@code octava7ActionPerformed}.
     *
     * @param evt
     */
    private void octava7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_octava7ActionPerformed
        currentDo = C4 + (3 * 12);
    }//GEN-LAST:event_octava7ActionPerformed

    /**
     * TODO: Description of {@code formWindowClosing}.
     *
     * @param evt
     */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (!isRemote) {                                                        // Si no es un piano remoto.
            Application.pianoFrames.remove(this.getTitle());                    // Borra el frame actual del map en la clase principal.
            dispose();                                                          // Cierra la ventana.
        }
    }//GEN-LAST:event_formWindowClosing

    /**
     * TODO: Description of {@code aumentarHorizontalActionPerformed}.
     *
     * @param evt
     */
    private void aumentarHorizontalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aumentarHorizontalActionPerformed
        if (this.getWidth() + whiteKeyWith < whiteKeyWith * 53 && this.getWidth() + whiteKeyWith < screenWith) {
            this.setSize(this.getWidth() + whiteKeyWith, this.getHeight());
            pianoScrollPane.setSize(pianoScrollPane.getWidth() + whiteKeyWith, pianoScrollPane.getHeight());
        }
    }//GEN-LAST:event_aumentarHorizontalActionPerformed

    /**
     * TODO: Description of {@code disminuirHorizontalActionPerformed}.
     *
     * @param evt
     */
    private void disminuirHorizontalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disminuirHorizontalActionPerformed
        if (this.getWidth() - whiteKeyWith > whiteKeyWith * 8 && this.getWidth() - whiteKeyWith > 150) {
            this.setSize(this.getWidth() - whiteKeyWith, this.getHeight());
            pianoScrollPane.setSize(pianoScrollPane.getWidth() - whiteKeyWith, pianoScrollPane.getHeight());
        }
    }//GEN-LAST:event_disminuirHorizontalActionPerformed

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
            java.util.logging.Logger.getLogger(PianoFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(() -> {
            new PianoFrame().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu Archivo;
    private javax.swing.JMenuItem aumentar;
    private javax.swing.JMenuItem aumentarHorizontal;
    private javax.swing.JMenuItem comeBack;
    private javax.swing.JMenuItem disminuir;
    private javax.swing.JMenuItem disminuirHorizontal;
    private javax.swing.JMenu edit;
    private javax.swing.JMenuItem exit;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuBar menu;
    private javax.swing.JMenuItem octava1;
    private javax.swing.JMenuItem octava2;
    private javax.swing.JMenuItem octava3;
    private javax.swing.JMenuItem octava4;
    private javax.swing.JMenuItem octava5;
    private javax.swing.JMenuItem octava6;
    private javax.swing.JMenuItem octava7;
    private javax.swing.JMenu selectC;
    // End of variables declaration//GEN-END:variables

    /**
     * FIXME: Description of {@code keyTyped}. Key Typed
     *
     * @param ke es la tecla tocada.
     */
    @Override
    public void keyTyped(KeyEvent ke) {

    }

    /**
     * FIXME: Description of {@code keyPressed}. Evalúa la tecla oprimida.
     *
     * @param ke es la tecla oprimida.
     */
    @Override
    public void keyPressed(KeyEvent ke) {
        int c = ke.getKeyChar();
        int key = ke.getKeyCode();
        switch (c) {
            case 'a':
            case 'A':
                if (!isRemote && status[currentDo - 3] == 0) {
                    status[currentDo - 3] = 1;
                    keys[currentDo - 3].setIcon(blancaTcdIcon);
                    piano.getMidiChannels()[0].noteOn(currentDo - 3 + C4Offset, 100);// Reproduce la nota A en el canal indicado con velocidad 100.
                }
                break;

            case 'w':
            case 'W':
                if (!isRemote && status[currentDo - 2] == 0) {
                    status[currentDo - 2] = 1;
                    keys[currentDo - 2].setIcon(negraTcdIcon);
                    piano.getMidiChannels()[0].noteOn(currentDo - 2 + C4Offset, 100);// Reproduce la nota A# en el canal indicado con velocidad 100.
                }
                break;

            case 's':
            case 'S':
                if (!isRemote && status[currentDo - 1] == 0) {
                    status[currentDo - 1] = 1;
                    keys[currentDo - 1].setIcon(blancaTcdIcon);
                    piano.getMidiChannels()[0].noteOn(currentDo - 1 + C4Offset, 100);// Reproduce la nota B en el canal indicado con velocidad 100.
                }
                break;

            case 'd':
            case 'D':
                if (!isRemote && status[currentDo + 0] == 0) {
                    if (C4 == currentDo)
                        keys[currentDo + 0].setIcon(blancaC4TcdIcon);
                    else
                        keys[currentDo + 0].setIcon(blancaTcdIcon);

                    status[currentDo + 0] = 1;
                    piano.getMidiChannels()[0].noteOn(currentDo + 0 + C4Offset, 100);// Reproduce la nota C en el canal indicado con velocidad 100.
                }
                break;

            case 'r':
            case 'R':
                if (!isRemote && status[currentDo + 1] == 0) {
                    status[currentDo + 1] = 1;
                    keys[currentDo + 1].setIcon(negraTcdIcon);
                    piano.getMidiChannels()[0].noteOn(currentDo + 1 + C4Offset, 100);// Reproduce la nota C# en el canal indicado con velocidad 100.
                }
                break;

            case 'f':
            case 'F':
                if (!isRemote && status[currentDo + 2] == 0) {
                    status[currentDo + 2] = 1;
                    keys[currentDo + 2].setIcon(blancaTcdIcon);
                    piano.getMidiChannels()[0].noteOn(currentDo + 2 + C4Offset, 100);// Reproduce la nota D en el canal indicado con velocidad 100.
                }
                break;

            case 't':
            case 'T':
                if (!isRemote && status[currentDo + 3] == 0) {
                    status[currentDo + 3] = 1;
                    keys[currentDo + 3].setIcon(negraTcdIcon);
                    piano.getMidiChannels()[0].noteOn(currentDo + 3 + C4Offset, 100);// Reproduce la nota D# en el canal indicado con velocidad 100.
                }
                break;

            case 'g':
            case 'G':
                if (!isRemote && status[currentDo + 4] == 0) {
                    status[currentDo + 4] = 1;
                    keys[currentDo + 4].setIcon(blancaTcdIcon);
                    piano.getMidiChannels()[0].noteOn(currentDo + 4 + C4Offset, 100);// Reproduce la nota E en el canal indicado con velocidad 100.
                }
                break;

            case 'h':
            case 'H':
                if (!isRemote && status[currentDo + 5] == 0) {
                    status[currentDo + 5] = 1;
                    keys[currentDo + 5].setIcon(blancaTcdIcon);
                    piano.getMidiChannels()[0].noteOn(currentDo + 5 + C4Offset, 100);// Reproduce la nota F en el canal indicado con velocidad 100.
                }
                break;

            case 'u':
            case 'U':
                if (!isRemote && status[currentDo + 6] == 0) {
                    status[currentDo + 6] = 1;
                    keys[currentDo + 6].setIcon(negraTcdIcon);
                    piano.getMidiChannels()[0].noteOn(currentDo + 6 + C4Offset, 100);// Reproduce la nota F# en el canal indicado con velocidad 100.
                }
                break;

            case 'j':
            case 'J':
                if (!isRemote && status[currentDo + 7] == 0) {
                    status[currentDo + 7] = 1;
                    keys[currentDo + 7].setIcon(blancaTcdIcon);
                    piano.getMidiChannels()[0].noteOn(currentDo + 7 + C4Offset, 100);// Reproduce la nota G en el canal indicado con velocidad 100.
                }
                break;

            case 'i':
            case 'I':
                if (!isRemote && status[currentDo + 8] == 0) {
                    status[currentDo + 8] = 1;
                    keys[currentDo + 8].setIcon(negraTcdIcon);
                    piano.getMidiChannels()[0].noteOn(currentDo + 8 + C4Offset, 100);// Reproduce la nota G# en el canal indicado con velocidad 100.
                }
                break;

            case 'k':
            case 'K':
                if (!isRemote && status[currentDo + 9] == 0) {
                    status[currentDo + 9] = 1;
                    keys[currentDo + 9].setIcon(blancaTcdIcon);
                    piano.getMidiChannels()[0].noteOn(currentDo + 9 + C4Offset, 100);// Reproduce la nota A en el canal indicado con velocidad 100.
                }
                break;

            case 'o':
            case 'O':
                if (!isRemote && status[currentDo + 10] == 0) {
                    status[currentDo + 10] = 1;
                    keys[currentDo + 10].setIcon(negraTcdIcon);
                    piano.getMidiChannels()[0].noteOn(currentDo + 10 + C4Offset, 100);// Reproduce la nota A# en el canal indicado con velocidad 100.
                }
                break;

            case 'l':
            case 'L':
                if (!isRemote && status[currentDo + 11] == 0) {
                    status[currentDo + 11] = 1;
                    keys[currentDo + 11].setIcon(blancaTcdIcon);
                    piano.getMidiChannels()[0].noteOn(currentDo + 11 + C4Offset, 100);// Reproduce la nota B en el canal indicado con velocidad 100.
                }
                break;

            case 'ñ':
            case 'Ñ':
                if (!isRemote && status[currentDo + 12] == 0) {
                    if (C4 == currentDo + 12)
                        keys[currentDo + 12].setIcon(blancaC4TcdIcon);
                    else
                        keys[currentDo + 12].setIcon(blancaTcdIcon);

                    status[currentDo + 12] = 1;
                    piano.getMidiChannels()[0].noteOn(currentDo + 12 + C4Offset, 100);// Reproduce la nota C en el canal indicado con velocidad 100.
                }
                break;

            default:
                switch (key) {
                    case KeyEvent.VK_LEFT:
                        if (this.getWidth() - whiteKeyWith > whiteKeyWith * 8 && this.getWidth() - whiteKeyWith > 150) {
                            setSize(this.getWidth() - whiteKeyWith, this.getHeight());
                            pianoScrollPane.setSize(pianoScrollPane.getWidth() - whiteKeyWith, pianoScrollPane.getHeight());
                        }
                        break;

                    case KeyEvent.VK_RIGHT:
                        if (this.getWidth() + whiteKeyWith < whiteKeyWith * 53 && this.getWidth() + whiteKeyWith < screenWith) {
                            setSize(this.getWidth() + whiteKeyWith, this.getHeight());
                            pianoScrollPane.setSize(pianoScrollPane.getWidth() + whiteKeyWith, pianoScrollPane.getHeight());
                        }
                        break;
                }
                break;
        }
    }

    /**
     * FIXME: Description of {@code keyReleased}. Evalúa la tecla soltada.
     *
     * @param ke es la tecla soltada.
     */
    @Override
    public void keyReleased(KeyEvent ke) {
        int c = ke.getKeyChar();
        int key = ke.getKeyCode();
        switch (c) {
            case 'a':
            case 'A':
                if (!isRemote) {                                                    // Si no es un piano remoto.
                    status[currentDo - 3] = 0;
                    keys[currentDo - 3].setIcon(blancaIcon);
                    piano.getMidiChannels()[0].noteOff(currentDo - 3 + C4Offset);   // Para de reproducir la nota A en el canal indicado con velocidad 100.
                }
                break;

            case 'w':
            case 'W':
                if (!isRemote) {                                                    // Si no es un piano remoto.
                    status[currentDo - 2] = 0;
                    keys[currentDo - 2].setIcon(negraIcon);
                    piano.getMidiChannels()[0].noteOff(currentDo - 2 + C4Offset);   // Para de reproducir la nota A# en el canal indicado con velocidad 100.
                }
                break;

            case 's':
            case 'S':
                if (!isRemote) {                                                    // Si no es un piano remoto.
                    status[currentDo - 1] = 0;
                    keys[currentDo - 1].setIcon(blancaIcon);
                    piano.getMidiChannels()[0].noteOff(currentDo - 1 + C4Offset);   // Para de reproducir la nota B en el canal indicado con velocidad 100.
                }
                break;

            case 'd':
            case 'D':
                if (!isRemote) {                                                    // Si no es un piano remoto.
                    status[currentDo + 0] = 0;
                    if (C4 == currentDo)
                        keys[currentDo + 0].setIcon(blancaC4Icon);
                    else
                        keys[currentDo + 0].setIcon(blancaIcon);
                    piano.getMidiChannels()[0].noteOff(currentDo + 0 + C4Offset);    // Para de reproducir la nota C en el canal indicado con velocidad 100.
                }
                break;

            case 'r':
            case 'R':
                if (!isRemote) {                                                    // Si no es un piano remoto.
                    status[currentDo + 1] = 0;
                    keys[currentDo + 1].setIcon(negraIcon);
                    piano.getMidiChannels()[0].noteOff(currentDo + 1 + C4Offset);   // Para de reproducir la nota C# en el canal indicado con velocidad 100.
                }
                break;

            case 'f':
            case 'F':
                if (!isRemote) {                                                    // Si no es un piano remoto.
                    status[currentDo + 2] = 0;
                    keys[currentDo + 2].setIcon(blancaIcon);
                    piano.getMidiChannels()[0].noteOff(currentDo + 2 + C4Offset);   // Para de reproducir la nota D en el canal indicado con velocidad 100.
                }
                break;

            case 't':
            case 'T':
                if (!isRemote) {                                                    // Si no es un piano remoto.
                    status[currentDo + 3] = 0;
                    keys[currentDo + 3].setIcon(negraIcon);
                    piano.getMidiChannels()[0].noteOff(currentDo + 3 + C4Offset);   // Para de reproducir la nota D# en el canal indicado con velocidad 100.
                }
                break;

            case 'g':
            case 'G':
                if (!isRemote) {                                                    // Si no es un piano remoto.
                    status[currentDo + 4] = 0;
                    keys[currentDo + 4].setIcon(blancaIcon);
                    piano.getMidiChannels()[0].noteOff(currentDo + 4 + C4Offset);   // Para de reproducir la nota E en el canal indicado con velocidad 100.
                }
                break;

            case 'h':
            case 'H':
                if (!isRemote) {                                                    // Si no es un piano remoto.
                    status[currentDo + 5] = 0;
                    keys[currentDo + 5].setIcon(blancaIcon);
                    piano.getMidiChannels()[0].noteOff(currentDo + 5 + C4Offset);   // Para de reproducir la nota F en el canal indicado con velocidad 100.
                }
                break;

            case 'u':
            case 'U':
                if (!isRemote) {                                                    // Si no es un piano remoto.
                    status[currentDo + 6] = 0;
                    keys[currentDo + 6].setIcon(negraIcon);
                    piano.getMidiChannels()[0].noteOff(currentDo + 6 + C4Offset);   // Para de reproducir la nota F# en el canal indicado con velocidad 100.
                }
                break;

            case 'j':
            case 'J':
                if (!isRemote) {                                                    // Si no es un piano remoto.
                    status[currentDo + 7] = 0;
                    keys[currentDo + 7].setIcon(blancaIcon);
                    piano.getMidiChannels()[0].noteOff(currentDo + 7 + C4Offset);   // Para de reproducir la nota G en el canal indicado con velocidad 100.
                }
                break;

            case 'i':
            case 'I':
                if (!isRemote) {                                                    // Si no es un piano remoto.
                    status[currentDo + 8] = 0;
                    keys[currentDo + 8].setIcon(negraIcon);
                    piano.getMidiChannels()[0].noteOff(currentDo + 8 + C4Offset);   // Para de reproducir la nota G# en el canal indicado con velocidad 100.
                }
                break;

            case 'k':
            case 'K':
                if (!isRemote) {                                                    // Si no es un piano remoto.
                    status[currentDo + 9] = 0;
                    keys[currentDo + 9].setIcon(blancaIcon);
                    piano.getMidiChannels()[0].noteOff(currentDo + 9 + C4Offset);   // Para de reproducir la nota A en el canal indicado con velocidad 100.
                }
                break;

            case 'o':
            case 'O':
                if (!isRemote) {                                                    // Si no es un piano remoto.
                    status[currentDo + 10] = 0;
                    keys[currentDo + 10].setIcon(negraIcon);
                    piano.getMidiChannels()[0].noteOff(currentDo + 10 + C4Offset);  // Para de reproducir la nota A# en el canal indicado con velocidad 100.
                }
                break;

            case 'l':
            case 'L':
                if (!isRemote) {                                                    // Si no es un piano remoto.
                    status[currentDo + 11] = 0;
                    keys[currentDo + 11].setIcon(blancaIcon);
                    piano.getMidiChannels()[0].noteOff(currentDo + 11 + C4Offset);  // Para de reproducir la nota B en el canal indicado con velocidad 100.
                }
                break;

            case 'ñ':
            case 'Ñ':
                if (!isRemote) {                                                    // Si no es un piano remoto.
                    status[currentDo + 12] = 0;
                    if (C4 == currentDo + 12)
                        keys[currentDo + 12].setIcon(blancaC4Icon);
                    else
                        keys[currentDo + 12].setIcon(blancaIcon);
                    piano.getMidiChannels()[12].noteOff(currentDo + 12 + C4Offset); // Para de reproducir la nota C en el canal indicado con velocidad 100.
                }
                break;

            default:
                switch (key) {
                    case KeyEvent.VK_MINUS:
                    case KeyEvent.VK_SUBTRACT:
                        if (currentDo - 12 > 0)
                            currentDo -= 12;
                        break;

                    case KeyEvent.VK_ADD:
                        if (currentDo + 13 < piano.getKeyQuantity())
                            currentDo += 12;
                        break;

                    case KeyEvent.VK_UP:
                        if (whiteKeyHeigth + 50 < screenHeight) {
                            whiteKeyHeigth += 50;
                            setSizes();
                            relloadIcons();
                        }
                        break;

                    case KeyEvent.VK_DOWN:
                        if (whiteKeyHeigth > 50) {
                            whiteKeyHeigth -= 50;
                            setSizes();
                            relloadIcons();
                        }
                        break;
                }
                break;
        }
    }

    /**
     * TODO: Description of {@code actionPerformed}.
     *
     * @param ae
     */
    @Override
    public void actionPerformed(ActionEvent ae) {

    }
}
