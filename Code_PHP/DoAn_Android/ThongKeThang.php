<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "ql_cafe";

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die("Kết nối thất bại: " . $conn->connect_error);
}

$query = "SELECT MONTH(`NgayThanhToan`) AS Thang, SUM(`TongTien`) AS TongTienThang FROM `hoadon`GROUP BY YEAR(`NgayThanhToan`), MONTH(`NgayThanhToan`)";
$result = $conn->query($query);

$data = array(); 

if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        $data[] = $row;
    }
}

echo json_encode($data);
?>