<template>
  <!-- 存在子菜单，则递归显示 -->
  <el-submenu :index="item.menuLink" v-if="item.childrenMenu && item.childrenMenu.length>0" class="el-menu-sub">
    <template slot="title"><i :class="item.menuIcon"></i><span>{{item.menuName}}</span></template>
    <template v-for="child in item.childrenMenu">
      <!-- 递归组件 -->
      <sub-menu v-if="child.childrenMenu && child.childrenMenu.length>0" :param="child"></sub-menu>
      <el-menu-item :index="child.menuLink" v-else><i :class="child.menuIcon"></i><span>{{child.menuName}}</span></el-menu-item>
    </template>
  </el-submenu>
  <!-- 不存在子菜单，则直接显示 -->
  <el-menu-item :index="item.menuLink" v-else class="el-menu-each"><i :class="item.menuIcon"></i><span>{{item.menuName}}</span></el-menu-item>
</template>
<script>
  import subMenu from "./SubMenu.vue"

  export default {
    name: 'subMenu',
    props: ['param'],
    data: function () {
      return {item: this.param}
    },
    components: {
      subMenu
    }
  }
</script>
<style>
  .el-menu .fa {
    margin-right: 10px;
  }
</style>
