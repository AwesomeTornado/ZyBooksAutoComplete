public class Main {
    public static void main(String[] args) {
        String myBookCode = "UMNCSCI1913PicoralFall2025";
        ZyBooksAutoComplete book = new ZyBooksAutoComplete(myBookCode, null);
        book.completeEverything();
    }
}
