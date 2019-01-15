<template>
  <!-- 控制路由的显示，以及是否只是显示图标 -->
  <aside class="main-sidebar animated" :class="{ showSlide: true, hideSlide: false, expandSide:(!sidebar.collapsed)}">
    <el-scrollbar tag="div" wrapClass="vue-scrollbar" v-if="(!sidebar.collapsed)">
      <div class="sidebar">
        <el-menu :default-active="onRoutes"
                 :default-openeds="onRouteKeys"
                 background-color="#d4dbe0"
                 router :collapse="sidebar.collapsed">
          <template v-for="item in menuList">
            <sub-menu :param="item"></sub-menu>
          </template>
        </el-menu>
      </div>
    </el-scrollbar>
    <el-scrollbar tag="div" wrapClass="vue-scrollbar" v-else>
      <div class="sidebar">
        <el-menu :default-active="onRoutes"
                 background-color="#d4dbe0"
                 router :collapse="sidebar.collapsed">
          <template v-for="item in menuList">
            <sub-menu :param="item"></sub-menu>
          </template>
        </el-menu>
      </div>
    </el-scrollbar>
  </aside>
</template>
<script>
  import subMenu from "./SubMenu.vue";
  import {mapGetters, mapActions, mapMutations} from 'vuex';

  export default {
    props: {
      show: Boolean,
    },
    data() {
      return {}
    },
    components: {
      subMenu,
    },
    computed: {
      ...mapGetters({
        sidebar: 'sidebar',
      }),
      onRoutes(){
        let result;
        const findMenuByLocation_ =(path,items) => {
          items.forEach((item) => {
            //当前路径和一个路径做对比 path_:自己传入的路径,link:从数据库中找到的路径
            const isEqual = (link,path_) => {
              let temp = false;
              while(path_ != '/' && path_.length>0) {
                if(link == path_) {
                  temp = true;
                  break;
                } else {
                  path_ = path.substr(0, path_.lastIndexOf('/'))
                }
              }
              return temp;
            }
            if(item.childrenMenu && !(item.childrenMenu.length>0)) {
              if(isEqual(item.menuLink,path)) {
                result = item.menuLink;
              }
            } else if(item.childrenMenu && item.childrenMenu.length>0) {
              findMenuByLocation_(path,item.childrenMenu);
            }
          });
        }

        findMenuByLocation_(this.$route.path,this.menuList);
        return result;
      },
      onRouteKeys(){
        const getParentArray = (path, menus, kas = []) => {
          if (menus && menus.length > 0) {
            for (let k = 0, length = menus.length; k < length; k++) {
              if (path === menus[k].menuLink) {
                kas.push(menus[k].menuLink);
                break;
              }
              let i = kas.length;
              if (menus[k].childrenMenu && menus[k].childrenMenu.length > 0) {
                getParentArray(path, menus[k].childrenMenu, kas);
              }
              if (i < kas.length) {
                kas.push(menus[k].menuLink);
              }
            }
          }
          return kas.reverse();
        }
        return getParentArray(this.$route.path, this.menuList);
      },
      // 使用对象展开运算符将 getters 混入 computed 对象中
      ...mapGetters([
        'menuList'
      ])
    },
    mounted () {
      let route = this.$route
    },
    created() {
      this.load();
    },
    methods: {
      ...mapActions({
        load: 'loadMenuList'
      })
    }
  }
</script>
<style>
  .showSlide {
    animation-duration: .2s;
    animation-name: slideInLeft;
  }

  .hideSlide {
    animation-duration: .2s;
    animation-name: slideOutLeft;
  }

  .main-sidebar {
    /*background-color: #ffffff;*/
    background-color: #d4dbe0;
    position: fixed;
    top: 50px;
    left: 0;
    bottom: 0;
    height: calc(100vh - 50px);
    width: 44px;
    z-index: 810;
    -webkit-transition: -webkit-transform 0.3s ease-in-out, width 0.3s ease-in-out;
    -moz-transition: -moz-transform 0.3s ease-in-out, width 0.3s ease-in-out;
    -o-transition: -o-transform 0.3s ease-in-out, width 0.3s ease-in-out;
    transition: transform 0.3s ease-in-out, width 0.3s ease-in-out;
  }

  .expandSide {
    width: 180px;
  }

  .el-menu-style,
  .el-menu-style .el-menu{
    background-color: #ffffff;
  }
  .el-menu-style .el-menu-item:hover,
  .el-menu-style .el-submenu__title:hover{
    background-color: #eeeeee !important;
  }

  .el-menu-style .el-submenu .el-menu-item {
    height: 45px;
    line-height: 45px;
  }

  .el-menu-style .el-menu-item,
  .el-menu-style .el-submenu__title {
    height: 45px;
    line-height: 45px;
  }

  .main-sidebar .el-menu--collapse {
    width: 44px;
  }

  .main-sidebar .el-menu--collapse>.el-menu-item,
  .main-sidebar .el-menu--collapse>.el-submenu>.el-submenu__title {
    padding-left: 13px !important;
  }

  .vue-scrollbar{
    height: calc(100vh - 0px);
    background-color:"#d4dbe0"
    /*background-color: #ffffff !important;*/
    /**/
    /*height: 100%;*/
  }

  .main-sidebar .el-scrollbar__bar.is-vertical{
    display: none;
  }

  .sidebar{
  }


</style>
