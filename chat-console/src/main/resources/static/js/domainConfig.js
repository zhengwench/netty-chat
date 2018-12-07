var domainConfigTable;
$(function () {
    domainConfigTableList();
    searchDomainConfigTableList();
})

function domainConfigTableList() {
    domainConfigTable = $('#domainConfig_table').dataTable({
        language: datatable_zn,
        'bAutoWidth': false,
        'bFilter': false,
        'bProcessing': false,
        'bSort': false,
        'bServerSide': true,
        'fnServerParams': function (aoData) {
            getAllSearchValue(aoData);
        },
        'sAjaxSource': '/console/ajaxDomainConfig',
        "createdRow": function (row, data, index) {
            $('td', row).eq(5).css('text-align', "center");
        },
        'aoColumns': [
            {
                'mDataProp': 'id',
                'bSortable': false
            },{
                'mDataProp': 'parentId',
                'bSortable': false,
            }
            ,{
                'mDataProp': 'domainName',
                'bSortable': false,
            }, {
                'mDataProp': 'websocketUrl',
                'bSortable': false
            }, {
                'mDataProp': 'httpUrl',
                'bSortable': false
            }, {
                'mDataProp': 'startTime',
                'bSortable': false,
                'mRender': function (data, type, row) {
                    return new Date(data).Format("yyyy-MM-dd hh:mm:ss");
                }
            }, {
                'mDataProp': 'endTime',
                'bSortable': false,
                'mRender': function (data, type, row) {
                    return new Date(data).Format("yyyy-MM-dd hh:mm:ss");
                }
            }, {
                'mDataProp': 'status',
                'bSortable': false,
                'mRender': function (data, type, row) {
                    if (data==1){
                        return "正在运行";
                    }
                    return "已停止";
                }
            }, {
                'mDataProp': 'remarks',
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
        "name": 'domainName',
        "value": trimStr($('#domainName').val())
    });
    aoData.push({
        "name": 'parentId',
        "value": trimStr($('#parentId').val())
    });
}

function getAllOperation(row) {
    var btn = '';
    var edit = '<input id="edit" data-id="' + row.id + '" class="btn btn-secondary radius" type="button" value="修改">';
    var remove = '<input id="remove" data-id="' + row.id + '" class="btn btn-danger radius" type="button" value="删除">';
    btn = edit + remove;
    return btn;
}

function searchDomainConfigTableList() {
    $("#search").on("click", function () {
        domainConfigTable.fnDraw();
    });
}

$("#domainConfig_table").on("click", '#edit', function () {
    var id = $(this).attr("data-id");
    layer_show('编辑服务', '/console/addDomainConfig?id=' + id + '', '', '510');
});

$("#domainConfig_table").on("click", '#remove', function () {
    var id = $(this).attr("data-id");
    layer.confirm("确定删除服务", function (result) {
        if (result) {
            $.post(
                '/console/removeDomainConfig', { id: id }, function (data) {
                    if (data.success) {
                        layer.msg('操作成功', { icon: 1, time: 1000 });
                        domainConfigTable.fnDraw();
                    } else {
                        layer.msg(data.errorDesc, { icon: 5, time: 1000 });
                    }
                }
            );
        }
    })
});