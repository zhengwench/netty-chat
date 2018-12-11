$(function () {
    $('#seve-to-desktop').click(function () {
        var e = e || event
        e.preventDefault()
        var uri = '//' + (window.location.host + '/chat/saveDeskTop/?title=' + document.title + '&url=' + encodeURIComponent(window.location.href))
        console.log(uri)
        window.location.href = uri
    })
    //生成二维码
    var qrcode = new QRCode(document.querySelector('#qrcode'), {
        text: window.location.href,
        width: 120,
        height: 120,
        colorDark: '#000000',
        colorLight: '#ffffff',
        correctLevel: QRCode.CorrectLevel.H
    })
    $('.mobile-chat').hover()
    //初始化编辑器
    $('.editor').DDeditor();
    //用户列表
    $.post(base_url + '/chat/onlineUser', {roomId: NeedInfo.roomId})
        .success(function (res) {
            if (res.success) {
                $('#online-num').html(res.result.count)
                var html = []
                var list = res.result.list
                for (var i = 0, l = list.length; i < l; i++) {
                    var user = list[i],
                        id = !!user.id ? user.id : 'null',
                        name = user.userName,
                        userIcon = !user.userIcon ? parseUserIcon('userIcon' + Math.ceil(Math.random() * 24)) : parseUserIcon(user.userIcon),
                        tpl = '<div id="' + id + '" class="item" data-username="' + user.userName + '" data-uid="' + id + '" oncontextmenu="slideBarRightContextMenu(this)">' +
                            '<img class="user-icon" src="' + userIcon + '">' +
                            '<span class="user-name">' + name + '</span>'
                    if (user.roleIcon) {
                        if (user.roleIcon.indexOf('http://') != -1) {
                            tpl += '<span class="user-level"><img src="' + user.roleIcon + '" ></span></div>'
                        } else {
                            tpl += '<span class="user-level ' + user.roleIcon + '"></span></div>'
                        }
                    } else {
                        tpl += '<span class="user-level user-icon-nomal"></span></div>'
                    }
                    html.push(tpl)
                }
                $t.storage.set('userlist', JSON.stringify(html))
                $('.user-list').html(html.join(''))
            }
        })
    //在线用户搜索
    $('#search-input').on("input propertychange", searchOnlineUser);
    //在线用户搜索搜索按钮
    $('.left-sidebar-search >img').click(searchOnlineUser)
    //切换房间
    $('.left-sidebar-title > img').click(function () {
        setTimeout(function () {
            $('.select-room-container ').toggle()
        }, 10)
    })
    /*//选择房间
    $('.select-room-container > .room-item ').each(function () {
        $(this).click(function () {
            if (NeedInfo.roomId == $(this).attr('data-roomId')) {
                return
            }
            NeedInfo.roomId = $(this).attr('data-roomId')
            window.chatRoom.connecton.close()
            initChatRoom()
        })
    })*/
    //清屏功能
    $('#clear-content').click(function () {
        $('.msg-container').html('')
    })
    //聊天窗口滚动事件
    $('.msg-container').on('mousewheel', function () {
        var e = e || event
        NeedInfo.autoScroll = false
        NeedInfo.showHaveMoreMessage = true
    })
    $('.msg-container').scroll(function () {

    })
    $('.have-more-message').click(function () {
        NeedInfo.autoScroll = true
        NeedInfo.showHaveMoreMessage = false
        $('#new-msg-count').text('0')
        $(this).hide()
        $('.middle-container .msg-container')[0].scrollTop = $('.middle-container .msg-container')[0].scrollHeight;
    })
    //换肤
    var themeFlag = 0
    $('#change-theme').click(function () {
        var theme = [1, 2]
        if (themeFlag > theme.length - 1) {
            themeFlag = 0
        }
        $('.container').css({
            'background-image': 'url("/resources/chat/pc/images/main-bg' + theme[themeFlag] + '.jpg")',
            'background-size': '100% 100%'
        })
        themeFlag += 1
    })
    //设置循环滚动
    $(".proclamation").scrollForever();
    //设置modal关闭
    $('.price-modal-mask-close-btn').click(function () {
        $('.price-modal-mask').fadeOut(200)
    })
    //登陆
    $('#login').click(function (e) {
        e.stopPropagation()
        $('.login-modal-mask').css('display', 'flex')
    })
    //登陆框阻止冒泡
    $('.login-form-container').click(function (e) {
        e.stopPropagation()
    })
    //登陆请求
    $('#login-form').submit(function () {
        $username = $('#login-form input[type=text]')
        $password = $('#login-form input[type=password]')
        if (!$username.val()) {
            $('#username').css(
                {
                    boxShadow: "0 0 3px #f00",
                }
            )
            $username.focus()
            return
        }
        $username.keydown(function () {
            $('#username').css(
                {
                    boxShadow: "none"
                }
            )
        })
        if (!$password.val()) {
            $('#password').css(
                {
                    boxShadow: "0 0 3px #f00",
                }
            )
            $password.focus()
            return
        }
        $password.keydown(function () {
            $('#password').css(
                {
                    boxShadow: "none"
                }
            )
        })
        var index = layer.load(2, {
            shade: [0.1, '#fff'] //0.1透明度的白色背景
        });
        //start login
        $.post(base_url + '/chat/logined', $('#login-form').serialize())
            .success(function (res) {
                if (res.success) {
                    $t.cookie.set('user_info', JSON.stringify(res.result), 'd30')
                    $t.cookie.set('chat_token', res.result.token, 'd30')
                    window.uToken = res.result.token
                    if (res.result.userIcon.indexOf('http://') != -1) {
                        $('#nav-username').html('<img title="' + res.result.userName + '" src="' + res.result.userIcon
                            + '"/>')
                    } else {
                        $('#nav-username').html('<img title="' + res.result.userName +
                            '" src="/resources/chat/pc/images/default/' + res.result.userIcon + '.png"/>')
                    }
                    window.location.reload()
                } else {
                    layer.msg(res.errorDesc)
                    $('#login-form')[0].reset()
                }
            })
            .done(function (res) {
                layer.close(index)
            })
    })
    //关闭登陆
    $('.login-form-container-close-btn').click(function () {
        $('.login-modal-mask').fadeOut(200)
    })
    //tabbar切换
    $('.tab-bar > a').each(function (i, d) {
        $(this).click(function (e) {
            e.preventDefault()
            $('#iframe').attr('src', $(this).attr('href'))
        })
    })
    //注销
    $('#nav-logout').click(function () {
        layer.confirm('要退出吗？', {}, function () {
            $t.cookie.del('user_info')
            $t.cookie.del('chat_token')
            clearInterval(window.checkInterval)
            clearInterval(window.heartBeatInterval)
            chatRoom.connecton.close()
            //window.location.reload()
            $('.nav-user-info').hide()
            $('.login').show()
            layer.closeAll()
        }, function () {
            return true
        })
    })
    //阻止领取红包冒泡
    $('.redbag-modal-container').click(function (e) {
        e.stopPropagation()
    })
    //关闭收到红包
    $('.redbag-modal-mask-close-btn').click(function () {
        $('.redbag-modal-mask').fadeOut(200)
    })
    //
    $('.my-redbag-container').click(function () {
        var e = e || event
        e.stopPropagation()
    })
    //查看我的红包
    $('.redbag-modal-container-read-my-redbag > a').click(function () {
        var e = e || event
        e.preventDefault()
        $('.redbag-modal-mask').hide()
        $('.my-redbag-modal-mask').css('display', 'flex')
        getRedBagList()
    })
    $('#show-my-redbag').click(function () {
        if (!$t.cookie.get('user_info')) {
            layer.alert('登陆后才能领取红包哦~', function () {
                layer.closeAll()
                setTimeout(function () {
                    $('.login-modal-mask').css('display', 'flex')
                }, 20)
            })
            return null
        }

        getRedBagList()

        setTimeout(function () {
            $('.my-redbag-modal-mask').css('display', 'flex')
        }, 10)

    })
    //关闭查看我的红包
    $('.my-redbag-close-btn').click(function () {
        $('.my-redbag-modal-mask').fadeOut(200)
    })
    //发送消息
    $('.msg-send-btn').click(function () {
        NeedInfo.autoScroll = true
        if (!$t.cookie.get('user_info')) {
            layer.alert('登陆后才能参与聊天哦~', function () {
                layer.closeAll()
                setTimeout(function () {
                    $('.login-modal-mask').css('display', 'flex')
                }, 20)
            })
            return null
        }
    })
    //发送图片
    $('#sendImg').click(function () {
        if (!$t.cookie.get('user_info')) {
            layer.alert('登陆后才能参与聊天哦~', function () {
                layer.closeAll()
                setTimeout(function () {
                    $('.login-modal-mask').css('display', 'flex')
                }, 20)
            })
            return null
        }
    })
    //打开个人中心
    $('#nav-username').click(function () {
        var e = e || event
        var user = $t.json($t.cookie.get('user_info'))
        e.stopPropagation()
        e.preventDefault()
        $('#modify-user-icon').attr('src', $(this).find('img').attr('src'))

        $.post(base_url + '/chat/userInfo', {token: uToken})
            .success(function (res) {
                if (res.success) {
                    $('#modify-user-icon').attr('src', parseUserIcon(res.result.icon))
                    $('#modify-show-username').html('用户名：' + res.result.userName)
                    $('#modify-show-user-ip').html('登陆IP：' + res.result.loginIp)
                    $('#modify-show-user-register-time').html('注册时间：' + transTime(res.result.createTime))

                    $('.person-center').css('display', 'flex')
                }
            })
        $('.user-icon-logo').show()
        $('.modify-user-info').hide()
        $('#modify-form')[0].reset()
    })
    //点击头像显示默认头像
    $('#modify-user-icon-edit').click(function () {
        $('.user-icon-logo').show()
        $('.modify-user-info').hide()
        var oContainer = $('.person-center .person-center-container')
        oContainer.css('height', '425px')
    })
    //阻止冒泡
    $('.person-center-container').click(function () {
        var e = e || event
        e.stopPropagation()
    })
    //切换到修改密码页
    $('.modify-password-tab-btn').click(function () {
        var oContainer = $('.person-center .person-center-container')
        oContainer.css('height', '322px')
        $('.user-icon-logo').hide()
        $('.modify-user-info').show()
        $('#input-token').val(uToken)
    })
    //切换
    $('#modify-user-info-close').click(function () {
        $('.person-center .person-center-container').css('height', '90px')
        $('#modify-form')[0].reset()
    })
    //修改密码
    $('#modify-form').submit(function () {
        $.post(base_url + '/chat/updatePassword', $(this).serialize())
            .success(function (res) {
                if (res.success) {
                    layer.msg('密码修改成功~')
                    $('#modify-form')[0].reset()
                } else {
                    layer.msg(res.errorDesc)
                }
            })
    })
    //发送消息
    $('.msg-send-btn').click(sendMessageHandler)
    //查看更多红包
    $('.get-more-bag-list-btn').click(function () {

        //获取用户信息
        var user = $t.json($t.cookie.get('user_info'))

        if (currentPage > totalPage - 1) {
            //判断是否有更多了
            if ($('#red-bag-list-container').find('.not-more').length > 0) {
                return null
            }
            var html = $('#red-bag-list-container').html()
            $('#red-bag-list-container').html(html + '<div class="my-redbag-table-item not-more">没有更多了</div>')
            return ''
        }
        $.post(base_url + '/chat/ajaxReceiveRedBag', {
            token: uToken,
            iDisplayStart: currentPage * 5,
            iDisplayLength: 5
        })
            .success(function (res) {
                data = res.result.data
                //存储已存在的消息
                var html = $('#red-bag-list-container').html()
                var list = [html]
                if (res.success) {
                    for (var i = 0, l = data.length; i < l; i++) {
                        var status = ''
                        if (data[i].status == 1) {
                            status = '未兑换'
                        } else if (data[i].status == 2) {
                            status = '已兑换'
                        } else {
                            status = '已过期'
                        }
                        var html = '<div class="my-redbag-table-item">\n' +
                            '           <div class="id">' + data[i].id + '</div>\n' +
                            '           <div class="price">' + data[i].amount + '</div>\n' +
                            '           <div class="useTime">' + transTime(data[i].createTime) + '</div>\n' +
                            '           <div class="status ' + (status == 3 ? 'pass' : ' ') + '">' + status + '</div>\n' +
                            '        </div>'
                        list.push(html)
                    }
                    $('#red-bag-list-container').html(list.join(''))
                    currentPage += 1
                }
            })
    })
    //编辑器绑定粘贴图片事件
    $('#editor').on('paste', function (event) {
        var isChrome = false;
        if (event.clipboardData || event.originalEvent) {
            //某些chrome版本使用的是event.originalEvent
            var clipboardData = (event.clipboardData || event.originalEvent.clipboardData);
            if (clipboardData.items) {
                // for chrome
                var items = clipboardData.items,
                    len = items.length,
                    blob = null;
                isChrome = true;
                for (var i = 0; i < len; i++) {
                    if (items[i].type.indexOf("image") !== -1) {
                        //getAsFile() 此方法只是living standard firefox ie11 并不支持
                        blob = items[i].getAsFile();
                    }
                }
                if (blob !== null) {
                    var blobUrl = URL.createObjectURL(blob);
                    //blob对象显示
                    /*document.getElementById("imgNode").src=blobUrl;*/
                    var reader = new FileReader()
                    reader.readAsDataURL(blob);
                    //base64码显示
                    reader.onload = function (event) {
                        // event.target.result 即为图片的Base64编码字符串
                        var base64_str = event.target.result;
                        compress(base64_str, 0.6, function (res) {
                            var fd = new FormData();
                            fd.append("base64Data", res);
                            fd.append('token', uToken)
                            //ajax提交数据
                            $.ajax({
                                url: base_url + "/chat/uploadBaseImg",
                                type: 'POST',
                                cache: false,
                                data: fd,
                                processData: false,
                                contentType: false,
                                dataType: "json",
                                success: function (res) {
                                    var html = ''
                                    if (res.success) {
                                        if ($('#editor').text() == '输入聊天内容，@TA') {
                                            html = ''
                                        } else {
                                            html = $('#editor').html()
                                        }
                                        var img = '<img class="uploadImg" onclick="showBigImg(this)" src="' + res.result + '">'
                                        $('#editor').html(html + img)
                                    }
                                }
                            });
                        })
                    }
                }
            }
        }
    })
    //确定修改头像
    $('#modify-user-icon-btn').click(function () {
        if (!window.chooseUserIcontype == null) {
            layer.msg('请选择头像')
            return null
        }
        //获取登陆信息
        var user_info = $t.json($t.cookie.get('user_info'))
        layer.load()
        if (window.choseUserIcontype == 1) {
            var icon = $('#modify-user-icon').attr('data-usericon')
            $.post(base_url + '/chat/updateIcon', {token: uToken, icon: icon})
                .success(function (res) {
                    if (res.success) {
                        user_info.userIcon = icon
                        new Tools().cookie.set('user_info', JSON.stringify(user_info))
                        $('#modify-user-icon').attr('src', parseUserIcon(icon))
                        $('#nav-username > img').attr('src', parseUserIcon(icon))
                        layer.msg('修改成功')
                    }
                })
                .done(function () {
                    layer.closeAll()
                })
        }
        if (window.choseUserIcontype == 2) {
            //创建数据对象
            var fd = new FormData(document.querySelector('#userIconForm'))
            fd.append('token', uToken)
            $.ajax({
                url: base_url + '/chat/uploadForeverImg',
                method: 'post',
                data: fd,
                processData: false,
                contentType: false,
            })
                .success(function (res) {
                    var userIcon = res.result
                    if (res.success) {
                        $.post(base_url + '/chat/updateIcon', {token: uToken, icon: res.result})
                            .success(function (res) {
                                if (res.success) {
                                    //更新cookie
                                    user_info.userIcon = userIcon
                                    $t.cookie.set('user_info', JSON.stringify(user_info))
                                    //更新头像信息
                                    $('#modify-user-icon').attr('src', userIcon)
                                    $('#nav-username > img').attr('src', userIcon)
                                    layer.msg('修改成功')
                                }
                            })
                    }
                })
                .done(function () {
                    layer.closeAll()
                })
        }
    })
    //关闭@提示
    $('#at-msg-tips').click(function () {
        $(this).css('transform', 'translate3d(150%, 0, 0)')
    })
    //scroll-bottom
    $('#scroll-bottom').click(function () {
        NeedInfo.autoScroll = true
        $('.msg-container')[0].scrollTop = $('.msg-container')[0].scrollHeight
    })
    //tabbar增加类
    $('.tab-bar > a').each(function (i, m) {
        if (i == 0) {
            $(m).addClass('active')
        }
        $(m).click(function () {
            $(this).addClass('active').siblings().removeClass('active')
        })
    })
});

