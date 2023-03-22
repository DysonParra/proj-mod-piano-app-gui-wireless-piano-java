/*
 * @fileoverview    {Application}
 *
 * @version         2.0
 *
 * @author          Dyson Arley Parra Tilano <dysontilano@gmail.com>
 *
 * @copyright       Dyson Parra
 * @see             github.com/DysonParra
 *
 * History
 * @version 1.0     Implementación realizada.
 * @version 2.0     Documentación agregada.
 */
package com.project.dev;

import com.project.dev.wirelesspiano.frame.MainFrame;
import com.project.dev.wirelesspiano.frame.PianoFrame;
import com.project.dev.wirelesspiano.struct.UdpServer;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Definición de {@code Application}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
public class Application {

    public static MainFrame mainFrame;                                          // Frame con el menú principal.
    public static Map<String, PianoFrame> pianoFrames;                          // Ventanas con un piano creadas.
    public static int serverPort = -1;                                          // Puerto de conexión.
    public static UdpServer udpServer;                                          // Servidor udp.

    /**
     * Entrada principal del sistema.
     *
     * @param args argumentos de la linea de comandos.
     * @throws java.net.SocketException
     */
    public static void main(String[] args) throws SocketException {
        mainFrame = new MainFrame();                                            // Inicializa instancia de Main_Frame.
        mainFrame.setVisible(true);                                             // Pone visible el Main_Frame.

        pianoFrames = new HashMap<>();                                          // Inicializa map que tendrá cada piano creado.
        udpServer.startServer();                                                // Lanza el daemon del servidor udp.
    }

    /**
     * FIXME: Definición de {@code printArray}. Imprime un array.
     *
     * @param array
     */
    public static void printArray(byte[] array) {
        ByteArrayOutputStream index = new ByteArrayOutputStream();              // Crea objeto de tipo ByteArrayOutputStream.
        ByteArrayOutputStream value = new ByteArrayOutputStream();              // Crea objeto de tipo ByteArrayOutputStream.

        PrintStream indexPrinter = new PrintStream(index);                      // Crea objeto de tipo PrintStream.
        PrintStream valuePrinter = new PrintStream(value);                      // Crea objeto de tipo PrintStream.

        String output;                                                          // Crea String.

        for (int i = 0; i < array.length; i++) {
            indexPrinter.printf("%2d ", i);
            valuePrinter.printf("%2d ", array[i]);
        }

        output = index.toString();                                              // Asigna a output el buffer index.
        System.out.println("print " + output);                                  // Muestra en consola output.

        output = value.toString();                                              // Asigna a output el buffer value.
        System.out.println("print " + output);                                  // Muestra en consola output.

        System.out.println("--");                                                // Muestra en consola output.
    }

}
