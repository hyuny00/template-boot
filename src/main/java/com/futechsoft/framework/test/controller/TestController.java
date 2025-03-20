package com.futechsoft.framework.test.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.futechsoft.framework.exception.ErrorCode;
import com.futechsoft.framework.exception.FileUploadException;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

@Controller
public class TestController {



	//@Value("${flt.output}")
	private String outputPath;

	 @RequestMapping("/genXml")
	 public String genXml() throws IOException{

		 // Data Model 만들기
        Map<String, Object> root = new HashMap<>();
        root.put("key1", "value1");
        Map<String, Object> key2 = new HashMap<>();
        key2.put("key3", "value3");
        key2.put("key4", "value4");
        root.put("key2", key2);

        // Template Loader
        Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        URL fileUrl  = this.getClass().getClassLoader().getResource("ftl");

        FileTemplateLoader loader = new FileTemplateLoader(new File(fileUrl.getFile()));
        cfg.setTemplateLoader(loader);

        Template template = cfg.getTemplate("template.ftl");

		try (Writer file = new OutputStreamWriter(new FileOutputStream(new File(outputPath+File.separator+"result.txt")), StandardCharsets.UTF_8);){
			template.process(root, file);
			file.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}


		 return "tiles:main";
	 }

	// @PostMapping(value="/proxy/**", method = {RequestMethod.GET, RequestMethod.POST})
	 @RequestMapping(value="/proxy/**" , method = {RequestMethod.GET, RequestMethod.POST})
	    public ResponseEntity<byte[]>  proxy(HttpServletRequest request, HttpServletResponse response, @RequestBody(required = false) byte[] body) throws IOException, URISyntaxException {

	     HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
	        httpRequestFactory.setConnectionRequestTimeout(360000); //6m
	        httpRequestFactory.setConnectTimeout(360000); //6m
	        httpRequestFactory.setReadTimeout(360000); //6m

	     // restTempate tobe bean
	     RestTemplate restTemplate = new RestTemplate(httpRequestFactory);

	     // url
	     String originReqURL = request.getRequestURI().replaceAll("^/proxy", "");
	     String originQueryString = request.getQueryString();
	     String urlStr = "https://www.osmb.go.kr/main/main.jsp" + originReqURL + (StringUtils.isEmpty(originQueryString) ? "" : "?"+originQueryString);


	     System.out.println("urlStr........"+urlStr);

	     URI url = new URI(urlStr);

	     // method
	    // String originMethod = request.getHeader("x-origin-method");
	     HttpMethod method = HttpMethod.valueOf("POST");


	     // header
	     Enumeration<String> headerNames = request.getHeaderNames();
	     MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
	     while(headerNames.hasMoreElements()) {
	      String headerName = headerNames.nextElement();
	      String headerValue = request.getHeader(headerName);

	      headers.add(headerName, headerValue);
	     }



	     	// http entity (body, header)
	     	HttpEntity<byte[]> httpEntity = new HttpEntity<>(body, headers);

	     	return restTemplate.exchange(url, method, httpEntity, byte[].class);
	    }



	    @RequestMapping("/createPdf")
	    @ResponseBody
		public Map<String, String> createPdf(@RequestParam String image) throws Exception {


	    	System.out.println("image........"+image);


	    	image=  image.replaceAll("data:image/png;base64,", "");
	    	System.out.println("image2........"+image);


	        byte[] decodedBytes = Base64.getDecoder().decode(image.toString().getBytes("UTF-8"));

	        File imgFile= new File("E://sss.png");
	        FileUtils.writeByteArrayToFile(imgFile, decodedBytes);


	        BufferedImage awtImage = ImageIO.read(imgFile);
	        System.out.println("awtImage.getWidth() ..."+awtImage.getWidth() );
	        System.out.println("awtImage.getHeight() ..."+awtImage.getHeight() );




	        String imagePath = "E://sss.png";
	        String pdfPath = "E://sss.pdf";

	        if (!pdfPath.endsWith(".pdf"))
	        {
	            System.err.println("Last argument must be the destination .pdf file");
	            System.exit(1);
	        }

	        PDDocument doc = new PDDocument();
	        try
	        {

	            PDPage page = new PDPage(PDRectangle.A4);
	            doc.addPage(page);

	            PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath, doc);
	            PDPageContentStream contents = new PDPageContentStream(doc, page);

	            contents.drawImage(pdImage, 0, 0, PDRectangle.A4.getWidth(), PDRectangle.A4.getHeight());
	            contents.close();


	            doc.save(pdfPath);


	        }
	        finally
	        {
	            doc.close();
	            System.out.println("");
	            System.out.println("fin");
	        }




	    	Map<String, String> fileInfo = new HashMap<String, String>();
			 fileInfo.put("fileInfo", "Dfd" );
			 return fileInfo;
		}



	    @RequestMapping("/createPdf1")
	    @ResponseBody
		public Map<String, String> createPdf1(@RequestParam MultipartFile pdf) throws Exception {


			try {

				File f = new File("E://dd1.pdf");

				byte[] data = pdf.getBytes();
				FileOutputStream fos = new FileOutputStream(f);
				FileCopyUtils.copy(data, fos);


			}catch (Exception e) {
				e.printStackTrace();
				throw new FileUploadException(ErrorCode.FILE_UPLOAD_ERROR, e);
			}


	    	Map<String, String> fileInfo = new HashMap<String, String>();
			 fileInfo.put("fileInfo", "Dfd" );
			 return fileInfo;
		}

	    public void test() throws IOException {


	    	File file = new File("E:/aaa.pdf");

	    	PDDocument document = PDDocument.load(file);
	    	int pageCount = document.getNumberOfPages();

	    	System.out.println("pageCount..."+pageCount);

	    	PDFRenderer pdfRenderer = new PDFRenderer(document);


			 BufferedImage imageObj = pdfRenderer.renderImageWithDPI(0, 100, ImageType.RGB);
			 File outputfile = new File("E:/test.jpg");
			 ImageIO.write(imageObj, "jpg", outputfile);

			 System.out.println("end");



	    }


	    public  static void main(String args[]) throws IOException {
	    	new TestController().test();
	    }

}





