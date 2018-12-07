var tabMenuTable;
$(function () {
    tabMenuTableList();
    searchTabMenuList();
})

function tabMenuTableList() {
    tabMenuTable = $('#tabMenu_table').dataTable({
        language: datatable_zn,
        'bAutoWidth': false,
        'bFilter': false,
        'bProcessing': false,
        'bSort': false,
        'bServerSide': true,
        'fnServerParams': function (aoData) {
            getAllSearchValue(aoData);
        },
        'sAjaxSource': '/sys/ajaxTabMenu',
        "createdRow": function (row, data, index) {
            $('td', row).eq(5).css('text-align', "center");
        },
        'aoColumns': [
            {
                'mDataProp': 'remark',
                'bSortable': false
            }, {
                'mDataProp': 'sysValue',
                'bSortable': false
            }, {
                'mDataProp': 'sysType',
                'bSortable': false,
                'mRender': function (data, type, row) {
                    if (data == 1){
                        return '下注地址';
                    }
                    return '普通地址';
                }
            }, {
                'mDataProp': 'sysKey',
                'bSortable': false,
                'mRender': function (data, type, row) {
                    if (data == 'mTabMenu'){
                        return '手机端菜单';
                    }
                    if (data == 'pcTabMenu'){
                        return 'PC端菜单';
                    }
                    return "";
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
        "name": 'sysKey',
        "value": trimStr($('#sysKey').val())
    });
}

function searchTabMenuList() {
    $("#search").on("click", function () {
        tabMenuTable.fnDraw();
    });
}

function getAllOperation(row) {
    var btn = '';
    var edit = '<input id="edit" data-id="' + row.id + '" class="btn btn-secondary radius" type="button" value="修改">';
    var remove = '<input id="remove" data-id="' + row.id + '" class="btn btn-danger radius" type="button" value="删除">';
    if ($.inArray("sys:addRoom", permUrls) > 0) {
        btn += edit;
    }
    if ($.inArray("sys:removeRoom", permUrls) > 0) {
        btn += remove;
    }
    return btn;
}

$("#tabMenu_table").on("click", '#edit', function () {
    var id = $(this).attr("data-id");
    layer_show('编辑菜单', '/sys/addTabMenu?id=' + id + '', '', '510');
});

$("#tabMenu_table").on("click", '#remove', function () {
    var id = $(this).attr("data-id");
    layer.confirm('确认要删除吗？', function (index) {
        $.post(
            '/sys/removeTabMenu', { id: id}, function (data) {
                if (data.success) {
                    layer.msg('操作成功', { icon: 1, time: 1000 });
                    tabMenuTable.fnDraw();
                } else {
                    layer.msg(data.errorDesc, { icon: 5, time: 1000 });
                }
            }
        );
    });
});