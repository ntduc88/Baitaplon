import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class GiaoDienChinh extends JFrame {
    private QuanLyHocSinh qlhs = new QuanLyHocSinh();
    private QuanLyDiem qld = new QuanLyDiem();

    private DefaultTableModel modelHocSinh;
    private JTable tableHocSinh;

    public GiaoDienChinh() {
        setTitle("Quan Ly Diem THCS - Demo");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Dashboard", taoPanelDashboard());
        tabs.add("Quan Ly Hoc Sinh", taoPanelQuanLyHocSinh());
        tabs.add("Quan Ly Diem", taoPanelQuanLyDiem());

        add(tabs);

        // sample data
        napDuLieuMau();
        refreshHocSinhTable();
    }

    private JPanel taoPanelDashboard() {
        JPanel p = new JPanel(new BorderLayout());
        JPanel north = new JPanel(new BorderLayout());
        JPanel info = new JPanel(new GridLayout(1, 3));
        JLabel lblTong = new JLabel("Tong hoc sinh: 0");
        JLabel lblTop = new JLabel("Top 5: -");
        JTextArea area = new JTextArea();
        area.setEditable(false);
        info.add(lblTong);
        info.add(lblTop);
        info.add(new JScrollPane(area));

        JPanel btns = new JPanel();
        JButton btnCapNhat = new JButton("Cap nhat thong ke");
        JButton btnLuu = new JButton("Luu du lieu");
        JButton btnKhoiPhuc = new JButton("Khoi phuc");
        JButton btnXuat = new JButton("Xuat CSV");
        btns.add(btnCapNhat); btns.add(btnLuu); btns.add(btnKhoiPhuc); btns.add(btnXuat);

        north.add(info, BorderLayout.CENTER);
        north.add(btns, BorderLayout.SOUTH);

        JPanel center = new JPanel(new BorderLayout());
        ChartPanel chart = new ChartPanel();
        center.add(chart, BorderLayout.CENTER);

        btnCapNhat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Map<String, Double> map = qld.tinhDTBCungTatCaHS();
                lblTong.setText("Tong hoc sinh: " + qlhs.getAll().size());
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, Double> en : UtilsThongKe.topN(map, 5)) sb.append(en.getKey()).append(": ").append(String.format("%.2f", en.getValue())).append("\n");
                lblTop.setText("Top 5: ");
                area.setText(sb.toString());
                Map<String, Integer> ph = UtilsThongKe.phanLoaiTheoDTB(map);
                chart.setData(ph);
            }
        });

        btnLuu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    LuuDuLieu.luuDanhSachHocSinh(qlhs.getAll());
                    LuuDuLieu.luuDiem(qld.getAll());
                    JOptionPane.showMessageDialog(null, "Luu thanh cong vao data/ ");
                } catch (Exception ex) { JOptionPane.showMessageDialog(null, "Luu that bai: " + ex.getMessage()); }
            }
        });

        btnKhoiPhuc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    List<HocSinh> hs = LuuDuLieu.khoiPhucHocSinh();
                    Map<String, List<Diem>> di = LuuDuLieu.khoiPhucDiem();
                    // replace
                    // clear qlhs
                    for (HocSinh h : qlhs.getAll()) qlhs.xoaTheoMa(h.getMaHS());
                    for (HocSinh h : hs) qlhs.them(h);
                    // update qld internal map
                    // we simply set all entries
                    // Note: QuanLyDiem.ds is private; but we expose via getAll()
                    Map<String, List<Diem>> map = qld.getAll();
                    map.clear();
                    map.putAll(di);
                    refreshHocSinhTable();
                    JOptionPane.showMessageDialog(null, "Khoi phuc thanh cong");
                } catch (Exception ex) { JOptionPane.showMessageDialog(null, "Khoi phuc that bai: " + ex.getMessage()); }
            }
        });

        btnXuat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    LuuDuLieu.luuDanhSachHocSinh(qlhs.getAll());
                    LuuDuLieu.luuDiem(qld.getAll());
                    JOptionPane.showMessageDialog(null, "Xuat CSV thanh cong (data/)");
                } catch (Exception ex) { JOptionPane.showMessageDialog(null, "Xuat that bai: " + ex.getMessage()); }
            }
        });

        p.add(north, BorderLayout.NORTH);
        p.add(center, BorderLayout.CENTER);
        return p;
    }

    private JPanel taoPanelQuanLyHocSinh() {
        JPanel p = new JPanel(new BorderLayout());
        modelHocSinh = new DefaultTableModel(new Object[]{"MaHS","HoTen","Lop","NamSinh"}, 0);
        tableHocSinh = new JTable(modelHocSinh);

        // enable sorting / filtering
        javax.swing.RowSorter<DefaultTableModel> dummy = null; // just for compile-time awareness
        final javax.swing.table.TableRowSorter<DefaultTableModel> sorter = new javax.swing.table.TableRowSorter<>(modelHocSinh);
        tableHocSinh.setRowSorter(sorter);

        // search / filter panel
        JPanel north = new JPanel();
        north.add(new JLabel("Loc theo lop (nhap mot phan):"));
        JTextField fLoc = new JTextField(12);
        north.add(fLoc);
        JButton btnClear = new JButton("Clear");
        north.add(btnClear);

        p.add(north, BorderLayout.NORTH);
        p.add(new JScrollPane(tableHocSinh), BorderLayout.CENTER);

        JPanel controls = new JPanel();
        JTextField fMa = new JTextField(6), fTen = new JTextField(12), fLop = new JTextField(6), fNam = new JTextField(5);
        JButton btnThem = new JButton("Them");
        JButton btnXoa = new JButton("Xoa");
        JButton btnSua = new JButton("Sua");

        controls.add(new JLabel("Ma:")); controls.add(fMa);
        controls.add(new JLabel("Ten:")); controls.add(fTen);
        controls.add(new JLabel("Lop:")); controls.add(fLop);
        controls.add(new JLabel("Nam:")); controls.add(fNam);
        controls.add(btnThem); controls.add(btnSua); controls.add(btnXoa);

        // when select row, auto-fill fields
        tableHocSinh.getSelectionModel().addListSelectionListener(evt -> {
            int viewRow = tableHocSinh.getSelectedRow();
            if (viewRow >= 0) {
                int modelRow = tableHocSinh.convertRowIndexToModel(viewRow);
                String ma = (String) modelHocSinh.getValueAt(modelRow, 0);
                String ten = (String) modelHocSinh.getValueAt(modelRow, 1);
                String lop = (String) modelHocSinh.getValueAt(modelRow, 2);
                String nam = String.valueOf(modelHocSinh.getValueAt(modelRow, 3));
                fMa.setText(ma); fTen.setText(ten); fLop.setText(lop); fNam.setText(nam);
                fMa.setEditable(false);
            }
        });

        btnClear.addActionListener(ae -> {
            tableHocSinh.clearSelection();
            fMa.setText(""); fTen.setText(""); fLop.setText(""); fNam.setText("");
            fMa.setEditable(true);
        });

        fLoc.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void apply() {
                String text = fLoc.getText();
                if (text.trim().length() == 0) sorter.setRowFilter(null);
                else sorter.setRowFilter(javax.swing.RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(text), 2));
            }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { apply(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { apply(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { apply(); }
        });

        btnThem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String ma = fMa.getText().trim();
                if (ma.isEmpty()) { JOptionPane.showMessageDialog(null, "Ma HS khong duoc rong"); return; }
                if (qlhs.timTheoMa(ma) != null) { JOptionPane.showMessageDialog(null, "Ma HS da ton tai"); return; }
                try {
                    HocSinh hs = new HocSinh(ma, fTen.getText().trim(), fLop.getText().trim(), Integer.parseInt(fNam.getText().trim()));
                    qlhs.them(hs);
                    refreshHocSinhTable();
                    btnClear.doClick();
                } catch (Exception ex) { JOptionPane.showMessageDialog(null, "Loi du lieu"); }
            }
        });

        btnXoa.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int viewRow = tableHocSinh.getSelectedRow();
                if (viewRow >= 0) {
                    int modelRow = tableHocSinh.convertRowIndexToModel(viewRow);
                    String ma = (String) modelHocSinh.getValueAt(modelRow, 0);
                    qlhs.xoaTheoMa(ma);
                    refreshHocSinhTable();
                    btnClear.doClick();
                }
            }
        });

        btnSua.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int viewRow = tableHocSinh.getSelectedRow();
                if (viewRow >= 0) {
                    int modelRow = tableHocSinh.convertRowIndexToModel(viewRow);
                    String ma = (String) modelHocSinh.getValueAt(modelRow, 0);
                    try {
                        HocSinh hs = new HocSinh(ma, fTen.getText().trim(), fLop.getText().trim(), Integer.parseInt(fNam.getText().trim()));
                        qlhs.capNhat(hs);
                        refreshHocSinhTable();
                        btnClear.doClick();
                    } catch (Exception ex) { JOptionPane.showMessageDialog(null, "Loi du lieu"); }
                }
            }
        });

        p.add(controls, BorderLayout.SOUTH);
        return p;
    }

    private JPanel taoPanelQuanLyDiem() {
        JPanel p = new JPanel(new BorderLayout());
        JPanel top = new JPanel();
        JTextField fMa = new JTextField(6), fMon = new JTextField(8), fTX = new JTextField(4), fGK = new JTextField(4), fCK = new JTextField(4);
        JButton btnThem = new JButton("Them/Sua");
        top.add(new JLabel("Ma:")); top.add(fMa);
        top.add(new JLabel("Mon:")); top.add(fMon);
        top.add(new JLabel("TX:")); top.add(fTX);
        top.add(new JLabel("GK:")); top.add(fGK);
        top.add(new JLabel("CK:")); top.add(fCK);
        top.add(btnThem);

        JTextArea area = new JTextArea(); area.setEditable(false);
        p.add(top, BorderLayout.NORTH);
        p.add(new JScrollPane(area), BorderLayout.CENTER);

        btnThem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String ma = fMa.getText().trim();
                String mon = fMon.getText().trim();
                if (ma.isEmpty() || mon.isEmpty()) { JOptionPane.showMessageDialog(null, "Nhap ma va mon"); return; }
                Double tx = parseDoubleOrNull(fTX.getText());
                Double gk = parseDoubleOrNull(fGK.getText());
                Double ck = parseDoubleOrNull(fCK.getText());
                qld.themHoacCapNhatDiem(ma, mon, tx, gk, ck);
                List<Diem> ds = qld.layDiemTheoHS(ma);
                StringBuilder sb = new StringBuilder();
                for (Diem d : ds) sb.append(d.getMon()).append(" DTB: ").append(String.format("%.2f", d.tinhDTBMon())).append("\n");
                sb.append("DTB chung: ").append(String.format("%.2f", qld.tinhDTBChungCuaHS(ma)));
                area.setText(sb.toString());
            }
        });

        return p;
    }

    private Double parseDoubleOrNull(String s) {
        try { if (s == null || s.trim().isEmpty()) return null; return Double.parseDouble(s.trim()); }
        catch (Exception e) { return null; }
    }

    private void refreshHocSinhTable() {
        modelHocSinh.setRowCount(0);
        for (HocSinh h : qlhs.getAll()) modelHocSinh.addRow(new Object[]{h.getMaHS(), h.getHoTen(), h.getLop(), String.valueOf(h.getNamSinh())});
    }

    private void napDuLieuMau() {
        qlhs.them(new HocSinh("HS001", "Nguyen Van A", "7A1", 2012));
        qlhs.them(new HocSinh("HS002", "Tran Thi B", "7A1", 2012));
        qlhs.them(new HocSinh("HS003", "Le Van C", "7A2", 2011));

        qld.themHoacCapNhatDiem("HS001", "Toan", 8.0, 7.5, 8.5);
        qld.themHoacCapNhatDiem("HS001", "Van", 7.0, 6.5, 7.0);
        qld.themHoacCapNhatDiem("HS002", "Toan", 9.0, 8.5, 9.0);
        qld.themHoacCapNhatDiem("HS003", "Toan", 5.0, 6.0, 5.5);
    }

    // Simple bar chart panel for classification counts
    private static class ChartPanel extends JPanel {
        private Map<String, Integer> data = null;

        public void setData(Map<String, Integer> data) {
            this.data = data;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (data == null || data.isEmpty()) {
                g.drawString("Chua co du lieu thong ke", 10, 20);
                return;
            }
            int w = getWidth();
            int h = getHeight();
            int padding = 40;
            int max = 1;
            for (Integer v : data.values()) if (v > max) max = v;
            int n = data.size();
            int barWidth = (w - padding*2) / Math.max(1, n);
            int i = 0;
            for (Map.Entry<String, Integer> e : data.entrySet()) {
                int val = e.getValue();
                int barHeight = (int) ((h - padding*2) * ((double)val / max));
                int x = padding + i * barWidth + 10;
                int y = h - padding - barHeight;
                g.setColor(new Color(100, 150, 220));
                g.fillRect(x, y, barWidth - 20, barHeight);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, barWidth - 20, barHeight);
                g.drawString(e.getKey() + " (" + val + ")", x, h - padding + 15);
                i++;
            }
        }
    }
}
