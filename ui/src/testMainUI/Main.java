package testMainUI;

import ui.ConsoleUserInterface;
import ui.UserInterface;

public class Main {
    public static void main(String[] args) {
        UserInterface UI = new ConsoleUserInterface();
        UI.run();
    }
}
