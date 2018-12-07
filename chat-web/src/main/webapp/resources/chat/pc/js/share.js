//分享插入二维码.join-container-qrcode
var qrcode = new QRCode(document.querySelector('.join-container-qrcode'), {
    text: window.location.href,
    width: 80,
    height: 80,
    colorDark: '#000000',
    colorLight: '#ffffff',
    correctLevel: QRCode.CorrectLevel.H
})
$('.join-container-qrcode').append('<span>扫一扫，加入聊天室</span>')
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

//分享到qq
function shareToqq(event) {
    event.preventDefault();
    var _shareUrl = 'https://connect.qq.com/widget/shareqq/iframe_index.html?';
    _shareUrl += 'url=' + encodeURIComponent(_url || location.href); //分享的链接
    _shareUrl += '&title=' + encodeURIComponent(_title || document.title); //分享的标题
    window.open(_shareUrl, '_blank');
}

//分享到QQ空间
function shareToQzone(event) {
    event.preventDefault();
    var _shareUrl = 'http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?';
    _shareUrl += 'url=' + encodeURIComponent(_url || document.location); //参数url设置分享的内容链接|默认当前页location
    _shareUrl += '&showcount=' + _showcount || 0; //参数showcount是否显示分享总数,显示：'1'，不显示：'0'，默认不显示
    _shareUrl += '&desc=' + encodeURIComponent(_desc || '分享的描述'); //参数desc设置分享的描述，可选参数
    _shareUrl += '&summary=' + encodeURIComponent(_summary || '分享摘要'); //参数summary设置分享摘要，可选参数
    _shareUrl += '&title=' + encodeURIComponent(_title || document.title); //参数title设置分享标题，可选参数
    _shareUrl += '&site=' + encodeURIComponent(_site || ''); //参数site设置分享来源，可选参数
    _shareUrl += '&pics=' + encodeURIComponent(_pic || ''); //参数pics设置分享图片的路径，多张图片以＂|＂隔开，可选参数
    window.open(_shareUrl, '_blank');
}

//微信分享
function showWxModal() {
    setTimeout(function () {
        $('#modal-weixin-share').css('display', 'flex')
    },20)
}
