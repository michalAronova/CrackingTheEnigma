# CrackingTheEnigma
A JavaFX application that allows you to handle an engima machine in various ways.
1. Upload an XML file which describes the details of the machine and its components: the rotors, reflectors, and its ABC.
2. Configure a code on the machine using the provided rotors and reflectors.
3. Process messages and watch as the rotors move with every keypress, as well as see your keyboard lightup for the encryption letter!
# The Decipher Agents
A core part of this application is the use of threads, threadpools, blocking queues.
This is demonstrated in the Brute Force tab, where you can encrypt a word from the dictionary and watch as your agents (threads) attempt to find a candidate for the encryption, using brute force (= going through all possible code configurations!)
Just imagine how much more difficult this was for Turing...
