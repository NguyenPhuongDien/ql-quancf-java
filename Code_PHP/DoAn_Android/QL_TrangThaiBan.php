<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "ql_cafe";

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die("Kết nối thất bại: " . $conn->connect_error);
}

$maBan = $_POST['maBan'];
$trangThai = $_POST['trangThai'];

$sql = "UPDATE Ban SET TrangThai = '$trangThai' WHERE MaBan = '$maBan'";
$result = mysqli_query($conn, $sql);

if ($result) {
    echo json_encode(["status" => "success"]);
} else {
    echo json_encode(["status" => "error", "message" => "Failed to update"]);
}

mysqli_close($conn);
?>
