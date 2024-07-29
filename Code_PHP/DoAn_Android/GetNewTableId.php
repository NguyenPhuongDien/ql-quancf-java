<?php
header('Content-Type: application/json; charset=utf-8');

$servername = "localhost";
$username = "root";
$password = "";
$dbname = "ql_cafe";

// Kết nối đến cơ sở dữ liệu
$conn = new mysqli($servername, $username, $password, $dbname);

// Kiểm tra kết nối
if ($conn->connect_error) {
    die(json_encode(array("status" => "error", "message" => "Kết nối thất bại: " . $conn->connect_error), JSON_UNESCAPED_UNICODE));
}

try {
    // Tạo mã bàn mới tự động
    $sql = "SELECT MAX(CAST(SUBSTRING(MaBan, 3) AS UNSIGNED)) as max_id FROM ban";
    $result = $conn->query($sql);
    $row = $result->fetch_assoc();
    $maxId = $row['max_id'] + 1;
    $maBan = 'BA' . str_pad($maxId, 4, '0', STR_PAD_LEFT);

    $response = array("maBan" => $maBan);
    echo json_encode($response);

} catch (Exception $e) {
    $response = array("status" => "error", "message" => "Lỗi hệ thống: " . $e->getMessage());
    echo json_encode($response);
} finally {
    $conn->close();
}
?>
