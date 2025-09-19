package web.core;

public class HitCheck {
    private final int x;
    private final float y;
    private final float r;

    public HitCheck(int x, float y, float r) {
        this.x = x;
        this.y = y;
        this.r = r;

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
