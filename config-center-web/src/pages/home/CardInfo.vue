<template>
    <div class="card-click" title="点击查看收起详情列表">
        <el-row>
            <el-col :span="6">
                <div class="back-circle" :style="colorName">
                  <i :class="cardIcon" v-if="cardIcon"></i>
                  <i class="el-icon-edit" v-else></i>
                </div>
            </el-col>
            <el-col :span="18">
              <div class='card-content'>
                <div class='card-content-title'>{{cardName}}</div>
                <div><span class="cardNumber">{{cardNum}}</span></div>
              </div>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    export default {
        name: "card-info",
        props:['cardName','cardNum','colorName','cardIcon'],
        data() {
          return {

          }
        }
    }
</script>

<style scoped lang="less">
  .card-click {
    background-color: #ecf0ff;
    width: 100%;
    height: 150px;
    -webkit-box-shadow: 2px 2px 6px rgba(0, 21, 41, .085);
    box-shadow: 2px 2px 6px rgba(0, 21, 41, .085);
    /*margin-top: 16px;*/
    padding-top: 40px;
    padding-left: 24px;
    padding-right: 24px;
    /*padding: 24px;*/

    &:hover {
      cursor: pointer;
      background-color: #d4dbe0;
      -webkit-box-shadow: 5px 5px 6px rgba(0, 21, 41, .085);
      box-shadow: 5px 5px 6px rgba(0, 21, 41, .085);
    }

    &:active {
      cursor: pointer;
      background-color: #cad9e1;
      -webkit-box-shadow: 2px 2px 6px rgba(0, 21, 41, .085);
      box-shadow: 2px 2px 6px rgba(0, 21, 41, .085);
    }
  }
  .back-circle{
    width: 60px;
    height: 60px;
    border-radius: 30px;
    line-height: 60px;
    font-size: 40px;
    text-align: center;
    display: inline-block;
    margin-right: 5px;
    color: #fff;
    background-color: #fff;
  }

  .card-content{
    margin: 0 0 0 24px;
    color: rgba(0,0,0,.45);
    line-height: 16px;

    .card-content-title{
      font-size: 16px;
      line-height: 28px;
      font-weight: 500;
      color: rgba(0,0,0,.85);
      margin-left:26px;
      margin-bottom: 12px;
    }
  }
  .cardNumber {
    color:#52c41a;
    margin-left:26px;
    font-size: 30px;
    font-weight: bold;
  }
</style>
