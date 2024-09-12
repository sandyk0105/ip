package rei;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

/**
 * This class encapsulates the parse method that takes the user prompt as its input
 */
public class Parser {
    /**
     * Different prompt types REI understands
     */
    public enum Prompt {LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, ANNYEONG, UNKNOWN};

    /**
     * Checks if a string only contains whitespace
     * @param input the string to be checked
     * @return true if the string only contains whitespace, false otherwise
     */
    public static boolean isAllWhitespace(String input) {
        return input.replaceAll("\\s+","").isEmpty();
    }

    /**
     * Parses a given user input
     * @param tasks current list of tasks
     * @param prompt the user input
     * @return a Prompt type
     */
    public static Prompt parse(TaskList tasks, String prompt) {

        List<String> prompts = Arrays.asList(prompt.split(" "));
        int id;
        switch (prompts.get(0)) {
            case "list":
                return Prompt.LIST;
            case "mark":
                // Read the rest of the line after "mark"
                prompt = prompt.substring(4).trim();

                // Check if the rest of the line is an integer
                if (prompt.isEmpty() || !prompt.matches("\\d+")) {
                    Ui.print("State the task number.");
                    return Prompt.UNKNOWN;
                }

                return Prompt.MARK;
            case "unmark":
                // Read the rest of the line after "unmark"
                prompt = prompt.substring(6).trim();

                // Check if the rest of the line is an integer
                if (prompt.isEmpty() || !prompt.matches("\\d+")) {
                    Ui.print("State the task number.");
                    return Prompt.UNKNOWN;
                }

                return Prompt.UNMARK;
            case "todo":
                if (isAllWhitespace(prompt.substring(4))) {
                    Ui.print("Task is empty. Please state the task name.");
                    return Prompt.UNKNOWN;
                }

                return Prompt.TODO;
            case "deadline":
                if (isAllWhitespace(prompt.substring(8))) {
                    Ui.print("Task is empty. Please state the task and deadline.");
                    return Prompt.UNKNOWN;
                } else if (prompt.indexOf("/by") == -1) {
                    Ui.print("When is the deadline? Please state the task with the deadline.");
                    return Prompt.UNKNOWN;
                } else if (isAllWhitespace(prompt.substring(8, prompt.indexOf("/by")))) {
                    Ui.print("Task name is empty. Please state the task and deadline.");
                    return Prompt.UNKNOWN;
                }

                try {
                    LocalDateTime.parse(prompt.substring(prompt.indexOf("/by") + 4));
                } catch (DateTimeParseException e) {
                    Ui.print("Wrong date format : YYYY-MM-DDTHH:MM \n For example, 2024-09-12T18:00");
                    return Prompt.UNKNOWN;
                }
                return Prompt.DEADLINE;
            case "event":
                if (isAllWhitespace(prompt.substring(5))) {
                    Ui.print("Event is empty. Please state the event and time range.");
                    return Prompt.UNKNOWN;
                } else if (prompt.indexOf("/from") == -1 || prompt.indexOf("/to") == -1) {
                    Ui.print("State the START and FINISH time of the event");
                    return Prompt.UNKNOWN;
                } else if (isAllWhitespace(prompt.substring(5, prompt.indexOf("/from")))) {
                    Ui.print("Task name is empty. Please state the task and event time.");
                    return Prompt.UNKNOWN;
                }

                try {
                    LocalDateTime.parse(prompt.substring(prompt.indexOf("/from") + 6, prompt.indexOf("/to") - 1));
                    LocalDateTime.parse(prompt.substring(prompt.indexOf("/to") + 4));
                } catch (DateTimeParseException e) {
                    Ui.print("Wrong date format : YYYY-MM-DDTHH:MM \n For example, 2024-09-12T18:00");
                    return Prompt.UNKNOWN;
                }
                return Prompt.EVENT;
            case "delete":
                // Read the rest of the line after "delete"
                prompt = prompt.substring(6).trim();

                // Check if the rest of the line is an integer
                if (prompt.isEmpty() || !prompt.matches("\\d+")) {
                    Ui.print("State the task number.");
                    return Prompt.UNKNOWN;
                }

                id = Integer.parseInt(prompt);
                tasks.deleteTask(id);
                return Prompt.DELETE;
            case "annyeong":
                Ui.print("Annyeong. Hope to see you soon.");
                return Prompt.ANNYEONG;
            default:
                Ui.print("I don't understand what you want me to do.");
                return Prompt.UNKNOWN;
        }
    }

}
