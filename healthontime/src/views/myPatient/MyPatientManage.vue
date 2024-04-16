<template>
  <div>
    <div style="margin-bottom: 5px;">
      <el-input v-model="name" placeholder="请输入病人名" suffix-icon="el-icon-search" style="width: 200px;"
                @keyup.enter.native="loadGet"></el-input>

      <el-button type="primary" style="margin-left: 5px;" @click="loadGet">查询病人</el-button>
      <el-button type="success" @click="resetParam">重置</el-button>
      <el-button type="primary" style="margin-left: 5px;" @click="add">新增病人</el-button>
      <el-button type="danger" style="margin-left: 5px;" @click="inGoods">记录打卡</el-button>
    </div>
    <el-table :data="tableData"
              :header-cell-style="{background: '#f2f5fc' , color: '#555555'}"
              highlight-current-row
              @current-change="selectCurrentChange"
              border>
      <el-table-column prop="id" label="Id" width="60">
      </el-table-column>

      <el-table-column prop="patientName" label="病人名" width="180">
      </el-table-column>
      <el-table-column   prop="phone" label="手机号" width="180" >
      </el-table-column>
      <el-table-column  label="性别" width="180" :formatter="formatSex">
      </el-table-column>
      <el-table-column label="年龄" :formatter="formatAge">
      </el-table-column>
      <el-table-column prop="remark" label="备注">
      </el-table-column>
      <el-table-column prop="operate" label="操作" width="180">
        <template slot-scope="scope">
          <el-popconfirm title="这是一段内容确定删除吗？" @confirm="del(scope.row)" style="margin-left: 5px">
            <el-button slot="reference" size="small" type="danger">删除</el-button>
          </el-popconfirm>
        </template>

      </el-table-column>
    </el-table>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-size="pageSize"
        :page-sizes="[5, 10]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>
    <el-dialog
        title="提示"
        :visible.sync="centerDialogVisible"
        width="30%"
        center>
      <el-form ref="form" :rules="rules" :model="form" label-width="100px">

        <el-form-item label="病人手机号" prop="phone">
          <el-input v-model="form.phone"></el-input>
        </el-form-item>

        <el-form-item label="本人关系" prop="remark">
          <el-input type="textarea" v-model="form.remark"></el-input>
        </el-form-item>

      </el-form>

      <span slot="footer" class="dialog-footer">
    <el-button @click="centerDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="save">确 定</el-button>
  </span>
    </el-dialog>
<!--    记录打卡 -->
    <el-dialog
        title="记录打卡"
        :visible.sync="inDialogVisible"
        width="30%"
        center>
      <el-form ref="form1" :rules="rules1" :model="form1" label-width="100px">

        <el-form-item label="病人名">
          <el-input v-model="form1.name" readonly></el-input>
        </el-form-item>
        <el-form-item label="病人电话">
          <el-input v-model="form1.phone" readonly ></el-input>
        </el-form-item>
        <el-form-item label="选择药品" prop="count">
          <el-input v-model="form1.name1" @click.native="selectUser"></el-input>
        </el-form-item>
        <el-form-item label="药品数量" prop="count">
          <el-input v-model="form1.count" readonly></el-input>
        </el-form-item >
        <el-form-item label="提醒天数" prop="count">
          <el-input-number v-model="form1.num" @change="handleChange" :min="1" :max="10" label="请选择天数"></el-input-number>
        </el-form-item >
        <el-form-item label="选择时间1">
          <el-time-picker
              v-model="form1.timeValue1"
              :picker-options="{  selectableRange: '00:00:00 - 23:59:59'}"
              placeholder="任意时间点">
          </el-time-picker>
        </el-form-item >
        <el-form-item label="选择时间2">
          <el-time-picker
              v-model="form1.timeValue2"
              :picker-options="{  selectableRange: '00:00:00 - 23:59:59'}"
              placeholder="任意时间点">
          </el-time-picker>
        </el-form-item >
        <el-form-item label="选择时间3">
          <el-time-picker
              v-model="form1.timeValue3"
              :picker-options="{  selectableRange: '00:00:00 - 23:59:59'}"
              placeholder="任意时间点">
          </el-time-picker>
        </el-form-item >

        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="form1.remark"></el-input>
        </el-form-item>

      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="inDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="doInGoods">确 定</el-button>
  </span>
    </el-dialog>
    <el-dialog
        width="75%"
        title="用户选择"
        :visible.sync="innerVisible"

       >
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
      <span slot="footer" class="dialog-footer">
    <el-button @click="innerVisible = false">取 消</el-button>
    <el-button type="primary"  @click="confirmUser">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>
