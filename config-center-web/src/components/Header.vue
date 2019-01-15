<template>
  <section>
    <header class="main-header animated">
      <!-- 图标 -->
      <span class="logo">
        <img src='../../static/img/cmlogo.png' style="width: 150px;height: 45px;
        border-radius: 5%; vertical-align: middle;" alt="U">
      </span>

      <nav class="navbar">
        <a href="#" :class="isExpand?'sidebar-toggle':'sidebar-toggle2'" data-toggle="offcanvas" role="button"
           @click.stop.prevent="toggleMenu(!sidebar.collapsed)">
        </a>
        <div class="navbar-custom-menu">
          <span class="badge-bell" @click="showNotifications">
            <el-badge :value="count" v-if="count>0" @noticeCount="count += $event">
              <i class="fa fa-bell-o fa-lg"></i>
            </el-badge>
            <span v-else>
              <i class="fa fa-bell-o fa-lg"></i>
            </span>
        </span>
          <el-dropdown trigger="click" class="navbar-dropdown" @command="handleCommand">
            <div class="el-dropdown-link">
              <img src='../../static/img/logo.png' style="width: 25px;height: 25px;
              border-radius: 50%; vertical-align: middle;" alt="U">
                {{username}}
            </div>
            <el-dropdown-menu slot="dropdown" style="padding: 0px">
              <el-dropdown-item command="logout">退出</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </nav>
    </header>
    <!-- 弹框 -->
    <el-dialog width="80%" title="通知"  :visible.sync="noticeVisible" size="large">
      <c-notice v-if="noticeVisible===true" @closeNotice="noticeVisible = false"></c-notice>
    </el-dialog>
  </section>
</template>
<script>
import { mapGetters, mapActions, mapMutations } from "vuex";
import types from "../store/mutation-types";
import CNotice from "../pages/system/Notice.vue";
import Stomp from 'stompjs'
import ReconnectingWebSocket from 'reconnecting-websocket';

export default {
  components: {
    CNotice
  },
  data() {
    return {
      websocket: null,
      count: 0,
      isExpand: true,
      noticeVisible: false
    };
  },

  computed: {
    ...mapGetters({
      sidebar: "sidebar"
    }),
    username() {
      return this.$store.getters.showName;
    }
  },
    methods: {
      handleCommand(command) {
        if(command === "logout") {
          this.$http.requestLogout().then(res => {
            sessionStorage.clear();
            window.location.reload();
            this.$router.push('/login');
          })
        }
      },
      showNotifications() {
        this.noticeVisible = true;
      },
      toggleMenu(collapsed){
        this.isExpand = !this.isExpand;
        this.toggleSidebar();
      },
      ...mapMutations({
        toggleSidebar: types.TOGGLE_SIDEBAR,
      }),

      //使用webSocket来获取数据
      initWebSocket(){
          const wsuri = process.env.WS_ADDRES + "/center/notify/websocket";
          this.websocket = new ReconnectingWebSocket(wsuri);
          this.websocket.onopen = this.websockonopen;
          this.websocket.onmessage = this.websocketonmessage;
          this.websocket.onclose = this.websocketclose;
      },
      websockonopen(e) {
      },
      websocketonmessage(e){ //数据接收
        const receiveData = JSON.parse(e.data);
        this.count = receiveData.result;
      },

      websocketclose(e){  //关闭

        const wsuri = process.env.WS_ADDRES + "/center/notify/websocket";
        this.websocket = new ReconnectingWebSocket(wsuri);
      },
  },
  created() {
    this.initWebSocket();
  },
  destoryed() {
    // this.websocket.close();
  }
};
</script>
<style scoped>
.animated {
  animation-duration: 0.2s;
}

.main-header a {
  text-decoration: none;
  color: #48576a;
}

.main-header {
  position: fixed;
  min-width: 100%;
  box-shadow: 0 2px 3px hsla(0, 0%, 7%, 0.1), 0 0 0 1px hsla(0, 0%, 7%, 0.1);
  z-index: 1999;
  animation-name: slideInDown;
  animation-fill-mode: both;
  color: #48576a;
}

