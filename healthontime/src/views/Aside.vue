<template>
  <el-menu background-color="#FFFFFF"
           text-color="#8C9AC0"
           active-text-color="#2FCCC0"
           default-active="/Home"
           :collapse="isCollapse"
           :collapse-transition="false"
           router
           style="height: 100vh;">
    <el-menu-item index="/Home">
      <i class="el-icon-s-home"></i>
      <span slot="title">首页</span>
    </el-menu-item>

    <el-menu-item :index="'/'+item.menuClick" v-for="(item,i) in menu" :key="i">
      <i :class=item.menuIcon></i>
      <span slot="title">{{item.menuName}}</span>
    </el-menu-item>

  </el-menu>
</template>

<script>
export default {
  name: "Aside",
  data(){
    return {
    }
  },
  computed:{
    "menu":{
      get(){
        return this.$store.state.menu
      }
    }
  },
  props:{
    isCollapse:Boolean
  }
}
</script>

<style scoped>

</style>
