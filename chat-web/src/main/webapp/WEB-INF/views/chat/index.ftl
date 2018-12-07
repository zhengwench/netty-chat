<#assign map = websetmap/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${map["web_name"]!''}</title>
    <link rel="icon" href="/resources/chat/pc/favicon.ico" type="image/x-icon" />
    <link rel="shortcut icon" href="/resources/chat/pc/favicon.ico" type="image/x-icon"/>
    <link rel="stylesheet" type="text/css" href="/resources/chat/pc/css/reset.css"/>
    <link rel="stylesheet" href="/resources/chat/pc/css/hidescroll.css">
    <link rel="stylesheet" href="/resources/chat/pc/css/chatroom.css"/>
    <link rel="stylesheet" type="text/css" href="/resources/chat/pc/css/share.css">
    <script>
        if (/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent)) {
            window.location.href = '//' + window.location.host + '/chat/mIndex'
        }
    </script>
</head>
<body>
<base target="_blank">
<div class="container scroll_content" style="background: url(${room.roomPcBg!''}) no-repeat;background-size: 100% 100%">
    <div class="head">
        <img class="logo" src="${map["pc_logo"]!''}">
        <ul class="nav">
            <li><a id="seve-to-desktop" class="active" href="javascript:;"><img
                            src="/resources/chat/pc/images/save-to-desktop.png">&nbsp;保存到桌面</a></li>
            <li>
                <a href="/chat/mIndex" class="mobile-chat">
                    <img src="/resources/chat/pc/images/mobile-phone.png">&nbsp;手机聊天室
                    <div id="qrcode">
                        <p style="line-height: 1.5">打开手机扫一扫</p>
                    </div>
                </a>
            </li>
            <li><a href="${map["web_url"]!''}" target="_blank"><img src="/resources/chat/pc/images/caipiao.png">&nbsp;彩票官网</a></li>
            <li><a target="_blank" href="javascript:void();"><img src="/resources/chat/pc/images/help.png">&nbsp;帮助中心</a></li>
        </ul>
        <a id="login" class="login" href="javascript:void();">登录</a>
        <div class="nav-user-info">
            <span id="nav-username"></span>
            <a href="javascript:;" id="nav-logout">退出</a>
        </div>
    </div>
    <div class="body">
        <div class="left-sidebar">
            <div class="left-sidebar-title">
                <span><img src="/resources/chat/pc/images/home.png">${room.roomName!''}</span>
                <img src="/resources/chat/pc/images/change-room_03.png" alt=""></div>
            <div class="select-room-container scroll_content">
                <#if roomList?exists>
                    <#list roomList as r>
                        <div data-roomId="1" class="room-item btn">
                        <span onclick="javascript:window.location.href='${request.getContextPath()}/chat/index?roomId=${r.id!''}'">${r.roomName!''}</span>
                        </div>
                    </#list>
                </#if>
            </div>
            <div class="left-sidebar-search">
                <input id="search-input" type="text" placeholder="搜索在线会员">
                <img src="/resources/chat/pc/images/search.png" height="80%" alt="">
            </div>
            <div class="left-sidebar-user-list">
                <div class="info">在线会员（<span id="online-num">0</span>人）</div>
                <div class="user-list scroll_content">

                </div>
            </div>
        </div>
        <div class="middle-container">
            <div class="middle-wellcom">
                <img src="/resources/chat/pc/images/toast.png">&nbsp;
                <div class="proclamation">
                    <ul>
                        <li>
                            ${map["pc_notice"]!''}
                        </li>
                        <li>
                            ${map["pc_notice"]!''}
                        </li>
                        <li>
                            ${map["pc_notice"]!''}
                        </li>
                    </ul>
                </div>
            </div>
            <div class="middle-content">
                <a href="#userid" target="_self" id="at-msg-tips" class="at-msg-tips"></a>
                <ul class="float-right-tools">
                    <li>
                        <a href="javascript:void();" id="clear-content">
                            <img src="/resources/chat/pc/images/clear.png">
                            <span>清屏</span>
                        </a>
                    </li>
                    <li>
                        <a href="javascript:void();" id="change-theme">
                            <img src="/resources/chat/pc/images/change-theme.png">
                            <span>换肤</span>
                        </a>
                    </li>
                    <li>
                        <a href="${map["customer_url"]!''}">
                            <img src="/resources/chat/pc/images/service.png">
                            <span>客服</span>
                        </a>
                    </li>
                    <li>
                        <a id="show-my-redbag" href="javascript:void();">
                            <img src="/resources/chat/pc/images/red-pack.png">
                            <span>红包</span>
                        </a>
                    </li>
                </ul>
                <div class="msg-container scroll_content">
                </div>
                <div class="have-more-message btn">收到<span id="new-msg-count">0</span>新消息</div>
                <div id="middle-join-room-container" class="middle-join-room-container">
                </div>
            </div>
            <div class="wirte-container">
                <div class="write-container-menu">
                    <ul>
                        <li>
                            <a class="smile" href="javascript:void(0);">
                                <img src="/resources/chat/pc/images/select-emji.png">
                            </a>
                        </li>
                        <li>
                            <a class="select-pic" href="javascript:void(0);">
                                <img id="sendImg" src="/resources/chat/pc/images/select-pic.png">
                            </a>
                            <input type="file" onchange="sendPictureContent(this)" id="sendImgBtn">
                        </li>
                        <li class="share-li">
                            <a class="share" href="javascript:void(0);">
                                <img src="/resources/chat/pc/images/share.png">
                            </a>
                            <div class="share-container">
                                <ul>
                                    <li>
                                        <a title="分享到微信朋友圈" onclick="showWxModal()" href="javascript:void(0)"
                                           class="bds_weixin">
                                            <img src="/resources/chat/pc/images/share_weichat.png"/>
                                            <span>朋友圈</span>
                                        </a>
                                        <div class="wechat-share">

                                        </div>
                                    </li>
                                    <li>
                                        <a onclick="showWxModal()" title="分享给微信朋友" href="javascript:void(0)"
                                           class="bds_weixin">
                                            <img src="/resources/chat/pc/images/share_weichat_friend.png"/>
                                            <span>微信朋友</span>
                                        </a>
                                        <div class="wechat-share_friend">
                                        </div>
                                    </li>
                                    <li>
                                        <a title="分享到QQ好友" href="javascript:void(0)" class="share_qq"
                                           onclick="shareToqq(event)">
                                            <img src="/resources/chat/pc/images/share_qq_friend.png"/>
                                            <span>QQ好友</span>
                                        </a>
                                    </li>
                                    <li>
                                        <a title="分享到QQ空间" href="javascript:void(0)" class="share_qzone"
                                           onclick="shareToQzone(event)">
                                            <img src="/resources/chat/pc/images/share_qq_zone.png"/>
                                            <span>QQ空间</span>
                                        </a>
                                    </li>
                                </ul>
                                <div class="join-container-qrcode">
                                </div>
                            </div>
                        </li>
                    </ul>
                </div>
                <div class="write-container-form">
                    <div class="editor-container">
                        <div id="editor" class="editor scroll_content" contenteditable="true">输入聊天内容，@TA</div>
                        <button type="button" class="msg-send-btn">
                            <img src="/resources/chat/pc/images/send.png" width="20">发送
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <div class="right-sidebar">
            <div class="tab-bar">
                <#if tabmenu?exists>
                    <#list tabmenu as r>
                         <a target="tabbar" href="${r.sysValue!''}">${r.remark!''}</a>
                    </#list>
                </#if>
            </div>
            <div style="position: absolute;width: 100%;top: 50px;bottom: 0px;">
                <iframe class="scroll_content" name="tabbar" id="iframe" width="100%" src="${defaultTab.sysValue!''}"
                        height="100%"
                        frameborder="0"></iframe>
            </div>
        </div>
    </div>
    <!--中奖-->
    <div class="price-modal-mask">
        <div class="text-container">
            <button class="price-modal-mask-close-btn"> &times;</button>
            <p class="price-info"></p>
        </div>
    </div>
    <!--登陆-->
    <div class="login-modal-mask">
        <div class="login-form-container">
            <button class="login-form-container-close-btn">&times;</button>
            <form id="login-form" action="javascript:;">
                <div id="username" class="login-form-control">
                    <img src="/resources/chat/pc/images/username-icon.png">
                    <input type="text" name="username" placeholder="请输入账号">
                </div>
                <div id="password" class="login-form-control">
                    <img src="/resources/chat/pc/images/password-icon.png">
                    <input type="password" name="password" placeholder="请输入密码">
                </div>
                <div class="login-form-control submit">
                    <input type="submit" value="登录">
                </div>
            </form>
        </div>
    </div>
    <!--红包-->
    <div class="redbag-modal-mask">
        <div class="redbag-modal-container">
            <button class="redbag-modal-mask-close-btn">&times;</button>
            <div class="redbag-modal-container-give-user-info">
                <img src="/resources/chat/pc/images/emoji/emoji1.jpg" width="65" height="65">
                <span></span>
            </div>
            <p class="redbag-modal-container-give-user-msg">恭喜发财，大吉大利</p>
            <p class="redbag-modal-container-get-amount"></p>
            <p class="redbag-modal-container-read-my-redbag"><a href="#">查看我的红包 &gt;&gt;</a></p>
            <p class="redbag-modal-container-tips">红包有失效期，请及时兑换</p>
        </div>
    </div>
    <!--红包已抢完-->
    <div class="redbag-empty-modal-mask">
        <div class="redbag-empty-modal-container">
            <button class="redbag-empty-modal-mask-close-btn">&times;</button>
            <div class="redbag-empty-modal-container-give-user-info">
                <img src="/resources/chat/pc/images/emoji/emoji1.jpg" width="65" height="65">
                <span>username</span>
            </div>
            <p class="redbag-empty-modal-container-give-user-msg">恭喜发财，大吉大利</p>
            <p class="redbag-empty-modal-container-get-amount">红包已抢完</p>
            <p class="redbag-empty-modal-container-read-my-redbag"><a href="#">查看我的红包 &gt;&gt;</a></p>
            <p class="redbag-empty-modal-container-tips">红包有失效期，请及时兑换</p>
        </div>
    </div>
    <!--我的红包-->
    <div class="my-redbag-modal-mask">
        <div class="my-redbag-container">
            <span class="my-redbag-title"><img src="/resources/chat/pc/images/red-bag-title.png"></span>
            <button class="my-redbag-close-btn">&times;</button>
            <div class="tab-title"><img src="/resources/chat/pc/images/my-red-bag-table-th_03.jpg"></div>
            <div class="my-redbag-table scroll_content">
                <div id="red-bag-list-container">

                </div>
            </div>
            <div class="get-more-bag-list">
                <button class="btn get-more-bag-list-btn">加载更多&gt;&gt;</button>
            </div>
            <div class="get-red-bag-info">共有<span id="surplus"></span>个红包还未过期，请及时兑换。</div>
        </div>
    </div>
    <!--个人中心-->
    <div class="person-center">
        <div class="person-center-container">
            <div class="main-content">
                <div style="position: relative">
                    <img id="modify-user-icon" src="/resources/chat/pc/images/emoji/emoji1.jpg" width="85px"
                         height="85px">
                    <div id="modify-user-icon-edit">
                        <img src="/resources/chat/pc/images/edit.png" width="30px"
                             height="30px">
                    </div>
                </div>
                <div class="modify-show-info">
                    <p id="modify-show-username"></p>
                    <p id="modify-show-user-ip"></p>
                    <p id="modify-show-user-register-time"></p>
                    <button class="modify-password-tab-btn btn">修改密码</button>
                </div>
            </div>
            <div class="user-icon-logo">
                <form id="userIconForm" action="javascript:;">
                    <button class="modify-icon-tab-btn btn">上传头像</button>
                    <input onchange="uploadFoeverUserIcon()" id="userIconFormInput" type="file" name="file">
                </form>
                <p class="user-icon-logo-title">选择默认头像</p>
                <div class="choose-user-icon-container"></div>
                <button class="btn" id="modify-user-icon-btn">确定</button>
            </div>
            <div class="modify-user-info">
                <button id="modify-user-info-close">&times;</button>
                <div style="height: 20px; margin-top: 30px; border-top: 1px solid #eee;"></div>
                <form action="javascript:;" id="modify-form">
                    <div class="modify-form-control">
                        <lable>账号：</lable>
                        <p class="modify-form-control-content" id="modify-form-item-username">用户名</p>
                    </div>
                    <input type="hidden" name="token" id="input-token">
                    <div class="modify-form-control">
                        <label for="">原密码：</label>
                        <input placeholder="请输入原密码" class="modify-form-control-content" type="password" required
                               name="oldPassword">
                    </div>
                    <div class="modify-form-control">
                        <label for="">新密码：</label>
                        <input placeholder="请输入新密码" class="modify-form-control-content" type="password" required
                               name="firstPassword">
                    </div>
                    <div class="modify-form-control">
                        <label for="">再次输入：</label>
                        <input placeholder="请再次输入新密码" class="modify-form-control-content" type="password" required
                               name="secondPassword">
                    </div>
                    <div class="modify-form-control">
                        <button type="submit" id="modify-password-btn">确定</button>
                        <button type="reset" id="cancel-password-btn">重置</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <!--右键菜单-->
    <div id="user-list-menu" class="context-menu">
        <button class="btn">踢出</button>
        <button class="btn">禁言</button>
    </div>
    <!--显示大图-->
    <div class="show-big-img-modal-mask">
        <img id="big-img">
    </div>
    <!--微信分享-->
    <div id="modal-weixin-share"></div>
