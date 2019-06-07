package com.xuecheng.framework.model.response;

import lombok.Data;
import lombok.ToString;

/**
 * @author Rambo
 */
@Data
@ToString
public class QueryResponseResult extends ResponseResult {

    private QueryResult queryResult;

    public QueryResponseResult(ResultCode resultCode,QueryResult queryResult){
        super(resultCode);
       this.queryResult = queryResult;
    }


}
