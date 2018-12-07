<!--_footer 作为公共模版分离出去-->
<script type="text/javascript" src="${request.getContextPath()}/resources/sys/lib/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="${request.getContextPath()}/resources/sys/lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="${request.getContextPath()}/resources/sys/static/h-ui/js/H-ui.min.js"></script>
<script type="text/javascript" src="${request.getContextPath()}/resources/sys/static/h-ui.admin/js/H-ui.admin.js"></script>
<!--/_footer 作为公共模版分离出去-->
<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="${request.getContextPath()}/resources/sys/lib/My97DatePicker/4.8/WdatePicker.js"></script>
<script type="text/javascript" src="${request.getContextPath()}/resources/sys/lib/laypage/1.2/laypage.js"></script>
<script type="text/javascript" src="${request.getContextPath()}/resources/sys/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="${request.getContextPath()}/resources/sys/js/dataTables.tableTools.js"></script>
<script type="text/javascript">
    var datatable_zn = {
        "sProcessing": "处理中...",
        "sLengthMenu": "显示 _MENU_ 项结果",

        "sZeroRecords": "没有匹配结果",

        "sInfo": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",

        "sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",

        "sInfoFiltered": "(由 _MAX_ 项结果过滤)",

        "sInfoPostFix": "",

        "sSearch": "搜索:",

        "sUrl": "",

        "sEmptyTable": "表中数据为空",

        "sLoadingRecords": "载入中...",

        "sInfoThousands": ",",

        "oPaginate": {

            "sFirst": "首页",

            "sPrevious": "上页",

            "sNext": "下页",

            "sLast": "末页"

        },

        "oAria": {

            "sSortAscending": ": 以升序排列此列",

            "sSortDescending": ": 以降序排列此列"

        }

    };

    Date.prototype.Format = function (fmt) {
        var o = {
            "M+": this.getMonth() + 1, //月份
            "d+": this.getDate(), //日
            "h+": this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    };

    function trimStr(str) {
        if (str) {
            return str.replace(/(^\s*)|(\s*$)/g, "");
        }
        return '';
    }
</script>