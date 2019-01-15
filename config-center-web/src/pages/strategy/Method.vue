<template>
  <section>
    <div class="config-container">
      <div class="render-item-wrapper">

        <div class="render-item-title">
          <el-row>
            <span> 第{{sequence+1}}步:方法</span>
            <span  style="float:right" v-if="!disabled">
              <el-button type="primary" size="mini" @click="saveMethod('method')">{{extValue.id? "修改":"保存"}}</el-button>
              <el-button type="danger" size="mini" @click="deleteMethod" >移除</el-button>
            </span>
          </el-row>
        </div>
        <div class="config-wrapper">
          <div class="content" style="margin-top: 10px">
            <el-form ref="method" :model="method" label-width="80px" size="small" :rules="rules" :disabled="disabled">
              <el-form-item label="类名" prop="clazz" style="margin-right: 10px">
                <el-input v-model="method.clazz"></el-input>
              </el-form-item>
              <el-row type="flex" justify="space-between">
                <el-form-item label="类实例">
                  <el-input v-model="method.classInstance"></el-input>
                </el-form-item>
                <el-form-item label="方法名" prop="methodName" style="margin-right: 10px">
                  <el-input v-model="method.methodName"></el-input>
                </el-form-item>
              </el-row>
              <el-row type="flex" justify="space-between">
                <el-form-item label="参数类型">
                  <el-input v-model="method.paramsType"></el-input>
                </el-form-item>
                <el-form-item label="参数值" style="margin-right: 10px" prop="paramsValue">
                  <el-input v-model="method.paramsValue"></el-input>
                </el-form-item>
              </el-row>
              <el-form-item label="描述" style="margin-right: 10px">
                <el-input type="textarea" v-model="method.description"></el-input>
              </el-form-item>
            </el-form>
          </div>
        </div>

      </div>
    </div>
  </section>
</template>

<script>
    export default {
        name: "method",
        props:['extValue','strategyId','sequence','operateType'],
        data() {
          return {
            disabled:false,
            method: {
              clazz:"",
              classInstance:"",
              methodName:"",
              paramsType:"",
              paramsValue:"",
              description:"",
            },
            rules: {
              clazz: [
                {required: true, message: '类名不能为空', trigger: 'blur' },
              ],
              methodName: [
                {required: true, message: '方法名不能为空', trigger: 'blur' },
              ],
            }
          }
        },
      methods: {
        saveMethod(formName) {
          let appId = this.$router.history.current.params.appId;
          this.$refs[formName].validate((valid) => {
            if (valid) {
              let data = {
                appId:appId,
                clazz:this.method.clazz,
                classInstance:this.method.classInstance,
                methodName:this.method.methodName,
                paramsType:this.method.paramsType,
                paramsValue:this.method.paramsValue,
                description:this.method.description,
                stepNum:this.sequence+1,
                strategyId:this.strategyId,
                strategyStepId:this.extValue.id?this.extValue.id:""
              };
              let boolean = false;
              if(this.method.paramsValue.indexOf('$') === 0) {
                let refIndex = this.method.paramsValue.slice(1,this.method.paramsValue.length);
                if(refIndex >= (this.sequence+1)) {
                  boolean = true;
                }
              }
              if(boolean) {
                this.$message({
                  message:"步骤" + (this.sequence+1) + "引用的步骤不能在当前步骤的后面，请重新输入",
                  type:"error",
                });
              } else {
                this.$http.createStrategyMethod(data).then(res => {
                  let item = {
                    id:res.data.result,
                    uniqueId:this.extValue.uniqueId,
                  };
                  if(this.method.paramsValue.indexOf('$') !== -1) {
                    item.refItem = this.method.paramsValue.slice(1, this.method.paramsValue.length);
                  }
                  this.$emit('getMethodInfo',item);
                }).catch(err => {
                  console.log('err',err);
                })
              }
            } else {
              return false;
            }
          });
        },
        modifyParamValue(msg) {
          if(msg === 0) {
            this.rules = {
              clazz: this.rules.clazz,
              methodName: this.rules.methodName,
              paramsValue: [
                {required: true, message: '请重新填写参数值', trigger: 'change'},
              ],
            };
            this.method.paramsValue = "";
          } else {
            this.method.paramsValue = "$"+msg;
          }

        },
        getCurrentParamValue() {
          return this.method.paramsValue;
        },
        deleteMethod() {
          this.$emit('deleteItem');
        }
      },
      created() {
        if(this.operateType && this.operateType == "show") {
          this.disabled = true;
        };
        if(this.extValue && this.extValue.result) {
          this.method.clazz = this.extValue.result.clazz;
          this.method.classInstance = this.extValue.result.classInstance;
          this.method.methodName = this.extValue.result.methodName;
          this.method.paramsType = this.extValue.result.paramsType;
          this.method.fieldValue = this.extValue.result.fieldValue;
          this.method.paramsValue = this.extValue.result.paramsValue;
        }
      }
    }
</script>

<style scoped>

</style>
