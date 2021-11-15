<template>
  <el-form>
    <el-form-item>
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="grid-content bg-purple">

            <ChartCard title="项目总数" :total="projectData.count">
              <div class="chart-wrapper">
                <line-chart :chart-data="datacollection" :options="options" class="chart"></line-chart>
              </div>
              <template slot="footer">本月立项 <span>3</span></template>
            </ChartCard>
          </div>
        </el-col>


        <el-col :span="6">
          <div class="grid-content bg-purple">

            <ChartCard title="项目总数" :total="projectData.count">
              <div class="chart-wrapper">
                <bar-chart :chart-data="datacollection" :options="options" class="chart"></bar-chart>
              </div>
              <template slot="footer">本月立项 <span>3</span></template>
            </ChartCard>
          </div>
        </el-col>

        <el-col :span="6">
          <div class="grid-content bg-purple">

            <doughnut-card title="项目分布" :total="projectData.count">
              <div class="chart-wrapper">
                <doughnut-chart :chart-data="doughnutdata" :options="options" class="chart"></doughnut-chart>
              </div>
            </doughnut-card>
          </div>
        </el-col>

        <el-col :span="6">
          <div class="grid-content bg-purple">

            <doughnut-card title="总任务进度" :total="projectData.count">
              <div class="chart-wrapper">
                <el-progress type="circle" :percentage="50" status="exception"></el-progress>
                <el-progress :percentage="50" status="exception"></el-progress>
              </div>
            </doughnut-card>
          </div>
        </el-col>

      </el-row>
    </el-form-item>
    <el-form-item>
      <BigChart></BigChart>
    </el-form-item>
  </el-form>
</template>

<script>
import LineChart from '../sub-components/LineChart.js'
import ChartCard from '../sub-components/ChartCard'
import BarChart from '../sub-components/BarChart'
import BigCard from "../sub-components/BigCard";
import BigChart from "../sub-components/BigCard";
import DoughnutChart from "../sub-components/DoughnutChart";
import DoughnutCard from "../sub-components/DoughnutCard";

const projectList = [];

export default {
  name: "data-analysis",
  components: {
    DoughnutCard,
    DoughnutChart,
    BigChart,
    BarChart,
    ChartCard,
    LineChart
  },
  data() {
    return {
      doughnutdata: {
        labels: [
          'Red',
          'Blue',
          'Yellow'
        ],
        datasets: [{
          label: 'My First Dataset',
          data: [300, 50, 100],
          backgroundColor: [
            'rgb(255, 99, 132)',
            'rgb(54, 162, 235)',
            'rgb(255, 205, 86)'
          ],
          hoverOffset: 4
        }]
      },
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
            },
            gridLines: {
              display: false
            }
          }],
          yAxes: [{
            ticks: {
              display: false
            },
            gridLines: {
              display: false
            },
          }],
        },
        layout: {
          padding: {
            right: 1,
            bottom: 15
          }
        },
      },
      projectData: {
        count: '0',
        projectSchedule: 0,
        chartData: {
          columns: ['日期', '数量'],
          rows: projectList
        },
        chartSettings: {
          itemStyle: {
            color: '#1890ff'
          },
        },
      },
    }
  },
  mounted() {
  },
  methods: {
    fillData() {
      this.datacollection = {
        labels: [this.getRandomInt(), this.getRandomInt()],
        datasets: [
          {
            label: 'Data One',
            backgroundColor: '#f87979',
            data: [this.getRandomInt(), this.getRandomInt()]
          }, {
            label: 'Data One',
            backgroundColor: '#f87979',
            data: [this.getRandomInt(), this.getRandomInt()]
          }
        ]
      }
    },
    getRandomInt() {
      return Math.floor(Math.random() * (50 - 5 + 1)) + 5
    }
  }
}

</script>

<style>
.el-col {
  border-radius: 4px;
}

.bg-purple-dark {
  background: #99a9bf;
}

.bg-purple {
  background: transparent;
}

.bg-purple-light {
  background: #e5e9f2;
}

.grid-content {
  border-radius: 4px;
  min-height: 36px;
}

.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}

.chart {
  position: relative;
  height: 100%;
  width: 100%;
}

.chart-wrapper {
  position: relative;
  height: 100%;
  width: 100%;
}
</style>
