var monitorTable;
$(function () {
    monitorTableList();
    searchMonitorTableList();
})

function monitorTableList() {
    monitorTable = $('#monitor_table').dataTable({
        language: datatable_zn,
        'bAutoWidth': false,
        'bFilter': false,
        'bProcessing': false,
        'bSort': false,
        'bServerSide': true,
        'fnServerParams': function (aoData) {
            getAllSearchValue(aoData);
        },
        'sAjaxSource': '/console/ajaxMonitor',
        "createdRow": function (row, data, index) {
            $('td', row).eq(5).css('text-align', "center");
        },
        'aoColumns': [
            {
                'mDataProp': 'serverIp',
                'bSortable': false
            },{
                'mDataProp': 'serverDomain',
                'bSortable': false,
            },{
                'mDataProp': 'httpPort',
                'bSortable': false,
            }, {
                'mDataProp': 'websocketPort',
                'bSortable': false
            }, {
                'mDataProp': 'remark',
                'bSortable': false
            }, {
                'mDataProp': 'operation',
                'bSortable': false,
                'mRender': function (data, type, row) {
                    return getAllOperation(row);
                }
            }
        ]
    });
}

function getAllSearchValue(aoData) {
    aoData.push({
        "name": 'serverIp',
        "value": trimStr($('#serverIp').val())
    });
    aoData.push({
        "name": 'serverDomain',
        "value": trimStr($('#serverDomain').val())
    });
}

function getAllOperation(row) {
    var remove = '<input id="remove" data-id="' + row.id + '" class="btn btn-danger radius" type="button" value="删除">';
    var queryCount = '<input id="queryCount" data-id="' + row.id + '" class="btn btn-secondary radius" type="button" value="查询在线人数">';
    return remove + queryCount;
}

function searchMonitorTableList() {
    $("#search").on("click", function () {
        monitorTable.fnDraw();
    });
}

$("#monitor_table").on("click", '#remove', function () {
    var id = $(this).attr("data-id");
    layer.confirm("确定删除服务器", function (result) {
        if (result) {
            $.post(
                '/console/deleteMonitorServer', { id: id }, function (data) {
                    if (data.success) {
                        layer.msg('操作成功', { icon: 1, time: 1000 });
                        monitorTable.fnDraw();
                    } else {
                        layer.msg(data.errorDesc, { icon: 5, time: 1000 });
                    }
                }
            );
        }
    })
});

$("#monitor_table").on("click", '#queryCount', function () {
    var id = $(this).attr("data-id");
    $.post(
        '/console/queryOnlineCount', {id: id}, function (data) {
            if (data.success) {
                layer.alert('在线人数【' + data.result + '】', {icon: 6});
            } else {
                layer.msg(data.errorDesc, {icon: 5, time: 1000});
            }
        }
    );
});