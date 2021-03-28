package board.a03_dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Repository;

import board.z01_vo.Board;
import board.z01_vo.BoardFile;
import board.z01_vo.BoardSch;

@Repository
public interface A01_BoardDao {
	public int totCnt(BoardSch sch);
	public ArrayList<Board> boardList(BoardSch sch);
	public void insertBoard(Board insert);
	public void uploadFile(BoardFile ins);
	public Board getBoard(int no);
	public ArrayList<BoardFile> fileInfo(int no);
	public void uptReadCnt(int no);
	public void updateBoard(Board upt);
	public void updateFile(HashMap<String, String> hs);
	public void deleteBoard(int no);
	public void deleteFile(int no);
}
