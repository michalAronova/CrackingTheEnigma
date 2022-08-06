package enigmaMachine.rotor;

public class TheRotor implements Rotor {
    private final int notch;
    private final int id;
    private int initialPosition;
    private int currentPosition;
    private final String alphabet;
    private final String rotorPermutation;
    private AdvanceSelf next;

    public TheRotor(int notch, int id, String rotorPermutation, String alphabet) {
        this.notch = notch;
        this.id = id;
        this.currentPosition = 2;
        this.initialPosition = 2;
        this.rotorPermutation = rotorPermutation;
        this.alphabet = alphabet;
    }

    @Override
    public void setInitialPosition(int index){
        this.initialPosition = index;
        this.currentPosition = index;
    }
    public void reset(){
        this.currentPosition = this.initialPosition;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void setNextRotor(AdvanceSelf nextRotor) {
        this.next = nextRotor;
    }

    /*
        rotor get index of previous value straiten to *starting position*
        therefore when search the char value of the input, starting position addition is needed
        now its fixed to base 0
        after finding the relavent index we can get the char, need to find its twin's index in the map
        map is staiten to base 0 so sub of starting position is needed
        index value returned straiten to *starting position*
         */
    @Override
    public int rightToLeftReturnIndex(int index) {
        //finds the char on base 0 of the char in the index location in the alphabet
        char curChar = alphabet.charAt(Math.floorMod((index + currentPosition), alphabet.length()));
        //finds the index of curChar in the permutation
        int permutatedIndex = rotorPermutation.indexOf(curChar);
        //finds the index in currentPosition base of the char
        int indexOut = Math.floorMod((permutatedIndex - currentPosition), alphabet.length());
        return indexOut;
    }
    @Override
    public int leftToRightReturnIndex(int index) {
        char curChar = rotorPermutation.charAt(Math.floorMod((index + currentPosition), alphabet.length()));
        int alphabetIndex = alphabet.indexOf(curChar);
        int indexOut = Math.floorMod((alphabetIndex - currentPosition), alphabet.length());
        return indexOut;
    }

    @Override
    public void advance() {
        currentPosition = Math.floorMod((currentPosition + 1), alphabet.length());
        if (currentPosition == notch && next != null){
            next.advance();
        }
    }
}
