function ChatRoom(url, protocol, options) {
    var userinfo = JSON.parse(new Tools().cookie.get('user_info'))
    var query = ''
    if (userinfo) {
        query = "?token=" + uToken + "&domain=" + NeedInfo.domain + "&roomId=" + NeedInfo.roomId
    } else {
        query = "?token=&domain=" + NeedInfo.domain + "&roomId=" + NeedInfo.roomId
    }
    this.url = url
    this.uri = url + query
    this.connecton = new WebSocket(this.uri)
    this.protocol = protocol

    var defaultOptions = {
        reconnect: true,
        reconnectInterval: 30,
        heartBeat: true,
        heartBeatInterval: 30,
        checkStatus: true,
        checkStatusInterval: 5
    }
    if (options !== null) {
        for (i in options) {
            defaultOptions[i] = options[i]
        }
    }
    this.options = defaultOptions
    this.connecton.onopen = this.open.bind(this)
    this.connecton.onerror = this.error.bind(this)
    this.connecton.onclose = this.close.bind(this)
    this.connecton.onmessage = this.message.bind(this)
}

ChatRoom.prototype = {
    type: null,
    options: null,
    open: function (res) {
        this.heartBeat()
        this.checkStatus()
    },
    error: function (err) {
        console.log(err)
    },
    close: function (res) {
        console.log('链接断开.')
    },
    message: function (res) {
        var data = JSON.parse(res.data)
        this.data = data
        this.type = data.command
        this.events()
    },
    events: function () {
        for (i in this.protocol) {
            if (i == this.type) {
                this.protocol[i].bind(this)()
            }
        }
        console.log('消息类型：', this.type)
        console.log(this.data)
    },
    send(data) {
        if (this.connecton.readyState !== 1) {
            console.warn('服务器未连接，请稍候重试~')
            return null
        }
        if (new Tools().type(data) == 'Object') {
            this.connecton.send(JSON.stringify(data))
        } else {
            this.connecton.send(data)
        }
    },
    reconnect: function (res) {
        clearInterval(window.reconnectInterval)
        //判断是否是配置重连
        if (!this.options.reconnect) {
            return null
        }
        if (this.connecton.readyState > 1) {
            this.connecton.close()
            initChatRoom()
        }
        window.reconnectInterval = setInterval(function () {
            if (this.connecton.readyState > 1) {
                this.connecton.close()
                console.log('正在重连...')
                initChatRoom()
            } else {
                this.checkStatus()
                this.heartBeat()
                clearInterval(window.reconnectInterval)
            }
        }.bind(this), this.options.reconnectInterval * 1000)
    },
    heartBeat: function () {
        if (!this.options.heartBeat) {
            return null
        }
        //清除上一次产生的定时器
        clearInterval(window.heartBeatInterval)
        var domain = this.uri.match(/.*domain=(\w{1,3}\.\w{1,3}\.\w{1,3}\.\w{1,3}).*/)
        var roomId = this.uri.match(/.*[roomId|roomid]=(\w+).*/)
        var heartBeatData = {
            "command": "C_HEART_BEAT",
            "domain": !domain ? '127.0.0.1' : domain[1],
            "roomId": !roomId ? '1' : roomId[1],
        }
        var userInfo = JSON.parse($t.cookie.get('user_info'))
        if (userInfo) {
            for (var i in userInfo) {
                heartBeatData[i] = userInfo[i]
            }
        }
        window.heartBeatInterval = setInterval(function () {
            this.send(heartBeatData)
        }.bind(this), this.options.heartBeatInterval * 1000)
    },
    checkStatus: function () {
        if (!this.options.checkStatus) {
            return null
        }
        //清除上一次产生的定时器
        clearInterval(window.checkInterval)
        window.checkInterval = setInterval(function () {
            if (this.connecton.readyState > 1) {
                //断开连接后清除定时器
                clearInterval(window.checkInterval)
                clearInterval(window.heartBeatInterval)
                this.reconnect()
            }
        }.bind(this), this.options.checkStatusInterval * 1000)
    }
}
