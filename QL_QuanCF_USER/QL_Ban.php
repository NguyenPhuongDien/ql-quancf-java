<?php
    include 'connectdatabase.php';
    $conn=new ConnectDataBase();
    $sql="SELECT * FROM ban";
    $result=$conn->select($sql);
    if ($result->num_rows > 0) {
        while($row = $result->fetch_assoc()) {
            $data[] = $row;
        }
    }
    echo json_encode($data);
?>