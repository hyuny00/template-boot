/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sample.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.futechsoft.framework.common.page.Page;
import com.futechsoft.framework.common.page.Pageable;
import com.futechsoft.framework.excel.CustomResultHandler;
import com.futechsoft.framework.excel.ExcelColumn;
import com.futechsoft.framework.excel.LargeExcel;
import com.futechsoft.framework.file.service.FileUploadService;
import com.futechsoft.framework.util.FtMap;

import sample.mapper.SampleMapper;


@Service("sample.service.SampleService")
public class SampleService {
	
		

	private static final Logger LOGGER = LoggerFactory.getLogger(SampleService.class);

	@Resource(name = "sample.mapper.SampleMapper")
	private SampleMapper sampleMapper;

	@Resource(name = "framework.file.service.FileUploadService")
	FileUploadService fileUploadService;



	/**
	 * 글목록을 조회한다
	 * @param pageable
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Page<FtMap> selectSampleList(Pageable pageable, FtMap params) throws Exception {

		List<FtMap> list = sampleMapper.selectSampleList(pageable, params);
		long count = sampleMapper.countSampleList(params);

		Page<FtMap> page = new Page<FtMap>(pageable, list, count);

		return page;
	}

	public void selectExlSampleList(FtMap params,  SXSSFSheet sheet,  ExcelColumn excelColumn,LargeExcel largeExcel) throws Exception {

		Pageable pageable = new Pageable();
		pageable.setPaged(false);
		sampleMapper.selectSampleList(pageable, params, new CustomResultHandler(sheet, excelColumn, largeExcel));

	}

	//@PostAuthorize("isAuthenticated() and ((returnObject.userId==authentication.principal.userId) or hasRole('ROLE_ADMIN'))")
	//@PostAuthorize("isAuthenticated() and (returnObject.getString('id') == 'SAMPLE-00001' or  hasRole('ROLE_ADMIN'))")
	//@PostAuthorize("principal.userId=='jinju'")
	public FtMap selectSample(FtMap params) throws Exception {
		return sampleMapper.selectSample(params);
	}

	@Transactional
	public void insertSample(FtMap params) throws Exception {

		//업무테이블명세팅
		params.put("tblNm", "tb_propodal_master");

		//썸네일 필요시
		params.put("isThumnail", true);
		params.put("thumnailWidth", 200);
		params.put("thumnailHeight", 200);

		//fileUploadService.saveFile(params, "AAAA");

		/*
		String docId=fileUploadService.saveFile(params, "AAAA","attcDocId");
		params.put("attcDocId", docId);
		String docId1=fileUploadService.saveFile(params, "AAAA","attcDocId2");
		params.put("attcDocId2", docId1);

*/

		List<String> tempDocIdList = fileUploadService.saveFiles(params, "AAAA","attcDocId");
		for(String  docInfo : tempDocIdList) {
			LOGGER.debug("mmmmmmm..."+docInfo);
		}
		params.put("attcDocId", tempDocIdList.get(0));
		params.put("attcDocId2", tempDocIdList.get(1));


		sampleMapper.insertSample(params);

	}

	@Transactional
	public void updateSample(FtMap params) throws Exception {

		//업무테이블명세팅
		params.put("tblNm", "tb_propodal_master");

		//썸네일 필요시
		params.put("isThumnail", true);
		params.put("thumnailWidth", 200);
		params.put("thumnailHeight", 200);

		fileUploadService.saveFile(params, "AAAA");

		/*
		String docId=fileUploadService.saveFile(params, "AAAA","attcDocId");
		params.put("attcDocId", docId);
		String docId1=fileUploadService.saveFile(params, "AAAA","attcDocId2");
		params.put("attcDocId2", docId1);



		List<String> tempDocIdList = fileUploadService.saveFiles(params, "AAAA","attcDocId2");
		for(String  docInfo : tempDocIdList) {
			LOGGER.debug("mmmmmmm..."+docInfo);
		}
		params.put("attcDocId", tempDocIdList.get(0));
		params.put("attcDocId2", tempDocIdList.get(1));
*/
		sampleMapper.updateSample(params);
	}

	public void deleteSample(FtMap params) throws Exception {
		sampleMapper.deleteSample(params);

	}

}
