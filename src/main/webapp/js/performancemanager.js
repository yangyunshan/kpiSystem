$(function () {
    $(document).ready(function () {
        $.ajax({
            url: "initCommonUserInfo",
            type: "get",
            dataType: "json",
            contentType: "application/json;charset=utf-8",
            success: function (data) {
                initTable(data);
            }
        })
    })
})

function initData() {
    $(document).ready(function () {
        $.ajax({
            url: "initCommonUserInfo",
            type: "get",
            dataType: "json",
            contentType: "application/json;charset=utf-8",
            success: function (data) {
                initTable(data);
            },
        })
    })
}

function initTable(data) {
    var status = data.status;
    if (status == 1) {
        $("button").attr("disabled", true);
    }

    var items = data.items;
    var count = items.length;
    var sumScore123 = 0;
    // var sumScore4 = 0;

    for (var i = 0; i < count; i++) {
        var id = items[i].id;
        console.log(items[i].deptNo);
        if (!id.startsWith("d")) {
            $("#deptNo_" + id).val(items[i].deptNo);
            $("#participantNo_" + id).val(items[i].participantNo);
            $("#rank_" + id).val(items[i].rank);
            $("#count_" + id).val(items[i].count);
            if(id!='b6'&&id!='b7'&&id!='b8')
                $("#description_" + id).val(items[i].description);
            // $("#filename_"+id).attr("href",items[i].filePath);
            // $("#filename_"+id).html(items[i].fileName);
            $("#score_" + id).val(items[i].score);
            $("#button_" + id).html("修改");
            sumScore123 += parseFloat(items[i].score);

        } else {
            $("#description_" + id).val(items[i].description);
        }

        var filenames = items[i].fileName;
        var filepaths = items[i].filePath;
        var html = "";
        for (var j = 0; j < filenames.length; j++) {
            html += "<a href='" + filepaths[j] + "'>" + filenames[j] + "||</a>";
        }
        $("#filename_" + id).html(html);
    }
    $("#sumScore123").val(sumScore123.toFixed(2));
    $("#sumScore4").val((data.score - sumScore123).toFixed(2));
    $("#sumScore").val(data.score.toFixed(2));
}

function submitInfoWithoutFile(id) {
    $(document).ready(function () {
        $("#" + id).html("(未提交)");

        var data = new FormData();

        if ($("#count_" + id).val() === "0" || $("#description_" + id).val() === "") {
            alert("缺少必要信息，无法提交！");
            return;
        }

        data.append("itemid", id);
        data.append("itemname", id);
        data.append("deptNo", $("#deptNo_" + id).val());
        data.append("participantNo", $("#participantNo_" + id).val());
        data.append("rank", $("#rank_" + id).val());
        data.append("count", $("#count_" + id).val());
        data.append("description", $("#description_" + id).val());
        data.append("score", $("#score_" + id).val());

        var url = "addInfoWithoutFile";

        $.ajax({
            url: url,
            type: "post",
            dataType: "json",
            data: data,
            processData: false,
            contentType: false,
            success: function (data) {
                if (data.flag === "success") {
                    $("#" + id).html("(已提交)");
                    initData();
                } else {
                    $("#" + id).html("(提交失败)");
                    return;
                }
            }
        })
    })
}

function submitInfo(id) {
    $(document).ready(function () {
        $("#" + id).html("(未提交)");

        var element = document.getElementById("file_" + id);
        var data = new FormData();
        var url;

        if ($("#count_" + id).val() === "0" || $("#description_" + id).val() === "") {
            alert("缺少必要信息，无法提交！");
            return;
        }
        if ($("#filename_" + id).html() === "" && element.files.length < 1) {
            alert("缺少文件信息，无法提交！");
            return;
        }

        if (id == 'b7' || id == 'b6' || id == 'b8') {
            if (document.getElementById("ifSumb_" + id).innerHTML == 'no') {
                alert("文件字数不足100，无法提交！");
                return;
            }
        }
        data.append("itemid", id);
        data.append("itemname", id);
        data.append("deptNo", $("#deptNo_" + id).val());
        data.append("participantNo", $("#participantNo_" + id).val());
        data.append("rank", $("#rank_" + id).val());
        data.append("count", $("#count_" + id).val());
        if (id != 'b6' && id != 'b7' && id != 'b8')
            data.append("description", $("#description_" + id).val());
        else
            data.append("description", "no description");
        data.append("score", $("#score_" + id).val());
        if (element.files.length > 0) {
            for (var i = 0; i < element.files.length; i++) {
                data.append("files", element.files[i]);
            }
            url = "addInfoWithFile";
        } else {
            url = "updateInfoWithoutFileByCommon";
        }

        $.ajax({
            url: url,
            type: "post",
            dataType: "json",
            data: data,
            processData: false,
            contentType: false,
            success: function (data) {
                if (data.flag === "success") {
                    $("#" + id).html("(已提交)");
                    initData();
                } else {
                    $("#" + id).html("(提交失败)");
                    return;
                }
            }
        })
    })
}

