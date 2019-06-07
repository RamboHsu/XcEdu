package com.xuecheng.manage_cms.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * @author Rambo
 */
@Service
public class PageService {

    @Autowired
    CmsTemplateRepository cmsTemplateRepository;
    @Autowired
    CmsPageRepository cmsPageRepository;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    GridFsTemplate gridFsTemplate;
    @Autowired
    GridFSBucket gridFSBucket;

    /**
     * 页面列表分页查询
     *
     * @param page             当前页码
     * @param size             页面显示个数
     * @param queryPageRequest 查询条件
     * @return 页面列表
     */
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        //条件匹配器
        //页面名称模糊查询，需要自定义字符串的匹配器实现模糊查询
        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("pageAlias", ExampleMatcher.GenericPropertyMatchers.contains());
        //条件值
        CmsPage cmsPage = new CmsPage();
        //站点id
        if (StringUtils.isNotEmpty(queryPageRequest.getSiteId())) {
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        //页面别名
        if (StringUtils.isNotEmpty(queryPageRequest.getPageAlias())) {
            cmsPage.setPageAlias(queryPageRequest.getPageAlias());
        }
        //创建实例条件
        Example example = Example.of(cmsPage, matcher);
        //页码
        page = page - 1;
        //分页对象
        Pageable pageable = new PageRequest(page, size);
        //分页查询
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        QueryResult<CmsPage> cmsPageQueryResult = new QueryResult<CmsPage>();
        cmsPageQueryResult.setList(all.getContent());
        cmsPageQueryResult.setTotal(all.getTotalElements());
        //返回结果
        return new QueryResponseResult(CommonCode.SUCCESS, cmsPageQueryResult);
    }

