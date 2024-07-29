<?php
header('Content-Type: application/json; charset=utf-8');

$servername = "localhost";
$username = "root";
$password = "";
$dbname = "ql_cafe";

// Connect to the database
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die(json_encode(array("status" => "error", "message" => "Kết nối thất bại: " . $conn->connect_error), JSON_UNESCAPED_UNICODE));
}

// Get data from the request using $_POST
$username = $_POST["username"];
$gmail = $_POST["gmail"];
$password = $_POST["password"];

// Validate input
if (empty($username) || empty($gmail) || empty($password)) {
    $response = array(
        "status" => "error",
        "message" => "Vui lòng điền đầy đủ thông tin!"
    );
    echo json_encode($response);
    exit();
}

// Generate automatic employee ID
$sql = "SELECT MAX(CAST(SUBSTRING(MaNV, 3) AS UNSIGNED)) as max_id FROM nhanvien";
$result = $conn->query($sql);
$row = $result->fetch_assoc();
$maxId = $row['max_id'] + 1;
$maNV = 'NV' . str_pad($maxId, 4, '0', STR_PAD_LEFT);

$sdt = null;
$diachi = null;

try {
    // Insert employee into "nhanvien" table
    $sql = "INSERT INTO nhanvien (MaNV, TenNV, SDT, DiaChi, Gmail) VALUES (?, ?, ?, ?, ?)";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("sssss", $maNV, $username, $sdt, $diachi, $gmail);

    if ($stmt->execute()) {
        // Insert account into "taikhoannv" table
        $sql = "INSERT INTO taikhoannv (TaiKhoan, MaNV, MatKhau) VALUES (?, ?, ?)";
        $stmt = $conn->prepare($sql);
        $hashedPassword = password_hash($password, PASSWORD_DEFAULT);
        $stmt->bind_param("sss", $gmail, $maNV, $hashedPassword);

        if ($stmt->execute()) {
            $response = array("status" => "success", "message" => "Đăng ký tài khoản thành công!");
            echo json_encode($response);
        } else {
            $response = array("status" => "error", "message" => "Đăng ký tài khoản thất bại! Lỗi: " . $stmt->error);
            echo json_encode($response);
        }
    } else {
        $response = array("status" => "error", "message" => "Đăng ký nhân viên thất bại! Lỗi: " . $stmt->error);
        echo json_encode($response);
    }
} catch (Exception $e) {
    $response = array("status" => "error", "message" => "Lỗi hệ thống: " . $e->getMessage());
    echo json_encode($response);
} finally {
    $stmt->close();
    $conn->close();
}
?>
