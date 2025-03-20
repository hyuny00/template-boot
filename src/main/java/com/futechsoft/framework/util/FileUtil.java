package com.futechsoft.framework.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Paths;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import com.futechsoft.framework.exception.ErrorCode;
import com.futechsoft.framework.file.vo.FileInfoVo;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

public class FileUtil {


	private static String USER="user";
	private static String GROUP="group";
	private static String OTHER="other";

	private static String READ_AUTH="r";
	private static String WRITE_AUTH="w";

	public static String getSaveFilePath() throws Exception {
		return CommonUtil.getToday("yyyyMM");
	}

	public static String getRandomId() throws Exception {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public static String getFileExt(String originFilename) throws Exception {
		//return originFilename.substring(originFilename.lastIndexOf("."), originFilename.length());
		return FilenameUtils.getExtension(originFilename);
	}



	public static String getMigrationFileExt(String fileId, String fileNm) {

		String fileExt=".FILE";
		if(fileId.indexOf("-")!=-1) {
			fileExt=FilenameUtils.getExtension(fileNm);
		}

		return fileExt;
	}


	public static String getMigrationFileId(String fileId) {

		String fileIdDiv=".";

		if(fileId.indexOf(fileIdDiv)!=-1) {
			fileId=fileId.substring(fileId.indexOf(fileIdDiv)+1);
		}
		return fileId;
	}

	/**
	 * 파일삭제권한
	 * @param fileInfoVo
	 * @param realFileInfoVo
	 * @return
	 * @throws Exception
	 */
	public static boolean hasFileDeleteAuth(FileInfoVo fileInfoVo, FileInfoVo realFileInfoVo) throws Exception {

		boolean deleteFileCheck=true;

		if(!SecurityUtil.hasAuth("ROLE_ADMIN")) {
			if(!StringUtils.defaultString(SecurityUtil.getUserNo()).equals(realFileInfoVo.getRgstpNo())){
				String fileAuth=realFileInfoVo.getFileAuth();
				if(fileAuth!=null && fileAuth.length()==6) {
					String writeAuth =  FileUtil.getFileAuthCode( fileAuth, FileUtil.WRITE_AUTH, FileUtil.OTHER);
					if(!writeAuth.equals(FileUtil.WRITE_AUTH)) {
						fileInfoVo.setFileMsg(ErrorCode.FILE_DELETE_DENIED.getCode());
						fileInfoVo.setDelYn("N");
						deleteFileCheck=false;
					}
				}
			}
		}

		return deleteFileCheck;
	}


	/**
	 * 파일다운로드 권한
	 * @param fileInfoVo
	 * @return
	 * @throws Exception
	 */
	public static boolean hasFileDownloadAuth(FileInfoVo fileInfoVo) throws Exception {

		boolean fileDownloadCheck=true;

		if(!SecurityUtil.hasAuth("ROLE_ADMIN")) {
			if(!StringUtils.defaultString(SecurityUtil.getUserNo()).equals(fileInfoVo.getRgstpNo())){
				String fileAuth=fileInfoVo.getFileAuth();
				if(fileAuth!=null && fileAuth.length()==6) {
					String readAuth =  FileUtil.getFileAuthCode( fileAuth, FileUtil.READ_AUTH, FileUtil.OTHER);
					if(!readAuth.equals( FileUtil.READ_AUTH)) {
						fileDownloadCheck = false;
					}
				}
			}
		}

		return fileDownloadCheck;
	}

	public static String getFileAuthCode(String fileAuth, String auth, String user) {

		String code="";
		if(user.equals(FileUtil.USER)) {
			if(auth.equals(FileUtil.READ_AUTH)) {
				code = fileAuth.substring(0, 1);
			}else if(auth.equals(FileUtil.WRITE_AUTH)) {
				code = fileAuth.substring(1, 2);
			}
		}

		if(user.equals(FileUtil.GROUP)) {
			if(auth.equals(FileUtil.READ_AUTH)) {
				code = fileAuth.substring(2, 3);
			}else if(auth.equals(FileUtil.WRITE_AUTH)) {
				code = fileAuth.substring(3, 4);
			}
		}

		if(user.equals(FileUtil.OTHER)) {
			if(auth.equals(FileUtil.READ_AUTH)) {
				code = fileAuth.substring(4, 5);
			}else if(auth.equals(FileUtil.WRITE_AUTH)) {
				code = fileAuth.substring(5, 6);
			}
		}

		return code;

	}

	public static String getFileAuthCode(String fileAuth, String user) {

		String code="";
		if(user.equals(FileUtil.USER)) {
			code = fileAuth.substring(0, 2);
		}
		if(user.equals(FileUtil.GROUP)) {
			code = fileAuth.substring(2, 4);
		}
		if(user.equals(FileUtil.OTHER)) {
			code = fileAuth.substring(4, 6);
		}
		return code;
	}

	public static String setFileAuthCode(String fileAuth, String auth, String user) {

		if(fileAuth.length()==6 && auth.length()==2) {
			if(user.equals(FileUtil.USER)) {
				fileAuth = auth+ getFileAuthCode(fileAuth, FileUtil.GROUP) + getFileAuthCode(fileAuth, FileUtil.OTHER);
			}
			if(user.equals(FileUtil.GROUP)) {
				fileAuth = getFileAuthCode(fileAuth, FileUtil.USER)+ auth + getFileAuthCode(fileAuth, FileUtil.OTHER);
			}
			if(user.equals(FileUtil.OTHER)) {
				fileAuth = getFileAuthCode(fileAuth, FileUtil.USER)+ getFileAuthCode(fileAuth, FileUtil.GROUP) + auth;
			}
		}

		return fileAuth;

	}

	public static void createThumnail(String realUploadPath, int width, int height, FileInfoVo fileInfoVo) throws Exception {


		File originalFile = Paths.get(realUploadPath,fileInfoVo.getFilePath(), fileInfoVo.getFileId()+".FILE").toFile();


		BufferedImage originalImage = ImageIO.read(originalFile);

		if(originalImage == null) return;


		File destinationDir = Paths.get(realUploadPath, fileInfoVo.getFilePath() , "thumbnail").toFile();
		if(!destinationDir.exists()) {
			destinationDir.mkdirs();
		}

		if(originalImage.getWidth()<width) {
			width=originalImage.getWidth();
		}

		if(originalImage.getHeight()<height) {
			height=originalImage.getHeight();
		}


         File file =  Paths.get(realUploadPath,fileInfoVo.getFilePath(), "thumbnail",fileInfoVo.getFileId()+"."+fileInfoVo.getFileExt()).toFile();


         BufferedImage thumbnail = Thumbnails.of(originalImage).sourceRegion(Positions.CENTER, width, height).size(width, height).asBufferedImage();

         ImageIO.write(thumbnail, fileInfoVo.getFileExt(), file);

         if(originalFile!=null && originalFile.isFile()) {
        	 originalFile.delete();
         }


	}
}
