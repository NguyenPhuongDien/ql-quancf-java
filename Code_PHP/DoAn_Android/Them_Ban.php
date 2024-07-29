<?php
    $servername = "localhost";
    $username = "root";
    $password = "";
    $dbname = "ql_cafe";
    
    $conn = new mysqli($servername, $username, $password, $dbname);

    if ($conn->connect_error) {
        die("Lỗi Database: " . $conn->connect_error);
    }

    $MaBan = $_POST['MaBan'];
    $TenBan = $_POST['TenBan'];
    $trangThai=0;

    $checkMaBan = "SELECT MaBan FROM ban WHERE MaBan = ?";
    $stmt = $conn->prepare($checkMaBan);
    $stmt->bind_param("s", $MaBan);
    $stmt->execute();
    $stmt->store_result();
    
    if ($stmt->num_rows > 0){
        echo "exist";
    } else {
        $sql = "INSERT INTO ban (MaBan, TenBan,TrangThai) VALUES (?, ?,?)";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("sss", $MaBan, $TenBan,$trangThai);
        
        if ($stmt->execute()) {
            echo "success";
        } else {
            echo "error";
        }
    }

    $stmt->close();
    $conn->close();
?>