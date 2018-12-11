var uploadTempImg=ip+'/chat/uploadTempImg';
var ajaxRooms=ip+'/chat/ajaxRooms';
var uploadForeverImg=ip+'/chat/uploadForeverImg';
var updateIconPort=ip+'/chat/updateIcon';
var userInfoPort=ip+'/chat/userInfo';
var ajaxReceiveRedBag=ip+'/chat/ajaxReceiveRedBag';
var updatePassword=ip+'/chat/updatePassword';
var logined=ip+'/chat/logined';
var webSocketUrlPort=ip+'/chat/webSocketUrl';
var onlineUser=ip+'/chat/onlineUser';

function Tools(el) {
    this.root = document.querySelector(el)
}


Tools.prototype = {
    dom: function (el) {
        return this.root.querySelector(el)
    },
    allDom: function (el) {
        return this.root.querySelectorAll(el)
    },
    bind: function (el, event, handler) {
        this.dom(el).addEventListener(event, handler)
    },
    storage: {
        set: function (key, data) {
            var stringifyData = null
            if (Object.prototype.toString.call(data).slice(8, -1) === "Object") {
                stringifyData = JSON.stringify(data)
            } else {
                stringifyData = JSON.stringify(data)
            }
            localStorage.setItem(key, stringifyData)
        },
        get: function (key) {
            var data = localStorage.getItem(key)
            return JSON.parse(data)
        }
    },
    cookie: {
        set: function (name, value, time) {
            var strsec = getsec(time);
            if (strsec) {
                var exp = new Date();
                exp.setTime(exp.getTime() + strsec * 1);
                document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
            } else {
                document.cookie = name + "=" + escape(value)
            }

        },
        get: function (name) {
            var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
            if (arr = document.cookie.match(reg)) {
                this.data = unescape(arr[2])
                return unescape(arr[2]);
            } else {
                return null;
            }
        },
        del: function (name) {
            var exp = new Date();
            exp.setTime(exp.getTime() - 19999999);
            var cval = this.get(name);
            if (cval != null) {
                document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
            }
        }
    },
    serialize: function (el, decode) {

        var fd = new FormData(document.querySelector(el)),
            keys = fd.keys()
        end = false
        s = []
        while (!end) {
            var data = keys.next()
            if (data.done) {
                end = true
            } else {
                if (decode) {
                    s.push(data.value + '=' + fd.get(encodeURIComponent(keys.value)))
                } else {
                    s.push(data.value + '=' + fd.get(data.value))
                }
            }
        }
        return s.join('&')
    },
    json: function (data) {
        if (this.type(data) == 'String') {
            return JSON.parse(data)
        } else {
            return data
        }
    },
    type: function (data) {
        return Object.prototype.toString.call(data).slice(8, -1)
    },

}


function getsec(str) {
    if (!str) {
        return false
    }
    var str1 = str.substring(1, str.length) * 1;
    var str2 = str.substring(0, 1);
    //s10 == 10s
    if (str2 == "s") {
        return str1 * 1000;
    }
    //h10 == 10小时
    else if (str2 == "h") {
        return str1 * 60 * 60 * 1000;
    }
    //d10 = 10天
    else if (str2 == "d                  ") {
        return str1 * 24 * 60 * 60 * 1000;
    }
}

window.$t = new Tools()