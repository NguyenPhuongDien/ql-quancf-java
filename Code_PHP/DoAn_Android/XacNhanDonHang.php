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
    $maDH = $_POST['MaDH'];
    $trangThai = $_POST['TrangThai'];
    $sql = "UPDATE DatHang SET TrangThai = ? WHERE MaDH = ?";
    $stmt_check = $conn->prepare($sql);
    $stmt_check->bind_param("ss", $trangThai, $maDH);

    if ($stmt_check->execute()) {
        echo "success";
    } else {
        echo "error: " . $stmt_update->error;
    }
    $stmt_check->close();
    $conn->close();
?>