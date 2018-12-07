$(function () {
    //初始化登陆状态
    initLoginStatus()
    //初始化禁言状态
    initForbidChat
    //初始化聊天室连接
    initChatRoom()
})

function initLoginStatus() {
    var userinfo = JSON.parse(new Tools().cookie.get('user_info'))

    window.uToken = $t.cookie.get('chat_token')
    if (userinfo) {
        if (userinfo.userIcon.indexOf('http://') != -1) {
            $('#nav-username').html('<img title="' + userinfo.userName + '" src="' + userinfo.userIcon + '"/>')
        } else {
            $('#nav-username').html('<img title="' + userinfo.userName + '" src="/resources/chat/pc/images/default/' + userinfo.userIcon + '.png"/>')
        }
        $('#login').hide()
        $('.nav-user-info').show()
    } else {
        $('.nav-user-info').hide()
        $('#login').show()
    }
}

function initForbidChat() {
    var user = $t.json($t.cookie.get('user_info'))

    if (user) {
        var forbidchat = false
        if (user.authority) {
            for (var i = 0, l = user.authority.length; i < l; i++) {
                if (user.authority[i] == 'chat:forbidchat') {
                    forbidchat = true
                }
            }
        }
        if (forbidchat == true) {
            $('#editor').attr('contenteditable', 'true')
            $('#editor').attr('title', '')
            $('#editor').css({
                background: 'transparent'
            })
        } else {
            $('#editor').attr('contenteditable', 'false')
            $('#editor').attr('title', '管理员已开启禁言')
            $('#editor').css({
                background: 'rgba(225,225,225,0.8)'
            })
        }

    } else {
        $('#editor').attr('contenteditable', 'false')
        $('#editor').attr('title', '管理员已开启禁言')
        $('#editor').css({
            background: 'rgba(225,225,225,0.8)'
        })
    }

}

function initChatRoom() {
    if (window.chatRoom) {
        window.chatRoom.connecton.close()
        window.chatRoom = null
    }
    $.post(base_url + '/chat/webSocketUrl/', {roomId: NeedInfo.roomId})
        .success(function (res) {
            if (res.success) {
                window.chatRoom = new ChatRoom(res.result, protocol, ws_options)
            } else {
                layer.alert('聊天室初始化失败')
                document.write('')
            }
        })
        .error(function () {
            layer.alert('聊天室初始化失败', function () {
                document.write('')
            })
        })
}

