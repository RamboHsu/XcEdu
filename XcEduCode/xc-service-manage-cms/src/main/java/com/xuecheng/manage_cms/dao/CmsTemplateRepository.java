package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created By Rambo on 2018/11/19
 */
public interface CmsTemplateRepository extends MongoRepository<CmsTemplate,String> {
}
