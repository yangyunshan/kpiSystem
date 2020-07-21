function updateUserInfo() {
    $(document).ready(function () {
        var password = $("#password").val();
        var phone = $("#phone").val();
        var email = $("#email").val();
        var data = {
            "password":password,
            "phone":phone,
            "email":email
        };
        $.ajax({
            url:"updateUserInfo",
            type:"post",
            dataType:"json",
            data:JSON.stringify(data),
            contentType:"application/json;charset=utf-8",
            success:function (data) {
                if (data.flag==="success") {
                    alert("成功");
                } else {
                    alert("失败");
                }
            }
        })
    })
}

$(function () {
    $(document).ready(function () {
        $.ajax({
            url:"getUserInfo",
            type:"get",
            dataType: "json",
            success:function (data) {
                $("#uid").val(data.id);
                $("#username").val(data.name);
                $("#password").val(data.password);
                $("#phone").val(data.phone);
                $("#email").val(data.email);
            }
        })
    })
})
