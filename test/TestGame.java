package test;

import gui.MainGui;

import java.io.IOException;

public class TestGame {

    public static void main(String[] args) throws IOException {
        MainGui gui = new MainGui("Air Traffic Control", 5, 5);
        Thread thread = new Thread(gui);
        thread.start();
    }
}