var currentPage = 1
var totalPage = 0

//图片压缩
function compress(base64data, quality, cb) {
    var oImg = new Image()
    oImg.src = base64data
    oImg.onload = function (ev) {
        var canvas = document.createElement("canvas"),
            width = oImg.width,
            height = oImg.height;
        canvas.width = width;
        canvas.height = height;
        canvas.getContext("2d").drawImage(oImg, 0, 0, width, height); //将图片绘制到canvas中
        dataURL = canvas.toDataURL('image/jpeg', quality); //转换图片为dataURL
        cb(dataURL)
    }
}

//获取红包列表
function getRedBagList() {
    var userInfo = $t.json($t.cookie.get('user_info'))
    if (totalPage > 0) {
        return null
    }
    $.post(base_url + '/chat/ajaxReceiveRedBag', {token: uToken, iDisplayLength: 5})
        .success(function (res) {
            data = res.result.data
            var list = []
            if (res.success) {
                for (var i = 0, l = data.length; i < l; i++) {
                    var status = ''
                    if (data[i].status == 1) {
                        status = '未兑换'
                    } else if (data[i].status == 2) {
                        status = '已兑换'
                    } else {
                        status = '已过期'
                    }
                    var html = '<div class="my-redbag-table-item">\n' +
                        '           <div class="id">' + data[i].id + '</div>\n' +
                        '           <div class="price">' + data[i].amount + '</div>\n' +
                        '           <div class="useTime">' + transTime(data[i].createTime) + '</div>\n' +
                        '           <div class="status ' + (status == 3 ? 'pass' : ' ') + '">' + status + '</div>\n' +
                        '        </div>'
                    list.push(html)
                }
                $('#red-bag-list-container').html(list.join(''))
                $('#surplus').html(res.result.iTotalRecords)
                totalPage = Math.ceil(res.result.iTotalDisplayRecords / res.result.iDisplayLength)
            }
        })

}

