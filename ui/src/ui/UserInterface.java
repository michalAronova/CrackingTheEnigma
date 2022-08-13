package ui;

public interface UserInterface extends Runnable {
    void loadDataFromXML();
    void showMachineDetails();
    void manuallySetMachineCode();
    void autoSetMachineCode();
    void processInput();
    void resetMachine();
    void showHistoryAndStatistics();
    void exit();
}
