package com.futechsoft.framework.common.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.futechsoft.framework.util.FtMap;
import com.futechsoft.framework.util.SecurityUtil;

@Controller
public class ViewController  extends AbstractController {

	@RequestMapping("/")
	public String main() throws Exception {
		return "tiles:main";
		//return "main";
	}

	@RequestMapping(value = "/common/view")
	public String samplePopup(HttpServletRequest request) throws Exception {

		FtMap params = super.getFtMap(request);
		params.put("userNo", SecurityUtil.getUserNo());

		return params.getString("view");
	}

}
