layui.use(['form', 'layer','formSelects'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    var formSelects = layui.formSelects;

    form.on('submit(addOrUpdateUser)',function (data){
        var index = top.layer.msg("数据提交中,请稍后...",{
            icon:16, // 图标
            time:false, // 不关闭
            shade:0.8 // 设置遮罩的透明度
        });

        var formData = data.field;
        // console.log(formData)

        var url = ctx + "/user/add";
        if($('[name="id"]').val()){
            var url = ctx + "/user/update";
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

    var userId = $("[name='id']").val();
    formSelects.config("selectId",{
        type:"post",
        searchUrl:ctx + "/role/queryAllRoles?userId=" + userId,
        keyName:'roleName',
        keyVal:'id'
    },true);


});