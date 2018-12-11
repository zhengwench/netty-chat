var ipTable;
$(function () {
    ipTableList();
    searchIpList();
})

function ipTableList() {
    ipTable = $('#ip_table').dataTable({
        language: datatable_zn,
        'bAutoWidth': false,
        'bFilter': false,
        'bProcessing': false,
        'bSort': false,
        'bServerSide': true,
        'fnServerParams': function (aoData) {
            getAllSearchValue(aoData);
        },
        'sAjaxSource': '/sys/ajaxBlackIp',
        "createdRow": function (row, data, index) {
            $('td', row).eq(5).css('text-align', "center");
        },
        'aoColumns': [
            {
                'mDataProp': 'ip',
                'bSortable': false
            }, {
                'mDataProp': 'createTime',
                'bSortable': false,
                'mRender': function (data, type, row) {
                    return new Date(data).Format("yyyy-MM-dd hh:mm:ss");
                }
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
        "name": 'ip',
        "value": trimStr($('#ip').val())
    });
}

function searchIpList() {
    $("#search").on("click", function () {
        ipTable.fnDraw();
    });
}

function getAllOperation(row) {
    var btn = '';
    var remove = '<input id="remove" data-id="' + row.id + '" class="btn btn-danger radius" type="button" value="删除">';
    if ($.inArray("sys:removeBlackip", permUrls) > 0) {
        btn += remove;
    }
    return btn;
}

$("#ip_table").on("click", '#edit', function () {
    var id = $(this).attr("data-id");
    layer_show('编辑用户', '/sys/addUser?id=' + id + '', '', '510');
});

$("#ip_table").on("click", '#remove', function () {
    var id = $(this).attr("data-id");
    layer.confirm('确认要删除吗？', function (index) {
        $.post(
            '/sys/removeBlackip', { id: id}, function (data) {
                if (data.success) {
                    layer.msg('操作成功', { icon: 1, time: 1000 });
                    ipTable.fnDraw();
                } else {
                    layer.msg(data.errorDesc, { icon: 5, time: 2000 });
                }
            }
        );
    });
});