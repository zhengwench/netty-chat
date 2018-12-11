var websocket;
var reConnect = true;
var webSocketUrl;
var faceList = document.getElementById("faceList");
var houseList = document.getElementById("houseList");
var shareList = document.getElementById("shareList");
var container = document.getElementById("container");
var showBox = document.getElementById("showBox");
var oprateBox = document.getElementById("oprateBox");
var redPackBox = document.getElementById("redPackBox");
var noPack = document.getElementById("noPack");
var hasPack = document.getElementById("hasPack");
var sendName = document.getElementById("sendName");
var sendNumber = document.getElementById("sendNumber");
var inputLogin = document.getElementById("inputLogin");
var rewordText = document.getElementById("rewordText");
var reword = document.getElementById("reword");
var jionAllList = document.getElementById('jionAllList');
var countBox = document.getElementById('countBox');
var sendRedbag = document.getElementById('sendRedbag');
var inferBox=document.getElementById('inferBox');
var infer=document.getElementById('infer');
var oul = document.getElementById('messageList');
var userInfo,userId, userName;
var token = '';
var icon;
var count;
var msgNew = 0;
var forbidchat=true;

console.log('token:'+$t.cookie.get('chat_token'));
console.log('user_info:'+$t.cookie.get('user_info'));
if($t.cookie.get('chat_token')){
    token=$t.cookie.get('chat_token');
    userInfo = $t.json($t.cookie.get('user_info'));
    if(userInfo){
        userId = userInfo.userId;
        userName = userInfo.userName;
        if(userInfo.authority){
            userInfo.authority.forEach(function (item,index) {
                if(item=='chat:forbidchat'){
                    forbidchat=false;
                }

            })
        }
    }
    document.getElementById('userName').style.display = 'none';
    document.getElementById('indexAvatar').style.display = 'block';
    inputLogin.style.display = 'none';
}
console.log(userInfo);




$("#faceList li").click(function () {

    $("#text").focus();
    var osrc = $(this).children(0).attr('src');
    var img_url = '<img class="faceImg"   src="' + osrc + '"/>'
    _insertimg(img_url);
})
//长按事件
// $.fn.longPress = function(fn) {
//     var timeout = undefined;
//     var $this = this;
//     for(var i = 0;i<$this.length;i++){
//         $this[i].addEventListener('touchstart', function(event) {
//             timeout = setTimeout(fn, 800);  //长按时间超过800ms，则执行传入的方法
//
//         }, false);
//         $this[i].addEventListener('touchend', function(event) {
//             clearTimeout(timeout);  //长按时间少于800ms，不会执行传入的方法
//         }, false);
//     }
// }
//
// $('.messageList li').longPress(function(){
//
// });

//点击聊天室
$("#chatHouse").click(function () {
    $("#chatHouseBox").css({
        'display': 'block'
    })
    $("#mask").css({
        'display': 'block'
    })
})
$("#redPackBtn").click(function () {
    $("#myPackBox").css({
        'display': 'block'
    })
    $("#mask").css({
        'display': 'block'
    })
})
$("#myPackBtn1,#myPackBtn2").click(function () {
    $("#myPackBox,#redPackBox").css({
        'display': 'none'
    })
    $("#mask").css({
        'display': 'none'
    })

    if (token != undefined && token != null && token != ''&&userInfo&&userInfo!=null) {
        packList.innerHTML='';
        pullupRefresh();
    }
    $("#myPackPage").show();
})
//打开和关闭新页面
function myPackClose() {
    $("#myPackPage").hide();
}

function userInfoClose() {
    $("#userInfoPage").hide();
}

function userPhotoClose() {
    $("#userPhotoPage").hide();
}

function userPassClose() {
    $("#userPassPage").hide();
}
function loginClose() {
    $("#loginPage").hide();
}
function loginPageFun() {
    $("#loginPage").show();
}
function userInfoPageFun() {
    $("#userInfoPage").show();
}

function userPassPageFun() {
    $("#userPassPage").show();
}

function userPhotoPageFun() {
    $("#userPhotoPage").show();
}

$("#mask").click(function () {
    $("#chatHouseBox").css({
        'display': 'none'
    })
    $("#myPackBox").css({
        'display': 'none'
    })
    $("#redPackBox").css({
        'display': 'none'
    })
    $("#reword").css({
        'display': 'none'
    })
    $("#mask").css({
        'display': 'none'
    })
})
window.onbeforeunload = function () {
    if(websocket){
        websocket.close();
    }
}
//退出登录
function  loginout() {
    var btnArray = ['否', '是'];
    mui.confirm('你确定退出登录吗？', '', btnArray, function(e) {
        if (e.index == 1) {
            $t.cookie.del('user_info');
            $t.cookie.del('chat_token');
            reConnect = false;
            websocket.close();
            //与服务器断开连接
            websocket.onclose = function (event) {
                console.log("已经与服务器断开连接当前连接状态：" + this.readyState);
            };

        } else {
        }
    })


}

//文字横向滚动
function ScrollImgLeft() {
    var speed = 50; //初始化速度 也就是字体的整体滚动速度
    var MyMar = null; //初始化一个变量为空 用来存放获取到的文本内容
    var scroll_begin = document.getElementById("scroll_begin"); //获取滚动的开头id
    var scroll_end = document.getElementById("scroll_end"); //获取滚动的结束id
    var scroll_div = document.getElementById("scroll_div"); //获取整体的开头id
    scroll_end.innerHTML = scroll_begin.innerHTML; //滚动的是html内部的内容,原生知识!
    //定义一个方法
    function Marquee() {
        if (scroll_end.offsetWidth - scroll_div.scrollLeft <= 0)
            scroll_div.scrollLeft -= scroll_begin.offsetWidth;
        else
            scroll_div.scrollLeft++;
            if(scroll_end.offsetWidth - scroll_div.scrollLeft <= 0){
                scroll_div.scrollLeft=0;
            }

    }

    MyMar = setInterval(Marquee, speed); //给上面的方法设置时间  setInterval
    //鼠标点击这条公告栏的时候,清除上面的方法,让公告栏暂停
    scroll_div.onmouseover = function () {
        clearInterval(MyMar);
    }
    //鼠标点击其他地方的时候,公告栏继续运动
    scroll_div.onmouseout = function () {
        MyMar = setInterval(Marquee, speed);
    }
}
// mui.init({
//     gestureConfig:{
//         longtap: true, //默认为false
//         release:true
//     }
// });
// mui(".messageList").on('longtap',".ico_jia",function(){
//     alert('触发长按');
// })
//
// mui("#MenuList").on('release',".ico_jia",function(){
//     console.log('触发离开');
//})


