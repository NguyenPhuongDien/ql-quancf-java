    <?php
        include 'connectdatabase.php';
        $conn = new ConnectDataBase();

        // Lấy mã loại từ yêu cầu POST
        $maLoai = $_POST["MaLoai"];

        // Tạo một mảng để chứa dữ liệu kết quả
        $data = array();

        // Thực hiện truy vấn SQL để lấy dữ liệu theo mã loại
        $sql = "SELECT * FROM Mon WHERE MaLoai = '$maLoai'";
        $result = $conn->select($sql);

        // Kiểm tra và xử lý kết quả trả về
        if ($result->num_rows > 0) {
            while ($row = $result->fetch_assoc()) {
                $data[] = $row; // Thêm dòng dữ liệu vào mảng
            }
        }

        // Trả về mảng dữ liệu dưới dạng JSON
        echo json_encode($data);
    ?>
