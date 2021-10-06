$(document).ready(function(){
    $('#dataTable').after('<div id="nav" style="text-align: center; padding: 10px"></div>');
    var rowsShown = 10;
    var rowsTotal = $('#dataTable tbody tr').length;
    var numPages = rowsTotal/rowsShown;
    for(i = 0;i < numPages;i++) {
        var pageNum = i + 1;
        $('#nav').append('<a href="#" rel="'+i+'" class="form-control btn btn-primary btn-user d-sm-inline" >'+pageNum+'</a> ');
    }
    $('#dataTable tbody tr').hide();
    $('#dataTable tbody tr').slice(0, rowsShown).show();
    $('#nav a:first').addClass('active');
    $('#nav a').bind('click', function(){

        $('#nav a').removeClass('active');
        $(this).addClass('active');
        var currPage = $(this).attr('rel');
        var startItem = currPage * rowsShown;
        var endItem = startItem + rowsShown;
        $('#dataTable tbody tr').css('opacity','0.0').hide().slice(startItem, endItem).
        css('display','table-row').animate({opacity:1}, 300);
    });
});

$(document).ready(function(){
    $('#userTable').after('<div id="nav" style="text-align: center; padding: 10px"></div>');
    var rowsShown = 5;
    var rowsTotal = $('#userTable tbody tr').length;
    var numPages = rowsTotal/rowsShown;
    for(i = 0;i < numPages;i++) {
        var pageNum = i + 1;
        $('#nav').append('<a href="#" rel="'+i+'" class="form-control btn btn-primary btn-user d-sm-inline" >'+pageNum+'</a> ');
    }
    $('#userTable tbody tr').hide();
    $('#userTable tbody tr').slice(0, rowsShown).show();
    $('#nav a:first').addClass('active');
    $('#nav a').bind('click', function(){

        $('#nav a').removeClass('active');
        $(this).addClass('active');
        var currPage = $(this).attr('rel');
        var startItem = currPage * rowsShown;
        var endItem = startItem + rowsShown;
        $('#userTable tbody tr').css('opacity','0.0').hide().slice(startItem, endItem).
        css('display','table-row').animate({opacity:1}, 300);
    });
});
