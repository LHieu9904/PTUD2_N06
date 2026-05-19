/*USE master;
GO

IF DB_ID('QLKSLuxury2') IS NOT NULL
BEGIN
    ALTER DATABASE QLKSLuxury2 SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE QLKSLuxury2;
END
GO

CREATE DATABASE QLKSLuxury2;
GO
USE QLKSLuxury2;
GO

-- ================= CHUCVU =================
CREATE TABLE ChucVu (
    MaChucVu NVARCHAR(10) PRIMARY KEY,
    TenChucVu NVARCHAR(50) NOT NULL,
    MoTa NVARCHAR(255)
);

-- ================= NHANVIEN =================
CREATE TABLE NhanVien (
    MaNV NVARCHAR(10) PRIMARY KEY,
    HoTen NVARCHAR(100) NOT NULL,
    GioiTinh INT NOT NULL CHECK (GioiTinh IN (0,1)), -- 1 Nam, 0 Nữ
    CCCD NVARCHAR(12) UNIQUE,
    NgaySinh DATE,
    TrangThaiLamViec NVARCHAR(50),
    MaChucVu NVARCHAR(10) NOT NULL,
    Luong DECIMAL(12,2) CHECK (Luong >= 0),
    AnhNhanVien NVARCHAR(255),
    Email NVARCHAR(150) UNIQUE,
    SDT NVARCHAR(10) UNIQUE,

    CONSTRAINT FK_NV_ChucVu FOREIGN KEY (MaChucVu)
        REFERENCES ChucVu(MaChucVu)
);

-- ================= TAIKHOAN (1-1) =================
CREATE TABLE TaiKhoan (
    MaTK NVARCHAR(10) PRIMARY KEY,
    TenDangNhap NVARCHAR(50) UNIQUE NOT NULL,
    MatKhau NVARCHAR(100) NOT NULL,
    MaNV NVARCHAR(10) UNIQUE NOT NULL, -- 🔥 đảm bảo 1-1
    TrangThai NVARCHAR(20) NOT NULL,

    CONSTRAINT FK_TK_NV FOREIGN KEY (MaNV)
        REFERENCES NhanVien(MaNV),

    CONSTRAINT CK_TK_TrangThai CHECK (TrangThai IN (N'Hoạt động', N'Khóa'))
);

-- ================= KHACHHANG =================
CREATE TABLE KhachHang (
    MaKH NVARCHAR(10) PRIMARY KEY,
    HoTen NVARCHAR(100),
    SDT NVARCHAR(15) UNIQUE,
    CCCD NVARCHAR(12) UNIQUE,
    GioiTinh INT CHECK (GioiTinh IN (0,1))
);

-- ================= LOAIPHONG =================
CREATE TABLE LoaiPhong (
    MaLP NVARCHAR(10) PRIMARY KEY,
    TenLP NVARCHAR(50),
    GiaGioDau DECIMAL(12,2),
    GiaGioTiepTheo DECIMAL(12,2),
    GiaCaNgay DECIMAL(12,2)
);

-- ================= PHONG =================
CREATE TABLE Phong (
    MaPhong NVARCHAR(10) PRIMARY KEY,
    TrangThai NVARCHAR(50),
    Tang INT,
    MaLP NVARCHAR(10),

    CONSTRAINT FK_Phong_LP FOREIGN KEY (MaLP)
        REFERENCES LoaiPhong(MaLP)
);

-- ================= DICHVU =================
CREATE TABLE DichVu (
    MaDichVu NVARCHAR(10) PRIMARY KEY,
    TenDichVu NVARCHAR(100),
    DonGia DECIMAL(12,2),
    TrangThai NVARCHAR(50)
);

-- ================= PHIEUDATPHONG =================
CREATE TABLE PhieuDatPhong (
    MaPhieuDatPhong NVARCHAR(10) PRIMARY KEY,
    ThoiGianDat DATETIME DEFAULT GETDATE(),
    MaKH NVARCHAR(10),
    MaNV NVARCHAR(10),

    FOREIGN KEY (MaKH) REFERENCES KhachHang(MaKH),
    FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV)
);

-- ================= CHITIETPHIEUDATPHONG =================
CREATE TABLE ChiTietPhieuDatPhong (
    MaPhieuDatPhong NVARCHAR(10),
    MaPhong NVARCHAR(10),
    ThoiGianNhan DATETIME,
    ThoiGianTra DATETIME,
    SoLuong INT,

    PRIMARY KEY (MaPhieuDatPhong, MaPhong, ThoiGianNhan),

    FOREIGN KEY (MaPhieuDatPhong) REFERENCES PhieuDatPhong(MaPhieuDatPhong),
    FOREIGN KEY (MaPhong) REFERENCES Phong(MaPhong)
);

-- ================= KHUYENMAI =================
CREATE TABLE KhuyenMai (
    MaKhuyenMai NVARCHAR(10) PRIMARY KEY,
    TenKhuyenMai NVARCHAR(100),
    PhanTramGiam DECIMAL(5,2),
    NgayBatDau DATE,
    NgayKetThuc DATE,
    TrangThai NVARCHAR(50)
);

-- ================= THUE =================
CREATE TABLE Thue (
    MaThue NVARCHAR(10) PRIMARY KEY,
    TenThue NVARCHAR(50),
    PhanTramThue DECIMAL(5,2)
);

-- ================= HOADONPHONG =================
CREATE TABLE HoaDonPhong (
    MaHoaDonPhong NVARCHAR(10) PRIMARY KEY,
    NgayLapHoaDon DATETIME DEFAULT GETDATE(),
    MaPhieuDatPhong NVARCHAR(10) UNIQUE,
    MaNV NVARCHAR(10),
    MaKH NVARCHAR(10),
    MaKhuyenMai NVARCHAR(10),
    MaThue NVARCHAR(10),
    TongTien DECIMAL(14,2),
    TienThue DECIMAL(14,2),
    TrangThai NVARCHAR(30),

    FOREIGN KEY (MaPhieuDatPhong) REFERENCES PhieuDatPhong(MaPhieuDatPhong),
    FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV),
    FOREIGN KEY (MaKH) REFERENCES KhachHang(MaKH),
    FOREIGN KEY (MaKhuyenMai) REFERENCES KhuyenMai(MaKhuyenMai),
    FOREIGN KEY (MaThue) REFERENCES Thue(MaThue)
);

-- ================= CHITIETHOADONPHONG =================
CREATE TABLE ChiTietHoaDonPhong (
    MaHoaDonPhong NVARCHAR(10),
    MaPhong NVARCHAR(10),
    ThoiGianNhan DATETIME,
    ThoiGianTra DATETIME,
    CachThue NVARCHAR(50),
    SoLuongNguoi INT,
    TrangThaiSuDung NVARCHAR(50),
    TrangThaiThanhToan NVARCHAR(50),

    PRIMARY KEY (MaHoaDonPhong, MaPhong, ThoiGianNhan),

    FOREIGN KEY (MaHoaDonPhong) REFERENCES HoaDonPhong(MaHoaDonPhong),
    FOREIGN KEY (MaPhong) REFERENCES Phong(MaPhong)
);

-- ================= HOADONDICHVU =================
CREATE TABLE HoaDonDichVu (
    MaHoaDonDichVu NVARCHAR(20) PRIMARY KEY,
    MaHoaDonPhong NVARCHAR(10),

    FOREIGN KEY (MaHoaDonPhong) REFERENCES HoaDonPhong(MaHoaDonPhong)
);

-- ================= CHITIETHOADONDICHVU =================
CREATE TABLE ChiTietHoaDonDichVu (
    MaHoaDonDichVu NVARCHAR(20),
    MaDichVu NVARCHAR(10),
    ThoiDiemSuDung DATETIME,
    SoLuong INT,
    DonGia DECIMAL(12,2),
    ThanhTien DECIMAL(12,2),

    PRIMARY KEY (MaHoaDonDichVu, MaDichVu, ThoiDiemSuDung),

    FOREIGN KEY (MaHoaDonDichVu) REFERENCES HoaDonDichVu(MaHoaDonDichVu),
    FOREIGN KEY (MaDichVu) REFERENCES DichVu(MaDichVu)
);
INSERT INTO ChucVu VALUES
('CV01', N'Quản lý', N'Quản lý hệ thống'),
('CV02', N'Lễ tân', N'Tiếp khách');

-- =====================================================
-- NHÂN VIÊN
-- =====================================================

INSERT INTO NhanVien VALUES
('NV01',N'Admin',1,'111111111111','1995-01-01',N'Đang làm','CV01',15000000,'a.jpg','admin@gmail.com','0900000001'),
('NV02',N'Trần Thị B',0,'222222222222','1996-02-02',N'Đang làm','CV02',10000000,'b.jpg','b@gmail.com','0900000002'),
('NV03',N'Lê Văn C',1,'333333333333','1997-03-03',N'Đang làm','CV02',9000000,'c.jpg','c@gmail.com','0900000003');

-- =====================================================
-- TÀI KHOẢN
-- =====================================================

INSERT INTO TaiKhoan VALUES
('TK01','admin','123','NV01',N'Hoạt động'),
('TK02','lt1','123','NV02',N'Hoạt động'),
('TK03','lt2','123','NV03',N'Hoạt động');

-- =====================================================
-- KHÁCH HÀNG
-- =====================================================

-- =====================================================
-- KHÁCH HÀNG
-- =====================================================

INSERT INTO KhachHang VALUES
('KH01',N'Nguyễn Văn A','0900000011','111111111112',1),
('KH02',N'Trần Thị B','0900000012','222222222223',0),
('KH03',N'Lê Văn C','0900000013','333333333334',1),
('KH04',N'Phạm Thị D','0900000014','444444444445',0),
('KH05',N'Hoàng Văn E','0900000015','555555555556',1),
('KH06',N'Võ Thị F','0900000016','666666666667',0);

-- =====================================================
-- LOẠI PHÒNG
-- =====================================================

INSERT INTO LoaiPhong VALUES
('LP01',N'Đơn',80000,50000,400000),
('LP02',N'Đôi',120000,80000,600000),
('LP03',N'VIP',250000,200000,1200000);

-- =====================================================
-- PHÒNG
-- =====================================================

INSERT INTO Phong VALUES
                      ('P101', N'Đang thuê', 1, 'LP01'),
                      ('P201', N'Đang thuê', 2, 'LP02'),
                      ('P301', N'Đang thuê', 3, 'LP03'),

                      ('P102', N'Đã đặt', 1, 'LP01'),
                      ('P202', N'Đã đặt', 2, 'LP02'),
                      ('P302', N'Đã đặt', 3, 'LP03'),

                      ('P103', N'Trống', 1, 'LP01'),
                      ('P203', N'Trống', 2, 'LP02'),
                      ('P303', N'Trống', 3, 'LP03');
-- =====================================================
-- DỊCH VỤ
-- =====================================================

INSERT INTO DichVu VALUES
('DV11',N'Nước suối',10000,N'Còn'),
('DV12',N'Trà sữa',35000,N'Còn'),
('DV13',N'Nước ép',40000,N'Còn'),
('DV14',N'Bún bò',50000,N'Còn'),
('DV15',N'Phở bò',55000,N'Còn');

-- =====================================================
-- PHIẾU ĐẶT PHÒNG
-- =====================================================

INSERT INTO PhieuDatPhong VALUES
                              ('DP01',GETDATE(),'KH01','NV02'),
                              ('DP02',GETDATE(),'KH02','NV03'),
                              ('DP03',GETDATE(),'KH03','NV02'),

                              ('DP04',GETDATE(),'KH04','NV01'),
                              ('DP05',GETDATE(),'KH05','NV02'),
                              ('DP06',GETDATE(),'KH06','NV03');

-- =====================================================
-- CHI TIẾT PHIẾU ĐẶT PHÒNG
-- =====================================================

INSERT INTO ChiTietPhieuDatPhong VALUES
-- đang thuê
('DP01','P101',GETDATE(),DATEADD(HOUR,5,GETDATE()),2),
('DP02','P201',GETDATE(),DATEADD(HOUR,6,GETDATE()),2),
('DP03','P301',GETDATE(),DATEADD(HOUR,24,GETDATE()),3),

-- đã đặt (chưa nhận)
('DP04','P102',DATEADD(HOUR,3,GETDATE()),NULL,2),
('DP05','P202',DATEADD(HOUR,5,GETDATE()),NULL,2),
('DP06','P302',DATEADD(HOUR,8,GETDATE()),NULL,3);
-- =====================================================
-- KHUYẾN MÃI
-- =====================================================

INSERT INTO KhuyenMai VALUES
('KM01',N'Giảm 10%',10,'2024-01-01','2026-01-01',N'Áp dụng');

-- =====================================================
-- THUẾ
-- =====================================================

INSERT INTO Thue VALUES
('T01',N'VAT 8%',8);

-- =====================================================
-- HÓA ĐƠN PHÒNG
-- CHỈ tạo cho phòng đang thuê
-- KHÔNG tạo cho phòng đã đặt
-- =====================================================

INSERT INTO HoaDonPhong VALUES
                            ('HD01',GETDATE(),'DP01','NV02','KH01','KM01','T01',500000,40000,N'Chưa thanh toán'),
                            ('HD02',GETDATE(),'DP02','NV03','KH02','KM01','T01',700000,56000,N'Chưa thanh toán'),
                            ('HD03',GETDATE(),'DP03','NV02','KH03','KM01','T01',1200000,96000,N'Chưa thanh toán');

-- =====================================================
-- CHI TIẾT HÓA ĐƠN PHÒNG
-- CHỈ có cho phòng đang thuê
-- =====================================================

INSERT INTO ChiTietHoaDonPhong VALUES
                                   ('HD01','P101',GETDATE(),DATEADD(HOUR,5,GETDATE()),N'Giờ',2,N'Đang dùng',N'Chưa TT'),
                                   ('HD02','P201',GETDATE(),DATEADD(HOUR,6,GETDATE()),N'Giờ',2,N'Đang dùng',N'Chưa TT'),
                                   ('HD03','P301',GETDATE(),DATEADD(HOUR,24,GETDATE()),N'Ngày',3,N'Đang dùng',N'Chưa TT');
-- =====================================================
-- HÓA ĐƠN DỊCH VỤ
-- =====================================================

INSERT INTO HoaDonDichVu VALUES
('HDDV01','HD01'),
('HDDV02','HD02'),
('HDDV03','HD03');

-- =====================================================
-- CHI TIẾT HÓA ĐƠN DỊCH VỤ
-- =====================================================

INSERT INTO ChiTietHoaDonDichVu VALUES
('HDDV01','DV11',GETDATE(),2,10000,20000),
('HDDV01','DV12',DATEADD(HOUR,1,GETDATE()),1,35000,35000),

('HDDV02','DV13',GETDATE(),3,40000,120000),

('HDDV03','DV14',GETDATE(),2,50000,100000);
SELECT p.MaPhong, kh.HoTen
FROM Phong p
LEFT JOIN ChiTietHoaDonPhong ct 
    ON p.MaPhong = ct.MaPhong
    AND ct.TrangThaiSuDung = N'Đang dùng'
LEFT JOIN HoaDonPhong hd 
    ON ct.MaHoaDonPhong = hd.MaHoaDonPhong
LEFT JOIN KhachHang kh 
    ON hd.MaKH = kh.MaKH
    SELECT * FROM HoaDonPhong
ALTER TABLE HoaDonPhong
ADD NgayLap DATETIME
SELECT * FROM KhachHang
SELECT * FROM PhieuDatPhong
SELECT * FROM ChiTietPhieuDatPhong
SELECT * FROM NhanVien
SELECT MaPhong, TrangThai
FROM Phong
ALTER TABLE HoaDonPhong
    DROP CONSTRAINT UQ__HoaDonPh__C916A186D3907AB0;
ALTER TABLE HoaDonPhong
    DROP CONSTRAINT UQ__HoaDonPh__C916A186535B94D4;
SELECT *
FROM HoaDonPhong
ORDER BY NgayLapHoaDon DESC
-- KIỂM TRA DATABASE

SELECT
    p.MaPhong,
    pd.MaPhieuDatPhong,
    hd.MaHoaDonPhong,
    hd.TrangThai
FROM Phong p
LEFT JOIN ChiTietPhieuDatPhong ct
    ON p.MaPhong = ct.MaPhong
LEFT JOIN PhieuDatPhong pd
    ON ct.MaPhieuDatPhong = pd.MaPhieuDatPhong
LEFT JOIN ChiTietHoaDonPhong cthd
    ON p.MaPhong = cthd.MaPhong
LEFT JOIN HoaDonPhong hd
    ON cthd.MaHoaDonPhong = hd.MaHoaDonPhong
ORDER BY p.MaPhong
*/
USE master;
GO

