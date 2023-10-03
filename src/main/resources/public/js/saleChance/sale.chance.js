layui.use(['table', 'layer'], function () {
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    var tableIns = table.render({
        id:'saleChanceTable'
        ,elem: '#saleChanceList'
        , height: 'full-125'
        , cellMinWidth: 95
        , url: ctx + '/sale_chance/list' //数据接口
        , page: true //开启分页
        , limit: 10
        , limits: [10, 20, 30, 40]
        , toolbar: '#toolbarDemo'
        , cols: [[ //表头
            // field：要求field属性值与返回的数据中对应的属性字段名一致
            // title：设置列的标题
            // sort：是否允许排序（默认：false）
            // fixed：固定列
            {type: 'checkbox', fixed: 'center'}
            , {field: 'id', title: '编号', sort: true, fixed: 'left'}
            , {field: 'chanceSource', title: '机会来源', align: 'center'}
            , {field: 'customerName', title: '客户名称', align: 'center'}
            , {field: 'cgjl', title: '成功几率', align: 'center'}
            , {field: 'overview', title: '概要', align: 'center'}
            , {field: 'linkMan', title: '联系人', align: 'center'}
            , {field: 'linkPhone', title: '联系号码', align: 'center'}
            , {field: 'description', title: '描述', align: 'center'}
            , {field: 'createMan', title: '创建人', align: 'center'}
            , {field: 'uname', title: '分配人', align: 'center'}
            , {field: 'assignTime', title: '分配时间', align: 'center'}
            , {field: 'createDate', title: '创建时间', align: 'center'}
            , {field: 'updateDate', title: '修改时间', align: 'center'}
            , {
                field: 'state', title: '分配状态', align: 'center', templet: function (d) {
                    // 调用函数，返回格式化的结果
                    return formatState(d.state);
                }
            }
            , {
                field: 'devResult', title: '开发状态', align: 'center', templet: function (d) {
                    // 调用函数，返回格式化的结果
                    return formatDevResult(d.devResult);
                }
            }
            , {title: '操作', templet: '#saleChanceListBar', fixed: 'right', align: 'center', minWidth: 150}
        ]]
    });

    /**
     * 格式化分配状态值
     *  0 = 未分配
     *  1 = 已分配
     *  其他 = 未知
     * @param state
     */
    function formatState(state) {
        if (state == 0) {
            return "<div style='color: yellow'>未分配</div>";
        } else if (state == 1) {
            return "<div style='color: green'>已分配</div>";
        } else {
            return "<div style='color: red'>未知</div>";
        }
    }

    function formatDevResult(devResult) {
        if (devResult == 0) {
            return "<div style='color: yellow'>未开发</div>";
        } else if (devResult == 1) {
            return "<div style='color: orange'>开发中</div>";
        } else if (devResult == 2) {
            return "<div style='color: green'>开发成功</div>";
        } else if (devResult == 3) {
            return "<div style='color: red'>开发失败</div>";
        } else {
            return "<div style='color: blue'>未知</div>";
        }
    }

    $(".search_btn").click(function () {
        tableIns.reload({
            where: { //设定异步数据接口的额外参数，任意设
                customerName: $("[name='customerName']").val()
                , createMan: $("[name='createMan']").val()
                , state: $("#state").val()
            }
            , page: {
                curr: 1 //重新从第 1 页开始
            }
        });
    });
    //监听头部工具栏事件
    table.on('toolbar(saleChances)', function (data) {
        // console.log(data)
        if (data.event == "add") {
            openSaleChanceDialog();
        } else if (data.event == "del") {
            deleteSaleChance(data);
        }
    })

    function openSaleChanceDialog(saleChanceId) {
        var title = "<h3>营销机会管理 - 添加营销机会</h3>";
        var url = ctx + "/sale_chance/toSaleChancePage"

        if (saleChanceId != null && saleChanceId != ''){
            title = "<h3>营销机会管理 - 更新营销机会</h3>";
            url += '?saleChanceId=' + saleChanceId;
        }
        // iframe层
        layui.layer.open({
            // 类型
            type: 2,
            // 标题
            title: title,
            // 宽高
            area: ['500px', '620px'],
            // url地址
            content: url,
            // 可以最大化与最小化
            maxmin: true
        });
    }

    function deleteSaleChance(data){
        var checkStatus = table.checkStatus("saleChanceTable");
        console.log(checkStatus)

        var saleChanceData = checkStatus.data;

        if (saleChanceData < 1){
            layer.msg("请选择要删除的记录！",{icon:5});
            return;
        }
        layer.confirm("您确定要删除所选的记录吗",{icon:3,title:"营销机会管理"},function (index){
            layer.close(index);
            var ids = "ids=";
            for(var i = 0;i<saleChanceData.length;i++){
               if(i < saleChanceData.length - 1){
                   ids = ids + saleChanceData[i].id + "&ids=";
               } else {
                   ids = ids + saleChanceData[i].id;
               }
            }
            // console.log(ids);
            $.ajax({
                type:"post",
                url:ctx + "/sale_chance/delete",
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


    //监听行工具栏
    table.on('tool(saleChances)',function (data){
        if (data.event == "edit") {
            var saleChanceId = data.data.id;
            openSaleChanceDialog(saleChanceId)
        } else if (data.event == "del") {
            layer.confirm('确定要删除该记录吗？',{icon:3,title:"营销机会管理"},function (index){
                layer.close(index);

                $.ajax({
                    type:"post",
                    url:ctx + "/sale_chance/delete",
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
    })

});
