import Vue from 'vue';
import Router from 'vue-router';
Vue.use(Router);
// 定义路由配置
let routes = []
let concat = (router) => {
  routes = routes.concat(router)
}
// // 导入路由规则
import HomeRouter from '@/module/home/router' //导入home中的路由
import CmsRouter from '@/module/cms/router' //导入Cms中的路由
// 合并路由规则
concat(HomeRouter) //将home的路由合并到项目中
concat(CmsRouter) //将Cms的路由合并到项目中
export default routes;
