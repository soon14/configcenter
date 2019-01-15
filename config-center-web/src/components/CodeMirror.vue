<template>
	<codemirror ref="myCm"
              id="code"
              :value="code"
              :options="cmOptions"
              @input="onCmCodeChange">
    </codemirror>
</template>

<script>
	import { codemirror } from 'vue-codemirror';
	import 'codemirror/lib/codemirror.css';
  import 'codemirror/mode/xml/xml.js';
	import 'codemirror/theme/monokai.css';

  import'codemirror/mode/clike/clike.js'
  import'codemirror/addon/edit/matchbrackets.js'
  import'codemirror/addon/comment/comment.js'
  import'codemirror/addon/dialog/dialog.js'
  import'codemirror/addon/dialog/dialog.css'
  import'codemirror/addon/search/searchcursor.js'
  import'codemirror/addon/search/search.js'
  import'codemirror/keymap/sublime.js'
  export default {
		name:"code-mirror",
		components: {
	      codemirror,
	    },
	    props: {
	      content:{
	        type: String,
	        required:true,
	      },
        readOnly:{
	        type:Boolean,
          required:false,
          default:function() {
            return false;
          }
        }
	    },
	    data() {
	      return {
	        code:this.getDealData(this.content),
	        cmOptions: {
	          tabSize: 4,
            mode: 'text/html',
            keyMap: "sublime",
	          theme: 'monokai',
	          lineNumbers: true,
	          line: true,
            readOnly:this.readOnly,
	        }
	      }
	    },
	    computed: {
		    codemirror() {
		      return this.$refs.myCm.codemirror
		    }
		  },
	    methods: {
          onCmCodeChange(newCode) {
            this.code = newCode;
          },
		      getDealData(msg) {
		        return msg;
		      },
		      returnValueToParent() {
		      	let data = this.code;
            this.$emit('returnValueToParent',data);
		      }
	    },
	    mounted() {
	    }
	}
</script>

<style>
  #code > .CodeMirror {
    height: 450px;
  }
</style>
