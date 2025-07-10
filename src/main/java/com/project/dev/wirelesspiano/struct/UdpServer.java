/*
 * @overview        {UdpServer}
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
package com.project.dev.wirelesspiano.struct;

import com.project.dev.Application;
import com.project.dev.wirelesspiano.frame.MainFrame;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * TODO: Description of {@code UdpServer}.
 *
 * @author Dyson Parra
 * @since Java 17 (LTS), Gradle 7.3
 */
public class UdpServer {

    public static int serverPort = 1024;                                                                        // Inicializa el numero del puerto.
    public static int packetSize = 89;                                                                          // Inicializa el tamaño del paquete.
    public static DatagramSocket socket;                                                                        // Como se envian los paquetes
    public static DatagramPacket packet;                                                                        // Cada paquete recibido y/o enviado.
    public static InetAddress address;                                                                          // Usada para obtener la ip desde donde llega cada paquete.
    public static int clientPort;                                                                               // Usada para obtener el puerto desde donde llegó un paquete.
    public static byte[] request;                                                                               // Usada para recibir paquetes de bytes.
    public static byte[] response;                                                                              // Usada para enviar paquetes de bytes.
    public static String ipAddress;                                                                             // Ip desde donde llegue cada paquete en String.

    /**
     * TODO: Description of method {@code UdpServer}.
     *
     * @param serverPort es el puerto del servidor.
     * @param packetSize es el tamaño de los paquetes.
     * @throws java.net.SocketException
     */
    public UdpServer(int serverPort, int packetSize) throws SocketException {
        UdpServer.serverPort = serverPort;
        UdpServer.packetSize = packetSize;

        socket = new DatagramSocket(serverPort);
        request = new byte[packetSize];
    }

    /**
     * FIXME: Description of method {@code startServer}. Inicia el servidor.
     *
     */
    public void startServer() {
        boolean requestStatus;                                                                          // Si se pudo procesar la solicitud indicada.

        while (true) {                                                                                  // Recibe paquetes infinitamente.
            requestStatus = false;                                                                      // Indica que aún no se ha podido procesar la solicitud.
            packet = new DatagramPacket(request, packetSize);                                           // Inicializa paquete para enviar y recibir mensajes.

            //System.out.println("...");
            try {                                                                                       // Espera indefinidamente a que llegue un paquete.
                socket.receive(packet);                                                                 // Intenta obtener un paquete.
            } catch (IOException e) {                                                                   // Si no pudo recibir un paquete.
            }
            address = packet.getAddress();                                                              // Obtiene la ip del cliente que envió el paquete.
            clientPort = packet.getPort();                                                              // Obtiene el puerto del cliente que envió el paquete.
            ipAddress = String.valueOf(address);                                                        // Obtiene la ip desde donde legó el paquete en string.

            switch (request[0]) {                                                                       // Evalúa la petición del cliente.
                case 0:                                                                                 // Si la petición es terminar conexión.
                    //System.out.printf("Request 0\n");
                    try {                                                                               // Intenta cerrar el piano.
                    Application.pianoFrames.get(ipAddress).dispose();                                   // Cierra el frame con titulo igual a la ip desde donde se envió la petición.
                    Application.pianoFrames.remove(ipAddress);                                          // Borra el frame con titulo igual a la ip desde donde se envió la petición del map en la clase principal.
                    packet = new DatagramPacket("true ".getBytes(), 4, address, clientPort);            // Crea paquete para enviar al cliente.
                    socket.send(packet);                                                                // Envía el paquete al cliente.
                    requestStatus = true;                                                               // Indica que la solicitud se procesó exitosamente.
                } catch (Exception e) {                                                                 // Si no pudo cerrar el piano.
                }
                break;                                                                                  // Termina de evaluar la petición.

                case 1:                                                                                 // Si la petición es comenzar conexión.
                    //System.out.printf("Request 1 '%d'\n", request[9]);
                    if (Application.mainFrame.getEnableAllConnections()) {                              // Si se permiten conexiones.
                        try {
                            packet = new DatagramPacket("true ".getBytes(), 4, address, clientPort);    // Crea paquete para enviar al cliente.
                            socket.send(packet);                                                        // Envía el paquete al cliente.
                            MainFrame.createPianoFrame(ipAddress, 89, 4, request[1], true);             // Crea un nuevo piano desde la dirección ip indicada.
                            requestStatus = true;                                                       // Indica que la solicitud se procesó exitosamente.
                        } catch (Exception e) {                                                         // Si no pudo iniciar una nueva conexión.
                        }
                    }
                    break;                                                                              // Termina de evaluar la petición.

                case 2:                                                                                 // Si la petición es actualizar teclas.
                    //System.out.printf("Request 2\n");
                    if (!Application.mainFrame.getDisableAllConnections()) {                            // Si no están deshabilitadas todas las conexiones.
                        try {                                                                           // Intenta actualizar las teclas.
                            Application.pianoFrames.get(ipAddress).relloadKeysStatus(request);          // actualiza los estados de las teclas.
                            packet = new DatagramPacket("true ".getBytes(), 4, address, clientPort);    // Crea paquete para enviar al cliente.
                            socket.send(packet);
                            requestStatus = true;                                                       // Indica que la solicitud se procesó exitosamente.
                        } catch (Exception e) {                                                         // Si no pudo actualizar las teclas.
                        }
                    }
                    break;                                                                              // Termina de evaluar la petición.
            }

            if (!requestStatus) {                                                                       // Si la solicitud no pudo ser procesada.
                try {                                                                                   // Intenta enviar un paquete.
                    response = "false".getBytes();                                                      // Indica que el mensaje que se devuelve es false.
                    packet = new DatagramPacket(response, response.length, address, clientPort);        // Crea paquete para enviar al cliente.
                    socket.send(packet);                                                                // Envía el paquete al cliente.
                } catch (IOException e) {                                                               // Si al cliente no le llegó el paquete.
                }
            }
        }
    }
}
