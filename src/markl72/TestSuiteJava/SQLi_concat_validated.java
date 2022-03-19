package markl72.TestSuiteJava;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet
public class SQLi_concat_validated extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<HTML><BODY><p><b>Concatenated and Validated </b></p>");
        		
        // print form
        out.println("<p>Enter your credentials to log in</p>" 
        		+ "<FORM action='" + request.getContextPath() +"/SQLi2' method='POST'>"
        		+ "userid: <input name='userid'>"
        		+ "<br>password: <input name='password'>"
        		+ "<br><input type='submit'>"
        		+ "</BODY></HTML>");
        
		//doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
		String userid = request.getParameter("userid");
		String password = request.getParameter("password");
		
		out.println("<p>Submitted userid: " + userid + "<BR>");
		out.println("Submitted password: " + password + "</p>");
		
		// validate input
		try {
			Pattern validPatternUserid = Pattern.compile("^[0-9]{3}$");
			if (!validPatternUserid.matcher( userid ).matches())  {
				throw new ServletException( "Userid failed validation rules.");
			} 		
			Pattern validPatternPassword = Pattern.compile("^[A-Za-z0-9]{6,10}$");
			if (!validPatternPassword.matcher( password ).matches())  {
				throw new ServletException( "Password failed validation rules.");
			}
		}
		catch(ServletException e){ 
			out.println(e);
			response.sendError(400, e.getMessage());
		}  

		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");  
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/insecureapp","insecureapp","45EUlZOpL7");  
			
			String sql = "select * from users where userid = '" + userid + "' and password = '" + password + "'";
			
        	out.println("<p><b>SQL:</b> " + sql + "</p>");
        	out.println("<p><b>Results:</b></p>");
  
			PreparedStatement pstmt = connection.prepareStatement( sql );
            ResultSet rs = pstmt.executeQuery(sql);  
            
            while(rs.next()) {
            	out.println("<p>Name: " + rs.getString(3) + " " + rs.getString(2) + ", Address: " + rs.getString(4) + ", Phone no: " + rs.getString(5) + "</p>");
            }
            connection.close();
            out.println("</BODY></HTML>");
            
        } 
		catch(Exception e){ 
			out.println(e);
		}  		
	}
}
