layui.use(['table', 'layer'], function () {
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;


    var tableIns = table.render({
        id: 'saleChanceTable'
        , elem: '#saleChanceList'
        , height: 'full-125'
        , cellMinWidth: 95
        , url: ctx + '/sale_chance/list?flag=1' //数据接口
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
            , {field: 'assignTime', title: '分配时间', align: 'center'}
            , {field: 'createDate', title: '创建时间', align: 'center'}
            , {field: 'updateDate', title: '修改时间', align: 'center'}
            , {
                field: 'devResult', title: '开发状态', align: 'center', templet: function (d) {
                    // 调用函数，返回格式化的结果
                    return formatDevResult(d.devResult);
                }
            }
            , {title: '操作', templet: '#op', fixed: 'right', align: 'center', minWidth: 150}
        ]]
    });

    /**
     * 格式化开发状态
     *  0 = 未开发
     *  1 = 开发中
     *  2 = 开发成功
     *  3 = 开发失败
     *  其他 = 未知
     * @param devResult
     */
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
                , devResult: $("#devResult").val()
            }
            , page: {
                curr: 1 //重新从第 1 页开始
            }
        });
    });

    table.on('tool(saleChances)',function (data){
        if(data.event == "dev"){
            openCusDevPlanDialog('计划项数据开发',data.data.id);
        }else if (data.event == "info"){
            openCusDevPlanDialog('计划项数据开发',data.data.id);
        }
    });

    function openCusDevPlanDialog(title, id){
        layui.layer.open({
            // 类型
            type: 2,
            // 标题
            title: title,
            // 宽高
            area: ['750px', '550px'],
            // url地址
            content: ctx + "/cus_dev_plan/toCusDevPlanPage?id=" + id,
            // 可以最大化与最小化
            maxmin: true
        });
    }

});
