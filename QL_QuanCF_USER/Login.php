<?php
header('Content-Type: application/json; charset=utf-8');

$servername = "localhost";
$username = "root";
$password = "";
$dbname = "ql_cafe";

// Kết nối đến cơ sở dữ liệu
$conn = new mysqli($servername, $username, $password, $dbname);

// Kiểm tra kết nối
if ($conn->connect_error) {
    die(json_encode(array("status" => "error", "message" => "Kết nối thất bại: " . $conn->connect_error), JSON_UNESCAPED_UNICODE));
}

// Kiểm tra xem có phải là yêu cầu đăng nhập bằng Google Sign-in hay không
$isGoogleSignIn = isset($_POST["email"]) && isset($_POST["tenKH"]);

$response = array(); // Khởi tạo mảng phản hồi

if ($isGoogleSignIn) {
    // Xử lý đăng nhập bằng Google Sign-in
    $email = $_POST["email"];
    $tenKH = $_POST["tenKH"];

    // Kiểm tra xem người dùng đã tồn tại trong cơ sở dữ liệu hay chưa
    $sql = "SELECT * FROM khachhang WHERE Email = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("s", $email);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
        // Người dùng đã tồn tại, lấy MaKH hiện tại
        $row = $result->fetch_assoc();
        $maKH = $row['MaKH'];

        // Cập nhật thông tin nếu cần
        $sql = "UPDATE khachhang SET TenKH = ? WHERE Email = ?";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("ss", $tenKH, $email);
        $stmt->execute();
    } else {
        // Người dùng mới, thêm vào cơ sở dữ liệu
        // Tạo MaKH mới
        $sql = "SELECT MAX(CAST(SUBSTRING(MaKH, 3) AS UNSIGNED)) as max_id FROM khachhang";
        $result = $conn->query($sql);
        $row = $result->fetch_assoc();
        $maxId = $row['max_id'] + 1;
        $maKH = 'KH' . str_pad($maxId, 4, '0', STR_PAD_LEFT);

        $sql = "INSERT INTO khachhang (Email, TenKH, MaKH) VALUES (?, ?, ?)";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("sss", $email, $tenKH, $maKH);
        $stmt->execute();

        // Tạo giỏ hàng mới cho khách hàng mới
        $sqlMaGH = "SELECT MAX(CAST(SUBSTRING(MaGH, 3) AS UNSIGNED)) as max_id FROM GioHang";
        $resultMaGH = $conn->query($sqlMaGH);
        $rowMaGH = $resultMaGH->fetch_assoc();
        $maxMaGH = $rowMaGH['max_id'] + 1;
        $maGH = 'GH' . str_pad($maxMaGH, 4, '0', STR_PAD_LEFT);

        $sqlGioHang = "INSERT INTO GioHang (MaGH, MaKH, TongTien) VALUES (?, ?, 0)";
        $stmtGioHang = $conn->prepare($sqlGioHang);
        $stmtGioHang->bind_param("ss", $maGH, $maKH);
        $stmtGioHang->execute();
        $stmtGioHang->close();
    }

    // Kiểm tra xem người dùng đã có tài khoản trong bảng taikhoankh chưa
    $sql = "SELECT * FROM taikhoankh WHERE TaiKhoan = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("s", $email);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows == 0) {
        // Người dùng chưa có tài khoản, tạo mới
        $sql = "INSERT INTO taikhoankh (TaiKhoan, MaKH, MatKhau) VALUES (?, ?, ?)";
        $stmt = $conn->prepare($sql);
        // Tạo mật khẩu ngẫu nhiên
        $randomPassword = bin2hex(random_bytes(8));
        $hashedPassword = password_hash($randomPassword, PASSWORD_DEFAULT);
        $stmt->bind_param("sss", $email, $maKH, $hashedPassword);
        $stmt->execute();
    }

    $response = array(
        "status" => "success",
        "message" => "Đăng nhập thành công!",
        "gmail" => $email,
        "tenKH" => $tenKH,
        "maKH" => $maKH
    );
} else {
    $response = array("status" => "error", "message" => "Yêu cầu không hợp lệ.");
}

// Trả về kết quả dưới dạng JSON
echo json_encode($response);

// Đóng kết nối
$stmt->close();
$conn->close();
?>
