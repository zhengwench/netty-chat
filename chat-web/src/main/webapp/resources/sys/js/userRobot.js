var userRobotTable;
$(function () {
    userRobotTableList();
    searchUserRobot();
})

function userRobotTableList() {
    userRobotTable = $('#userRobot_table').dataTable({
        language: datatable_zn,
        'bAutoWidth': false,
        'bFilter': false,
        'bProcessing': false,
        'bSort': false,
        'bServerSide': true,
        'fnServerParams': function (aoData) {
            getAllSearchValue(aoData);
        },
        'sAjaxSource': '/sys/ajaxRobot',
        "createdRow": function (row, data, index) {
            $('td', row).eq(5).css('text-align', "center");
        },
        'aoColumns': [
            {
                'mDataProp': 'id',
                'bSortable': false,
                'mRender': function (data, type, row) {
                    return '<input type="checkbox" value="' + data + '">';
                }
            }, {
                'mDataProp': 'userName',
                'bSortable': false,
            }, {
                'mDataProp': 'roomName',
                'bSortable': false,
            }, {
                'mDataProp': 'createTime',
                'bSortable': false,
                'mRender': function (data, type, row) {
                    return new Date(data).Format("yyyy-MM-dd hh:mm:ss");
                }
            }
        ]
    });
}

function getAllSearchValue(aoData) {
    aoData.push({
        "name": 'roomName',
        "value": trimStr($('#roomName').val())
    });
}

function searchUserRobot() {
    $("#search").on("click", function () {
        userRobotTable.fnDraw();
    });
}

function deleteBatch() {
    var roborids = [];
    $("input[type=checkbox]").each(function () {
        if (this.checked) {
            roborids.push($(this).val());
        }
    });
    if (roborids.length<1){
        layer.msg("请选择需要删除的数据");
        return;
    }
    layer.confirm('确认要批量删除吗？', function (index) {
        jQuery.post(
            '/sys/deleteBatchRobot', { ids:roborids.join(',')}, function (data) {
                if (data.success) {
                    userRobotTable.fnDraw();
                }else {
                    layer.msg(data.errorDesc);
                }
            }
        );
        layer.closeAll();
    });
}