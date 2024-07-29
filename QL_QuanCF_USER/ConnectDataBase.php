<?php
    class ConnectDataBase
    {
        private $servername;
        private $username;
        private $password;
        private $database;
        public $conn; // Change visibility to public
    
        public function __construct()
        {
            $this->servername = "localhost";
            $this->username = "root";
            $this->password = "";
            $this->database = "ql_cafe";
            $this->conn = mysqli_connect($this->servername, $this->username, $this->password, $this->database);
        }
    
        public  function select($sql)
        {
            // Accessing $conn is not possible within a static method.
            // You need to either change $conn visibility or remove static keyword.
            $result = $this->conn->query($sql); // Change $this to self if keeping it static
            return $result;
        }
    
        // You can also provide a public method to access $conn if needed
        public function getConnection()
        {
            return $this->conn;
        }
        public function callProcedure($sql, $params = array()) {
            // Chuẩn bị câu lệnh và kiểm tra lỗi
            $stmt = $this->conn->prepare($sql);
            if (!$stmt) {
                throw new Exception("Error in SQL: " . $this->conn->error);
            }
    
            // Bind các tham số nếu có
            if (!empty($params)) {
                $types = str_repeat('s', count($params)); // Giả sử tất cả các tham số đều là kiểu string
                $stmt->bind_param($types, ...$params); // Sử dụng unpacking (...) từ PHP 7.4 trở lên
            }
    
            // Thực thi stored procedure
            $stmt->execute();
    
            // Lấy kết quả
            $result = $stmt->get_result();
    
            // Trả về kết quả
            return $result;
        }
    }
    
?>