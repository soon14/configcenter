<template>
	<div>
		<div class="ant-layout-content-header bd">
            <el-row>
                <el-col>
                    <span class="title">{{title}}</span>
                </el-col>
            </el-row>
        </div>
		<slot class="ant-layout-content-box"></slot>
	</div>
</template>

<script>
	export default{
    name:"box",
    props: {
      title: {
        type: String
      }
    }
  }
</script>

<style>

	span.title {
      border-left: 3px solid #108EE9;
      padding-left: 5px;
    }

    .bd{
	    border-bottom: 1px solid #D9D9D9;
	  }

	.ant-layout-content-box {
	  padding-top: 16px;
	  padding-bottom: 16px;
	}

	.ant-layout-content-header {
	    height: 38px;
	    line-height: 38px;
	    padding-left: 0px;
	    font-weight: bold;
	    position: relative;
	  }
</style>