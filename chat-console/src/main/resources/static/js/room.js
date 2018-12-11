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
        'sAjaxSource': '/console/ajaxRoom',
        "createdRow": function (row, data, index) {
            $('td', row).eq(5).css('text-align', "center");
        },
        'aoColumns': [
            {
                'mDataProp': 'id',
                'bSortable': false
            }, {
                'mDataProp': 'roomName',
                'bSortable': false
            }, {
                'mDataProp': 'websocketUrl',
                'bSortable': false
            }, {
                'mDataProp': 'httpUrl',
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
        "name": 'domain',
        "value": trimStr($('#domain').val())
    });
}

function searchRoomList() {
    $("#search").on("click", function () {
        roomTable.fnDraw();
    });
}

function getAllOperation(row) {
    var edit = '<input id="edit" data-id="' + row.id + '" class="btn btn-secondary radius" type="button" value="修改">';
    return edit;
}

$("#room_table").on("click", '#edit', function () {
    var id = $(this).attr("data-id");
    layer_show('编辑用户', '/console/editRoom?id=' + id + '', '', '800');
});