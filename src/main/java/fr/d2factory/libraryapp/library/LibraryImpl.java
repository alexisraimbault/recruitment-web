package fr.d2factory.libraryapp.library;

import java.time.LocalDate;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.member.Member;
import java.time.temporal.ChronoUnit;

public class LibraryImpl implements Library{
	
	public BookRepository book_repository;
	
	public LibraryImpl(BookRepository book_repository) {
		this.book_repository = book_repository;
	}

	@Override
	public Book borrowBook(long isbnCode, Member member, LocalDate borrowedAt) throws HasLateBooksException {
		if(book_repository.isLate(member))
			throw new HasLateBooksException();
		else {
			Book book = book_repository.findBook(isbnCode);
			
			if(book != null)
				book_repository.saveBookBorrow(book, borrowedAt, member);
			return book;
		}
	}

	@Override
	public void returnBook(Book book, Member member) {
		int number_of_days = (int) ChronoUnit.DAYS.between(book_repository.findBorrowedBookDate(book), (LocalDate.now()));
		member.payBook(number_of_days);
		book_repository.returnBook(book, member);
	}

}
