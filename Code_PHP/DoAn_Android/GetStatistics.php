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

// Truy vấn dữ liệu từ bảng hoadon
$sql = "SELECT tongtien, ngaythanhtoan FROM hoadon";
$result = $conn->query($sql);

$data = array();
if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        $data[] = $row;
    }
    $response = array("status" => "success", "data" => $data);
} else {
    $response = array("status" => "error", "message" => "Không có dữ liệu");
}

echo json_encode($response, JSON_UNESCAPED_UNICODE);

$conn->close();
?>
