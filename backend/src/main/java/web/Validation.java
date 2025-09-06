package web;
import java.util.Map;
import java.util.Set;

public class Validation {
    private int x;
    private float y;
    private float r;
    private final Map<String, String> coordinates;
    public static final Set<Integer> availableX = Set.of(-5, -4, -3, -2, -1, 0, 1, 2, 3);
    public static final Set<Float> availableR = Set.of(1F, 1.5F, 2F, 2.5F, 3F);

    public Validation(Map<String, String> coordinates) {
        this.coordinates = coordinates;
    }

    public boolean validateXYR() {
        try {
            String xValue = coordinates.get("x");
            if (xValue == null || !availableX.contains(Integer.parseInt(xValue))) {
                return false;
            }
            x = Integer.parseInt(xValue);
            String yValue = coordinates.get("y");
            if (yValue == null) {
                return false;
            }
            float parsedY = Float.parseFloat(yValue);
            if (parsedY < -3 || parsedY > 3) {
                return false;
            }
            y = parsedY;
            String rValue = coordinates.get("r");
            if (rValue == null || !availableR.contains(Float.parseFloat(rValue))) {
                return false;
            }
            r = Float.parseFloat(rValue);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean wasThereHit() {
        return quarterWithTriangle() || quarterWithSector() || quarterWithRectangle();
    }

    private boolean quarterWithTriangle() {
        if (x >= 0 && y >= 0) {
            return (x + y) <= r / 2;
        }
        return false;
    }

    private boolean quarterWithSector() {
        if (x > 0 && y < 0) {
            return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) <= r/2;
        }
        return false;
    }

    private boolean quarterWithRectangle() {
        if (x < 0 && y >= 0) {
            return (x >= -r/2) && (y <= r);
        }
        return false;
    }
}
