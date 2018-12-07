<#import "/sys/layout/layout.ftl" as layout>
<@layout.layout>
    <div class="page-container">
        <p class="f-20 text-success">欢迎使用聊天室</p>
        <p><span class="f-16 text-success c-error">服务到期时间：</span>${(endtime?string('yyyy-MM-dd HH:mm:ss'))!''}  </p>
        <p><span class="f-16 text-success c-error">剩余天数：</span>${remainDays!''}</p>
        <table class="table table-border table-bordered table-bg mt-20">
            <thead>
            <tr>
                <th colspan="2" scope="col">聊天室实时在线人数统计</th>
            </tr>
            </thead>
            <tbody id="resultMap">
            </tbody>
        </table>
    </div>
    <#include "/sys/layout/footer.ftl">

    <script type="text/javascript">
        $(function () {
            getOnline();
            monitor();
        });

        function getOnline() {
            jQuery.ajax({
                url: '/sys/monitorOnline',
                type: "post",
                success: function (data) {
                    if (data.success) {
                        if (data.list) {
                            var html = "";
                            html += '<tr>';
                            html += '<th width="30%">房间名</th>';
                            html += '<td>在线人数</td>';
                            html += '</tr>';
                            for (var i = 0; i < data.list.length; i++) {
                                html += '<tr>';
                                html += '<th width="30%">' + data.list[i].roomName + '</th>';
                                html += '<td>' + data.list[i].onlineCount + '</td>';
                                html += '</tr>';
                            }
                        }
                        $("#resultMap").html(html);
                    }
                    else {
                        layer.msg(data.errorDesc, {icon: 5, time: 1000});
                    }
                }
            });
        }

        function monitor() {
            setTimeout(function () {
                getOnline();
                monitor();
            }, 10000);
        }
    </script>
</@layout.layout>

