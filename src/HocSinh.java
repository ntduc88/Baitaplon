import java.util.Objects;

public class HocSinh {
    private String maHS;
    private String hoTen;
    private String lop;
    private int namSinh;

    public HocSinh(String maHS, String hoTen, String lop, int namSinh) {
        this.maHS = maHS;
        this.hoTen = hoTen;
        this.lop = lop;
        this.namSinh = namSinh;
    }

    public String getMaHS() { return maHS; }
    public String getHoTen() { return hoTen; }
    public String getLop() { return lop; }
    public int getNamSinh() { return namSinh; }

    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public void setLop(String lop) { this.lop = lop; }
    public void setNamSinh(int namSinh) { this.namSinh = namSinh; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HocSinh hocSinh = (HocSinh) o;
        return Objects.equals(maHS, hocSinh.maHS);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maHS);
    }

    @Override
    public String toString() {
        return maHS + " - " + hoTen;
    }
}
