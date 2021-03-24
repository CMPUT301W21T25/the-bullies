package com.example.cmput301w21t25.emulator;



public class EmulatorDemo {

    public EmulatorDemo() {
        EmulatorSuite emulator = new EmulatorSuite();
        emulator.useEmulator();
        //emulator.useFirestore();
        emulator.updateDoc();
    }

}
