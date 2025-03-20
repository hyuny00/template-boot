package sample.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.futechsoft.framework.common.constant.AuthConstant;
import com.futechsoft.framework.common.constant.ViewInfo;
import com.futechsoft.framework.common.controller.AbstractController;
import com.futechsoft.framework.common.page.Page;
import com.futechsoft.framework.common.page.Pageable;
import com.futechsoft.framework.excel.CellVo;
import com.futechsoft.framework.excel.ExcelColumn;
import com.futechsoft.framework.excel.LargeExcel;
import com.futechsoft.framework.util.FtMap;
import com.futechsoft.framework.util.SecurityUtil;

import sample.service.SampleService;

/**
 * 샘플을 관리하는 클래스임
 * @author futech001
 *
 */
@Controller
public class SampleController extends AbstractController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SampleController.class);

	@Resource(name = "sample.service.SampleService")
	SampleService sampleService;
	
	
	 
	/**
	 * 샘플목록을 조회한다
	 * @param pageble
	 * @param request
	 * @return 목록페이지 경로
	 * @throws Exception
	 */
	@RequestMapping("/sample/selectSampleList")
	public String selectSampleList(Pageable pageble, HttpServletRequest request, Model model) throws Exception {
		
		//  String key = "iamkey";
	      //  String value = "iamvalue";
	        
	        // redis에 set
	       // redisTemplate.opsForValue().set(key, value);
	        
	    //	LOGGER.debug(redisTemplate.opsForValue().get(key).toString());
	        
	        // redis에서 get
	        //RedisVO redisVO = (RedisVO) redisTemplate.opsForValue().get(key);
		
	//	  Address address = new Address("부천","원미구");
	     //   People people = new People(null, "진민", "최", address);

	        //when
	      //  People savePeople = peopleRedisRepository.save(people);

	        //then
	      //  Optional<People> findPeople = peopleRedisRepository.findById(savePeople.getId());
	      //  LOGGER.debug("AAAAAAAA..."+findPeople.get().getFirstName());
	        
	        

		if (SecurityUtil.hasAuth(AuthConstant.ROLE_ADMIN)) {
			LOGGER.debug("jjjjjjjjjjjjjjjjjjjjjjjj");
		}


		FtMap params = super.getFtMap(request);

		params.put("userNo", SecurityUtil.getUserNo());


		LOGGER.debug("jjjjjjjjjjjjjjjjjjjjjjjj.............."+pageble.getPageNo());

		Page<FtMap> page = sampleService.selectSampleList(pageble, params);


		/* 공통코드목록 가져오는 방법*/
		params.put("upCdSeq", 800);
		List<FtMap> codeList = getCommonService().selectCommonCodeList(params);
		request.setAttribute("codeList", codeList);

		/*공통코드 목록을 map형식으로 변환*/
		//FtMap codeMap = getCommonService().selectCommonCodeMap(params);
		FtMap etcCode = getCommonService().selectCommonCodeMap(codeList);
		LOGGER.debug(etcCode.toString());
		request.setAttribute("etcCode", etcCode);

		model.addAttribute("list", page.getList());
		model.addAttribute("pageable", page.getPageable());
		
		request.setAttribute("list", page.getList());
		request.setAttribute("pageable", page.getPageable());

		return "tiles:sample/sampleList";
	}

	/**
	 * 샘플폼으로 이동한다
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sample/sampleForm")
	public String sampleForm(HttpServletRequest request) throws Exception {

		return "tiles:sample/sampleForm";
	}

	/**
	 * 샘플을 저장한다
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sample/insertSample")
	public String insertSample(HttpServletRequest request) throws Exception {

		FtMap params = super.getFtMap(request);
		params.put("userNo", SecurityUtil.getUserNo());


		Enumeration<String> e = request.getParameterNames();
		while ( e.hasMoreElements() ){
			String name =  e.nextElement();
			String[] values = request.getParameterValues(name);
			for (String value : values) {
				System.out.println("name=" + name + ",value=" + value);
			}
		}


		sampleService.insertSample(params);

		request.setAttribute(ViewInfo.REDIRECT_URL, "/sample/selectSampleList");
		return ViewInfo.REDIRECT_PAGE;

	}

	/**
	 * 샘플단건을 조회한다
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/sample/selectSample")
	//@PostAuthorize("isAuthenticated() and ((returnObject.userId==principal.userId) or hasRole('ROLE_ADMIN'))")
		//@PostAuthorize("isAuthenticated() and (returnObject.getString('id') == 'SAMPLE-00001' or  hasRole('ROLE_ADMIN'))")
	//로그인하고 리턴값의 id가  'SAMPLE-00001' 이거나 권한이 ROLE_ADMIN인경우만 조회됨
//	@PostAuthorize("isAuthenticated() and (#model['result'].getString('id') == 'SAMPLE-00001'  or  hasRole('ROLE_ADMIN') )")
	public String selectSample(HttpServletRequest request, Model model) throws Exception {

		FtMap params = super.getFtMap(request);
		params.put("userNo", SecurityUtil.getUserNo());





		FtMap result = sampleService.selectSample(params);

		model.addAttribute("result", result);

		return "tiles:sample/sampleForm";
	}

	@RequestMapping("/sample/updateSample")
	public String updateSample(HttpServletRequest request) throws Exception {

		FtMap params = super.getFtMap(request);
		params.put("userNo", SecurityUtil.getUserNo());
		sampleService.updateSample(params);

		// 조회할 key값세팅. 여러개 세팅시: request.setAttribute("sendParams","id,name");, 검색조건,
		// 페이지번호등은 세팅할 필요없음.
		request.setAttribute("sendParams", "id");
		request.setAttribute(ViewInfo.REDIRECT_URL, "/sample/selectSample");
		return ViewInfo.REDIRECT_PAGE;
	}

	@RequestMapping("/sample/deleteSample")
	public String deleteSample(HttpServletRequest request) throws Exception {

		FtMap params = super.getFtMap(request);
		params.put("userNo", SecurityUtil.getUserNo());

		sampleService.deleteSample(params);

		request.setAttribute("message", "삭제되었습니다.");
		request.setAttribute(ViewInfo.REDIRECT_URL, "/sample/selectSampleList");
		return ViewInfo.REDIRECT_PAGE;

	}

	@RequestMapping(value = "/sample/samplePopup")
	public String samplePopup(HttpServletRequest request) throws Exception {

		FtMap params = super.getFtMap(request);
		LOGGER.debug("testId.." + params.getString("testId"));

		params.put("userNo", SecurityUtil.getUserNo());

		request.setAttribute("contents", "hello....");

		return "sample/samplePopup";
	}

	@RequestMapping(value = "/dashboard")
	public String getChatViewPage(ModelAndView mav) {
		return "tiles:dashboard/dashboard";
	}




	@RequestMapping(value = "/file/excelDown")
	public void excelDown(HttpServletRequest request, HttpServletResponse response) throws Exception {

		FtMap params = getFtMap(request);

		Pageable pageable = new Pageable();
		pageable.setPaged(false);
		Page<FtMap> page = sampleService.selectSampleList(pageable, params);

		String templateFilePath = getExceltemplatePath(request);
		File templateFile=new File(templateFilePath, "test_1.xlsx");


		//단순 엑셀다운로드
		//String[] columnValue= {"id", "name", "regUser"};
		//getExcelHelper().excelDownload(response, templateFile, page.getList(), columnValue);


		/*  포맷 , 코드 ,정렬 필요시*/
		params.put("upCdSeq", 800);
		FtMap codeMap = getCommonService().selectCommonCodeMap(params);
		ExcelColumn excelColumn  = new ExcelColumn(
													new CellVo(CellVo.CELL_STRING, "id", 			CellVo.ALIGN_RIGHT),
													new CellVo(CellVo.CELL_STRING, "attc_doc_id2", 	CellVo.ALIGN_RIGHT),
													new CellVo(CellVo.CELL_STRING, "etcCode",	codeMap, CellVo.ALIGN_LEFT)
							);
		getExcelHelper().excelDownload(response, templateFile, page.getList(), excelColumn);


	}

	@RequestMapping(value = "/file/largeExcelDown")
	public void largeExcelDown(HttpServletRequest request, HttpServletResponse response) throws Exception {

		FtMap params = getFtMap(request);
		 params.put("userNo", SecurityUtil.getUserNo());

		String templateFilePath = getExceltemplatePath(request);
		File templateFile=new File(templateFilePath, "test_1.xlsx");

		LargeExcel largeExcel = getExcelHelper().preparedLargeExcel(templateFile);

		//String[] columnValue= {"id", "name", "etcCode"};

		params.put("upCdSeq", 800);
		FtMap codeMap = getCommonService().selectCommonCodeMap(params);
		ExcelColumn excelColumn  = new ExcelColumn(
													new CellVo(CellVo.CELL_STRING, "id", 			CellVo.ALIGN_LEFT),
													new CellVo(CellVo.CELL_STRING, "attc_doc_id2", 	CellVo.ALIGN_RIGHT),
													new CellVo(CellVo.CELL_STRING, "etcCode",	codeMap, CellVo.ALIGN_RIGHT)
							);

		sampleService.selectExlSampleList(params, largeExcel.getSheet(), excelColumn, largeExcel);

		getExcelHelper().endLargeExcel( response,  largeExcel.getWorkbook(), templateFile.getName());

	}


	@RequestMapping(value = "/sample/selectCode")
	@ResponseBody
	public List<FtMap> selectCode(HttpServletRequest request) throws Exception {

		FtMap params = super.getFtMap(request);

		LOGGER.debug("id.." + params.getString("test"));

		params.put("userNo", SecurityUtil.getUserNo());

		List<FtMap> codeList = new ArrayList<FtMap>();
		FtMap ftmap = new FtMap();
		ftmap.put("id","0");
		ftmap.put("text","enhancement");
		codeList.add(ftmap);

		 ftmap = new FtMap();
		ftmap.put("id","1");
		ftmap.put("text","bug");
		codeList.add(ftmap);

		 ftmap = new FtMap();
		ftmap.put("id","2");
		ftmap.put("text","duplicate");
		codeList.add(ftmap);




		return codeList;

	}


	@RequestMapping(value = "/file/jexcelDown")
	public void jexcelDown(HttpServletRequest request, HttpServletResponse response) throws Exception {


		try ( InputStream io = new FileInputStream(new File(getExceltemplatePath(request), "test.xlsx"));
				OutputStream os = response.getOutputStream();) {

			List<FtMap> dataList = new ArrayList<FtMap>();
			FtMap ftMap= new FtMap();
			ftMap.put("seq", "1");
			ftMap.put("name", "122");
			ftMap.put("phone", "00-22-22");

			dataList.add(ftMap);


			 ftMap= new FtMap();
			ftMap.put("seq", "11");
			ftMap.put("name", "1221");
			ftMap.put("phone", "00-212-22");

			dataList.add(ftMap);

			Context context = new Context();
			context.putVar("dataList", dataList);

		//	response.setContentType("application/msexcel");
			response.setHeader("Set-Cookie", "fileDownload=true; path=/");
			response.setHeader("Content-Disposition", "attachment; filename=\"TestDown.xlsx\"");
			JxlsHelper.getInstance().processTemplate(io, os, context);
		} catch (Exception e) {
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			e.printStackTrace();
		}

	}
	
	
	
}



