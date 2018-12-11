<#assign map = webSite/>
<#import "/sys/layout/layout.ftl" as layout>
<@layout.layout>
    <article class="page-container">
        <div id="tab-system" class="HuiTab">
            <div class="tabBar cl">
                <span>基本设置</span>
            </div>
            <div class="tabCon">
                <form action="" method="post" class="form form-horizontal" id="form-setup-add">
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>网站名称：</label>
                        <div class="formControls col-xs-8 col-sm-9">
                            <input type="text" class="input-text" value="${map["web_name"]!''}" placeholder="" id="web_name" name="web_name">
                        </div>
                    </div>
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>在线客服：</label>
                        <div class="formControls col-xs-8 col-sm-9">
                            <input type="text" class="input-text" value="${map["customer_url"]!''}" placeholder="" id="customer_url" name="customer_url">
                        </div>
                    </div>
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>PC公告：</label>
                        <div class="formControls col-xs-8 col-sm-9">
                            <textarea id="pc_notice" name="pc_notice" name="mobile_notice" class="textarea">${map["pc_notice"]!''}</textarea>
                        </div>
                    </div>
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>手机端公告：</label>
                        <div class="formControls col-xs-8 col-sm-9">
                            <textarea id="mobile_notice" name="mobile_notice" class="textarea">${map["mobile_notice"]!''}</textarea>
                        </div>
                    </div>
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">PC-logo：</label>
                        <div class="formControls col-xs-8 col-sm-9">
                            <span class="btn-upload form-group">
                                <a href="javascript:void();" class="btn btn-primary radius upload-btn"><i class="Hui-iconfont"></i> 上传图片</a>
                                <input id="pcLogoUpload" type="file" class="input-file valid" name="fileupload" data-url="/upload" multiple="">
                            </span>
                            <input type="hidden" name="pc_logo" id="pc_logo" value="${map["pc_logo"]!''}" style="width:200px">
                            <img style="width:100px;" id="pcLogoUploadSrc" src="${map["pc_logo"]!''}" />
                        </div>
                    </div>
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">手机版-logo：</label>
                        <div class="formControls col-xs-8 col-sm-9">
                            <span class="btn-upload form-group">
                                <a href="javascript:void();" class="btn btn-primary radius upload-btn"><i class="Hui-iconfont"></i> 上传图片</a>
                                <input id="mLogoUpload" type="file" class="input-file valid" name="fileupload" data-url="/upload" multiple="">
                            </span>
                            <input type="hidden" name="m_logo" id="m_logo" value="${map["m_logo"]!''}" style="width:200px">
                            <img style="width:100px;" id="mLogoUploadSrc" src="${map["m_logo"]!''}" />
                        </div>
                    </div>
                    <div class="row cl">
                        <div class="col-xs-8 col-sm-9 col-xs-offset-4 col-sm-offset-3">
                            <input class="btn btn-primary radius" type="submit" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
                        </div>
                    </div>
                </form>
            </div>
        </div>

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
            $("#tab-system").Huitab({
                index:0
            });

            $('#pcLogoUpload').fileupload({
                dataType: 'json',
                done: function (e, data) {
                    if (data.result.success) {
                        $("#pc_logo").val(data.result.result);
                        $('#pcLogoUploadSrc').attr("src", data.result.result);
                    }
                    else {
                        layer.msg(data.result.errorDesc, { icon: 5, time: 2000 });
                    }
                }
            });

            $('#mLogoUpload').fileupload({
                dataType: 'json',
                done: function (e, data) {
                    if (data.result.success) {
                        $("#m_logo").val(data.result.result);
                        $('#mLogoUploadSrc').attr("src", data.result.result);
                    }
                    else {
                        layer.msg(data.result.errorDesc, { icon: 5, time: 2000 });
                    }
                }
            });

            $('.skin-minimal input').iCheck({
                checkboxClass: 'icheckbox-blue',
                radioClass: 'iradio-blue',
                increaseArea: '20%'
            });

            $("#form-setup-add").validate({
                onkeyup: false,
                focusCleanup: true,
                success: "valid",
                submitHandler: function (form) {
                    jQuery.ajax({
                        url: '/sys/saveWebSite',
                        data: jQuery('#form-setup-add').serialize(),
                        type: "post",
                        success: function (data) {
                            if (data.success) {
                                alert('保存成功');
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
