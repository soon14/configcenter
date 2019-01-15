<template>
  <transition mode="out-in" enter-active-class="fadeIn" leave-active-class="fadeOut" appear>
    <!-- 根据路由显示 -->
    <router-view></router-view>
  </transition>
</template>

<script>
  //引用awesome图标库
  import 'font-awesome/css/font-awesome.min.css'
  export default{
      name:'frame',
  }
</script>
<style>
  html {
    /*background-color: #f5f5f5;*/
    background-color:#ffffff;
    font-size: 14px;
    -moz-osx-font-smoothing: grayscale;
    -webkit-font-smoothing: antialiased;
    min-width: 300px;
    overflow-x: hidden;
    /*overflow-y: hidden;*/
    text-rendering: optimizeLegibility;
    box-sizing: border-box;
  }

  body {
    color: #4a4a4a;
    font-size: 1rem;
    font-weight: 400;
    line-height: 1.5;
  }

  .animated {
    animation-duration: .5s;
  }
</style>