$('#sendImgBtn').click(function () {
    //获取登陆信息
    var user = $t.json($t.cookie.get('user_info'))
    if (!user) {
        layer.alert('登陆后才能聊天哦~', function () {
            layer.closeAll()
            setTimeout(function () {
                $('.login-modal-mask').css('display', 'flex')
            }, 20)
        })
        return false
    }
    //用户权限
    var forbidchat = false
    if (user.authority) {
        for (var i = 0, l = user.authority.length; i < l; i++) {
            if (user.authority[i] == 'chat:forbidchat') {
                forbidchat = true
            }
        }
    }
    if (forbidchat == false && NeedInfo.forbidChat == 1) {
        layer.msg('聊天室已禁言')
        return false
    }
})

//转换时间
function transTime(timestamp) {
    var dateObj = new Date(timestamp),
        year = dateObj.getFullYear(),
        mouth = dateObj.getMonth() + 1 >= 10 ? dateObj.getMonth() + 1 : '0' + (dateObj.getMonth() + 1),
        date = (dateObj.getDate() >= 10 ? dateObj.getDate() : '0' + dateObj.getDate()),
        hour = dateObj.getHours() >= 10 ? dateObj.getHours() : '0' + dateObj.getHours(),
        minute = dateObj.getMinutes() >= 10 ? dateObj.getMinutes() : '0' + dateObj.getMinutes(),
        seconds = dateObj.getSeconds() >= 10 ? dateObj.getSeconds() : '0' + dateObj.getSeconds()
    return year + '-' + mouth + '-' + date + ' ' + hour + ':' + minute + ':' + seconds
}

