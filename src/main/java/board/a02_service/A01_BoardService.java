package board.a02_service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import board.a03_dao.A01_BoardDao;
import board.z01_vo.Board;
import board.z01_vo.BoardFile;
import board.z01_vo.BoardSch;

@Service
public class A01_BoardService {
	
	@Autowired(required = false)
	A01_BoardDao dao;
	@Value("${upload}")
	private String upload;
	@Value("${uploadTmp}")
	private String uploadTmp;
	
	// 게시판 리스트
	public ArrayList<Board> boardList(BoardSch sch) {
		if(sch.getSubject() == null) sch.setSubject("");
		if(sch.getWriter() == null) sch.setWriter("");
		// 1. 데이터 총건수 할당.
		sch.setCount(dao.totCnt(sch));
		// 2. 화면에서 요청값으로 가져온 pageSize로 총페이지수 처리.
		//		1) 초기 화면에 표시될 pageSize를 default로 설정.
		if(sch.getPageSize()==0)
			sch.setPageSize(5);
		// 		2) 총페이지수 : 올림처리(총건수/페이지크기)
		//				ex) 총건수 17건이면 / 5 ==> 3.4 ==> 4 페이지필요
		
		sch.setPageCount((int)Math.ceil(
				sch.getCount()/(double)sch.getPageSize()));
		// 4. 클릭한 현재 페이지(요청) default를 1로 선언.
		if(sch.getCurPage() == 0)
			sch.setCurPage(1);
		
		// start, end 속성을 도출하기 위하여.
		sch.setEnd(sch.getCurPage()*sch.getPageSize());
		sch.setStart((sch.getCurPage()-1)*sch.getPageSize()+1);
		
		// # 블럭 처리..
		// 1. 초기 block의 크기 지정 : 5
		sch.setBlocksize(5);
		// 2. blocknum : 현재페이지/블럭의 크기 를 올림 처리
		int blocknum = (int)Math.ceil(
							sch.getCurPage()/(double)sch.getBlocksize());
		// 3. endblock
		int endBlock = blocknum * sch.getBlocksize();
		sch.setEndBlock(endBlock>sch.getPageCount()?
					sch.getPageCount():endBlock);
		
		// 4. startblock
		sch.setStartBlock((blocknum-1)*sch.getBlocksize()+1);
		
		return dao.boardList(sch);
	}
	
	// 게시판 추가
	public void insertBoard(Board insert) {
		System.out.println("upload:"+upload);
		System.out.println("uploadTmp:"+uploadTmp);
		// 2. 데이터베이스 처리..
		dao.insertBoard(insert);
		// 1. 물리적 파일업로드
		String fname = null;
		File tmpFile = null; // 임시위치
		File orgFile = null; // 업로딩 위치
		// # 다중 파일 처리/반복문 수행
		File pathFile = new File(uploadTmp); //폴드 객체생성
		// .listFiles() : 해당 폴드 객체안에 있는 파일을 가져오기.
		// 임시 폴드에 있는 모든 파일을 삭제 함으로 중복 예외를 방지한다.
		for(File f:pathFile.listFiles()) {
			System.out.println("삭제할 파일:"+f.getName());
			// 단위 파일을 삭제 처리.
			f.delete();
		}
		// # 다중 파일 처리/반복문 수행
		System.out.println("등록size:"+insert.getReport().length);
		for(MultipartFile mpf:insert.getReport()) {
			// 1) 파일명 지정.
			fname = mpf.getOriginalFilename();
			// 파일을 등록하지 않을 때, 제외 처리
			if(fname!=null && !fname.trim().equals("")) {
			// 임시파일 객체 선언(경로+파일명)
				// ps) File객체는 파일과 폴드를 처리할 수 있다.
				tmpFile = new File(uploadTmp+fname);
			// MultipartFile ==> File로 변환 처리.
				try {
					mpf.transferTo(tmpFile);
					// io발생 예외 필수 처리
					// 해당 위치에 파일이 생성됨..
					// 임시위치에서 다운로드할 폴드로 z01_upload로 복사 처리.
					orgFile = new File(upload+fname);
					// 복사 처리
					// StandardCopyOption.REPLACE_EXISTING : 동일한 파일명 업로드시,
					// 대체 처리..
					Files.copy(tmpFile.toPath(), orgFile.toPath(),
								StandardCopyOption.REPLACE_EXISTING);
					
					// 파일명, 업로드위치, 제목..
					dao.uploadFile(new BoardFile(fname, upload,
												insert.getSubject()));
					
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("파일 생성 에러:"+e.getMessage());
				} catch (Exception e) {
					System.out.println("기타 에러:"+e.getMessage());
				}
			}
		}
		
		
	}

