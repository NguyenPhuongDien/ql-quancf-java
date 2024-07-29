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

// Get data from the request body (use json_decode for security)
$data = json_decode(file_get_contents('php://input'), true);

if (!is_array($data) || !isset($data['maBan']) || !isset($data['tongTien']) || !isset($data['chiTietHoaDon'])) {
    die(json_encode(array("status" => "error", "message" => "Dữ liệu không hợp lệ")));
}

$maBan = $data['maBan'];
$tongTien = $data['tongTien'];

// Get MaNV and GhiChu (or handle their absence as needed)
$maNV = isset($data['maNV']) ? $data['maNV'] : null; // Example of optional handling


// Create a unique order ID
$maGH = uniqid();

// Prepare and execute the query for inserting the new order
$sql_hoadon = "INSERT INTO giohang (MaGH, MaBan, TongTien, MaNV) VALUES (?,?,?,?)";
$stmt_hoadon = $conn->prepare($sql_hoadon);

// Bind parameters (using prepared statements for security)
$stmt_hoadon->bind_param("ssss", $maGH, $maBan, $tongTien,$maNV);

if ($stmt_hoadon->execute()) {
    // Add order details
    foreach ($data['chiTietHoaDon'] as $dish) {
        $maMon = $dish['maMon']; 
        $soLuong = $dish['soLuong']; 
        $thanhTien = $dish['thanhTien']; 

        $sql_chitiethd = "INSERT INTO chitietgh (MaGH, MaMon, SoLuong, ThanhTien) VALUES (?,?,?,?)";
        $stmt_chitiethd = $conn->prepare($sql_chitiethd);
        $stmt_chitiethd->bind_param("ssii", $maGH, $maMon, $soLuong, $thanhTien);

        // Check for errors during chitiethd insertion
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