<template>
  <div>
    <div style="margin-bottom: 5px;">
      <el-input v-model="name" placeholder="请输入药品名" suffix-icon="el-icon-search" style="width: 200px;"
                @keyup.enter.native="loadGet"></el-input>

      <el-select v-model="medicineType" placeholder="请选择药品分类" style="margin-left: 5px;">
        <el-option
            v-for="item in goodstypeData"
            :key="item.id"
            :label="item.name"
            :value="item.id">
        </el-option>
      </el-select>
      <el-button type="primary" style="margin-left: 5px;" @click="loadGet">查询药品</el-button>
      <el-button type="success" @click="resetParam">重置</el-button>
      <el-button type="primary" style="margin-left: 5px;" @click="add">新增药品</el-button>
      <el-button type="warning" style="margin-left: 5px;" @click="inGoods">药品入库</el-button>
      <el-button type="danger" style="margin-left: 5px;" @click="outGoods">开药</el-button>
    </div>
    <el-table :data="tableData"
              :header-cell-style="{background: '#f2f5fc' , color: '#555555'}"
              highlight-current-row
              @current-change="selectCurrentChange"
              border>
      <el-table-column prop="id" label="Id" width="60">
      </el-table-column>

      <el-table-column prop="name" label="药品名" width="180">
      </el-table-column>
      <el-table-column prop="goodstype" label="药品分类" width="180" :formatter="formatGoodstype">
      </el-table-column>
      <el-table-column prop="count" label="数量" width="180">
      </el-table-column>
      <el-table-column prop="remark" label="备注">
      </el-table-column>
      <el-table-column prop="operate" label="操作" width="180">
        <template slot-scope="scope">
          <el-button size="small" type="success" @click="mod(scope.row)">编辑</el-button>
          <el-popconfirm title="这是一段内容确定删除吗？" @confirm="del(scope.row.id)" style="margin-left: 5px">
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
      <el-form ref="form" :rules="rules" :model="form" label-width="80px">

        <el-form-item label="物品名" prop="name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>

        <el-form-item label="分类" prop="goodstype">
          <el-select v-model="form.medicineType" placeholder="请选择分类" style="margin-left: 5px;" readonly>
            <el-option
                v-for="item in goodstypeData"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="数量" prop="count">
          <el-input v-model="form.count"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="form.remark"></el-input>
        </el-form-item>

      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="centerDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="save">确 定</el-button>
  </span>
    </el-dialog>

    <el-dialog
        title="入库"
        :visible.sync="inDialogVisible"
        width="30%"
        center>

      <el-form ref="form1" :rules="rules1" :model="form1" label-width="100px">

        <el-form-item label="药品名">
          <el-input v-model="form1.name" readonly></el-input>
        </el-form-item>
        <el-form-item label="药品进入人">
          <el-input v-model="this.user.name"></el-input>
        </el-form-item>
        <el-form-item label="数量" prop="count">
          <el-input v-model="form1.count"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="form1.remark"></el-input>
        </el-form-item>

      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="inDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="doInGoods">确 定</el-button>
  </span>
    </el-dialog>

<!--    出库-->
    <el-dialog
        title="开药"
        :visible.sync="inDialogVisible1"
        width="30%"
        center>

      <el-dialog
          width="75%"
          title="用户选择"
          :visible.sync="innerVisible1"
          append-to-body>
        <SelectUser @doSelectUser="doSelectUser"></SelectUser>
        <span slot="footer" class="dialog-footer">
    <el-button @click="innerVisible1 = false">取 消</el-button>
    <el-button type="primary" @click="confirmUser">确 定</el-button>
  </span>
      </el-dialog>
      <el-form ref="form1" :rules="rules1" :model="form1" label-width="100px">

        <el-form-item label="药品名">
          <el-input v-model="form1.name" readonly></el-input>
        </el-form-item>
        <el-form-item label="病人">
          <el-input v-model="form1.username" readonly @click.native="selectUser"></el-input>
        </el-form-item>
        <el-form-item label="数量" prop="count">
          <el-input v-model="form1.count"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="form1.remark"></el-input>
        </el-form-item>

      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="inDialogVisible1 = false">取 消</el-button>
    <el-button type="primary" @click="doOutGoods">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>
import SelectUser from "../user/SelectUser";

