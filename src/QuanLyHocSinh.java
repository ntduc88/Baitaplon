import java.util.ArrayList;
import java.util.List;

public class QuanLyHocSinh {
    private List<HocSinh> danhSach = new ArrayList<>();

    public boolean them(HocSinh hs) {
        if (timTheoMa(hs.getMaHS()) != null) return false;
        danhSach.add(hs);
        return true;
    }

    public HocSinh timTheoMa(String ma) {
        for (HocSinh h : danhSach) if (h.getMaHS().equals(ma)) return h;
        return null;
    }

    public boolean xoaTheoMa(String ma) {
        HocSinh h = timTheoMa(ma);
        if (h == null) return false;
        return danhSach.remove(h);
    }

    public List<HocSinh> getAll() { return new ArrayList<>(danhSach); }

    public boolean capNhat(HocSinh hsMoi) {
        HocSinh h = timTheoMa(hsMoi.getMaHS());
        if (h == null) return false;
        h.setHoTen(hsMoi.getHoTen());
        h.setLop(hsMoi.getLop());
        h.setNamSinh(hsMoi.getNamSinh());
        return true;
    }
}