IF DB_ID('QLKSLuxury2') IS NOT NULL
    BEGIN
        ALTER DATABASE QLKSLuxury2
            SET SINGLE_USER WITH ROLLBACK IMMEDIATE;

        DROP DATABASE QLKSLuxury2;
    END
GO

CREATE DATABASE QLKSLuxury2;
GO

USE QLKSLuxury2;
GO

-- =====================================================
-- CHỨC VỤ
-- =====================================================

CREATE TABLE ChucVu (
    MaChucVu NVARCHAR(10) PRIMARY KEY,
    TenChucVu NVARCHAR(50) NOT NULL,
    MoTa NVARCHAR(255)
);

INSERT INTO ChucVu VALUES
    ('CV01',N'Quản lý',N'Quản lý hệ thống'),
    ('CV02',N'Lễ tân',N'Tiếp khách');

-- =====================================================
-- NHÂN VIÊN
-- =====================================================

CREATE TABLE NhanVien (
    MaNV NVARCHAR(10) PRIMARY KEY,
    HoTen NVARCHAR(100) NOT NULL,
    GioiTinh INT CHECK(GioiTinh IN(0,1)),
    CCCD NVARCHAR(12) UNIQUE,
    NgaySinh DATE,
    TrangThaiLamViec NVARCHAR(50),
    MaChucVu NVARCHAR(10),
    Luong DECIMAL(12,2) CHECK(Luong >= 0),
    AnhNhanVien NVARCHAR(255),
    Email NVARCHAR(100) UNIQUE,
    SDT NVARCHAR(10) UNIQUE,

    FOREIGN KEY (MaChucVu)
    REFERENCES ChucVu(MaChucVu)
);

