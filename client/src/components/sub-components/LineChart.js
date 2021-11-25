import { Line, mixins } from 'vue-chartjs'
const { reactiveProp } = mixins

export default {
    extends: Line,
    mixins: [reactiveProp],
    props: ['options', 'show'],
    watch:{
        show(newValue, oldValue) {
            // console.log(this.chartData)
            this.renderChart(this.chartData, this.options)
        }
    },
    mounted () {

        // this.chartData is created in the mixin.
        // If you want to pass options please create a local options object
        if(this.show){
            this.renderChart(this.chartData, this.options)
        }
    }
}
