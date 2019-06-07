//导入模块文件model.js的add方法
var {add}=require('./model.js');
//导入vue的核心文件，文件的后缀.js可以省略
var Vue=require('./vue.min');

/*****************html页面中*******************/
// 实例化Vue对象
// vm :叫做MVVM中的 View Model
var VM = new Vue({
    el: "#app",//表示当前vue对象接管app的div区域
    data: {
        name: '黑马程序员',// 相当于是MVVM中的Model这个角色
        num1: 0,
        num2: 0,
        result: 0,   //计算结果数据
        url: 'http://www.itcast.cn/'
    },
    //定义vm中的方法
    methods: {
        //定义计算事件触发的方法
        change: function () {
            // this.result = Number.parseInt(this.num1)+Number.parseInt(this.num2)
            //调用model.js中的add方法
            this.result = add(Number.parseInt(this.num1),Number.parseInt(this.num2))
        }
    }
});
/*****************html页面中*******************/