INSERT INTO NhanVien VALUES
('NV1250001',N'Nguyễn Văn A',1,'079201000001','1995-01-01',N'Đang làm','CV01',15000000,'a.jpg','a@gmail.com','0900000001'),
('NV0250001',N'Trần Thị B',0,'079301000002','2001-02-02',N'Đang làm','CV02',9000000,'b.jpg','b@gmail.com','0900000002'),
('NV0250002',N'Lê Văn C',1,'079201000003','2001-03-03',N'Đang làm','CV02',9000000,'c.jpg','c@gmail.com','0900000003'),
('NV0250003',N'Phạm Thị D',0,'079301000004','2001-04-04',N'Đang làm','CV02',9000000,'d.jpg','d@gmail.com','0900000004'),
('NV0250004',N'Hoàng Văn E',1,'079201000005','2001-05-05',N'Đang làm','CV02',9000000,'e.jpg','e@gmail.com','0900000005'),
('NV0250005',N'Võ Thị F',0,'079301000006','2001-06-06',N'Đang làm','CV02',9000000,'f.jpg','f@gmail.com','0900000006'),
('NV0250006',N'Đặng Văn G',1,'079201000007','2001-07-07',N'Đang làm','CV02',9000000,'g.jpg','g@gmail.com','0900000007'),
('NV0250007',N'Bùi Thị H',0,'079301000008','2001-08-08',N'Đang làm','CV02',9000000,'h.jpg','h@gmail.com','0900000008'),
('NV0250008',N'Đỗ Văn I',1,'079201000009','2001-09-09',N'Đang làm','CV02',9000000,'i.jpg','i@gmail.com','0900000009'),
('NV0250009',N'Ngô Thị K',0,'079301000010','2001-10-10',N'Đang làm','CV02',9000000,'k.jpg','k@gmail.com','0900000010');

