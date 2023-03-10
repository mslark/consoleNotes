import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Note[] notes = new Note[100];
        boolean flag = true;
        readFile(notes);
        showHelp();
        char choose;
        while (flag) {
            System.out.print("Enter command: ");
            choose = scan.next().charAt(0);
            switch (choose) {
                case 'q' -> {
                    flag = false;
                    writeFile(notes);
                    scan.close();
                }
                case 'v' -> viewNote(notes);
                case 'l' -> showList(notes);
                case 'n' -> createNote(notes, sizeNotes(notes));
                case 'e' -> editNote(notes);
                case 'd' -> deleteNote(notes);
                case 'h' -> showHelp();
                default -> {
                    System.out.println("unknown command!\n");
                    showHelp();
                }
            }
        }
    }

    static void readFile(Note[] notes) {
        try (BufferedReader reader = new BufferedReader(new FileReader("notes.txt"))) {
            getList(reader, notes);
        } catch (IOException e) {
            System.out.println("File for notes not found");
            System.exit(1);
        }
    }

    static void getList(BufferedReader reader, Note[] notes) throws IOException {
        int counter = 0;
        Arrays.fill(notes, null);
        String line = reader.readLine();
        try {
            while (line != null) {
                String[] temp = line.split(" ; ");
                Note tempNote = new Note(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), temp[2]);
                notes[counter] = tempNote;
                line = reader.readLine();
                counter++;
            }
        } catch (Exception e) {
            System.out.println("Invalid file format");
            System.exit(1);
        }
    }

    static void writeFile(Note[] notes) {
        int counter = 0;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("notes.txt"))) {
            while (notes[counter] != null) {
                if (notes[counter].isDeleted == 0) {
                    String line = notes[counter].noteId + " ; " + notes[counter].isDeleted + " ; " + notes[counter].content;
                    writer.write(line);
                    writer.newLine();
                }
                counter++;
                if (counter == notes.length) break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void showHelp() {
        String helpMessage = """
                +-----------------------+
                | q - quit              |
                | l - show notes list   |
                | n - create new note   |
                | v - view note by ID   |
                | e - edit note by ID   |
                | d - delete note by ID |
                | h - view this message |
                +-----------------------+""";
        System.out.println(helpMessage);
    }

    static void showList(Note[] notes) {
        int counter = 0;
        while (notes[counter] != null) {
            if (notes[counter].isDeleted == 0) {
                if (notes[counter].content.length() > 30) {
                    System.out.println("ID:" + notes[counter].noteId + " | " + notes[counter].content.substring(0, 29) + "...");
                } else {
                    System.out.println("ID:" + notes[counter].noteId + " | " + notes[counter].content);
                }
            }
            counter++;
            if (counter == notes.length) break;
        }
        if (sizeNotes(notes) == 0) {
            System.out.println("List is empty. Write something using command \"n\"");
        }
    }

    static void editNote(Note[] notes) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter note ID for edit: ");
        try {
            int editId = Integer.parseInt(scan.nextLine());
            int counter = 0;
            while (notes[counter].content != null && editId != notes[counter].noteId) {
                counter++;
            }
            System.out.println(buetyNote(notes[counter].content));
            String inputContent = scan.nextLine();
            while (inputContent.equals("")) {
                System.out.print("Empty note. Enter text of note: ");
                inputContent = scan.nextLine();
            }
            notes[counter].content = inputContent;
            writeFile(notes);
            System.out.println("Edited");
        } catch (Exception e) {
            System.out.println("Incorrect input");

        }
    }

    static void createNote(Note[] notes, int sizeNotes) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter your note: ");
        String inputContent = scan.nextLine();
        while (inputContent.equals("")) {
            System.out.print("Empty note. Enter text of note: ");
            inputContent = scan.nextLine();
        }
        try {
            notes[sizeNotes] = new Note(sizeNotes > 0 ? notes[sizeNotes - 1].noteId + 1 : 1, 0, inputContent);
            writeFile(notes);
            System.out.println("Created");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Cannot create new note\nMax notes count is: " + notes.length);
        }

    }

    static int sizeNotes(Note[] notes) {
        int counter = 0;
        while (notes[counter] != null) {
            counter++;
            if (counter == notes.length) break;
        }
        return counter;
    }

    static void deleteNote(Note[] notes) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter note ID for delete: ");
        int counter = 0;
        boolean flag = false;
        try {
            int deleteId = Integer.parseInt(scan.nextLine());
            while (notes[counter] != null) {
                if (notes[counter].noteId == deleteId) {
                    notes[counter].isDeleted = 1;
                    flag = true;
                }
                counter++;
                if (counter == notes.length) break;
            }
            if (!flag) {
                System.out.println("Note ID " + deleteId + " not found");
            } else {
                System.out.println("Deleted");
            }
            writeFile(notes);
            readFile(notes);
        } catch (Exception e) {
            System.out.println("Incorrect input");
        }
    }

    static void viewNote(Note[] notes) {
        Scanner scan = new Scanner(System.in);
        int counter = 0;
        boolean flag = false;
        System.out.print("Enter note ID for view: ");
        try {
            int viewId = Integer.parseInt(scan.nextLine());
            while (notes[counter] != null) {
                if (viewId == notes[counter].noteId) {
                    System.out.println(buetyNote(notes[counter].content));
                    flag = true;
                }
                counter++;
                if (counter == notes.length) break;
            }
            if (!flag) System.out.println("Note ID " + viewId + " note found");
        } catch (Exception e) {
            System.out.println("Incorrect input");
        }
    }

    static String buetyNote(String noteString) {
        String[] noteArr = noteString.split(" ");
        StringBuilder buetyString = new StringBuilder();
        for (int i = 0; i < noteArr.length; i++) {
            if (i % 5 != 0) {
                buetyString.append(noteArr[i]).append(" ");
            } else {
                if (i == 0) {
                    buetyString.append(noteArr[i]).append(" ");
                } else {
                    buetyString.append("\n").append(noteArr[i]).append(" ");
                }
            }
        }
        return buetyString.toString();
    }
}
