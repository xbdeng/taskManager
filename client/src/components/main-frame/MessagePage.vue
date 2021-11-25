<template>
  <div style="background-color: rgba(175, 175, 175, 0.26)">
    <el-form>
      <el-form-item v-for="(data, index) in Message" :key="index" class="remove_bottom">
        <el-card class="card" shadow="hover" :style="cardstyle(data.status)">
          <div>
            <el-row :gutter="10">
              <el-col :span="5">
                <i class="el-icon-sort" style="font-size: 50px" v-if="data.type === 0"></i>
                <i class="el-icon-s-check" style="font-size: 50px" v-if="data.type === 1"></i>
                <i class="el-icon-sort-up" style="font-size: 50px" v-if="data.type === 2"></i>
                <i class="el-icon-s-promotion" style="font-size: 50px" v-if="data.type === 3"></i>
                <i class="el-icon-success" style="font-size: 50px" v-if="data.type === 4"></i>
                <i class="el-icon-error" style="font-size: 50px" v-if="data.type === 5"></i>
                <i class="el-icon-check" style="font-size: 50px" v-if="data.type === 6"></i>
                <i class="el-icon-close" style="font-size: 50px" v-if="data.type === 7"></i>
                <i class="el-icon-circle-check" style="font-size: 50px" v-if="data.type === 8"></i>
                <i class="el-icon-circle-close" style="font-size: 50px" v-if="data.type === 8"></i>
<!--                <div class="card__icon"><i class="fas fa-bolt"></i></div>-->
              </el-col>
              <el-col :span="8">
                <span v-show="data.type === 0" class="card__title">Group Invitation</span>
                <span v-show="data.type === 1" class="card__title">Friend Request</span>
                <span v-show="data.type === 2" class="card__title">Group Application</span>
                <span v-show="data.type === 3" class="card__title">Assign Task</span>
                <span v-show="data.type === 4" class="card__title">Friend ACK</span>
                <span v-show="data.type === 5" class="card__title">Friend NAK</span>
                <span v-show="data.type === 6" class="card__title">Group Invit ACK</span>
                <span v-show="data.type === 7" class="card__title">Group Invit NAK</span>
                <span v-show="data.type === 8" class="card__title">Group Appli ACK</span>
                <span v-show="data.type === 9" class="card__title">Group Appli NAK</span>
              </el-col>
              <el-col :span=11>
                <el-button type="success" icon="el-icon-check" circle
                           v-if="data.status !== 'handled' && data.status !== 'read'"
                           @click="clickBottom(data.messageId, 0)"></el-button>
                <el-button type="danger" icon="el-icon-close" circle
                           v-if="data.status !== 'handled' && data.status !== 'read'"
                           @click="clickBottom(data.messageId, 1)"></el-button>
                <el-button type="warning" icon="el-icon-delete" circle v-if="data.status !== 'read'"
                           @click="clickBottom(data.messageId, 2)"></el-button>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24" class="center">
                <span v-show="data.type === 0" class="card__title "><span class="card__body">{{ data.usernameFrom }}</span>想要与你一起组队成为<span class="card__body">{{ data.message }}</span></span>
                <span v-show="data.type === 1" class=" card__title"><span class="card__body">{{ data.usernameFrom }}</span>想要与你成为好友</span>
                <span v-show="data.type === 2" class="card__title "><span class="card__body">{{ data.usernameFrom }}</span>邀请你加入<span class="card__body">{{ data.message }}</span></span>
                <span v-show="data.type === 3" class="card__title "><span class="card__body"></span>你收到来自组<span class="card__body">{{ data.message }}</span>由<span class="card__body">{{ data.usernameFrom }}</span>布置的一个新任务</span>
                <span v-show="data.type === 4" class="card__title "><span class="card__body"></span>您的好友请求已被<span class="card__body">{{ data.usernameFrom }}</span>接受</span>
                <span v-show="data.type === 5" class="card__title "><span class="card__body"></span>您的好友请求已被<span class="card__body">{{ data.usernameFrom }}</span>拒绝</span>
                <span v-show="data.type === 6" class="card__title "><span class="card__body"></span>您的入组邀请<span class="card__body">{{ data.message }}</span>已被<span class="card__body">{{ data.usernameFrom }}</span>接受</span>
                <span v-show="data.type === 7" class="card__title "><span class="card__body"></span>您的入组邀请<span class="card__body">{{ data.message }}</span>已被<span class="card__body">{{ data.usernameFrom }}</span>拒绝</span>
                <span v-show="data.type === 8" class="card__title "><span class="card__body"></span>您的入组请求<span class="card__body">{{ data.message }}</span>已被<span class="card__body">{{ data.usernameFrom }}</span>接受</span>
                <span v-show="data.type === 9" class="card__title "><span class="card__body"></span>您的入组请求<span class="card__body">{{ data.message }}</span>已被<span class="card__body">{{ data.usernameFrom }}</span>拒绝</span>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import axios from "axios";