//tab切换ifarame
var b_width = Math.max(document.body.scrollWidth,document.body.clientWidth);
var b_height = Math.max(document.body.scrollHeight,document.body.clientHeight);
var iframeBox=document.getElementById("iframeBox");
var loading=document.getElementById("loading");
var c_iframe,dataHref,showId,showValue,openUrl,c_iframe;
$(".navlist li").click(function () {

    dataHref=$(this).children(0).attr("dataHref");
    showId=$(this).children(0).attr("showId");
    showValue=$(this).children(0).attr("showValue");
    openUrl=$(this).children(0).attr("openUrl");//是否新窗口打开
    c_iframe=document.getElementById(showId);

    $(".iframepage").css('display','none');

    if(dataHref==''){
        loading.style.display='none';
        $(".active").removeClass("active");
        $(this).addClass("active");
        iframeBox.style.display = 'none';
    }else if(dataHref!==''&&showValue=='true') {

        loading.style.display='block';
        iframeBox.style.display = 'block';
        $(".active").removeClass("active");
        $(this).addClass("active");
        //判断有没有iframe,没有就增加
        if (!c_iframe) {
            var frameHtml = '<iframe src="' + dataHref + '" class="iframepage" id="' + showId + '" name="iframepage" frameBorder=0 marginheight=0 marginwidth=0 scrolling=auto  scrolling  width="100%" height="100%"  onload="endLoad()">' +
                '</iframe>';
            iframeBox.innerHTML += frameHtml;
            c_iframe=$("#"+showId+"");
            if(device.iphone() || device.ipad() || device.ios()){
                c_iframe.attr('scrolling', 'no');
                c_iframe.css('width', $(document).width());
                c_iframe.addClass("scroll-wrapper-ios");
            }else{
                c_iframe.attr('scrolling-y', 'auto');
            }
            //判断是否为苹果手机
            if(openUrl=='true'&&!$t.cookie.get('isSafari')){
                $t.cookie.set('isSafari','true', 'd7');
                if (device.iphone() || device.ipad() || device.ios()) {// /Android/.test(navigator.userAgent/
                    window.open(dataHref);
                }
            }
        } else {
            iframeBox.style.display = 'block';
            c_iframe.style.display = 'block';
            setTimeout(function () {
                loading.style.display='none';
            },400)

        }

    }


})

// $(window).bind("pageshow", function () {
//         iframeBox.style.display = 'block';
//         c_iframe.src=dataHref;
//         loading.style.display='none';
//         c_iframe=$("#"+showId+"");
//         c_iframe.attr('scrolling', 'no');
//         c_iframe.css('width', $(document).width());
//         c_iframe.addClass("scroll-wrapper-ios");
// })

//iframe加载完关闭loading
function  endLoad() {
    loading.style.display = 'none';
}

//分享到qq
function shareToqq(event) {
    event.preventDefault();
    var _shareUrl = 'https://connect.qq.com/widget/shareqq/iframe_index.html?';
    _shareUrl += 'url=' + encodeURIComponent(url || location.href); //分享的链接
    _shareUrl += '&title=' + encodeURIComponent(title || document.title); //分享的标题
    window.open(_shareUrl, '_blank');
}
var showcount=0;
//分享到QQ空间
function shareToQzone(event) {
    event.preventDefault();
    var _shareUrl = 'http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?';
    _shareUrl += 'url=' + encodeURIComponent(url || document.location); //参数url设置分享的内容链接|默认当前页location
    _shareUrl += '&showcount=' + showcount || 0; //参数showcount是否显示分享总数,显示：'1'，不显示：'0'，默认不显示
    _shareUrl += '&desc=' + encodeURIComponent(desc || '分享的描述'); //参数desc设置分享的描述，可选参数
    _shareUrl += '&summary=' + encodeURIComponent(summary || '分享摘要'); //参数summary设置分享摘要，可选参数
    _shareUrl += '&title=' + encodeURIComponent(title || document.title); //参数title设置分享标题，可选参数
    _shareUrl += '&site=' + encodeURIComponent(site || ''); //参数site设置分享来源，可选参数
    _shareUrl += '&pics=' + encodeURIComponent(pic || ''); //参数pics设置分享图片的路径，多张图片以＂|＂隔开，可选参数
    window.open(_shareUrl, '_blank');
}


//模态框
new QRCode(document.querySelector('#modal-weixin-share'), {
    text: window.location.href,
    width: 200,
    height: 200,
    colorDark: '#000000',
    colorLight: '#ffffff',
    correctLevel: QRCode.CorrectLevel.H
})
$('#modal-weixin-share').append('<span style="color: #fff">请打开微信扫码分享</span>')


//微信分享
function showWxModal() {
    setTimeout(function () {
        $('#modal-weixin-share').css('display', 'flex')
    },20)
}
$("#modal-weixin-share").click(function () {
    $(this).hide();
})


ScrollImgLeft();
window.onload = function () {
    // 阻止双击放大
    var lastTouchEnd = 0;
    document.addEventListener('touchstart', function(event) {
        if (event.touches.length > 1) {
            event.preventDefault();
        }
    });
    document.addEventListener('touchend', function(event) {
        var now = (new Date()).getTime();
        if (now - lastTouchEnd <= 300) {
            event.preventDefault();
        }
        lastTouchEnd = now;
    }, false);

    // 阻止双指放大
    document.addEventListener('gesturestart', function(event) {
        event.preventDefault();
    });

    connectSocket();
    reConnectSocket();
}

var getting=false;
//链接websoket;
function connectSocket() {
    //如果ajax未完成则等待完成
    if (getting) {
        return null
    }
    getting=true;
    $.ajax(webSocketUrlPort, {
        data: {
            roomId:roomId
        },
        dataType: 'json',
        type: 'post',
        timeout: 10000,
        success: function (data) {
            console.log(data);
            getting=false;
            if (data.success) {
                webSocketUrl = data.result;
                if (!window.WebSocket && !window.WebSocket.prototype.send) {
                    mui.toast("当前浏览器不支持websocket");
                    return;
                }
                var protocolStr = document.location.protocol;
                //判断当前浏览器是否支持WebSocket
                try {
                    var socketurl;
                    socketurl = webSocketUrl + '/?token=' + token + '&domain=' + domain + '&roomId=' + roomId + '';
                    websocket = new WebSocket(socketurl); //连接服务器
                    websocket.onopen = function (event) {
                        console.log("已经与服务器建立了连接当前连接状态：" + this.readyState);
                        //console.log(event);
                        console.log("用户信息：");
                        console.log(userInfo);
                        //加入房间
                        var msg = {
                            Id: userId,
                            UserName: userName,
                            RoleIcon: '',
                            Icon: icon,
                            Commond: 'S_JOIN_ROOM',
                            Token: token
                        };
                        if (websocket && websocket.readyState == 1) {
                            websocket.send(JSON.stringify(msg));
                        } else {
                            console.log('当前webSocket服务状态为' + websocket.readyState);
                        }
                        heartConnect();
                    };
                    //接收消息
                    websocket.onmessage = function (event) {
                        isNew = true;
                        message(event);
                    };
                    //与服务器断开连接
                    websocket.onclose = function (event) {
                        console.log("已经与服务器断开连接当前连接状态：" + this.readyState);
                    };
                    //连接异常
                    websocket.onerror = function (event) {
                        console.log("WebSocket异常！");
                    };

                } catch (ex) {
                    console.log("连接socket异常" + ex);
                }
            } else {
                mui.toast(data.errorDesc);
            }
        },
        error: function (xhr, type, errorThrown) {
            getting=false;
        }
    });

}


