var redbagTable;
$(function () {
    redbagTableList();
    searchRedbagList();
})

function redbagTableList() {
    redbagTable = $('#redbag_table').dataTable({
        language: datatable_zn,
        'bAutoWidth': false,
        'bFilter': false,
        'bProcessing': false,
        'bSort': false,
        'bServerSide': true,
        'fnServerParams': function (aoData) {
            getAllSearchValue(aoData);
        },
        'sAjaxSource': '/sys/ajaxRedbag',
        "createdRow": function (row, data, index) {
            $('td', row).eq(5).css('text-align', "center");
        },
        'aoColumns': [
            {
                'mDataProp': 'id',
                'bSortable': false
            }, {
                'mDataProp': 'sendUserName',
                'bSortable': false
            }, {
                'mDataProp': 'amount',
                'bSortable': false
            }, {
                'mDataProp': 'count',
                'bSortable': false
            }, {
                'mDataProp': 'createTime',
                'bSortable': false,
                'mRender': function (data, type, row) {
                    return new Date(data).Format("yyyy-MM-dd hh:mm:ss");
                }
            }, {
                'mDataProp': 'remark',
                'bSortable': false,
            }
        ]
    });
}

function getAllSearchValue(aoData) {
    aoData.push({
        "name": 'sendMemberName',
        "value": trimStr($('#sendMemberName').val())
    });
    aoData.push({
        "name": 'startTime',
        "value": trimStr($('#startTime').val())
    });
    aoData.push({
        "name": 'endTime',
        "value": trimStr($('#endTime').val())
    });
}

function searchRedbagList() {
    $("#search").on("click", function () {
        redbagTable.fnDraw();
    });
}

function getAllOperation(row) {
    var btn = '';
    return btn;
}

$("#member_table").on("click", '#edit', function () {
    var id = $(this).attr("data-id");
    layer_show('编辑用户', '/sys/addUser?id=' + id + '', '', '510');
});

$("#member_table").on("click", '#remove', function () {
    var id = $(this).attr("data-id");
    layer.confirm('确认要删除吗？', function (index) {
        $.post(
            '/Sys/Remove', { id: id}, function (data) {
                if (data.success) {
                    layer.msg('操作成功', { icon: 1, time: 1000 });
                    memberTable.fnDraw();
                } else {
                    layer.msg(data.errorDesc, { icon: 5, time: 1000 });
                }
            }
        );
    });
});