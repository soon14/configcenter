<template>
  <el-row>
    <div style="padding-left: 80px;">
      <el-upload
        class="upload-demo"
        ref="upload"
        action=""
        :file-list="fileList"
        :limit="1"
        :on-exceed="handleExceed"
        :before-upload="beforeUpload"
        :http-request="uploadFile"
        :auto-upload="false">
        <el-button slot="trigger"  type="primary">上传文件</el-button>
        <div slot="tip" class="el-upload__tip">{{uploadTip}}</div>
      </el-upload>
      <br/>
    </div>
  </el-row>
</template>

<script>
  export default {
    name: "UploadFile",
    data() {
      return {
        fileList: [],
        sizeUnit: 1024*1024
      };
    },
    props: {
      uploadTip: {
        type: String,
        required: false,
        default() {
          return "请选择文件";
        }
      },
      fileSize: {
        type: Number,
        required: false,
        default() {
          return 1;//默认大小限制为1M
        }
      },
      includeSuffix: {
        type: String,
        required: false,
        default() {
          return "*";//默认允许所有格式
        }
      }
    },
    methods: {
      submitUpload() {
        this.$refs.upload.submit();
      },
      beforeUpload(file) {
        if (this.includeSuffix !== "*"){
          let suffixArray = this.includeSuffix.split(",");
          let fileSuffix = file.name.substring(file.name.lastIndexOf('.')+1);

          if (suffixArray.indexOf(fileSuffix) === -1){
            this.$message.warning(`文件格式不符合要求，支持的后缀[${this.includeSuffix}]`);
            return false;
          }
        }
        if (file.size > this.fileSize*this.sizeUnit){
          this.$message.warning(`文件大小超出限制，最大为${this.fileSize}Mb`);
          return false;
        }
      },
      handleExceed(files, fileList) {
        this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
      },
      uploadFile(item) {
        const reader = new FileReader();
        reader.readAsDataURL(item.file);
        reader.onload = function () {
          let fileData = {
            isOld: false,
            uid: item.file.uid,
            type: item.file.type,
            name: item.file.name,
            url: reader.result
          };
          this.$emit("uploadFunction", fileData);
        }.bind(this)
      },
      clearFiles() {
        this.$refs.upload.clearFiles();
      }
    }
  }
</script>

<style scoped>

</style>