    /**
     * cmsPage添加方法
     *
     * @param cmsPage 需要添加的cmsPage数据
     * @return CmsPageResult Api接口规范的响应结果对象
     */
    public CmsPageResult add(CmsPage cmsPage) {
        //1.如果传来的cmsPage数据为空，返回失败结果
        if (ObjectUtils.isEmpty(cmsPage)) {//return new CmsPageResult(CommonCode.FAIL,null);
            //返回非法参数自定义异常信息
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }

        //2.根据页面名称、站点id、页面访问路径三个值的唯一判断cmsPage是否存在
        CmsPage one = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if (!ObjectUtils.isEmpty(one)) {//return new CmsPageResult(CommonCode.FAIL,null);
            //返回页面存在的自定义异常信息
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        //3.如果mongodb中没有对应的数据，再保存cmsPage
        //为了保存新数据，可以设置cmsPage的Id为空
        cmsPage.setPageId(null);
        cmsPageRepository.save(cmsPage);

        //4.保存完毕，返回成果的结果
        return new CmsPageResult(CommonCode.SUCCESS, cmsPage);
    }


    /**
     * 根据Id查询CmsPage数据
     *
     * @param id -- String  CmsPage的Id值
     * @return CmsPage CmsPage实体类对象
     */
    public CmsPageResult getById(String id) {
        //1.判断id是否为空
        if (StringUtils.isBlank(id)) {
            return null;
        }
        //2.根据id查询
        Optional<CmsPage> cmsPageOptional = cmsPageRepository.findById(id);
        //3.返回结果数据
        CmsPage cmsPage = cmsPageOptional.isPresent() ? cmsPageOptional.get() : null;
        //4.根据查询后的数据返回结果
        if (cmsPageOptional.isPresent()) {
            return new CmsPageResult(CommonCode.SUCCESS, cmsPage);
        } else {
            return new CmsPageResult(CommonCode.FAIL, null);
        }
    }


    /**
     * 根据pageName精确查询CmsPage数据
     *
     * @param pageName -- String  CmsPage的pageName
     * @return CmsPage CmsPage实体类对象
     */
    public CmsPageResult findByPageName(String pageName) {
        //1.判断pageName是否为空
        if (StringUtils.isBlank(pageName)) {
            return null;
        }
        //2.根据pageName精确查询
        CmsPage cmsPage = cmsPageRepository.findByPageName(pageName);
        //3.根据查询后的数据返回结果
        if (!cmsPage.equals(null)) {
            return new CmsPageResult(CommonCode.SUCCESS, cmsPage);
        } else {
            return new CmsPageResult(CommonCode.FAIL, null);
        }
    }


    /**
     * 根据CmsPage的Id值修改CmsPage数据
     *
     * @param id      String  CmsPage的Id值
     * @param cmsPage 要修改的CmsPage数据
     * @return CmsPageResult Cms定义的规范响数据
     */
    public CmsPageResult update(String id, CmsPage cmsPage) {
        //1.判断id和cmsPage页面数据信息的合法性
        if (!StringUtils.isBlank(id) && !ObjectUtils.isEmpty(cmsPage)) {
            //2.根据id查询cmsPage信息
            CmsPageResult result = this.getById(id);
            //3.判断根据id查询后的数据
            if (result.isSuccess()) {
                //获取cmsPage对象
                CmsPage one = result.getCmsPage();
                //更新模板id
                one.setTemplateId(cmsPage.getTemplateId());
                //更新所属站点
                one.setSiteId(cmsPage.getSiteId());
                //更新页面别名
                one.setPageAlias(cmsPage.getPageAlias());
                //更新页面名称
                one.setPageName(cmsPage.getPageName());
                //更新访问路径
                one.setPageWebPath(cmsPage.getPageWebPath());
                //更新物理路径
                one.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
                //更新DataURL
                one.setDataUrl(cmsPage.getDataUrl());
                //4.执行更新
                CmsPage save = cmsPageRepository.save(one);
                //5.判断更新后的数据是否正确
                if (save != null) {
                    //6.返回成功结果
                    return new CmsPageResult(CommonCode.SUCCESS, save);
                }
            }
        }
        //7.返回修改失败信息
        return new CmsPageResult(CommonCode.FAIL, cmsPage);
    }


    /**
     * 根据CmsPage的Id删除CmsPage数据
     *
     * @param id String CmsPage的Id值
     * @return ResponseResult Api定义的通用规范响数据
     */
    public ResponseResult delete(String id) {
        //判断id是否合法
        if (!StringUtils.isBlank(id)) {
            //根据id获取数据并判断数据是否存在
            if (this.getById(id).isSuccess()) {
                //根据id删除
                cmsPageRepository.deleteById(id);
                //返回成功结果集
                return new ResponseResult(CommonCode.SUCCESS);
            }
        }
        //返回失败结果
        return new ResponseResult(CommonCode.FAIL);
    }




    /*页面静态化流程
    静态化程序获取DataUrl
    静态化程序远程请求DataUrl获取数据模型
    静态化程序获取页面的模板信息
    执行页面静态化*/


    public String getPageHtml(String pageId) {
        //获取页面模型数据
        Map model = this.getModelByPageId(pageId);
        if (model == null) {
            //获取页面模型数据为空
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        //获取页面模板
        String templateContent = this.getTemplateByPageId(pageId);
        if (StringUtils.isEmpty(templateContent)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        //执行静态化
        String html = generateHtml(templateContent, model);
        if (StringUtils.isBlank(html)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        return html;
    }

    //执行页面静态化
    private String generateHtml(String template, Map model) {
        try {
            //生成配置文件
            Configuration configuration = new Configuration(Configuration.getVersion());//Freemarker包里的类,注意别导错了
            //模板加载器
            StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
            stringTemplateLoader.putTemplate("template", template);
            //配置模板加载器
            configuration.setTemplateLoader(stringTemplateLoader);
            //获取模板
            Template template1 = configuration.getTemplate("template");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template1, model);
            return html;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //获取页面模板
    public String getTemplateByPageId(String pageId) {
        //查询页面信息
        CmsPage cmsPage = this.getById(pageId).getCmsPage();
        if (cmsPage==null){
            //页面不存在
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        //获取页面模板
        String templateId = cmsPage.getTemplateId();
        if (StringUtils.isBlank(templateId)){
            //模板为空
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(templateId);
        if (optional.isPresent()){
            CmsTemplate cmsTemplate = optional.get();
            //模板文件id
            String templateFileId = cmsTemplate.getTemplateFileId();
            //取出模板文件
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
            //打卡下载流对象
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            //创建GridFsResource
            GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);
            try {
                String content = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
                return content;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //获取页面模型数据
    public Map getModelByPageId(String pageId) {
        //查询页面信息
        CmsPage cmsPage = this.getById(pageId).getCmsPage();
        if (cmsPage == null) {
            //页面不存在
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        //取出dataUrl
        String dataUrl = cmsPage.getDataUrl();
        if (StringUtils.isBlank(dataUrl)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        ResponseEntity<Map> entity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = entity.getBody();

        return body;
    }






   /* //页面静态化
    public String getPageHtml(String pageId){
        //获取页面模型数据
        Map model = this.getModelByPageId(pageId);
        if(model == null){
            //获取页面模型数据为空
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        //获取页面模板
        String templateContent = getTemplateByPageId(pageId);
        if(StringUtils.isEmpty(templateContent)){
            //页面模板为空
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        //执行静态化
        String html = generateHtml(templateContent, model);
        if(StringUtils.isEmpty(html)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        return html;

    }
    //页面静态化
    public String generateHtml(String template,Map model){
        try {
            //生成配置类
            Configuration configuration = new Configuration(Configuration.getVersion());
            //模板加载器
            StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
            stringTemplateLoader.putTemplate("template",template);
            //配置模板加载器
            configuration.setTemplateLoader(stringTemplateLoader);
            //获取模板
            Template template1 = configuration.getTemplate("template");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template1, model);
            return html;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
    //获取页面模板
    public String getTemplateByPageId(String pageId){
        //查询页面信息
        CmsPageResult cmsPageResult = this.getById(pageId);
        CmsPage cmsPage = cmsPageResult.getCmsPage();
        if(cmsPage == null){
            //页面不存在
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        //页面模板
        String templateId = cmsPage.getTemplateId();
        if(StringUtils.isEmpty(templateId)){
            //页面模板为空
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(templateId);
        if(optional.isPresent()){
            CmsTemplate cmsTemplate = optional.get();
            //模板文件id
            String templateFileId = cmsTemplate.getTemplateFileId();
            //取出模板文件内容
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
            //打开下载流对象
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            //创建GridFsResource
            GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);
            try {
                String content = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
                return content;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }
    //获取页面模型数据
    public Map getModelByPageId(String pageId){
        //查询页面信息
        CmsPageResult cmsPageResult = this.getById(pageId);
        CmsPage cmsPage = cmsPageResult.getCmsPage();
        if(cmsPage == null){
            //页面不存在
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        //取出dataUrl
        String dataUrl = cmsPage.getDataUrl();
        if(StringUtils.isEmpty(dataUrl)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = forEntity.getBody();
        return body;

    }
*/
}
