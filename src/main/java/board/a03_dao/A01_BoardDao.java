package board.a03_dao;

import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import board.z01_vo.Board;
import board.z01_vo.BoardFile;

@Repository
public interface A01_BoardDao {

	public ArrayList<Board> boardList(Board sch);
	public void insertBoard(Board insert);
	public void uploadFile(BoardFile ins);
	public Board getBoard(int no);
	public ArrayList<BoardFile> fileInfo(int no);
	public void uptReadCnt(int no);
}
