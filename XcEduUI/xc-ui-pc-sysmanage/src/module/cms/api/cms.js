//public是对axios的工具类封装，定义了http请求方法
import http from './../../../base/api/public'  //这里的导入也可以使用下面的形式: import http from '@/base/api/public'
import utils from './../../../common/utils.js'
//导入querySrting工具组件，querystring在package-lock.json已配置
import querystring from 'querystring'

//导入标识前缀的统一配置文件sysConfig.js
let sysConfig=require('@/../config/sysConfig.js');
let apiUrl=sysConfig.xcApiUrlPre;//调用前缀的标识


//编写调用微服务的接口地址，并将方法通过export const进行导出，提供给外部使用
export const page_list = (page,size,params) => {
  //将json对象转换成key/value对
  let query = querystring.stringify(params);
  //对微服务的地址进行修改，添加调用前缀的标识,在get请求路径后拼接key=value请求参数
  return http.requestQuickGet(apiUrl+'/cms/page/list/'+page+'/'+size+'/?'+query)
}

/***********************添加cmsPage添加的接口地址服务**********************/
//调用微服务cmsPage添加的接口地址
export const page_add = params =>{
  return http.requestPost(apiUrl+"/cms/page/add",params);
}

/****************根据cmsPage的Id查询数据****************/
export const page_get = (id) =>{
  return http.requestQuickGet(apiUrl+"/cms/page/get/"+id)
}

/****************根据cmsPage的pageName查询数据****************/
export const page_findByPageName = (pageName) =>{
  return http.requestQuickGet(apiUrl+"/cms/page/findByPageName/"+pageName)
}

/*********************CmsPage数据修改，采用put方法******************/
export const page_edit = (id,params) =>{
  return http.requestPut(apiUrl+"/cms/page/edit/"+id,params)
}

/**************根据id删除CmsPage数据，采用delete方法******************/
export const page_del = id =>{
  return http.requestDelete(apiUrl+"/cms/page/del/"+id)
}
