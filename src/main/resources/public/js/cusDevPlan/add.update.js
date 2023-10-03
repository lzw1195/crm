layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;


    form.on('submit(addOrUpdateCusDevPlan)',function (data){
        var index = top.layer.msg("数据提交中,请稍后...",{
            icon:16, // 图标
            time:false, // 不关闭
            shade:0.8 // 设置遮罩的透明度
        });

        var formData = data.field;
        var url = ctx + "/cus_dev_plan/add";
        if($('[name="id"]').val()){
            var url = ctx + "/cus_dev_plan/update";
        }

        $.post(url,formData,function (result){
            if (result.code == "200"){
                top.layer.msg("操作成功",{icon: 6});
                top.layer.close(index);
                layer.closeAll("iframe");
                parent.location.reload();
            } else {
                layer.msg(result.msg,{icon: 5});
            }
        });
        return false;
    });


    $("#closeBtn").click(function (){
        // 当你在iframe页面关闭自身时
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭
    })

});