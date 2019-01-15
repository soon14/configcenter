// 高亮信息展示
//使用方式<div v-highlight><pre><code v-html="html"></code></pre></div>
import Vue from 'vue'
import hljs from "highlight.js";
import "highlight.js/styles/github.css";
Vue.directive('highlight', {
  inserted(el) {
    let blocks = el.querySelectorAll('pre code');
    blocks.forEach((block) => {
      hljs.highlightBlock(block);
    })
  },
})

//比较diff的内容
/*<div style="max-height: 100px;">
    <vue-code-diff :old-string="oldStr" :new-string="newStr" :context="10"/>
</div>*/