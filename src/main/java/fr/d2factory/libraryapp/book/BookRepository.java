package fr.d2factory.libraryapp.book;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.d2factory.libraryapp.member.Member;

/**
 * The book repository emulates a database via 2 HashMaps
 */
public class BookRepository {
    private Map<ISBN, Book> availableBooks;
    private Map<Book, LocalDate> borrowedBooks;
    //storing a list of books currently borrowed for each member having a book borrowed
    private Map<Member, List<Book>> borrowers;
    
    public BookRepository() {
    	availableBooks = new HashMap<>();
    	borrowedBooks = new HashMap<>();
    	borrowers = new HashMap<>();
    }

    public void addBooks(List<Book> books){
        for(Book book : books)
        	addBook(book);
    }
    
    //usefull for returning a single book
    public void addBook(Book book){
        availableBooks.put(book.isbn, book);
    }

    public Book findBook(long isbnCode) {
    	//search...
        for(ISBN isbn : availableBooks.keySet())
        	if(isbn.isbnCode == isbnCode)
        		return availableBooks.get(isbn);
        //return null if not found
        return null;
    }

    public void saveBookBorrow(Book book, LocalDate borrowedAt, Member member){
    	//adding the location "database"/memory
        borrowedBooks.put(book, borrowedAt);
        //removing the book from the available list
        availableBooks.remove(book.isbn);
        //adding the borrowed book in the list of the member's borrowed books
        if(borrowers.containsKey(member))
        	borrowers.get(member).add(book);
        else {
        	List<Book> tmp_books = new ArrayList<Book>();
        	tmp_books.add(book);
        	borrowers.put(member, tmp_books);
        }
    }
    
    public void returnBook(Book book, Member member) {
    	//the member has actually borrowed this book; so the "database" contains the fields of the considered book and member
    	borrowers.get(member).remove(book);
    	borrowedBooks.remove(book);
    	addBook(book);
    }

    public LocalDate findBorrowedBookDate(Book book) {
    	if(borrowedBooks.containsKey(book))
    		return borrowedBooks.get(book);
        //return null if not found
        return null;
    }
    
    public boolean isLate(Member member) {
    	if(borrowers.containsKey(member))
    		for(Book b : borrowers.get(member))
    			if( borrowedBooks.containsKey(b) && borrowedBooks.get(b).isBefore(LocalDate.now().minus(member.maxLocationDays, ChronoUnit.DAYS)) )
    				return true;
    	
    	return false;
    }
}