function getScore(id) {
    $(document).ready(function () {
        var deptNo = $("#deptNo_" + id).val();
        var participantNo = $("#participantNo_" + id).val();
        var rank = $("#rank_" + id).val();
        var count = $("#count_" + id).val();

        var weight = getWeightByItemId(id, rank);
        var score;
        if (parseInt(deptNo) != null && parseInt(participantNo) != null && parseInt(rank) != null && parseFloat(count) != null) {
            score = 0.5 ** parseInt(deptNo - 1) * 0.5 ** parseInt(participantNo - 1) * parseFloat(count) * weight;
        }
        $("#score_" + id).val(score.toFixed(2));
    })
}

function getSumScore() {
    $(document).ready(function () {
        $.ajax({
            url: "getSumScore",
            type: "post",
            dataType: "json",
            contentType: "application/json;charset=utf-8",
            success: function (data) {
                $("#sumScore").val(data.score);
            }
        })
    })
}

function getWeightByItemId(itemId, rank) {
    var weight;
    switch (itemId) {
        case "a1": {
            switch (parseInt(rank)) {
                case 1: {
                    weight = 5;
                    break;
                }
                case 2: {
                    weight = 2;
                    break;
                }
            }
            break;
        }
        case 'a2': {
            weight = 5;
            break;
        }
        case 'a3': {
            switch (parseInt(rank)) {
                case 1: {
                    weight = 3;
                    break;
                }
                case 2: {
                    weight = 2;
                    break;
                }
            }
            break;
        }
        case 'a4': {
            switch (parseInt(rank)) {
                case 1: {
                    weight = 5;
                    break;
                }
                case 2: {
                    weight = 4;
                    break;
                }
                case 3: {
                    weight = 3;
                    break;
                }
                case 4: {
                    weight = 2;
                    break;
                }
            }
            break;
        }
        case 'a5': {
            weight = 10;
            break;
        }
        case 'a6': {
            switch (parseInt(rank)) {
                case 1: {
                    weight = 10;
                    break;
                }
                case 2: {
                    weight = 8;
                    break;
                }
                case 3: {
                    weight = 6;
                    break;
                }
            }
            break;
        }
        case 'a7': {
            weight = 15;
            break;
        }
        case 'a8': {
            switch (parseInt(rank)) {
                case 1: {
                    weight = 3;
                    break;
                }
                case 2: {
                    weight = 4;
                    break;
                }
                case 3: {
                    weight = 5;
                    break;
                }
            }
            break;
        }
        case 'a9': {
            switch (parseInt(rank)) {
                case 1: {
                    weight = 10;
                    break;
                }
                case 2: {
                    weight = 5;
                    break;
                }
                case 3: {
                    weight = 4;
                    break;
                }
                case 4: {
                    weight = 3;
                    break;
                }
                case 5: {
                    weight = 2;
                    break;
                }
            }
            break;
        }
        case 'b1': {
            weight = 0.2;
            break;
        }
        case 'b2': {
            weight = 0.5;
            break;
        }
        case 'b3': {
            weight = 0.2;
            break;
        }
        case 'b4': {
            weight = 0.15;
            break;
        }
        case 'b5': {
            weight = 0.3;
            break;
        }
        case 'b6': {
            weight = 0.2;
            break;
        }
        case 'b7': {
            weight = 0.2;
            break;
        }
        case 'b8': {
            weight = 0.2;
            break;
        }
        case 'b9': {
            switch (parseInt(rank)) {
                case 1: {
                    weight = 10;
                    break;
                }
                case 2: {
                    weight = 6;
                    break;
                }
                case 3: {
                    weight = 4;
                    break;
                }
                case 4: {
                    weight = 3;
                    break;
                }
                case 5: {
                    weight = 2;
                    break;
                }
            }
            break;
        }
        case 'b10': {
            switch (parseInt(rank)) {
                case 1: {
                    weight = 4;
                    break;
                }
                case 2: {
                    weight = 3;
                    break;
                }
                case 3: {
                    weight = 2;
                    break;
                }
                case 4: {
                    weight = 2;
                    break;
                }
                case 5: {
                    weight = 1;
                    break;
                }
                case 6: {
                    weight = 0.5;
                    break;
                }
            }
            break;
        }
        case 'b11': {
            weight = 1;
            break;
        }
        case 'b12': {
            weight = 3;
            break;
        }
        case 'b13': {
            switch (parseInt(rank)) {
                case 1: {
                    weight = 5;
                    break;
                }
                case 2: {
                    weight = 4;
                    break;
                }
                case 3: {
                    weight = 3;
                    break;
                }
                case 4: {
                    weight = 2;
                    break;
                }
            }
            break;
        }
        case 'b14': {
            weight = 0.5;
            break;
        }
        case 'b15': {
            weight = 0.5;
            break;
        }
        case 'c1': {
            switch (parseInt(rank)) {
                case 1: {
                    weight = 80;
                    break;
                }
                case 2: {
                    weight = 40;
                    break;
                }
            }
            break;
        }
        case 'c2': {
            switch (parseInt(rank)) {
                case 1: {
                    weight = 30;
                    break;
                }
                case 2: {
                    weight = 20;
                    break;
                }
                case 3: {
                    weight = 15;
                    break;
                }
            }
            break;
        }
        case 'c3': {
            switch (parseInt(rank)) {
                case 1: {
                    weight = 80;
                    break;
                }
                case 2: {
                    weight = 40;
                    break;
                }
            }
            break;
        }
        case 'c4': {
            switch (parseInt(rank)) {
                case 1: {
                    weight = 80;
                    break;
                }
                case 2: {
                    weight = 40;
                    break;
                }
            }
            break;
        }
        case 'c5': {
            switch (parseInt(rank)) {
                case 1: {
                    weight = 80;
                    break;
                }
                case 2: {
                    weight = 40;
                    break;
                }
            }
            break;
        }
        case 'c6': {
            switch (parseInt(rank)) {
                case 1: {
                    weight = 80;
                    break;
                }
                case 2: {
                    weight = 40;
                    break;
                }
            }
            break;
        }
        case 'c7': {
            switch (parseInt(rank)) {
                case 1: {
                    weight = 30;
                    break;
                }
                case 2: {
                    weight = 15;
                    break;
                }
            }
            break;
        }
        case 'c8': {
            switch (parseInt(rank)) {
                case 1: {
                    weight = 20;
                    break;
                }
                case 2: {
                    weight = 12;
                    break;
                }
                case 3: {
                    weight = 6;
                    break;
                }
            }
            break;
        }
        case 'c9': {
            switch (parseInt(rank)) {
                case 1: {
                    weight = 6;
                    break;
                }
                case 2: {
                    weight = 5;
                    break;
                }
                case 3: {
                    weight = 4;
                    break;
                }
            }
            break;
        }
        default: {
            weight = 0;
        }
    }
    return weight;
}