//领取红包
function getRedBag(id, user, icon) {
    if (!$t.cookie.get('user_info')) {
        layer.alert('登陆后才能领取红包哦~', function () {
            layer.closeAll()
            setTimeout(function () {
                $('.login-modal-mask').css('display', 'flex')
            }, 20)
        })
        return null
    } else {
        $t.storage.set('sendRedBagUserName', {user: user, icon: icon})
        var e = e || event
        e.stopPropagation()

        var userinfo = $t.json($t.cookie.get('user_info'))
        var msg = {
            "command": "C_RECEIVE_REDBAG",
            "domain": NeedInfo.domain,
            "roomId": NeedInfo.roomId,
            "token": uToken,
            "userId": userinfo.userId,
            "userName": userinfo.userName,
            "userIcon": userinfo.userIcon,
            "roleName": userinfo.roleName,
            "content": {
                "redbagId": id
            },
        }

        if (window.chatRoom) {
            window.chatRoom.send(msg)
        }
    }
}

//表情渲染
$(function () {
    for (var i = 1; i <= 24; i++) {
        $('.choose-user-icon-container').append('<div class="choose-user-icon-btn" data-icon="userIcon' + i + '" onclick="chooseUserIcon(\'userIcon' + i + '\')"></div>')
    }
    $('.person-center-container > user-icon-logo')
})

