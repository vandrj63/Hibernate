package data;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
import util.HibernateUtil;
import org.hibernate.*;
public class StudentInfoServlet extends HttpServlet {
SessionFactory sessionFactory;
Session session;

protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
	try {	
		PrintWriter out = response.getWriter();
		out.println("<html><head><title>students</title></head><body>");
		// Begin unit of work
		printStudentForm(out);
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		if ("store".equals(request.getParameter("action"))) {
			String name = request.getParameter("name");
			String gpa = request.getParameter("gpa");
			String year = request.getParameter("year");
			String age = request.getParameter("age");
			String sex = request.getParameter("sex");
			if ("".equals(name) || "".equals(sex)||year.equals("")||gpa.equals("")||age.equals("")) {
				out.println("<b><i>enter name, age, gpa, sex, year.</i></b>");
			} 
			else {
				System.out.println("before call to create and store");
				createAndStoreEvent(name,age,gpa,sex,year);
				out.println("<b><i>Added event.</i></b>");                
			}            
		}
		else if  ("list".equals(request.getParameter("action"))) {
			System.out.println("");
			listStudents(out);}
		else if ("update".equals(request.getParameter("action"))){
			String name = request.getParameter("name");
			String gpa = request.getParameter("gpa");
			String year = request.getParameter("year");
			String age = request.getParameter("age");
			String sex = request.getParameter("sex");
			List marked=HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from Student as student where student.name=?").setString(0,name).list();
			if(marked.size()>0 && !"".equals(sex) && !year.equals("") && !gpa.equals("") && !age.equals("")) {
				Student student=(Student)marked.get(0);
				student.setAge(Integer.parseInt(age));
				student.setYear(year);
				student.setGpa(Double.parseDouble(gpa));
				student.setSex(sex);
				out.println("Updating "+ student.getName());
				HibernateUtil.getSessionFactory().getCurrentSession().update(student);
			}
			else {out.println("");}
		}
		else if  ("delete".equals(request.getParameter("action"))) {
			String name = request.getParameter("name");
			List marked = HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from Student as student where student.name=?").setString(0,name).list();
			if(marked.size()>0){
				Student student=(Student)marked.get(0);
				out.println("deleting "+ student.getName());
				HibernateUtil.getSessionFactory().getCurrentSession().delete(student);
			}
			else {out.println("");}}

		
		
		out.println("</body></html>");
		out.flush();
		out.close();

		// End unit of work
		HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
		System.out.println("commit after save");
		
        } catch (Exception ex) {
            //if (session.getTransaction() != null){HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();}
            throw new ServletException(ex);
        } 
    }

    
private void listStudents(PrintWriter out) {

	List result1 = HibernateUtil.getSessionFactory().getCurrentSession().createCriteria(Student.class).list();

            if (result1.size() > 0) {
            out.println("<h2>Student Info:</h2>");
            out.println("<table border='2'>");
            out.println("<tr>");
            out.println("<th>student name</th>");
            out.println("<th>student Age</th>");
           out.println("<th>gpa</th>");
            out.println("<th>sex</th>");
           out.println("<th>year</th>");
             out.println("</tr>");

            for (Iterator it = result1.iterator(); it.hasNext();) {

                Student student = (Student) it.next();
		out.println("<tr>");
                out.println("<td>" + student.getName()+ "</td>");
		out.println("<td>" + student.getAge() + "</td>");
                out.println("<td>" + student.getGpa() + "</td>");
                out.println("<td>" + student.getSex() + "</td>");
                out.println("<td>" + student.getYear() + "</td>");
                out.println("</tr>");
            }
            out.println("</table>");
	}
}


private void printStudentForm(PrintWriter out) {
	//label = new JLabel("A label");
	//label.setFont(new Font("Serif", Font.PLAIN, 14));
	//label.setForeground(new Color(0xffffdd));
	out.println("<h2>Student:</h2>");
	out.println("<form>");
        out.println("Name: <input name='name' length='40'/><br/>");
        out.println("Age: <input name='age' length='10'/><br/>");
	out.println("Sex(M/F): <input name='sex' length='10'/><br/>");
	out.println("Year: <input name='year' length='15'/><br/>");
	out.println("GPA: <input name='gpa' length='10'/><br/>");
	out.println("<input type='radio' name='action' value='list' checked> List<br>");
	out.println(" <input type='radio' name='action' value='store'> Store<br>");
	out.println("<input type='radio' name='action' value='update'> Update<br>");
	out.println(" <input type='radio' name='action' value='delete'> Delete<br>");
        out.println("<input type='submit' />");
        out.println("</form>");
}


protected void createAndStoreEvent(String name, String age, String gpa, String sex, String year) {
	Student theStudent = new Student();
	theStudent.setName(name);
	theStudent.setAge(Integer.parseInt(age));
	theStudent.setGpa(Double.parseDouble(gpa));
	theStudent.setSex(sex);
	theStudent.setYear(year);
	HibernateUtil.getSessionFactory().getCurrentSession().save(theStudent);
}
protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
	processRequest(request, response);
}
protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
	processRequest(request, response);
}
}