import websocket from "../sub-components/WebSocket";
import {storeRequest} from "../sub-components/cache";

export default {
  name: "MessagePage",
  props: ['MessageShow'],
  data() {
    return {
      Message: [{
        messageId: 0,
        usernameFrom: 'Bill',
        type: 1,
        message: null,
        status: 'unhandled'
      },
        {
          messageId: 1,
          usernameFrom: 'Alisa',
          type: 0,
          message: 'ooad',
          status: 'unhandled'
        }]
    }
  },
  mounted() {
    this.getMessage()
  },
  beforeUpdate() {
    // this.getMessage()
  },
  watch: {
    MessageShow(newName, oldName) {
      if (newName === true) {
        this.getMessage()
        this.$forceUpdate()
      }
    }
  },
  methods: {
    getMessage() {
      const that = this
      axios.post(
          '/message/getmessages',
          {},
          {
            headers: {
              Authorization: window.sessionStorage.getItem('token')
            }
          }
      ).then(
          function (response) {
            if (response.data.code === 200) {
              that.$message({
                message: 'get msg success',
                type: 'success'
              })
            }
            that.Message = response.data.data
            console.log(that.Message)
            that.Message = that.Message.sort(
                function(a, b){
                  var x = a.status.toLowerCase();
                  var y = b.status.toLowerCase();
                  var xx = 0;
                  var yy = 0;
                  switch (x){
                    case 'unhandled': xx = 3; break;
                    case 'handled': xx = 2; break;
                    case 'read': xx = 1;break;
                  }
                  switch (y){
                    case 'unhandled': yy = 3; break;
                    case 'handled': yy = 2; break;
                    case 'read': yy = 1;break;
                  }
                  return yy - xx;
                }
            )
            let newToken = response.headers.authorization
            if (newToken != null) window.sessionStorage.setItem('token', newToken)
          },
          function (err) {
            that.$message({
              message: 'server error',
              type: 'error'
            })
          }
      )
    },
    clickBottom(messageId, operation) {
      if(websocket.getStatus() === "连接已关闭" || websocket.getStatus() === "未连接" ){
        storeRequest('/api/message/confirm',{messageId: messageId, operation: operation})
        this.$message({
          message: 'Offline request, request cache',
          type: 'error'
        })
        return
      }
      const that = this
      axios.post(
          '/message/confirm',
          {
            messageId: messageId,
            operation: operation
          },
          {
            headers: {
              Authorization: window.sessionStorage.getItem('token')
            }
          }
      ).then(
          function (response) {
            if (response.data.code === 200) {
              that.$message({
                message: 'confirm success',
                type: 'success'
              })
            }
            that.getMessage()
            let newToken = response.headers.authorization
            if (newToken != null) window.sessionStorage.setItem('token', newToken)
            that.$forceUpdate()
          },
          function (err) {
            that.$message({
              message: 'server error',
              type: 'error'
            })
            that.$forceUpdate()
          }
      )
    },
    cardstyle(status){
      if(status === 'read'){
        return {background: 'radial-gradient(#76b2fe, #b69efe)'}
      }
      if(status === 'handled'){
        return {background: 'radial-gradient(#fbc1cc, #fa99b2)'}
      }
      if(status === 'unhandled'){
        return {background: 'radial-gradient(#1fe4f5, #3fbafe)'}
      }
    }
  }
}
</script>

<style scoped>
/*.box-card-x {*/
/*  width: 400px;*/
/*  padding: 5px;*/
/*  height: 125px;*/
/*}*/

.remove_bottom {
  margin-bottom: 5px;
}

.card {
  margin: 5px;
  padding: 5px;
  width: 400px;
  height: 125px;
  display: grid;
  grid-template-rows: 20px 50px 1fr 50px;
  border-radius: 10px;
  box-shadow: 0px 6px 10px rgba(0, 0, 0, 0.25);
  transition: all 0.2s;
}

.card:hover {
  box-shadow: 0px 6px 10px rgba(0, 0, 0, 0.4);
  transform: scale(1.01);
}

.card__title {
  grid-row: 3/4;
  font-weight: bold;
  font-size: 13px;
  position:relative;
  color: #103154;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica,
  Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol";
}

.card__body{
  color: #a23737;
  font-size: 15px;
  text-align: center;
}

.card__icon {
  position: relative;
  text-decoration: none;
  color: rgba(255, 255, 255, 0.9);
}
</style>
