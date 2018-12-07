var roleTable;
$(function () {
    roleTableList();
    searchRoleTableList();
})

function roleTableList() {
    roleTable = $('#role_table').dataTable({
        language: datatable_zn,
        'bAutoWidth': false,
        'bFilter': false,
        'bProcessing': false,
        'bSort': false,
        'bServerSide': true,
        'fnServerParams': function (aoData) {

        },
        'sAjaxSource': '/sys/ajaxRole',
        "createdRow": function (row, data, index) {
            $('td', row).eq(5).css('text-align', "center");
        },
        'aoColumns': [
            {
                'mDataProp': 'roleName',
                'bSortable': false,
            }, {
                'mDataProp': 'roleIcon',
                'bSortable': false,
                'mRender': function (data, type, row) {
                    if (data.indexOf("http")>=0){
                        return '<img width="50" class="picture-thumb" src="' + data + '">';
                    }
                    return '<img class="picture-thumb '+data+'">';
                }
            }, {
                'mDataProp': 'createTime',
                'bSortable': false,
                'mRender': function (data, type, row) {
                    return new Date(data).Format("yyyy-MM-dd hh:mm:ss");
                }
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

function getAllOperation(row) {
    var btn = '';
    var edit = '<input id="edit" data-id="' + row.id + '" class="btn btn-secondary radius" type="button" value="修改">';
    var remove = '<input id="remove" data-id="' + row.id + '" class="btn btn-danger radius" type="button" value="删除">';

    if ($.inArray("sys:addRole", permUrls) > 0) {
        btn += edit;
    }
    if ($.inArray("sys:removeRole", permUrls) > 0) {
        btn += remove;
    }
    return btn;
}

function searchRoleTableList() {
    $("#search").on("click", function () {
        roleTable.fnDraw();
    });
}

$("#role_table").on("click", '#edit', function () {
    var id = $(this).attr("data-id");
    layer_show('编辑开奖网', '/sys/addRole?id=' + id + '', '', '800');
});

$("#role_table").on("click", '#remove', function () {
    var id = $(this).attr("data-id");
    layer.confirm("确定删除该角色？", function (result) {
        if (result) {
            $.post(
                '/sys/removeRole', { id: id }, function (data) {
                    if (data.success) {
                        layer.msg('操作成功', { icon: 1, time: 1000 });
                        roleTable.fnDraw();
                    } else {
                        layer.msg(data.errorDesc, { icon: 5, time: 1000 });
                    }
                }
            );
        }
    })
});