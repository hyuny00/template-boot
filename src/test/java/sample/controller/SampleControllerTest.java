package sample.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import com.futechsoft.TemplateApplication;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ContextConfiguration(classes = TemplateApplication.class)
public class SampleControllerTest {
	 @Autowired
	 private MockMvc mockMvc;
	 
	
	 
	  @Autowired
	    private WebApplicationContext webApplicationContext;


	    @Before
	    public void setUp() {
	        mockMvc = MockMvcBuilders
	                .webAppContextSetup(webApplicationContext)
	                .apply(springSecurity())
	                .build();
	    }
	    
	 
	 @Test
	 @WithMockUser(roles="USER")
	// @WithMockUser(username = "user1", password = "pwd", roles = "USER")
	 //@WithUserDetails("admin")
	 public void getAllTest() throws Exception {
		 
		 LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
				 requestParams.add("id", "1");
				 requestParams.add("name", "john");
				 requestParams.add("age", "30");
				 
				 
	      //  mockMvc.perform(get("/sample/testId").params(requestParams))
	        		
	            //   .andExpect(status().isOk())
	              // .andDo(print());
	        
	        mockMvc.perform(MockMvcRequestBuilders.get("/sample/selectSampleList").accept(MediaType.TEXT_HTML))
	         .andExpect(MockMvcResultMatchers.status().isOk())
	         .andExpect(model().attribute("pageable",hasProperty("pageNo", is(1))))
	        		
	         .andDo(MockMvcResultHandlers.print()); 
 
	    }


	 
}
