$(document).ready(function () {
    $('.nav .nav-item:nth-child(5)').addClass('active');
    $('#listTicket').DataTable({
        "language": {
            "search": "Tìm kiếm thông tin:",
            "lengthMenu": "Hiển thị _MENU_ bản ghi 1 trang",
            "zeroRecords": "Không tìm thấy kết quả",
            "info": "Hiển thị _PAGE_ trang trên tổng số _PAGES_ trang",
            "infoEmpty": "Không tìm thấy",
            "infoFiltered": "(Lọc từ _MAX_ bản ghi)"
        }
    });
});