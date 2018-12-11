var roomTable;
$(function () {
    roomTableList();
    searchRoomList();
})

function roomTableList() {
    roomTable = $('#room_table').dataTable({
        language: datatable_zn,
        'bAutoWidth': false,
        'bFilter': false,
        'bProcessing': false,
        'bSort': false,
        'bServerSide': true,
        'fnServerParams': function (aoData) {
            getAllSearchValue(aoData);
        },
        'sAjaxSource': '/sys/ajaxRoom',
        "createdRow": function (row, data, index) {
            $('td', row).eq(5).css('text-align', "center");
        },
        'aoColumns': [
            {
                'mDataProp': 'roomName',
                'bSortable': false
            }, {
                'mDataProp': 'roomLogo',
                'bSortable': false
            }, {
                'mDataProp': 'roomPcBg',
                'bSortable': false
            }, {
                'mDataProp': 'roomMobileBg',
                'bSortable': false
            }, {
                'mDataProp': 'forbidStatus',
                'bSortable': false,
                'mRender': function (data, type, row) {
                    if (data == 1){
                        return "已开启禁言";
                    }
                    return "已关闭禁言";
                }
            }, {
                'mDataProp': 'openRoom',
                'bSortable': false,
                'mRender': function (data, type, row) {
                    if (data == 1){
                        return "公开房间";
                    }else {
                        return "非公开房间";
                    }
                }
            }, {
                'mDataProp': 'id',
                'bSortable': false,
                'mRender': function (data, type, row) {
                    return "http://" + window.location.host + "/chat/index?roomId=" + data + "";
                }
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
        "name": 'id',
        "value": trimStr($('#id').val())
    });
    aoData.push({
        "name": 'receiverName',
        "value": trimStr($('#receiverName').val())
    });
    aoData.push({
        "name": 'status',
        "value": trimStr($('#status').val())
    });
}

function searchRoomList() {
    $("#search").on("click", function () {
        roomTable.fnDraw();
    });
}

function getAllOperation(row) {
    var btn = '';
    var edit = '<input id="edit" data-id="' + row.id + '" class="btn btn-secondary radius" type="button" value="修改">';
    var forbid = '<input id="forbid" data-id="' + row.id + '" class="btn btn-warning radius" type="button" value="开启禁言">';
    var unforbid = '<input id="unforbid" data-id="' + row.id + '" class="btn btn-default radius" type="button" value="关闭禁言">';
    var remove = '<input id="remove" data-id="' + row.id + '" class="btn btn-danger radius" type="button" value="删除">';
    if ($.inArray("sys:addRoom", permUrls) > 0) {
        btn += edit;
    }
    if (row.forbidStatus == 1 && $.inArray("sys:forbid", permUrls) > 0){
        btn += unforbid;
    }
    if (row.forbidStatus != 1 && $.inArray("sys:forbid", permUrls) > 0){
        btn += forbid;
    }
    if (row.roomType !=1 && $.inArray("sys:removeRoom", permUrls) > 0) {
        btn += remove;
    }
    return btn;
}

$("#room_table").on("click", '#edit', function () {
    var id = $(this).attr("data-id");
    layer_show('编辑房间', '/sys/addRoom?id=' + id + '', '', '800');
});

$("#room_table").on("click", '#forbid', function () {
    var id = $(this).attr("data-id");
    layer.confirm('确认要开启禁言吗？', function (index) {
        $.post(
            '/sys/forbid', { roomId: id}, function (data) {
                if (data.success) {
                    layer.msg('操作成功', { icon: 1, time: 1000 });
                    roomTable.fnDraw();
                } else {
                    layer.msg(data.errorDesc, { icon: 5, time: 2000 });
                }
            }
        );
    });
});

$("#room_table").on("click", '#unforbid', function () {
    var id = $(this).attr("data-id");
    layer.confirm('确认要关闭禁言吗？', function (index) {
        $.post(
            '/sys/forbid', { roomId: id}, function (data) {
                if (data.success) {
                    layer.msg('操作成功', { icon: 1, time: 1000 });
                    roomTable.fnDraw();
                } else {
                    layer.msg(data.errorDesc, { icon: 5, time: 2000 });
                }
            }
        );
    });
});

$("#room_table").on("click", '#remove', function () {
    var id = $(this).attr("data-id");
    layer.confirm('确认要删除吗？', function (index) {
        $.post(
            '/sys/removeRoom', { id: id}, function (data) {
                if (data.success) {
                    layer.msg('操作成功', { icon: 1, time: 1000 });
                    roomTable.fnDraw();
                } else {
                    layer.msg(data.errorDesc, { icon: 5, time: 2000 });
                }
            }
        );
    });
});