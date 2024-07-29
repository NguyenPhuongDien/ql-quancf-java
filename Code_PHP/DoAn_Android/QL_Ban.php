<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "ql_cafe";

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die("Kết nối thất bại: " . $conn->connect_error);
}

$query = "SELECT * FROM ban";
$result = $conn->query($query);

$data = array(); 

if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        $data[] = $row;
    }
}

echo json_encode($data);
?>