//心跳发送
function heartConnect() {
    setTimeout(function () {
        if(reConnect){
            //发送心跳消息
            var payload = {
                "command": "C_HEART_BEAT",
                "domain": domain,
                "roomId": roomId
            };
            var jStr = JSON.stringify(payload);
            if (websocket && websocket.readyState == 1) {
                websocket.send(jStr)
            }

            heartConnect();
        }

    }, 30000);
}

//重连
function reConnectSocket() {
    setTimeout(function () {
        if ((!websocket || websocket.readyState == 3) && reConnect) {
            console.log("正在重连！");
            connectSocket();
            if (websocket && websocket.readyState == 1) {
                console.log("重新连接成功！");
            }
        }
        if(reConnect){
            reConnectSocket();
        }
    }, 5000);
}

function addFun() {
    if (oprateBox.style.display == 'flex') {
        oprateBox.style.display = 'none';
    } else {
        oprateBox.style.display = 'flex';
    }
    shareList.style.display = 'none';
    faceList.style.display = 'none';

}

function shareFun() {
    oprateBox.style.display = "none";
    shareList.style.display = 'flex';
    faceList.style.display = 'none';
}

function faceFun() {
    if (faceList.style.display == 'flex') {
        faceList.style.display = 'none';
    } else {
        faceList.style.display = 'flex';
    }
    shareList.style.display = "none";
    oprateBox.style.display = "none";
}


//获取聊天室人数
$.ajax(onlineUser, {
    data: {},
    dataType: 'json',//服务器返回json格式数据
    type: 'post',//HTTP请求类型
    timeout: 10000,//超时时间设置为10秒；
    success: function (data) {
        console.log(data);
        if (data.success) {
            count = data.result.count;
            countBox.innerHTML = count + '人';
        } else {
            //mui.toast(data.errorDesc);
        }
    },
    error: function (xhr, type, errorThrown) {
    }
});

//调整页面
function OnResize() {
    var headHeight = $('#header').outerHeight(),
        footerHeight = $('#footer').outerHeight(),
        tabs = $('#head_1').outerHeight(),
        bodyH = document.body.offsetHeight;

    var height = bodyH - headHeight - tabs - footerHeight;

    $('#msgListBox').height(height);
    if (device.iphone() || device.ipad() || device.ios()) {
        $('#msgListBox').addClass("scroll-wrapper-ios");
    }
}
function scrollToBottom() {
    setTimeout(function () {
        if (device.iphone() || device.ipad() || device.ios()) {
            oul.scrollTop = oul.scrollHeight;
        }else{
            oul.scrollTop = oul.scrollHeight;
            oul.scrollIntoView();
        }
    }, 200)
}

