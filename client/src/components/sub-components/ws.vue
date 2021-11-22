<template>

</template>

<script>
import router from "../../router";

export default {
  name: "ws",
  data() {
    return {
      timeout: 5000,
      username: ''
    }
  },
  // mounted() {
  //   this.connWebSocket();
  // },
  // beforeDestroy() {
  //   // 监听窗口关闭事件,vue生命周期销毁之前关闭socket当窗口关闭时，防止连接还没断开就关闭窗口。
  //   this.onbeforeunload();
  // },
  methods: {
    getWebsocket(username) {
      if (this.websocket) {
        return this.websocket
      } else {
        return this.connWebSocket(username)
      }
    },
    // websocket
    connWebSocket(username_in) {
      // let userInfo = JSON.parse(sessionStorage.getItem("userInfos"));
      // let userId = userInfo.userId;
      // WebSocket
      if ("WebSocket" in window) {
        this.username = username_in
        this.websocket = new WebSocket(
            "ws://localhost:8081/messagepush/" + username_in //userId 传此id主要后端java用来保存session信息，用于给特定的人发送消息，广播类消息可以不用此参数
        );
        console.log(this.websocket)
        //初始化socket
        this.initWebSocket();
        if (this.offline === true) {
          this.offline = false;
          this.$notify({
            title: '警告',
            message: '您已上线',
            type: 'warning'
          });
        }
      } else {
        alert("浏览器不支持websocket");
      }
    },

    initWebSocket() {
      // 连接错误
      this.websocket.onerror = this.setErrorMessage;

      // 连接成功
      this.websocket.onopen = this.setOnopenMessage;

      // 收到消息的回调
      this.websocket.onmessage = this.setOnmessageMessage;

      // 连接关闭的回调
      this.websocket.onclose = this.setOncloseMessage;

      // 监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
      window.onbeforeunload = this.onbeforeunload;
    },
    setErrorMessage() {
      console.log(
          "WebSocket连接发生错误   状态码：" + this.websocket.readyState
      );
      if (this.offline === false) {
        this.$notify({
          title: '警告',
          message: '您已离线 可能无法使用部分功能',
          type: 'warning'
        });
        this.offline = true;
      }
      this.reconnect();
    },
    setOnopenMessage() {
      console.log(this.websocket)
      console.log("WebSocket连接成功    状态码：" + this.websocket.readyState);
      this.start()
    },
    setOnmessageMessage(result) {
      this.reset();
      console.log("服务端返回：" + result.data);
      let msgMap = JSON.parse(result.data);
      let id = msgMap.id;
      let title = msgMap.title;
      let type = msgMap.type;
      // 根据服务器推送的消息做自己的业务处理
      console.log('get info ', result)

      // this.$notify({
      //   title: "你有一条新信息",
      //   type: "info",
      //   duration: 0,
      //   dangerouslyUseHTMLString: true,
      //   message:
      //       '<div style="height:100px;width:100px">' +
      //       title,
      //   position: "bottom-right"
      // });
    },
    setOncloseMessage() {
      console.log("WebSocket连接关闭    状态码：" + this.websocket.readyState);
      if (this.offline === false) {
        this.$notify({
          title: '警告',
          message: '您已离线 可能无法使用部分功能',
          type: 'warning'
        });
        this.offline = true;
      }
      this.reconnect();
    },
    onbeforeunload() {
      this.closeWebSocket();
    },
    closeWebSocket() {
      this.websocket.close();
    },
// heartCheck
    reconnect() {//重新连接
      let that = this;
      if (that.lockReconnect) {
        return;
      }
      that.lockReconnect = true;
      //没连接上会一直重连，设置延迟避免请求过多
      that.timeoutnum && clearTimeout(that.timeoutnum);
      that.timeoutnum = setTimeout(function () {
        //新连接
        that.connWebSocket();
        that.lockReconnect = false;
      }, 5000);
    },
    reset() {//重置心跳
      let that = this;
      //清除时间
      clearTimeout(that.timeoutObj);
      clearTimeout(that.serverTimeoutObj);
      //重启心跳
      that.start();
    },
    start() {//开启心跳
      let self = this;
      self.timeoutObj && clearTimeout(self.timeoutObj);
      self.serverTimeoutObj && clearTimeout(self.serverTimeoutObj);
      self.timeoutObj = setTimeout(function () {
        //这里发送一个心跳，后端收到后，返回一个心跳消息，
        if (self.websocket.readyState === 1) {//如果连接正常
          self.websocket.send("{heartCheck: 1}");
        } else {//否则重连
          self.reconnect();
        }
        self.serverTimeoutObj = setTimeout(function () {
          //超时关闭
          self.websocket.close();
        }, self.timeout);
      }, self.timeout)
    },
  },
}
</script>

<style scoped>

</style>