-- =====================================================
-- TÀI KHOẢN
-- =====================================================

CREATE TABLE TaiKhoan (
MaTK NVARCHAR(10) PRIMARY KEY,
TenDangNhap NVARCHAR(50) UNIQUE,
MatKhau NVARCHAR(100),
MaNV NVARCHAR(10) UNIQUE,
TrangThai NVARCHAR(20),

FOREIGN KEY (MaNV)
REFERENCES NhanVien(MaNV)
);

INSERT INTO TaiKhoan VALUES
('TK001','admin','Admin@123','NV1250001',N'Hoạt động'),
('TK002','lt1','LeTan@123','NV0250001',N'Hoạt động'),
('TK003','lt2','LeTan@123','NV0250002',N'Hoạt động'),
('TK004','lt3','LeTan@123','NV0250003',N'Hoạt động'),
('TK005','lt4','LeTan@123','NV0250004',N'Hoạt động'),
('TK006','lt5','LeTan@123','NV0250005',N'Hoạt động'),
('TK007','lt6','LeTan@123','NV0250006',N'Hoạt động'),
('TK008','lt7','LeTan@123','NV0250007',N'Hoạt động'),
('TK009','lt8','LeTan@123','NV0250008',N'Hoạt động'),
('TK010','lt9','LeTan@123','NV0250009',N'Hoạt động');