//选择修改默认头像
function chooseUserIcon(icon) {
    var userInfo = JSON.parse(new Tools().cookie.get('user_info'))
    $('#modify-user-icon').attr('data-usericon', icon)
    $('#modify-user-icon').attr('src', '/resources/chat/pc/images/default/' + icon + '.png')
    window.choseUserIcontype = 1
}

//选择修改上传头像
function uploadFoeverUserIcon() {
    window.choseUserIcontype = 2
    var fileObj = $('#userIconFormInput')[0].files[0]
    //未选择文件
    if (!fileObj) {
        return null
    }
    //文件不是图片
    if (!/image\/png|image\/jpeg|image\/gif/ig.test(fileObj.type)) {
        layer.msg('请选择正确的图片文件')
        return null
    }
    //读取图片
    var reader = new FileReader()
    reader.readAsDataURL(fileObj)
    reader.onload = function (ev) {
        var ev = ev || event
        var base64data = ev.target.result
        $('#modify-user-icon').attr('src', base64data)
    }
    return null

}

//聊天发送图片
function sendPictureContent(e) {
    //获取登陆信息
    var user = $t.json($t.cookie.get('user_info'))
    if (!user) {
        layer.alert('登陆后才能聊天哦~', function () {
            layer.closeAll()
            setTimeout(function () {
                $('.login-modal-mask').css('display', 'flex')
            }, 20)
        })
        return null
    }
    //用户权限
    var forbidchat = false
    if (user.authority) {
        for (var i = 0, l = user.authority.length; i < l; i++) {
            if (user.authority[i] == 'chat:forbidchat') {
                forbidchat = true
            }
        }
    }
    if (forbidchat == false && NeedInfo.forbidChat == 1) {
        layer.msg('聊天室已禁言')
        return null
    }
    var _this = e
    var fileObj = $(e)[0].files[0]
    //未选择文件
    if (!fileObj) {
        return null
    }
    //文件不是图片
    if (!/image\/png|image\/jpeg|image\/gif/ig.test(fileObj.type)) {
        layer.msg('请选择正确的图片')
        return null
    }

    var reader = new FileReader()
    reader.readAsDataURL(fileObj)
    reader.onload = function (ev) {
        var ev = ev || event
        var data = ev.target.result
        compress(data, 0.6, function (res) {
            //创建数据对象
            var fd = new FormData()
            fd.append('base64Data', res)
            fd.append('token', uToken)
            $.ajax({
                url: base_url + "/chat/uploadBaseImg",
                method: 'post',
                data: fd,
                processData: false,
                contentType: false,
            })
                .success(function (res) {
                    if (res.success) {
                        var content = '<img class="uploadImg" onclick="showBigImg(this)" src="' + res.result + '">'
                        $('#sendImgBtn').val('')
                        var html = $('#editor').html()
                        if (html == '输入聊天内容，@TA') {
                            html = ''
                        }
                        $('#editor').html(html + content)
                    } else {
                        layer.msg('图片发送失败~')
                    }
                })
        })
    }
    return null
}

