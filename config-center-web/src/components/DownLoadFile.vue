<template>

  <el-row>
   <div style="margin-bottom: 20px;"> <span>本应用环境下的文件已打包成zip文件</span></div>
    <div style="text-align: end;">
      <el-button type="primary"  @click="saveFile">
        点击下载
      </el-button>
    </div>
  </el-row>
</template>

<script>
    export default {
        name: "DownLoadFile",

      data(){
          return{
            fileContent:[],
          }
      },
      props:{
          envId:""
      },
      methods:{
        saveFile: function () {
            this.$emit("save",this.envId);
        },
      }
    }
</script>

<style scoped>

</style>
