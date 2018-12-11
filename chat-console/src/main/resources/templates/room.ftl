<#import "/layout/layout.ftl" as layout>
<@layout.layout>
    <nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页
        <span class="c-gray en">&gt;</span> 系统管理
        <span class="c-gray en">&gt;</span> 聊天室服务房间管理
        <a class="btn btn-success radius r" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新">
            <i class="Hui-iconfont">&#xe68f;</i>
        </a>
    </nav>
    <div class="page-container">
        <div class="text-c">
            域名:<input type="text" class="input-text" style="width:250px" placeholder="" id="domain" name="domain">
            <button type="submit" class="btn btn-success radius" id="search" name=""><i class="Hui-iconfont">&#xe665;</i> 搜索</button>
        </div>
        <table id="room_table" class="table table-border table-bordered table-hover table-bg">
            <thead>
            <tr class="text-c">
                <th>房间id</th>
                <th>房间名</th>
                <th>websocketUrl</th>
                <th>httpUrl</th>
                <th>操作</th>
            </tr>
            </thead>
        </table>
    </div>
    <#include "/layout/footer.ftl">
    <script type="text/javascript" src="${request.getContextPath()}/js/room.js"></script>
</@layout.layout>
