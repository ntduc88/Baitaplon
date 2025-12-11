import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuanLyDiem {
    // map maHS -> list diem (each entry is one subject)
    private Map<String, List<Diem>> ds = new HashMap<>();

    public void themHoacCapNhatDiem(String maHS, String mon, Double tx, Double gk, Double ck) {
        List<Diem> list = ds.get(maHS);
        if (list == null) {
            list = new ArrayList<>();
            ds.put(maHS, list);
        }
        Diem target = null;
        for (Diem d : list) if (d.getMon().equalsIgnoreCase(mon)) { target = d; break; }
        if (target == null) {
            target = new Diem(maHS, mon);
            list.add(target);
        }
        if (tx != null) target.themDiemTX(tx);
        if (gk != null) target.setDiemGK(gk);
        if (ck != null) target.setDiemCK(ck);
    }

    public List<Diem> layDiemTheoHS(String maHS) {
        List<Diem> l = ds.get(maHS);
        if (l == null) return new ArrayList<>();
        return new ArrayList<>(l);
    }

    public double tinhDTBChungCuaHS(String maHS) {
        List<Diem> l = ds.get(maHS);
        if (l == null || l.isEmpty()) return 0;
        double sum = 0;
        int n = 0;
        for (Diem d : l) {
            sum += d.tinhDTBMon();
            n++;
        }
        return n == 0 ? 0 : sum / n;
    }

    public Map<String, Double> tinhDTBCungTatCaHS() {
        Map<String, Double> res = new HashMap<>();
        for (String ma : ds.keySet()) res.put(ma, tinhDTBChungCuaHS(ma));
        return res;
    }

    public Map<String, List<Diem>> getAll() { return ds; }
}
