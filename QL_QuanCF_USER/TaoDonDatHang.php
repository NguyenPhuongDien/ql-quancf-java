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
// Lấy thông tin khách hàng từ dữ liệu gửi lên từ form
$MaKH = $_POST['MaKH'];
//Lấy mã giỏ hàng
$MaGH = $_POST['MaGH'];
$DiaChi = $_POST['DiaChi'];
$SDT = $_POST['SDT'];
// Lấy ngày hiện tại
date_default_timezone_set('Asia/Ho_Chi_Minh');
$NgayDat = date('Y-m-d H:i:s');

// Lấy tổng tiền từ dữ liệu gửi lên từ form
$TongTien = (double)$_POST['TongTien'];

// Lấy số lượng đơn hàng hiện tại trong bảng dathang
$count_query = "SELECT COUNT(*) AS soLuong FROM DATHANG";
$count_result = $conn->query($count_query);
if ($count_result->num_rows > 0) {
    $count_row = $count_result->fetch_assoc();
    $soLuong = $count_row['soLuong'];
    // Tạo mã đơn hàng gồm 'DH' + số lượng đơn hàng + 1
    $MaDH = 'DH' . ($soLuong + 1);
} else {
    // Nếu không có đơn hàng nào trong bảng, gán mã đơn hàng là 'DH1'
    $MaDH = 'DH1';
}
$time=date('Y-m-d H:i:s');
// Thêm dữ liệu vào bảng dathang
$insert_query = "INSERT INTO DATHANG (MaDH, MaKH, NgayDat, TongTien,TrangThai,GhiChu,DiaChi,SDT,created_at) 
                VALUES ('$MaDH', '$MaKH', '$NgayDat', '$TongTien','Chờ xác nhận','Không có','$DiaChi','$SDT','$time')";
if ($conn->query($insert_query) === TRUE) {
    //Clear giỏ hàng khi thanh toán xong
  
    echo $MaDH;
} else {
    echo "Lỗi khi thêm đơn hàng: " . $conn->error;
}


// Đóng kết nối
$conn->close();
?>
