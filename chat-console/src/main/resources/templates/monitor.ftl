<#import "/layout/layout.ftl" as layout>
<@layout.layout>
    <nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页
        <span class="c-gray en">&gt;</span> 系统管理
        <span class="c-gray en">&gt;</span> 聊天室服务管理
        <a class="btn btn-success radius r" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新">
            <i class="Hui-iconfont">&#xe68f;</i>
        </a>
    </nav>
    <div class="page-container">
        <div class="text-c">
            服务器ip:<input type="text" class="input-text" style="width:250px" placeholder="输入服务域名" id="serverIp" name="serverIp">
            服务器域名:<input type="text" class="input-text" style="width:250px" placeholder="" id="serverDomain" name="serverDomain">
            <button type="submit" class="btn btn-success radius" id="search" name=""><i class="Hui-iconfont">&#xe665;</i> 搜索</button>
        </div>
        <div class="cl pd-5 bg-1 bk-gray mt-20">
            <span class="l">
                <a href="javascript:;" onclick="layer_show('新增服务器','/console/addMonitor','','800');" class="btn btn-primary radius">
                    <i class="Hui-iconfont">&#xe600;</i> 新增服务器
                </a>
            </span>
        </div>
        <table id="monitor_table" class="table table-border table-bordered table-hover table-bg">
            <thead>
            <tr class="text-c">
                <th>服务器ip</th>
                <th>服务器域名</th>
                <th>服务器http端口</th>
                <th>服务器websocket端口</th>
                <th>备注</th>
                <th>操作</th>
            </tr>
            </thead>
        </table>
    </div>
    <#include "/layout/footer.ftl">
    <script type="text/javascript" src="${request.getContextPath()}/js/monitor.js"></script>
</@layout.layout>
