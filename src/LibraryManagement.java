import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibraryManagement {
    private List<Book> books;
    private Map<String, User> users;
    private User currentUser;

    public LibraryManagement(){
        this.books = new ArrayList<>();
        this.users = new HashMap<>();
        this.users.put("admin",new User("admin","i<3c++",true));
        this.currentUser = null;
    }

    public void loadBooks(String filename){
        try (BufferedReader br = new BufferedReader(new FileReader(filename))){
            String line;
            while((line = br.readLine()) != null){
                String[] data = line.split(",");
                Book book = new Book(data[0],data[1],data[2],data[3],Integer.parseInt(data[4]),data[5],Double.parseDouble(data[6]),data[7]);
                books.add(book);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void login(String username, String password){
        if (users.containsKey(username)) {
            if (users.get(username).getPassword().equals(password)) {
                currentUser = users.get(username);
                System.out.println("Welcome, " + username + "!");
            }
            else{
                System.out.println("Wrong password!");
            }
        }
        else{
            System.out.println("Wrong username!");
        }
    }

    public void logout(){
        currentUser = null;
        System.out.println("Logged out successfully!");
    }

    public void allBooks(){
        for (Book book: books) {
            System.out.println(book.getTitle() + ", " + book.getAuthor() + " ," + book.getGenre() + ", " + book.getIsbn());
        }

    }

    public void bookInfo(String isbn){
        for (Book book: books) {
            if (book.getIsbn().equals(isbn)) {
                System.out.println(book.getTitle() + ", " + book.getAuthor() + " ," + book.getGenre() + ", " + book.getIsbn() + ", " + book.getAnnotation());
            }
        }
    }

    public void usersAdd(String username, String password, boolean isAdmin){
     users.put(username, new User(username, password, isAdmin));
    }

    public void usersRemove(String username){
        if(users.containsKey(username)){
            users.remove(username);
            System.out.println("User with username: " + username + " has been removed!");
        }
        else{
            System.out.println("User with username: " + username + " has not been found!");
        }

    }




}
