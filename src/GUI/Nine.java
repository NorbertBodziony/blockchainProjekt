package GUI;

public class Nine<T> {

    private final T first;
    private final T second;
    private final T third;
    private final T four;
    private final T five;
    private final T six;
    private final T seven;
    private final T eight;
    private final T nine;


    public Nine(T first, T second, T third, T four, T five, T six, T seven, T eight, T nine) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.four = four;
        this.five = five;
        this.six = six;
        this.seven = seven;
        this.eight = eight;
        this.nine = nine;
    }

    public T getFirst() {
        return first;
    }

    public T getFour() {
        return four;
    }

    public T getFive() {
        return five;
    }

    public T getSix() {
        return six;
    }

    public T getSeven() {
        return seven;
    }

    public T getEight() {
        return eight;
    }

    public T getNine() {
        return nine;
    }

    public T getSecond() {
        return second;
    }

    public T getThird() {
        return third;
    }
}