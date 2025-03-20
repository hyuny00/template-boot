package com.futechsoft.framework.common.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import com.futechsoft.framework.common.service.CommonService;
import com.futechsoft.framework.excel.ExcelHelper;
import com.futechsoft.framework.util.FtMap;

public abstract class AbstractController {



	@Resource(name = "framework.common.service.CommonService")
	private CommonService commonService;

	@Autowired
	private ExcelHelper excelHelper;

	@Autowired
	PropertiesConfiguration propertiesConfiguration;

	protected FtMap getFtMap(HttpServletRequest request) {
		return new FtMap(request.getParameterMap());
	}

	protected CommonService getCommonService() {
		return commonService;
	}

	protected ExcelHelper getExcelHelper() {
		return excelHelper;
	}


	protected String getExceltemplatePath(HttpServletRequest request) {

		String serviceType = propertiesConfiguration.getString("service.type");

		String templatePath = propertiesConfiguration.getString("excel.template.path.real");

		if(serviceType.equals("dev")) {
			templatePath = propertiesConfiguration.getString("excel.template.path.dev");
		}



		return  templatePath;
	}


}
