<template>
  <section>
    <code-diff :old-string="oldString" :new-string="newString" :context="context" :output-format="outputFormat" />
  </section>
</template>

<script>
  import codeDiff from './code-diff/index';
  export default {
    components: {
      codeDiff
    },
    name:"code-view",
    data() {
      return {
      }
    },
    props: {
      oldString: {
        type: String,
        default: ''
      },
      newString: {
        type: String,
        default: ''
      },
      context: {
        type: Number,
        default: 5
      },
      outputFormat: {
        type: String,
        // default: 'line-by-line',
        default: 'side-by-side',
      }
    },
  }
</script>

<style>

</style>
