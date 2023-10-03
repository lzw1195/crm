layui.use(['table','layer'],function(){
       var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

       var tableIns = table.render({
              id:'roleTable'
              // 容器元素的ID属性值
              ,elem: '#roleList'
              // 容器的高度 full-差值
              ,height: 'full-125'
              // 单元格最小的宽度
              ,cellMinWidth:95
              // 访问数据的URL（后台的数据接口）
              ,url: ctx + '/role/list'
              // 开启分页
              ,page: true
              // 默认每页显示的数量
              ,limit:10
              // 每页页数的可选项
              ,limits:[10,20,30,40,50]
              // 开启头部工具栏
              ,toolbar:'#toolbarDemo'
              // 表头
              ,cols: [[
                     // field：要求field属性值与返回的数据中对应的属性字段名一致
                     // title：设置列的标题
                     // sort：是否允许排序（默认：false）
                     // fixed：固定列
                     {type:'checkbox', fixed:'center'}
                     ,{field: 'id', title: '编号',  sort: true, fixed: 'left'}
                     ,{field: 'roleName', title: '角色名称', align:'center'}
                     ,{field: 'roleRemark', title: '角色备注', align:'center'}
                     ,{field: 'createDate', title: '创建时间', align:'center'}
                     ,{field: 'updateDate', title: '修改时间', align:'center'}
                     ,{title:'操作',templet:'#roleListBar', fixed: 'right', align:'center', minWidth:150}
              ]]
       });

       $(".search_btn").click(function () {
              tableIns.reload({
                     where: { //设定异步数据接口的额外参数，任意设
                            roleName: $("[name='roleName']").val()
                     }
                     , page: {
                            curr: 1 //重新从第 1 页开始
                     }
              });
       });



       table.on('toolbar(roles)', function (data) {
              // console.log(data)
              if (data.event == "add") {
                     openSaleChanceDialog();
              } else if (data.event == "grant") {
                     var checkStatus = table.checkStatus(data.config.id);
                     openAddGrantDialog(checkStatus.data);
              }
       });

       table.on('tool(roles)',function (data){
              if (data.event == "edit") {
                     var roleId = data.data.id;
                     openSaleChanceDialog(roleId)
              } else if (data.event == "del") {
                     var roleId = data.data.id;
                     // 弹出确认框，询问用户是否确认删除
                     layer.confirm('确定要删除该记录吗？',{icon:3, title:"角色管理"}, function (index) {
                            // 关闭确认框
                            layer.close(index);

                            // 发送ajax请求，删除记录
                            $.ajax({
                                   type:"post",
                                   url:ctx + "/role/delete",
                                   data:{
                                          roleId:roleId
                                   },
                                   success:function (result) {
                                          // 判断删除结果
                                          if (result.code == 200) {
                                                 // 提示成功
                                                 layer.msg("删除成功！",{icon:6});
                                                 // 刷新表格
                                                 tableIns.reload();
                                          } else {
                                                 // 提示失败
                                                 layer.msg(result.msg, {icon:5});
                                          }
                                   }
                            });
                     });
              }
       })


       function openSaleChanceDialog(roleId) {
              var title = "<h3>角色管理 - 添加角色</h3>";
              var url = ctx + "/role/toAddOrUpdateRolePage";

              if (roleId != null && roleId != ''){
                     title = "<h3>营销机会管理 - 更新营销机会</h3>";
                     url += '?roleId=' + roleId;
              }
              // iframe层
              layui.layer.open({
                     // 类型
                     type: 2,
                     // 标题
                     title: title,
                     // 宽高
                     area: ['500px', '400px'],
                     // url地址
                     content: url,
                     // 可以最大化与最小化
                     maxmin: true
              });
       }

       function openAddGrantDialog(data) {
              if(data.length == 0){
                     layer.msg("请选择要授权的角色",{icon:5});
                     return;
              }

              if(data.length > 1){
                     layer.msg("暂不支持批量数据授权",{icon:5});
                     return;
              }

              var url = ctx + "/module/toAddGrantPage?roleId="+data[0].id;
              var title = "<h3>角色管理 - 角色授权</h3>";
              layui.layer.open({
                     title:title,
                     content:url,
                     type:2,
                     area:["600px","600px"],
                     maxmin: true
              });
       }
});