//@消息的处理
function doAtHandler(data, msgInfo) {

    var pattern1 = /^([\w&#;]+?)\s.*/g
    var pattern2 = /^([\w&#;]+?)&nbsp;.*/g
    var splitData = data.split('@')
    var atList = []
    if (splitData.length < 1) {
        return null
    }
    for (var i = 1, l = splitData.length; i < l; i++) {
        var r1 = pattern1.exec(splitData[i])
        var r2 = pattern2.exec(splitData[i])
        if (r1) {
            atList.push(r1[1])
        }
        if (r2) {
            atList.push(r2[1].replace('&nbsp;', ''))
        }
    }
    console.log('atList:', atList)
    //获取用户信息
    if (!userInfo) {
        return null
    }
    //循环信息
    for (var i = 0, l = atList.length; i < l; i++) {
        if (atList[i] == userInfo.userName) {
            if (userId !== msgInfo.userId) {
               $("#infer").show();
            }
        }
    }
}

function inferFun(){//@滚动到某条消息
    var atId= $("#infer").attr("data-mess");
    window.location.hash = atId;
    $("#infer").hide();
}

//接收到消息的回调方法
var oimg;
function message(event) {
    var data = JSON.parse(event.data);
    console.log(data);
    switch (data.command) {

        case   'S_HISTORY_CHAT':
            oul.innerHTML = '';
            msgNew = 0;
            $('.newMessage').css('display', 'none');
            data.content.forEach(function (item, index) {
                var item = JSON.parse(item);
                var avatar = item.userIcon || '/resources/chat/m/images/userSet.png';
                if (item.userIcon == '随机') {
                    avatar = '/resources/chat/m/images/userSet.png';
                }
                var userName = item.userName || '';

                switch (item.command) {
                    //这里表示普通消息记录
                    case 'S_MESSAGE':
                        var odiv = document.createElement('div');
                        odiv.id = item.messageId;
                        var a = item.content;//反转义开始
                        var c = document.createElement('div');
                        c.innerHTML = a;
                        a = c.innerText || c.textContent; ///反转义结束
                        content = c.innerText || c.textContent;
                        if (item.userId == userId) {
                            if (item.userIcon && item.userIcon.match(/userIcon.*?/g)) {
                                oimg = '<div  class="rightAvatar  userIcon  ' + item.userIcon + '" ></div>';
                            } else {
                                oimg = '<img class="rightAvatar" src="' + avatar + '" />';
                            }
                            odiv.className = 'rightRecieve flex flexEnd';
                            odiv.innerHTML = '<div class="reciever">' +
                                oimg +
                                '<div class="content" >' +
                                '<div class="userInfo">' +
                                '<span>' + item.messageTime + '</span>' +
                                '</div>' +
                                '<div class="textcontent">' +
                                '<div class="rightTriangle"></div>' + a +
                                '</div>' +
                                '</div>' +
                                '</div>';
                        } else {
                            if (item.userIcon && item.userIcon.match(/userIcon.*?/g)) {
                                oimg = '<div  class="leftAvatar  userIcon  ' + item.userIcon + '" ></div>';
                            } else {
                                oimg = '<img class="leftAvatar" src="' + avatar + '" />';
                            }
                            odiv.className = 'leftRecieve flex flexStart';
                            odiv.innerHTML = '<div class="reciever">' +
                                oimg +
                                '<div class="content">' +
                                '<div class="userInfo">' +
                                '<span class="userName">' + userName + '</span><span>' + item.messageTime + '</span>' +
                                '</div>' +
                                '<div class="textcontent">' +
                                '<div class="leftTriangle"></div>' +
                                a +
                                '</div>' +
                                '</div>' +
                                '</div>';
                        }

                        oul.insertBefore(odiv, oul.children[0]);

                        break;


                    //这里是红包记录
                    case  'S_SEND_REDBAG':
                        if (item.userId == userId) {
                            if (item.userIcon && item.userIcon.match(/userIcon.*?/g)) {
                                oimg = '<div  class="rightAvatar  userIcon  ' + item.userIcon + '" ></div>';
                            } else {
                                oimg = '<img class="rightAvatar" src="' + avatar + '" />';
                            }
                            var odiv = document.createElement('div');
                            odiv.className = 'rightRecieve flex flexStart';
                            odiv.innerHTML = '<div class="reciever redPack" redbagId=' + item.content.redbagId + '>' +
                                oimg +
                                '<div class="content"  >' +
                                '<div class="userInfo">' +
                                '<span class="userName">' + item.userName + '</span><span>' + item.messageTime + '</span>' +
                                '</div>' +
                                '<div class="textcontent" onclick="getPack(' + item.content.redbagId + ')">' +
                                '<div class="rightTriangle"></div>' +
                                '<div class="flex alignItems">' +
                                '<img class="packIcon" src="/resources/chat/m/images/redPack.png"/>' +
                                '<div class="">' +
                                '<p class="packP1">' + userName + '的红包</p>' +
                                '<p class="packP2">领取红包</p>' +
                                '</div>' +
                                '</div>' +
                                '<p class="packP3">聊天室的红包</p>' +
                                '</div>' +
                                '</div>' +
                                '</div>';
                        } else {
                            if (item.userIcon && item.userIcon.match(/userIcon.*?/g)) {
                                oimg = '<div  class="leftAvatar  userIcon  ' + item.userIcon + '" ></div>';
                            } else {
                                oimg = '<img class="leftAvatar" src="' + avatar + '" />';
                            }
                            var odiv = document.createElement('div');
                            odiv.className = 'leftRecieve flex flexStart';
                            odiv.innerHTML = '<div class="reciever redPack" redbagId=' + item.content.redbagId + '>' +
                                oimg +
                                '<div class="content" >' +
                                '<div class="userInfo">' +
                                '<span class="userName">' + item.userName + '</span><span>' + item.messageTime + '</span>' +
                                '</div>' +
                                '<div class="textcontent" onclick="getPack(' + item.content.redbagId + ')">' +
                                '<div class="leftTriangle"></div>' +
                                '<div class="flex alignItems">' +
                                '<img class="packIcon" src="/resources/chat/m/images/redPack.png"/>' +
                                '<div class="">' +
                                '<p class="packP1">' + userName + '的红包</p>' +
                                '<p class="packP2">领取红包</p>' +
                                '</div>' +
                                '</div>' +
                                '<p class="packP3">聊天室的红包</p>' +
                                '</div>' +
                                '</div>' +
                                '</div>';
                        }

                        oul.insertBefore(odiv, oul.children[0]);
                        return;
                }

            })
            $("img").on("error", function () {
                var Loadsrc=$(this).attr('src');
                $(this).attr('data-imgError',true);
                $(this).attr('data-src',Loadsrc);
                $(this).attr('src','/resources/chat/m/images/reloadimg.png');
            });
            $("img").on("click", function () {
                if($(this).attr('data-imgError')){
                    var Loadsrc=$(this).attr('data-src')+'?'+ new Date().getTime();
                    $(this).attr('src',Loadsrc);
                }
            });

            //打开页面判断聊天室是否禁言
            if(forbidstatus){
                $("#forbiddenYes").show();
            }

            scrollToBottom();
            break;

            //这里是接受到消息
            case  "S_MESSAGE":
                var avatar = data.userIcon || '/resources/chat/m/images/userSet.png';
                if (data.userIcon == '随机') {
                    avatar = '/resources/chat/m/images/userSet.png';
                }
                var userName = data.userName || '';
                var odiv = document.createElement('div');
                odiv.id = data.messageId;
                var a = data.content; ///反转义开始
                var c = document.createElement('div');
                c.innerHTML = a;
                a = c.innerText || c.textContent; ///反转义结束
                var d = c.innerText || c.textContent;
                content = c.innerText || c.textContent;
                a = entitiestoUtf16(a);
                if (data.userId == userId) {
                    if (data.userIcon && data.userIcon.match(/userIcon.*?/g)) {
                        oimg = '<div  class="rightAvatar  userIcon  ' + data.userIcon + '" ></div>';
                    } else {
                        oimg = '<img class="rightAvatar" src="' + avatar + '" />';
                    }
                    odiv.className = 'rightRecieve flex flexEnd';
                    odiv.innerHTML = '<div class="reciever">' +
                        oimg +
                        '<div class="content">' +
                        '<div class="userInfo">' +
                        '<span>' + data.messageTime + '</span>' +
                        '</div>' +
                        '<div class="textcontent">' +
                        '<div class="rightTriangle"></div>' +
                        a +
                        '</div>' +
                        '</div>' +
                        '</div>';
                    document.getElementById('text').innerHTML = '';
                    faceList.style.display = "none";
                } else {
                    doAtHandler(a,a );
                    if(infer.style.display=='block'){
                        $("#infer").attr("data-mess",data.messageId);
                    }
                    if (data.userIcon && data.userIcon.match(/userIcon.*?/g)) {
                        oimg = '<div  class="leftAvatar  userIcon  ' + data.userIcon + '" ></div>';
                    } else {
                        oimg = '<img class="leftAvatar" src="' + avatar + '" />';
                    }
                    odiv.className = 'leftRecieve flex flexStart';
                    odiv.innerHTML = '<div class="reciever">' +
                        oimg +
                        '<div class="content">' +
                        '<div class="userInfo">' +
                        '<span class="userName">' + userName + '</span><span>' + data.messageTime + '</span>' +
                        '</div>' +
                        '<div class="textcontent">' +
                        '<div class="leftTriangle"></div>' +
                        a +
                        '</div>' +
                        '</div>' +
                        '</div>';
                }

                faceList.style.display = 'none';
                shareList.style.display = 'none';
                oprateBox.style.display = 'none';
                oul.appendChild(odiv);
                if (data.userId == userId) {
                    scrollToBottom();
                }else{
                    newMessageFun();
                }
                $("img").on("error", function () {
                    var Loadsrc=$(this).attr('src');
                    $(this).attr('imgError',true);
                    $(this).attr('osrc',Loadsrc);
                    $(this).attr('src','/resources/chat/m/images/reloadimg.png');
                });
                $("img").on("click", function () {
                    if($(this).attr('imgError')){
                        var Loadsrc=$(this).attr('osrc')+'?'+ new Date().getTime();
                        $(this).attr('src',Loadsrc);
                    }
                });
            break;


            //用户领取红包
            case "S_RECEIVE_REDBAG":
                if (data.content.amount && data.content.amount > 0 && data.userId == userId) {
                    var odiv = document.createElement('div');
                    odiv.className = 'getRedBox';
                    odiv.innerHTML = '<span class="getRed">你已领取' + data.content.sendName + '的</em><em>红包</em></span>';
                    oul.appendChild(odiv);
                    sendNumber.innerHTML = data.content.amount;
                    sendName.innerHTML = data.content.sendName;
                    sendRedbag.src = data.content.userIcon || '/resources/chat/m/images/userSet.png';
                    if (data.content.sendIcon == '随机') {
                        sendRedbag.src = data.content.sendIcon || '/resources/chat/m/images/userSet.png';
                    }
                    if (data.content.sendIcon && data.content.sendIcon.match(/userIcon.*?/g)) {
                        var icon2 = '/resources/chat/m/images/defalt/' + data.content.sendIcon + '.png';
                        sendRedbag.src = icon2;
                    }
                    noPack.style.display = 'none';
                    hasPack.style.display = 'block';
                    redPackBox.style.display = 'block';
                    mask.style.display = 'block';
                }
            break;

            //进入房间
            case  "S_JOIN_ROOM":
                var op = document.createElement('p');
                op.className = 'joinList joinList1';
                op.innerHTML = '欢迎<span>' + data.userName + '</span>加入聊天室';
                jionAllList.appendChild(op);
                count++;
                countBox.innerHTML = count + '人';
                setTimeout(function () {
                    $(".joinList").hide();
                }, 1500);
            break;

            //离开聊天室
            case  "S_OUT_CHAT":
                count--;
                countBox.innerHTML = count + '人';
            break;


            //红包已领完
            case "S_REDBAG_OUT":
                var odiv = document.createElement('div');
                noPack.style.display = 'block';
                hasPack.style.display = 'none';
                redPackBox.style.display = 'block';
                mask.style.display = 'block';
             break;

            //中奖资讯
            case 'S_LOTTERY_GOODNEWS':
                reword.style.display = 'block';
                rewordText.innerHTML = data.content;
                mask.style.display = 'block';
            break;

            //登录信息失效
            case  'S_LOGIN_INFO_LOSE':
                reConnect = false;
                $t.cookie.del('user_info');
                $t.cookie.del('chat_token');
                reConnect = false;
                websocket.close();
                console.log('登录信息失效')
                //location.reload();
             break;

            //游客该ip已在别处登陆
            case  'S_IP_ERROR':
                reConnect = false;
                mui.alert('游客该ip已在别处登陆', '', function () {
                    //location.reload();
                });
            break

            //账号在别处登录
            case 'S_LOGIN_ANOTHER':
                $t.cookie.del('user_info');
                $t.cookie.del('chat_token');
                reConnect = false;
                mui.alert('您的账号已在另外设备登录', '', function () {
                     $t.cookie.del('user_info');
                     $t.cookie.del('chat_token');
                    reConnect = false;
                    websocket.close();
                   // location.reload();
                });
                console.log('您的账号已在另外设备登录');
            break;

             //连接信息信息错误
            case 'S_CONNECT_ERROR':
                console.log('连接信息信息错误');
            break;

            //服务错误
            case 'S_DOMAIN_ERROR':
                reConnect = false;
                console.log('服务错误');
            break;


            //房间错误
            case  'S_ROOM_ERROR':
                reConnect = false;
                console.log('房间信息错误')
            break;


            //不在聊天室内
            case 'S_NOT_IN_ROOM':
                reConnect = false;
                console.log('当前不在聊天室房间内')
            break;


            //ip黑名单
            case 'S_IP_BLACK':
                $t.cookie.del('user_info');
                $t.cookie.del('chat_token');
                reConnect = false;
                websocket.close();
                mui.alert('ip已加入黑名单，请联系客服', '', function () {
                });
            break;

            //该房间需要登录
            case ' S_ROOM_NEEDLOGIN':
            reConnect = false;
            mui.alert('该房间需要登录', '', function () {
                $("#loginPage").show();
            });
            break;

            //没有进入该房间权限
            case ' S_ROOM_ERROR':
            reConnect = false;
            mui.alert('没有进入该房间权限', '', function () {
            });
            break;


            //聊天室开启禁言
            case 'S_FORBID_CHAT':
                forbidstatus = true;
                $("#forbiddenYes").show();
            break;

            //聊天室关闭禁言
            case 'S_UNFORBID_CHAT':
                forbidstatus = false;
                $("#forbiddenYes").hide();
                $("#forbiddenNo").show();
                setTimeout(function () {
                    $("#forbiddenNo").hide();
                },2000)
            break;

            //撤回消息
            case 'S_RECALL_MESSAGE':
                $("#" + data.content.messageId + "").remove();
                var odiv = ' <div  class="revert">' + data.userName + '撤回一条消息</div>';
                oul.innerHTML += odiv;
                scrollToBottom();
            break;


            //被踢出聊天室
            case 'S_REMOVE_CHAT' :
                mui.alert('您已被踢出聊天室', '', function () {
                    $t.cookie.del('user_info');
                    $t.cookie.del('chat_token');
                    reConnect = false;
                    websocket.close();
                    // location.reload();
                });
            break;

            //收到别人发的红包
            case 'S_SEND_REDBAG':
                var avatar = data.userIcon || '/resources/chat/m/images/userSet.png';
                console.log(data.userIcon);
                if (data.userIcon && data.userIcon.match(/userIcon.*?/g)) {
                    oimg = '<div  class="leftAvatar  userIcon  ' + data.userIcon + '" ></div>';
                } else {
                    oimg = '<img class="leftAvatar" src="' + avatar + '" />';
                }
                if (data.userIcon == '随机') {
                    avatar = '/resources/chat/m/images/userSet.png';
                }
                var odiv = document.createElement('div');
                odiv.className = 'leftRecieve flex flexStart';
                odiv.innerHTML = '<div class="reciever redPack" redbagId=' + data.content.redbagId + '>' +
                    oimg +
                    '<div class="content">' +
                    '<div class="userInfo">' +
                    '<span class="userName">' + data.userName + '</span><span>' + data.messageTime + '</span>' +
                    '</div>' +
                    '<div class="textcontent" onclick="getPack(' + data.content.redbagId + ')">' +
                    '<div class="leftTriangle"></div>' +
                    '<div class="flex alignItems">' +
                    '<img class="packIcon" src="/resources/chat/m/images/redPack.png"/>' +
                    '<div class="">' +
                    '<p class="packP1">' + data.userName + '的红包</p>' +
                    '<p class="packP2">领取红包</p>' +
                    '</div>' +
                    '</div>' +
                    '<p class="packP3">聊天室的红包</p>' +
                    '</div>' +
                    '</div>' +
                    '</div>';
                oul.appendChild(odiv);
                scrollToBottom();
            return;
    }
}



function conFun() {
    faceList.style.display = 'none';
    shareList.style.display = 'none';
    oprateBox.style.display = 'none';
    redPackBox.style.display = 'none';
}

function scrollBottom() {
    msgNew = 0;
    scrollToBottom();
    $('.newMessage').css('display', 'none');
}

//新消息条数
function newMessageFun() {
    var offsetHeight = oul.offsetHeight;
    var nScrollHight = oul.scrollHeight;
    var nScrollTop = oul.scrollTop;
    if (offsetHeight + nScrollTop >= nScrollHight-80) {
        scrollToBottom();
    } else {
        msgNew++;
        $('.newMessage').css('display', 'block');
        if (msgNew > 99) {
            $('.newMessage').text(msgNew + '+');
        } else {
            $('.newMessage').text(msgNew);
        }
    }
}

//用户下滚屏幕新消息条数消失
var p = 0, t = 0;
$("#messageList").scroll(function () {
    p = $(this).scrollTop();
    if (t <= p) {//下滚
        $('.newMessage').css('display', 'none');
        msgNew = 0;
    } else {//上滚
    }
    setTimeout(function () {
        t = p;
    }, 0);

});
//判断是否登录
function isLogin() {
    if (token == undefined || token == null || token == ''|| !userInfo||userInfo==null) {
        mui.toast("需先登录才能发送消息哦~");
        setTimeout(function () {
            $("#loginPage").show();
        }, 800)
    }
}

/*调起大图 S*/
var mySwiper = new Swiper('.swiper-container2', {
    loop: false,
    pagination: '.swiper-pagination2',
})
$("#messageList").on("click", ".uploadImg",
    function () {
        var c = $(".uploadImg").length;
        console.log(c);
        var imgBox = $(".uploadImg");
        $(".big_img .swiper-wrapper").html("")
        var i = $(imgBox).index(this);
        for (var j = 0; j < c; j++) {
            $(".big_img .swiper-wrapper").append('<div class="swiper-slide"><div class="cell"><img src="' + imgBox.eq(j).attr("src") + '" / ></div></div>');
        }
        mySwiper.updateSlidesSize();
        mySwiper.updatePagination();
        $(".big_img").css({
            "z-index": 1001,
            "opacity": "1"
        });
        mySwiper.slideTo(i, 0, false);
        return false;
    });

$(".big_img").on("click",
    function () {
        $(this).css({
            "z-index": "-1",
            "opacity": "0"
        });

    });

// pc大图方法
function showBigImg(){

}

//发送图片
document.getElementById("sendImg").onchange = function (evt) {
    if (token == undefined || token == null || token == ''|| !userInfo||userInfo==null) {
        mui.toast('要先登录才能发送图片哦~')
        setTimeout(function () {
            $("#loginPage").show();
        }, 1000);
        return;
    }
    if(forbidstatus&&forbidchat){
        mui.toast('聊天室已禁言~');
        return;
    }

    var form = document.getElementById("sendImgForm");
    var formData = new FormData(form);
    formData.append("token", token);
    $.ajax(uploadTempImg, {
        data: formData,
        type: 'post', //HTTP请求类型
        timeout: 10000, //超时时间设置为10秒；
        processData: false, // 告诉jQuery不要去处理发送的数据
        contentType: false,
        success: function (data) {
            console.log(data);
            if (data.success) {
                var chatImg = data.result;
                var Content = '<img class="uploadImg"   src="' + chatImg + '"  onclick="showBigImg(this)">';
                if (websocket.readyState == 1) {
                    var msg = {
                        command: 'C_MESSAGE',
                        domain: domain,
                        roomId: roomId,
                        content: Content,
                        token: token,
                        userIcon: icon,
                        userName: userName,
                        userId: userId
                    };
                    websocket.send(JSON.stringify(msg));
                    setTimeout(function () {
                        oul.style.bottom = '0.9rem';
                        scrollToBottom();
                    }, 200)
                } else {
                    console.log("连接状态为" + websocket.readyState + "");
                }
            } else {
                mui.toast(data.errorDesc);
            }
        },
        error: function (xhr, type, errorThrown) {
        }
    });

}
//表情转码
function utf16toEntities(str) {
    var patt=/[\ud800-\udbff][\udc00-\udfff]/g;
    // 检测utf16字符正则
    str = str.replace(patt, function(char){
        var H, L, code;
        if (char.length===2) {
            H = char.charCodeAt(0);
            // 取出高位
            L = char.charCodeAt(1);
            // 取出低位
            code = (H - 0xD800) * 0x400 + 0x10000 + L - 0xDC00;
            // 转换算法
            return "&#" + code + ";";
        } else {
            return char;
        }
    });
    return str;
}



//表情解码
function entitiestoUtf16(str){
    // 检测出形如&#12345;形式的字符串
    var strObj=utf16toEntities(str);
    var patt = /&#\d+;/g;
    var H,L,code;
    var arr = strObj.match(patt)||[];
    for (var i=0;i<arr.length;i++){
        code = arr[i];
        code=code.replace('&#','').replace(';','');
        // 高位
        H = Math.floor((code-0x10000) / 0x400)+0xD800;
        // 低位
        L = (code - 0x10000) % 0x400 + 0xDC00;
        code = "&#"+code+";";
        var s = String.fromCharCode(H,L);
        strObj.replace(code,s);
    }
    return strObj;
}
//发送消息
function send() {
    if (token == undefined || token == null || token == ''||!userInfo||userInfo==null) {
        mui.toast('要先登录才能发送消息哦~')
        setTimeout(function () {
            $("#loginPage").show();
        }, 1000);
        return;
    }
    if(forbidstatus&&forbidchat){
        mui.toast('聊天室已禁言~');
        return;
    }

    var message = document.getElementById('text').innerHTML;
    var message =utf16toEntities(message);
    if (message == "") {
        mui.toast("写些内容再发表吧~");
        return;
    }


    if (websocket.readyState == 1) {
        var msg = {
            command: 'C_MESSAGE',
            domain: domain,
            roomId: roomId,
            content: message,
            token: token,
            userIcon: icon,
            userName: userName,
            userId: userId
        };
        websocket.send(JSON.stringify(msg));
        setTimeout(function () {
            oul.style.bottom = '0.9rem';
            scrollToBottom();
        }, 200)
    } else {
        console.log("连接状态为" + websocket.readyState + "");
    }
}

//领取红包
function getPack(redbagId) {
    if (token == undefined || token == null || token == ''||!userInfo||userInfo==null) {
        mui.toast("需先登录才能领取红包哦~")
        return;
    }
    if (websocket.readyState == 1) {
        var msg = {
            "command": "C_RECEIVE_REDBAG",
            "domain": domain,
            "roomId": roomId,
            "token": token,
            "userId": userId,
            "userName": userName,
            "userIcon": icon,
            "roleIcon": "roleicon",
            "roleName": "计划员",
            "content": {
                "redbagId": redbagId
            }
        }
        websocket.send(JSON.stringify(msg));
    } else {
        console.log("连接状态为" + websocket.readyState + "");
    }

}

//请求房间
// $.ajax(ajaxRooms, {
//     data: {},
//     dataType: 'json', //服务器返回json格式数据
//     type: 'post', //HTTP请求类型
//     timeout: 10000, //超时时间设置为10秒；
//     success: function (data) {
//         console.log(data);
//         data.list.forEach(function (item, index) {
//             var oli = document.createElement('li');
//             oli.innerHTML = '<a onclick="room(' + item.id + ')">' + item.roomName + '</a>';
//             houseList.appendChild(oli);
//         })
//     },
//     error: function (xhr, type, errorThrown) {
//         //mui.toast('请求失败');
//     }
// });

function room(roomId) {
    document.getElementById("chatHouseBox").style.display = 'none';
    roomId = roomId;
    mask.style.display='none';
    connectSocket();
}



//上拉加载更多初始化
mui.init({
    pullRefresh: {
        container: '#pullrefresh',
        up: {
            contentrefresh: '正在加载...',
            contentnomore:'',
            callback: pullupRefresh
        }
    }
});

//上拉加载更多具体实现
function pullupRefresh() {
        if (token != undefined && token != null && token != ''&&userInfo) {
            if(iDisplayStart>1){
                setTimeout(function() {
                    receiveRedBagFun();
                }, 1000);
            }else{
                receiveRedBagFun();
            }
        }else{
            mui('#pullrefresh').pullRefresh().endPullupToRefresh(false);
        }

}

//以下为登录
function loginSubmit(){
    var $username=$("#loginPageUsername").val();
    var $password=$("#loginPagePassword").val();
    var userInfo;
    if($username==''){
        mui.toast("请输入用户名、手机号或者邮箱");
        return $("#loginPageUsername").focus();
    }else if($password==''){
        mui.toast("请输入密码");
        return $("#loginPagePassword").focus();
    }
    $.ajax(logined,{
        data:{
            username:$username,
            password:$password,
        },
        dataType:'json',//服务器返回json格式数据
        type:'post',//HTTP请求类型
        timeout:10000,//超时时间设置为10秒；
        success:function(data){
            console.log(data);
            if(data.success){
                mui.toast('登录成功');
                $t.cookie.set('user_info', JSON.stringify(data.result), 'd30');
                $t.cookie.set('chat_token', data.result.token, 'd30');

                $t.cookie.set('user_info', JSON.stringify(data.result), 'd30')
                setTimeout(function(){
                    location.reload();
                },1000)
            }else{
                mui.toast(data.errorDesc);
            }


        },
        error:function(xhr,type,errorThrown){

        }
    });
}

function pwdFun1(obj){
    obj.style.display='none';
    $("#passSee").css('display','block');
    document.getElementById("loginPagePassword").type='text';
}
function pwdFun2(obj){
    obj.style.display='none';
    $("#passHidden").css('display','block');
    document.getElementById("loginPagePassword").type='password';
}

//以下为我的红包
var packList = document.getElementById("packList");
var num = document.getElementById("num");
var total = document.getElementById("total");
var iDisplayStart=0;
var iDisplayLength=10;
function receiveRedBagFun(){
    $.ajax(ajaxReceiveRedBag, {
        data: {
            token: token,
            iDisplayStart:iDisplayStart,
            iDisplayLength:iDisplayLength
        },
        dataType: 'json',//服务器返回json格式数据
        type: 'post',//HTTP请求类型
        timeout: 10000,//超时时间设置为10秒；
        success: function (data) {
            console.log(data);
            var sum = 0
            if (data.result.data.length>0) {
                var pageSize = data.result.data.length;
                num.innerHTML =data.result.iTotalRecords;
                data.result.data.forEach(function (item, index) {
                    // sum += item.amount;
                    // total.innerHTML = sum;
                    var oli = document.createElement('li');
                    oli.className = 'flex spaceAround';
                    var createTime = item.createTime? new Date(item.createTime).Format("yyyy-MM-dd") : ''
                    oli.innerHTML = '<div class="packId">' + item.id + '</div>' +
                        '<div class="price">' + item.amount + '元</div>' +
                        '<div class="time">' + createTime + '</div>';
                    if (item.status == 1) {
                        oli.innerHTML += '<div class="status2">未兑换</div>';
                    } else if (item.status == 2) {
                        oli.innerHTML += '<div class="status2">已兑换</div>';
                    }
                    packList.appendChild(oli);
                    document.getElementById("isPack").style.display='block';
                })
                if(pageSize>=iDisplayLength){
                    iDisplayStart++;
                    mui('#pullrefresh').pullRefresh().endPullupToRefresh(false);
                }else{
                    if(iDisplayStart>1){
                        mui.toast('没有更多数据了')
                    }
                    mui('#pullrefresh').pullRefresh().endPullupToRefresh(true);
                }
            }else{
                if(iDisplayStart>1){
                    mui.toast('没有更多数据了')
                }
               document.getElementById("isPack").style.display='none';
                mui('#pullrefresh').pullRefresh().endPullupToRefresh(false);
            }
        },
        error: function (xhr, type, errorThrown) {
            mui.toast('请求失败');
        }
    });
}






//以下为用户信息
var userNameBox = document.getElementById("userNameBox1");
var roleNameBox = document.getElementById("roleNameBox");
var regTime = document.getElementById("regTime");
var loginIp = document.getElementById("loginIp");
var iconUser = document.getElementById("iconUser");

$.ajax(userInfoPort, {
    data: {
        token: token
    },
    dataType: 'json', //服务器返回json格式数据
    type: 'post', //HTTP请求类型
    timeout: 10000, //超时时间设置为10秒；
    success: function (data) {
        console.log(data);
        if (data.success) {

            userNameBox.innerHTML = data.result.userName;
            roleNameBox.innerHTML = data.result.roleName;
            regTime.innerHTML = new Date(data.result.createTime).Format("yyyy-MM-dd hh:mm:ss");
            loginIp.innerHTML = data.result.loginIp;

            icon = data.result.icon;
            iconUser.src = data.result.icon || '/resources/chat/m/images/userSet.png';
            document.getElementById("indexAvatar").src = data.result.icon || '/resources/chat/m/images/userSet.png';
            document.getElementById("imgPhoto").src = data.result.icon || '/resources/chat/m/images/userSet.png';
            if (data.result.icon == '随机') {
                iconUser.src = '/resources/chat/m/images/userSet.png';
                document.getElementById("indexAvatar").src = '/resources/chat/m/images/userSet.png';
                document.getElementById("imgPhoto").src = '/resources/chat/m/images/userSet.png';
            }
            var icon1 = data.result.icon;
            if (icon1.match(/userIcon.*?/g)) {
                icon1 = '/resources/chat/m/images/defalt/' + data.result.icon + '.png';
                iconUser.src = icon1;
                document.getElementById("indexAvatar").src = icon1;
                document.getElementById("imgPhoto").src = icon1;
            }
        } else {
           // mui.toast(data.errorDesc);
        }

    },
    error: function (xhr, type, errorThrown) {
    }
});


//以下为修改密码
var token, userName;
var userNameBox2 = $("#userNameBox2");
var oldPassword = document.getElementById("oldPassword");
var firstPassword = document.getElementById("firstPassword");
var secondPassword = document.getElementById("secondPassword");
if (token != undefined && token != null && token != ''&&userInfo&&userInfo!=null) {
    userName = userInfo.userName;
    userNameBox2.html(userName);
}

function submit() {
    if (oldPassword.value == '') {
        mui.toast("请输入原始密码");
        return $("#$oldPassword").focus();
    }
    if (firstPassword.value == '') {
        mui.toast("请输入新密码");
        return $("#$firstPassword").focus();
    } else {
        if (firstPassword.value.length < 6) {
            mui.toast('请输入至少6位密码！');
            return firstPassword.value = '';
        }
    }
    if (secondPassword.value == '') {
        mui.toast("请确认新密码");
        return $("#secondPassword").focus();
    } else {
        if (firstPassword.value != secondPassword.value) {
            mui.toast('两次密码不一致');
            return secondPassword.value = "";
        }
    }
    $.ajax(updatePassword, {
        data: {
            token: token,
            oldPassword: oldPassword.value,
            firstPassword: firstPassword.value,
            secondPassword: secondPassword.value,
        },
        dataType: 'json', //服务器返回json格式数据
        type: 'post', //HTTP请求类型
        timeout: 10000, //超时时间设置为10秒；
        success: function (data) {
            console.log(data);
            if (data.success) {
                mui.toast('修改密码成功');
                setTimeout(function () {
                    userPassClose();
                }, 1000)
            } else {
                mui.toast(data.errorDesc);
            }

        },
        error: function (xhr, type, errorThrown) {
            mui.toast('登录失败，请检查您填写的账号和密码');
        }
    });
}

function reset() {
    $("#oldPassword").val('');
    $("#firstPassword").val('');
    $("#secondPassword").val('');
}


//以下为修改头像部分
var userIconPhoto;

//相册
document.getElementById("picture").onchange = function (evt) {
    if (!window.FileReader)
        return;
    var files = evt.target.files;
    for (var i = 0, f; f = files[i]; i++) {
        if (!f.type.match('image.*')) {
            continue;
        }
        var reader = new FileReader();
        reader.onload = (function (theFile) {
            return function (e) {
                document.getElementById("imgPhoto").src = e.target.result;
            };
        })(f);
        reader.readAsDataURL(f);
    }
    var form = document.getElementById("myForm2");
    var formData = new FormData(form);
    formData.append("token", token);
    $.ajax(uploadForeverImg, {
        data: formData,
        type: 'post',//HTTP请求类型
        timeout: 10000,//超时时间设置为10秒；
        processData: false,  // 告诉jQuery不要去处理发送的数据
        contentType: false,
        success: function (data) {
            console.log(data);
            if (data.success) {
                userIconPhoto = data.result;
                updateIconFun();
            } else {
                mui.toast(data.errorDesc);
            }
        },
        error: function (xhr, type, errorThrown) {
        }
    });

}
//相机
document.getElementById("camera").onchange = function (evt) {
    if (!window.FileReader)
        return;
    var files = evt.target.files;
    for (var i = 0, f; f = files[i]; i++) {
        if (!f.type.match('image.*')) {
            continue;
        }
        var reader = new FileReader();
        reader.onload = (function (theFile) {
            return function (e) {
                document.getElementById("imgPhoto").src = e.target.result;
            };
        })(f);
        reader.readAsDataURL(f);
    }
    var form = document.getElementById("myForm1");
    var formData = new FormData(form);
    formData.append("token", token);
    $.ajax(uploadForeverImg, {
        data: formData,
        type: 'post',//HTTP请求类型
        timeout: 10000,
        processData: false,
        contentType: false,
        success: function (data) {
            console.log(data);
            if (data.success) {
                userIconPhoto = data.result;
                updateIconFun();
            } else {
                mui.toast(data.errorDesc);
            }

        },
        error: function (xhr, type, errorThrown) {
        }
    });

}

function updateIconFun() {
    $.ajax(updateIconPort, {
        data: {
            token: token,
            icon: userIconPhoto
        },
        dataType: 'json',
        type: 'post',
        timeout: 10000,
        success: function (data) {
            console.log(data);
            if (data.success) {
                mui.toast("头像上传成功");
                icon = userIconPhoto;
                if (userIconPhoto.match(/userIcon.*?/g)) {
                    document.getElementById('indexAvatar').src = '/resources/chat/m/images/defalt/' + userIconPhoto + '.png';
                    document.getElementById('iconUser').src = '/resources/chat/m/images/defalt/' + userIconPhoto + '.png';
                } else {
                    document.getElementById('indexAvatar').src = userIconPhoto
                    document.getElementById('iconUser').src = userIconPhoto;
                }

                setTimeout(function () {
                    userPhotoClose()();
                }, 1000)
            } else {
                mui.toast(data.errorDesc);
            }
        },
        error: function (xhr, type, errorThrown) {
        }
    });
}

$(".photoList li").click(function () {
    userIconPhoto = $(this).attr('class');
    document.getElementById("imgPhoto").src = '/resources/chat/m/images/defalt/' + userIconPhoto + '.png';
    updateIconFun();
})


//以下部分为监听光标的位置
function pasteHandler() {

    setTimeout(function () {

        var content = document.getElementById("testdiv").innerHTML;

        valiHTML = ["br"];

        //
        // if (!$.browser.mozilla) {
        //
        //     content = content.replace(/\r?\n/gi, "<br>");
        //
        // }

        document.getElementById("text").innerHTML = content;

    }, 1)

}

//锁定编辑器中鼠标光标位置。。

function _insertimg(str) {

    var selection = window.getSelection ? window.getSelection() : document.selection;

    var range = selection.createRange ? selection.createRange() : selection.getRangeAt(0);

    if (!window.getSelection) {

        document.getElementById('text').focus();

        var selection = window.getSelection ? window.getSelection() : document.selection;

        var range = selection.createRange ? selection.createRange() : selection.getRangeAt(0);

        range.pasteHTML(str);

        range.collapse(false);

        range.select();

    } else {

        document.getElementById('text').focus();

        range.collapse(false);

        var hasR = range.createContextualFragment(str);

        var hasR_lastChild = hasR.lastChild;

        while (hasR_lastChild && hasR_lastChild.nodeName.toLowerCase() == "br" && hasR_lastChild.previousSibling && hasR_lastChild.previousSibling.nodeName.toLowerCase() == "br") {

            var e = hasR_lastChild;

            hasR_lastChild = hasR_lastChild.previousSibling;

            hasR.removeChild(e)

        }

        range.insertNode(hasR);

        if (hasR_lastChild) {

            range.setEndAfter(hasR_lastChild);

            range.setStartAfter(hasR_lastChild)

        }

        selection.removeAllRanges();

        selection.addRange(range)

    }

}

//监控按enter键和空格键，如果按了enter键，则取消原事件，用<BR/ >代替。此处还等待修改！！！！！！如果后端能实现各个浏览器回车键产生的P，div, br的输出问题话就无需采用这段JS、

function enterkey() {

    e = event.keyCode;

    if (e == 13 || e == 32) {

        var keyCode = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;

        event.returnValue = false; // 取消此事件的默认操作

        if (document.selection && document.selection.createRange) {

            var myRange = document.selection.createRange();

            myRange.pasteHTML('<br />');

        } else if (window.getSelection) {

            var selection = window.getSelection();

            var range = window.getSelection().getRangeAt(0);

            range.deleteContents();

            var newP = document.createElement('br');

            range.insertNode(newP);

        }

        //alert(document.getElementById("testdiv").innerHTML)

    }

}

//此处必须防止在最下端。

var edt = document.getElementById("text");

if (edt.addEventListener) {

    edt.addEventListener("paste", pasteHandler, false);

} else {

    edt.attachEvent("onpaste", pasteHandler);

}

