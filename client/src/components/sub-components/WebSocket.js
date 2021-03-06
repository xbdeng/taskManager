import {Notification} from 'element-ui'
import {removeRequest} from "./cache";
import App from "../../App";

var url = 'ws://localhost:8081/messagepush/'
export var ws;
var tt;
var lockReconnect = false;//避免重复连接
var broken = false;
var reconnectvar = true;
var clientId = ''

var websocket = {
    // Init: function (clientId) {
    //     if ("WebSocket" in window) {
    //         ws = new WebSocket(url + clientId);
    //     } else if ("MozWebSocket" in window) {
    //         ws = new MozWebSocket(url + clientId);
    //     } else {
    //         console.log("您的浏览器不支持 WebSocket!");
    //         return;
    //     }

        // ws.onmessage = function (e) {
        //     console.log("接收消息:" + e.data)
        //     heartCheck.start()
        //     if (e.data === 'ok') {//心跳消息不做处理
        //         return
        //     }
        //     broken = false
        //     //messageHandle(e.data)
        // }

        // ws.onclose = function () {
        //     console.log("连接已关闭")
        //     if (broken === false) {
        //         Notification({
        //             title: '警告',
        //             message: '连接已关闭',
        //             type: 'warning'
        //         });
        //     }
        //     broken = true
        //     if (reconnectvar === true) {
        //         reconnect(clientId);
        //     }
        // }

        // ws.onopen = function () {
        //     console.log("连接成功")
        //     Notification({
        //         title: '成功',
        //         message: '连接成功',
        //         type: 'success'
        //     });
        //     broken = false
        //     heartCheck.start();
        // }

    //     ws.onerror = function (e) {
    //         console.log("数据传输发生错误");
    //         if (broken === false) {
    //             Notification({
    //                 title: '警告',
    //                 message: '数据传输发生错误',
    //                 type: 'warning'
    //             });
    //         }
    //         broken = true
    //         reconnect(clientId)
    //     }
    // },

    Send: function (sender, reception, body, flag) {
        let data = {
            sender: sender,
            reception: reception,
            body: body,
            flag: flag
        }
        let msg = JSON.stringify(data)
        console.log("发送消息：" + msg)
        ws.send(msg)
    },
    getWebSocket() {
        return ws;
    },
    getStatus() {
        if (ws.readyState === 0) {
            return "未连接";
        } else if (ws.readyState === 1) {
            return "已连接";
        } else if (ws.readyState === 2) {
            return "连接正在关闭";
        } else if (ws.readyState === 3) {
            return "连接已关闭";
        }
    },
    setReconnectVar(bol){
        reconnectvar = bol
    },
    setBroken(bol){
        broken = bol
    },
    setInit(inId){
        ws = new WebSocket(url + inId);
        clientId = inId
    },
    getBroken(){
        return broken
    },
    getReconnectVar(){
        return reconnectvar
    },
    setclientId(val){
        clientId = val
    },
    getlockReconnect(){
        return lockReconnect
    },
    gettt(){
        return tt
    },
    setlockReconnect(val){
        lockReconnect = val
    },
    settt(val){
        tt = val
    }
}

//根据消息标识做不同的处理
function messageHandle(message) {
    let msg = JSON.parse(message)
    switch (msg.flag) {
        case 'command':
            console.log("指令消息类型")
            break;
        case 'inform':
            console.log("通知")
            break;
        default:
            console.log("未知消息类型")
    }
}

// export function reconnect(sname) {
//     if (lockReconnect) {
//         return;
//     };
//     lockReconnect = true;
//     //没连接上会一直重连，设置延迟避免请求过多
//     tt && clearTimeout(tt);
//     tt = setTimeout(function () {
//         ws.close()
//         console.log("执行断线重连...")
//         ws = new WebSocket(url + sname);
//         lockReconnect = false;
//     }, 5000);
// }

//心跳检测
export var heartCheck = {
    timeout: 5000,
    timeoutObj: null,
    serverTimeoutObj: null,
    start: function () {
        console.log('开始心跳检测');
        var self = this;
        this.timeoutObj && clearTimeout(this.timeoutObj);
        this.serverTimeoutObj && clearTimeout(this.serverTimeoutObj);
        this.timeoutObj = setTimeout(function () {
            //这里发送一个心跳，后端收到后，返回一个心跳消息，
            //onmessage拿到返回的心跳就说明连接正常
            // console.log('心跳检测...');
            ws.send("{heartCheck: 1}");
            self.serverTimeoutObj = setTimeout(function () {
                if (ws.readyState !== 1) {
                    // console.log(ws)
                    ws.close();
                }
                // createWebSocket();
            }, self.timeout);

        }, this.timeout)
    }
}

export default websocket;
