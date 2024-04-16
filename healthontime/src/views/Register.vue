<template>
  <div id="app">
    <div class="loginBody">
      <div class="loginDiv">
        <div class="login-content">
          <el-header style="text-align: center; color: #4CAF50; font-size: 24px;">准点健康管理系统</el-header>
            <el-form ref="form" :rules="rules" :model="form" label-width="80px">
              <el-form-item label="账号" prop="number">
                <el-input v-model="form.number"></el-input>
              </el-form-item>
              <el-form-item label="名字" prop="name">
                <el-input v-model="form.name"></el-input>
              </el-form-item>
              <el-form-item label="密码" prop="password">
                <el-input v-model="form.password"></el-input>
              </el-form-item>
              <el-form-item label="年龄" prop="age">
                <el-input v-model="form.age"></el-input>
              </el-form-item>
              <el-form-item label="电话" prop="phone">
                <el-input v-model="form.phone"></el-input>
              </el-form-item>
              <el-form-item label="性别">
                <el-radio-group v-model="form.sex">
                  <el-radio label="1">男</el-radio>
                  <el-radio label="0">女</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
    <el-button @click="toLogin" style="margin-left: 27%" >取 消</el-button>
    <el-button type="primary" @click="save">确 定</el-button>
  </span>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
export default {
  name: "Register",
  data() {
    let checkAge = (rule, value, callback) => {
      if (value > 150) {
        callback(new Error('年龄输入过大'));
      } else {
        callback();
      }
    };
    let checkDuplicate = (rule, value, callback) => {
      if (this.form.id) {
        return callback();
      }
      this.$axios.get('http://localhost:8188/user/findByNumber?number='+this.form.number).then(res=>res.data).then(res => {
        if (res.code != 200) {
          callback();
        } else {
          callback(new Error('账号已经存在'));
        }
      })
    };
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 2,
      total: 0,
      name: "",
      sex: "",
      sexs: [
        {
          value: '1',
          label: "男",
        },
        {
          value: '0',
          label: "女",
        }
      ],
      centerDialogVisible: true,
      form: {
        id:"",
        name: "",
        number: "",
        password: "",
        age: "",
        phone: "",
        sex: '0',
        roleId:'2',
      },
      rules: {
        name: [
          {required: true, message: '请输入名字', trigger: 'blur'},
          {min: 3, max: 8, message: '长度在 3 到 8 个字符', trigger: 'blur'},
        ],
        number: [
          {required: true, message: '请输入账号', trigger: 'blur'},
          {min: 3, max: 8, message: '长度在 3 到 8 个字符', trigger: 'blur'},
          {validator: checkDuplicate, trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 3, max: 8, message: '长度在 3 到 8 个字符', trigger: 'blur'}
        ],
        age: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 1, max: 3, message: '长度在 1 到 3 个字符', trigger: 'blur'},
          {pattern: /^([1-9][0-9]*){1,3}$/, message: '年龄必须为正数', trigger: 'blur'},
          {validator: checkAge, trigger: 'blur'}
        ],
        phone: [
          {required: true, message: '手机号不能为空', trigger: 'blur'},
          {pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: '请输入正确手机号', trigger: 'blur'},
        ],
      }
    }
  },
  methods: {
    toLogin(){
      this.$router.push('/')
    },
    save() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.$axios.post('http://localhost:8188/user/saveOrMod', this.form).then(res => res.data).then(res => {
            if (res.code === 200) {
              this.tableData = res.data
              this.total = res.total
              this.toLogin()
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
  }
}
</script>

<style scoped>
.loginBody {
  position: absolute;
  width: 100%;
  height: 100%;
  background-color: #B3C0D1;
}
.loginDiv {
  position: absolute;
  top: 40%;
  left: 58%;
  margin-top: -200px;
  margin-left: -350px;
  width: 450px;
  height: 500px;
  background: #fff;
  border-radius: 5%
}
.login-title {
  margin: 20px 0;
  text-align: center;
}
.login-content{
  width: 400px;
  height: 250px;
  position: absolute;
  top: 25px;
  left: 25px;
}
</style>
