package enigmaMachine.rotor;

public interface Rotor extends AdvanceSelf {
    int getID();
    String getRightPermutation();
    void setNextRotor(AdvanceSelf nextRotor);
    int rightToLeftReturnIndex(int index);
    int leftToRightReturnIndex(int index);
    void setInitialPosition(int index);
    void setInitialPosition(Character let);
    void reset();
    int getNotch();
    int getRelativeNotch();
}