//发送信息
function sendMessageHandler() {
    var user = $t.json($t.cookie.get('user_info'))
    if (!user) {
        return null
    }
    //用户权限
    var forbidchat = false
    if (user.authority) {
        for (var i = 0, l = user.authority.length; i < l; i++) {
            if (user.authority[i] == 'chat:forbidchat') {
                forbidchat = true
            }
        }
    }

    if (forbidchat == false && NeedInfo.forbidChat == 1) {
        return null
    }
    NeedInfo.showHaveMoreMessage = false
    NeedInfo.autoScroll = true
    $('#new-msg-count').text('0')
    $('.have-more-message').hide()
    //获取编辑内容
    var content = $('#editor').html()

    //空消息不允许发送
    if (content == '' || content == '输入聊天内容，@TA') {
        layer.msg('请不要发送空消息~')
        return
    }
    //初始化用户信息
    var user = $t.json($t.cookie.get('user_info'))
    //正式发送数据
    var msg = {
        "command": "C_MESSAGE",
        "domain": NeedInfo.domain,
        "roomId": NeedInfo.roomId,
        "token": uToken,
        "userId": user.userId,
        "userName": user.userName,
        "userIcon": user.userIcon,
        "roleName": user.roleName,
        "content": content,
    }
    window.chatRoom.send(msg)
    //清空编辑框
    $('#editor').html('')
}

