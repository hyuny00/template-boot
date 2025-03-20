package com.futechsoft.framework.file.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.futechsoft.framework.common.sqlHelper.TableInfo;
import com.futechsoft.framework.exception.ErrorCode;
import com.futechsoft.framework.exception.FileUploadException;
import com.futechsoft.framework.exception.ZipParsingException;
import com.futechsoft.framework.file.mapper.FileMapper;
import com.futechsoft.framework.file.vo.FileInfoVo;
import com.futechsoft.framework.util.ConvertUtil;
import com.futechsoft.framework.util.FileUtil;
import com.futechsoft.framework.util.FtMap;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


//@PropertySource("classpath:globals.properties")

@Service("framework.file.service.FileUploadService")
public class FileUploadService {


	private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadService.class);


	@Resource(name = "framework.file.mapper.FileMapper")
	private FileMapper mapper;

	@Autowired
	PropertiesConfiguration propertiesConfiguration;

	/**
	 * 파일을 임시경로에 업로드
	 *
	 * @param multipartFile
	 * @param fileInfoVo
	 * @return
	 * @throws FileUploadException
	 */
	public FileInfoVo upload(MultipartFile multipartFile, FileInfoVo fileInfoVo) throws FileUploadException {
		String tempUploadPath = propertiesConfiguration.getString("file.uploadPath.temp");
		FileInfoVo fileObject = null;
		try {
			File f = new File(tempUploadPath);

			if (!f.exists()) f.mkdirs();

			fileObject = writeFile(multipartFile, fileInfoVo);
		} catch (Exception e) {
			e.printStackTrace();
			throw new FileUploadException(ErrorCode.FILE_UPLOAD_ERROR, e);
		}
		return fileObject;
	}

	private FileInfoVo writeFile(MultipartFile multipartFile, FileInfoVo fileInfoVo) throws Exception {
		String tempUploadPath = propertiesConfiguration.getString("file.uploadPath.temp");
		long totalChunks = fileInfoVo.getTotalChunks();
		long chunkIndex = fileInfoVo.getChunkIndex();

		String fileId = "";
		if (!fileInfoVo.getFileId().equals("")) {
			fileId = fileInfoVo.getFileId();
		} else {
			fileId = FileUtil.getRandomId();
		}

		String fileName = fileInfoVo.getFileNm();

		// 파일 정보
		String originFilename = fileName;

		String extName = "";
		if (originFilename.lastIndexOf(".") != -1) {
			extName = FileUtil.getFileExt(originFilename);
		}

		byte[] data = multipartFile.getBytes();

		File uploadFile = Paths.get(tempUploadPath,fileId + ".TEMP").toFile();

		FileOutputStream fos = new FileOutputStream(uploadFile, true);

		FileCopyUtils.copy(data, fos);

		fileInfoVo.setUploadComplete("N");
		fileInfoVo.setFileId(fileId);
		fileInfoVo.setFileNm(fileName);
		fileInfoVo.setTemp("Y");

		if (totalChunks == chunkIndex) {

			fileId = fileId.substring(fileId.lastIndexOf("\\") + 1);

			if (uploadFile.length() != fileInfoVo.getFileSize()) {
				throw new IOException();
			}

			fileInfoVo.setFileSize(uploadFile.length());
			fileInfoVo.setFileExt(extName);
			fileInfoVo.setFileId(fileId);
			fileInfoVo.setTempFilePath(Paths.get(tempUploadPath, fileId + ".TEMP").toString());
			fileInfoVo.setFileNm(fileName);
			fileInfoVo.setUploadComplete("Y");
			fileInfoVo.setTemp("Y");

		}

		return fileInfoVo;
	}




	@Transactional
	public void saveFile(FtMap paramMap, String saveFilePath) throws Exception {
		save( paramMap,  saveFilePath,  "");
	}

	@Transactional
	public String saveFile(FtMap paramMap, String saveFilePath, String findDocId) throws Exception {
		List<String> tempDocIdList = save( paramMap,  saveFilePath, findDocId);

		LOGGER.debug("tempDocIdList......"+tempDocIdList);
		return  (tempDocIdList!=null && tempDocIdList.size()==1)? tempDocIdList.get(0) : "";
	}

	@Transactional
	public List<String> saveFiles(FtMap paramMap, String saveFilePath, String findDocId) throws Exception {
		return  save( paramMap,  saveFilePath, findDocId);
	}


	private List<String> save(FtMap paramMap, String saveFilePath, String findDocId) throws Exception {

		List<String> tempDocIdList = new ArrayList<String>();

		try {

				for (String value : ((String[]) paramMap.get("fileInfoList"))) {


					if (StringUtils.isEmpty(value)) continue;

					JsonObject jsonObject = JsonParser.parseString(value).getAsJsonObject();
					String refDocId = jsonObject.get("refDocId").getAsString();

					if (!findDocId.equals("")) {
						if (!refDocId.equals(findDocId))  continue;
					}

					if (refDocId.equals("NONE")) continue;

					String docId= jsonObject.get("docId").getAsString();


					if(StringUtils.isEmpty(docId)) {
						docId = FileUtil.getRandomId();
					}
					if (findDocId.equals("")) {
						paramMap.put(refDocId, docId);
					}

					saveProcess(jsonObject, paramMap, saveFilePath, findDocId, docId);
					tempDocIdList.add(docId);
				}



		} catch (IOException e) {
			throw new FileUploadException(ErrorCode.FILE_SAVE_ERROR, e);
		}

		return  tempDocIdList;
	}



	private void saveProcess(JsonObject jsonObject, FtMap paramMap, String saveFilePath, String findDocId, String docId) throws Exception {

		String tempUploadPath = propertiesConfiguration.getString("file.uploadPath.temp");
		String realUploadPath = propertiesConfiguration.getString("file.uploadPath.real");

		saveFilePath = Paths.get(saveFilePath, FileUtil.getSaveFilePath() ).toString();
				
		Gson gson = new Gson();

		FileInfoVo[] fileInfoVos = gson.fromJson(jsonObject.get("fileInfo").getAsJsonArray(), FileInfoVo[].class);


		String refDocId = gson.fromJson(jsonObject.get("refDocId").getAsString(), String.class);

		if (refDocId.equals("NONE")) return;

		if (!findDocId.equals("")) {
			if (!refDocId.equals(findDocId)) return;
		}

		List<FtMap> columnList = mapper.getColumnList(TableInfo.FILE_TABLE);

		int fileOrd = 0;
		for (FileInfoVo fileInfoVo : fileInfoVos) {

			fileInfoVo.setFileOrd(fileOrd++);


			if (StringUtils.defaultString(fileInfoVo.getTemp()).equals("Y")) {
				File file = Paths.get(tempUploadPath, fileInfoVo.getFileId() + ".TEMP").toFile();
				File fileToMove = Paths.get(realUploadPath, saveFilePath, fileInfoVo.getFileId() + ".FILE").toFile();


				FileUtils.moveFile(file, fileToMove);


				fileInfoVo.setDocId(docId);
				fileInfoVo.setFilePath(saveFilePath);
				fileInfoVo.setTblColNm(FtMap.getSnakeCase(refDocId));


				if(paramMap.getString("tblNm").equals("")) {
					LOGGER.debug("테이블 명을 입력하지 않았습니다.");
					throw new Exception();
				}

				fileInfoVo.setTblNm(paramMap.getString("tblNm"));

				if(paramMap.getString("openYn").equals("N")) {
					fileInfoVo.setFileAuth("rwr---");
				}else {
					fileInfoVo.setFileAuth("rwr-r-");
				}

				Map<String, Object> map = ConvertUtil.beanToMap(fileInfoVo);
				FtMap param = new FtMap();
				param.setFtMap(map);

				mapper.insertFileInfo(param);

				if(StringUtils.defaultString(fileInfoVo.getThumbnailYn()).equals("Y")) {
					
					FileUtil.createThumnail(realUploadPath, paramMap.getInt("thumnailWidth"), paramMap.getInt("thumnailHeight"), fileInfoVo);

					String ext= FileUtil.getFileExt(fileInfoVo.getFileNm()) ;
					File thumnail = Paths.get(realUploadPath, saveFilePath, "thumbnail",  fileInfoVo.getFileId() + "."+ext).toFile();

					String thumnailPath= Paths.get(saveFilePath,"thumbnail").toString();

					long fileSize=thumnail.length();

					fileInfoVo.setFilePath(thumnailPath);
					fileInfoVo.setFileSize(fileSize);

					mapper.updateThumbNnailFileInfo(fileInfoVo);
					
				}


			} else {
				FtMap param = new FtMap();
				param.put("fileId", fileInfoVo.getFileId());
				param.put("fileOrd", fileInfoVo.getFileOrd());
				mapper.updateFileOrd(param);
			}

			/*
			 * if(!(StringUtils.defaultString(fileInfoVo.getTemp()).equals("Y")) &&
			 * fileInfoVo.getDelYn().equals("Y")) {
			 *
			 * paramMap.put("fileId", fileInfoVo.getFileId()); fileInfoVo =
			 * getFileInfo(paramMap);
			 *
			 * File delFile = new
			 * File(realUploadPath+File.separator+fileInfoVo.getFilePath()+File.separator+
			 * fileInfoVo.getFileId()+".FILE");
			 *
			 *
			 *
			 * //DB삭제 //if(check) { FtMap params = new FtMap(); params.put("file_id",
			 * fileInfoVo.getFileId()); deleteFile(params); //}
			 *
			 * //실제파일삭제 -> 휴지통이동 boolean check = delFile.delete();
			 *
			 * }
			 */
		}
	}

	public void deleteFile(FtMap params) throws Exception {
		mapper.deleteFile(params);
	}

	public void createZip(String src, String target) throws ZipParsingException {
		Path srcDir = Paths.get(src);

		File targetDir = new File(target).getParentFile();
		boolean check = false;

		if (!targetDir.exists() && targetDir.toString().indexOf(src) != -1) {
			check = targetDir.mkdirs();
			if (!check) throw new ZipParsingException("디렉토리 생성 실패");
		}

		File zipFileName = Paths.get(target).toFile();

		try (ZipOutputStream zipStream = new ZipOutputStream(new FileOutputStream(zipFileName))) {

			DirectoryStream<Path> dirStream = Files.newDirectoryStream(srcDir);
			for (Path path : dirStream) {
				addToZipFile(path, zipStream);
			}
			dirStream.close();

		} catch (IOException | ZipParsingException e) {
			throw new ZipParsingException(e.getMessage());
		}
	}

	public void createZip(FileInfoVo[] fileInfoVos, String target) throws Exception {
		String realUploadPath = propertiesConfiguration.getString("file.uploadPath.real");

		File targetDir = new File(target).getParentFile();
		boolean check = false;

		if (!targetDir.exists()) {
			check = targetDir.mkdirs();
			if (!check) throw new ZipParsingException("디렉토리 생성 실패");
		}

		File zipFileName = Paths.get(target).toFile();

		try (ZipOutputStream zipStream = new ZipOutputStream(new FileOutputStream(zipFileName))) {

			for (FileInfoVo fileInfoVo : fileInfoVos) {

				//파일다운로드 권한
				boolean fileDownloadCheck =FileUtil.hasFileDownloadAuth(fileInfoVo);
				if(fileDownloadCheck) {
					File file = Paths.get(realUploadPath, fileInfoVo.getFilePath(), fileInfoVo.getFileId()+".FILE").toFile();
					addToZipFile(file, fileInfoVo.getFileNm(), zipStream);
				}

			}

		} catch (IOException | ZipParsingException e) {
			e.printStackTrace();
			throw new ZipParsingException(e.getMessage());
		}
	}

	private void addToZipFile(Path file, ZipOutputStream zipStream) throws ZipParsingException {

		if (file.toFile().isDirectory()) return;
		String inputFileName = file.toFile().getPath();

		try (FileInputStream inputStream = new FileInputStream(inputFileName)) {

			ZipEntry entry = new ZipEntry(file.toFile().getName());
			entry.setCreationTime(FileTime.fromMillis(file.toFile().lastModified()));
			entry.setComment("");
			zipStream.putNextEntry(entry);

			byte[] readBuffer = new byte[2048];
			int amountRead;

			while ((amountRead = inputStream.read(readBuffer)) > 0) {
				zipStream.write(readBuffer, 0, amountRead);
			}

		} catch (IOException e) {
			throw new ZipParsingException("Unable to process " + inputFileName, e);
		}
	}

	private void addToZipFile(File file, String fileNm, ZipOutputStream zipStream) throws ZipParsingException {

		if (file.isDirectory())
			return;

		try (FileInputStream inputStream = new FileInputStream(file)) {

			ZipEntry entry = new ZipEntry(fileNm);

			// entry.setCreationTime(FileTime.fromMillis(file.lastModified()));
			entry.setComment("");
			zipStream.putNextEntry(entry);

			byte[] readBuffer = new byte[2048];
			int amountRead;

			while ((amountRead = inputStream.read(readBuffer)) > 0) {
				zipStream.write(readBuffer, 0, amountRead);
			}

		} catch (IOException e) {
			throw new ZipParsingException("Unable to process " + fileNm, e);
		}
	}

	public List<FtMap> selectFileList(String docId) throws Exception {
		FtMap t = new FtMap();
		t.put("docId", docId);

		return mapper.selectFileList(t);
	}

	public FileInfoVo getFileInfo(FtMap params) throws Exception {

		return mapper.selectFileInfo(params);
	}
	
	@Transactional
	public String newMergeDocId(List<String> tempDocIdList) throws Exception {

		String mergeDocId = "";
		if(tempDocIdList!= null && tempDocIdList.size()>0) {
			mergeDocId=tempDocIdList.get(0);
		}

		if(StringUtils.defaultString(mergeDocId).equals("")) {
			mergeDocId = FileUtil.getRandomId();
		}

		for(String tempDocId: tempDocIdList) {

			FtMap params = new FtMap();
			params.put("mergeDocId", mergeDocId);
			params.put("tempDocId", tempDocId);

			mapper.updateMergeDocId(params);
		}
		return mergeDocId;
	}
}
