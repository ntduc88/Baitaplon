import java.util.ArrayList;
import java.util.List;

public class Diem {
    private String maHS;
    private String mon;
    private List<Double> diemTX = new ArrayList<>();
    private Double diemGK = null;
    private Double diemCK = null;

    public Diem(String maHS, String mon) {
        this.maHS = maHS;
        this.mon = mon;
    }

    public String getMaHS() { return maHS; }
    public String getMon() { return mon; }
    public List<Double> getDiemTX() { return diemTX; }
    public Double getDiemGK() { return diemGK; }
    public Double getDiemCK() { return diemCK; }

    public void themDiemTX(double d) { diemTX.add(d); }
    public void setDiemGK(double d) { diemGK = d; }
    public void setDiemCK(double d) { diemCK = d; }

    public double tinhDTBMon() {
        int nTX = Math.max(0, diemTX.size());
        double sumTX = 0;
        for (double d : diemTX) sumTX += d;
        double gk = diemGK == null ? 0 : diemGK;
        double ck = diemCK == null ? 0 : diemCK;
        double tuSo = sumTX * 1 + gk * 2 + ck * 3;
        double mauSo = nTX * 1 + 2 + 3;
        if (mauSo == 0) return 0;
        return tuSo / mauSo;
    }
}
