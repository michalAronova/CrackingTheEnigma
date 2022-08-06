package enigmaMachine.rotor;

public interface Rotor extends AdvanceSelf {
    int getID();

    void setNextRotor(AdvanceSelf nextRotor);

    int rightToLeftReturnIndex(int index);

    int leftToRightReturnIndex(int index);

    void setInitialPosition(int index);

    void reset();

    int getNotch();
}