<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <link href="${request.getContextPath()}/resources/sys/static/h-ui/css/H-ui.min.css" rel="stylesheet" type="text/css" />
    <link href="${request.getContextPath()}/resources/sys/static/h-ui.admin/css/H-ui.login.css" rel="stylesheet" type="text/css" />
    <link href="${request.getContextPath()}/resources/sys/static/h-ui.admin/css/style.css" rel="stylesheet" type="text/css" />
    <link href="${request.getContextPath()}/resources/sys/lib/Hui-iconfont/1.0.8/iconfont.css" rel="stylesheet" type="text/css" />
    <title>后台登录</title>
</head>
<body>
<header class="navbar-wrapper">
    <div style="background-color:#426374;" class="navbar navbar-fixed-top">
        <div class="container-fluid cl">
            <div class="logo navbar-logo f-l mr-10 hidden-xs" style="font-size: large">开奖网后台</div>
        </div>
    </div>
</header>
<div class="loginWraper">
    <div id="loginform" class="loginBox">
        <form id="loginForm" class="form form-horizontal">
            <div class="row cl">
                <label class="form-label col-xs-3"><i class="Hui-iconfont">&#xe60d;</i></label>
                <div class="formControls col-xs-8">
                    <input id="username" name="username" type="text" placeholder="账户" class="input-text size-L">
                </div>
            </div>
            <div class="row cl">
                <label class="form-label col-xs-3"><i class="Hui-iconfont">&#xe60e;</i></label>
                <div class="formControls col-xs-8">
                    <input id="password" name="password" type="password" placeholder="密码" class="input-text size-L">
                </div>
            </div>
            <#--<div class="row cl">-->
                <#--<div class="formControls col-xs-8 col-xs-offset-3">-->
                    <#--<input class="input-text size-L" type="text" placeholder="验证码" onclick="" id="verifyCode" name="verifyCode" value="" style="width:150px;">-->
                    <#--<img id="verifyCodeimg" src="/sysLoginVerifyCode"> <a href="javascript:fGetCode();">看不清，换一张</a>-->
                <#--</div>-->
            <#--</div>-->
            <div id="errordiv" style="display:none;" class="row cl">
                <div class="formControls col-xs-8 col-xs-offset-3">
                    <label id="errormsg" style="color:red;" for="errormsg">
                        用户名错误
                    </label>
                </div>
            </div>
            <div class="row cl">
                <div class="formControls col-xs-8 col-xs-offset-3">
                    <input type="button" class="btn btn-success radius size-L" onclick="login();" value="&nbsp;登&nbsp;&nbsp;&nbsp;&nbsp;录&nbsp;">
                </div>
            </div>
        </form>
    </div>
</div>
<div class="footer">Copyright 双赢科技</div>
<script type="text/javascript" src="${request.getContextPath()}/resources/sys/lib/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="${request.getContextPath()}/resources/sys/static/h-ui/js/H-ui.min.js"></script>
<script type="text/javascript" src="${request.getContextPath()}/resources/sys/js/js.cookie.js"></script>

<script type="text/javascript">
    $(function () {
        $(document).keyup(function (event) {
            if (event.keyCode == 13) {
                login();
            }
        });
    });

    function login() {
        if (!$('#username').val()) {
            $('#errordiv').show();
            $('#errormsg').text('请填写用户名');
            return;
        }
        if (!$('#password').val()) {
            $('#errordiv').show();
            $('#errormsg').text('请填写用户名');
        }
        // if (!$('#verifyCode').val()) {
        //     $('#errordiv').show();
        //     $('#errormsg').text('请填写验证码');
        //     return;
        // }
        $.ajax({
            //几个参数需要注意一下
            type: "POST",//方法类型
            dataType: "json",//预期服务器返回的数据类型
            url: "/sys/logined",//url
            data: $('#loginForm').serialize(),
            success: function (result) {
                if (result.success) {
                    Cookies.set('sys_token', result.result.token, { expires: 7 });
                    window.location.href = "/sys/index";
                } else {
                    //fGetCode();
                    $('#errormsg').text(result.errorDesc);
                    $('#errordiv').show();
                }
            },
            error: function () {
                alert("异常！");
                return false;
            }
        });
    }

    function fGetCode() {
        document.getElementById('verifyCodeimg').src = '/sysLoginVerifyCode?' + Math.random();
    }
</script>
</body>
</html>

