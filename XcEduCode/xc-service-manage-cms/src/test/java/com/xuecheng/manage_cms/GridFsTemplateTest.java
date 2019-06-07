package com.xuecheng.manage_cms;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created By Rambo on 2018/11/18
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class GridFsTemplateTest {

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    GridFSBucket gridFSBucket;


    /**
     * 存文件
     *
     * @throws Exception
     */
    @Test
    public void testGridFs() throws Exception {
        //要存储的文件
        File file = new File("D:/XcEdu/index_banner.ftl");
        //定义输出流
        FileInputStream fileInputStream = new FileInputStream(file);
        //向GridFs中存储文件
        ObjectId objectId = gridFsTemplate.store(fileInputStream, "index_banner.ftl");
        //得到文件id
        String s = objectId.toString();
        System.out.println(s);

    }

    /**
     * 获取文件
     * @throws Exception
     */
    @Test
    public void testQueryFile() throws Exception {
        String fileId = "5bf13070112a4a22f4f9a9df";
        //根据id查询文件
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
        //打开下载流对象
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        //创建gridFsResource，用于获取流对象
        GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);
        //获取流中的数据
        String s = IOUtils.toString(gridFsResource.getInputStream(), "UTF-8");
        System.out.println(s);

    }


    /**
     * 删除文件
     */
    @Test
    public void testDelFile(){
        //根据文件id删除fs.files和fs.chunks中的记录
        gridFsTemplate.delete(Query.query(Criteria.where("_id").is("5bf13070112a4a22f4f9a9df")));
    }
}
