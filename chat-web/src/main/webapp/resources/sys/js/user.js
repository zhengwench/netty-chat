var userTable;
$(function () {
    userTableList();
    searchUserList();
})

function userTableList() {
    userTable = $('#user_table').dataTable({
        language: datatable_zn,
        'bAutoWidth': false,
        'bFilter': false,
        'bProcessing': false,
        'bSort': false,
        'bServerSide': true,
        'fnServerParams': function (aoData) {
            getAllSearchValue(aoData);
        },
        'sAjaxSource': '/sys/ajaxUser',
        "createdRow": function (row, data, index) {
            $('td', row).eq(5).css('text-align', "center");
        },
        'aoColumns': [
            {
                'mDataProp': 'userName',
                'bSortable': false
            }
            // , {
            //     'mDataProp': 'password',
            //     'bSortable': false,
            // }
            , {
                'mDataProp': 'loginIp',
                'bSortable': false,
            }, {
                'mDataProp': 'roomName',
                'bSortable': false,
            }, {
                'mDataProp': 'roleName',
                'bSortable': false
            }, {
                'mDataProp': 'remark',
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
        "name": 'UserName',
        "value": trimStr($('#userName').val())
    });
    aoData.push({
        "name": 'RoleId',
        "value": trimStr($('#roleId').val())
    });
    aoData.push({
        "name": 'roomId',
        "value": trimStr($('#roomId').val())
    });
    aoData.push({
        "name": 'remark',
        "value": trimStr($('#remark').val())
    });
}

function searchUserList() {
    $("#search").on("click", function () {
        userTable.fnDraw();
    });
}

function getAllOperation(row) {
    var btn = '';
    var edit = '<input id="edit" data-id="' + row.id + '" class="btn btn-secondary radius" type="button" value="修改">';
    var join = '<input id="join" data-id="' + row.id + '" class="btn btn-default radius" type="button" value="拉入聊天室">';
    var unjoin = '<input id="unjoin" data-id="' + row.id + '" class="btn btn-warning radius" type="button" value="踢出聊天室">';
    var addBlackip = '<input id="addBlackip" data-ip="' + row.loginIp + '" class="btn btn-danger radius" type="button" value="加入ip黑名单">';
    var joinroom = '<input id="joinroom" data-id="' + row.id + '" class="btn btn-default radius" type="button" value="加入房间">';
    var updatePassword = '<input id="updatePassword" data-id="' + row.id + '" class="btn btn-warning radius" type="button" value="重置密码">';
    var updateRole = '<input id="updateRole" data-id="' + row.id + '" class="btn btn-warning radius" type="button" value="修改角色">';
    var remove = '<input id="remove" data-id="' + row.id + '" class="btn btn-danger radius" type="button" value="删除">';
    if ($.inArray("sys:addUser", permUrls) > 0) {
        btn += edit;
    }
    if (row.status == 0 && $.inArray("sys:joinChat", permUrls) > 0) {
        btn += join;
    }
    if (row.status == 1 && $.inArray("sys:unJoinChat", permUrls) > 0) {
        btn += unjoin;
    }
    if ($.inArray("sys:updateUserPassword", permUrls) > 0) {
        btn += updatePassword;
    }
    if ($.inArray("sys:updateUserRole", permUrls) > 0) {
        btn += updateRole;
    }
    if ($.inArray("sys:addBlackip", permUrls) > 0) {
        btn += addBlackip;
    }
    if ($.inArray("sys:removeUser", permUrls) > 0) {
        btn += remove;
    }
    return btn;
}

$("#user_table").on("click", '#edit', function () {
    var id = $(this).attr("data-id");
    layer_show('编辑用户', '/sys/addUser?id=' + id + '', '', '800');
});

$("#user_table").on("click", '#join', function () {
    var id = $(this).attr("data-id");
    layer.confirm('确认要加入聊天室吗？', function (index) {
        $.post(
            '/sys/joinChat', { id: id}, function (data) {
                if (data.success) {
                    layer.msg('操作成功', { icon: 1, time: 1000 });
                    userTable.fnDraw();
                } else {
                    layer.msg(data.errorDesc, { icon: 5, time: 2000 });
                }
            }
        );
    });
});

$("#user_table").on("click", '#addBlackip', function () {
    var ip = $(this).attr("data-ip");
    layer.confirm('确认要加入ip黑名单吗？', function (index) {
        $.post(
            '/sys/saveBlackIp', { ip:ip}, function (data) {
                if (data.success) {
                    layer.msg('操作成功', { icon: 1, time: 1000 });
                    userTable.fnDraw();
                } else {
                    layer.msg(data.errorDesc, { icon: 5, time: 2000 });
                }
            }
        );
    });
});

$("#user_table").on("click", '#unjoin', function () {
    var id = $(this).attr("data-id");
    layer.confirm('确认要踢出聊天室吗？', function (index) {
        $.post(
            '/sys/unJoinChat', { id: id}, function (data) {
                if (data.success) {
                    layer.msg('操作成功', { icon: 1, time: 1000 });
                    userTable.fnDraw();
                } else {
                    layer.msg(data.errorDesc, { icon: 5, time: 2000 });
                }
            }
        );
    });
});

$("#user_table").on("click", '#remove', function () {
    var id = $(this).attr("data-id");
    layer.confirm('确认要删除吗？', function (index) {
        $.post(
            '/sys/removeUser', { id: id}, function (data) {
                if (data.success) {
                    layer.msg('操作成功', { icon: 1, time: 1000 });
                    userTable.fnDraw();
                } else {
                    layer.msg(data.errorDesc, { icon: 5, time: 2000 });
                }
            }
        );
    });
});

$("#user_table").on("click", '#updatePassword', function () {
    var id = $(this).attr("data-id");
    layer_show('重置密码', '/sys/updateUserPassword?id=' + id + '', '', '800');
});

$("#user_table").on("click", '#updateRole', function () {
    var id = $(this).attr("data-id");
    layer_show('修改角色', '/sys/updateUserRole?id=' + id + '', '', '800');
});