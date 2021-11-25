<template>
<!-- 这个页面是一个模板页面，用来向用户显示好友信息 -->
  <div>
    <template>
      <el-container>
        <!-- 头部栏显示好友名称 -->
          <el-header>
              用户名: {{ friend.username }}
          </el-header>
          <!-- 主页面显示好友信息-->
          <el-main>
              <p>邮箱: {{ friend.email }}</p>
              <p>电话: {{ friend.phone }}</p>
              <p>个人签名: {{ friend.personalSign }}</p>
          </el-main>
      </el-container>
    </template>
  </div>
</template>

<script>

export default {

  name: 'FriendShow',
  // 这个组件接收的参数是一个任务对象
  props: ['friend'],
  mounted() {
    console.log(this.friend)
  }

}
</script>