function getTpl(data) {
    var type = null
    var userCookie = JSON.parse(new Tools().cookie.get('user_info'))
    if (userCookie) {
        if (userCookie.userId != data.userId) {
            type = 'other'
        } else {
            type = 'owen'
        }
    } else {
        type = 'other'
    }

    if (new Tools().type(data.content) == 'Object') {
        type = 'redbag'
    }

    switch (type) {
        case 'other':
            var otherMsg = [
                '<div id="' + data.messageId + '" class="msg-item  ' + data.messageId + '">',
                '<div class="msg-user-icon">',
            ]
            var basePath = '/resources/chat/pc/images/default/'
            var userIcon = ''
            //判断头像信息
            if (/^userIcon\w{1,2}$/.test(data.userIcon)) {
                userIcon = '<img data-uid="' + data.userId + '" data-username="' + data.userName + '" oncontextmenu="messageContentContextMenu(this)" src="' + basePath + data.userIcon + '.png">'
            } else {
                userIcon = '<img data-uid="' + data.userId + '" data-username="' + data.userName + '" oncontextmenu="messageContentContextMenu(this)" src="' + data.userIcon + '">'
            }
            //插入otherMsgMoban
            otherMsg.push(userIcon)
            otherMsg.push('</div>')
            otherMsg.push('<div class="msg-info other">')
            //发送时间
            var userInfo = '<p class="msg-detail"><span class="user-name">' + data.userName + '&nbsp;</span> <span class="time">' + data.messageTime + '</span></p>'
            otherMsg.push(userInfo)
            otherMsg.push('<p class="msg-content" oncontextmenu="reCallOtherContextMenu(\'' + data.messageId + '\')">')
            otherMsg.push(transContent(data.content))
            otherMsg.push('</p>')
            otherMsg.push('</div></div>')
            return otherMsg.join('')
        case 'redbag':
            var tpl = [
                '<div id="' + data.messageId + '" class="msg-item" >',
                '<div class="msg-user-icon other">',
            ]
            var basePath = '/resources/chat/pc/images/default/'
            var userIcon = ''
            //判断头像信息
            if (/^userIcon\w{1,2}$/.test(data.userIcon)) {
                userIcon = '<img data-uid="' + data.userId + '" data-username="' + data.userName + '" oncontextmenu="messageContentContextMenu(this)"  src="' + basePath + data.userIcon + '.png">'
            } else {
                userIcon = '<img data-uid="' + data.userId + '" data-username="' + data.userName + '" oncontextmenu="messageContentContextMenu(this)"  src="' + data.userIcon + '">'
            }
            tpl.push(userIcon)
            tpl.push('</div>')
            tpl.push('<div class="msg-info"><p class="msg-detail"><span class="user-name">')
            tpl.push(data.userName)
            tpl.push('&nbsp;</span><span class="time">')
            tpl.push(data.messageTime)
            tpl.push('</span></p><p onclick="getRedBag(' + data.content.redbagId + ',\'' + data.userName + '\',\'' + parseUserIcon(data.userIcon) + '\')" class="red-pack">')
            tpl.push('<span class="user-name">')
            tpl.push(data.userName + '的红包')
            tpl.push('</span><button  class="get-red-pack">领取红包</button>')
            tpl.push('<span class="chat-room-red-pack">聊天室红包</span>')
            tpl.push('</p></div></div>')
            return tpl.join('')
        case 'owen':
            //判断头像信息
            var basePath = '/resources/chat/pc/images/default/'
            var userIcon = ''
            if (/^userIcon\w{1,2}$/.test(data.userIcon)) {
                userIcon = '<img src="' + basePath + data.userIcon + '.png">'
            } else {
                userIcon = '<img src="' + data.userIcon + '">'
            }
            var tpl = '<div id="' + data.messageId + '" class="msg-item ' + data.messageId + '">' +
                '    <div class="msg-user-icon owen-msg">' + userIcon +
                '    </div>' +
                '    <div class="msg-info owen-msg">' +
                '        <p class="msg-detail">' +
                '            <span class="user-name">' + data.userName + '</span>' +
                '            <span class="time"> ' + data.messageTime + '</span></p>' +
                '        <p class="msg-content" oncontextmenu="reCallContextMenu(\'' + data.messageId + '\')">' +
                transContent(data.content) +
                '        </p>' +
                '    </div>' +
                '</div>'
            return tpl
    }


    if (userCookie) {
        if (userCookie.userId !== data.userId) {
            return otherMsg.join('')
        }
    }
}

function transContent(data) {

    var content = data
    content = content.replace(/\&lt;/ig, '<')
    content = content.replace(/\&gt;/ig, '>')
    content = content.replace(/\&quot\;/ig, '\"')
    content = content.replace(/\&amp\;/ig, '&')

    return content
}

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
    var user = $t.json($t.cookie.get('user_info'))
    if (!user) {
        return null
    }
    //循环信息
    for (var i = 0, l = atList.length; i < l; i++) {
        if (atList[i] == user.userName) {
            if (user.userId !== msgInfo.userId) {
                $('#at-msg-tips').attr('href', '#' + msgInfo.messageId).html(msgInfo.userName + '@了你').css('transform',' translate3d(0, 0, 0)')
            }
        }
    }
}

function clearChatRoomAll() {
    clearInterval(window.reconnectInterval)
    clearInterval(window.checkInterval)
    clearInterval(window.heartBeatInterval)
}

