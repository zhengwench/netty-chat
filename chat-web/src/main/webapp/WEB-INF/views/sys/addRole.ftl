<#import "/sys/layout/layout.ftl" as layout>
<@layout.layout>
    <style>
        .user-icon-admin {
            height: 30px;
            width: 30px;
            background: url(${request.getContextPath()}/resources/chat/pc/images/user_level_icon.png) center top no-repeat;
            background-size: 100% auto;
        }

        .user-icon-plan {
            height: 30px;
            width: 30px;
            background: url(${request.getContextPath()}/resources/chat/pc/images/user_level_icon.png) center -35px no-repeat;
            background-size: 100% auto;
        }

        .user-icon-vip {
            height: 30px;
            width: 30px;
            background: url(${request.getContextPath()}/resources/chat/pc/images/user_level_icon.png) center -69px no-repeat;
            background-size: 100% auto;
        }

        .user-icon-nomal {
            height: 30px;
            width: 30px;
            background: url(${request.getContextPath()}/resources/chat/pc/images/user_level_icon.png) center bottom no-repeat;
            background-size: 100% auto;
        }
    </style>
    <article class="page-container">
        <form action="" method="post" class="form form-horizontal" id="form-role-add">
            <input type="hidden" value="${role.id!''}" id="id" name="id">
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>角色名称：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" class="input-text" value="${role.roleName!''}" placeholder="" id="roleName" name="roleName">
                </div>
            </div>
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>角色icon：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <span class="btn-upload form-group">
                        <a href="javascript:void();" class="btn btn-primary radius upload-btn"><i class="Hui-iconfont"></i> 上传图片</a>
                        <input id="roleIconUpload" type="file" class="input-file valid" name="fileupload" data-url="/upload" multiple="">
                    </span>
                    <input type="hidden" name="roleIcon" id="roleIcon" value="${role.roleIcon!''}" style="width:200px">
                    <#if role.roleIcon?? && role.roleIcon?contains("http")>
                        <img style="width:100px;" id="roleIconUploadSrc" src="${role.roleIcon!''}" />
                        <#else>
                        <img style="width:100px;" id="roleIconUploadSrc" class="${role.roleIcon!''}" />
                    </#if>
                </div>
            </div>
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-3">权限：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <#if perms?exists>
                        <#list perms as f>
                            <dl class="permission-list">
                                <dt>
                                    <label>
                                        <#if idList?seq_contains(f.id)>
                                            <input type="checkbox" checked="checked" value="${f.id!''}" name="user-Character-0" id="user-Character-${f.id!''}">
                                            <#else>
                                            <input type="checkbox" value="${f.id!''}" name="user-Character-0" id="user-Character-${f.id!''}">
                                        </#if>
                                        ${f.name!''}
                                    </label>
                                </dt>
                                <dd>
                                    <#if f.list?exists>
                                        <#list f.list as s>
                                        <dl class="cl permission-list2">
                                            <dt>
                                                <label class="">
                                                    <#if idList?seq_contains(s.id)>
                                                        <input type="checkbox" checked="checked" value="${s.id!''}" name="user-Character-0-0" id="user-Character-0-0">
                                                        <#else>
                                                        <input type="checkbox" value="${s.id!''}" name="user-Character-0-0" id="user-Character-0-0">
                                                    </#if>
                                                    ${s.name!''}
                                                </label>
                                            </dt>
                                            <dd>
                                                <#if s.list?exists>
                                                    <#list s.list as t>
                                                        <label class="">
                                                            <#if idList?seq_contains(t.id)>
                                                                <input type="checkbox" checked="checked" value="${t.id!''}" name="user-Character-0-0-0" id="user-Character-0-0-0">
                                                                <#else>
                                                                <input type="checkbox" value="${t.id!''}" name="user-Character-0-0-0" id="user-Character-0-0-0">
                                                            </#if>
                                                            ${t.name!''}
                                                        </label>
                                                    </#list>
                                                </#if>
                                            </dd>
                                        </dl>
                                        </#list>
                                    </#if>
                                </dd>
                            </dl>
                        </#list>
                    </#if>
                </div>
            </div>
            <input type="hidden" id="permIds" name="permIds" />
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-3">备注：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" class="input-text" value="${role.remark!''}" placeholder="" id="remark" name="remark">
                </div>
            </div>
            <div class="row cl">
                <div class="col-xs-8 col-sm-9 col-xs-offset-4 col-sm-offset-3">
                    <button type="submit" class="btn btn-success radius" id="admin-role-save" name="admin-role-save"><i class="icon-ok"></i> 确定</button>
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
            $(".permission-list dt input:checkbox").click(function () {
                $(this).closest("dl").find("dd input:checkbox").prop("checked", $(this).prop("checked"));

                var l2 = $(this).parents(".permission-list").find(".permission-list2 dd").find("input:checked").length;
                if ($(this).prop("checked")){
                    $(this).parents(".permission-list").find("dt").first().find("input:checkbox").prop("checked", $(this).prop("checked"));
                }else {
                    if (l2 == 0){
                        $(this).parents(".permission-list").find("dt").first().find("input:checkbox").prop("checked", $(this).prop("checked"));
                    }
                }
            });
            $(".permission-list2 dd input:checkbox").click(function () {
                var l = $(this).parent().parent().find("input:checked").length;
                var l2 = $(this).parents(".permission-list").find(".permission-list2 dd").find("input:checked").length;
                if ($(this).prop("checked")) {
                    $(this).closest("dl").find("dt input:checkbox").prop("checked", true);
                    $(this).parents(".permission-list").find("dt").first().find("input:checkbox").prop("checked", true);
                }
                else {
                    if (l == 0) {
                        $(this).closest("dl").find("dt input:checkbox").prop("checked", false);
                    }
                    if (l2 == 0) {
                        $(this).parents(".permission-list").find("dt").first().find("input:checkbox").prop("checked", false);
                    }
                }
            });

            $('#roleIconUpload').fileupload({
                dataType: 'json',
                done: function (e, data) {
                    if (data.result.success) {
                        $("#roleIcon").val(data.result.result);
                        $('#roleIconUploadSrc').attr("src", data.result.result);
                    }
                    else {
                        layer.msg(data.result.errorDesc, { icon: 5, time: 1000 });
                    }
                }
            });

            $("#form-role-add").validate({
                rules: {
                    roleName: {
                        required: true,
                    },
                },
                onkeyup: false,
                focusCleanup: true,
                success: "valid",
                submitHandler: function (form) {
                    var permids = [];
                    $("input[type=checkbox]").each(function () {
                        if (this.checked) {
                            permids.push($(this).val());
                        }
                    })
                    $('#permIds').val(permids.join(','));
                    jQuery.ajax({
                        url: '/sys/saveRole',
                        data: jQuery('#form-role-add').serialize(),
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

                    //$(form).ajaxSubmit();
                    //var index = parent.layer.getFrameIndex(window.name);
                    //parent.layer.close(index);
                }
            });
        });
    </script>
</@layout.layout>
