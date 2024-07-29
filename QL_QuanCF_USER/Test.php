<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF8">
    <title>Test Form for Gio Hang</title>
</head>
<body>
    <h1>Test Gio Hang Form</h1>
    <form action="TaoDonDatHang.php" method="post">
        <label for="MaGH">Mã Giỏ Hàng:</label>
        <input type="text" id="MaGH" name="MaKH">
        <input type="text" id="MaGH" name="TongTien">
        <input type="text" id="MaGH" name="MaGH">
        <input type="submit" value="Submit">
    </form>
    <h1><?php echo date('Y-m-d H:i:s');?></h1>
</body>
</html>
