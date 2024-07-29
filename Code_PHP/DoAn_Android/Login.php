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

// Kiểm tra xem có phải là yêu cầu đăng nhập bằng Google Sign-in hay không
$isGoogleSignIn = isset($_POST["email"]) && isset($_POST["tenNV"]) && isset($_POST["maNV"]);

if ($isGoogleSignIn) {
    // Xử lý đăng nhập bằng Google Sign-in
    $email = $_POST["email"];
    $tenNV = $_POST["tenNV"];

    // Kiểm tra xem người dùng đã tồn tại trong cơ sở dữ liệu hay chưa
    $sql = "SELECT * FROM nhanvien WHERE Gmail = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("s", $email);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
        // Người dùng đã tồn tại, lấy MaNV hiện tại
        $row = $result->fetch_assoc();
        $maNV = $row['MaNV']; // Sử dụng MaNV hiện tại

        // Cập nhật thông tin nếu cần
        $sql = "UPDATE nhanvien SET TenNV = ? WHERE Gmail = ?";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("ss", $tenNV, $email);
        $stmt->execute();

        $response = array(
            "status" => "success",
            "message" => "Đăng nhập thành công!",
            "gmail" => $email,
            "tenNV" => $tenNV,
            "maNV" => $maNV
        );
    } else {
        // Người dùng mới, thêm vào cơ sở dữ liệu
        // Tạo MaNV mới
        $sql = "SELECT MAX(CAST(SUBSTRING(MaNV, 3) AS UNSIGNED)) as max_id FROM nhanvien";
        $result = $conn->query($sql);
        $row = $result->fetch_assoc();
        $maxId = $row['max_id'] + 1;
        $maNV = 'NV' . str_pad($maxId, 4, '0', STR_PAD_LEFT);

        $sql = "INSERT INTO nhanvien (Gmail, TenNV, MaNV) VALUES (?, ?, ?)";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("sss", $email, $tenNV, $maNV);
        $stmt->execute();

        if ($stmt->affected_rows > 0) {
            $response = array(
                "status" => "success",
                "message" => "Đăng nhập thành công!",
                "gmail" => $email,
                "tenNV" => $tenNV,
                "maNV" => $maNV
            );
        } else {
            $response = array("status" => "error", "message" => "Thêm thông tin thất bại! Lỗi: " . $stmt->error);
        }
    }

    // Kiểm tra xem người dùng đã có tài khoản trong taikhoannv chưa
    $sql = "SELECT * FROM taikhoannv WHERE TaiKhoan = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("s", $email);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows == 0) {
        // Người dùng chưa có tài khoản, tạo mới
        $sql = "INSERT INTO taikhoannv (TaiKhoan, MaNV, MatKhau) VALUES (?, ?, ?)";
        $stmt = $conn->prepare($sql);
        // Tạo mật khẩu ngẫu nhiên (tuỳ chọn, bạn cũng có thể yêu cầu người dùng đặt mật khẩu)
        $randomPassword = bin2hex(random_bytes(8));
        $hashedPassword = password_hash($randomPassword, PASSWORD_DEFAULT);
        $stmt->bind_param("sss", $email, $maNV, $hashedPassword);

        if ($stmt->execute()) {
            $response = array(
                "status" => "success",
                "message" => "Đăng nhập thành công!",
                "gmail" => $email,
                "tenNV" => $tenNV,
                "maNV" => $maNV
            );
        } else {
            $response = array("status" => "error", "message" => "Lỗi khi tạo tài khoản: " . $stmt->error);
        }
    } else {
        $response = array(
            "status" => "success",
            "message" => "Đăng nhập thành công!",
            "gmail" => $email,
            "tenNV" => $tenNV,
            "maNV" => $maNV
        );
    }
} else {
    // Xử lý đăng nhập bằng tài khoản/mật khẩu
    $taikhoan = isset($_POST["taikhoan"]) ? $_POST["taikhoan"] : "";
    $matkhau = isset($_POST["matkhau"]) ? $_POST["matkhau"] : "";

    // Truy vấn cơ sở dữ liệu để lấy mật khẩu đã băm
    $sql = "SELECT t.MatKhau, nv.MaNV, nv.Gmail, nv.TenNV FROM taikhoannv t JOIN nhanvien nv ON t.MaNV = nv.MaNV WHERE t.TaiKhoan = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("s", $taikhoan);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
        $row = $result->fetch_assoc();
        $hashedPassword = $row["MatKhau"];

        // Kiểm tra mật khẩu người dùng nhập vào với mật khẩu đã băm
        if (password_verify($matkhau, $hashedPassword)) {
            $response = array(
                "status" => "success",
                "message" => "Đăng nhập thành công!",
                "gmail" => $row['Gmail'],
                "tenNV" => $row['TenNV'],
                "maNV" => $row['MaNV']
            );
        } else {
            $response = array("status" => "error", "message" => "Mật khẩu không đúng!");
        }
    } else {
        $response = array("status" => "error", "message" => "Tài khoản không tồn tại!");
    }
}

// Trả về kết quả dưới dạng JSON
echo json_encode($response);

// Đóng kết nối
$stmt->close();
$conn->close();
?>