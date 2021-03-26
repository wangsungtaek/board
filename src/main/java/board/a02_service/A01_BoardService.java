package board.a02_service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import board.a03_dao.A01_BoardDao;
import board.z01_vo.Board;

@Service
public class A01_BoardService {
	
	@Autowired(required = false)
	A01_BoardDao dao;
	
	public ArrayList<Board> boardList(Board sch) {
		if(sch.getSubject() == null) sch.setSubject("");
		if(sch.getWriter() == null) sch.setWriter("");
		
		return dao.boardList(sch);
	}
	public void insertBoard(Board insert) {
		dao.insertBoard(insert);
	}
}