.sidebar-toggle {
  float: left;
  background-color: transparent;
  background-image: none;
  padding: 15px 15px;
  font-family: fontAwesome;
  line-height: 20px;
  display: block;
}

.sidebar-toggle:before {
  content: "\f03b";
}

.sidebar-toggle2 {
  float: left;
  background-color: transparent;
  background-image: none;
  padding: 15px 15px;
  font-family: fontAwesome;
  line-height: 20px;
  display: block;
}

.sidebar-toggle2:before {
  content: "\f03c";
}

.sidebar-toggle2:hover {
  background: #f9f9f9;
}


.sidebar-toggle:hover {
  background: #f9f9f9;
}

.main-header {
  background-color: #ffffff;
}

.main-header .logo {
  -webkit-transition: width 0.3s ease-in-out;
  -o-transition: width 0.3s ease-in-out;
  transition: width 0.3s ease-in-out;
  display: block;
  float: left;
  height: 50px;
  font-size: 20px;
  line-height: 50px;
  text-align: center;
  width: 180px;
  font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
  padding: 0 15px;
  font-weight: 300;
  overflow: hidden;
  text-decoration: none;
}

.main-header .logo .logo-lg {
  display: block;
  height: 38px;
  line-height: 38px;
  margin-top: 6px;
}

.main-header .navbar {
  -webkit-transition: margin-left 0.3s ease-in-out;
  -o-transition: margin-left 0.3s ease-in-out;
  transition: margin-left 0.3s ease-in-out;
  margin-bottom: 0;
  margin-left: 180px;
  border: none;
  min-height: 50px;
  border-radius: 0;
  /*background-color: #ffffff;*/
}

.layout-top-nav .main-header .navbar {
  margin-left: 0;
}

body.hold-transition .main-header .navbar,
body.hold-transition .main-header .logo {
  -webkit-transition: none;
  -o-transition: none;
  transition: none;
}

.main-header .logo {
  border-bottom: 0 solid transparent;
}

@media (max-width: 800px) {
  .main-header .logo {
    display: none;
  }

  .main-header .navbar {
    margin: 0;
  }

  .main-header .logo,
  .main-header .navbar {
    width: 100%;
    float: none;
  }
}
.main-header.closeLogo .navbar {
  margin-left: 44px;
}

.main-header.closeLogo .logo {
  width: 44px;
  padding: 0 8px;
}
.main-header.closeLogo .logo .logo-lg b {
  display: none;
}
.main-header.closeLogo .sidebar-toggle {
  background: #f9f9f9;
}

.main-header.closeLogo .navbar .sidebar-toggle:before {
  content: "\f03c";
}

.main-header.mobileLogo .navbar .sidebar-toggle:before {
  content: "\f03a";
}

.navbar-custom-menu {
  float: right;
}

.navbar-custom-menu .el-dropdown-link {
  cursor: pointer;
  height: 50px;
  padding: 13px 5px;
  min-width: 50px;
  text-align: center;
}
.navbar-custom-menu .el-dropdown-link img {
  background-color: #108ee9;
}

.navbar-custom-menu .el-dropdown-link:hover {
  background: #f9f9f9;
}
.message-list {
  list-style: none;
  padding: 0 10px;
}
.message-list li {
  list-style: none;
  height: 25px;
  line-height: 25px;
}
.message-list li a {
  text-decoration: none;
  color: #666666;
}
.message-list li:hover {
  background-color: #f9f9f9;
}

.el-dropdown-menu .header-pic {
  text-align: center;
  background-color: #108ee9;
  padding: 20px;
}
.el-dropdown-menu .header-pic img {
  vertical-align: middle;
  height: 90px;
  width: 90px;
  border: 3px solid;
  border-color: transparent;
  border-color: hsla(0, 0%, 100%, 0.2);
  background-color: #108ee9;
}
.el-dropdown-menu .header-pic p {
  font-size: 1.5rem;
  color: #ffffff;
}

.badge-bell {
  margin-right: 30px;
  cursor: pointer;
}

</style>
