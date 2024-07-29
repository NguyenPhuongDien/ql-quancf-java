<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "ql_cafe";

// Kết nối đến cơ sở dữ liệu
$conn = new mysqli($servername, $username, $password, $dbname);

// Kiểm tra kết nối
if ($conn->connect_error) {
    die("Lỗi Database: " . $conn->connect_error);
}
$trangThaiDH = "Chờ xác nhận";

// Truy vấn cơ sở dữ liệu
$sql = "SELECT * FROM DatHang WHERE TrangThai = ? ORDER BY NgayDat DESC";
$stmt = $conn->prepare($sql);
$stmt->bind_param("s", $trangThaiDH);
$stmt->execute();
$result = $stmt->get_result();

// Chuyển kết quả thành mảng dữ liệu JSON
$data = array();
while ($row = $result->fetch_assoc()) {
    $data[] = $row;
}

// Trả về dữ liệu dưới dạng JSON
echo json_encode($data);

// Đóng kết nối
$stmt->close();
$conn->close();
?>
