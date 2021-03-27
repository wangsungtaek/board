package board.a01_controller;

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
import board.z01_vo.Member;

@Controller
@RequestMapping("board.do")
public class A01_BoardController {
	
	@Autowired(required = false)
	A01_BoardService service;
	
	// http://localhost:7080/board/board.do?method=list
	@RequestMapping(params = "method=list")
	public String boardList(@ModelAttribute("sch") Board sch,
			Model m, HttpServletRequest request) {
		// 세션 설정.
		HttpSession session = request.getSession();
		session.setAttribute("mem", new Member("himan", "7777"));
		
		m.addAttribute("boardList", service.boardList(sch));
		return "a01_boardList";
	}
	// http://localhost:7080/board/board.do?method=insForm
	@RequestMapping(params = "method=insForm")
	public String insForm() {
		return "a02_boardInsert";
	}
	// http://localhost:7080/board/board.do?method=insert
	@RequestMapping(params = "method=insert")
	public String boardInsert(Board insert) {
		System.out.println("파일:"+insert.getReport()[0].getOriginalFilename());
		service.insertBoard(insert);
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
	// http://localhost:7080/board/board.do?method=delete


}
