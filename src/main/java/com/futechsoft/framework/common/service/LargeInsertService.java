package com.futechsoft.framework.common.service;

import javax.annotation.Resource;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.futechsoft.framework.common.page.Pageable;
import com.futechsoft.framework.util.FtMap;

import sample.mapper.SampleMapper;

@Service("framework.common.service.LargeInsertService")
public class LargeInsertService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommonService.class);


	@Resource(name = "sample.mapper.SampleMapper")
	private SampleMapper sampleMapper;

	public void selectHandler(FtMap params) throws Exception {


		ResultHandler<FtMap> rsltHndlr = new ResultHandler<FtMap>(){
            public void handleResult(ResultContext<? extends FtMap> context) {
            	// 처리 로직
            	FtMap ftMap = context.getResultObject();
            	// LOGGER.info("DEBUG"  + test);

            	// 삽입 처리
            	try{
            		sampleMapper.insertSample(ftMap);
            	} catch (Exception e){
                	LOGGER.error("ResultHandler testVO 삽입 처리 실패 ");
            	}
			}
		};
		Pageable pageable = new Pageable();
		pageable.setPaged(false);

		sampleMapper.selectSampleList(pageable, params, rsltHndlr);
	}
}


