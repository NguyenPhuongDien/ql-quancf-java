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

    $checkMaBan = "SELECT MaBan FROM ban WHERE MaBan = ? ";
    $stmt = $conn->prepare($checkMaBan);
    $stmt->bind_param("s", $MaBan);
    $stmt->execute();
    $stmt->store_result();
    
    if ($stmt->num_rows == 0){
        echo "exist";
    } else {
        $sql = "DELETE FROM ban WHERE MaBan = ?";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("s", $MaBan);
        if ($stmt->execute()) {
            echo "success";
        } else {
            echo "error" . $conn->error;
        }
    }

    $stmt->close();
    $conn->close();
?>