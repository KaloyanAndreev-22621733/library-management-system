import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class LibraryManagement {
    private List<Book> books;
    private Map<String, User> users;
    private User currentUser;
    private String currentFile = null;
    private boolean loggedIn;

    public LibraryManagement(){
        this.books = new ArrayList<>();
        this.users = new HashMap<>();
        this.users.put("admin",new User("admin","i<3c++",true));
        this.currentUser = null;
    }
    public void open(String filename) {
        if (Files.exists(Paths.get(filename))) {
            currentFile= filename;
            System.out.println("Successfully opened " + filename);
            switch (filename){
                case "books.xml":
                    loadBooks(filename);
                case "users.xml":
                    loadUsersFromFile(filename);
            }
            //loadBooks(filename);
        } else {
            try {
                Files.createFile(Paths.get(filename));
                currentFile = filename;
                System.out.println("New file created: " + filename);
            } catch (IOException e) {
                System.out.println("Error: Unable to create new file.");
            }
        }
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

    public void loadUsersFromFile(String file){
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String username = parts[0].trim();
                    String password = parts[1].trim();
                    users.put(username, new User(username, password,false));
                }
            }
            //System.out.println("Users loaded successfully from " + file);
        } catch (IOException e) {
            System.out.println("Error loading users from file: " + e.getMessage());
        }
    }

    public void close() {
        currentFile = null;
        System.out.println("Successfully closed the file.");
    }

    public void save() {
        if (currentFile != null) {
            saveFile(currentFile);
            System.out.println("Successfully saved " + currentFile);
        } else {
            System.out.println("No file is currently open.");
        }
    }

    public void saveAs(String filename) {
        //currentFile = filename;
        File file = new File(currentFile);
        if (file.exists()) {
            // Rename the existing file before creating a new one

            File renamedFile = new File(filename);
            if (file.renameTo(renamedFile)) {
                System.out.println("Existing file renamed to: " + filename);
            } else {
                System.out.println("Error: Unable to rename existing file.");
                return;
            }
        }
        else{
            try {
                file.createNewFile();
                System.out.println("File saved as: " + filename);
            } catch (IOException e) {
                System.out.println("Error: Unable to save file.");
                e.printStackTrace();
            }
        }

    }

    private void saveFile(String filename) {
        File file = new File(filename);
        try {
            if (file.exists()) {
                System.out.println("File already exists. Overwriting...");

            }
            file.createNewFile();
            System.out.println("File saved as: " + filename);
        } catch (IOException e) {
            System.out.println("Error: Unable to save file.");
            e.printStackTrace();
        }
    }

    public void help() {
        System.out.println("The following commands are supported:");
        System.out.println("open <file> - opens <file>");
        System.out.println("close - closes currently opened file");
        System.out.println("save - saves the currently open file");
        System.out.println("saveas <file> - saves the currently open file in <file>");
        System.out.println("help - prints this information");
        System.out.println("exit - exits the program");
    }
    public void login(String username, String password){
        loadUsersFromFile("users.xml");
        if (users.containsKey(username)) {
            if (users.get(username).getPassword().equals(password)) {
                currentUser = users.get(username);
                System.out.println("Welcome, " + username + "!");
                loggedIn = true;
            }
            else{
                System.out.println("Wrong password!");
                loggedIn = false;
            }
        }
        else{
            System.out.println("Wrong username!");
            loggedIn = false;
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
        try(BufferedWriter bf = new BufferedWriter(new FileWriter("users.xml",true))){
            if (Files.exists(Paths.get("users.xml"))) {
                bf.write(username + ", " + password + ", " + isAdmin);
                bf.newLine();
            }
            else {
                try {
                    Files.createFile(Paths.get("users.xml"));
                    currentFile = "users.xml";
                    System.out.println("New file created: users.xml");
                    saveUserToFile(username,password,isAdmin);
                } catch (IOException e) {
                    System.out.println("Error: Unable to create new file.");
                }
            }
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
        try(BufferedReader br = new BufferedReader(new FileReader("users.xml"));
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

        File originalFile = new File("users.xml");
        File tempFile = new File("temp_users.txt");
        if (!tempFile.renameTo(originalFile)) {
            System.out.println("Error: Unable to rename temp file to original file.");
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
    public boolean getLog(){
        return loggedIn;
    }
}
