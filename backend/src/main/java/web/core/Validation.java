package web.core;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Validation {
    private int x;
    private float y;
    private float r;
    private final Map<String, String> coordinates;
    public static final Set<Integer> availableX = Set.of(-5, -4, -3, -2, -1, 0, 1, 2, 3);
    public static final Set<Float> availableR = Set.of(1F, 1.5F, 2F, 2.5F, 3F);
    public static final String floatPattern = "^-?(?:0|[1-9][0-9]*)(?:\\.[0-9]+)?$";
    private final List<String> errors = new ArrayList<>();

    public Validation(Map<String, String> coordinates) {
        this.coordinates = coordinates;
    }

    public void validateXYR() {
        validateX();
        validateY();
        validateR();
    }

    private void validateX() {
        String xValue = coordinates.get("x");
        if (xValue.trim().isEmpty()) {
            errors.add("Координата X отсутствует!");
            return;
        }
        try {
            int parsedX = Integer.parseInt(xValue);
            if (!availableX.contains(parsedX)) {
                errors.add("Координата X должна быть от -5 до 3!");
                return;
            }
            this.x = parsedX;
        } catch (NumberFormatException e) {
            errors.add("Координата X должна быть целым числом!");
        }
    }

    private void validateY() {
        String yValue = coordinates.get("y");
        if (yValue.trim().isEmpty()) {
            errors.add("Координата Y отсутствует!");
            return;
        }
        if (!yValue.matches(floatPattern)) {
            errors.add("Координата Y должна быть числом с плавающей точкой!");
            return;
        }
        float parsedY = Float.parseFloat(yValue);
        if (parsedY < -3 || parsedY > 3) {
            errors.add("Координата Y должна быть от -3 до 3!");
            return;
        }
        this.y = parsedY;
    }

    private void validateR() {
        String rValue = coordinates.get("r");
        if (rValue.trim().isEmpty()) {
            errors.add("Радиус R отсутствует!");
            return;
        }
        if (!rValue.matches(floatPattern)) {
            errors.add("Радиус R должен быть числом с плавающей точкой!");
            return;
        }
        float parsedR = Float.parseFloat(rValue);
        if (!availableR.contains(parsedR)) {
            errors.add("Радиус R должен быть равен одному из значений: 1.0, 1.5, 2.0, 2.5, 3.0");
            return;
        }
        this.r = parsedR;
    }

    public List<String> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public int getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getR() {
        return r;
    }
}