function deleteData(id) {
    $(document).ready(function () {
        if (!confirm("确定删除此项？")) {
            return;
        }
        var data = {
            "itemid": id,
        };
        $.ajax({
            url: "deleteData",
            type: "post",
            dataType: "json",
            contentType: "application/json;charset=utf-8",
            data: JSON.stringify(data),
            success: function (data) {
                if (data.flag === "success") {
                    $("#delete_" + id).html("已删除");
                    freshitem(id);
                    initData();
                } else {
                    $("#delete_" + id).html("未删除");
                    freshitem(id);
                    initData();
                }
            }
        })
    })
}

function freshitem(id) {
    $(document).ready(function () {
        var data = {
            "itemid": id,
        };
        $.ajax({
            url: "freshItem",
            type: "post",
            dataType: "json",
            contentType: "application/json;charset=utf-8",
            data: JSON.stringify(data),
            success: function (data) {
                if (data.flag === "0") {
                    $("#deptNo_" + id).val(1);
                    $("#participantNo_" + id).val(1);
                    $("#rank_" + id).val(1);
                    $("#count_" + id).val(0);
                    $("#description_" + id).val("");
                    $("#score_" + id).val(0);
                    $("#filename_" + id).html("");
                    $("#" + id).html("未提交");
                } else {
                    return;
                }

            }
        })
    })
}