-- =====================================================
-- KHÁCH HÀNG
-- =====================================================

CREATE TABLE KhachHang (
MaKH NVARCHAR(10) PRIMARY KEY,
HoTen NVARCHAR(100),
SDT NVARCHAR(10) UNIQUE,
CCCD NVARCHAR(12) UNIQUE,
GioiTinh INT CHECK(GioiTinh IN(0,1))
);

INSERT INTO KhachHang VALUES
('KH0001',N'Nguyễn Minh A','0911111111','079201111111',1),
('KH0002',N'Trần Thị B','0911111112','079301111112',0),
('KH0003',N'Lê Văn C','0911111113','079201111113',1),
('KH0004',N'Phạm Thị D','0911111114','079301111114',0),
('KH0005',N'Hoàng Văn E','0911111115','079201111115',1),
('KH0006',N'Võ Thị F','0911111116','079301111116',0),
('KH0007',N'Đặng Văn G','0911111117','079201111117',1),
('KH0008',N'Bùi Thị H','0911111118','079301111118',0),
('KH0009',N'Đỗ Văn I','0911111119','079201111119',1),
('KH0010',N'Ngô Thị K','0911111120','079301111120',0);

-- =====================================================
-- LOẠI PHÒNG
-- =====================================================

CREATE TABLE LoaiPhong (
MaLP NVARCHAR(10) PRIMARY KEY,
TenLP NVARCHAR(50),
GiaGioDau DECIMAL(12,2),
GiaGioTiepTheo DECIMAL(12,2),
GiaCaNgay DECIMAL(12,2)
);

