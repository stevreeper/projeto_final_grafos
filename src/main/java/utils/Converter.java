package utils;

public abstract class Converter {
    static final double planeSpeed = 900;

    /**
     * Converts the distance in minutes based on an planes average speed
     *
     * @param distance distance to be converted
     * @return the minutes taken to go the distance by plane
     */
    public static int distanceInMinutes(double distance) {
        return (int) ((distance / planeSpeed) * 60);
    }
}
