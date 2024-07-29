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

// Lấy dữ liệu từ form gửi lên
$MaGH = $_POST['MaGH'];
$MaMon = $_POST['MaMon'];
$SoLuong = (int)$_POST['SoLuong'];
$ThanhTien = (double)$_POST['ThanhTien'];

// Kiểm tra xem bản ghi đã tồn tại chưa
$sql_check = "SELECT SoLuong FROM ChiTietGH WHERE MaGH = ? AND MaMon = ?";
$stmt_check = $conn->prepare($sql_check);
$stmt_check->bind_param("ss", $MaGH, $MaMon);
$stmt_check->execute();
$stmt_check->store_result();

if ($stmt_check->num_rows > 0) {
    // Nếu đã tồn tại, cập nhật số lượng
    $stmt_check->bind_result($current_quantity);
    $stmt_check->fetch();

    $sql_update = "UPDATE ChiTietGH SET SoLuong = ?, ThanhTien = ? WHERE MaGH = ? AND MaMon = ?";
    $stmt_update = $conn->prepare($sql_update);
    $stmt_update->bind_param("iiss", $SoLuong, $ThanhTien, $MaGH, $MaMon);

    if ($stmt_update->execute()) {
        echo "success";
    } else {
        echo "error: " . $stmt_update->error;
    }
    $stmt_update->close();
} else {
    echo "success";
}

$stmt_check->close();
$conn->close();
?>
