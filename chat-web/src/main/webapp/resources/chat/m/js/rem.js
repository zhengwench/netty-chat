(function (doc, win) {
        var docEl = doc.documentElement,
            resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
            recalc = function () {
                var clientWidth = docEl.clientWidth;
                if (!clientWidth) return;
                if(clientWidth>=750){
                    docEl.style.fontSize = '100px';
                }else{
                    docEl.style.fontSize = 100 * (clientWidth / 750) + 'px';
                }
            };

        if (!doc.addEventListener) return;
        win.addEventListener(resizeEvt, recalc, false);
        doc.addEventListener('DOMContentLoaded', recalc, false);
})(document, window);
window.onload=function(){ 
if(document.documentElement.scrollHeight <= document.documentElement.clientHeight) { 
bodyTag = document.getElementsByTagName('body')[0]; 
bodyTag.style.height = document.documentElement.clientWidth / screen.width * screen.height + 'px'; 
} 
setTimeout(function() { 
window.scrollTo(0, 1) 
}, 0); 
}; 
