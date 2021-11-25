<template>
  <div class="bigchart-wrapper">
    <el-tabs v-model="activeName" @tab-click="handleClick">

      <el-tab-pane label="本年" name="first">
        <el-row :gutter="20">
          <el-col :span="15">
            <div class="chart-wrapper-t">
              <line-chart :chart-data="datacollection_Month" :options="options" class="chart"
                          :show="this.Father_Mount"></line-chart>
            </div>
          </el-col>

          <el-col :span="5">
            <el-form>
              <el-form-item>
                <el-row :gutter="10">
                  <!--                  <el-col :span="25">-->
                  <!--                    <el-radio-group v-model="radio1">-->
                  <!--                      <el-radio-button label="本周"></el-radio-button>-->
                  <!--                      <el-radio-button label="本月"></el-radio-button>-->
                  <!--                      <el-radio-button label="本年"></el-radio-button>-->
                  <!--                    </el-radio-group>-->
                  <!--                  </el-col>-->
                  <!--                  <el-col :span="5">-->
                  <!--                    <el-date-picker-->
                  <!--                        v-model="value2"-->
                  <!--                        type="daterange"-->
                  <!--                        align="right"-->
                  <!--                        unlink-panels-->
                  <!--                        range-separator="至"-->
                  <!--                        start-placeholder="开始日期"-->
                  <!--                        end-placeholder="结束日期"-->
                  <!--                        :picker-options="pickerOptions">-->
                  <!--                    </el-date-picker>-->
                  <!--                  </el-col>-->
                </el-row>
              </el-form-item>

              <el-form-item>
                <div v-for="o in 4" :key="o" class="text item">
                  {{ o + '. dxbdltql' }}
                </div>
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
      </el-tab-pane>

      <el-tab-pane label="近五年" name="second">
        <el-row :gutter="20">
          <el-col :span="15">
            <div class="chart-wrapper-t">
              <line-chart :chart-data="datacollection_Year" :options="options" class="chart"
                          :show="this.Father_Mount"></line-chart>
            </div>
          </el-col>

          <el-col :span="5">
            <el-form>
              <el-form-item>
                <el-row :gutter="10">
                </el-row>
              </el-form-item>

              <el-form-item>
                <div v-for="o in 4" :key="o" class="text item">
                  {{ o + '. dxbdltql' }}
                </div>
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="本月" name="third">
        <el-row :gutter="20">
          <el-col :span="15">
            <div class="chart-wrapper-t">
              <line-chart :chart-data="datacollection_Day" :options="options" class="chart" :show="this.Father_Mount"></line-chart>
            </div>
          </el-col>

          <el-col :span="5">
            <el-form>
              <el-form-item>
                <el-row :gutter="10">
                </el-row>
              </el-form-item>

              <el-form-item>
                <div v-for="o in 4" :key="o" class="text item">
                  {{ o + '. dxbdltql' }}
                </div>
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
import BigChartCard from "./BigChartCard";
import LineChart from "./LineChart";

export default {
  name: 'BigChart',
  components: {LineChart},
  props: ['Month_data', 'Year_data', 'Day_data', 'Father_Mount'],
  mounted() {
    for (let i = 0; i < 32; i++) {
      this.list_day.push(i.toString())
    }
    // console.log(this.list_day)
    // console.log(this.Father_Mount)
  },
  watch: {
    Father_Mount(newValue, oldValue) {
      // console.log(this.datacollection_Day)
      this.datacollection_Month.datasets[0].data = this.Month_data[0]
      this.datacollection_Month.datasets[1].data = this.Month_data[1]
      this.datacollection_Month.datasets[2].data = this.Month_data[2]

      this.datacollection_Year.datasets[0].data = this.Year_data[0]
      this.datacollection_Year.datasets[1].data = this.Year_data[1]
      this.datacollection_Year.datasets[2].data = this.Year_data[2]

      this.datacollection_Day.datasets[0].data = this.Day_data[0]
      this.datacollection_Day.datasets[1].data = this.Day_data[1]
      this.datacollection_Day.datasets[2].data = this.Day_data[2]
      this.datacollection_Day.labels = this.list_day
    }
  },
  data() {
    return {
      list_day: [],
      radio1: '本年',
      activeName: 'first',
      datacollection_Month: {
        labels: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
        datasets: [{
          label: "start",
          // backgroundColor: "rgb(53,117,201)",
          borderColor: "rgb(5,173,194)",
          borderWidth: 2,
          hoverBackgroundColor: "rgb(53,117,201)",
          hoverBorderColor: "rgb(5,173,194)",
          data: this.Month_data[0],
        },
          {
            label: "due",
            // backgroundColor: "rgb(53,117,201)",
            borderColor: "rgb(164,5,5)",
            borderWidth: 2,
            hoverBackgroundColor: "rgb(164,5,5)",
            hoverBorderColor: "rgb(162,55,55)",
            data: this.Month_data[1],
          },
          {
            label: "finish",
            // backgroundColor: "rgb(53,117,201)",
            borderColor: "rgb(5,5,5)",
            borderWidth: 2,
            hoverBackgroundColor: "rgb(5,5,5)",
            hoverBorderColor: "rgba(175,175,175,0.26)",
            data: this.Month_data[2],
          }
        ]
      },
      datacollection_Year: {
        labels: ['前年', '去年', '今年', '明年', '后年'],
        datasets: [{
          label: "start",
          // backgroundColor: "rgb(53,117,201)",
          borderColor: "rgb(5,173,194)",
          borderWidth: 2,
          hoverBackgroundColor: "rgb(53,117,201)",
          hoverBorderColor: "rgb(5,173,194)",
          data: this.Year_data[0],
        },
          {
            label: "due",
            // backgroundColor: "rgb(53,117,201)",
            borderColor: "rgb(164,5,5)",
            borderWidth: 2,
            hoverBackgroundColor: "rgb(164,5,5)",
            hoverBorderColor: "rgb(162,55,55)",
            data: this.Year_data[1],
          },
          {
            label: "finish",
            // backgroundColor: "rgb(53,117,201)",
            borderColor: "rgb(5,5,5)",
            borderWidth: 2,
            hoverBackgroundColor: "rgb(5,5,5)",
            hoverBorderColor: "rgba(175,175,175,0.26)",
            data: this.Year_data[2],
          },
        ]
      },
      datacollection_Day: {
        labels: this.list_day,
        datasets: [{
          label: "start",
          // backgroundColor: "rgb(53,117,201)",
          borderColor: "rgb(5,173,194)",
          borderWidth: 2,
          hoverBackgroundColor: "rgb(53,117,201)",
          hoverBorderColor: "rgb(5,173,194)",
          data: this.Day_data[0],
        },
          {
            label: "due",
            // backgroundColor: "rgb(53,117,201)",
            borderColor: "rgb(164,5,5)",
            borderWidth: 2,
            hoverBackgroundColor: "rgb(164,5,5)",
            hoverBorderColor: "rgb(162,55,55)",
            data: this.Day_data[1],
          },
          {
            label: "finish",
            // backgroundColor: "rgb(53,117,201)",
            borderColor: "rgb(5,5,5)",
            borderWidth: 2,
            hoverBackgroundColor: "rgb(5,5,5)",
            hoverBorderColor: "rgba(175,175,175,0.26)",
            data: this.Day_data[2],
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
