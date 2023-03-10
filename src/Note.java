public class Note {
    int noteId;
    int isDeleted;
    String content;

    public Note(int noteId, int isDeleted, String content) {
        this.noteId = noteId;
        this.isDeleted = isDeleted;
        this.content = content;
    }
}
