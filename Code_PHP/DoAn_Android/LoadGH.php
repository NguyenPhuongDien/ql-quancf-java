<?php
header('Content-Type: application/json; charset=utf-8');

// Thực hiện kết nối đến cơ sở dữ liệu
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "ql_cafe";

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die(json_encode(array("status" => "error", "message" => "Kết nối thất bại: " . $conn->connect_error)));
}

// Kiểm tra xem đã nhận được mã bàn từ yêu cầu GET chưa
if (isset($_GET['maBan'])) {
    $maBan = $_GET['maBan'];

    // Truy vấn để lấy thông tin giỏ hàng dựa trên mã bàn
    $sql = "SELECT * FROM giohang WHERE MaBan = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("s", $maBan);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
        // Duyệt qua các hàng kết quả và đưa vào mảng dữ liệu
        $gioHangData = array();
        while ($row = $result->fetch_assoc()) {
            // Lấy mã giỏ hàng
            $maGH = $row['MaGH'];

            // Truy vấn chi tiết giỏ hàng dựa trên mã giỏ hàng
            $sqlChiTietGH = "SELECT * FROM chitietgh WHERE MaGH = ?";
            $stmtChiTietGH = $conn->prepare($sqlChiTietGH);
            $stmtChiTietGH->bind_param("s", $maGH);
            $stmtChiTietGH->execute();
            $resultChiTietGH = $stmtChiTietGH->get_result();

            // Mảng chứa chi tiết giỏ hàng
            $chiTietGHData = array();
            while ($rowChiTietGH = $resultChiTietGH->fetch_assoc()) {
                $chiTietGHData[] = $rowChiTietGH;
            }
            $row['chiTietGioHang'] = $chiTietGHData;

            // Đưa thông tin giỏ hàng vào mảng dữ liệu chung
            $gioHangData[] = $row;
        }
        $stmt->close();

        // Trả về dữ liệu giỏ hàng dưới dạng JSON
        echo json_encode(array("status" => "success", "gioHangData" => $gioHangData));
    } else {
        echo json_encode(array("status" => "error", "message" => "Không tìm thấy giỏ hàng cho mã bàn: " . $maBan));
    }
} else {
    echo json_encode(array("status" => "error", "message" => "Thiếu tham số mã bàn trong yêu cầu"));
}
?>
