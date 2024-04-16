<template>
  <div>
    <div style="margin-bottom: 5px;">
      <el-input v-model="names" placeholder="请输入药品名" suffix-icon="el-icon-search" style="width: 200px;"
                @keyup.enter.native="loadGets"></el-input>

      <el-select v-model="medicineTypes" placeholder="请选择药品分类" style="margin-left: 5px;">
        <el-option
            v-for="item in goodstypeDatas"
            :key="item.id"
            :label="item.name"
            :value="item.id">
        </el-option>
      </el-select>
      <el-button type="primary" style="margin-left: 5px;" @click="loadGets">查询药品</el-button>
      <el-button type="success" @click="resetParams">重置</el-button>

    </div>
    <el-table :data="tableDatas"
              :header-cell-style="{background: '#f2f5fc' , color: '#555555'}"
              highlight-current-row
              @current-change="selectCurrentChanges"
              border>
      <el-table-column prop="id" label="Id" width="60">
      </el-table-column>

      <el-table-column prop="name" label="药品名" width="230">
      </el-table-column>
      <el-table-column prop="goodstype" label="药品分类" width="230" :formatter="formatGoodstypes">
      </el-table-column>
      <el-table-column prop="count" label="数量" width="230">
      </el-table-column>
      <el-table-column prop="remark" label="备注">
      </el-table-column>

    </el-table>
    <el-pagination
        @size-change="handleSizeChanges"
        @current-change="handleCurrentChanges"
        :current-page="pageNums"
        :page-size="pageSizes"
        :page-sizes="[5, 10]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="totals">
    </el-pagination>
  </div>
</template>

<script>
export default {
  name: "SelectMedicine",
  props: {
    patientId:'',
  },
  data() {
    let checkCount = (rule,value,callback) =>{
      if(value>9999){
        callback(new Error("数量输入过大"));
      }else{
        callback();
      }
    };
    return {
      storageDatas:[],
      goodstypeDatas:[],
      medicineTypes:"",
      goodstype:'',
      tableDatas: [],
      pageNums: 1,
      pageSizes: 5,
      totals: 0,
      names: "",
      currentRows:{},
      centerDialogVisibles: false,
      inDialogVisibles: false,
      inDialogVisible1s: false,
      innerVisibles:false,
      innerVisible1s:false,
      tempUsr:'',
      forms: {
        medicineType:"",
        id: "",
        name: "",
        remark: "",
        count: 0,
        storage: '',
        possessorId:'',
      },
      form1s:{
        medicineId:"",
        medicineTypeId:'',
        goods:'',
        name:'',
        count:'',
        username:'',
        patientId:'',
        adminId:'',
        remark:'',
        action:''
      },
      rules1:{

      },
      rules: {
        name: [
          {required: true, message: '请输入物品名', trigger: 'blur'},
        ],
        medicineType:[
          {required: true, message: '请选择分类', trigger: 'blur'},
        ],
        storage:[
          {required: true, message: '请选择仓库', trigger: 'blur'},
        ],
        count: [
          {required: true, message: '请输入数量', trigger: 'blur'},
          {pattern: /^([1-9][0-9]*){1,4}$/, message: '数量必须为正整数', trigger: "blur"},
          {validator: checkCount, trigger: 'blur'},
        ]
      }
    }
  },
  methods: {
    doSelectUsers(val) {
      console.log(val)
      this.tempUser = val;
    },
    selectUsers() {
      this.innerVisible1 = true
    },
    selectCurrentChanges(val) {
      this.$emit("doSelectMedicine", val)
    },
    formatGoodstypes(row) {
      let temp = this.goodstypeDatas.find(item => {
        return item.id == row.medicineType
      })
      return temp.name
    },
    resetForms() {
      this.$refs.forms.resetFields()
    },
    resetInForms() {
      this.$refs.form1s.resetFields()
    },
    handleSizeChanges(val) {
      console.log(`每页 ${val} 条`)
      this.pageSize = val
      this.pageNum = 1
      this.loadGets()
    },
    handleCurrentChanges(val) {
      console.log(`当前页: ${val}`);
      this.pageNum = val
      this.loadGets()
    },
    loadGets() {
      this.$axios.post('http://localhost:8188/medicine/listPage', {
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        param: {
          name: this.names,
          medicineType: this.medicineTypes + '',
          possessorId: 10 + '',
        }
      }).then(res => res.data).then(res => {
        if (res.code === 200) {
          this.tableDatas = res.data
          this.totals = res.total
        } else {
          alert("获取失败")
        }
      })
    },
    loadGoodstypes() {
      this.$axios.get('http://localhost:8188/medicineType/list').then(res => res.data).then(res => {
        if (res.code === 200) {
          this.goodstypeDatas = res.data
        } else {
          alert("获取失败")
        }
      })
    },
    resetParams() {
      this.names = ''
      this.medicineTypes = ''
      this.loadGets()
    },
  }, created() {
    console.log(this.patientId)
    this.loadGoodstypes()
    this.loadGets()
  }
}
</script>

<style scoped>

</style>
