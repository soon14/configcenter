<template>
  <section>
    <div>
      <div class="quill-editor-style">
        <quill-editor ref="myTextEditor"
                      v-model="content"
                      :options="editorOption"
                      @blur="onEditorBlur($event)"
                      @focus="onEditorFocus($event)"
                      @change="onEditorChange($event)"
                      @ready="onEditorReady($event)">
        </quill-editor>
      </div>
    </div>
  </section>
</template>

<script>
  import hljs from 'highlight.js';
  export default {
    props: {
      showData:{
        type: String,
        required:true,
      }
    },
    data() {
      return {
        content:this.dealString(this.showData),
        lastContent:'',
        editorOption: {
          modules: {
            toolbar: [
              ['bold', 'italic', 'underline', 'strike'],
              ['blockquote', 'code-block'],
              [{ 'header': 1 }, { 'header': 2 }],
              [{ 'list': 'ordered' }, { 'list': 'bullet' }],
              [{ 'script': 'sub' }, { 'script': 'super' }],
              [{ 'indent': '-1' }, { 'indent': '+1' }],
              [{ 'direction': 'rtl' }],
              [{ 'size': ['small', false, 'large', 'huge'] }],
              [{ 'header': [1, 2, 3, 4, 5, 6, false] }],
              [{ 'font': [] }],
              [{ 'color': [] }, { 'background': [] }],
              [{ 'align': [] }],
              ['clean'],
              ['link', 'image', 'video']
            ],
          }
        }
      }
    },
    methods: {
      dealString(msg) {
        let data = this.hTMLEncode(msg);
        return data;
      },
      onEditorBlur(editor) {
      },
      onEditorFocus(editor) {
      },
      onEditorReady(editor) {
      },
      onEditorChange({ editor, html, text }){
        this.lastContent = text;
      },
      hTMLEncode(str) {
        let s = "";
        if (str.length == 0) return "";
        s = str.replace(/&/g, "&amp;");
        s = s.replace(/</g, "&lt;");
        s = s.replace(/>/g, "&gt;");
        s = s.replace(/ /g, "&nbsp;");
        s = s.replace(/\'/g, "&#39;");
        s = s.replace(/\"/g, "&quot;");
        s = s.replace(/\n/g, "<br/>");
        return s;
      },
      hTMLDecode(str) {
        let s = "";
        if (str.length == 0) return "";
        s = str.replace(/&amp;/g, "&");
        s = s.replace(/&lt;/g, "<");
        s = s.replace(/&gt;/g, ">");
        s = s.replace(/&nbsp;/g, " ");
        s = s.replace(/&#39;/g, "\'");
        s = s.replace(/&quot;/g, "\"");
        s = s.replace(/<br\/>/g, "\n");
        return s;
      },
  returnValueToParent() {
        let currentStr = this.$refs.myTextEditor.$refs.editor.innerText;
        let oldArray = currentStr.split('\n\n');
        let newArray = [];
        for(let i=0;i<oldArray.length;i++) {
          newArray.push(this.trimRight(oldArray[i]));
        }
        let newStr = newArray.join("\n");

        let str = this.hTMLDecode(this.content);

        this.$emit('returnValueToParent',newStr);
      },
      trimRight(s){
        if(s == null) return "";
        var whitespace = new String(" \t\n\r");
        var str = new String(s);
        if (whitespace.indexOf(str.charAt(str.length-1)) != -1){
          var i = str.length - 1;
          while (i >= 0 && whitespace.indexOf(str.charAt(i)) != -1){
            i--;
          }
          str = str.substring(0, i+1);
        }
        return str;
      },

     /* cancelDoubleEnter(msg) {
        let arr = [];
        for(let i=0;i<msg.length;i++) {
          if(msg[i] == ' ') continue;
          if(msg[i] == '\n' && msg[i+1] == '\n') {
            i++;
          }
          arr.push(msg[i]);
        };
        return arr.join('');
      }*/
    },
    computed: {
    },
    created() {
    },
    mounted() {
    }
  }
</script>

<style lang="scss" scoped>
  .quill-editor-style > .quill-editor /deep/ .ql-container {
    min-height: 300px;
  }


</style>
