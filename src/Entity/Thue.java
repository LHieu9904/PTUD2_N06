// =====================================================
// FILE: Entity/Thue.java
// =====================================================

package Entity;

public class Thue {

    private String maThue;
    private String tenThue;
    private double phanTramThue;

    public Thue() {
    }

    public Thue(
            String maThue,
            String tenThue,
            double phanTramThue
    ) {
        this.maThue = maThue;
        this.tenThue = tenThue;
        this.phanTramThue = phanTramThue;
    }

    public String getMaThue() {
        return maThue;
    }

    public void setMaThue(String maThue) {
        this.maThue = maThue;
    }

    public String getTenThue() {
        return tenThue;
    }

    public void setTenThue(String tenThue) {
        this.tenThue = tenThue;
    }

    public double getPhanTramThue() {
        return phanTramThue;
    }

    public void setPhanTramThue(double phanTramThue) {
        this.phanTramThue = phanTramThue;
    }

    @Override
    public String toString() {
        return tenThue;
    }
}