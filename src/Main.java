import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        LibraryManagement librarySystem = new LibraryManagement();
        Scanner scanner = new Scanner(System.in);
        boolean loggedIn = false;

        while (true) {
            System.out.print("Enter command: ");
            String command = scanner.nextLine().trim();

            if (command.equals("exit")) {
                break;
            }

            if (loggedIn || command.equals("login") || command.equals("open")) {
                switch (command) {
                    case "login":
                        if (loggedIn) {
                            System.out.println("You are already logged in.");
                        } else {
                            System.out.print("Enter username: ");
                            String username = scanner.nextLine().trim();
                            System.out.print("Enter password: ");
                            String password = scanner.nextLine().trim();
                            librarySystem.login(username, password);
                            loggedIn = true;
                        }
                        break;
                    case "logout":
                        librarySystem.logout();
                        loggedIn = false;
                        break;
                    case "open":
                        System.out.print("Enter filename: ");
                        String filename = scanner.nextLine().trim();
                        librarySystem.open(filename);
                        break;
                    case "save":
                        librarySystem.save();
                        break;
                    case "close":
                        librarySystem.close();
                        break;
                    case "saveas":
                        System.out.print("Enter filename: ");
                        String saveAsFilename = scanner.nextLine().trim();
                        librarySystem.saveAs(saveAsFilename);
                        break;
                    case "books all":
                        librarySystem.allBooks();
                        break;
                    case "books info":
                        String isbnValue = scanner.nextLine().trim();
                        librarySystem.bookInfo(isbnValue);
                        break;
                    case "books find":
                        System.out.print("Enter option (title, author, tag): ");
                        String option = scanner.nextLine().trim();
                        System.out.print("Enter option string: ");
                        String optionString = scanner.nextLine().trim();
                        librarySystem.booksFind(option, optionString);
                        break;
                    case "books sort":
                        System.out.print("Enter option (title, author, year, rating): ");
                        String sortOption = scanner.nextLine().trim();
                        System.out.print("Enter order (asc, desc): ");
                        String sortOrder = scanner.nextLine().trim();
                        librarySystem.bookSort(sortOption, sortOrder);
                        break;
                    case "users add":
                        System.out.print("Enter username: ");
                        String newUser = scanner.nextLine().trim();
                        System.out.print("Enter password: ");
                        String newPassword = scanner.nextLine().trim();
                        librarySystem.usersAdd(newUser, newPassword, false);
                        break;
                    case "users remove":
                        System.out.print("Enter username: ");
                        String removeUser = scanner.nextLine().trim();
                        librarySystem.usersRemove(removeUser);
                        break;
                    case "help":
                        librarySystem.help();
                        break;
                    default:
                        System.out.println("Invalid command. Type 'help' for a list of commands.");
                        break;
                }
            } else {
                System.out.println("You need to log in first.");
            }
        }

        scanner.close();
    }
}
