layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;


    var tableIns = table.render({
        id:'userTable'
        ,elem: '#userList'
        , height: 'full-125'
        , cellMinWidth: 95
        , url: ctx + '/user/list' //数据接口
        , page: true //开启分页
        , limit: 10
        , limits: [10, 20, 30, 40]
        , toolbar: '#toolbarDemo'
        , cols: [[ //表头
            // field：要求field属性值与返回的数据中对应的属性字段名一致
            // title：设置列的标题
            // sort：是否允许排序（默认：false）
            // fixed：固定列
            {type:'checkbox', fixed:'center'}
            ,{field: 'id', title: '编号',  sort: true, fixed: 'left'}
            ,{field: 'userName', title: '用户名称', align:'center'}
            ,{field: 'trueName', title: '真实姓名', align:'center'}
            ,{field: 'email', title: '用户邮箱', align:'center'}
            ,{field: 'phone', title: '用户号码', align:'center'}
            ,{field: 'createDate', title: '创建时间', align:'center'}
            ,{field: 'updateDate', title: '修改时间', align:'center'}
            ,{title:'操作',templet:'#userListBar', fixed: 'right', align:'center', minWidth:150}
        ]]
    });

    $(".search_btn").click(function () {
        tableIns.reload({
            where: { //设定异步数据接口的额外参数，任意设
                userName: $("[name='userName']").val()
                , email: $("[name='email']").val()
                , phone: $("[name='phone']").val()
            }
            , page: {
                curr: 1 //重新从第 1 页开始
            }
        });
    });

    table.on('toolbar(users)',function (data){
        if (data.event == "add"){
            console.log(data)
            openSaleChanceDialog();
        } else if(data.event == "del"){
            deleteSaleChance(data);
        }
    });

    table.on('tool(users)',function (data){
        if (data.event == "edit"){
            openSaleChanceDialog(data.data.id);
        } else if(data.event == "del"){
            layer.confirm('确定要删除该记录吗？',{icon:3,title:"用户管理"},function (index){
                layer.close(index);

                $.ajax({
                    type:"post",
                    url:ctx + "/user/deleteUser",
                    data:{
                        ids:data.data.id
                    },
                    success:function (result){
                        if (result.code == 200){
                            layer.msg("删除成功！",{icon: 6});
                            tableIns.reload();
                        } else {
                            layer.msg(result.msg, {icon:5});
                        }
                    }
                })
            });
        }
    });

    function openSaleChanceDialog(id){
        var title = "<h3>用户管理 - 添加用户</h3>";
        var url = ctx + "/user/toAddOrUpdateUserPage";
        if (id != null && id != ''){
            var title = "<h3>用户管理 - 更新用户</h3>";
            url += "?id=" + id;
        }
        layui.layer.open({
            // 类型
            type: 2,
            // 标题
            title: title,
            // 宽高
            area: ['650px', '400px'],
            // url地址
            content: url,
            // 可以最大化与最小化
            maxmin: true
        });
    }

    function deleteSaleChance(data){
        var checkStatus = table.checkStatus(data.config.id);
        console.log(checkStatus)
        var userData = checkStatus.data;
        if (userData < 1){
            layer.msg("请选择要删除的记录！",{icon:5});
            return;
        }
        layer.confirm("您确定要删除所选的记录吗",{icon:3,title:"用户管理"},function (index){
            layer.close(index);
            var ids = "ids=";
            for(var i = 0;i<userData.length;i++){
                if(i < userData.length - 1){
                    ids = ids + userData[i].id + "&ids=";
                } else {
                    ids = ids + userData[i].id;
                }
            }
            // console.log(ids);
            $.ajax({
                type:"post",
                url:ctx + "/user/deleteUser",
                data:ids,
                success:function (result){
                    if (result.code == 200){
                        layer.msg("删除成功！",{icon: 6});
                        tableIns.reload();
                    } else {
                        layer.msg(result.msg, {icon:5});
                    }
                }
            })
        })
    }
});