//左边列表右键事件
function slideBarRightContextMenu(el) {
    var e = e || event
    e.preventDefault()
    var user = $t.json($t.cookie.get('user_info'))
    if (!user) {
        return null
    }
    if ($(el).attr('data-uid') == user.userId) {
        return null
    }
    var html = ''
    if (user.authority) {
        for (var i = 0, l = user.authority.length; i < l; i++) {
            if (user.authority[i] == 'menu:chat') {
                html += '<button class="btn" onclick="removeRoomUser(' + $(el).attr("data-uid") + ',' + $(el).attr('data-auth')
                    + ')">踢出</button>'
            }
        }
    }
    $omenu = $('#user-list-menu')
    html += '<button class="btn" onclick="closeRoomUserMouse(' + $(el).attr("data-uid") + ',' + $(el).attr('data-auth') + ')">@TA</button>'
    $omenu.html(html)
    $omenu.css({
        display: 'flex',
        left: e.clientX + 'px',
        top: e.clientY + 'px'
    })
}

//用户头像右键事件
function messageContentContextMenu(el) {
    var e = e || event
    e.preventDefault()
    var user = $t.json($t.cookie.get('user_info'))
    if (!user) {
        return null
    }
    $omenu = $('#user-list-menu')
    var html = '<button class="btn" onclick="removeRoomUser(' + $(el).attr("data-uid") + ',' + $(el).attr('data-auth')
        + ')">踢出</button><button class="btn" onclick="closeRoomUserMouse(' + $(el).attr("data-uid") + ',' + $(el).attr('data-auth') + ')">@TA</button>'
    $omenu.html(html)
    $omenu.css({
        display: 'flex',
        left: e.clientX + 'px',
        top: e.clientY + 'px'
    })
}

//左下角消息提醒
function showNotification(data) {
    window.Notification.permission = "granted";
    var u = null
    if ($t.cookie.get('user_info')) {
        u = $t.json($t.cookie.get('user_info'))
        if (u.userId == data.userId) {
            return null
        }
    }
    if (window.Notification) {
        if (window.Notification.permission == "granted") {
            var notification = new Notification("用户" + data.userName + "发了一条消息", {
                body: transContent(data.content),
                icon: parseUserIcon(data.userIcon)
            });
            setTimeout(function () {
                notification.close();
            }, 5000);
        } else {
            window.Notification.requestPermission();
        }
    }
};

//解析用户头像
function parseUserIcon(userIcon) {
    var baseUrl = '/resources/chat/pc/images/default/'
    if (!userIcon) {
        return (baseUrl + 'userIcon' + Math.ceil(Math.random() * 24) + '.png')
    }
    if (userIcon.indexOf('http://') != -1) {
        return userIcon
    }
    return (baseUrl + userIcon + '.png')
}

//搜索在线用户方法
function searchOnlineUser() {
    var uList = $t.json($t.storage.get('userlist')),
        $val = $('#search-input').val()
    var html = []
    for (var i = 0, l = uList.length; i < l; i++) {
        var uname = $(uList[i]).find('.user-name').text()
        if (uname.indexOf($val) != -1) {
            html.push(uList[i])
        }
    }
    if (html.length == 0) {
        $('.user-list').html('<div style="line-height:30px;text-align: center;font-size: 12px;color: #666"><span></span>没有搜索到 "' + $val + '" 用户</span></div>')
        return
    }
    $('.user-list').html(html.join(''))
}

