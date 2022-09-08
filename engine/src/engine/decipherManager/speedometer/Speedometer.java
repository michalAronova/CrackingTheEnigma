package engine.decipherManager.speedometer;

public interface Speedometer<T, K> {
    T calculateNext(T value, K calculateBy);
}
