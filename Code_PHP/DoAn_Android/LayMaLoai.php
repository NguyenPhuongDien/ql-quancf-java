<?php
// Kết nối CSDL
$servername = "localhost"; // Thay thế bằng server của bạn
$username = "root"; // Thay thế bằng username của bạn
$password = ""; // Thay thế bằng password của bạn
$dbname = "ql_cafe"; // Thay thế bằng tên database của bạn

$conn = new mysqli($servername, $username, $password, $dbname);

// Kiểm tra kết nối
if ($conn->connect_error) {
    die("Kết nối thất bại: " . $conn->connect_error);
}

// Lấy dữ liệu từ bảng "loai_mon"
$sql = "SELECT MaLoai FROM loaimon";
$result = $conn->query($sql);

$maLoaiArray = array();

// Kiểm tra xem có dữ liệu nào được trả về không
if ($result->num_rows > 0) {
    // Duyệt qua từng hàng dữ liệu
    while($row = $result->fetch_assoc()) {
        // Thêm giá trị của cột "MaLoai" vào mảng
        $maLoaiArray[] = $row["MaLoai"];
    }
}

// Trả về mảng JSON
echo json_encode($maLoaiArray);

// Đóng kết nối
$conn->close();
?>