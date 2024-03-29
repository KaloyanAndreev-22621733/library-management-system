import java.io.*;
import java.util.*;

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
     saveUserToFile(username,password,isAdmin);
     System.out.println("User added successfully.");
    }

    private void saveUserToFile(String username, String password, boolean isAdmin){
        try(BufferedWriter bf = new BufferedWriter(new FileWriter("users.txt",true))){
            bf.write(username + ", " + password + ", " + isAdmin);
            bf.newLine();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void usersRemove(String username){
        if(users.containsKey(username)){
            users.remove(username);
            removeUserFromFile(username);
            System.out.println("User with username: " + username + " has been removed!");
        }
        else{
            System.out.println("User with username: " + username + " has not been found!");
        }

    }

    private void removeUserFromFile(String username){
        try(BufferedReader br = new BufferedReader(new FileReader("users.txt"));
        BufferedWriter bw = new BufferedWriter(new FileWriter("temp_users.txt"))){
            String line;
            while ((line = br.readLine()) != null){
                String[] userdata = line.split(",");
                if (!userdata[0].equals(username)) {
                    bw.write(line);
                    bw.newLine();
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void booksFind(String option, String optionString){
        for (Book book : books) {
            String value = "";
            switch (option){
                case "title":
                    value = book.getTitle().toLowerCase();
                    break;
                case "author":
                    value = book.getAuthor().toLowerCase();
                    break;
                case "tag":
                    value = book.getKeywords().toLowerCase();
                    break;
            }
            if (value.contains(optionString.toLowerCase())){
                System.out.println(book.getTitle() + ", " + book.getAuthor() + ", " + book.getGenre() + ", " + book.getIsbn());
            }
        }
    }

    public void bookSort(String option, String order){
        Comparator<Book> comparator = null;
        switch (option){
            case "title":
                comparator = Comparator.comparing(Book::getTitle);
                break;
            case "author":
                comparator = Comparator.comparing(Book::getAuthor);
                break;
            case "year":
                comparator = Comparator.comparing(Book::getPublishingYear);
                break;
            case "rating":
                comparator = Comparator.comparing(Book::getRating);
                break;
            default:
        }

        if (comparator != null){
            if (order.equals("desc")){
                comparator = comparator.reversed();
            }
            Collections.sort(books,comparator);
            for (Book book : books) {
                System.out.println(book.getTitle() + ", " + book.getAuthor() + ", " + book.getGenre() + ", " + book.getIsbn());
            }
        }
    }
}
