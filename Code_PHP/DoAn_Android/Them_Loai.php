<?php
    $servername = "localhost";
    $username = "root";
    $password = "";
    $dbname = "ql_cafe";
    
    $conn = new mysqli($servername, $username, $password, $dbname);

    if ($conn->connect_error) {
        die("Lỗi Database: " . $conn->connect_error);
    }

    $MaLoai = $_POST['MaLoai'];
    $TenLoai = $_POST['TenLoai'];


    $checkMaLoai = "SELECT MaLoai FROM loaimon WHERE MaLoai = ?";
    $stmt = $conn->prepare($checkMaLoai);
    $stmt->bind_param("s", $MaLoai);
    $stmt->execute();
    $stmt->store_result();
    
    if ($stmt->num_rows > 0){
        echo "exist";
    } else {
        $sql = "INSERT INTO loaimon (MaLoai, TenLoai) VALUES (?, ?)";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("ss", $MaLoai, $TenLoai);
        
        if ($stmt->execute()) {
            echo "success";
        } else {
            echo "error";
        }
    }

    $stmt->close();
    $conn->close();
?>