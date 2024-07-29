<?php
header('Content-Type: application/json');
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "ql_cafe";

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die("Kết nối thất bại: " . $conn->connect_error);
}

// Monthly Statistics Query
$queryMonthly = "SELECT MONTH(`NgayThanhToan`) AS Thang, SUM(`TongTien`) AS TongTienThang FROM `hoadon` GROUP BY MONTH(`NgayThanhToan`)";
$resultMonthly = $conn->query($queryMonthly);

$monthlyData = array(); 
if ($resultMonthly->num_rows > 0) {
    while($row = $resultMonthly->fetch_assoc()) {
        $monthlyData[$row['Thang']] = $row['TongTienThang'];
    }
}

// Daily Statistics Query
$currentMonth = date('m');
$currentYear = date('Y');
$queryDaily = "SELECT DATE(`NgayThanhToan`) AS Ngay, SUM(`TongTien`) AS TongTienNgay FROM `hoadon` WHERE MONTH(`NgayThanhToan`) = $currentMonth AND YEAR(`NgayThanhToan`) = $currentYear GROUP BY DATE(`NgayThanhToan`)";
$resultDaily = $conn->query($queryDaily);

$dailyData = array(); 
if ($resultDaily->num_rows > 0) {
    while($row = $resultDaily->fetch_assoc()) {
        $dailyData[$row['Ngay']] = $row['TongTienNgay'];
    }
}

echo json_encode(array("monthly" => $monthlyData, "daily" => $dailyData));
$conn->close();
?>
