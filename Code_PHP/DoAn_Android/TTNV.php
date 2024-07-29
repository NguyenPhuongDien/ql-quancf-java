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

    $stmt = $conn->prepare("SELECT MaNV, TenNV, SDT, DiaChi, Gmail FROM nhanvien WHERE MaNV = ?");
    $stmt->bind_param("s", $maNV);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
        $row = $result->fetch_assoc();
        echo json_encode(array("status" => "success", "data" => $row));
    } else {
        echo json_encode(array("status" => "error", "message" => "Không tìm thấy nhân viên"));
    }

    $stmt->close();
} else {
    echo json_encode(array("status" => "error", "message" => "Phương thức không hợp lệ"));
}

$conn->close();
?>