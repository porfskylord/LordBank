package interfaces;

@FunctionalInterface
public interface Validator<T> {

    boolean validate(T input);
    

    default Validator<T> and(Validator<? super T> other) {
        return input -> validate(input) && other.validate(input);
    }
    

    default Validator<T> negate() {
        return input -> !validate(input);
    }
    

    default Validator<T> or(Validator<? super T> other) {
        return input -> validate(input) || other.validate(input);
    }
}
