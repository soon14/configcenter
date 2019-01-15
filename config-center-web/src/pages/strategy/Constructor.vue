<template>
  <section>
    <div class="config-container">
      <div class="render-item-wrapper">
        <div class="render-item-title">
          <el-row>
            <span> 第{{sequence+1}}步:构造器</span>
            <span  style="float:right" v-if="!disabled">
              <el-button type="primary" size="mini" @click="saveConstructor('constructor')">{{extValue.id? "修改":"保存"}}</el-button>
              <el-button type="danger" size="mini" @click="deleteConstructor" >移除</el-button>
            </span>
          </el-row>
        </div>

        <div class="config-wrapper">
          <div class="content" style="margin-top: 10px">
            <el-form ref="constructor" :model="constructor" label-width="80px" size="small" :rules="rules" :disabled="disabled">
            <el-form-item label="类名" prop="clazz" style="margin-right: 10px">
              <el-input v-model="constructor.clazz"></el-input>
            </el-form-item>
            <el-row type="flex" justify="space-between">
              <el-form-item label="参数类型">
                <el-input v-model="constructor.paramsType"></el-input>
              </el-form-item>
              <el-form-item label="参数值" prop="paramsValue" style="margin-right: 10px">
                <el-input v-model="constructor.paramsValue"></el-input>
              </el-form-item>
            </el-row>
            <el-form-item label="描述" prop="description" style="margin-right: 10px">
              <el-input type="textarea" v-model="constructor.description"></el-input>
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
        name: "constructor",
        props:['extValue','strategyId','sequence','operateType'],
        data() {
          return {
            disabled:false,
            constructor: {
              clazz:"",
              paramsType:"",
              paramsValue:"",
              description:"",
            },
            rules: {
              clazz: [
                {required: true, message: '类名不能为空', trigger: 'blur' },
              ],
            }
          }
        },
      methods: {
        saveConstructor(formName) {
          let appId = this.$router.history.current.params.appId;
          this.$refs[formName].validate((valid) => {
            if (valid) {
              let data = {
                appId:appId,
                clazz:this.constructor.clazz,
                paramsType:this.constructor.paramsType,
                paramsValue:this.constructor.paramsValue,
                description:this.constructor.description,
                stepNum:this.sequence+1,
                strategyId:this.strategyId,
                strategyStepId:this.extValue.id?this.extValue.id:""
              };
              //没有保存的数据，需要保存的时候才判断引用值是否满足条件
              let boolean = false;
              if(this.constructor.paramsValue.indexOf('$') === 0) {
                let refIndex = this.constructor.paramsValue.slice(1,this.constructor.paramsValue.length);
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
                this.$http.createStrategyConstructor(data).then(res => {
                  let item = {
                    id:res.data.result,
                    uniqueId:this.extValue.uniqueId,
                  };
                  if(this.constructor.paramsValue.indexOf('$') === 0) {
                    item.refItem = this.constructor.paramsValue.slice(1, this.constructor.paramsValue.length);
                  }
                  this.$emit('getConstructorInfo',item);
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
              paramsValue: [
                {required: true, message: '请重新填写参数值', trigger: 'change'},
              ],
            };
            this.constructor.paramsValue = "";
          } else {
            this.constructor.paramsValue = "$"+msg;
          }
        },
        getCurrentParamValue() {
          return this.constructor.paramsValue;
        },
        deleteConstructor() {
          this.$emit('deleteItem');
        }
      },
      created() {
        if(this.operateType && this.operateType == "show") {
          this.disabled = true;
        };
        if(this.extValue && this.extValue.result) {
          this.constructor.clazz = this.extValue.result.clazz;
          this.constructor.paramsType = this.extValue.result.paramsType;
          this.constructor.paramsValue = this.extValue.result.paramsValue;
          this.constructor.description = this.extValue.result.description;
        }
      }
    }
</script>

<style scoped>

</style>
