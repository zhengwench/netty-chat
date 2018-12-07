<#import "/layout/layout.ftl" as layout>
<@layout.layout>
    <article class="page-container">
        <form action="" method="post" class="form form-horizontal" id="form-domain-add">
            <input type="hidden" value="${domain.id!''}" id="id" name="id">
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>主域名：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <span class="select-box">
                        <select class="select" size="1" name="parentId">
                            <option value="">无</option>
                            <#if parents?exists>
                                <#list parents as r>
                                    <#if (domain.parentId!'0'?number) == r.id>
                                        <option selected="selected" value="${r.id!''}">${r.domainName!''}</option>
                                    <#else>
                                        <option value="${r.id!''}">${r.domainName!''}</option>
                                    </#if>
                                </#list>
                            </#if>
                        </select>
                    </span>
                </div>
            </div>
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>域名：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" class="input-text" value="${domain.domainName!''}" placeholder="" id="domainName" name="domainName">
                </div>
            </div>
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>websocketUrl：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" value="${domain.websocketUrl!''}" class="input-text valid" id="websocketUrl" name="websocketUrl">
                </div>
            </div>
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>httpUrl：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" value="${domain.httpUrl!''}" class="input-text valid" id="httpUrl" name="httpUrl">
                </div>
            </div>
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>publicKey：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <textarea name="publicKey" class="textarea">${domain.publicKey!''}</textarea>
                </div>
            </div>
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>privateKey：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <textarea name="privateKey" class="textarea">${domain.privateKey!''}</textarea>
                </div>
            </div>
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>服务开始时间：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" onfocus="WdatePicker()" value="${(domain.startTime?string('yyyy-MM-dd HH:mm:ss'))!''}" class="input-text valid" id="startTime" name="startTime" readonly="readonly">
                </div>
            </div>
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>服务结束时间：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <input type="text" onfocus="WdatePicker()" value="${(domain.endTime?string('yyyy-MM-dd HH:mm:ss'))!''}" class="input-text valid" id="endTime" name="endTime" readonly="readonly">
                </div>
            </div>
            <div class="row cl">
                <label class="form-label col-xs-4 col-sm-3">备注：</label>
                <div class="formControls col-xs-8 col-sm-9">
                    <textarea name="remarks" class="textarea">${domain.remarks!''}</textarea>
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
    <#include "/layout/footer.ftl">
    <script type="text/javascript" src="${request.getContextPath()}/lib/jquery.validation/1.14.0/jquery.validate.js"></script>
    <script type="text/javascript" src="${request.getContextPath()}/lib/jquery.validation/1.14.0/validate-methods.js"></script>
    <script type="text/javascript" src="${request.getContextPath()}/lib/jquery.validation/1.14.0/messages_zh.js"></script>

    <script type="text/javascript">
        $(function () {
            $('.skin-minimal input').iCheck({
                checkboxClass: 'icheckbox-blue',
                radioClass: 'iradio-blue',
                increaseArea: '20%'
            });

            $("#form-domain-add").validate({
                rules: {
                    domainName: {
                        required: true,
                    },
                    websocketUrl: {
                        required: true,
                    },
                    httpUrl: {
                        required: true,
                    },
                    startTime: {
                        required: true,
                    },
                    endTime: {
                        required: true,
                    },

                },
                onkeyup: false,
                focusCleanup: true,
                success: "valid",
                submitHandler: function (form) {
                    jQuery.ajax({
                        url: '/console/saveDomainConfig',
                        data: jQuery('#form-domain-add').serialize(),
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
