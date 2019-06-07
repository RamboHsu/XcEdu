//cms的页面需要在home页面中显示，需要导入home页面
import Home from '@/module/home/page/home.vue';
import page_list from '@/module/cms/page/page_list.vue';
import page_add from '@/module/cms/page/page_add.vue';
import page_edit from '@/module/cms/page/page_edit.vue';



//在路由js中配置信息也需要导入，不然也无法使用
export default [{
  path:'/cms',//编写src/module下的路径
  component:Home,//将cms引入的组件名称
  name:'CMS内容管理',//cms组件名称
  hidden:false,//内容是否隐藏
  children:[//添加路由配置
    {path: '/cms/page/list',name: '页面列表',component: page_list,hidden: false},
    {path: '/cms/page/add',name: '添加页面',component: page_add,hidden: true},
    {path: '/cms/page/edit/:pageId',name: '修改页面',component: page_edit,hidden: true}
  ]
}]
