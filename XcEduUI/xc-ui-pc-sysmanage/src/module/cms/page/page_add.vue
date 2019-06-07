<template>
  <div>
    <!--给el-form添加验证数据:rules;
        给el-form添加ref属性，在表单校验时引用此表单对象,ref 的值和 :model 的值保持一致-->
    <el-form :model="pageForm" :rules="pageFormRules" label-width="80px" ref="pageForm">
      ​
      <!--所属站点下拉框-->
      <el-form-item label="所属站点" prop="siteId">
        <el-select v-model="pageForm.siteId" placeholder="请选择站点">
          <el-option
            v-for="item in siteList"
            :key="item.siteId"
            :label="item.siteName"
            :value="item.siteId">
          </el-option>
        </el-select>
      </el-form-item>
      ​
      <!--模版下拉框-->
      <el-form-item label="选择模版" prop="templateId">
        <el-select v-model="pageForm.templateId" placeholder="请选择">
          <el-option
            v-for="item in templateList"
            :key="item.templateId"
            :label="item.templateName"
            :value="item.templateId">
          </el-option>
        </el-select>
      </el-form-item>
      ​
      <!--页面名称输入框-->
      <el-form-item label="页面名称" prop="pageName">
        <el-input v-model="pageForm.pageName" auto-complete="off"></el-input>
      </el-form-item>
      ​
      <!--别名输入框-->
      <el-form-item label="别名" prop="pageAliase">
        <el-input v-model="pageForm.pageAliase" auto-complete="off"></el-input>
      </el-form-item>
      ​
      <!--访问路径输入框-->
      <el-form-item label="访问路径" prop="pageWebPath">
        <el-input v-model="pageForm.pageWebPath" auto-complete="off"></el-input>
      </el-form-item>
      ​
      <!--物理路径输入框-->
      <el-form-item label="物理路径" prop="pagePhysicalPath">
        <el-input v-model="pageForm.pagePhysicalPath" auto-complete="off"></el-input>
      </el-form-item>

      <!--DataUrl输入框-->
      <el-form-item label="DataUrl" prop="dataUrl">
        <el-input v-model="pageForm.dataUrl" auto-complete="off" ></el-input>
      </el-form-item>
      ​
      <!--类型输入框-->
      <el-form-item label="类型">
        <el-radio-group v-model="pageForm.pageType">
          <el-radio class="radio" label="0">静态</el-radio>
          <el-radio class="radio" label="1">动态</el-radio>
        </el-radio-group>
      </el-form-item>
      ​
      <!--创建时间输入框-->
      <el-form-item label="创建时间">
        <el-date-picker type="datetime" placeholder="创建时间" v-model="pageForm.pageCreateTime"></el-date-picker>
      </el-form-item>
      ​
    </el-form>
    ​
    <!--form表单底部的提交按钮-->
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="addSubmit">提交</el-button>
      <!--返回pageList页面的按钮-->
      <el-button type="primary" @click="go_back">返回</el-button>
    </div>

  </div>
</template>
<script>
  import * as cmsApi from '../api/cms';
  import utils from './../../../common/utils.js'
  //定义的vm中的分页数据,并将数据进行导出
  export default {
    data() {
      return {
        //站点列表
        siteList: [],
        //模版列表
        templateList: [],
        //新增界面数据
        pageForm: {
          siteId: '',
          templateId: '',
          pageName: '',
          pageAliase: '',
          pageWebPath: '',
          pageParameter: '',
          pagePhysicalPath: '',
          dataUrl:'',
          pageType: '',
          pageCreateTime: new Date()
        },
        /*************添加的rules数据规则**********/
        pageFormRules: {
          siteId: [{required: true, message: '请选择站点', trigger: 'blur'}],
          templateId: [{required: true, message: '请选择模板', trigger: 'blur'}],
          pageName: [{required: true, message: '请输入页面名称', trigger: 'blur'}],
          pageWebPath: [{required: true, message: '请输入访问路径', trigger: 'blur'}],
          pagePhysicalPath: [{required: true, message: '请输入物理路径', trigger: 'blur'}]
        }
      }
    },
    methods: {
      //page_add表单提交触发的方法
      addSubmit: function () {
        this.$refs.pageForm.validate((valid) => {
          console.info(valid);
          if (valid) {
            /*************1.表单验证通过或添加用户确认窗口**************/
            this.$confirm('确认提交？', '提示', {}).then(() => {
              /**********表单验证成功后，调用远端接口Api**************/
              cmsApi.page_add(this.pageForm).then((res) => {
                console.log(res);//打印相应数据
                /*****************2.提交后的响应数据弹窗显示*********************/
                if (res.success){
                  this.$message({
                    message:'提交成功',
                    type:'success'
                  });
                  this.$refs['pageForm'].resetFields();
                }else if (res.message) {
                  /************提交后响应失败数据，并提示错误信息***********/
                  this.$message.error(res.message);
                }else {
                 this.$message.error('提交失败')
                }
              })

            })
          } else {
            alert('校验失败');
            return false;
          }
        })
      },
      //返回page_list方法
      go_back: function () {
        this.$router.push({
          path: "/cms/page/list",
          query: {
            page: this.$route.query.page, //获得page_list传来的当前页数据
            siteId: this.$route.query.siteId, //获得page_list传来的站点Id值
            pageAliase: this.$route.query.pageAliase  //获得page_list传来的页面模板Id值
          }
        })
      }
    },
    mounted() {
      //初始化站点列表静态数据
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
      //初始化模板列表静态数据
      this.templateList = [
        {
          templateId: '5a962b52b00ffc514038faf7',
          templateName: '首页'
        },
        {
          templateId: '5a962bf8b00ffc514038fafa',
          templateName: '轮播图'
        }
      ]
    }
  }
</script>
<style>
  /*css样式*/
</style>
