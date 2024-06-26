package kr.co.hivesys.comm.file;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.hivesys.comm.file.service.FileService;
import kr.co.hivesys.comm.file.vo.FileVo;

@Component("fileLogic")
public class FileUploadSave implements ApplicationContextAware {

	private WebApplicationContext context = null;
	public static final Logger logger = LoggerFactory.getLogger(FileUploadSave.class);

	@Resource(name = "fileService")
	private FileService fileService;
	
	//윈도우용
	//private String firstPath="C:\\";
	//리눅스용
	private String firstPath="/usr/local/tomcat/share_data/";
	/**
	 * 업로드할 파일의 상세 정보 얻어옴
	 * 
	 * @param files : 실제 단일 파일 또는 파일 리스트
	 * @param fvo   : 받아온 filevo
	 * @return 업로드 개수
	 * @throws Exception
	 * @throws IllegalStateException
	 * @throws IOException
	 */

	// 여러 개의 파일 업로드 + 디비저장
	// (insert or update)

	public int fileUploadMultiple(List<MultipartFile> files, FileVo fvo) {
		List<FileVo> fileList = new ArrayList<>();
		List<FileVo> exList = new ArrayList<>();
		int i = 0;
		try {
			//수정 0415 1619
			logger.debug("■■■■■■■■■■■■■■■■■■■■■■■■■ firstPath ■■■■■■■■■■■■■■■■■■■■■■■■■ : "+firstPath);
			logger.debug("■■■■■■■■■■■■■■■■■■■■■■■■■ fvo.getFILE_DIR() ■■■■■■■■■■■■■■■■■■■■■■■■■ : "+fvo.getFILE_DIR());
			
			// 1. 파일 저장 경로 설정 : 실제 서비스되는 위치(프로젝트 외부에 저장)
			String inputPath = firstPath + fvo.getFILE_DIR();
			logger.debug("■■■■■■■■■■■■■■■■■■■■■■■■■ 1. inputPath ■■■■■■■■■■■■■■■■■■■■■■■■■ : "+inputPath);
			// 1.1 기존 파일 존재여부 판단
			exList = fileService.selectFileList(fvo);
			// 여러 개의 원본 파일을 저장할 리스트 생성
			for (MultipartFile mfile : files) {
				// 2. 원본 파일 이름 알아오기
				String originalFileName = mfile.getOriginalFilename();
				logger.debug("■■■■■■■■■■■■■■■■■■■■■■■■■ 2. 원본 파일 이름 알아오기 ■■■■■■■■■■■■■■■■■■■■■■■■■ : "+originalFileName);
				// 4. 파일 이름 중복되지 않게 이름 변경(서버에 저장할 이름) UUID 사용
				// 5. 파일 생성
				File file = new File(inputPath + originalFileName);// 경로+파일
				logger.debug("■■■■■■■■■■■■■■■■■■■■■■■■■ 3. 파일 생성 ■■■■■■■■■■■■■■■■■■■■■■■■■ : "+file);

				// 아래로직은 반복문을 여러번 탈필요 없음 최초에만
				if (i == 0) {
					if (exList.size() == 0) {// 신규 생성
						logger.debug("■■■■■■■■■■■■■■■■■■■■■■■■■ 파일경로 신규 생성 ■■■■■■■■■■■■■■■■■■■■■■■■■");
						file.getParentFile().mkdirs();// 파일 상부의 모든 존재하지 않는 경로 생성
						logger.debug("■■■■■■■■■■■■■■■■■■■■■■■■■ 파일경로 신규 생성 완료 ■■■■■■■■■■■■■■■■■■■■■■■■■");
					} else { // 기존 것 날리고 새로
						fileDelete(fvo);
						logger.debug("■■■■■■■■■■■■■■■■■■■■■■■■■ 파일경로 내 파일 기존것 삭제 ■■■■■■■■■■■■■■■■■■■■■■■■■");
					}
				}
				// 6. 서버내 파일 경로로 전송
				// 한번 트랜스퍼 된건 또다시 트랜스퍼 되지 않음 거지같은...
				mfile.transferTo(file);
				logger.debug("■■■■■■■■■■■■■■■■■■■■■■■■■ 파일 경로로 전송 완료 ■■■■■■■■■■■■■■■■■■■■■■■■■");

				// 7.리턴값 적재
				FileVo dbvo = new FileVo();
				dbvo.setFILE_ID(UUID.randomUUID().toString());
				dbvo.setFILE_NAME(originalFileName);
				dbvo.setFILE_DIR(inputPath.split("resources")[1]);
				dbvo.setFILE_ORIGIN(fvo.getFILE_ORIGIN());
				fileList.add(dbvo);
				i++;
			}

			// 반복문 종료 후 db 작업 실행
			fileService.insertFile(fileList);
			// model로 저장
			// model.addAttribute("originalFileNameList", originalFileNameList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	// 파일 delete
	// 파라미터 : 파일 vo
	// 경로 내 존재하는 모든 파일을 삭제한다
	public void fileDelete(FileVo fvo) {
		try {
			/* 경로에서 파일 삭제 */
			File dir = new File(firstPath + fvo.getFILE_DIR());// 경로만
			if (dir.isDirectory()) { // 파일이 디렉토리인지 확인
				File[] exfiles = dir.listFiles();// 경로내 존재하는 파일 배열
				for (int i = 0; i < exfiles.length; i++) {
					if (exfiles[i].delete()) {
						logger.debug(exfiles[i].getName() + " 삭제성공");
					} else {
						logger.debug(exfiles[i].getName() + " 삭제실패");
					}
				}
			}
			/* DB에서 파일 삭제 */
			fileService.delete(fvo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 파일 delete
	// 파라미터 : dir(경로)
	// 경로+파일 일괄 삭제
	public void folderDelete(List<FileVo> fList) {
		try {
			for (FileVo fileVo : fList) {
				// 폴더와 파일들 삭제
				File folder = new File(firstPath + fileVo.getFILE_DIR());// 경로만
				if (folder.exists()) {
					FileUtils.cleanDirectory(folder);// 하위 폴더와 파일 모두 삭제
					if (folder.isDirectory()) {
						folder.delete(); // 대상폴더 삭제
						logger.debug(folder + "폴더가 삭제되었습니다.");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/* DB에서 파일 삭제 */
		fileService.deleteAll(fList);
	}

	public void setApplicationContext(org.springframework.context.ApplicationContext applicationContext)
			throws BeansException {
		this.context = (WebApplicationContext) applicationContext;
	}
}
