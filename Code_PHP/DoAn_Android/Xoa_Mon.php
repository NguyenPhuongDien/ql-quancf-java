<?php
    $servername = "localhost";
    $username = "root";
    $password = "";
    $dbname = "ql_cafe";
    
    $conn = new mysqli($servername, $username, $password, $dbname);

    if ($conn->connect_error) {
        die("Lỗi Database: " . $conn->connect_error);
    }

    $MaMon = $_POST['MaMon'];

    $checkMaMon = "SELECT MaMon FROM mon WHERE MaMon = ? ";
    $stmt = $conn->prepare($checkMaMon);
    $stmt->bind_param("s", $MaMon);
    $stmt->execute();
    $stmt->store_result();
    
    if ($stmt->num_rows == 0){
        echo "exist";
    } else {
        $sql = "DELETE FROM mon WHERE MaMon = ?";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("s", $MaMon);
        if ($stmt->execute()) {
            echo "success";
        } else {
            echo "error" . $conn->error;
        }
    }

    $stmt->close();
    $conn->close();
?>