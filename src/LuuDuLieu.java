import java.io.*;
import java.util.*;

public class LuuDuLieu {
    private static final String DIR = "data";
    private static final String HS_FILE = DIR + File.separator + "hocsinh.csv";
    private static final String DIEM_FILE = DIR + File.separator + "diem.csv";

    public static void luuDanhSachHocSinh(List<HocSinh> ds) throws IOException {
        File d = new File(DIR);
        if (!d.exists()) d.mkdirs();
        try (PrintWriter pw = new PrintWriter(new FileWriter(HS_FILE))) {
            for (HocSinh h : ds) {
                pw.printf("%s,%s,%s,%d\n", escape(h.getMaHS()), escape(h.getHoTen()), escape(h.getLop()), h.getNamSinh());
            }
        }
    }

    public static List<HocSinh> khoiPhucHocSinh() throws IOException {
        List<HocSinh> res = new ArrayList<>();
        File f = new File(HS_FILE);
        if (!f.exists()) return res;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = splitCSV(line);
                if (parts.length >= 4) {
                    try {
                        res.add(new HocSinh(unescape(parts[0]), unescape(parts[1]), unescape(parts[2]), Integer.parseInt(parts[3])));
                    } catch (Exception ex) { /* skip invalid */ }
                }
            }
        }
        return res;
    }

    public static void luuDiem(Map<String, List<Diem>> ds) throws IOException {
        File d = new File(DIR);
        if (!d.exists()) d.mkdirs();
        try (PrintWriter pw = new PrintWriter(new FileWriter(DIEM_FILE))) {
            for (Map.Entry<String, List<Diem>> en : ds.entrySet()) {
                String ma = en.getKey();
                for (Diem di : en.getValue()) {
                    StringBuilder txs = new StringBuilder();
                    for (Double t : di.getDiemTX()) { if (txs.length()>0) txs.append("|"); txs.append(t); }
                    pw.printf("%s,%s,%s,%s\n", escape(ma), escape(di.getMon()), txs.toString(), valueOrEmpty(di.getDiemGK()) + ":" + valueOrEmpty(di.getDiemCK()));
                }
            }
        }
    }

    public static Map<String, List<Diem>> khoiPhucDiem() throws IOException {
        Map<String, List<Diem>> res = new HashMap<>();
        File f = new File(DIEM_FILE);
        if (!f.exists()) return res;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                // format: ma,mon,tx1|tx2|...,gk:ck
                String[] parts = splitCSV(line);
                if (parts.length >= 4) {
                    String ma = unescape(parts[0]);
                    String mon = unescape(parts[1]);
                    String txs = parts[2];
                    String[] gkck = parts[3].split(":");
                    Diem d = new Diem(ma, mon);
                    if (!txs.isEmpty()) {
                        for (String s : txs.split("\\|")) {
                            try { d.themDiemTX(Double.parseDouble(s)); } catch (Exception ex) {}
                        }
                    }
                    if (gkck.length>=1 && !gkck[0].isEmpty()) try { d.setDiemGK(Double.parseDouble(gkck[0])); } catch(Exception ex) {}
                    if (gkck.length>=2 && !gkck[1].isEmpty()) try { d.setDiemCK(Double.parseDouble(gkck[1])); } catch(Exception ex) {}
                    res.computeIfAbsent(ma, k -> new ArrayList<>()).add(d);
                }
            }
        }
        return res;
    }

    private static String valueOrEmpty(Double d) { return d == null ? "" : d.toString(); }

    // simple CSV helpers (no quotes support required for demo)
    private static String escape(String s) { return s == null ? "" : s.replace("\n"," ").replace(",",";"); }
    private static String unescape(String s) { return s == null ? "" : s.replace(";",","); }
    private static String[] splitCSV(String line) { return line.split(",", -1); }
}
