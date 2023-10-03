layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    var tableIns = table.render({
        id:'cusDevPlanTable'
        ,elem: '#cusDevPlanList'
        , height: 'full-125'
        , cellMinWidth: 95
        , url: ctx + '/cus_dev_plan/list?saleChanceId=' + $("[name='id']").val() //数据接口
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
            ,{field: 'planItem', title: '计划项', align:'center'}
            ,{field: 'planDate', title: '计划时间', align:'center'}
            ,{field: 'exeAffect', title: '执行效果', align:'center'}
            ,{field: 'createDate', title: '创建时间', align:'center'}
            ,{field: 'updateDate', title: '修改时间', align:'center'}
            ,{title:'操作',templet:'#cusDevPlanListBar', fixed: 'right', align:'center', minWidth:150}
        ]]
    });

    table.on('toolbar(cusDevPlans)',function (data){
        if (data.event == "add"){
            openAddOrUpdateCusDevPlanDialog();
        }else if(data.event == "success"){
                updateSaleChanceDevResult(2);
        }else if(data.event == "failed"){
            updateSaleChanceDevResult(3);
        }
    });

    table.on('tool(cusDevPlans)',function (data){
        if (data.event == "edit"){
            openAddOrUpdateCusDevPlanDialog(data.data.id);
        } else if(data.event == "del"){
            deleteCusDevPlan(data.data.id);
        }
    })

        function openAddOrUpdateCusDevPlanDialog(id){
        var title = "计划项管理 - 添加计划项";
        var url = ctx + "/cus_dev_plan/toAddOrUpdateCusDevPlanPage?sId=" + $("[name='id']").val();
        if(id != null && id != ''){
            title = "计划项管理 - 更新计划项";
            url += "&id=" + id;
        }
        // iframe层
        layui.layer.open({
            // 类型
            type: 2,
            // 标题
            title: title,
            // 宽高
            area: ['500px', '300px'],
            // url地址
            content: url,
            // 可以最大化与最小化
            maxmin: true
        });
    }

    function deleteCusDevPlan(id) {
        layer.confirm("您确定要删除所选的记录吗",{icon:3,title:"开发数据管理"},function (index){
            $.post(ctx + "/cus_dev_plan/delete",{id:id},function (result){
                if (result.code == 200){
                    layer.msg("删除成功",{icon:6});
                    tableIns.reload();
                } else {
                    layer.msg(result.msg,{icon: 5});
                }
            })

        });
    }

    function updateSaleChanceDevResult(devResult){
        layer.confirm("您确定执行该操作吗？",{icon:3,title:"营销机会管理"},function (index){
            var sId = $("[name='id']").val();
            $.post(ctx + "/sale_chance/updateSaleChanceDevResult",{id:sId,devResult:devResult},function (result){
                if(result.code == 200){
                    layer.msg("更新成功",{icon:6});
                    layer.closeAll("iframe");
                    parent.location.reload();
                }else {
                    layer.msg(result.msg,{icon:5});
                }
            });
        })
    }


});
