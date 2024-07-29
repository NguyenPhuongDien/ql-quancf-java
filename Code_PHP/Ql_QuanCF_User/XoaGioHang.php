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
$MaGH = $_POST['MaGH'];

// Câu lệnh SQL để xóa dữ liệu từ bảng chitietgh
$sql_delete = "DELETE FROM chitietgh WHERE MaGH = '$MaGH'";

// Thực thi câu lệnh SQL xóa dữ liệu
if ($conn->query($sql_delete) === TRUE) {
    // Câu lệnh SQL để cập nhật tổng tiền trong bảng giohang thành 0
    $sql_update = "UPDATE giohang SET TongTien = 0 WHERE MaGH = '$MaGH'";
    // Thực thi câu lệnh SQL cập nhật tổng tiền
    $update_result = $conn->query($sql_update);
    if ($update_result) {
        echo "Xoá giỏ hàng thành công!";
    } else {
        echo "Lỗi khi cập nhật tổng tiền: " . $conn->error;
    }
} else {
    echo "Lỗi khi xoá dữ liệu từ bảng chitietgh: " . $conn->error;
}

// Đóng kết nối
$conn->close();
?>