export default {
  name: "MedicineManage",
  components: {SelectUser},
  data() {
    let checkCount = (rule,value,callback) =>{
      if(value>9999){
        callback(new Error("数量输入过大"));
      }else{
        callback();
      }
    };
    return {
      user:JSON.parse(sessionStorage.getItem('CurUser')),
      storageData:[],
      goodstypeData:[],
      medicineType:"",
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
      inDialogVisible1: false,
      innerVisible:false,
      innerVisible1:false,
      tempUsr:'',
      form: {
        medicineType:"",
        id: "",
        name: "",
        remark: "",
        count: 0,
        storage: '',
        possessorId:'',
      },
      form1:{
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
    doSelectUser(val){
      console.log(val)
      this.tempUser = val;
    },
    confirmUser(){
      this.form1.username = this.tempUser.patientName
      this.form1.patientId = this.tempUser.patientId
      this.innerVisible1 = false
    },
    selectUser(){
      this.innerVisible1 = true
    },
    selectCurrentChange(val) {
      this.currentRow = val;
    },
    formatGoodstype(row){
      let temp = this.goodstypeData.find(item=>{
        return item.id == row.medicineType
      })
      return temp.name
    },
    resetForm() {
      this.$refs.form.resetFields()
    },
    resetInForm() {
      this.$refs.form1.resetFields()
    },
    add() {
      if(this.user.roleId == 2){
        this.$message({
          message: '权限不足！',
          type: 'error'
        });
        return;
      }
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
      if(this.user.roleId == 2){
      this.$message({
        message: '权限不足！',
        type: 'error'
      });
      return;
    }
      this.inDialogVisible = true
      this.$nextTick(() => {
        this.resetInForm();
        this.form.id = '';
      })
      this.form1.name = this.currentRow.name
      this.form1.medicineTypeId = this.currentRow.medicineType
      this.form1.medicineId = this.currentRow.id
      this.form1.username = this.user.name
      this.form1.adminId = this.user.userId
      //this.form1.goods = this.currentRow.id
      this.form1.action = '1'
    },
    outGoods(){
      this.form1.username = ''
      if(!this.currentRow.id){
        alert("请选择记录")
        return
      }
      if(this.user.roleId == 2){
        this.$message({
          message: '权限不足！',
          type: 'error'
        });
        return;
      }
      this.inDialogVisible1 = true
      this.$nextTick(() => {
        this.resetInForm();
        this.form.id = '';
      })
      this.form1.name = this.currentRow.name
      this.form1.medicineTypeId = this.currentRow.medicineType
      this.form1.medicineId = this.currentRow.id
      //this.form1.username = this.user.name
      this.form1.adminId = this.user.userId
      //this.form1.goods = this.currentRow.id
      this.form1.action = '2'
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`)
      this.pageSize = val
      this.pageNum = 1
      this.loadGet()
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.pageNum = val
      this.loadGet()
    },
    loadGet() {
      this.$axios.post('http://localhost:8188/medicine/listPage', {
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        param: {
          name: this.name,
          medicineType:this.medicineType+'',
          possessorId: this.user.userId+'',
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
    loadGoodstype() {
      this.$axios.get('http://localhost:8188/medicineType/list').then(res => res.data).then(res => {
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
      this.medicineType = ''
      this.storage = ''
      this.loadGet()
    },
    doInGoods(){
      this.form1.patientId = this.user.userId
      this.$axios.post('http://localhost:8188/record/save',this.form1).then(res=>res.data).then(res=>{
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
    doOutGoods(){
      console.log(this.form1)
      console.log(this.form1.medicineTypeId+'')
      this.$axios.post('http://localhost:8188/record/save',this.form1).then(res=>res.data).then(res=>{
        console.log(res)
        if(res.code==200){

          this.$message({
            message: '操作成功！',
            type: 'success'
          });
          this.inDialogVisible1 = false
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
      this.form.possessorId = this.user.userId
      console.log(this.form)
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.$axios.post('http://localhost:8188/medicine/saveOrMod', this.form).then(res => res.data).then(res => {
            console.log(res.data)
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
    mod(row){
      this.centerDialogVisible=true
      this.$nextTick(()=>{
        this.form.id=row.id
        this.form.name=row.name
        this.form.remark=row.remark
        this.form.goodstype=row.goodstype
        this.form.storage=row.storage
        this.form.count=row.count
      })
    },
    del(id){
      this.$axios.get('http://localhost:8188/medicine/delete?id='+id).then(res => res.data).then(res => {
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
  },
  created() {
    this.loadGoodstype()
    this.loadGet()
  }
}
</script>

<style scoped>

</style>
