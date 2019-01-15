<template>
  <section>
    <div class="config-container">
      <div class="render-item-wrapper">
        <div class="render-item-title">
          <el-row>
            <span>刷新策略的基本信息</span>
            <span style="float: right" v-if="!options.disabled">
                <el-button type="primary" size="mini" @click="saveStrategyInfo('strategyInfo')" >{{strategyInfo.id ? "修改":"保存"}}</el-button>
            </span>
          </el-row>
        </div>

        <div class="config-wrapper">
          <div class="content" style="margin-top: 10px">
            <el-form :model="strategyInfo" label-width="120px" ref="strategyInfo" :rules="rules" size="small" :disabled="options.disabled">
              <el-form-item label="刷新策略名称" prop="strategyName" style="margin-right: 10px">
                <el-input v-model="strategyInfo.strategyName"></el-input>
              </el-form-item>
              <el-form-item label="刷新策略描述" prop="description" style="margin-right: 10px">
                <el-input type="textarea" v-model="strategyInfo.description"></el-input>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </div>
    </div>

    <draggable v-model="strategyInfoData" :move="getDataItem" @update="dataDragEnd" :options="options">
      <transition-group>
        <div v-for="(item, index) in strategyInfoData" :key="item.uniqueId">
          <div v-if="item.type == 'constructor'">
            <constructor @getConstructorInfo="getConstructorInfo" :extValue="item" :sequence="index" :strategyId="strategyInfo.id" :ref="refNameValue('constructor',item.uniqueId)" @deleteItem="deleteOneStep(item)" :operateType="operateType"></constructor>
          </div>
          <div v-if="item.type == 'field'">
            <field @getFieldInfo="getFieldInfo" :extValue="item" :sequence="index" :strategyId="strategyInfo.id" :ref="refNameValue('field',item.uniqueId)" @deleteItem="deleteOneStep(item)" :operateType="operateType"></field>
          </div>
          <div v-if="item.type == 'method'">
            <method @getMethodInfo="getMethodInfo" :extValue="item" :sequence="index" :strategyId="strategyInfo.id" :ref="refNameValue('method',item.uniqueId)" @deleteItem="deleteOneStep(item)" :operateType="operateType"></method>
          </div>
        </div>
      </transition-group>
    </draggable>

    <el-button style="width: 100%;margin: 10px 0px;" @click="isAddStepVisible" type="success" size="medium" v-if="!options.disabled">+</el-button>

    <el-dialog title="新增步骤" :visible.sync="addStepVisible" width="50%" :close-on-click-modal="false" append-to-body>
        <el-select v-model="stepType" placeholder="请选择添加类型步骤" clearable>
          <el-option label="构造器" value="constructor"></el-option>
          <el-option label="属性" value="field"></el-option>
          <el-option label="方法" value="method"></el-option>
        </el-select>
      <el-button type="primary" size="mini" @click="addOneStep">确定</el-button>
    </el-dialog>
  </section>
</template>