//踢出用户
function removeRoomUser(uid, authority) {
    var user = $t.json($t.cookie.get('user_info'))
    var ulist = $t.json($t.storage.get('userlist'))
    if (!user.authority) {
        layer.msg('权限不足~')
        return
    }
    layer.confirm('确定要踢出该用户吗？', {}, function () {
        console.log(ulist)
        //清除本地用户列表数据
        for (var i = 0, l = ulist.length; i < l; i++) {
            var $uid = $(ulist[i]).attr('data-uid')
            if ($uid == uid) {
                delete ulist[i]
            }
        }
        //更新本地数据
        $t.storage.set('userlist', ulist)

        //移除显示状态
        $('.user-list > .item').each(function (i, e) {
            if ($(this).attr('data-uid') == uid) {
                $(this.remove())
            }
        })
        if (uid == null) {
            layer.closeAll()
            return false
        }
        //发出移除指令
        var command = {
            "command": "C_REMOVE_CHAT",
            "domain": NeedInfo.domain,
            "roomId": NeedInfo.roomId,
            "token": uToken,
            "userId": user.userId,
            "userName": user.userName,
            "userIcon": user.userIcon,
            "roleName": user.roleName,
            "content": {
                "removeId": uid
            },
        }
        chatRoom.send(command)
        layer.closeAll()
    }, function () {

    })
}

//提出其他用户

window.onbeforeunload = function (ev) {
    //清除本地历史记录
    $t.storage.set('history', '')
    clearChatRoomAll()
    chatRoom.connecton.close()
}

function closeRoomUserMouse(uid, authority) {
    var atName = ''
    $('.user-list > .item').each(function () {
        if ($(this).attr('data-uid') == uid) {
            atName += $(this).find('.user-name').text()
        }
    })
    if (!atName) {
        layer.msg('该用户不在聊天室！')
        return
    }

    var html = $('#editor').html()
    if (html == '输入聊天内容，@TA') {
        html = ''
    }
    html += '@' + atName + '&nbsp;'
    $('#editor').html(html)
}

//撤回消息
function reCallContextMenu(msgId) {
    var e = e || event
    e.preventDefault()
    var user = $t.json($t.cookie.get('user_info'))
    if (!user) {
        return null
    }
    if (!user.authority) {
        return null
    }
    if (user.authority[2] != 'chat:recall') {
        return null
    }
    var $omenu = $('#user-list-menu')
    $omenu.html('<button class="btn" onclick="reCallMessage(\'' + msgId + '\')">撤回</button>')
    $omenu.css({
        display: 'flex',
        left: e.clientX + 'px',
        top: e.clientY + 'px'
    })
}

//撤回别人的消息
function reCallOtherContextMenu(msgId) {
    var e = e || event
    e.preventDefault()
    var user = $t.json($t.cookie.get('user_info'))
    if (!user) {
        return null
    }
    if (!user.authority) {
        return null
    }
    var recall = false
    for (var i = 0, l = user.authority.length; i < l; i++) {
        if (user.authority[i] == "chat:recall") {
            recall = true
        }
    }
    if (!recall) {
        return null
    }

    var $omenu = $('#user-list-menu')
    $omenu.html('<button class="btn" onclick="reCallMessage(\'' + msgId + '\')">撤回</button>')
    $omenu.css({
        display: 'flex',
        left: e.clientX + 'px',
        top: e.clientY + 'px'
    })
}

function reCallMessage(msgId) {
    var user = $t.json($t.cookie.get('user_info'))
    if (!user) {
        return null
    }
    if (!user.authority) {
        return null
    }
    if (user.authority[2] != 'chat:recall') {
        return null
    }
    //替换当前消息
    var $d = $('.msg-container').find('.' + msgId)
    $d.html('<p class="re-call-msg"><span class="re-call-text">你撤回了一条消息</span></p>')

    //组合消息
    var msg = {
        "command": "C_RECALL_MESSAGE",
        "domain": NeedInfo.domain,
        "roomId": NeedInfo.roomId,
        "token": uToken,
        "userId": user.userId,
        "userName": user.userName,
        "userIcon": user.userIcon,
        "roleName": user.roleName,
        "content": {
            messageId: msgId
        },
    }
    chatRoom.send(msg)
}

//点击图片放大
function showBigImg(el) {
    var $fd = $('.show-big-img-modal-mask')
    var $fdImg = $('#big-img')
    var width = $fd.width()
    var height = $fd.height()
    $fdImg.attr('src', $(el).attr('src'))
    $fdImg.css({
        'max-height': height + 'px',
        'max-width': width + 'px'
    })
    setTimeout(function () {
        $fd.css('display', 'flex')
    }, 10)
}