INSERT INTO LoaiPhong VALUES
('LP01',N'Đơn',80000,50000,400000),
('LP02',N'Đôi',120000,80000,600000),
('LP03',N'VIP',250000,200000,1200000)

-- =====================================================
-- PHÒNG
-- =====================================================

CREATE TABLE Phong (
MaPhong NVARCHAR(10) PRIMARY KEY,
TrangThai NVARCHAR(50),
Tang INT,
MaLP NVARCHAR(10),

FOREIGN KEY (MaLP)
REFERENCES LoaiPhong(MaLP)
);

INSERT INTO Phong VALUES
('P101',N'Đang thuê',1,'LP01'),
('P102',N'Đã đặt',1,'LP02'),
('P103',N'Trống',1,'LP03'),
('P201',N'Đang thuê',2,'LP01'),
('P202',N'Đã đặt',2,'LP02'),
('P203',N'Trống',2,'LP03'),
('P301',N'Đang thuê',3,'LP01'),
('P302',N'Đã đặt',3,'LP01'),
('P303',N'Trống',3,'LP02'),
('P401',N'Trống',4,'LP03');

-- =====================================================
-- DỊCH VỤ
-- =====================================================

CREATE TABLE DichVu (
MaDichVu NVARCHAR(10) PRIMARY KEY,
TenDichVu NVARCHAR(100),
DonGia DECIMAL(12,2) CHECK(DonGia > 0),
TrangThai NVARCHAR(50)
);

INSERT INTO DichVu VALUES
('DV001',N'Nước suối',10000,N'Còn'),
('DV002',N'Coca',15000,N'Còn'),
('DV003',N'Pepsi',15000,N'Còn'),
('DV004',N'Bún bò',50000,N'Còn'),
('DV005',N'Phở bò',55000,N'Còn'),
('DV006',N'Cơm chiên',60000,N'Còn'),
('DV007',N'Mì xào',45000,N'Còn'),
('DV008',N'Trà sữa',35000,N'Còn'),
('DV009',N'Nước ép',40000,N'Còn'),
('DV010',N'Cafe',30000,N'Còn');
-- =====================================================
-- PHIẾU ĐẶT PHÒNG
-- =====================================================

CREATE TABLE PhieuDatPhong (
MaPhieuDatPhong NVARCHAR(10) PRIMARY KEY,
ThoiGianDat DATETIME DEFAULT GETDATE(),
MaKH NVARCHAR(10),
MaNV NVARCHAR(10),

FOREIGN KEY (MaKH)
REFERENCES KhachHang(MaKH),

FOREIGN KEY (MaNV)
REFERENCES NhanVien(MaNV)
);

INSERT INTO PhieuDatPhong VALUES
('DP001',GETDATE(),'KH0001','NV0250001'),
('DP002',GETDATE(),'KH0002','NV0250002'),
('DP003',GETDATE(),'KH0003','NV0250003'),
('DP004',GETDATE(),'KH0004','NV0250004'),
('DP005',GETDATE(),'KH0005','NV0250005'),
('DP006',GETDATE(),'KH0006','NV0250006'),
('DP007',GETDATE(),'KH0007','NV0250007'),
('DP008',GETDATE(),'KH0008','NV0250008'),
('DP009',GETDATE(),'KH0009','NV0250009'),
('DP010',GETDATE(),'KH0010','NV0250001');

