//计算加法的方法
function add(x, y) {
    return x+y;
}
//如果还有其他方法也可以在下面进行定义
//比如下面的方法add2
function add2(x, y) {
    return x + y+1
}


//在webpack的模块文件js中，除了定义编写js方法外，还要将方法进行导出操作 这样，外面才能调用到定义的 add方法了，不然其他的模块无法调用到add方法

/*******新添加导出方法********/
//将定义的方法进行导出
// 导出add方法

//方式一  单独将add方法导出
module.exports.add = add;

//方式二  可以导出多个方法
//这种方式不仅导出add方法还可以导出add2方法
//module.exports ={add,add2}; //此为es5的语法​
/*******新添加导出方法********/