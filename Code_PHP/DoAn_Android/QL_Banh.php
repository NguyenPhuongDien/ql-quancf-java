<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "ql_cafe";

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die("Kết nối thất bại: " . $conn->connect_error);
}

$query = "SELECT * FROM mon WHERE MaLoai = 'Loai02'"; // Thay 'cafe' bằng mã món bạn muốn lấy
$result = $conn->query($query);

$data = array(); 

if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        $data[] = $row;
    }
}

echo json_encode($data);
?>