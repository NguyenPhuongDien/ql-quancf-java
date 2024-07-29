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

// Truy vấn cơ sở dữ liệu
$sql = "SELECT * FROM LoaiMon";
$stmt = $conn->prepare($sql);

// Kiểm tra và thực thi truy vấn
if ($stmt) {
    $stmt->execute();
    $result = $stmt->get_result();

    // Chuyển kết quả thành mảng dữ liệu JSON
    $data = array();
    while ($row = $result->fetch_assoc()) {
        $data[] = $row;
    }

    // Trả về dữ liệu dưới dạng JSON
    echo json_encode($data);

    // Đóng statement
    $stmt->close();
} else {
    // Lỗi trong quá trình chuẩn bị statement
    echo json_encode(array("error" => "Lỗi khi chuẩn bị truy vấn: " . $conn->error));
}

// Đóng kết nối
$conn->close();
?>
