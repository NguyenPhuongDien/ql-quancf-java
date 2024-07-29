<?php
    include 'connectdatabase.php';
    $conn=new ConnectDataBase();

    // Lấy tên món từ phía client (truyền qua POST request)
    $tenMon = $_POST["TenMon"];

    // Xử lý tên món để tránh tấn công SQL Injection và bảo vệ dữ liệu
    $tenMon = '%' . $tenMon . '%';

    // Chuẩn bị câu truy vấn SQL với điều kiện tìm kiếm tên món
    $sql = "SELECT * FROM Mon WHERE TenMon LIKE '$tenMon'";

    // Thực hiện truy vấn và lấy kết quả từ cơ sở dữ liệu
    $result = $conn->select($sql);

    // Kiểm tra và xử lý kết quả trả về
    if ($result->num_rows > 0) {
        // Lặp qua các hàng kết quả và thêm vào mảng data
        while ($row = $result->fetch_assoc()) {
            $data[] = $row;
        }
    }

    // Trả về kết quả dưới dạng JSON
    echo json_encode($data);
?>
