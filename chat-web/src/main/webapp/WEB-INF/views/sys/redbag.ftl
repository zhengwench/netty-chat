<#import "/sys/layout/layout.ftl" as layout>
<@layout.layout>
    <nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页
        <span class="c-gray en">&gt;</span> 红包管理
        <span class="c-gray en">&gt;</span> 红包列表
        <a class="btn btn-success radius r" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新"><i class="Hui-iconfont">&#xe68f;</i></a>
    </nav>
    <div class="page-container">
        <div class="text-c">
            发放人:<input type="text" class="input-text" style="width:250px" placeholder="" id="sendMemberName" name="sendMemberName">
            查询开始时间:<input type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', maxDate:'#F{$dp.$D(\'endTime\')||\'%y-%M-%d %H-%m-%s\'}' })" value="" class="input-text Wdate" style="width:160px;" id="startTime" name="startTime" readonly="readonly">
            查询结束时间:<input type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', minDate:'#F{$dp.$D(\'startTime\')}',maxDate:'%y-%M-%d %H-%m-%s' })" value="" class="input-text Wdate" style="width:160px;" id="endTime" name="endTime" readonly="readonly">
            <button type="submit" class="btn btn-success radius" id="search" name=""><i class="Hui-iconfont">&#xe665;</i> 搜索</button>
        </div>
        <div class="cl pd-5 bg-1 bk-gray mt-20">
            <span class="l">
                <#if permArray?exists>
                    <#list permArray as perm>
                        <#if perm=="sys:addRedbag">
                            <a href="javascript:;" onclick="layer_show('发放红包','/sys/addRedbag','','800');" class="btn btn-primary radius">
                                <i class="Hui-iconfont">&#xe600;</i> 发放红包
                            </a>
                        </#if>
                    </#list>
                </#if>
            </span>
        </div>
        <div class="mt-20">
            <table id="redbag_table" class="table table-border table-bordered table-hover table-bg table-sort">
                <thead>
                <tr class="text-c">
                    <th>红包id</th>
                    <th>发放人</th>
                    <th>金额</th>
                    <th>个数</th>
                    <th>发放时间</th>
                    <th>备注</th>
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
    <script type="text/javascript" src="${request.getContextPath()}/resources/sys/js/redbag.js"></script>
</@layout.layout>