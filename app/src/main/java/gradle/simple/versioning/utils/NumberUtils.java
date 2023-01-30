package gradle.simple.versioning.utils;

public class NumberUtils {
    private NumberUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isPositiveInteger(String str) {
        return Integer.valueOf(str) > 0;
    }
}
