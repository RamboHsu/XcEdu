package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created By Rambo on 2018/11/15
 */

public interface CmsConfigRepository extends MongoRepository<CmsConfig,String> {

}