-- =====================================================
-- CHI TIẾT PHIẾU ĐẶT PHÒNG
-- =====================================================

CREATE TABLE ChiTietPhieuDatPhong (
MaPhieuDatPhong NVARCHAR(10),
MaPhong NVARCHAR(10),
ThoiGianNhan DATETIME,
ThoiGianTra DATETIME,
SoLuong INT CHECK(SoLuong > 0),
PRIMARY KEY (
             MaPhieuDatPhong,
             MaPhong,
             ThoiGianNhan
    ),
FOREIGN KEY (MaPhieuDatPhong)
    REFERENCES PhieuDatPhong(MaPhieuDatPhong),

FOREIGN KEY (MaPhong)
    REFERENCES Phong(MaPhong)

);

INSERT INTO ChiTietPhieuDatPhong VALUES
('DP001','P101',GETDATE(),DATEADD(HOUR,5,GETDATE()),2),
('DP002','P102',GETDATE(),DATEADD(HOUR,6,GETDATE()),2),
('DP003','P103',GETDATE(),DATEADD(HOUR,7,GETDATE()),1),
('DP004','P201',GETDATE(),DATEADD(HOUR,8,GETDATE()),3),
('DP005','P202',GETDATE(),DATEADD(HOUR,9,GETDATE()),2),
('DP006','P203',GETDATE(),DATEADD(HOUR,10,GETDATE()),2),
('DP007','P301',GETDATE(),DATEADD(HOUR,11,GETDATE()),4),
('DP008','P302',GETDATE(),DATEADD(HOUR,12,GETDATE()),2),
('DP009','P303',GETDATE(),DATEADD(HOUR,13,GETDATE()),1),
('DP010','P401',GETDATE(),DATEADD(HOUR,14,GETDATE()),2);

-- =====================================================
-- KHUYẾN MÃI
-- =====================================================

CREATE TABLE KhuyenMai (
MaKhuyenMai NVARCHAR(10) PRIMARY KEY,
TenKhuyenMai NVARCHAR(100),
PhanTramGiam DECIMAL(5,2)
CHECK(PhanTramGiam >= 0
AND PhanTramGiam <= 100),

NgayBatDau DATE,
NgayKetThuc DATE,
TrangThai NVARCHAR(50),

CHECK (NgayBatDau <= NgayKetThuc)
);

INSERT INTO KhuyenMai VALUES
('KM001',N'Giảm 5%',5,'2025-01-01','2026-01-01',N'Áp dụng'),
('KM002',N'Giảm 10%',10,'2025-01-01','2026-01-01',N'Áp dụng'),
('KM003',N'Giảm 15%',15,'2025-01-01','2026-01-01',N'Áp dụng'),
('KM004',N'Giảm 20%',20,'2025-01-01','2026-01-01',N'Áp dụng'),
('KM005',N'Giảm 25%',25,'2025-01-01','2026-01-01',N'Áp dụng'),
('KM006',N'Giảm 30%',30,'2025-01-01','2026-01-01',N'Áp dụng'),
('KM007',N'Giảm 35%',35,'2025-01-01','2026-01-01',N'Áp dụng'),
('KM008',N'Giảm 40%',40,'2025-01-01','2026-01-01',N'Áp dụng'),
('KM009',N'Giảm 45%',45,'2025-01-01','2026-01-01',N'Áp dụng'),
('KM010',N'Giảm 50%',50,'2025-01-01','2026-01-01',N'Áp dụng');

-- =====================================================
-- THUẾ
-- =====================================================

CREATE TABLE Thue (
MaThue NVARCHAR(10) PRIMARY KEY,
TenThue NVARCHAR(50),
PhanTramThue DECIMAL(5,2)
);

INSERT INTO Thue VALUES
('T001',N'VAT 5%',5),
('T002',N'VAT 6%',6),
('T003',N'VAT 7%',7),
('T004',N'VAT 8%',8),
('T005',N'VAT 9%',9),
('T006',N'VAT 10%',10),
('T007',N'VAT 11%',11),
('T008',N'VAT 12%',12),
('T009',N'VAT 13%',13),
('T010',N'VAT 14%',14);

-- =====================================================
-- HÓA ĐƠN PHÒNG
-- =====================================================

