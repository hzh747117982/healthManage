<template>
  <div>
    <h1>{{this.checkIn.patientName}}的打卡信息</h1>
    <el-card>
      <div slot="header">
        打卡信息
      </div>
      <el-row>
        <el-col :span="6">病人名：</el-col>
        <el-col :span="18">{{ checkIn.patientName }}</el-col>
      </el-row>
      <el-row>
        <el-col :span="6">电话：</el-col>
        <el-col :span="18">{{ checkIn.phone }}</el-col>
      </el-row>
      <el-row>
        <el-col :span="6">药品信息：</el-col>
        <el-col :span="18">{{ checkIn.medicineName }} {{checkIn.count}}副</el-col>
      </el-row>
      <!-- 其他用户信息 -->
    </el-card>
    <div ref="myChart" style="width: 400px; height: 400px; background-color: #ffffff; padding: 20px; border-radius: 20px;"></div>
    <div>
      <el-button type="primary" :disabled="checkIn.checked === '1' || checkIn.patientId != this.user.userId" @click="doCheck">
        {{(checkIn.patientId === this.user.userId) ?(checkIn.checked === '1' ? '已打卡':'打卡'):'只可本人打卡'}}</el-button>
    </div>

  </div>
</template>
<script>

import * as echarts from 'echarts'
export default {

  name: "CheckInDatail",
  data(){
    return{
      user:JSON.parse(sessionStorage.getItem('CurUser')),
      id:"",
      notice:"",
      checkIn:{},
      date:''
    }
  },
  methods:{
    async loadCheckIn() {
       await this.$axios.get('http://localhost:8188/checkIn/findCheck?id='+this.id).then(res => res.data).then(res => {
        console.log(res.data)
        if (res.code === 200) {
          this.checkIn = res.data
        } else {
          alert("获取失败")
        }
      })
    },
    doCheck(){
      this.$axios.get('http://localhost:8188/checkIn/doCheck?id='+this.id).then(res => res.data).then(res => {
        console.log(res.data)
        if (res.code === 200) {
          this.checkIn = res.data
        } else if (res.code === 600){
          alert("还未到打卡时间")
        }else {
          alert("超过打卡时间15分钟,下次请准时打卡吧")
        }
      })
    }
  },
   async mounted() {

    this.id = this.$route.query.id
     await this.loadCheckIn()
    this.myChart = echarts.init(this.$refs.myChart)
    this.myChart.setOption({
      title: {
        text: this.checkIn.checkIn+this.checkIn.notCheckIn,
        subtext: '总打卡数',
        left: 'center',
        top: '43%',
        subtextStyle: {
          fontSize: 18
        }
      },
      tooltip: {
        trigger: 'item'
      },
      legend: {
        icon: 'circle',
        top: '0',
        left: 'right'
      },
      series: [
        {
          name: '',
          type: 'pie',
          radius: ['40%', '55%'],
          label: {
            show: true,
            padding: [0, -60],
            overflow: 'none',
            fontSize: '15',
            fontWeight: 'bold',
            formatter: '{d}%\n\n{c}'
          },
          labelLine: {
            show: true,
            length: 15,
            length2: 60
          },
          itemStyle: {
            normal: {
              color: function (params) {
                var colorList = ['#00C853', '#DC143C', '#F57F17']
                return colorList[params.dataIndex]
              }
            }
          },
          data: [
            { name: '已打卡', value: this.checkIn.checkIn },
            { name: '未打卡', value: this.checkIn.notCheckIn },
          ]
        }
      ]
    })
  }
}
</script>

<style scoped>

</style>