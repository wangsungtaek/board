package board.a01_controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import board.a02_service.A01_BoardService;
import board.z01_vo.Board;
import board.z01_vo.BoardSch;
import board.z01_vo.Member;

@Controller
@RequestMapping("board.do")
public class A01_BoardController {
	
	@Autowired(required = false)
	A01_BoardService service;
	
	@ModelAttribute("pageOp")
	public List<String> tools(){
		return Arrays.asList("3","5","10","20","30");
	}
	
	// http://localhost:7080/board/board.do?method=list
	@RequestMapping(params = "method=list")
	public String boardList(@ModelAttribute("sch") BoardSch sch,
			Model m, HttpServletRequest request) {
		// 세션 설정.
		HttpSession session = request.getSession();
		session.setAttribute("mem", new Member("himan", "7777"));
		
		m.addAttribute("boardList", service.boardList(sch));
		return "a01_boardList";
	}
	// http://localhost:7080/board/board.do?method=insForm
	@RequestMapping(params = "method=insForm")
	public String insForm(@ModelAttribute("board") Board d) {
		return "a02_boardInsert";
	}
	// 초기에 @ModelAttribute에 객체가 데이터를 받지 못할 때,
	// 객체형태는 null "" 숫자형태는 0으로 default값이 설정된다.
	
	
	
	// http://localhost:7080/board/board.do?method=insert
	@RequestMapping(params = "method=insert")
	public String boardInsert(Board insert, Model d) {
		System.out.println("파일:"+insert.getReport()[0].getOriginalFilename());
		service.insertBoard(insert);
		
		d.addAttribute("proc", "insert");
		return "a02_boardInsert";
	}
	
	// http://localhost:7080/board/board.do?method=detail
	@RequestMapping(params = "method=detail")
	public String boardDetail(@RequestParam("no") int no, Model m) {
		m.addAttribute("board",service.getBoard(no));
		
		return "a03_boardDetail";
	}
	
	// 화면단에서 클릭시,
	// http://localhost:7080/board/board.do?method=download&fname=파일명
	@RequestMapping(params = "method=download")
	public String download(@RequestParam("fname") String fname, Model d) {
		
		d.addAttribute("downloadFile", fname); // viewer안에서 선언한 모델명
		// 컨테이너 안에 있는 viewer명.
		return "downloadviewer";
	}
	
	// http://localhost:7080/board/board.do?method=update
	@RequestMapping(params = "method=update")
	public String updateBoard(Board board) {
		service.updateBoard(board);
		return "forward:/board.do?method=detail";
		// 수정 후, 다시 조회할 수 있게 하기 위하여 forward로 해당 controller호출..
	}
	// http://localhost:7080/board/board.do?method=delete
	@RequestMapping(params = "method=delete")
	public String deleteBoard(@RequestParam("no") int no) {
		service.deleteBoard(no);
		return "a03_boardDetail";
	}
}
