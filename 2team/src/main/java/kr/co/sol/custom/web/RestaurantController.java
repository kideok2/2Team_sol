package kr.co.sol.custom.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.sol.custom.dto.MenuDTO;
import kr.co.sol.custom.dto.PageDTO;
import kr.co.sol.custom.dto.RestaurantDTO;
import kr.co.sol.custom.dto.ReviewDTO;
import kr.co.sol.custom.service.MemberService;
import kr.co.sol.custom.service.RestaurantService;
import kr.co.sol.custom.service.ReviewService;

@Controller
public class RestaurantController {

	@Autowired
	RestaurantService restaurantService;
	
	@Autowired
	ReviewService reviewService;
	
	@Autowired
	MemberService memberService;
	
	
	// sub1 page 
	@RequestMapping("/custom/sub1")
	public String testCon(RestaurantDTO tdto, Model model) {
		
		List<RestaurantDTO> tdto2 = restaurantService.getRestaurants(tdto);		
		model.addAttribute("tdto",tdto2);
		
		return "/custom/sub1";
	}

	
	// sub2 - selected restaurant page
	@RequestMapping(value="/custom/sub2")
	public String sub2(HttpServletRequest request, HttpServletResponse response,
			RestaurantDTO tdto, Model model , PageDTO pdto
			/*@RequestParam("no") int res_no*/) {
		
		int res_no = 1; // 임시 음식점 번호
		
		tdto.setNo(res_no);
		
		// restaurant info
		List<RestaurantDTO> tlist = restaurantService.getRestaurants(tdto); 
		model.addAttribute("tdto", tlist.get(0));
		
		// favorite check
		char favoriteCheck = 'f';
		HttpSession session = request.getSession();
		String idKey = (String)session.getAttribute("idKey");
		
		if(idKey == null || idKey.equals(""))
		{
			favoriteCheck = 'f';
			
		}else {
			
			int mem_no = memberService.getMemberNo(idKey);
			HashMap<String,Integer> hmap = new HashMap<String,Integer>();
			hmap.put("res_no",res_no);
			hmap.put("mem_no",mem_no);
			
			int r = restaurantService.favoriteCheck(hmap);
			
			if(r>0) {
				favoriteCheck = 't';
			}else {
				favoriteCheck = 'f';
			}		
		}
		model.addAttribute("favoriteCheck",favoriteCheck);
		
		// review count & avg(rating) -> reviewService.reviewCountAndAvg
		Map<String,Object> rmap = reviewService.reviewCountAndAvg(tdto);
		model.addAttribute("count",rmap.get("count"));
		model.addAttribute("avg",rmap.get("avg"));
	
		// menu info -> restaurantService.getMenus
		List<MenuDTO> mlist = restaurantService.getMenus(tdto);
		model.addAttribute("mlist",mlist);
		
		// paging info
			// 전체 레코드수
		int cnt = Integer.parseInt(String.valueOf(rmap.get("count")+""));
		pdto.setAllCount(cnt);
			// 전체 페이지 수 계산
		int pageCnt = cnt % pdto.getLinePerPage();
		pdto.setAllPage(pageCnt);
		if(pageCnt == 0) {
			pdto.setAllPage(cnt/pdto.getLinePerPage());
		}else {
			pdto.setAllPage(cnt/pdto.getLinePerPage() + 1);
		}
		
			// 현재 페이지
		int currentPage = 0;
		
		if(request.getParameter("currentPage") == null || request.getParameter("currentPage").equals("0"))
		{
			currentPage = 1;
		}else {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}
		
			// 현재 블럭
		int currPageBlock = 0;
		
		if(request.getParameter("currPageBlock") == null || request.getParameter("currPageBlock").equals(0))
		{
			currPageBlock = 1;
		}else {
			currPageBlock = Integer.parseInt(request.getParameter("currPageBlock"));
		}
		pdto.setCurrentPage(currentPage);
		pdto.setCurrPageBlock(currPageBlock);
		
		int startPage = 1;
		int endPage = 1;
		
		startPage = (currPageBlock - 1) * pdto.getPageBlock() + 1;
    	endPage = currPageBlock * pdto.getPageBlock() > pdto.getAllPage() ? 
    			pdto.getAllPage() : currPageBlock * pdto.getPageBlock();
    
    	pdto.setStartPage(startPage);
    	pdto.setEndPage(endPage);
    	model.addAttribute("pdto",pdto);
    	
    	int sRow = (currentPage-1) * pdto.getLinePerPage() + 1;
    	
    	HashMap<String,Integer> hmap2 = new HashMap<String,Integer>();
    	hmap2.put("start", sRow);
    	hmap2.put("end", currentPage * pdto.getLinePerPage());
    	hmap2.put("res_no", res_no);
    	if(idKey != null )
    	{
    		int mem_no = memberService.getMemberNo(idKey);
    		hmap2.put("mem_no2",mem_no);
    	}
    	
    	
		// reviews info
		List<ReviewDTO> rlist = reviewService.getReviews(hmap2);
		model.addAttribute("rlist",rlist);
		
		return "/custom/sub2";
	}
	
	
	
	// favorites process
	@RequestMapping(value="/custom/favorites")
	public String favorites(HttpServletRequest request , Model model) {
		
		int res_no = 1; // 임시 음식점 번호
		
		HttpSession session = request.getSession();
		String idKey = (String)session.getAttribute("idKey");
		
		String msg = "";
		String url = "/custom/sub2";
		
		// 세션에 id값이 없을 때 로그인 페이지로 이동 
		if(idKey == null || idKey.equals(""))
		{
			msg="로그인 부터 해주시길 바랍니다. ";
			url="/custom/login";
			
		}else {
			
			
			// 세션에 저장된 idKey(mem_id) 값으로 mem_no 구해오기 
			int mem_no = memberService.getMemberNo(idKey);
			
			HashMap<String,Integer> hmap = new HashMap<String,Integer>();
			hmap.put("res_no",res_no);
			hmap.put("mem_no",mem_no);
			
			
			int r = restaurantService.favoriteCheck(hmap);
			
			if(r > 0)
			{
				r = restaurantService.dislikeRestaurant(hmap); // 좋아요 해제
				
				if(r > 0)
					msg = "favorite off";
				else
					msg = "favorite off error";
			}else {
				
				r = restaurantService.likeRestaurant(hmap); // 좋아요 
				
				if(r > 0)
					msg = "favorite on";
				else
					msg = "favorite on error";
			}
			
		}
		
		model.addAttribute("msg", msg);
		model.addAttribute("url",url);
		
		return "/custom/msgPage";
	}
	
	
	// booking page 
	
	
}
