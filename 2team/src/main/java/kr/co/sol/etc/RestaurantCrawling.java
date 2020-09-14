package kr.co.sol.etc;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class RestaurantCrawling extends JFrame{
	private JLabel la;
	private JTextField txtSearch;
	private JPanel searchPanel;
	private JButton btnSearch;
	private JTable table;
	private JScrollPane sc;
	private DefaultTableModel dtm;
    
	private Object [] columnTitle= {"순번","이름","전화번호","주소"};

	public RestaurantCrawling()
    {
    	super( "네이버 지도 검색 후 데이터 추출" );
        // FlowLayout사용
        setLayout( new FlowLayout() );
        searchPanel = new JPanel();
        la = new JLabel("검색어");
        txtSearch = new JTextField(16);
        searchPanel.add(la);
        searchPanel.add(txtSearch);
        btnSearch = new JButton("검색");
        searchPanel.add( btnSearch );
          
        ActionListener listenerSearch = new ActionListener() {
			
        	@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					search();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
        
		btnSearch.addActionListener(listenerSearch);
		
		
        add(searchPanel);
        
        
        Action ok = new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					search();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		
		txtSearch.setText("강남역 스타벅스");
        
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
        txtSearch.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter, "ENTER");
        txtSearch.getActionMap().put("ENTER", ok);
        
        Object[][] rowData=new Object[0][4];
		dtm=new DefaultTableModel(rowData, columnTitle);
		table = new JTable(dtm);
		table.getColumnModel().getColumn(0).setPreferredWidth(5);
		
		sc = new JScrollPane(table);
		
		add(sc);
		
  		setSize( 500, 700 );
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
	// search
    public void search() throws Exception{
    	final String USER_AGENT = "Mozilla/5.0";
		
		// db처리 
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
		Class.forName("oracle.jdbc.driver.OracleDriver"); // JDBC 드라이브 로드(db주소)
		String url1 = "jdbc:oracle:thin:@172.16.1.3:1521:XE"; // db이름
		String user = "testdba";
		String password = "1234";
		
		conn = DriverManager.getConnection(url1, user, password); // 데이터 베이스 접속
		System.out.println("DB 연결 성공");
		
		String sql = "insert into restaurant(no,c_no,name,address1,tel,hour) "
				+ "values( ? , ? , ? , ? , ? , ?)" ;
		
		
		System.out.println("search() start");
		
		String txtSch=txtSearch.getText();
		System.out.println("txtSch="+txtSch);
		
		String encSch = URLEncoder.encode(txtSch, "UTF-8");
		System.out.println("encSch="+encSch);
		
		int seq=1;
		
		for(int i=1;i<4;i++) {

			System.out.println("i="+i);
			
			String targetUrl="https://map.naver.com/v5/api/search?caller=pcweb&query="+encSch+"&type=place&searchCoord=126.97392940521242;37.5757732700577&page="+i+"&displayCount=20&isPlaceRecommendationReplace=true&lang=ko";
			//String targetUrl="https://map.naver.com/v5/api/search?caller=pcweb&query=종각역 스타벅스&type=place&searchCoord=126.97392940521242;37.5757732700577&page="+i+"&displayCount=20&isPlaceRecommendationReplace=true&lang=ko";
			//String targetUrl="https://www.google.com/";
			
			URL url = new URL(targetUrl); 
			HttpURLConnection con = (HttpURLConnection) url.openConnection(); 
			con.setRequestMethod("GET"); 
			// optional default is GET 
			con.setRequestProperty("User-Agent", USER_AGENT); 
			// add request header 
			int responseCode = con.getResponseCode(); 
			System.out.println("HTTP 응답 코드 : " + responseCode);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8")); 
			String inputLine; 
			StringBuffer response = new StringBuffer(); 
			while ((inputLine = in.readLine()) != null) { 
				response.append(inputLine); 
			} in.close(); // print result 
			System.out.println("HTTP body : " + response.toString());
			
			JSONParser jsonParser = new JSONParser();
            String jsonInfo=response.toString(); 

            JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonInfo);
	        System.out.println(jsonObject);
	        
            JSONObject objResult = (JSONObject) jsonObject.get("result");
            JSONObject objPlace = (JSONObject) objResult.get("place");
            JSONArray objList = (JSONArray) objPlace.get("list");
 
            System.out.println(objList);
            
            for(int j=0; j<objList.size(); j++){
            	 
                System.out.println("=DATA_"+j+" ===========================================");
                 
                //배열 안에 있는것도 JSON형식 이기 때문에 JSON Object 로 추출
                JSONObject bookObject = (JSONObject) objList.get(j);
                
                int no = 0;
                String name=(String) bookObject.get("name");
                String tel=(String) bookObject.get("tel");
                String address=(String) bookObject.get("address");
                
                //JSON name으로 추출
                System.out.println("Info: name==>"+name);
                System.out.println("Info: tel==>"+tel);
                System.out.println("Info: address==>"+address);
                
                Object[] newRow= {seq,name,tel,address};
        		dtm.addRow(newRow);
        		
        		pstmt = conn.prepareStatement("select max(no) + 1 no from restaurant ");
        		rs = pstmt.executeQuery();
        		while(rs.next())
        		{
        			no = rs.getInt("no");
        		}
        		
        		// sql process
        		pstmt = conn.prepareStatement(sql);
        		pstmt.setInt(1, no);
        		pstmt.setInt(2, 5); // 2 : 한식 ,  3: 중식 , 4 : 양식 , 5: 일식  , 6 카페 
        		pstmt.setString(3, name);
        		pstmt.setString(4, address);
        		pstmt.setString(5, tel);
        		pstmt.setString(6, "09:00 ~ 22:00");
        		
        		int result = pstmt.executeUpdate();
        		
        		
        		if (result > 0) {
    				System.out.println("update complete");
    			} else {
    				System.out.println("update fail");
    			}
        		

        		seq++;
            }
            
		}
			System.out.println("search() end");
		
		}catch (SQLException e) {
			System.out.println("DB connection error ");
			e.printStackTrace();
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
    }
    
    /*
    public static void main(String [] agrs)
    {
    	// 검색 버튼 클릭시 db에 들어감 
    	// 207 번 라인 카테고리 숫자 맞게 작성하시구 
    	// 주소 검색 양식 : ex) 종로구 양식집 
    	new RestaurantCrawling();
    }
    */
}
