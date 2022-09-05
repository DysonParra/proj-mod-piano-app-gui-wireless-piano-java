/*
 * @fileoverview {FileName} se encarga de realizar tareas especificas.
 *
 * @version             1.0
 *
 * @author              Dyson Arley Parra Tilano <dysontilano@gmail.com>
 * Copyright (C) Dyson Parra
 *
 * @History v1.0 --- La implementacion de {FileName} fue realizada el 31/07/2022.
 * @Dev - La primera version de {FileName} fue escrita por Dyson A. Parra T.
 */
package com.project.dev.wirelesspiano.struct;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.JLabel;
import lombok.Data;

import static com.project.dev.wirelesspiano.WirelessPianoConstant.SOUNDFONT_RES_PATH;

/**
 * TODO: Definición de {@code Piano}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
//@AllArgsConstructor
//@Builder
@Data
//@NoArgsConstructor
public final class Piano {

    private int keyQuantity;                                                                // Cantidad de teclas del piano.
    private int C4;                                                                         // Número de tecla que es el do central del piano.
    private JLabel[] keys;                                                                  // Teclas del piano.
    private byte[] status;                                                                  // Estado de cada tecla del piano.
    private int instrumentId;                                                               // Id del instrumento con los sonidos de cada tecla del piano.

    private Synthesizer synthesizer;                                                        // Sintetizador midi del piano.
    private MidiChannel[] midiChannels;                                                     // Canales de reproducción midi del piano.

    /**
     * TODO: Definición de {@code Piano}.
     *
     * @param keyQuantity  indica la cantidad de teclas del piano.
     * @param C4           indica el número de do que será el do central.
     * @param instrumentId indica el número de instrumento con que sonará cada tecla.
     */
    public Piano(int keyQuantity, int C4, int instrumentId) {
        this.keyQuantity = keyQuantity;                                                     // Asigna valor a la cantidad de teclas.
        this.C4 = 4 + ((C4 - 1) * 12);                                                      // Asigna valor al do central.
        this.instrumentId = instrumentId;                                                   // Asigna valor al id del instrumento.
        this.keys = new JLabel[keyQuantity];                                                // Inicializa el array con las teclas.
        this.status = new byte[keyQuantity];                                                // Inicializa el array con los estados de cada notas.

        initMidiSystem(this.instrumentId);                                                  // Inicia el sistema midi.
    }

    /**
     * FIXME: Definición de {@code initMidiSystem}. Inicia el sistema midi.
     *
     * @param instrumentId indica el número de instrumento con que sonará cada tecla.
     */
    public void initMidiSystem(int instrumentId) {
        try {                                                                               // Intenta.
            synthesizer = MidiSystem.getSynthesizer();                                      // Crea un sintetizador.
            synthesizer.open();                                                             // Abre el sintetizador.
            synthesizer.unloadAllInstruments(synthesizer.getDefaultSoundbank());            // Borra instrumentos por defecto del sintetizador.

            InputStream soundfont = Piano.class.getResourceAsStream(SOUNDFONT_RES_PATH);
            InputStream bufferedSoundfont = new BufferedInputStream(soundfont);

            synthesizer.loadAllInstruments(MidiSystem.getSoundbank(bufferedSoundfont));     // Carga un soundfont en el sintetizador.
            midiChannels = synthesizer.getChannels();                                       // Obtiene los canales del sintetizador.
            midiChannels[0].programChange(midiChannels[0].getProgram(), instrumentId);      // Asigna id del instrumento a los canales dle sintetizador.
        } catch (MidiUnavailableException | InvalidMidiDataException | IOException e) {     // Si no fue posible capturar el sistema midi.
        }
    }

}
