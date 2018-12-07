<#import "/sys/layout/layout.ftl" as layout>
<@layout.layout>
    <nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 机器人管理 <span class="c-gray en">&gt;</span> 机器人列表 <a class="btn btn-success radius r" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新"><i class="Hui-iconfont">&#xe68f;</i></a></nav>
    <div class="page-container">
        <div class="text-c">
            房间名
            <select id="roomName" class="input-text" style="width:250px">
                <option value="0">全部</option>
                <#if rooms?exists>
                    <#list rooms as r>
                        <option value="${r.id}">${r.roomName}</option>
                    </#list>
                </#if>
            </select>
            <button type="submit" class="btn btn-success radius" id="search" name=""><i class="Hui-iconfont">&#xe665;</i> 搜索</button>
        </div>
        <div class="cl pd-5 bg-1 bk-gray mt-20">
        <span class="l">
            <#if permArray?exists>
                <#list permArray as perm>
                    <#if perm=="sys:addRobot">
                        <a href="javascript:;" onclick="layer_show('新增用户','/sys/addRobot','','800');" class="btn btn-primary radius">
                            <i class="Hui-iconfont">&#xe600;</i> 批量添加机器人
                        </a>
                    </#if>
                </#list>
            </#if>
            <#if permArray?exists>
                <#list permArray as perm>
                    <#if perm=="sys:deleteBatchRobot">
                        <a href="javascript:;" onclick="deleteBatch();" class="btn btn-danger radius">
                            <i class="Hui-iconfont">&#xe600;</i> 批量删除机器人
                        </a>
                    </#if>
                </#list>
            </#if>
        </span>
        </div>
        <div class="mt-20">
            <table id="userRobot_table" class="table table-border table-bordered table-hover table-bg table-sort">
                <thead>
                    <tr class="text-c">
                        <th width="25"><input type="checkbox" name="" value=""></th>
                        <th>用户名</th>
                        <th>所在房间</th>
                        <th>创建时间</th>
                    </tr>
                </thead>
            </table>
        </div>
    </div>
    <#include "/sys/layout/footer.ftl">
    <script type="text/javascript" src="${request.getContextPath()}/resources/sys/js/userRobot.js"></script>
</@layout.layout>