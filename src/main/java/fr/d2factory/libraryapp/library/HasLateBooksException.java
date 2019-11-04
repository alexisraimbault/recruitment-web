package fr.d2factory.libraryapp.library;

/**
 * This exception is thrown when a member who owns late books tries to borrow another book
 */
public class HasLateBooksException extends RuntimeException {
	
	public HasLateBooksException() {
		super("this member has some un-returned books, he cannot borrow again !");
	}
}