CREATE TABLE HoaDonPhong (
MaHoaDonPhong NVARCHAR(10) PRIMARY KEY,
NgayLapHoaDon DATETIME DEFAULT GETDATE(),

MaPhieuDatPhong NVARCHAR(10),
MaNV NVARCHAR(10),
MaKH NVARCHAR(10),
MaKhuyenMai NVARCHAR(10),
MaThue NVARCHAR(10),

TongTien DECIMAL(14,2),
TienThue DECIMAL(14,2),

 TrangThai NVARCHAR(30)
        DEFAULT N'Chưa thanh toán',

FOREIGN KEY (MaPhieuDatPhong)
    REFERENCES PhieuDatPhong(MaPhieuDatPhong),

FOREIGN KEY (MaNV)
    REFERENCES NhanVien(MaNV),

FOREIGN KEY (MaKH)
    REFERENCES KhachHang(MaKH),

FOREIGN KEY (MaKhuyenMai)
    REFERENCES KhuyenMai(MaKhuyenMai),

FOREIGN KEY (MaThue)
    REFERENCES Thue(MaThue)
);


INSERT INTO HoaDonPhong VALUES
('HD001',GETDATE(),'DP001','NV0250001','KH0001','KM001','T001',500000,25000,N'Chưa thanh toán'),
('HD002',GETDATE(),'DP002','NV0250002','KH0002','KM002','T002',600000,36000,N'Chưa thanh toán'),
('HD003',GETDATE(),'DP003','NV0250003','KH0003','KM003','T003',700000,49000,N'Chưa thanh toán');

-- =====================================================
-- CHI TIẾT HÓA ĐƠN PHÒNG
-- =====================================================

CREATE TABLE ChiTietHoaDonPhong (
MaHoaDonPhong NVARCHAR(10),
MaPhong NVARCHAR(10),
ThoiGianNhan DATETIME,
ThoiGianTra DATETIME,

CachThue NVARCHAR(50),
SoLuongNguoi INT
    CHECK(SoLuongNguoi > 0),
TrangThaiSuDung NVARCHAR(50),
TrangThaiThanhToan NVARCHAR(50),
PRIMARY KEY (
             MaHoaDonPhong,
             MaPhong,
             ThoiGianNhan
    ),
FOREIGN KEY (MaHoaDonPhong)
    REFERENCES HoaDonPhong(MaHoaDonPhong),

FOREIGN KEY (MaPhong)
    REFERENCES Phong(MaPhong)




);


INSERT INTO ChiTietHoaDonPhong VALUES
('HD001','P101',GETDATE(),DATEADD(HOUR,5,GETDATE()),N'Giờ',2,N'Đang dùng',N'Chưa TT'),
('HD002','P201',GETDATE(),DATEADD(HOUR,6,GETDATE()),N'Giờ',2,N'Đang dùng',N'Chưa TT'),
('HD003','P301',GETDATE(),DATEADD(HOUR,7,GETDATE()),N'Ngày',1,N'Đang dùng',N'Chưa TT');

-- =====================================================
-- HÓA ĐƠN DỊCH VỤ
-- =====================================================

CREATE TABLE HoaDonDichVu (
MaHoaDonDichVu NVARCHAR(20) PRIMARY KEY,
MaHoaDonPhong NVARCHAR(10),

FOREIGN KEY (MaHoaDonPhong)
REFERENCES HoaDonPhong(MaHoaDonPhong)
);

INSERT INTO HoaDonDichVu VALUES
('HDDV001','HD001'),
('HDDV002','HD002'),
('HDDV003','HD003')

-- =====================================================
-- CHI TIẾT HÓA ĐƠN DỊCH VỤ
-- =====================================================

CREATE TABLE ChiTietHoaDonDichVu (
MaHoaDonDichVu NVARCHAR(20),
MaDichVu NVARCHAR(10),
ThoiDiemSuDung DATETIME,

SoLuong INT,
DonGia DECIMAL(12,2),
ThanhTien DECIMAL(12,2),

PRIMARY KEY (
             MaHoaDonDichVu,
             MaDichVu,
             ThoiDiemSuDung
    ),

FOREIGN KEY (MaHoaDonDichVu)
    REFERENCES HoaDonDichVu(MaHoaDonDichVu),

FOREIGN KEY (MaDichVu)
    REFERENCES DichVu(MaDichVu)
);

INSERT INTO ChiTietHoaDonDichVu VALUES
('HDDV001','DV001',GETDATE(),2,10000,20000),
('HDDV002','DV002',GETDATE(),1,15000,15000),
('HDDV003','DV003',GETDATE(),3,15000,45000)
ALTER TABLE HoaDonPhong
    ADD TienPhong FLOAT DEFAULT 0;

ALTER TABLE HoaDonPhong
    ADD TienDichVu FLOAT DEFAULT 0;