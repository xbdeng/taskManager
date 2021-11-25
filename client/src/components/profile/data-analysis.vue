<template>
  <el-form>
    <el-form-item>
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="grid-content bg-purple">

            <ChartCard title="项目总数" :total="this.totalStart.toString()">
              <div class="chart-wrapper" v-if="this.Father_Mount">
                <line-chart :chart-data="datacollection_A" :options="options" class="chart" :show="true"></line-chart>
              </div>
              <template slot="footer">本年立项:<span>{{this.totalStart}}</span></template>
            </ChartCard>
          </div>
        </el-col>


        <el-col :span="6">
          <div class="grid-content bg-purple">

            <ChartCard title="项目完成总数" :total="this.totalFinish.toString()">
              <div class="chart-wrapper" v-if="this.Father_Mount">
                <bar-chart :chart-data="datacollection" :options="options_B" class="chart"></bar-chart>
              </div>
              <template slot="footer">本年完成:<span>{{this.totalFinish}}</span></template>
            </ChartCard>
          </div>
        </el-col>

        <el-col :span="6">
          <div class="grid-content bg-purple">

            <doughnut-card title="项目分布" :total="this.totalStart.toString()+'/'+this.totalFinish.toString()+'/'+this.totalDue">
              <div class="chart-wrapper" v-if="this.Father_Mount">
                <doughnut-chart :chart-data="doughnutdata" :options="options" class="chart"></doughnut-chart>
              </div>
            </doughnut-card>
          </div>
        </el-col>

        <el-col :span="6">
          <div class="grid-content bg-purple">

            <doughnut-card title="组队任务">
              <div class="chart-wrapper">
                <el-progress type="circle" :percentage="this.progress_Rate_A" status="exception"></el-progress>
                <el-progress :percentage="this.progress_Rate_B" status="exception"></el-progress>
              </div>
            </doughnut-card>
          </div>
        </el-col>

      </el-row>
    </el-form-item>
    <el-form-item>
      <BigChart :Month_data="this.BigChart_Month" :Year_data="this.BigChart_Year" :Day_data="this.BigChart_Days" :Father_Mount="this.Father_Mount"></BigChart>
    </el-form-item>
  </el-form>
</template>

