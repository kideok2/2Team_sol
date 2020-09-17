package kr.co.sol.admin;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sol.common.dao.MemberDAO;
import kr.co.sol.common.dto.MemberDTO;
import kr.co.sol.common.dto.PageDTO;
import kr.co.sol.common.dto.RestaurantDTO;

@Service("AdminService")
public class AdminServiceImpl implements AdminService {

	@Autowired
	AdminDAO adminDao;
	@Autowired
	MemberDAO memberDao;
	
	@Override
	public List<HashMap<String, Object>> getMember() {
		return adminDao.getMember();
	}
	@Override
	public List<HashMap<String, Object>> getMemberList(@Param("searchOption") String searchOption, @Param("keyword") String keyword) {
		// TODO Auto-generated method stub
		return adminDao.getMemberList(searchOption, keyword);
	}
	@Override
	public List<RestaurantDTO> getStore(String searchOption, String keyword) {
		System.out.println("임플::"+searchOption+"=="+keyword);
		
		return adminDao.getStore(searchOption, keyword);
	}
	@Override
	public List<RestaurantDTO> getStoreList(PageDTO pdto, int curPage) { // 현재 페이지 가져오기.
		// paging 처리
		// 전체 리스트 가져오기.;
		
		int total = adminDao.getStoreListCnt(); // 전체 게시물 수
		int listSize = 10;
		int startPage = (curPage-1)*10+1;
		int endPage = (curPage)*10;
//		int allList = adminDao.allList();
		
		System.out.println("startP = "+startPage+"endP = "+endPage);
		return adminDao.getStoreList(startPage, endPage);
	}
	@Override
	public int nameChk(String name) {
		// TODO Auto-generated method stub
		return adminDao.nameChk(name);
	}
	@Override
	public List<HashMap<String,Object>> getBookingList() {
		// TODO Auto-generated method stub
		return adminDao.getBookingList();
	}
	@Override
	public List<HashMap<String,Object>> getBooking(String searchOption, String keyword) {
		// TODO Auto-generated method stub
		return adminDao.getBooking(searchOption, keyword);
	}
	@Override
	public int bCancel(int no) {
		// TODO Auto-generated method stub
		return adminDao.bCancel(no);
	}
}
