import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtilsThongKe {
    public static Map<String, Integer> phanLoaiTheoDTB(Map<String, Double> mapDTB) {
        Map<String, Integer> res = new HashMap<>();
        for (Double dtb : mapDTB.values()) {
            String loai = xepLoai(dtb);
            res.put(loai, res.getOrDefault(loai, 0) + 1);
        }
        return res;
    }

    public static String xepLoai(double dtb) {
        if (dtb >= 8.0) return "Gioi";
        if (dtb >= 6.5) return "Kha";
        if (dtb >= 5.0) return "Trung binh";
        return "Yeu";
    }

    public static List<Map.Entry<String, Double>> topN(Map<String, Double> mapDTB, int n) {
        List<Map.Entry<String, Double>> list = new ArrayList<>(mapDTB.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            public int compare(Map.Entry<String, Double> a, Map.Entry<String, Double> b) {
                return Double.compare(b.getValue(), a.getValue());
            }
        });
        if (list.size() > n) return list.subList(0, n);
        return list;
    }

    public static Map<String, Double> thongKeTheoMon(Map<String, List<Diem>> allDiem) {
        Map<String, Double> avg = new HashMap<>();
        Map<String, Integer> count = new HashMap<>();
        for (List<Diem> l : allDiem.values()) {
            for (Diem d : l) {
                double dtb = d.tinhDTBMon();
                avg.put(d.getMon(), avg.getOrDefault(d.getMon(), 0.0) + dtb);
                count.put(d.getMon(), count.getOrDefault(d.getMon(), 0) + 1);
            }
        }
        for (String mon : new ArrayList<>(avg.keySet())) {
            avg.put(mon, avg.get(mon) / count.get(mon));
        }
        return avg;
    }
}
