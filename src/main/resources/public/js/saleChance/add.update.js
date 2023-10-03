layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;


    form.on('submit(addOrUpdateSaleChance)',function (data){
        // 提交数据时的加载层 （https://layer.layui.com/）
        var index = layer.msg("数据提交中,请稍后...",{
            icon:16, // 图标
            time:false, // 不关闭
            shade:0.8 // 设置遮罩的透明度
        });
        var url = ctx + "/sale_chance/add";
        var saleChanceId = $("[name='id']").val();
        if(saleChanceId != null && saleChanceId != ''){
            url = ctx + "/sale_chance/update";
        }

        $.post(url,data.field,function (result){
            if (result.code == "200"){
                layer.msg("操作成功",{icon: 6});
                layer.close(index);
                layer.closeAll("iframe");
                parent.location.reload();
            } else {
                layer.msg(result.msg,{icon: 5});
            }
        })

        //return true;
        return false;
    })


    $("#closeBtn").click(function (){
        // 当你在iframe页面关闭自身时
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭
    })

    $.ajax({
        type:"get",
        url: ctx + "/user/queryAllSales",
        data:{},
        success:function (data){
            // console.log(data)
            if(data != null){
                var assignManId = $("#assignManId").val();
                for(var i = 0; i < data.length; i++){
                    var opt = "";
                    if (assignManId == data[i].id){
                        opt = "<option value='"+data[i].id+"' selected>"+data[i].uname+"</option>";
                    } else {
                        opt = "<option value='"+data[i].id+"'>"+data[i].uname+"</option>";
                    }

                    $("#assignMan").append(opt);
                }
            }
            layui.form.render("select");
        }
    })
});