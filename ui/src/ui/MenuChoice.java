package ui;

public enum MenuChoice {
    LOAD_XML(1, "Load data from XML file"),
    SHOW_DETAILS(2, "Show machine details"),
    MANUAL_SET(3, "Manually set code"),
    AUTO_SET(4, "Set by auto generated code"),
    PROCESS(5, "Process a message"),
    RESET(6, "Reset machine to initial code"),
    HISTORY_AND_STATISTICS(7, "Show machine history and statistics"),
    EXIT(8, "Exit");
    private final int menuNumber;
    private final String description;
    private MenuChoice(int menuNumber, String description){
        this.menuNumber = menuNumber;
        this.description = description;
    }

    public static boolean isValidChoice(int choice){
        for (MenuChoice menuChoice: MenuChoice.values()) {
            if(menuChoice.menuNumber == choice)
                return true;
        }
        return false;
    }

    public static MenuChoice valueByInt(int num){
        MenuChoice MC = null;
        for (MenuChoice menuChoice: MenuChoice.values()) {
            if(menuChoice.menuNumber == num)
                MC = menuChoice;
        }
        return MC;
    }

    @Override
    public String toString(){
        return description;
    }
}
