<#import "/layout/layout.ftl" as layout>
<@layout.layout>
    <article class="page-container">
        <form action="" method="post" class="form form-horizontal" id="form-room-add">
            <input type="hidden" value="${room.id!''}" id="id" name="id">
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>websocketUrl：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" class="input-text" value="${room.websocketUrl!''}" placeholder="" id="websocketUrl" name="websocketUrl">
                </div>
            </div>
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>httpUrl：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" class="input-text" value="${room.httpUrl!''}" placeholder="" id="httpUrl" name="httpUrl">
                </div>
            </div>
            <div class="row cl">
                <div class="col-xs-8 col-sm-9 col-xs-offset-4 col-sm-offset-3">
                    <input class="btn btn-primary radius" type="submit" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
                </div>
            </div>
        </form>
    </article>
    <#include "/layout/footer.ftl">
    <script type="text/javascript" src="${request.getContextPath()}/lib/jquery.validation/1.14.0/jquery.validate.js"></script>
    <script type="text/javascript" src="${request.getContextPath()}/lib/jquery.validation/1.14.0/validate-methods.js"></script>
    <script type="text/javascript" src="${request.getContextPath()}/lib/jquery.validation/1.14.0/messages_zh.js"></script>
    <script type="text/javascript" src="${request.getContextPath()}/js/jquery.ui.widget.js"></script>
    <script type="text/javascript" src="${request.getContextPath()}/js/jquery.iframe-transport.js"></script>
    <script type="text/javascript" src="${request.getContextPath()}/js/jquery.fileupload.js"></script>

    <script type="text/javascript">
        $(function () {
            $('.skin-minimal input').iCheck({
                checkboxClass: 'icheckbox-blue',
                radioClass: 'iradio-blue',
                increaseArea: '20%'
            });

            $("#form-room-add").validate({
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
                        url: '/console/saveRoom',
                        data: jQuery('#form-room-add').serialize(),
                        type: "post",
                        success: function (data) {
                            if (data.success) {
                                window.parent.location.reload();
                            }
                            else {
                                layer.msg(data.errorDesc, { icon: 5, time: 1000 });
                            }
                        }
                    });
                }
            });
        });
    </script>
</@layout.layout>
