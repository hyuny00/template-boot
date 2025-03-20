
package sample.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.ResultHandler;

import com.futechsoft.framework.annotation.Mapper;
import com.futechsoft.framework.common.page.Pageable;
import com.futechsoft.framework.util.FtMap;




@Mapper("sample.mapper.SampleMapper")
public interface SampleMapper {



	/**
	 * 글 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 글 목록
	 * @exception Exception
	 */
	List<FtMap> selectSampleList(@Param("pageable")Pageable pageable, @Param("params")FtMap params) throws Exception;


	long countSampleList(@Param("params")FtMap params) throws Exception;


	FtMap selectSample(FtMap params) throws Exception;
	void insertSample(FtMap params) throws Exception;
	void deleteSample(FtMap params) throws Exception;
	void updateSample(FtMap params) throws Exception;


	public void selectSampleList(@Param("pageable")Pageable pageable, @Param("params")FtMap params, ResultHandler<FtMap> customResultHandler);

}
