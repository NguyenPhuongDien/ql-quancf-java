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
$GiaBan = $_POST['GiaBan'];
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
    $new_quantity = $SoLuong;
    //Update số lượng + tổng tiền
    $new_thanhTien=$new_quantity*$GiaBan;

    $sql_update = "UPDATE ChiTietGH SET SoLuong = ?, ThanhTien = ? WHERE MaGH = ? AND MaMon = ?";
    $stmt_update = $conn->prepare($sql_update);
    $stmt_update->bind_param("diss", $new_quantity, $new_thanhTien, $MaGH, $MaMon);

    if ($stmt_update->execute()) {
        //Update tổng tiền giỏ hàng
        $sql = "SELECT TongTien FROM GioHang WHERE MaGH = ? LIMIT 1";
        $stmt_update_GH = $conn->prepare($sql);
        $stmt_update_GH->bind_param("s", $MaGH);
        $stmt_update_GH->execute();
        
        // Fetch the result
        $result = $stmt_update_GH->get_result();
        
        if ($result->num_rows > 0) {
            $row = $result->fetch_assoc();
            $tongTien = $row['TongTien']+$new_quantity;
            $sql_update = "UPDATE GioHang SET TongTien = ? WHERE MaGH = ?";
            $stmt_update = $conn->prepare($sql_update);
            $stmt_update->bind_param("ds", $tongTien, $MaGH);
        } 
        echo "success";
    } else {
        echo "error: " . $stmt_update->error;
    }
} else {
    // Nếu chưa tồn tại, thêm bản ghi mới
    $sql_insert = "INSERT INTO ChiTietGH (MaGH, MaMon, SoLuong, ThanhTien) VALUES (?, ?, ?, ?)";
    $stmt_insert = $conn->prepare($sql_insert);
    $stmt_insert->bind_param("sssd", $MaGH, $MaMon, $SoLuong, $ThanhTien);

    if ($stmt_insert->execute()) {
        echo "success";
    } else {
        echo "error: " . $stmt_insert->error;
    }
}

$stmt_check->close();
$conn->close();
?>
