package enigmaMachine;

import enigmaMachine.keyBoard.KeyBoard;
import enigmaMachine.plugBoard.PlugBoard;
import enigmaMachine.plugBoard.Plugs;
import enigmaMachine.reflector.Reflecting;
import enigmaMachine.reflector.Reflector;
import enigmaMachine.rotor.Rotor;
import enigmaMachine.rotor.TheRotor;
import enigmaMachine.secret.Secret;

import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        KeyBoard keyboard = new KeyBoard();
        Plugs plugBoard = new PlugBoard();
        Rotor rotor1 = new TheRotor(3, 1, "fedcba", "abcdef");
        rotor1.setInitialPosition(2);
        Rotor rotor2 = new TheRotor(0, 2, "ebdfca", "abcdef");
        rotor2.setInitialPosition(2);

        List<Rotor> rotors = new LinkedList<>();
        rotors.add(rotor1);
        rotors.add(rotor2);

        Reflecting reflector = new Reflector();

        Secret secret = new Secret(rotors, reflector, plugBoard);
        Machine machine = new Machine(secret, keyboard);
        ///for(char c: keyboard.getAsCharList()){
        ///   System.out.println(c+" -> " + machine.process(c));
        ///}
        System.out.println("a -> " + machine.process('a'));
        System.out.println("a -> " + machine.process('a'));
        System.out.println("f -> " + machine.process('f'));
        System.out.println("run again.......");
        System.out.println("a -> " + machine.process('a'));
        System.out.println("a -> " + machine.process('a'));
        System.out.println("f -> " + machine.process('f'));
        System.out.println("a -> " + machine.process('a'));
        System.out.println("a -> " + machine.process('a'));
        System.out.println("f -> " + machine.process('f'));
        System.out.println("a -> " + machine.process('a'));
        System.out.println("a -> " + machine.process('a'));
        System.out.println("f -> " + machine.process('f'));
        machine.resetRotorsToInitial();
        System.out.println("reset and run again.......");
        System.out.println("a -> " + machine.process('a'));
        System.out.println("a -> " + machine.process('a'));
        System.out.println("f -> " + machine.process('f'));


    }
}
