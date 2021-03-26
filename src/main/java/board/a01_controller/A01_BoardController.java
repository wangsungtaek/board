package board.a01_controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import board.a02_service.A01_BoardService;
import board.z01_vo.Board;

@Controller
@RequestMapping("board.do")
public class A01_BoardController {
	
	@Autowired(required = false)
	A01_BoardService service;
	
	// http://localhost:7080/board/board.do?method=list
	@RequestMapping(params = "method=list")
	public String boardList(@ModelAttribute("sch") Board sch, Model m) {
		
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
		service.insertBoard(insert);
		return "a02_boardInsert";
	}

	
	
	// http://localhost:7080/board/board.do?method=detail
	// http://localhost:7080/board/board.do?method=update
	// http://localhost:7080/board/board.do?method=delete

}