//协议事件
var protocol = {
    S_DOMAIN_ERROR: function () {
        layer.alert('服务错误')
        clearChatRoomAll()
    },
    S_CONNECT_ERROR: function () {
        layer.alert('连接信息信息错误')
        clearChatRoomAll()
    },
    //加入房间
    S_JOIN_ROOM: function () {
        var html = '<p class="join-room-user-item"><img src="/resources/chat/pc/images/wellcom.png" width="auto" height="25px" > 欢迎' + this.data.userName + '加入聊天室 </p>'
        var dom = $(html)
        $('#middle-join-room-container').append(dom)
        setTimeout(function () {
            dom.addClass('move-to-hide')
            dom.on('animationend', function () {
                dom.remove()
            })
        }, 3500)
        //更新人数显示
        $oNum = $('#online-num')
        $oNum.text(parseInt($oNum.text()) + 1)
        //如果id不存在则是游客
        if (!this.data.userId) {
            var html = '<div id="' + this.data.userId +
                '" class="item" data-username="' + this.data.userName +
                '" data-uid="' + this.data.userId +
                '" oncontextmenu="slideBarRightContextMenu(this)"><img class="user-icon" src="' +
                parseUserIcon(this.data.userIcon) + '"><span class="user-name">' +
                this.data.userName + '</span><span class="user-level user-icon-nomal"></span></div>'
            $('.user-list').append(html)
        } else { //登陆用户
            //拼接登陆加入用户html
            var joinUserHtml = ''
            if (/^(http|https):\/\/.*/i.test(this.data.roleIcon)) {
                joinUserHtml = '<div id="' + this.data.userId +
                    '" class="item" data-username="' + this.data.userName +
                    '" data-uid="' + this.data.userId +
                    '" oncontextmenu="slideBarRightContextMenu(this)"><img class="user-icon" src="' +
                    parseUserIcon(this.data.userIcon) + '"><span class="user-name">' +
                    this.data.userName + '</span><span class="user-level "><img src="' + this.data.roleIcon + '" alt=""></span></div>'
            } else {
                joinUserHtml = '<div id="' + this.data.userId +
                    '" class="item" data-username="' + this.data.userName +
                    '" data-uid="' + this.data.userId +
                    '" oncontextmenu="slideBarRightContextMenu(this)"><img class="user-icon" src="' +
                    parseUserIcon(this.data.userIcon) + '"><span class="user-name">' +
                    this.data.userName + '</span><span class="user-level ' + this.data.roleIcon + '"></span></div>'
            }
            //左边在线列表已经存在则不处理
            if ($('.user-list').find('#' + this.data.userId).length > 0) {

                return null
            }
            //如果有权限则，插入到前面
            var nomallList = []
            var adminList = []
            $('.user-list .item').each(function () {
                var base = '<div id="' + $(this).attr('id') +
                    '" class="item" data-username="' + $(this).attr('data-username') +
                    '" data-uid="' + $(this).attr('data-uid') + '" oncontextmenu="slideBarRightContextMenu(this)">'
                if ($(this).find('.user-icon-nomal').length > 0) {
                    nomallList.push(base + $(this).html() + '</div>')
                } else {
                    adminList.push(base + $(this).html() + '</div>')
                }
            })
            if (this.data.roleIcon != 'user-icon-nomal') {
                adminList.push(joinUserHtml)
            } else {
                nomalHtml.unshift(joinUserHtml)
            }
            var adminHtml = adminList.join('')
            var nomalHtml = nomallList.join('')
            var userListHtml = adminHtml + nomalHtml
            $('.user-list').html(userListHtml)
        }
    },
    //发送消息
    S_MESSAGE: function () {
        var data = getTpl(this.data)
        //去重
        if ($('.msg-container').find('#' + this.data.messageId).length > 0) {
            return null
        }

        $('.middle-container .msg-container').append($(getTpl(this.data)))
        showNotification(this.data)
        if (NeedInfo.autoScroll) {
            $('.middle-container .msg-container')[0].scrollTop = $('.middle-container .msg-container')[0].scrollHeight;
        }
        if (NeedInfo.showHaveMoreMessage == true) {
            $('#new-msg-count').text(parseInt($('#new-msg-count').text()) + 1)
            $('.have-more-message').show()
        } else {
            $('#new-msg-count').text('0')
            $('.have-more-message').hide()
        }
        doAtHandler(data, this.data)
    },
    //离开房间
    S_OUT_CHAT: function () {
        var html = '<p class="join-room-user-item"> ' + this.data.userName + '离开聊天室 </p>'
        var dom = $(html)
        $('#middle-join-room-container').append(dom)
        setTimeout(function () {
            dom.addClass('move-to-hide')
            dom.on('animationend', function () {
                dom.remove()
            })
            $('#online-num').html(parseInt($('#online-num')) - 1)
        }, 3500)
    },
    S_IP_BLACK: function () {
        layer.alert('ip已加入黑名单，请联系客服')
        clearChatRoomAll()
    },
    //ip错误
    S_IP_ERROR: function () {
        layer.alert('游客该ip已在别处登陆')
        clearChatRoomAll()
    },
    //登陆信息错误
    S_LOGIN_ERROR: function () {
        layer.alert('登录信息错误')
        clearChatRoomAll()
    },
    //踢出聊天室
    S_REMOVE_CHAT: function () {
        layer.alert('你已被踢出聊天室！')
        clearChatRoomAll()
        //关闭连接
        chatRoom.connecton.close()
        //清除登陆信息
        $t.cookie.del('user_info')
    },
    //当前不在聊天室房间内
    S_NOT_IN_ROOM: function () {
        layer.alert('当前不在聊天室房间内')
    },
    //禁言
    S_FORBID_CHAT: function () {
        var user = $t.json($t.cookie.get('user_info'))
        if (user.authority) {
            for (var i = 0, l = user.authority.length; i < l; i++) {
                if (user.authority[i] == 'chat:forbidchat') {
                    return null
                }
            }
        }
        layer.msg('管理员已开启禁言')
        NeedInfo.forbidChat = true
        $('#editor').attr('contenteditable', 'false')
        $('#editor').attr('title', '管理员已开启禁言')
        $('#editor').css({
            background: 'rgba(225,225,225,0.8)'
        })
    },
    //取消禁言
    S_UNFORBID_CHAT: function () {
        layer.msg('管理员已关闭禁言')
        NeedInfo.forbidChat = false
        $('#editor').attr('contenteditable', 'true')
        $('#editor').attr('title', '')
        $('#editor').css({
            background: 'transparent'
        })
    },
    //历史消息
    S_HISTORY_CHAT: function () {
        //先清除一遍聊天室内容
        var tmp = []
        for (var i = 0, l = this.data.content.length; i < l; i++) {
            tmp.unshift(this.data.content[i])
        }
        for (var i = 0, l = tmp.length; i < l; i++) {
            var data = JSON.parse(tmp[i])
            $('.middle-container .msg-container').append($(getTpl(data)))
        }
        $('.middle-container .msg-container')[0].scrollTop = $('.middle-container .msg-container')[0].scrollHeight;
    },
    //撤回消息
    S_RECALL_MESSAGE: function () {
        if (this.data.userId == $t.json($t.cookie.get('user_info')).userId) {
            return null
        }
        var $d = $('.msg-container').find('.' + this.data.messageId)
        $d.html('<p class="re-call-msg"><span class="re-call-text">' + this.data.userName + '撤回了一条消息</span></p>')
    },
    //登录信息失效
    S_LOGIN_INFO_LOSE: function () {
        clearChatRoomAll()
        if (new Tools().cookie.get('user_info')) {
            layer.alert('登录信息失效', function () {
                $('.nav-user-info').hide()
                $('.login').show()
                new Tools().cookie.del('user_info')
                layer.closeAll()
                setTimeout(function () {
                    $('.login-modal-mask').css('display', 'flex')
                }, 20)
            })
        }
    },
    //心跳检测
    S_HEART_BEAT: function () {
    },
    //发红包
    S_SEND_REDBAG: function () {
        if ($('.middle-content').find('#' + this.data.messageId) > 0) {
            return null
        }
        $('.middle-container .msg-container').append($(getTpl(this.data)))
        $('.middle-container .msg-container')[0].scrollTop = $('.middle-container .msg-container')[0].scrollHeight;
    },
    S_RECEIVE_REDBAG_ERROR: function () {
        layer.alert('红包领取出错')
    },
    //领取红包
    S_RECEIVE_REDBAG: function () {
        var userInfo = JSON.parse($t.cookie.get('user_info'))
        if (!userInfo) {
            return null
        }
        if (userInfo.userId != this.data.userId) {
            return null
        }

        if (this.data.userIcon.indexOf('http://') == -1) {
            $('.redbag-modal-container-give-user-info > img').attr('src', '/resources/chat/pc/images/default/' + this.data.content.sendIcon + '.png')
        } else {
            $('.redbag-modal-container-give-user-info > img').attr('src', this.data.content.sendIcon)
        }
        $('.redbag-modal-container-give-user-info > span').html(this.data.content.sendName + '&nbsp;的红包')
        $('.redbag-modal-container-get-amount').html('￥' + this.data.content.amount)
        $('.redbag-modal-mask').css('display', 'flex')
    },
    //红包已领取完
    S_REDBAG_OUT: function () {

        var userInfo = $t.json($t.storage.get('sendRedBagUserName'))

        if (!userInfo) {
            return null
        }

        $('.redbag-empty-modal-container-give-user-info > img').attr('src', userInfo.icon)
        $('.redbag-empty-modal-container-give-user-info > span').html(userInfo.user + '&nbsp;的红包')

        $('.redbag-empty-modal-mask').css('display', 'flex')
    },
    //中奖喜讯
    S_LOTTERY_GOODNEWS: function () {
        $('.price-info').html(this.data.content)
        $('.price-modal-mask').css('display', 'flex')
    },
    //房间错误
    S_ROOM_ERROR: function () {
        layer.alert('房间信息错误')
        clearChatRoomAll()
    },
    //已在另外设备登录
    S_LOGIN_ANOTHER: function () {
        clearChatRoomAll()
        layer.alert('已在另外设备登录')
        //清除cookie
        $t.cookie.del('user_info')
        initLoginStatus()
    }
}
