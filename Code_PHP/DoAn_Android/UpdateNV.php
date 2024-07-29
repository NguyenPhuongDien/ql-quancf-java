<?php
header('Content-Type: application/json');

$servername = "localhost";
$username = "root";
$password = "";
$dbname = "ql_cafe";

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die(json_encode(array("status" => "error", "message" => "Kết nối thất bại: " . $conn->connect_error)));
}

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $maNV = $_POST['maNV'];
    $tenNV = $_POST['tenNV'];
    $sdt = $_POST['sdt'];
    $diachi = $_POST['diachi'];

    $stmt = $conn->prepare("UPDATE nhanvien SET TenNV = ?, SDT = ?, DiaChi = ? WHERE MaNV = ?");
    $stmt->bind_param("ssss", $tenNV, $sdt, $diachi, $maNV);

    if ($stmt->execute()) {
        echo json_encode(array("status" => "success", "message" => "Cập nhật thành công"));
    } else {
        echo json_encode(array("status" => "error", "message" => "Cập nhật thất bại"));
    }

    $stmt->close();
} else {
    echo json_encode(array("status" => "error", "message" => "Phương thức không hợp lệ"));
}

$conn->close();
?>