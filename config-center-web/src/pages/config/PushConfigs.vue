<template>
    <section>
      <el-form ref="configPush" :model="configPush" label-width="120px" :rules="rules" size="small" style="margin-right: 20px">
        <el-form-item label="配置推送名称" prop="name">
          <el-input v-model="configPush.name"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input type="textarea" v-model="configPush.description"></el-input>
        </el-form-item>
      </el-form>
      <push-config-instance :selectedEnv="selectedEnv" ref="pushConfigInstance"></push-config-instance>
      <el-row type="flex" justify="end" style="margin-top: 10px">
        <el-button type="primary" size="small">取消</el-button>
        <el-button type="primary" size="small" @click="submitConfigPush">确定</el-button>
      </el-row>
    </section>
</template>

<script>
    import PushConfigInstance from "./PushConfigInstance";
    export default {
        name: "push-configs",
        props:['currentItem','selectedEnv','fileId'],
        components: {
          PushConfigInstance
        },
        data() {
          return {
            configPush: {
              name:"",
              description:"",
            },
            rules: {
              name: [
                {required: true, message: '推送名称不能为空', trigger: 'blur' },
              ],
            }
          }
        },
        methods: {
          submitConfigPush() {
            let data = this.$refs.pushConfigInstance.returnResultToParent();
          },
        },
        created() {}
    }
</script>

<style scoped>

</style>
