package com.futechsoft.framework.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.futechsoft.framework.annotation.Mapper;
import com.futechsoft.framework.util.FtMap;

@Mapper("framework.common.mapper.CommonMapper")
public interface CommonMapper extends GenericMapper<FtMap> {

	List<FtMap> selectCommonCodeList(FtMap params) throws Exception;

	void insertUserAuth(FtMap params) throws Exception;
	void deleteUserAuth(FtMap params) throws Exception;
	
	
	
	void deleteCache(@Param("cacheName") String cacheName) throws Exception;
	void insertCache(@Param("cacheName") String cacheName) throws Exception;


}
