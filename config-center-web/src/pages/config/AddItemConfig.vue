<template>
  <div class="body">
    <el-form :model="submitItems" label-width="120px" :rules="rules" ref="submitItemForm">
      <el-form-item label="名称:" prop="itemKey">
        <el-input v-model="submitItems.itemKey" style="width: 80%" placeholder="不可为空" :disabled="isMod">
        </el-input>
      </el-form-item>
      <el-form-item label="修改选项:" v-if="isMod">
        <el-radio-group  v-model="modOption" @change="modItemChange">
          <el-radio label="content" border>配置内容修改</el-radio>
          <el-radio label="strategy" border>刷新策略修改</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="内容:" prop="itemValue" v-if="isModContent" ref="clearItemValue">
        <el-input v-model="submitItems.itemValue" style="width: 80%" placeholder="不可为空" >
        </el-input>
      </el-form-item>
      <el-form-item label="刷新策略:" v-if="isModStrategy" prop="strategyName">
        <el-input v-model="submitItems.strategyName" style="width: 80%" placeholder="选填(右侧查询后双击选中)" :disabled="true" >
          <el-button slot="append" icon="el-icon-search" @click="queryStrategyVisible = true"></el-button>
        </el-input>
      </el-form-item>
      <el-form-item label="描述:">
        <el-input type="textarea" v-model="submitItems.desc" style="width: 80%" placeholder="描述一下您的配置项吧"></el-input>
      </el-form-item>
    </el-form>
    <div class="footer">
      <el-button @click="handleSubmit('submitItemForm')" type="primary" icon="el-icon-check">提交</el-button>
    </div>
    <el-dialog title="策略查询" :visible.sync="queryStrategyVisible" width="70%" :close-on-click-modal="false"
               append-to-body>
      <StrategyIdList @dealSelectStrategyRow="getStrategyId"></StrategyIdList>
    </el-dialog>
  </div>
</template>

<script>
  import {CHECK_KEY_VALUE} from "../../common/regexp";
  import StrategyIdList from "./QueryStrategyId"

  export default {
    name: "AddItem",
    components: {
      StrategyIdList
    },
    data() {
      return {
        isMod: true,
        queryStrategyVisible: false,
        isModStrategy: false,
        isModContent: true,
        modOption: 'content',
        submitItems: {
          itemKey: '',
          itemValue: '',
          strategyId: '',
          strategyName:'',
          desc: '',
        },
        rules: {
          itemKey: [
            { required: true, message: '请输入配置项名称！', trigger: 'blur'},
            {
              pattern: CHECK_KEY_VALUE,
              message: '只能由英文字母、数字、下划线组成，首字母只能为字母，2-32个字符',
              trigger: ['blur', 'change']
            }
          ],
          itemValue: [ { required: true ,message: '请输入具体的内容！', trigger: 'blur'} ],
          strategyName: [ { required: true ,message: '请输入刷新策略', trigger: 'blur'} ],
        },
      }
    },
    props: {
      envId: {
        type: String,
        required: true
      },
      modItem: {},
      isAddOrMod: {}
    },
    methods: {
      AddOrMod() {
        if (this.isAddOrMod === true) {
          this.isMod = false;
          this.isModStrategy = true;
          this.isModContent = true;
        }
        if (this.isAddOrMod === false) {
          this.toGetStrategyName();
          this.isMod = true;
          this.submitItems = this.modItem;
        }
      },
      handleSubmit(formName) {
          this.submitItems.envId = this.envId;
        this.$refs[formName].validate((valid) => {
          if(valid) {
                if (this.isMod === false) {
                        this.$http.addConfigItem(this.submitItems).then(res => {
                          this.$message({
                            message: res.data.result,
                            type: 'success'
                          });
                          this.submitItems.strategyName="";
                        }).catch(err => {
                          console.log('err', err);
                          this.submitItems.strategyName="";
                        })
                }
                if (this.isMod === true) {
                  this.submitItems.modOption = this.modOption;
                  this.$http.modConfigItem(this.submitItems).then(res => {
                    this.$message({
                      message: res.data.result,
                      type: 'success'
                    });
                    this.submitItems.strategyName="";
                  }).catch(err => {
                    console.log('err', err);
                    this.submitItems.strategyName="";
                  });
                }
            this.$emit('closeEvent', false)
          }
        })
      },
      getStrategyId(row) {
        this.queryStrategyVisible = false;
        this.submitItems.strategyId = row.id;
        this.submitItems.strategyName = row.strategyName;
      },
      modItemChange(val){
        if (val === 'strategy'){
          this.isModStrategy = true;
          this.isModContent = false;
          this.submitItems = this.modItem;
          this.$refs['clearItemValue'].clearValidate();
        }
        if (val === 'content') {
          this.isModStrategy = false;
          this.isModContent = true;
          this.submitItems = this.modItem;
        }
      },
      toGetStrategyName() {
        let data = {
          appId: this.$router.history.current.params.appId,
          strategyId: this.modItem.strategyId
        };
        this.$http.getStrategyName(data).then(res => {
          this.submitItems.strategyName=res.data.result;
        }).catch(err => {
          console.log("err", err);
          this.submitItems.strategyName="";
        });
      }
      },
    created() {
      this.AddOrMod();
    },
  }
</script>

<style scoped>
  .footer {
    text-align: center;
  }

</style>