	// 게시판 상세
	public Board getBoard(int no) {
		// 1. 조회 cnt 수정.(readcnt)증가..
		dao.uptReadCnt(no);
		// 2. 기본 board정보 할당.
		Board board = dao.getBoard(no);
		// 3. 첨부파일 정보 할당.
		board.setFileInfo(dao.fileInfo(no));
		
		return board;
	}
	
	// 게시판 수정
	public void updateBoard(Board upt) {
//	      if(upt.getSubject() == null) upt.setSubject("");
//	      if(upt.getContent() == null) upt.setContent("");
	      int no = upt.getNo();
	      /*
	      # 파일 수정시, 처리할 내용.
	      1. 수정할 파일 upload(물리적)
	      2. 기존 파일을 수정할 파일명으로 변경(DB 처리)
	      
	       */
	      // 수정시, 등록한 파일이 없을 때,
	      if(upt.getFnames()!=null &&
	    		  upt.getFnames().length>0) {
		      // 첨부 파일 물리적 위치 지정
		      String fname = null;   // 수정할 파일명
		      String orgFname = null;	// 기존 파일명
		      File tmpFile = null;	// 임시 폴드
		      File orgFile = null;	// 실제 업로드 폴드
		      // 변경할 파일
		      MultipartFile mpf = null;
		      // 임시 파일 삭제 처리
		      File pathFile = new File(uploadTmp);   // 폴더 객체생성
		      for(File f : pathFile.listFiles()) {
		         System.out.println("삭제할 파일: " + f.getName());
		         f.delete();
		      }
		      
		      // 수정할 파일과 업로드로 대체할 파일은 index가 같다.
		      for(int idx=0;idx<upt.getReport().length;idx++) {
		         mpf = upt.getReport()[idx]; // 대체할 파일 가져오기
		         fname = mpf.getOriginalFilename(); // 대체할 파일명 가져오기
		         
		         // 기존 파일명
		         orgFname = upt.getFnames()[idx]; // 수정할 기존 파일명
		         // 변경할 파일 선택해서 추가할 때만 처리할 수 있도록 조건
		         if(fname != null && !fname.trim().equals("")) {
		            // 해당 폴더에 기존 파일은 일단 삭제(임시 폴더)
		            tmpFile = new File(uploadTmp+orgFname);
		            if(tmpFile.exists()) {
		               tmpFile.delete();
		            }
		            // 해당 폴더에 기존 파일은 일단 삭제(대상 폴더)
		            orgFile = new File(upload+orgFname);
		            if(orgFile.exists()) {
		               orgFile.delete();
		            }
		            // 선택한 파일을 임시폴드 위치/업로드할 위치로 파일객체 생성.
		            tmpFile = new File(uploadTmp+fname);
		            orgFile = new File(upload+fname);
		            
		            try {
		               // MultipartFile을 임시파일객체로 변환 처리
		               mpf.transferTo(tmpFile);
		               
		               Files.copy(tmpFile.toPath(), orgFile.toPath(),
		                     StandardCopyOption.REPLACE_EXISTING);
		               
		            } catch (IllegalStateException e) {
		               // TODO Auto-generated catch block
		               e.printStackTrace();
		               System.out.println("# 상태 에러: " + e.getMessage());
		            } catch (IOException e) {
		               // TODO Auto-generated catch block
		               e.printStackTrace();
		               System.out.println("# 파일 에러: " + e.getMessage());
		            } catch (Exception e) {
		               System.out.println("# 기타 에러: " + e.getMessage());
		            }
		            // 변경된 파일 정보를 수정 처리.
		            HashMap<String, String> hs = new HashMap<String, String>();
		            hs.put("no", ""+no);
		            hs.put("fname", fname);
		            hs.put("orgFname", upt.getFnames()[idx]);
		            // dao단 호출 처리
		            dao.updateFile(hs);
	         	}
	         }
	      }
	      // 메일 게시판 수정 정보.
	      dao.updateBoard(upt);     
	   
	}
	
	// 게시판 삭제
	public void deleteBoard(int no) {
		dao.deleteFile(no);
		dao.deleteBoard(no);
	}
}
