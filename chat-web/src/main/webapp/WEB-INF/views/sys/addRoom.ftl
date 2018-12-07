<#import "/sys/layout/layout.ftl" as layout>
<@layout.layout>
    <article class="page-container">
        <form action="" method="post" class="form form-horizontal" id="form-room-add">
            <input type="hidden" value="${room.id!''}" id="id" name="id">
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>房间名：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" class="input-text" value="${room.roomName!''}" placeholder="" id="roomName" name="roomName">
                </div>
            </div>
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-3">房间类型：</label>
                <div class="formControls col-xs-8 col-sm-9">
                <span class="select-box">
                    <select class="select" size="1" name="openRoom">
                        <#if (room.openRoom!'0'?number) == 1>
                            <option selected="selected" value="1">开放房间</option>
                            <#else>
                            <option value="1">开放房间</option>
                        </#if>
                        <#if (room.openRoom!'0'?number) == 2>
                            <option selected="selected" value="2">非开放房间</option>
                        <#else>
                            <option value="2">非开放房间</option>
                        </#if>
                    </select>
                </span>
                </div>
            </div>
            <#--<div class="row cl">-->
                <#--<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>房间logo：</label>-->
                <#--<div class="formControls col-xs-8 col-sm-9">-->
                    <#--<span class="btn-upload form-group">-->
                        <#--<a href="javascript:void();" class="btn btn-primary radius upload-btn"><i class="Hui-iconfont"></i> 上传图片</a>-->
                        <#--<input id="roomLogoUpload" type="file" class="input-file valid" name="fileupload" data-url="/upload" multiple="">-->
                    <#--</span>-->
                    <#--<input type="hidden" name="roomLogo" id="roomLogo" value="${room.roomLogo!''}" style="width:200px">-->
                    <#--<img id="roomLogoUploadSrc" src="${room.roomLogo!''}" />-->
                <#--</div>-->
            <#--</div>-->
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>PC房间聊天背景：</label>
                <div class="formControls col-xs-8 col-sm-9">
                <span class="btn-upload form-group">
                    <a href="javascript:void();" class="btn btn-primary radius upload-btn"><i class="Hui-iconfont"></i> 上传图片</a>
                    <input id="roomPcBgUpload" type="file" class="input-file valid" name="fileupload" data-url="/upload" multiple="">
                </span>
                    <input type="hidden" name="roomPcBg" id="roomPcBg" value="${room.roomPcBg!''}" style="width:200px">
                    <img style="width:100px;" id="roomPcBgUploadSrc" src="${room.roomPcBg!''}" />
                </div>
            </div>
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>手机房间聊天背景：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <span class="btn-upload form-group">
                        <a href="javascript:void();" class="btn btn-primary radius upload-btn"><i class="Hui-iconfont"></i> 上传图片</a>
                        <input id="roomMobileBgUpload" type="file" class="input-file valid" name="fileupload" data-url="/upload" multiple="">
                    </span>
                    <input type="hidden" name="roomMobileBg" id="roomMobileBg" value="${room.roomMobileBg!''}" style="width:200px">
                    <img style="width:100px;" id="roomMobileBgUploadSrc" src="${room.roomMobileBg!''}" />
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
            $('#roomLogoUpload').fileupload({
                dataType: 'json',
                done: function (e, data) {
                    if (data.result.success) {
                        $("#roomLogo").val(data.result.result);
                        $('#roomLogoUploadSrc').attr("src", data.result.result);
                    }
                    else {
                        layer.msg(data.result.errorDesc, { icon: 5, time: 1000 });
                    }
                }
            });

            $('#roomPcBgUpload').fileupload({
                dataType: 'json',
                done: function (e, data) {
                    if (data.result.success) {
                        $("#roomPcBg").val(data.result.result);
                        $('#roomPcBgUploadSrc').attr("src", data.result.result);
                    }
                    else {
                        layer.msg(data.result.errorDesc, { icon: 5, time: 1000 });
                    }
                }
            });

            $('#roomMobileBgUpload').fileupload({
                dataType: 'json',
                done: function (e, data) {
                    if (data.result.success) {
                        $("#roomMobileBg").val(data.result.result);
                        $('#roomMobileBgUploadSrc').attr("src", data.result.result);
                    }
                    else {
                        layer.msg(data.result.errorDesc, { icon: 5, time: 1000 });
                    }
                }
            });

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
                        url: '/sys/saveRoom',
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
