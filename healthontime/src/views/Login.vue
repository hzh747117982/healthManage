<template>
  <div id="app">
    <div class="loginBody">
      <div class="loginDiv">
        <div class="login-content">
    <el-container style="height: 100vh;">
      <el-header style="text-align: center; color: #4CAF50; font-size: 24px;">准点健康管理系统</el-header>
      <el-main>
        <el-form ref="loginForm" :model="loginForm" label-width="80px" style="width: 300px; margin: 0 auto;">
          <el-form-item label="账号" prop="username">
            <el-input v-model="loginForm.number" placeholder="请输入账号"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input type="password" v-model="loginForm.password" placeholder="请输入密码"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="confirm">登录</el-button>
            <el-button type="primary" @click="toRegister">注册</el-button>
          </el-form-item>
        </el-form>
        <div v-if="errorMsg" style="color: red; text-align: center;">{{ errorMsg }}</div>
      </el-main>
    </el-container>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      loginForm: {
        number: '',
        password: ''
      },
      errorMsg: ''
    };
  },
  methods: {
    confirm() {
      this.$refs.loginForm.validate((valid) => {
        if (valid) { //valid成功为true，失败为false
          //去后台验证用户名密码
          this.$axios.post('http://localhost:8188/user/login', this.loginForm).then(res => res.data).then(res => {
            if (res.code == 200) {//存储
              sessionStorage.setItem("CurUser", JSON.stringify(res.data.user))
              this.$store.commit("setMenu",res.data.menu)
              console.log(res.data.menu)
              this.$router.replace("/Index");
            } else {
              this.confirm_disabled = false;
              alert("校验失败，用户名或密码错误!");
              return false;
            }
          });
        } else {
          this.confirm_disabled = false;
          console.log("校验失败");
          return false;
        }
      });
    },
    toRegister(){
      this.$router.push('/register')
    }
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
  top: 50%;
  left: 58%;
  margin-top: -200px;
  margin-left: -350px;
  width: 450px;
  height: 330px;
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
