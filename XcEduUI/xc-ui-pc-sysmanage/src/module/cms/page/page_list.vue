<template>
  <!--编写静态页面部分，即View部分-->
  <div>
    <!--查询表单-->
    <el-form :model="params">
      <el-select v-model="params.siteId" placeholder="请选择站点">
        <el-option
          v-for="item in siteList"
          :key="item.siteId"
          :label="item.siteName"
          :value="item.siteId">
        </el-option>
      </el-select>
      <!--查询模板-->
      <el-select v-model="params.templateId" placeholder="请选择模板">
        <el-option
          v-for="item in templateList"
          :key="item.templateId"
          :label="item.templateName"
          :value="item.templateId">
        </el-option>
      </el-select>
      页面名称：
      <el-input v-model="params.pageName" style="width: 100px"></el-input>
      页面别名：
      <el-input v-model="params.pageAliase" style="width: 100px"></el-input>
      <el-radio-group v-model="params.pageType">
        <el-radio class="radio" label="0">静态</el-radio>
        <el-radio class="radio" label="1">动态</el-radio>
      </el-radio-group>
      <el-button type="primary" @click="query" size="small">查询</el-button>

      <router-link class="mui-tab-item"
                   :to="{path:'/cms/page/add',
                        query:{
                               page:this.params.page,
                               siteId:this.params.siteId,
                               pageAliase:this.params.pageAliase
                               }
                        }">
        <el-button type="primary" size="small">新增页面</el-button>
      </router-link>
    </el-form>
    <!--将:data后的值改为list，对应vm中data定义的list-->
    <el-table
      :data="list"
      border
      style="width:100%">
      <!---------------修改的列表中的prop属性值，与返回数据的属性名一致-------------------->
      <el-table-column type="index" width="60"></el-table-column>
      <el-table-column prop="pageName" label="页面名称" width="120"></el-table-column>
      <el-table-column prop="pageAliase" label="别名" width="140"></el-table-column>
      <el-table-column prop="pageType" label="页面类型" width="70"></el-table-column>
      <el-table-column prop="pageWebPath" label="访问路径" width="250"></el-table-column>
      <el-table-column prop="pagePhysicalPath" label="物理路径" width="250"></el-table-column>
      <el-table-column prop="pageCreateTime" label="创建时间" width="180"></el-table-column>
      <el-table-column label="操作" width="280">
        <template slot-scope="page">
          <el-button size="small" @click="edit(page.row.pageId)">编辑</el-button>
          <el-button size="small" @click="del(page.row.pageId)">删除</el-button>
          <el-button size="small" @click="preview(page.row.pageId)">页面预览</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!--------------------添加的分页组件------------------------->
    <el-pagination
      layout="prev, pager, next"
      :page-size="params.size"
      :total="total"
      :current-page="params.page"
      v-on:current-change="changePage"
      style="float:right;">
    </el-pagination>
    <!--------------------添加的分页组件------------------------->
  </div>
</template>
<script>
  /************添加cms.js引用*****************/
  //导入对cms.js文件的引用
  import * as cmsApi from '../api/cms';

  /************添加cms.js引用*****************/

//定义VM中的分页数据，并将数据进行导出
  export default {
    data() {
      return {
        templateList: [],//模板集合数据
        siteList: [],//增加的站点集合数据
        list: [],//定义table列表中显示的数据
        total: 50, //总条数
        params: {
          templateId: '',//增加查询条件cmsPage的模板id
          siteId: '',//增加的查询条件cmsPage的站点id
          pageAliase: '',//增加的查询条件cmsPage的页面别名
          pageName: '',//增加查询条件cmsPage的页面名称
          pageType: '',//增加查询条件cmsPage的页面类型
          page: 1,//页码
          size: 10//每页显示个数
        }
      }
    },
    methods: {
      //点击页面调用的change事件方法,
      //事件触发都会将点击的页面传入该方法中
      changePage: function (page) {
        this.params.page = page;//将传来的页面传给vm中定义页码变量
        this.query();//调用查询方法
      },
      //点击事件的query事件方法
      query: function () {
        cmsApi.page_list(this.params.page, this.params.size, this.params).then((res) => {
          console.log(res);
          this.total = res.queryResult.total;
          this.list = res.queryResult.list;
        })
      },
      //添加编辑方法
      edit: function (pageId) {//打开修改页面
        this.$router.push({
          path: '/cms/page/edit/' + pageId,
          query: {
            page: this.params.page,
            siteId: this.params.siteId,
            pageAliase: this.params.pageAliase
          }
        })
      },
      //添加删除方法
      del: function (pageId) {
        this.$confirm('确定删除？', '提示', {}).then(() => {
          cmsApi.page_del(pageId).then((res) => {
            console.log(res);
            if (res.success) {
              this.$message({
                message: '删除成功',
                type: 'success'
              });
              this.query();//查询页面
            } else {
              this.$message.error('删除失败')
            }
          })
        })
      },
      preview(pageId) {
        window.open("http://www.xuecheng.com/cms/preview/" + pageId)
      }
    },

    /***************************添加生命周期钩子函数 mounted()***************************/
    mounted() {
      //默认查询页面
      this.query();
      //初始化站点列表
      this.siteList = [
        {
          siteId: '5a751fab6abb5044e0d19ea1',
          siteName: '门户主站'
        },
        {
          siteId: '102',
          siteName: '测试站'
        }
      ]
    },
    /***************************添加生命周期钩子函数 created()***************************/
    created() {
      //获取page_add回传回来的当前页数据
      this.params.page = Number.parseInt(this.$route.query.page || 1),
        //获取page_add回传回来的站点Id值
        this.params.siteId = this.$route.query.siteId || '',
        //获取page_add回传回来的页面别名
        this.params.pageAliase = this.$route.query.pageAliase || ''
    }
  }
</script>
<style>
  /*编写页面样式，不是必须*/

</style>