<script>
    import Constructor from './Constructor';
    import Field from './Field';
    import Method from './Method';
    import draggable from 'vuedraggable';
    export default {
      name: "add-strategy",
      components: {
        draggable,
        Constructor,
        Field,
        Method
      },
      props:['strategyResult','operateType'],
      data() {
        return {
          options:{disabled:false},
          strategyInfo: {
            id:"",
            strategyName:"",
            description:"",
          },
          rules: {
            strategyName: [
              {required: true, message: '刷新策略的名称不能为空', trigger: 'blur' },
            ],
          },
          strategyInfoData:[],
          uuid:0,
          addStepVisible:false,
          stepType:"",
          disabled:false,
          buttonDisabled:false,
        }
      },
      methods: {
        isAllItemSaved() {
          let result = true;
          for(let i=0;i<this.strategyInfoData.length;i++) {
            if(!this.strategyInfoData[i].id) {
              this.$message({
                message:"关闭前请先保存第"+(i + 1)+"步骤的数据，如果不需要请先移除后再关闭页面",
                type:"error",
              });
              result = false;
            }
          }
          return result;
        },
        isAddStepVisible() {
          let index = -1;
          for(let i=0;i<this.strategyInfoData.length;i++) {
            if(!this.strategyInfoData[i].id) {
              index = i;
            }
          };
          if(!this.strategyInfo.id) {
            this.$message({
              message:"请先保存刷新策略的基本信息",
              type:"error",
            });
          } else if(index != -1) {
            this.$message({
              message:"请先保存第"+(index + 1)+"步骤的数据，然后才能添加新的步骤",
              type:"error",
            });
          } else {
            this.addStepVisible = true;
          }
        },

        saveStrategyInfo(formName) {
          let appId = this.$router.history.current.params.appId;
          let message = this.strategyInfo.id ? "更新刷新策略基本信息成功" : "创建刷新策略基本信息成功";
          this.$refs[formName].validate((valid) => {
            if (valid) {
              let data = {
                appId:appId,
                strategyId:this.strategyInfo.id ? this.strategyInfo.id :"",
                strategyName:this.strategyInfo.strategyName,
                desc:this.strategyInfo.description,
              };
              this.$http.createUpdateStrategy(data).then(res => {
                this.strategyInfo.id = res.data.result;
                this.$message({
                  message:message,
                  type:'success',
                });
                let para={};
                this.$emit("refreshData",para);
              }).catch(err => {
                console.log('err',err);
              })
            } else {
              return false;
            }
          });
        },

        //选中时的回调函数
        getDataItem (evt) {
        },
        //移动位子成功后的回调函数
        dataDragEnd (evt) {
          //找到所有可能引用其他组件的出参
          let flag = false;
          for(let i=0;i<this.strategyInfoData.length;i++) {
            //判断是否有引用其他的组件
            if(this.strategyInfoData[i].refUniqueId) {
              let data = this.strategyInfoData[i];
              for(let j=0;j<this.strategyInfoData.length;j++) {
                if(data.refUniqueId == this.strategyInfoData[j].uniqueId) {
                  let refName = data.type + data.uniqueId;
                  if(i < j) {
                    flag = true;
                    this.strategyInfoData[i].refUniqueId = undefined;
                    this.$refs[refName][0].modifyParamValue(0);
                  } else {
                    let index = j + 1;
                    this.$refs[refName][0].modifyParamValue(index);
                  }
                }
              }
            }
          };
          if(!flag) {
            this.$message({
              message:"移动成功",
              type:'success',
            });
            //这里调用接口，来改变顺序
            this.getStepSequence();
          };
          if(flag) {
            this.$message({
              message:"存在前面的步骤引用了后面的步骤的问题,请重新填写这个值后保存",
              type:'error',
            });
          }
        },
        getStepSequence() {
          let strategyStepInfoVOS = [];
          let j = 1;
          for(let i=0;i<this.strategyInfoData.length;i++) {
            let item = this.strategyInfoData[i];
            if(item.id) {
              let refName = item.type + item.uniqueId;
              let paramValue = this.$refs[refName][0].getCurrentParamValue();
              let strategyStepInfoVO = {
                strategyStepId:item.id,
                stepNum: j,
                paramValue:paramValue,
              };
              strategyStepInfoVOS.push(strategyStepInfoVO);
              j  = j + 1;
            }
          };
          let data = {
            appId: this.$router.history.current.params.appId,
            strategyId: this.strategyInfo.id,
            strategyStepInfoVOS:strategyStepInfoVOS
          };
          this.$http.updateStrategyStepNums(data).then(res => {
          }).catch(err => {
            console.log('err',err);
          });
        },
        //添加
        addOneStep() {
          this.uuid = this.uuid + 1;
          this.strategyInfoData.push({
            uniqueId:this.uuid,
            type:this.stepType,
          });
          this.addStepVisible = false;
        },
        //删除
        deleteOneStep(item) {
          this.$confirm('确定要移除此步骤吗？','提示',{
            type: "warning",
          }).then(() => {
            let isRef = false;
            for(let i=0;i<this.strategyInfoData.length;i++) {
              if(this.strategyInfoData[i].refUniqueId && this.strategyInfoData[i].refUniqueId == item.uniqueId) {
                isRef = true;
                break;
              }
            };
            if(isRef) {
              this.$message({
                message:"移除失败，此组件被其他组件引用，请先移除其他组件或者更改其他组件的引用后再移除此组件",
                type:'warning',
                duration:'5000',
              });
            } else {
              let result = this.strategyInfoData.filter(value => {
                return value.uniqueId != item.uniqueId;
              });
              if(item.id) {
                let data = {};
                data.appId = this.$router.history.current.params.appId;
                data.strategyId = this.strategyInfo.id;
                data.strategyStepId = item.id;
                this.$http.deleteStrategyStep(data).then(res => {

                })
              };
              this.strategyInfoData = result;
              if(result.length>0) {
                this.getStepSequence();
              };
              this.$message({
                message:"移除成功",
                type:'success',
              });
            }
          }).catch(() => {})
        },

        refNameValue(msg,key) {
          return msg + key;
        },

        changItemState(data) {
          let index = -1;
          let temp = {};
          for(let i=0;i<this.strategyInfoData.length;i++) {
            if(this.strategyInfoData[i].uniqueId == data.uniqueId) {
              index = i;
              temp = this.strategyInfoData[i];
            }
          };
          let returnResult  = {};
          returnResult.index = index;
          returnResult.item = temp;
          return returnResult;
        },
        getConstructorInfo(data) {
          let result = this.strategyInfoData.filter(value => {
            return value.uniqueId == data.uniqueId;
          });
          let message = result.length>0 && result[0].id ? "修改构造器成功" : "创建构造器成功";
          let returnResult = this.changItemState(data);
          if(returnResult.index >=0) {
            let result = {
              id:data.id,
              refUniqueId:data.refItem,
              type:returnResult.item.type,
              uniqueId:returnResult.item.uniqueId,
            };
            this.strategyInfoData.splice(returnResult.index,1,result);
          };
          this.$message({
            message:message,
            type:'success',
          });
          this.getStepSequence();
        },

        getFieldInfo(data) {
          let result = this.strategyInfoData.filter(value => {
            return value.uniqueId == data.uniqueId;
          });
          let message = result.length>0 && result[0].id ? "修改属性成功" : "创建属性成功";
          let returnResult = this.changItemState(data);
          if(returnResult.index >=0) {
            let result = {
              id:data.id,
              refUniqueId:data.refItem,
              type:returnResult.item.type,
              uniqueId:returnResult.item.uniqueId,
            };
            this.strategyInfoData.splice(returnResult.index,1,result);
          };
          this.$message({
            message:message,
            type:'success',
          });
          this.getStepSequence();
        },

        getMethodInfo(data) {
          let result = this.strategyInfoData.filter(value => {
            return value.uniqueId == data.uniqueId;
          });
          let message = result.length>0 && result[0].id ? "修改方法成功" : "创建方法成功";
          let returnResult = this.changItemState(data);
          if(returnResult.index >=0) {
            let result = {
              id:data.id,
              refUniqueId:data.refItem,
              type:returnResult.item.type,
              uniqueId:returnResult.item.uniqueId,
            };
            this.strategyInfoData.splice(returnResult.index,1,result);
          };
          this.$message({
            message:message,
            type:'success',
          });
          this.getStepSequence();
        }
      },
      created() {
        if(this.operateType && this.operateType == "show") {
          this.options.disabled = true;
        };
        if(this.strategyResult) {
          //设置策略的基本内容
          this.strategyInfo.strategyName = this.strategyResult.strategyName;
          this.strategyInfo.description = this.strategyResult.description;
          this.strategyInfo.id = this.strategyResult.strategyId;
          //设置基本的步骤
          if(this.strategyResult.strategyStepVOS && this.strategyResult.strategyStepVOS.length>0) {
            for(let i=0;i<this.strategyResult.strategyStepVOS.length;i++) {
              this.uuid = this.uuid + 1;
              let item = this.strategyResult.strategyStepVOS[i];
              let data = {};
              data.id = item.strategyStepId;
              data.uniqueId = this.uuid;
              if(item.strategyType == "C") {
                data.type = "constructor";
                data.result = item.ccStrategyStepConstructorEntity;
                if(item.ccStrategyStepConstructorEntity.paramsValue && item.ccStrategyStepConstructorEntity.paramsValue.indexOf('$') == 0) {
                  let refItem = item.ccStrategyStepConstructorEntity.paramsValue.slice(1,item.ccStrategyStepConstructorEntity.paramsValue.length);
                  data.refUniqueId = refItem;
                }
              } else if(item.strategyType == "F") {
                data.type = "field";
                data.result = item.ccStrategyStepFieldEntity;
                if(item.ccStrategyStepFieldEntity.fieldValue && item.ccStrategyStepFieldEntity.fieldValue.indexOf('$') == 0) {
                  let refItem = item.ccStrategyStepFieldEntity.fieldValue.slice(1,item.ccStrategyStepFieldEntity.fieldValue.length);
                  data.refUniqueId = refItem;
                }
              } else if(item.strategyType == "M") {
                data.type = "method";
                data.result = item.ccStrategyStepMethodEntity;
                if(item.ccStrategyStepMethodEntity.paramsValue && item.ccStrategyStepMethodEntity.paramsValue.indexOf('$') == 0) {
                  let refItem = item.ccStrategyStepMethodEntity.paramsValue.slice(1,item.ccStrategyStepMethodEntity.paramsValue.length);
                  data.refUniqueId = refItem;
                }
              }
              this.strategyInfoData.push(data);
            }
          }
        };
      }
    }