</div>
</body>
<script>
    var base_url = '${request.getContextPath()}' //接口请求地址
    /**
     * 分享参数
     * _title : 分享标题
     * _showcount 是否显示分享总数,显示：'1'，不显示：'0'，默认不显示
     * _desc 分享的描述，可选参数
     * _summary 分享摘要，可选参数
     * _site 分享来源，可选参数
     * _url  分享的链接
     * _pic  分享图片的路径，多张图片以＂|＂隔开，可选参数
     */
    var _title = 'test',
        _showcount = 'test',
        _desc = 'test',
        _summary = 'test',
        _site = location.href,
        _desc = 'test',
        _url = location.href,
        _pic = '';

    /**
     *聊天室初始化参数
     */
    var NeedInfo = {
        domain: '${domainName!''}',
        roomId: ${room.id!''},
        forbidChat: ${room.forbidStatus!'0'?number}, //是否禁言状态
        autoScroll: true,
        showHaveMoreMessage: false
    }

    /**
     * 聊天室客户端配置
     */
    var ws_options = {
        reconnect: true, //是否重连 默认true 可选
        reconnectInterval: 30, //重连间隔（秒）默认30秒重连一次 可选
        heartBeat: true, //是否发送心跳 默认true 可选
        heartBeatInterval: 30, // 心跳间隔 默认30秒发送一次心跳 可选
        checkStatus: true, //状态检测 默认true 可选
        checkStatusInterval: 5 //检测间隔 默认5秒检测一次 可选
    }
</script>
<script src="/resources/chat/pc/js/base.js" type="text/javascript" charset="utf-8"></script>
<script src="/resources/chat/pc/js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
<script src="/resources/chat/pc/js/layer/layer.js" type="text/javascript" charset="utf-8"></script>
<script src="/resources/chat/pc/js/qrcode.min.js" type="text/javascript" charset="utf-8"></script>
<script src="/resources/chat/pc/js/chatroom.js" type="text/javascript" charset="utf-8"></script>
<script src="/resources/chat/pc/js/init.js" type="text/javascript" charset="utf-8"></script>
<script src="/resources/chat/pc/js/ddeditor.plugin.js" type="text/javascript" charset="utf-8"></script>
<script src="/resources/chat/pc/js/scrollForever.min.js"></script>
<script src="/resources/chat/pc/js/index.js" type="text/javascript" charset="utf-8"></script>
<script src="/resources/chat/pc/js/share.js" type="text/javascript" charset="utf-8"></script>
</html>

