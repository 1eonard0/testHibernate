
import com.mycompany.testcrudhibernate.HibernateUtil;
import com.mycompany.testcrudhibernate.Student;
import java.util.List;
import java.util.Scanner;
import org.hibernate.Session;
import org.hibernate.Transaction;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Leo
 */
public class Main {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        int op;
        
        do {            
            System.out.println("1.See stundents. \n"
                            + "2.Add student. \n"
                            + "3.Remove student. \n"
                            + "4.Search students. \n"
                            + "5.Exit. \n"
                            + "Insert an option. \n");
            
            Scanner sc = new Scanner(System.in);
            
            try {
                op = sc.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("You should insert only numbers. (1 to 4) \n");
                op = 0;
            }
            
            
            switch(op){
                case 1:
                    printStudents();
                    break;
                case 2:
                    saveStudent();
                    break;
                case 3:
                    removeStudent();
                    break;
                case 4:
                    Student student = getStudent();
                    if (student != null) {
                        System.out.println("ID : " + student.getId() + "\n"
                                        + "Name : " + student.getName() + "\n"
                                        + "Surname : " + student.getSurname() + "\n");
                    }
                    break;
            }
            
            
        } while (op != 5);
        System.exit(0);
    }
    
    private static void printStudents(){
        Transaction transaction = null;
        List<Student> students = null;
        
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            
            transaction = session.beginTransaction();
            
            students = session.createQuery("from Student", Student.class).list();
            
            transaction.commit();
            
        }catch(Exception e){
            if(transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
        }
        
        if (!students.isEmpty()) {
            System.out.format("%11s%50s%60s", "Id", "Name", "Surname");
            System.out.println("\n");
            for (Student student : students) {
                System.out.format("%11d%50s%60s", student.getId(), student.getName(), student.getSurname());
                System.out.println("\n");
            }
            
        }
    }
    
    private static void saveStudent(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert the name.\n");
        String name = scanner.nextLine();
        System.out.println("Insert the surname.\n");
        String surname = scanner.nextLine();
        
        
        Transaction transaction = null;
        
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            
            transaction = session.beginTransaction();
            
            session.save(new Student(name, surname));
            
            transaction.commit();
            
        }catch(Exception e){
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }
    
    private static void removeStudent(){
        /*
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert the student's id to remove.\n");
        int idStudent;
        try {
            idStudent = scanner.nextInt();
        } catch (java.util.InputMismatchException e) {
            System.out.println("You should insert only numbers. \n");
            idStudent = 0;
        }*/
        
        Student student = getStudent();
        
        if (student != null) {
            Transaction transaction = null;
        
            try(Session session = HibernateUtil.getSessionFactory().openSession()){

                transaction = session.beginTransaction();

                session.delete(student);

                transaction.commit();

            }catch(Exception e){
                e.printStackTrace();
                if (transaction != null) {
                    transaction.rollback();
                }
            }
        }
        
    }
    
    private static Student getStudent(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert the student's id.\n");
        int idStudent;
        try {
            idStudent = scanner.nextInt();
        } catch (java.util.InputMismatchException e) {
            System.out.println("You should insert only numbers. \n");
            idStudent = 0;
        }
        
        Student student = null;
        
        Transaction transaction = null;
        
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            
            transaction = session.beginTransaction();
            
            student = session.get(Student.class, idStudent);
            
            transaction.commit();
            
        }catch(Exception e){
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
       
        return student;
    }
    
}
