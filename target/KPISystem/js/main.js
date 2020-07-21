$(function () {
    $(document).ready(function () {
        $.ajax({
            url:"getLoginInfo",
            type:"get",
            dataType:"json",
            contentType:"application/json;charset=utf-8",
            success:function (data) {
                var tid = data.id;
                var username = data.name;
                $("#loginInfo").html("欢迎您，"+username+"("+tid+")");
            }
        })
    })
})

function logout() {
    $.ajax({
        url: "logout",
        type: "post",
        dataType: "json",
        contentType:"application/json;charset=utf-8",
        success:function (data) {
            if (data.flag==="1") {
                // alert(data.other);
                window.location.href = data.other.substring(0,data.other.indexOf("/html"))+"/index.html";
            } else {
                alert("退出登陆失败");
            }
        },
        error:function (data) {
            alert("1");
        }
    })
}