<?php
    $servername = "localhost";
    $username = "root";
    $password = "";
    $dbname = "ql_cafe";
    
    // Kết nối đến cơ sở dữ liệu
    $conn = new mysqli($servername, $username, $password, $dbname);

    // Kiểm tra kết nối
    if ($conn->connect_error) {
        die("Lỗi Database: " . $conn->connect_error);
    }

    // Lấy dữ liệu từ form gửi lên
    $Email = $_POST['Email'];
    $TenKH = $_POST['TenKH'];
    $SDT = $_POST['SDT'];
    $MatKhau = $_POST['MatKhau'];

    // Lấy mã KH hiện tại lớn nhất
    $sql = "SELECT MaKH FROM KhachHang ORDER BY MaKH DESC LIMIT 1";
    $result = $conn->query($sql);
    if ($result->num_rows > 0) {
        // Nếu có dữ liệu, lấy MaKH hiện tại lớn nhất và tăng lên 1
        $row = $result->fetch_assoc();
        $currentMaxMaKH = $row['MaKH'];
        $currentMaxNumber = (int)substr($currentMaxMaKH, 2); // Loại bỏ "KH" và chuyển đổi thành số nguyên
        $newMaKH = 'KH' . ($currentMaxNumber + 1);
    } else {
        // Nếu không có dữ liệu, khởi tạo MaKH là KH1
        $newMaKH = 'KH1';
    }

    // Chuẩn bị câu lệnh SQL để chèn khách hàng mới vào bảng KhachHang
    $sql = "INSERT INTO KhachHang (MaKH, TenKH, SDT, Email) VALUES (?, ?, ?, ?)";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("ssss", $newMaKH, $TenKH, $SDT, $Email);

    // Thực hiện chèn dữ liệu vào bảng KhachHang
    if ($stmt->execute()) {
        // Nếu chèn thành công, tiếp tục chèn dữ liệu vào bảng TaiKhoanKH
        $sqlTaiKhoan = "INSERT INTO TaiKhoanKH (TaiKhoan, MaKH, MatKhau) VALUES (?, ?, ?)";
        $stmtTaiKhoan = $conn->prepare($sqlTaiKhoan);
        $stmtTaiKhoan->bind_param("sss", $Email, $newMaKH, $MatKhau);
        
        if ($stmtTaiKhoan->execute()) {
            // Tạo MaGH mới
            $sqlMaGH = "SELECT MaGH FROM GioHang ORDER BY MaGH DESC LIMIT 1";
            $resultMaGH = $conn->query($sqlMaGH);
            if ($resultMaGH->num_rows > 0) {
                $rowMaGH = $resultMaGH->fetch_assoc();
                $currentMaxMaGH = $rowMaGH['MaGH'];
                $currentMaxGHNumber = (int)substr($currentMaxMaGH, 2); // Loại bỏ "GH" và chuyển đổi thành số nguyên
                $newMaGH = 'GH' . ($currentMaxGHNumber + 1);
            } else {
                $newMaGH = 'GH1';
            }
                
            // Thêm giỏ hàng mới vào bảng GioHang
            $sqlGioHang = "INSERT INTO GioHang (MaGH, MaKH, TongTien) VALUES (?, ?, 0)";
            $stmtGioHang = $conn->prepare($sqlGioHang);
            $stmtGioHang->bind_param("ss", $newMaGH, $newMaKH);
            
            if ($stmtGioHang->execute()) {
                echo "success";
            } else {
                echo "error: " . $stmtGioHang->error;
            }
            $stmtGioHang->close();
        } else {
            echo "error: " . $stmtTaiKhoan->error;
        }
        $stmtTaiKhoan->close();
    } else {
        echo "error: " . $stmt->error;
    }

    // Đóng các statement và kết nối
    $stmt->close();
    $conn->close();
?>
