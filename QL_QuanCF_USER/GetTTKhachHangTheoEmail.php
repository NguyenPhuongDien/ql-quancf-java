<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "ql_cafe";

// Kết nối đến cơ sở dữ liệu
$conn = new mysqli($servername, $username, $password, $dbname);

// Kiểm tra kết nối
if ($conn->connect_error) {
    die(json_encode(array("status" => "error", "message" => "Lỗi kết nối Database: " . $conn->connect_error)));
}

// Lấy dữ liệu từ form gửi lên
$TaiKhoan = $_POST['TaiKhoan'];

// Tìm mã khách hàng dựa trên email
$sql = "SELECT MaKH FROM TaiKhoanKH WHERE TaiKhoan = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("s", $TaiKhoan);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    $row = $result->fetch_assoc();
    $MaKH = $row['MaKH'];

    // Lấy thông tin khách hàng
    $sql_khachhang = "SELECT * FROM KhachHang WHERE MaKH = ?";
    $stmt_khachhang = $conn->prepare($sql_khachhang);
    $stmt_khachhang->bind_param("s", $MaKH);
    $stmt_khachhang->execute();
    $result_khachhang = $stmt_khachhang->get_result();
    $row_khachhang = $result_khachhang->fetch_assoc();

    // Lấy thông tin giỏ hàng của khách hàng
    $sql_giohang = "SELECT * FROM GioHang WHERE MaKH = ?";
    $stmt_giohang = $conn->prepare($sql_giohang);
    $stmt_giohang->bind_param("s", $MaKH);
    $stmt_giohang->execute();
    $result_giohang = $stmt_giohang->get_result();

    // Kiểm tra xem có giỏ hàng cho khách hàng này hay không
    if ($result_giohang->num_rows > 0) {
        $row_giohang = $result_giohang->fetch_assoc();

        // Tạo mảng chứa thông tin khách hàng và giỏ hàng
        $data = array(
            'status' => 'success',
            'KhachHang' => $row_khachhang,
            'GioHang' => $row_giohang
        );
    } else {
        $data = array(
            'status' => 'error',
            'message' => "Không tìm thấy giỏ hàng cho khách hàng có email: $TaiKhoan"
        );
    }
} else {
    $data = array(
        'status' => 'error',
        'message' => "Không tìm thấy khách hàng có email: $TaiKhoan"
    );
}

// Chuyển mảng thành dạng JSON và trả về
echo json_encode($data);

$conn->close();
?>
