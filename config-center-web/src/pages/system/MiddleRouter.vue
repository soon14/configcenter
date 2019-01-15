<template>
	<div></div>
</template>
<script>
	//写一个中间的组件用来路由到另外一个组件，可以保证每次组件都能够重新刷新页面
	export default {
		data() {
			this.$router.push({
				path:this.$router.history.current.params.data.routerUrl,
				name: this.$router.history.current.params.data.routerName, //必须,不然参数传不过去
				params:this.$router.history.current.params.data,
			});
			return {
			}
		},
    created() {
    }
	}
</script>

<style></style>
