# Ứng Dụng Quản Lý Điểm THCS (Demo)

Ứng dụng mẫu bằng Java (Swing) để quản lý học sinh và điểm cho trường trung học cơ sở.

Tính năng chính:
- Quản lý học sinh: thêm / sửa / xóa, kiểm tra trùng mã HS
- Quản lý điểm: thêm nhiều điểm Thường Xuyên (TX), Giữa kỳ (GK), Cuối kỳ (CK)
- Tính DTB môn theo công thức trọng số
- Dashboard: thống kê tổng HS, Top 5 theo DTB, biểu đồ phân loại học lực
- Lưu / khôi phục dữ liệu vào thư mục `data/` (CSV)
- Xuất báo cáo CSV

Files chính trong `src/`:
- `App.java` - điểm bắt đầu, khởi chạy UI
- `GiaoDienChinh.java` - giao diện Swing với Dashboard, Quản lý học sinh, Quản lý điểm
- `HocSinh.java`, `Diem.java` - các lớp mô hình
- `QuanLyHocSinh.java`, `QuanLyDiem.java` - logic quản lý (collection)
- `LuuDuLieu.java` - lưu / khôi phục CSV trong `data/`
- `UtilsThongKe.java` - hàm thống kê

Hướng dẫn biên dịch & chạy (Windows PowerShell):

1) Cài đặt JDK (nếu chưa có): tải OpenJDK / Oracle JDK và thêm `javac`/`java` vào `PATH`.

2) Biên dịch:
```powershell
cd "C:\Users\admin\Downloads\QLDiemTHCS\quanlydiemthcs"
if (!(Test-Path out)) { New-Item -ItemType Directory out | Out-Null }
javac -d out src\*.java
```

3) Chạy:
```powershell
cd "C:\Users\admin\Downloads\QLDiemTHCS\quanlydiemthcs"
java -cp out App
```

Dữ liệu sẽ được lưu vào thư mục `data/` như các file `hocsinh.csv` và `diem.csv` khi chọn "Luu du lieu" hoặc "Xuat CSV".

Gợi ý nâng cấp tiếp theo:
- Lưu/đọc bằng JSON hoặc SQLite để hỗ trợ cấu trúc phức tạp hơn
- Thêm biểu đồ đẹp hơn bằng thư viện JFreeChart
- Thêm xác thực form, tìm kiếm, lọc theo lớp