<script>
import LineChart from '../sub-components/LineChart.js'
import ChartCard from '../sub-components/ChartCard'
import BarChart from '../sub-components/BarChart'
import BigChart from "../sub-components/BigCard";
import DoughnutChart from "../sub-components/DoughnutChart";
import DoughnutCard from "../sub-components/DoughnutCard";
import axios from "axios";

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
      totalStart: 0,
      totalFinish: 0,
      totalDue: 0,
      progress_rate_A: [],
      doughnutdata: {},
      // doughnutdata: {
      //   labels: [
      //     'Red',
      //     'Blue',
      //     'Yellow'
      //   ],
      //   datasets: [{
      //     label: 'My First Dataset',
      //     data: this.progress_rate_A,
      //     backgroundColor: [
      //       'rgb(255, 99, 132)',
      //       'rgb(54, 162, 235)',
      //       'rgb(255, 205, 86)'
      //     ],
      //     hoverOffset: 4
      //   }]
      // },
      // datacollection_A: {
      //   labels: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul"],
      //   datasets: [{
      //     label: "Dataset #1",
      //     backgroundColor: "rgb(53,117,201)",
      //     borderColor: "rgb(5,173,194)",
      //     borderWidth: 2,
      //     hoverBackgroundColor: "rgb(53,117,201)",
      //     hoverBorderColor: "rgb(5,173,194)",
      //     data: [65, 59, 20, 81, 56, 55, 40],
      //   }]
      // },
      datacollection_A: {},
      Father_Mount: false,
      // datacollection: {
      //   labels: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul"],
      //   datasets: [{
      //     label: "Dataset #1",
      //     backgroundColor: "rgb(53,117,201)",
      //     borderColor: "rgb(5,173,194)",
      //     borderWidth: 2,
      //     hoverBackgroundColor: "rgb(53,117,201)",
      //     hoverBorderColor: "rgb(5,173,194)",
      //     data: [65, 59, 20, 81, 56, 55, 40],
      //   }]
      // },
      datacollection: {},
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
      options_B: {
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
      progress_Rate_A: 0,
      progress_Rate_B: 0,
      BigChart_Month: [],
      BigChart_Year: [],
      BigChart_Days: []
    }
  },
  mounted() {
    this.getAnalysisData()
  },
  methods: {
    getAnalysisData() {
      var date = new Date();
      const that = this
      axios.post(
          '/user/personalstatistics',
          {},
          {
            headers: {
              Authorization: window.sessionStorage.getItem('token')
            }
          }
      ).then(
          function (response) {
            console.log(response)
            if (response.data.code === 200) {
              // 饼状图
              that.doughnutdata = {
                labels: [
                  'totalFinishTaskNumber',
                  'totalOverdueTaskNumber',
                  'NotDoneYet'
                ],
                datasets: [{
                  label: 'My First Dataset',
                  data: [response.data.data.totalFinishTaskNumber, response.data.data.totalOverdueTaskNumber, response.data.data.totalTaskNumber - response.data.data.totalOverdueTaskNumber - response.data.data.totalFinishTaskNumber],
                  backgroundColor: [
                    'rgb(255, 99, 132)',
                    'rgb(54, 162, 235)',
                    'rgb(255, 205, 86)'
                  ],
                  hoverOffset: 4
                }]
              }

              let datacollection_A_data = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]

              for (let tmp in response.data.data.startTaskNumbersByMonths) {
                if (response.data.data.startTaskNumbersByMonths[tmp].year === date.getFullYear() && response.data.data.startTaskNumbersByMonths[tmp].month === date.getMonth() + 1) {
                  datacollection_A_data[date.getMonth()] = response.data.data.startTaskNumbersByMonths[tmp].count
                }
              }


              that.datacollection_A = {
                labels: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
                datasets: [{
                  label: "Dataset #1",
                  backgroundColor: "rgb(53,117,201)",
                  borderColor: "rgb(5,173,194)",
                  borderWidth: 2,
                  hoverBackgroundColor: "rgb(53,117,201)",
                  hoverBorderColor: "rgb(5,173,194)",
                  data: datacollection_A_data,
                }]
              }
              // another chart
              let datacollection_B_data = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]

              for (let tmp in response.data.data.finishTaskNumbersByMonths) {
                if (response.data.data.finishTaskNumbersByMonths[tmp].year === date.getFullYear() && response.data.data.finishTaskNumbersByMonths[tmp].month === date.getMonth() + 1) {
                  datacollection_B_data[date.getMonth()] = response.data.data.finishTaskNumbersByMonths[tmp].count
                }
              }

              that.datacollection = {
                labels: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
                datasets: [{
                  label: "Dataset #2",
                  backgroundColor: "rgb(53,117,201)",
                  borderColor: "rgb(5,173,194)",
                  borderWidth: 2,
                  hoverBackgroundColor: "rgb(53,117,201)",
                  hoverBorderColor: "rgb(5,173,194)",
                  data: datacollection_B_data,
                }]
              }

              // Now progress rate
              // that.progress_Rate_A = Math.ceil(((response.data.data.totalTaskNumber - response.data.data.totalOverdueTaskNumber - response.data.data.totalFinishTaskNumber) / response.data.data.totalTaskNumber) * 100)
              // that.progress_Rate_B = Math.ceil(((response.data.data.totalTaskNumber - response.data.data.totalFinishTaskNumber) / response.data.data.totalTaskNumber) * 100)
              // console.log(that.progress_Rate_A)
              that.progress_Rate_A = response.data.data.totalFinishTeamTaskNumber/response.data.data.totalTeamTaskNumber
              that.progress_Rate_B = (response.data.data.totalFinishTeamTaskNumber - response.data.data.totalOverdueTeamTaskNumber)/response.data.data.totalFinishTeamTaskNumber
              // Big Chart
              // due
              let month_due = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
              let year_due = [0, 0, 0, 0, 0]
              let day_due = new Array(32).fill(0);

              // finish
              let month_finish = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
              let year_finish = [0, 0, 0, 0, 0]
              let day_finish = new Array(32).fill(0);

              // start
              let month_start = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
              let year_start = [0, 0, 0, 0, 0]
              let day_start = new Array(32).fill(0);

              // due
              for (let i in response.data.data.dueTaskNumberByYears) {
                if (Math.abs(response.data.data.dueTaskNumberByYears[i].year - date.getFullYear()) <= 2) {
                  year_due[2 + response.data.data.dueTaskNumberByYears[i].year - date.getFullYear()] = response.data.data.dueTaskNumberByYears[i].count
                }
              }
              for (let i in response.data.data.dueTaskNumbersByMonths) {
                if (response.data.data.dueTaskNumbersByMonths[i].year === date.getFullYear()) {
                  month_due[response.data.data.dueTaskNumbersByMonths[i].month] = response.data.data.dueTaskNumbersByMonths[i].count
                }
              }
              for (let i in response.data.data.dueTaskNumbersByDays) {
                if (response.data.data.dueTaskNumbersByDays[i].year === date.getFullYear() && response.data.data.dueTaskNumbersByDays[i].month === date.getMonth() + 1) {
                  day_due[response.data.data.dueTaskNumbersByDays[i].day] = response.data.data.dueTaskNumbersByDays[i].count
                }
              }

              // finish
              for (let i in response.data.data.finishTaskNumberByYears) {
                if (Math.abs(response.data.data.finishTaskNumbersByMonths[i].year - date.getFullYear()) <= 2) {
                  year_finish[2 + response.data.data.finishTaskNumbersByMonths[i].year - date.getFullYear()] = response.data.data.finishTaskNumbersByMonths[i].count
                }
              }
              for (let i in response.data.data.finishTaskNumbersByMonths) {
                if (response.data.data.finishTaskNumbersByMonths[i].year === date.getFullYear()) {
                  month_finish[response.data.data.finishTaskNumbersByMonths[i].month] = response.data.data.finishTaskNumbersByMonths[i].count
                }
              }
              for (let i in response.data.data.finishTaskNumbersByDays) {
                if (response.data.data.finishTaskNumbersByDays[i].year === date.getFullYear() && response.data.data.finishTaskNumbersByDays[i].month === date.getMonth() + 1) {
                  day_finish[response.data.data.finishTaskNumbersByDays[i].day] = response.data.data.finishTaskNumbersByDays[i].count
                }
              }

              // start
              for (let i in response.data.data.startTaskNumberByYears) {
                if (Math.abs(response.data.data.startTaskNumbersByMonths[i].year - date.getFullYear()) <= 2) {
                  year_start[2 + response.data.data.startTaskNumbersByMonths[i].year - date.getFullYear()] = response.data.data.startTaskNumbersByMonths[i].count
                }
              }
              for (let i in response.data.data.startTaskNumbersByMonths) {
                if (response.data.data.startTaskNumbersByMonths[i].year === date.getFullYear()) {
                  month_start[response.data.data.startTaskNumbersByMonths[i].month] = response.data.data.startTaskNumbersByMonths[i].count
                }
              }
              for (let i in response.data.data.startTaskNumbersByDays) {
                if (response.data.data.startTaskNumbersByDays[i].year === date.getFullYear() && response.data.data.startTaskNumbersByDays[i].month === date.getMonth() + 1) {
                  day_start[response.data.data.startTaskNumbersByDays[i].day] = response.data.data.startTaskNumbersByDays[i].count
                }
              }

              that.BigChart_Month = [month_start, month_due, month_finish]
              that.BigChart_Year = [year_start, year_due, year_finish]
              that.BigChart_Days = [day_start, day_due, day_finish]
              that.totalStart = response.data.data.totalTaskNumber
              that.totalFinish = response.data.data.totalFinishTaskNumber
              that.totalDue = response.data.data.totalOverdueTaskNumber
              // console.log(that.totalDue)
              that.Father_Mount = true
            }
          },
          function (err) {
            that.$notify({
              message: 'server error',
              type: 'error'
            })
          }
      )
    },
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
