var receiveRedbagTable;
$(function () {
    receiveRedbagTableList();
    searchReceiveRedbagList();
})

function receiveRedbagTableList() {
    receiveRedbagTable = $('#receiveRedbag_table').dataTable({
        language: datatable_zn,
        'bAutoWidth': false,
        'bFilter': false,
        'bProcessing': false,
        'bSort': false,
        'bServerSide': true,
        'fnServerParams': function (aoData) {
            getAllSearchValue(aoData);
        },
        'sAjaxSource': '/sys/ajaxReceiveRedbag',
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
                'mDataProp': 'receiveUserName',
                'bSortable': false
            }, {
                'mDataProp': 'amount',
                'bSortable': false
            }, {
                'mDataProp': 'status',
                'bSortable': false,
                'mRender': function (data, type, row) {
                    if (data == 2){
                        return "已兑换";
                    }
                    return "未兑换";
                }
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
        "name": 'id',
        "value": trimStr($('#id').val())
    });
    aoData.push({
        "name": 'redbagId',
        "value": trimStr($('#redbagId').val())
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

function searchReceiveRedbagList() {
    $("#search").on("click", function () {
        receiveRedbagTable.fnDraw();
    });
}

function getAllOperation(row) {
    var btn = '';
    var exchange = '<input id="exchange" data-id="' + row.id + '" class="btn btn-secondary radius" type="button" value="兑换">';
    if (row.status == 1 && $.inArray("sys:exchangeRedbag", permUrls) > 0) {
        btn += exchange;
    }
    return btn;
}

$("#receiveRedbag_table").on("click", '#exchange', function () {
    var id = $(this).attr("data-id");
    layer_show('兑换红包', '/sys/exchangeRedbag?receiveId=' + id + '', '', '510');
});