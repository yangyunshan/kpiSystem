function login() {
    $(document).ready(function () {
        var id = $("#uid").val();
        var password = $("#password").val();
        console.log(id);
        var jsonStr = {
            "id":id,
            "password":password
        };
        $.ajax({
            url:"login",
            type:"POST",
            dataType:"json",
            data:JSON.stringify(jsonStr),
            contentType:"application/json;charset=utf-8",
            success:function (data) {
                console.log(data);
                if (data.flag==="success") {
                    if (data.other==="0") {
                        window.location.href = "html/performancemanager.html";
                    }
                    if (data.other==="1") {
                        window.location.href = "html/report.html";
                    }
                }
                if (data.flag==="fail") {
                    alert("工号或密码错误");
                }
            },
            error:function (data) {
                console.log(data);
                alert("!!!");
            }
        })
    })
}