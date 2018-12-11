<#import "/sys/layout/layout.ftl" as layout>
<@layout.layout>
    <article class="page-container">
        <form action="" method="post" class="form form-horizontal" id="form-exchangeRedbag-add">
            <input type="hidden" value="${receiveId!''}" id="receiveId" name="receiveId">
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-3">备注：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <textarea name="remark" class="textarea"></textarea>
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

    <script type="text/javascript">
        $(function () {
            $('.skin-minimal input').iCheck({
                checkboxClass: 'icheckbox-blue',
                radioClass: 'iradio-blue',
                increaseArea: '20%'
            });

            $("#form-exchangeRedbag-add").validate({
                rules: {
                },
                onkeyup: false,
                focusCleanup: true,
                success: "valid",
                submitHandler: function (form) {
                    jQuery.ajax({
                        url: '/sys/postExchangeRedbag',
                        data: jQuery('#form-exchangeRedbag-add').serialize(),
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