</script>

<style lang="less">
  .render-item-wrapper{

  .render-item-title{
    &:hover{
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
    }
    position: relative;

    /*border-top: 1px solid #e8e8e8;
    border-right: 1px solid #e8e8e8;
    border-left: 1px solid #e8e8e8;*/
    border-top-left-radius:5px;
    border-top-right-radius:5px;

    padding:10px;
    font-size:14px;
    color: rgba(0, 0, 0, 0.43);
    background-color: #E7F5F2;
  }
  .title-configed{
    background-color: #E7F5F2;
  }
  .title-not-configed{
    background-color: #E2DED3;
  }
}
.config-container{
    margin-top: 10px;
    /*border-right: 1px solid #EFF2F7;
    border-bottom: 1px solid #EFF2F7;
    border-top: 1px solid #EFF2F7;*/
    width: 100%;
    background-color: #fff;
    padding: 10px;

    .config-clicked{
      border: 2px solid #429FFD;
      border-radius: 4px;
    }
  }

  .config-wrapper{
    background-color: #fff;
    display: inline-block;
    width: 100%;

    border: 1px solid #e8e8e8;
    border-bottom-right-radius:5px;
    border-bottom-left-radius:5px;
    transition: all .3s;

    &:hover{
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
    }

    .content{
      min-height: 0;
      padding:0;
      .ant-form-item{
        margin-bottom: 12px;
      }
    }
  }
</style>
