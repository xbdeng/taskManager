<template>
  <div class="chart-div">
    <el-row :gutter="20">
      <el-col :span="15">
        <div class="chart-card-content">
          <div class="content-fix">
            <slot></slot>
          </div>
        </div>
      </el-col>

      <el-col :span="5">
        some special things
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'BigChartCard'
}
</script>

<style>
.chart-div {

}

.chart-card-content{

}

.content-fix{

}
</style>
