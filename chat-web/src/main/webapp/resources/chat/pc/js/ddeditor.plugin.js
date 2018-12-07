jQuery.fn.extend({
    DDeditor: function (options) {
        var smileItems = new Array();//表情图片集合
        for (var i = 0; i < 21; i++) {
            smileItems[i] = 'face' + (i + 1) + '.png';
        }
        var smleDescriptions = [
            '微笑', '撇嘴', '色', '发呆'
        ];

        var defaults = {
            smile_id: '.smile',
            smile_path: '/resources/chat/pc/images/emoji/',
            smile_items: smileItems,
            smile_descriptions: smleDescriptions
        };
        var option = $.extend(defaults, options);
        var smile = $(option.smile_id);
        var editor = $(this);

        cursors = [];

        $(this).each(function () {
            cursors.push(new Cursor(this))
        });
        smile.click(function () {
            var user = $t.json($t.cookie.get('user_info'))
            if (!user) {
                layer.alert('登陆后才能参与聊天哦~', function () {
                    layer.closeAll()
                    setTimeout(function () {
                        $('.login-modal-mask').css('display', 'flex')
                    }, 20)
                })
                return null
            }
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
            if ($(editor).html() == "输入聊天内容，@TA") {
                $(editor).html('')
            }
            var index = smile.index(this);
            var strFace, path, items, descriptions;
            path = option.smile_path;
            items = option.smile_items;
            descriptions = option.smile_descriptions;
            $('#faceBox').remove();

            strFace = '<div id="faceBox" style="position:absolute;display:none;z-index:1000;" class="qqFace">'

            for (var i = 0; i < items.length; i++) {
                strFace += '<img onclick="cursors[' + index + '].insertHTML(getImgStr($(this).attr(\'src\'),' + i + '))" alt="" src="'
                    + path + items[i] + '" />';
            }
            strFace += '</div>';

            $(this).parent().append(strFace);
            var offset = $(this).position();
            var top = offset.top + 24;
            $('#faceBox').css({
                'left': offset.left,
                'bottom': top + 'px'
            });
            $('#faceBox').fadeIn(500);
            return false;
        });

//记录光标位置
        $(editor).click(function () {
            //获取用户信息
            var user = $t.json($t.cookie.get('user_info'))
            if (!user) {
                layer.alert('登陆后才能参与聊天哦~', function () {
                    layer.closeAll()
                    setTimeout(function () {
                        $('.login-modal-mask').css('display', 'flex')
                    }, 20)
                })
                cursors[$(editor).index(this)].initRange();
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
            if ($(this).html() == "输入聊天内容，@TA") {
                $(this).html('')
            }
            cursors[$(editor).index(this)].initRange();
        }).blur(function () {
            if ($(this).html() == '') {
                $(editor).html("输入聊天内容，@TA")
            }
            cursors[$(editor).index(this)].initRange();
        }).select(function () {
            cursors[$(editor).index(this)].initRange();
            return false;
        }).keyup(function () {
            cursors[$(editor).index(this)].initRange();
            return false;
        }).keydown(function (event) {//监听键盘回车键
            var e = e || event
            var editor = $('#editor')
            editor[0].scrollTop = editor[0].scrollHeight
            if (e.shiftKey && e.keyCode == 13) {
                cursors[$(editor).index(this)].insertHTML('<br/>');
                return false
            }
            if (e.ctrlKey && e.keyCode == 13) {
                cursors[$(editor).index(this)].insertHTML('<br/>');
                return false
            }
            if (e.keyCode == 13) {
                sendMessageHandler()
                return false;
            }
        });

        //隐藏表情区域
        $(document).click(function () {
            $('#faceBox').fadeOut(200, function () {
                $('#faceBox').remove();
            });
            $('.login-modal-mask').fadeOut(200)
            $('.redbag-modal-mask').fadeOut(200)
            $('.my-redbag-modal-mask').fadeOut(200)
            $('.person-center').fadeOut(200)
            $('#user-list-menu').hide()
            $('.select-room-container').fadeOut(200)
            $('.redbag-empty-modal-mask').fadeOut(200)
            $('.show-big-img-modal-mask').fadeOut(200)
            setTimeout(function () { //重置个人中心
                var oContainer = $('.person-center .person-center-container')
                oContainer.css('height', '90px')
                $('.person-center-container .user-icon-logo').show()
                $('.person-center-container .modify-user-info').hide()
            }, 200)
            $('#modal-weixin-share').fadeOut(200)
        });
    }
})
;

function getImgStr(src, i) {
    var face_asc = [
        '[::A]', '[::B]', '[::C]', '[::D]', '[::E]',
        '[::F]', '[::G]', '[::H]', '[::I]', '[::J]',
        '[::K]', '[::L]', '[::M]', '[::N]', '[::O]',
        '[::P]', '[::Q]', '[::R]', '[::S]', '[::T]',
        '[::U]',
    ]
    return '<img class="faceImg" data-replace="' + face_asc[i] + '" src="' + src + '" />';
}

function Cursor(a) {
    this.element = a;
}

Cursor.prototype = {
    getRange: function () {
        if (document.selection) {
            return document.selection.createRange().duplicate();
        } else if (window.getSelection) {
            return window.getSelection().getRangeAt(0).cloneRange();
        }
    },
    initRange: function () {
        var oDoc = this.element;
        oDoc.range = new Cursor(oDoc).getRange();
    },
    insertHTML: function (html) {
        var oDoc = this.element;
        if (document.selection && !!oDoc.range) {
            var ierange = oDoc.range;
            //在非标准浏览器中 要先让你需要插入html的div 获得焦点
            oDoc.focus();
            ierange.pasteHTML(html);
            ierange.select();//把选区转换成对象，用以显示光标

        } else if (!!window.getSelection && !!oDoc.range) {
            var sel = window.getSelection();
            var w3cRange = oDoc.range;
            w3cRange.deleteContents();
            var node = $(html).get(0);
            w3cRange.insertNode(node);
            oDoc.focus();
            w3cRange.setStartAfter(node);
            w3cRange.collapse(true);
            sel.removeAllRanges();
            sel.addRange(w3cRange);

        } else {
            oDoc.focus();
            if (document.selection) {
                var ierange = oDoc.range = document.selection.createRange().duplicate();
                ierange.pasteHTML(html);
                //在光标位置插入html 如果只是插入text 则就是fus.text="..."
                oDoc.focus();
            } else {
                var sel = window.getSelection();
                var w3cRange = oDoc.range = sel.getRangeAt(0).cloneRange();
                w3cRange.deleteContents();
                var node = $(html).get(0);
                w3cRange.insertNode(node);
                w3cRange.setStartAfter(node);
                w3cRange.collapse(true);
                sel.removeAllRanges();
                sel.addRange(w3cRange);
            }
        }
    },
    getHTML: function () {
        return $(this.element).html();
    },
    formatDoc: function (sCmd, sValue) {
        document.execCommand(sCmd, false, sValue);
        this.element.focus();
    }
};