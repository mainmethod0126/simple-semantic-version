package io.github.mainmethod0126.gradle.simple.versioning.task.version;

import io.github.mainmethod0126.gradle.simple.versioning.exception.MinimumLimitException;

public class IntegerVersion implements Increaseable, Decrementable {

    private int value = 0;

    public IntegerVersion() {
    }

    public IntegerVersion(int value) {
        this.value = value;
    }

    @Override
    public void increase() {
        this.value++;
    }

    public void increase(int amount) {
        this.value += amount;
    }

    @Override
    public void decrease() throws MinimumLimitException {

        if (this.value - 1 < 0) {
            throw new MinimumLimitException("It cannot decrease any further. Minimum value is 0");
        }

        this.value--;

    }

    public void decrease(int amount) throws MinimumLimitException {
        if (this.value - amount < 0) {
            throw new MinimumLimitException("It cannot decrease any further. Minimum value is 0");
        }

        this.value -= amount;
    }

    public String toString() {
        return String.valueOf(value);
    }

    public int get() {
        return value;
    }

    public void set(int value) {
        this.value = value;
    }

    public void set(String value) {
        this.value = Integer.valueOf(value);
    }

}
