$(function () {
    $(document).ready(function () {
        $.ajax({
            url: "getAllScore",
            type:"post",
            dataType:"json",
            contentType:"application/json;charset=utf-8",
            success:function (data) {
                $("#sum").append(parseFloat(data.other).toFixed(2));
            }
        })
    })
})

