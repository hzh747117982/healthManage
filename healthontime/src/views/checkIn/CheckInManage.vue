<template>
  <div>
    <div style="margin-bottom: 5px;">
      <el-input v-model="name" placeholder="请输入病人名" suffix-icon="el-icon-search" style="width: 200px;"
                @keyup.enter.native="loadGet"></el-input>

      <el-button type="primary" style="margin-left: 5px;" @click="loadGet">查询药品分类</el-button>
      <el-button type="success" @click="resetParam">重置</el-button>
      <el-button type="primary" style="margin-left: 5px;" @click="add">新增类别</el-button>
    </div>
    <el-table :data="tableData"
              :header-cell-style="{background: '#f2f5fc' , color: '#555555'}"
              @row-click="handleClick"
              border>
      <el-table-column prop="id" label="Id" width="60">
      </el-table-column>

      <el-table-column prop="patientName" label="病人名" width="180">
      </el-table-column>
      <el-table-column prop="phone" label="病人电话" width="180">
      </el-table-column>
      <el-table-column prop="medicineName" label="药品名" width="180" >
      </el-table-column>
      <el-table-column prop="count" label="药品数量" width="180" >
      </el-table-column>
      <el-table-column prop="notCheckIn" label="未打卡次数" width="180" >
      </el-table-column>
      <el-table-column prop="checkIn" label="已打卡次数" width="180" >
      </el-table-column>
      <el-table-column prop="remark" label="备注">
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

        <el-form-item label="分类名" prop="name">
          <el-input v-model="form.name"></el-input>
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
  </div>
</template>

<script>
export default {
  name: "checkInManage",
  data() {
    return {
      allUser:[],
      tableData: [],
      pageNum: 1,
      pageSize: 5,
      total: 0,
      name: "",
      user:JSON.parse(sessionStorage.getItem("CurUser")),
      centerDialogVisible: false,
      form: {
        id:"",
        name: "",
        remark:"",
      },
      rules: {
        name: [
          {required: true, message: '请输入分类名', trigger: 'blur'},
          {min: 3, max: 8, message: '长度在 3 到 8 个字符', trigger: 'blur'},
        ],
      }
    }
  },
  methods: {
    resetForm() {
      this.$refs.form.resetFields()
    },
    formatPatientName(row){
      let temp = this.allUser.find(item=>{
        return item.userId == row.patientId
      })
      return temp && temp.name
    },
    add() {
      this.centerDialogVisible = true
      this.$nextTick(() => {
        this.resetForm();
      })
    },
    handleClick(val){
      this.$router.push({
        path: "/CheckInDatail",
        query: {
          id: val.id,
        },
      });
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
      this.$axios.post('http://localhost:8188/checkIn/listPage', {
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        param: {
          manageId: this.user.userId,
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
    resetParam() {
      this.name = ''
      this.loadGet()
    },
    save() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.$axios.post('http://localhost:8188/checkIn/save', this.form).then(res => res.data).then(res => {
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
    loadUser() {
      this.$axios.get('http://localhost:8188/user/list').then(res => res.data).then(res => {
        console.log(res.data)
        if (res.code === 200) {
          this.allUser = res.data
        } else {
          alert("获取失败")
        }
      })
    },
    mod(row){
      this.centerDialogVisible=true
      this.$nextTick(()=>{
        this.form.id=row.id
        this.form.name=row.name
        this.form.remark=row.remark
      })
    },
    del(id){
      this.$axios.get('http://localhost:8188/medicineType/delete?id='+id).then(res => res.data).then(res => {
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
    this.loadGet();
    this.loadUser();
  }
}
</script>

<style scoped>

</style>
