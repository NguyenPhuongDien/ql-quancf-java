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

    $checkMaLoai = "SELECT MaLoai FROM loaimon WHERE MaLoai = ? ";
    $stmt = $conn->prepare($checkMaLoai);
    $stmt->bind_param("s", $MaLoai);
    $stmt->execute();
    $stmt->store_result();
    
    if ($stmt->num_rows == 0){
        echo "exist";
    } else {
        $sql = "DELETE FROM loaimon WHERE MaLoai = ?";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("s", $MaLoai);
        if ($stmt->execute()) {
            echo "success";
        } else {
            echo "error" . $conn->error;
        }
    }

    $stmt->close();
    $conn->close();
?>