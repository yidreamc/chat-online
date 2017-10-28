/**
 * 格式时间
 * @param fmt
 * @returns {*}
 */
Date.prototype.format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ?
                (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
    }
    return fmt;
};

$(document).ready(function(e) {
    $('#message_box').scrollTop($("#message_box")[0].scrollHeight + 20);

    $('.sub_but').click(function(event){
        sendMessage(event, 0);
    });


    /*按下按钮或键盘按键*/
    $("#message").keydown(function(event){
        var e = window.event || event;
        var k = e.keyCode || e.which || e.charCode;
        //按下ctrl+enter发送消息
        if((event.ctrlKey && (k == 13 || k == 10) )){
            sendMessage(event, 0);
        }
    });
});
function sendMessage(event, to_uid){
    var msg = $("#message").val();
    var uname = $('#name').text();
    var uid = $('#uid').val();
    if (msg !== "") {
        headers = {};
        body = {
            'message': msg,
            'sendId': uid,
            'sendName': uname,
            'time': new Date().format("yy-MM-dd hh:mm:ss")
        };
        $("#message").val('');
        stompClient.send("/all", headers, JSON.stringify(body));
    }
}

window.onload = function () {
    function connect() {
        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect(
            {},
            function connectCallback (frame) {
                // 订阅广播消息
                var subscription_broadcast = stompClient.subscribe('/topic/all', function callBack (response) {
                    if (response.body) {

                        var msg = JSON.parse(response.body);

                        var htmlData =   '<div class="msg_item fn-clear">'
                            + '   <div class="uface"><img src="' + msg.sendAvatar  + '" width="40" height="40"  alt=""/></div>'
                            + '   <div class="item_right">'
                            + '     <div class="msg own">' +  msg.message + '</div>'
                            + '     <div class="name_time">'+ msg.sendName + ' ' + msg.time +  '</div>'
                            + '   </div>'
                            + '</div>';

                        $("#message_box").append(htmlData);
                        $('#message_box').scrollTop($("#message_box")[0].scrollHeight + 20);

                    } else {

                    }
                });

                //获取用户列表
                var subscription_getusers = stompClient.subscribe('/userlist', function callBack (response) {
                    if (response.body) {
                        var html = '';
                        var userlist = JSON.parse(response.body);
                        console.log(response.body);
                        for(var i =0;i<userlist.length;i++){
                         html += '<li class="fn-clear" data-id="1"><span><img src="' + userlist[i].avatar +  '" width="30" height="30"  alt=""/></span><em>' + userlist[i].name+ '</em><small class="online" title="在线"></small></li>'
                        }
                        $("#users").replaceWith(html);
                    } else {

                    }
                });
            },
            function errorCallBack (error) {
            }
        );
    }
    /**
     * 监听窗口关闭事件，窗口关闭前，主动关闭连接，防止连接还没断开就关闭窗口，server端会抛异常
     */
    window.onbeforeunload = function () {


        if (stompClient != null) {
            stompClient.disconnect();
            socket.close();

        }
    };
    connect();

};








































