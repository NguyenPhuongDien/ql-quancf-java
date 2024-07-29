<?php
header('Content-Type: application/json; charset=utf-8');

$servername = "localhost";
$username = "root";
$password = "";
$dbname = "ql_cafe";

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die(json_encode(array("status" => "error", "message" => "Kết nối thất bại: " . $conn->connect_error)));
}

// Lấy dữ liệu từ request body (sử dụng json_decode cho bảo mật)
$data = json_decode(file_get_contents('php://input'), true);

if (!is_array($data) || !isset($data['maBan']) || !isset($data['tongTien']) || !isset($data['chiTietHoaDon'])) {
    die(json_encode(array("status" => "error", "message" => "Dữ liệu không hợp lệ")));
}

$maBan = $data['maBan'];
$tongTien = $data['tongTien'];
date_default_timezone_set('Asia/Ho_Chi_Minh');
$NgayThanhToan=date('Y-m-d H:i:s');

// Lấy MaNV và GhiChu (hoặc xử lý nếu chúng không tồn tại)
$maNV = isset($data['maNV']) ? $data['maNV'] : null;
$ghiChu = isset($data['ghiChu']) ? $data['ghiChu'] : ''; // Nếu không có ghi chú, gán giá trị rỗng

// Tạo Mã Hóa Đơn duy nhất
$maHD = uniqid();

// Chuẩn bị và thực thi truy vấn để thêm hóa đơn mới
$sql_hoadon = "INSERT INTO hoadon (MaHD, MaNV, MaBan, TongTien, GhiChu, NgayThanhToan) VALUES (?,?,?,?,?,?)";
$stmt_hoadon = $conn->prepare($sql_hoadon);

// Gắn các tham số (sử dụng prepared statements để bảo mật)
$stmt_hoadon->bind_param("ssssss", $maHD, $maNV, $maBan, $tongTien, $ghiChu, $NgayThanhToan);

if ($stmt_hoadon->execute()) {
    // Thêm chi tiết hóa đơn
    foreach ($data['chiTietHoaDon'] as $dish) {
        $maMon = $dish['maMon']; 
        $soLuong = $dish['soLuong']; 
        $thanhTien = $dish['thanhTien']; 
        $ghiChu1=$dish['ghiChu'];
        $sql_chitiethd = "INSERT INTO chitiethd (MaHD, MaMon, SoLuong, ThanhTien,GhiChu) VALUES (?,?,?,?,?)";
        $stmt_chitiethd = $conn->prepare($sql_chitiethd);
        $stmt_chitiethd->bind_param("ssiis", $maHD, $maMon, $soLuong, $thanhTien,$ghiChu1);

        // Kiểm tra lỗi khi thêm chi tiết hóa đơn
        if (!$stmt_chitiethd->execute()) {
            $error = $conn->error;
            $conn->close();
            die(json_encode(array("status" => "error", "message" => "Lỗi khi thêm chi tiết hóa đơn: " . $error, "sql" => $sql_chitiethd)));
        }

        $stmt_chitiethd->close();
    }

    $stmt_hoadon->close();
    $conn->close();

    echo json_encode(array("status" => "success", "message" => "Thêm hóa đơn thành công", "maHD" => $maHD));

} else {
    $error = $conn->error;
    $conn->close();
    die(json_encode(array("status" => "error", "message" => "Lỗi: " . $error, "sql" => $sql_hoadon)));
}
?>