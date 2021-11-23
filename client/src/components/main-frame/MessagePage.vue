<template>
  <div>
    <el-form>
      <el-form-item v-for="(data, index) in Message" :key="index" class="remove_bottom">
        <el-card class="box-card-x" shadow="hover">
          <div>
            <el-row :gutter="10">
              <el-col :span="5">
                <i class="el-icon-s-check"></i>
              </el-col>
              <el-col :span="8">
                <span v-show="data.type === 0">Group invitation</span>
                <span v-show="data.type === 1">Friend request</span>
                <span v-show="data.type === 2">Group application</span>
              </el-col>
              <el-col :span=11>
                <el-button type="success" icon="el-icon-check" circle
                           v-if="data.status !== 'handle' && data.status !== 'read'"
                           @click="clickBottom(data.messageId, 0)"></el-button>
                <el-button type="danger" icon="el-icon-close" circle
                           v-if="data.status !== 'handle' && data.status !== 'read'"
                           @click="clickBottom(data.messageId, 1)"></el-button>
                <el-button type="warning" icon="el-icon-delete" circle v-if="data.status !== 'read'"
                           @click="clickBottom(data.messageId, 2)"></el-button>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24" class="center">
                <span v-show="data.type === 0">{{ data.usernameFrom }}想要与你一起组队成为{{ data.message }}</span>
                <span v-show="data.type === 1">{{ data.usernameFrom }}想要与你成为好友</span>
                <span v-show="data.type === 2">{{ data.usernameFrom }}邀请你加入{{ data.message }}</span>
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
          'http://localhost:8081/api/message/getmessages',
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
      const that = this
      axios.post(
          'http://localhost:8081/api/message/confirm',
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
    }
  }
}
</script>

<style scoped>
.box-card-x {
  width: 400px;
  padding: 5px;
  height: 125px;
}

.remove_bottom {
  margin-bottom: 5px;
}
</style>
