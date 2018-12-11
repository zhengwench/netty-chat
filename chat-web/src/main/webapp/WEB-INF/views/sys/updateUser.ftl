<#import "/sys/layout/layout.ftl" as layout>
<@layout.layout>
    <article class="page-container">
        <form action="" method="post" class="form form-horizontal" id="form-user-add">
            <input type="hidden" value="${user.id!''}" id="id" name="id">
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>用户名：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" class="input-text" value="${user.userName!''}" placeholder="" id="userName" name="userName">
                </div>
            </div>
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-3">房间：</label>
                <div class="formControls col-xs-8 col-sm-9">
                <span class="select-box">
                    <select class="select" size="1" name="roomId">
                            <#if roomList?exists>
                                <#list roomList as r>
                                    <#if (user.roomId!'0'?number) == r.id>
                                        <option selected="selected" value="${r.id!''}">${r.roomName!''}</option>
                                    <#else>
                                        <option value="${r.id!''}">${r.roomName!''}</option>
                                    </#if>
                                </#list>
                            </#if>
                    </select>
                </span>
                </div>
            </div>
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-3">备注：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <textarea name="remark" class="textarea">${user.remark!''}</textarea>
                    <p class="textarea-numberbar"><em class="textarea-length">0</em>/100</p>
                </div>
            </div>
            <div class="row cl">
                <div class="col-xs-8 col-sm-9 col-xs-offset-4 col-sm-offset-3">
                    <input class="btn btn-primary radius" type="submit" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
                </div>
            </div>
        </form>
    </article>
    <#include "/sys/layout/footer.ftl">
    <script type="text/javascript" src="${request.getContextPath()}/resources/sys/lib/jquery.validation/1.14.0/jquery.validate.js"></script>
    <script type="text/javascript" src="${request.getContextPath()}/resources/sys/lib/jquery.validation/1.14.0/validate-methods.js"></script>
    <script type="text/javascript" src="${request.getContextPath()}/resources/sys/lib/jquery.validation/1.14.0/messages_zh.js"></script>
    <script type="text/javascript" src="${request.getContextPath()}/resources/sys/js/jquery.ui.widget.js"></script>
    <script type="text/javascript" src="${request.getContextPath()}/resources/sys/js/jquery.iframe-transport.js"></script>
    <script type="text/javascript" src="${request.getContextPath()}/resources/sys/js/jquery.fileupload.js"></script>

    <script type="text/javascript">
        $(function () {
            $('.skin-minimal input').iCheck({
                checkboxClass: 'icheckbox-blue',
                radioClass: 'iradio-blue',
                increaseArea: '20%'
            });

            $('#iconUpload').fileupload({
                dataType: 'json',
                done: function (e, data) {
                    if (data.result.success) {
                        $("#icon").val(data.result.result);
                        $('#iconUploadSrc').attr("src", data.result.result);
                    }
                    else {
                        layer.msg(data.result.errorDesc, { icon: 5, time: 2000 });
                    }
                }
            });

            $("#form-user-add").validate({
                rules: {
                    username: {
                        required: true,
                        minlength: 2,
                        maxlength: 16
                    },
                    sex: {
                        required: true,
                    },
                    mobile: {
                        required: true,
                        isMobile: true,
                    },
                    email: {
                        required: true,
                        email: true,
                    },
                    uploadfile: {
                        required: true,
                    },

                },
                onkeyup: false,
                focusCleanup: true,
                success: "valid",
                submitHandler: function (form) {
                    jQuery.ajax({
                        url: '/sys/saveUser',
                        data: jQuery('#form-user-add').serialize(),
                        type: "post",
                        success: function (data) {
                            if (data.success) {
                                window.parent.location.reload();
                            }
                            else {
                                layer.msg(data.errorDesc, { icon: 5, time: 2000 });
                            }
                        }
                    });
                }
            });
        });
    </script>
</@layout.layout>
