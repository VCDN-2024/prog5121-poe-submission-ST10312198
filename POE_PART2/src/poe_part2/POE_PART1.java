package poe_part2;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class POE_PART1 {
    private static Login loginInstance;
    private static boolean loggedIn = false;
    private static List<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        boolean exit = false;
        while (!exit) {
            // Display menu options
            String[] options = {"Register", "Login", "Exit"};
            int choice = JOptionPane.showOptionDialog(null, "Select an option:", "Login System", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            switch (choice) {
                case 0: // Register
                    registerUser();
                    break;
                case 1: // Login
                    loginUser();
                    break;
                case 2: // Exit
                    exit = true;
                    JOptionPane.showMessageDialog(null, "Exiting the program.");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Invalid choice.");
            }

            if (loggedIn) {
                displayMainMenu();
            }
        }
    }

    private static void registerUser() {
        String regUsername = readInput("Enter username:");
        String regPassword = readInput("Enter password:");

        Registration registration = new Registration(regUsername, regPassword);
        String registrationStatus = registration.registerUser();
        JOptionPane.showMessageDialog(null, "Registration status: " + registrationStatus);
    }

    private static void loginUser() {
        String enteredUsername = readInput("Enter username:");
        String enteredPassword = readInput("Enter password:");

        // Create a Login object with the entered username and password
        loginInstance = new Login(enteredUsername, enteredPassword);
        boolean loginResult = loginInstance.verifyLogin(enteredUsername, enteredPassword);
        String loginStatus = loginInstance.returnLoginStatus(loginResult);
        JOptionPane.showMessageDialog(null, loginStatus);

        loggedIn = loginResult;
    }

    private static void displayMainMenu() {
        JOptionPane.showMessageDialog(null, "Welcome to EasyKanban");

        boolean quit = false;
        while (!quit) {
            String[] options = {"Add tasks", "Show report", "Search tasks", "Delete task", "Quit"};
            int choice = JOptionPane.showOptionDialog(null, "Select an option:", "Main Menu", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            switch (choice) {
                case 0: // Add tasks
                    addTasks();
                    break;
                case 1: // Show report
                    showReport();
                    break;
                case 2: // Search tasks
                    searchTasks();
                    break;
                case 3: // Delete task
                    deleteTask();
                    break;
                case 4: // Quit
                    quit = true;
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Invalid choice.");
            }
        }
    }

    private static void addTasks() {
        int numTasks = Integer.parseInt(readInput("How many tasks do you want to enter?"));

        for (int i = 0; i < numTasks; i++) {
            String taskName = readInput("Enter task name:");
            String taskDescription = readInput("Enter task description:");
            String developerDetails = readInput("Enter developer details:");
            int taskDuration = Integer.parseInt(readInput("Enter task duration in hours:"));
            String[] statusOptions = {"To Do", "Done", "Doing"};
            int statusChoice = JOptionPane.showOptionDialog(null, "Select task status:", "Task Status", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, statusOptions, statusOptions[0]);
            String taskStatus = statusOptions[statusChoice];

            Task task = new Task(taskName, tasks.size(), taskDescription, developerDetails, taskDuration, taskStatus);
            tasks.add(task);

            if (task.checkTaskDescription()) {
                JOptionPane.showMessageDialog(null, "Task successfully captured");
            } else {
                JOptionPane.showMessageDialog(null, "Please enter a task description of less than 50 characters");
            }

            JOptionPane.showMessageDialog(null, task.printTaskDetails());
        }

        int totalHours = tasks.stream().mapToInt(Task::getTaskDuration).sum();
        JOptionPane.showMessageDialog(null, "Total hours across all tasks: " + totalHours);
    }

    private static void showReport() {
        StringBuilder report = new StringBuilder("Task Report:\n");
        for (Task task : tasks) {
            report.append(task.printTaskDetails()).append("\n\n");
        }
        JOptionPane.showMessageDialog(null, report.toString());
    }

    private static void searchTasks() {
        String[] options = {"By Task Name", "By Developer"};
        int choice = JOptionPane.showOptionDialog(null, "Search tasks by:", "Search Tasks", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        switch (choice) {
            case 0: // By Task Name
                String taskName = readInput("Enter task name:");
                for (Task task : tasks) {
                    if (task.getTaskName().equalsIgnoreCase(taskName)) {
                        JOptionPane.showMessageDialog(null, task.printTaskDetails());
                        return;
                    }
                }
                JOptionPane.showMessageDialog(null, "Task not found.");
                break;
            case 1: // By Developer
                String developerName = readInput("Enter developer name:");
                for (Task task : tasks) {
                    if (task.getDeveloperDetails().equalsIgnoreCase(developerName)) {
                        JOptionPane.showMessageDialog(null, task.printTaskDetails());
                    }
                }
                break;
            default:
                JOptionPane.showMessageDialog(null, "Invalid choice.");
        }
    }

    private static void deleteTask() {
        String taskName = readInput("Enter task name to delete:");
        tasks.removeIf(task -> task.getTaskName().equalsIgnoreCase(taskName));
        JOptionPane.showMessageDialog(null, "Task '" + taskName + "' deleted successfully.");
    }

    // Helper method to read input using JOptionPane
    private static String readInput(String message) {
        return JOptionPane.showInputDialog(message);
    }
}
