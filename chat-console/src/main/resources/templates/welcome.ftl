<#import "/layout/layout.ftl" as layout>
<@layout.layout>
    <div class="page-container">
        <p class="f-20 text-success">欢迎使用聊天室控制台</p>
        <table class="table table-border table-bordered table-bg mt-20">
            <tbody>
                <tr>
                    <th width="30%">清除聊天室历史消息</th>
                    <td>
                        <input onclick="cleanHistory();" data-id="' + row.id + '" class="btn btn-warning radius" type="button" value="清除历史消息">
                    </td>
                </tr>
                <tr>
                    <th width="30%">清除聊天室房间人数统计信息</th>
                    <td>
                        <input onclick="cleanChatRoom();" data-id="' + row.id + '" class="btn btn-warning radius" type="button" value="清除聊天房间人数信息">
                    </td>
                </tr>
                <#--<tr>-->
                    <#--<th width="30%">清除domain服务缓存</th>-->
                    <#--<td>-->
                        <#--<input id="cleanDomain();" data-id="' + row.id + '" class="btn btn-warning radius" type="button" value="清除domain服务缓存">-->
                    <#--</td>-->
                <#--</tr>-->
            </tbody>
        </table>
    </div>
    <div class="modal fade" id="clean_history_modal" tabindex="-1" role="dialog" aria-labelledby="gridSystemModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header no-padding">
                    <div class="table-header">
                        <button aria-hidden="true" data-dismiss="modal" class="close" type="button">
                            <span class="white">×</span>
                        </button>
                        <span class="modalFormTitle">清除历史消息</span>
                    </div>
                </div>
                <div class="modal-body" style="text-align:center">
                    <form class="form form-horizontal" id="form-cleanhistory">
                        <div class="row cl">
                            <label class="form-label col-xs-4 col-sm-2">domain：</label>
                            <div class="formControls col-xs-8 col-sm-9">
                                <input type="text" class="input-text" value="" id="historyDomain" name="historyDomain">
                            </div>
                        </div>
                        <div class="row cl">
                            <label class="form-label col-xs-4 col-sm-2">roomId：</label>
                            <div class="formControls col-xs-8 col-sm-9">
                                <input type="text" class="input-text" value="" id="historyRoomId" name="historyRoomId">
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" onclick="postCleanHistory();" class="btn btn-primary">提交</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade" id="clean_chatroom_modal" tabindex="-1" role="dialog" aria-labelledby="gridSystemModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header no-padding">
                    <div class="table-header">
                        <button aria-hidden="true" data-dismiss="modal" class="close" type="button">
                            <span class="white">×</span>
                        </button>
                        <span class="modalFormTitle">清除聊天房间人数信息</span>
                    </div>
                </div>
                <div class="modal-body" style="text-align:center">
                    <form class="form form-horizontal" id="form-cleanChatroom">
                        <div class="row cl">
                            <label class="form-label col-xs-4 col-sm-2">domain：</label>
                            <div class="formControls col-xs-8 col-sm-9">
                                <input type="text" class="input-text" value="" id="chatroomDomain" name="chatroomDomain">
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" onclick="postCleanChatRoom();" class="btn btn-primary">提交</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                </div>
            </div>
        </div>
    </div>
    <#include "/layout/footer.ftl">
    <script type="text/javascript">
        function cleanHistory() {
            $('#historyDomain').val('');
            $('#historyRoomId').val('');
            $('#clean_history_modal').modal('show');
        }
        
        function postCleanHistory() {
            var domain = $('#historyDomain').val();
            if (!trimStr(domain)) {
                alert('domain为空');
                return;
            }
            var roomId = $('#historyRoomId').val();
            if (!trimStr(roomId)) {
                alert('roomId为空');
                return;
            }
            jQuery.ajax({
                url: '/console/cleanHistory',
                data: jQuery('#form-cleanhistory').serialize(),
                type: "post",
                success: function (data) {
                    if (data.success) {
                        $('#clean_history_modal').modal('hide');
                    } else {
                        layer.msg(data.errorDesc, { icon: 5, time: 1000 });
                    }
                }
            });
        }

        function cleanChatRoom() {
            $('#chatroomDomain').val('');
            $('#clean_chatroom_modal').modal('show');
        }

        function postCleanChatRoom() {
            var domain = $('#chatroomDomain').val();
            if (!trimStr(domain)) {
                alert('domain为空');
                return;
            }
            jQuery.ajax({
                url: '/console/cleanChatRoom',
                data: jQuery('#form-cleanChatroom').serialize(),
                type: "post",
                success: function (data) {
                    if (data.success) {
                        $('#clean_chatroom_modal').modal('hide');
                    } else {
                        layer.msg(data.errorDesc, { icon: 5, time: 1000 });
                    }
                }
            });
        }
    </script>
</@layout.layout>

