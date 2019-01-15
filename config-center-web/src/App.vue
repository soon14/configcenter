<template>
  <div class="wrapper fixed">
    <imp-header></imp-header>
    <side-menu></side-menu>
    <div class="content-wrapper" :class="{ slideCollapse: sidebar.collapsed}">
      <el-scrollbar tag="div" wrapClass="content-scrollbar">
        <section class="content">
          <el-breadcrumb separator=">" style="margin-bottom: 10px;">
              <template v-for="item in breadcrumbItems">
                <el-breadcrumb-item :to="{path:item.path}"><span class="breadcrumb_style"><i :class="item.icon"></i>{{item.name}}</span></el-breadcrumb-item>
              </template>
          </el-breadcrumb>
          <div style="min-height: calc(100vh - 200px); ">
            <transition mode="out-in" enter-active-class="fadeIn" leave-active-class="fadeOut" appear>
              <router-view></router-view>
            </transition>
          </div>
          <imp-footer style="margin-top: 20px"></imp-footer>
        </section>
      </el-scrollbar>
    </div>
  </div>
</template>

<script>
  import Vue from 'vue'
  import sideMenu from './components/SideMenu.vue'
  import impHeader from "./components/Header.vue"
  import impFooter from "./components/Footer.vue"
  import {mapGetters, mapActions,mapMutations} from 'vuex'
  import 'animate.css'

  export default {
    name: 'app',
    components: {
      sideMenu,
      impFooter,
      impHeader,
    },
    //计算属性
    computed: {
      ...mapGetters({
        sidebar: 'sidebar',
        breadcrumbItems: 'breadcrumbItems',
      })
    },
    data: function () {
      return {
      }
    },
    methods: {
    },
    beforeMount () {
    },
    mounted () {
    },
    beforeCreate () {
    } ,
    created () {
    }
  }

</script>

<style>
  .toolbar {
    background: #f2f2f2;
    margin: 10px 0px;
  }
  .breadcrumb_style {
     cursor: pointer;
  }

  .animated {
    animation-duration: .2s;
  }

  blockquote, body, dd, dl, dt, fieldset, figure, h1, h2, h3, h4, h5, h6, hr, html, iframe, legend, li, ol, p, pre, textarea, ul {
    margin: 0;
    padding: 0;
  }

  *,:after, :before {
    -webkit-box-sizing: border-box;
    -moz-box-sizing: border-box;
    box-sizing: border-box;
  }


  .content-wrapper {
    -webkit-transition: -webkit-transform 0.3s ease-in-out, margin 0.3s ease-in-out;
    -moz-transition: -moz-transform 0.3s ease-in-out, margin 0.3s ease-in-out;
    -o-transition: -o-transform 0.3s ease-in-out, margin 0.3s ease-in-out;
    transition: transform 0.3s ease-in-out, margin 0.3s ease-in-out;
    margin-left: 180px;
    padding-top: 50px;
  }

  .content-scrollbar{
    height: calc(100vh - 50px);
  }

  .content-wrapper .el-scrollbar__bar.is-vertical{
    display: none;
  }

  .content-wrapper .content {
    padding: 1.25rem;
  }

  .content-wrapper.slideCollapse{
    margin-left: 44px;
  }

  .content-wrapper.mobileSide{
    margin-left: 0px;
  }

</style>
