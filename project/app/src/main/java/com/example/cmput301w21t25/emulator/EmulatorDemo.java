package com.example.cmput301w21t25.emulator;


/**
 * This class is a simple demo that uses the emulator to update a document
 */
public class EmulatorDemo {

    public EmulatorDemo() {
        EmulatorSuite emulator = new EmulatorSuite();
        emulator.useEmulator();
        //emulator.useFirestore();
        emulator.updateDoc();
    }

}
