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
    <nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 会员管理 <span class="c-gray en">&gt;</span> 角色管理 <a class="btn btn-success radius r" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新"><i class="Hui-iconfont">&#xe68f;</i></a></nav>
    <div class="page-container">
    <div class="cl pd-5 bg-1 bk-gray">
        <span class="l">
            <#if permArray?exists>
                <#list permArray as perm>
                    <#if perm=="sys:addRole">
                        <a class="btn btn-primary radius" href="javascript:;" onclick="layer_show('添加角色','/sys/addRole','800')">
                            <i class="Hui-iconfont">&#xe600;</i> 添加角色
                        </a>
                    </#if>
                </#list>
            </#if>
        </span>
    </div>
    <table id="role_table" class="table table-border table-bordered table-hover table-bg">
        <thead>
        <tr>
            <th scope="col" colspan="6">角色管理</th>
        </tr>
        <tr class="text-c">
            <th>角色名</th>
            <th>角色图标</th>
            <th>创建时间</th>
            <th>描述</th>
            <th>操作</th>
        </tr>
        </thead>
    </table>
</div>
    <#include "/sys/layout/footer.ftl">
    <script type="text/javascript">
        var permUrls = [];
            <#if permArray?exists>
                <#list permArray as item>
                    permUrls.push("${item?js_string}");
                </#list>
            </#if>
    </script>
    <script type="text/javascript" src="${request.getContextPath()}/resources/sys/js/role.js"></script>
</@layout.layout>