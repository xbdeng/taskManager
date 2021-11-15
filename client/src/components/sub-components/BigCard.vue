<template>
  <div class="bigchart-wrapper">
    <el-tabs v-model="activeName" @tab-click="handleClick">

      <el-tab-pane label="用户管理" name="first">
        <el-row :gutter="20">
          <el-col :span="15">
            <div class="chart-wrapper-t">
              <line-chart :chart-data="datacollection" :options="options" class="chart"></line-chart>
            </div>
          </el-col>

          <el-col :span="5">
            <el-form>
              <el-form-item>
                <el-row :gutter="10">
                  <el-col :span="25">
                    <el-radio-group v-model="radio1">
                      <el-radio-button label="本周"></el-radio-button>
                      <el-radio-button label="本月"></el-radio-button>
                      <el-radio-button label="本年"></el-radio-button>
                    </el-radio-group>
                  </el-col>
                  <el-col :span="5">
                    <el-date-picker
                        v-model="value2"
                        type="daterange"
                        align="right"
                        unlink-panels
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        :picker-options="pickerOptions">
                    </el-date-picker>
                  </el-col>
                </el-row>
              </el-form-item>

              <el-form-item>
                <div v-for="o in 4" :key="o" class="text item">
                  {{o + '. dxbdltql'}}
                </div>
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
      </el-tab-pane>

      <el-tab-pane label="配置管理" name="second">配置管理</el-tab-pane>
      <el-tab-pane label="角色管理" name="third">角色管理</el-tab-pane>
      <el-tab-pane label="定时任务补偿" name="fourth">定时任务补偿</el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
import BigChartCard from "./BigChartCard";
import LineChart from "./LineChart";

export default {
  name: 'BigChart',
  components: {LineChart},
  data() {
    return {
      radio1: '本年',
      activeName: 'first',
      datacollection: {
        labels: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul"],
        datasets: [{
          label: "Dataset #1",
          backgroundColor: "rgb(53,117,201)",
          borderColor: "rgb(5,173,194)",
          borderWidth: 2,
          hoverBackgroundColor: "rgb(53,117,201)",
          hoverBorderColor: "rgb(5,173,194)",
          data: [65, 59, 20, 81, 56, 55, 40],
        }]
      },
      options: {
        maintainAspectRatio: false,
        responsive: true,
        tooltip: {
          position: "nearest"
        },
        legend: {
          display: false
        },
        scales: {
          xAxes: [{
            ticks: {
              display: false
            }
          }],
          yAxes: [{
            ticks: {
              display: false
            }
          }]
        },
        layout: {
          padding: {
            right: 1,
            bottom: 15
          }
        },
      },
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [start, end]);
          }
        }]
      },
      value1: '',
      value2: ''
    };
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    }
  }
};
</script>

<style>
.chart-wrapper-t {
  position: relative;
  height: 35vh;
  width: 100%;
}

.bigchart-wrapper {
  height: 40vh;
  border: 2px solid #a1a1a1;;
  border-radius: 10px;
  background: #c0c0d0;
}
</style>
