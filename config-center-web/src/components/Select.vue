<template>
	<section :select="select">
		<el-select :value="value" :clearable="select.clearable"
				   :placeholder="select.placeholder" @input="change($event)">

			<el-option
				v-for="item in select.options"
				:key="item.value"
				:label="item.text"
				:value="item.value"
			>
			</el-option>
		</el-select>
	</section>
</template>

<script>
	export default {
		name: 'cSelect',
		props: {
			select: {
				type: Object,
				default() {
					return {
						options:[],
						clearable:false,
						placeholder:"请选择",
					}
				}
			},
			value:{
				type: String,
				default: "",
			}
		},
		methods: {
			//父组件中的v-model等于子组件的属性value和@input
			change: function(value) {
				//将这个值抛出去，使父组件能够接受到这个值
				this.$emit('input', value);
			}
		}
	}
</script>

<style></style>