import SelectUser from "../user/SelectUser";
import SelectMedicine from "@/views/medicine/SelectMedicine";

export default {
  name: "MyPatientManage",
  components: {SelectMedicine, SelectUser},
  data() {
    let checkCount = (rule,value,callback) =>{
      if(value>9999){
        callback(new Error("数量输入过大"));
      }else{
        callback();
      }
    };
    let checkDuplicate = (rule, value, callback) => {
      if (this.form.id) {
        return callback();
      }
      this.$axios.get('http://localhost:8188/user/findByPhone?phone='+this.form.phone).then(res=>res.data).then(res => {
        if (res.code == 200) {
          callback();
        } else {
          callback(new Error('该电话号还未注册'));
        }
      })
    };
    return {
      user:JSON.parse(sessionStorage.getItem('CurUser')),
      storageDatas:[],
      timeValue1:"",
      timeValue2:"",
      timeValue3:"",
      goodstypeDatas:[],
      medicineTypes:"",
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
      UserData:[],
      storageData:[],
      goodstypeData:[],
      storage:'',
      goodstype:'',
      tableData: [],
      pageNum: 1,
      pageSize: 5,
      total: 0,
      name: "",
      currentRow:{},
      centerDialogVisible: false,
      inDialogVisible: false,
      innerVisible:false,
      tempUsr:'',
      form: {
        id:"",
        phone: "",
        remark: "",
        manageId:"",
        patientId:"",
      },
      form1:{
        num:'',
        id:'',
        name:'',
        name1:'',
        phone:'',
        medicineType:"",
        patientId:'',
        count:"",
        remark:'',
        manageId:"",
        timeValue1:"",
        timeValue2:"",
        timeValue3:"",
      },
      rules1:{

      },
      rules: {
        name: [
          {required: true, message: '请输入病人名', trigger: 'blur'},
        ],
        goodstype:[
          {required: true, message: '请选择分类', trigger: 'blur'},
        ],
        storage:[
          {required: true, message: '请选择仓库', trigger: 'blur'},
        ],
        count: [
          {required: true, message: '请输入数量', trigger: 'blur'},
          {pattern: /^([1-9][0-9]*){1,4}$/, message: '数量必须为正整数', trigger: "blur"},
          {validator: checkCount, trigger: 'blur'},
        ],
        phone: [
          {required: true, message: '手机号不能为空', trigger: 'blur'},
          {pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: '请输入正确手机号', trigger: 'blur'},
          {validator: checkDuplicate, trigger: 'blur'},
        ],
      }
    }
  },
  methods: {
    confirmUser(){
      //console.log(this.tempUser.name)
      this.form1.name1 = this.currentRows.name
      this.form1.id = this.currentRows.id
      this.form1.medicineType = this.currentRows.medicineType
      this.form1.count = this.currentRows.count
      this.innerVisible = false
      console.log(this.form1.id)
    },
    selectUser(){
      this.innerVisible = true
      this.loadGoodstypes()
      this.loadGets()
    },
    selectCurrentChange(val) {
      this.currentRow = val;
    },
    formatPhone(row){
      let temp = this.UserData.find(item=>{
        return item.userId == row.patientId
      })
      return temp && temp.phone
    },
    formatSex(row){
      let temp = this.UserData.find(item=>{
        return item.userId == row.patientId
      })
      if(temp.sex === 1){
        return "男"
      }
      return "女"
    },
    formatAge(row){
      let temp = this.UserData.find(item=>{
        return item.userId == row.patientId
      })
      return temp && temp.age
    },
    resetForm() {
      this.$refs.form.resetFields()
    },
    resetInForm() {
      this.$refs.form1.resetFields()
    },
    add() {
      this.centerDialogVisible = true
      this.$nextTick(() => {
        this.resetForm();
        this.form.id = '';
      })
    },
    inGoods(){
      if(!this.currentRow.id){
        alert("请选择记录")
        return
      }
      this.inDialogVisible = true
      this.$nextTick(() => {
        this.resetInForm();
        this.form.id = '';
      })
      this.form1.name1 = '',
      this.form1.name = this.currentRow.patientName
      this.form1.phone = this.currentRow.phone
      this.form1.id = this.currentRow.id
      this.form1.patientId = this.currentRow.patientId;
      this.form1.manageId = this.user.userId
      sessionStorage.setItem("CurPatient", JSON.stringify(this.form1))
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`)
      this.pageSize = val
      this.pageNum = '1'
      this.loadGet()
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.pageNum = val
      this.loadGet()
    },
    loadGet() {
      this.$axios.post('http://localhost:8188/myPatient/listPage', {
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        param: {
          manageId: this.user.userId+'',
          name:this.name,
        }
      }).then(res => res.data).then(res => {
        console.log(res.data)
        if (res.code === 200) {
          this.tableData = res.data
          this.total = res.total
        } else {
          alert("获取失败")
        }
      })
    },
    loadUser() {
      this.$axios.get('http://localhost:8188/user/list').then(res => res.data).then(res => {
        console.log(res.data)
        if (res.code === 200) {
          this.UserData = res.data
        } else {
          alert("获取失败")
        }
      })
    },
    loadGoodstype() {
      this.$axios.get('http://localhost:8188/myPatient/list').then(res => res.data).then(res => {
        console.log(res.data)
        if (res.code === 200) {
          this.goodstypeData = res.data
        } else {
          alert("获取失败")
        }
      })
    },
    resetParam() {
      this.name = ''
      this.goodstype = ''
      this.storage = ''
      this.loadGet()
    },
    doInGoods(){
      console.log(this.form1)
      this.$axios.post('http://localhost:8188/checkIn/save',this.form1).then(res=>res.data).then(res=>{
        console.log(res)
        if(res.code==200){

          this.$message({
            message: '操作成功！',
            type: 'success'
          });
          this.inDialogVisible = false
          this.loadGet()
          this. resetInForm()
        }else{
          this.$message({
            message: '操作失败！',
            type: 'error'
          });
        }

      })
    },
    save() {
      this.form.manageId = this.user.userId
      console.log(this.form)
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.$axios.post('http://localhost:8188/myPatient/saveOrMod', this.form).then(res => res.data).then(res => {
            console.log(this.form)
            if (res.code === 200) {
              this.tableData = res.data
              this.total = res.total
              this.$message({
                message: '操作成功',
                type: "success",
              })
              this.centerDialogVisible = false
              this.loadGet()
            } else {
              this.$message({
                message: '操作失败',
                type: "error",
              })
            }
          })
        } else {
          return false;
        }
      });
    },
    del(row){
      if(row.patientId == this.user.userId){
        this.$message({
          message: '不能删除自己',
          type: "error",
        })
        return
      }
      this.$axios.get('http://localhost:8188/myPatient/delete?id='+row.id).then(res => res.data).then(res => {
        console.log(res.data)
        if (res.code === 200) {
          this.tableData = res.data
          this.total = res.total
          this.$message({
            message: '删除成功',
            type: "success",
          })
          this.loadGet()
        } else {
          this.$message({
            message: '删除失败',
            type: "error",
          })
        }
      })
    },
    doSelectUsers(val) {
      console.log(val)
      this.tempUser = val;
    },
    selectUsers() {
      this.innerVisible1 = true
    },
    selectCurrentChanges(val) {
      this.currentRows = val;
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
          possessorId: this.form1.patientId + "",
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
  },
  created() {
    this.loadUser()
    this.loadGoodstype()
    this.loadGet()
    this.loadGoodstypes()
    this.loadGets()
  }
}
</script>

<style scoped>

</style>
