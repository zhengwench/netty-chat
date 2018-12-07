<#import "/sys/layout/layout.ftl" as layout>
<@layout.layout>
    <nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页
        <span class="c-gray en">&gt;</span> 红包管理
        <span class="c-gray en">&gt;</span> 红包兑换列表
        <a class="btn btn-success radius r" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新"><i class="Hui-iconfont">&#xe68f;</i></a>
    </nav>
    <div class="page-container">
        <div class="text-c">
            兑换id:<input type="text" class="input-text" style="width:250px" placeholder="" id="id" name="id">
            红包id:<input type="text" class="input-text" style="width:250px" placeholder="" id="redbagId" name="redbagId">
            领取人:<input type="text" class="input-text" style="width:250px" placeholder="" id="receiverName" name="receiverName">
            领取状态:
            <select id="status" class="input-text" style="width:250px">
                <option value="0">全部</option>
                <option value="1">未兑换</option>
                <option value="2">已兑换</option>
            </select>
            <button type="submit" class="btn btn-success radius" id="search" name=""><i class="Hui-iconfont">&#xe665;</i> 搜索</button>
        </div>
        <div class="mt-20">
            <table id="receiveRedbag_table" class="table table-border table-bordered table-hover table-bg table-sort">
                <thead>
                <tr class="text-c">
                    <th>兑换id</th>
                    <th>红包发放人</th>
                    <th>红包领取人</th>
                    <th>金额</th>
                    <th>状态</th>
                    <th>备注</th>
                    <th>领取时间</th>
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
    <script type="text/javascript" src="${request.getContextPath()}/resources/sys/js/receiveRedbag.js"></script>
</@layout.layout>