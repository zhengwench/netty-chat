<#import "/sys/layout/layout.ftl" as layout>
<@layout.layout>
    <article class="page-container">
        <form action="" method="post" class="form form-horizontal" id="form-tabMenu-add">
            <input type="hidden" value="${tabMenu.id!''}" id="id" name="id">
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>菜单名：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" class="input-text" value="${tabMenu.remark!''}" placeholder="" id="remark" name="remark">
                </div>
            </div>
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>菜单ifream地址：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" class="input-text" value="${tabMenu.sysValue!''}" placeholder="" id="sysValue" name="sysValue">
                </div>
            </div>
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-3">是否下注地址：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <span class="select-box">
                        <select class="select" size="1" name="sysType">
                            <#if (tabMenu.sysType!'0'?number) == 0>
                                    <option selected="selected" value="0">普通地址</option>
                                <#else>
                                    <option value="0">普通地址</option>
                                </#if>
                                <#if (tabMenu.sysType!'0'?number) == 1>
                                    <option selected="selected" value="1">下注地址</option>
                                <#else>
                                    <option value="1">下注地址</option>
                            </#if>
                        </select>
                    </span>
                </div>
            </div>
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-3">类型：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <span class="select-box">
                        <select class="select" size="1" name="sysKey">
                            <#if (tabMenu.sysKey!'') == 'mTabMenu'>
                                    <option selected="selected" value="mTabMenu">手机菜单</option>
                            <#else>
                                    <option value="mTabMenu">手机菜单</option>
                            </#if>
                            <#if (tabMenu.sysKey!'') == 'pcTabMenu'>
                                <option selected="selected" value="pcTabMenu">PC右侧菜单</option>
                            <#else>
                                <option value="pcTabMenu">PC右侧菜单</option>
                            </#if>
                            <#if (tabMenu.sysKey!'') == 'pcTopMenu'>
                                <option selected="selected" value="pcTopMenu">PC头部菜单</option>
                            <#else>
                                <option value="pcTopMenu">PC头部菜单</option>
                            </#if>
                        </select>
                    </span>
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

            $("#form-tabMenu-add").validate({
                rules: {
                    remark: {
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
                        url: '/sys/saveTabMenu',
                        data: jQuery('#form-tabMenu-add').serialize(),
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
