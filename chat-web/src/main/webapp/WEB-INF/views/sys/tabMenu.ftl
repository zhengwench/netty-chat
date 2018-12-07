<#import "/sys/layout/layout.ftl" as layout>
<@layout.layout>
    <nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页
        <span class="c-gray en">&gt;</span> 聊天室管理
        <span class="c-gray en">&gt;</span> tab菜单管理
        <a class="btn btn-success radius r" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新"><i class="Hui-iconfont">&#xe68f;</i></a>
    </nav>
    <div class="page-container">
        <div class="text-c">
            tab菜单类型:
            <select id="sysKey" class="input-text" style="width:250px">
                <option value="">全部</option>
                <option value="mTabMenu">手机菜单</option>
                <option value="pcTabMenu">pc菜单</option>
            </select>
            <button type="submit" class="btn btn-success radius" id="search" name=""><i class="Hui-iconfont">&#xe665;</i> 搜索</button>
        </div>
        <div class="cl pd-5 bg-1 bk-gray mt-20">
            <span class="l">
                <#if permArray?exists>
                    <#list permArray as perm>
                        <#if perm=="sys:addTabMenu">
                            <a href="javascript:;" onclick="layer_show('新增tab菜单','/sys/addTabMenu','','800');" class="btn btn-primary radius">
                                <i class="Hui-iconfont">&#xe600;</i> 新增tab菜单
                            </a>
                        </#if>
                    </#list>
                </#if>
            </span>
        </div>
        <div class="mt-20">
            <table id="tabMenu_table" class="table table-border table-bordered table-hover table-bg table-sort">
                <thead>
                <tr class="text-c">
                    <th>tab名</th>
                    <th>tab地址</th>
                    <th>是否下注地址</th>
                    <th>菜单类型</th>
                    <th>操作</th>
                </tr>
                </thead>
            </table>
        </div>
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
    <script type="text/javascript" src="${request.getContextPath()}/resources/sys/js/tabMenu.js"></script>
</@layout.layout>