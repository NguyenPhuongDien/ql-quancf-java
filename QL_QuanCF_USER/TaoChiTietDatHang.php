<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "ql_cafe";

// Kết nối đến cơ sở dữ liệu
$conn = new mysqli($servername, $username, $password, $dbname);

// Kiểm tra kết nối
if ($conn->connect_error) {
    die("Lỗi kết nối đến cơ sở dữ liệu: " . $conn->connect_error);
}

// Lấy dữ liệu từ form gửi lên từ ứng dụng Android
$MaDH = $_POST['MaDH'];
$MaMon = $_POST['MaMon'];
$SoLuong = (int)$_POST['SoLuong'];
$ThanhTien = (double)$_POST['ThanhTien'];

// Câu lệnh SQL để thêm dữ liệu vào bảng chitietdh
$sql = "INSERT INTO chitietdh (MaDH, MaMon, SoLuong, ThanhTien) VALUES ('$MaDH', '$MaMon', '$SoLuong', '$ThanhTien')";

// Thực thi câu lệnh SQL
if ($conn->query($sql) === TRUE) {
    echo "Thêm dữ liệu vào bảng chitietdh thành công!";
} else {
    echo "Lỗi khi thêm dữ liệu vào bảng chitietdh: " . $conn->error;
}

// Đóng kết nối
$conn->close();
?>
