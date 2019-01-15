<template>
  <section>
    <div class="config-container">
      <div class="render-item-wrapper">
        <div class="render-item-title">
          <el-row>
            <span> 第{{sequence+1}}步:属性</span>
            <span  style="float:right" v-if="!disabled">
              <el-button type="primary" size="mini" @click="saveField('field')">{{extValue.id? "修改":"保存"}}</el-button>
              <el-button type="danger" size="mini" @click="deleteField" >移除</el-button>
            </span>
          </el-row>
        </div>
        <div class="config-wrapper">
          <div class="content" style="margin-top: 10px">
            <el-form ref="field" :model="field" label-width="80px" size="small" :rules="rules" :disabled="disabled">
              <el-form-item label="类名" prop="clazz" style="margin-right: 10px">
                <el-input v-model="field.clazz"></el-input>
              </el-form-item>
              <el-row type="flex" justify="space-between">
                <el-form-item label="类实例">
                  <el-input v-model="field.classInstance"></el-input>
                </el-form-item>
                <el-form-item label="数据类型" prop="dataType" style="margin-right: 10px">
                  <el-input v-model="field.dataType"></el-input>
                </el-form-item>
              </el-row>
              <el-row type="flex" justify="space-between">
                <el-form-item label="属性名" prop="fieldName">
                  <el-input v-model="field.fieldName"></el-input>
                </el-form-item>
                <el-form-item label="属性值" prop="fieldValue" style="margin-right: 10px">
                  <el-input v-model="field.fieldValue"></el-input>
                </el-form-item>
              </el-row>
              <el-form-item label="描述" style="margin-right: 10px">
                <el-input type="textarea" v-model="field.description"></el-input>
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
        name: "field",
        props:['extValue','strategyId','sequence','operateType'],
        data() {
          return {
            disabled:false,
            field: {
              clazz:"",
              classInstance:"",
              dataType:"",
              fieldName:"",
              fieldValue:"",
              description:"",
            },
            rules: {
              clazz: [
                {required: true, message: '类名不能为空', trigger: 'blur' },
              ],
              classInstance: [
                {required: true, message: '类实例不能为空', trigger: 'blur' },
              ],
              dataType: [
                {required: true, message: '数据类型不能为空', trigger: 'blur' },
              ],
              fieldName: [
                {required: true, message: '字段名不能为空', trigger: 'blur' },
              ],
              fieldValue: [
                {required: true, message: '字段值不能为空', trigger: 'blur' },
              ]
            }
          }
        },
      methods: {
        saveField(formName) {
          let appId = this.$router.history.current.params.appId;
          this.$refs[formName].validate((valid) => {
            if (valid) {
              let data = {
                appId:appId,
                clazz:this.field.clazz,
                classInstance:this.field.classInstance,
                dataType:this.field.dataType,
                fieldName:this.field.fieldName,
                fieldValue:this.field.fieldValue,
                description:this.field.description,
                stepNum:this.sequence+1,
                strategyId:this.strategyId,
                strategyStepId:this.extValue.id?this.extValue.id:""
              };
              let boolean = false;
              if(this.field.fieldValue.indexOf('$') === 0) {
                let refIndex = this.field.fieldValue.slice(1,this.field.fieldValue.length);
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
                this.$http.createStrategyField(data).then(res => {
                  let item = {
                    id:res.data.result,
                    uniqueId:this.extValue.uniqueId,
                  };
                  if(this.field.fieldValue.indexOf('$') !== -1) {
                    item.refItem = this.field.fieldValue.slice(1, this.field.fieldValue.length);
                  }
                  this.$emit('getFieldInfo',item);
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
            this.rules.fieldValue = [
              {required: true, message: '请重新填写属性值', trigger: 'change' },
            ];
            this.field.fieldValue = "";
          } else {
            this.field.fieldValue = "$"+msg;
          }
        },
        getCurrentParamValue() {
          return this.field.fieldValue;
        },
        deleteField() {
          this.$emit('deleteItem');
        }
      },
      created() {
        if(this.operateType && this.operateType == "show") {
          this.disabled = true;
        };
        if(this.extValue && this.extValue.result) {
          this.field.clazz = this.extValue.result.clazz;
          this.field.classInstance = this.extValue.result.classInstance;
          this.field.dataType = this.extValue.result.dataType;
          this.field.fieldName = this.extValue.result.fieldName;
          this.field.fieldValue = this.extValue.result.fieldValue;
          this.field.description = this.extValue.result.description;
        }
      }
    }
</script>

<style scoped>

</style>
