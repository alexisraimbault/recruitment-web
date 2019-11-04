package fr.d2factory.libraryapp.library;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.book.ISBN;
import fr.d2factory.libraryapp.member.Member;
import fr.d2factory.libraryapp.member.Resident;
import fr.d2factory.libraryapp.member.Student;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class LibraryTest {
    private Library library ;
    private BookRepository bookRepository;
    Member young_student1;
    Member young_student2;
    Member ancient_student1;
    Member ancient_student2;
    Member resident1;
    Member resident2;
    
    @Before
    public void setup(){
    	List<Book> books = new ArrayList<Book>();
    	books.add(new Book("Harry Potter", "J.K. Rowling", new ISBN(Long.parseLong("46578964513", 10))));
    	books.add(new Book("Around the world in 80 days", "Jules Verne", new ISBN(Long.parseLong("3326456467846", 10))));
    	books.add(new Book("Catch 22", "Joseph Heller", new ISBN(Long.parseLong("968787565445", 10))));
    	books.add(new Book("La peau de chagrin", "Balzac", new ISBN(Long.parseLong("465789453149", 10))));
    	//...
    	
    	bookRepository = new BookRepository();
    	
    	bookRepository.addBooks(books);
    	
    	library = new LibraryImpl(bookRepository);
    	
    	young_student1 = new Student();
    	young_student2 = new Student();
    	ancient_student1 = new Student(4);
    	ancient_student2 = new Student(5);
    	resident1 = new Resident();
    	resident2 = new Resident();
    }

    @Test
    public void member_can_borrow_a_book_if_book_is_available(){
    	long to_borrow = Long.parseLong("3326456467846", 10);//harry potter
        try {
        	library.borrowBook(to_borrow, young_student1, LocalDate.now());
        	assertTrue(true);
        }catch(HasLateBooksException e) {
        	assertTrue(false);
        }
    }

    @Test
    public void borrowed_book_is_no_longer_available(){
    	long to_borrow_twice = Long.parseLong("46578964513", 10);//harry potter
        library.borrowBook(to_borrow_twice, young_student1, LocalDate.now());
        assertTrue(bookRepository.findBook(to_borrow_twice) == null);
    }

    @Test
    public void residents_are_taxed_10cents_for_each_day_they_keep_a_book(){
    	long to_borrow = Long.parseLong("968787565445", 10);//Catch 22
    	LocalDate today = LocalDate.now();
    	LocalDate location_day = today.minusDays(10);//10 days ago
    	Book borrowed = bookRepository.findBook(to_borrow) ;
    	library.borrowBook(to_borrow, resident1, location_day);
    	library.returnBook(borrowed, resident1);
    	assertTrue(resident1.getWallet() == 299);//default wallet is 300, and 10*0.1 = 1 , so he should have 299 left
    	
    	long to_borrow2 = Long.parseLong("465789453149", 10);
    	LocalDate location_day2 = today.minusDays(52);//52 days ago
    	Book borrowed2 = bookRepository.findBook(to_borrow2) ;
    	library.borrowBook(to_borrow2, resident2, location_day2);
    	library.returnBook(borrowed2, resident2);
    	assertTrue(resident2.getWallet() == (float)294.8);//default wallet is 300, and 52*0.1 = 5.2, so he should have 294.8 left
    }

    @Test
    public void students_pay_10_cents_the_first_30days(){
    	long to_borrow = Long.parseLong("968787565445", 10);//Catch 22
    	LocalDate today = LocalDate.now();
    	LocalDate location_day = today.minusDays(10);//10 days ago
    	Book borrowed = bookRepository.findBook(to_borrow) ;
    	library.borrowBook(to_borrow, ancient_student1, location_day);
    	library.returnBook(borrowed, ancient_student1);
    	assertTrue(ancient_student1.getWallet() == 299);//default wallet is 300, and 10*0.1 = 1 , so he should have 299 left
    	
    	long to_borrow2 = Long.parseLong("465789453149", 10);
    	LocalDate location_day2 = today.minusDays(22);//22 days ago
    	Book borrowed2 = bookRepository.findBook(to_borrow2) ;
    	library.borrowBook(to_borrow2, ancient_student1, location_day2);
    	library.returnBook(borrowed2, ancient_student1);
    	assertTrue(ancient_student1.getWallet() == (float)296.8);//default wallet is 300, but he already used 1, and 22*0.1 = 2.2, so he should have 296.8 left
    }

    @Test
    public void students_in_1st_year_are_not_taxed_for_the_first_15days(){
    	long to_borrow = Long.parseLong("968787565445", 10);//Catch 22
    	LocalDate today = LocalDate.now();
    	LocalDate location_day = today.minusDays(10);//10 days ago
    	Book borrowed = bookRepository.findBook(to_borrow) ;
    	library.borrowBook(to_borrow, young_student1, location_day);
    	library.returnBook(borrowed, young_student1);
    	assertTrue(young_student1.getWallet() == 300);//wallet should be full, so default = 300
    	
    	long to_borrow2 = Long.parseLong("465789453149", 10);
    	LocalDate location_day2 = today.minusDays(14);//14 days ago
    	Book borrowed2 = bookRepository.findBook(to_borrow2) ;
    	library.borrowBook(to_borrow2, young_student1, location_day2);
    	library.returnBook(borrowed2, young_student1);
    	assertTrue(young_student1.getWallet() == 300);//wallet should be full, so default = 300
    }

    @Test
    public void students_pay_15cents_for_each_day_they_keep_a_book_after_the_initial_30days(){
    	long to_borrow = Long.parseLong("968787565445", 10);//Catch 22
    	LocalDate today = LocalDate.now();
    	LocalDate location_day = today.minusDays(44);//44 days ago
    	Book borrowed = bookRepository.findBook(to_borrow) ;
    	library.borrowBook(to_borrow, ancient_student1, location_day);
    	library.returnBook(borrowed, ancient_student1);
    	assertTrue(ancient_student1.getWallet() == (float)(300 - ( 0.1*30 + 0.15*14 ) ));//default wallet is 300
    	
    	long to_borrow2 = Long.parseLong("465789453149", 10);
    	LocalDate location_day2 = today.minusDays(44);//44 days ago
    	Book borrowed2 = bookRepository.findBook(to_borrow2) ;
    	library.borrowBook(to_borrow2, young_student1, location_day2);
    	library.returnBook(borrowed2, young_student1);
    	assertTrue(young_student1.getWallet() == (float)(300 - (  0.1*15 + 0.15*14 ) ));//default wallet is 300, and he has the 15 firsts days free.
    }

    @Test
    public void residents_pay_20cents_for_each_day_they_keep_a_book_after_the_initial_60days(){
    	long to_borrow = Long.parseLong("968787565445", 10);//Catch 22
    	LocalDate today = LocalDate.now();
    	LocalDate location_day = today.minusDays(65);//62 days ago
    	Book borrowed = bookRepository.findBook(to_borrow) ;
    	library.borrowBook(to_borrow, resident1, location_day);
    	library.returnBook(borrowed, resident1);
    	assertTrue(resident1.getWallet() == (float)(300 - (0.1*60 + 0.2*5)));//applying the formula
    	
    	long to_borrow2 = Long.parseLong("465789453149", 10);
    	LocalDate location_day2 = today.minusDays(72);//72 days ago
    	Book borrowed2 = bookRepository.findBook(to_borrow2) ;
    	library.borrowBook(to_borrow2, resident2, location_day2);
    	library.returnBook(borrowed2, resident2);
    	assertTrue(resident2.getWallet() == (float)(300 - (0.1*60 + 0.2*12)));//applying the formula
    }

    @Test
    public void members_cannot_borrow_book_if_they_have_late_books(){
    	LocalDate today = LocalDate.now();
    	long to_borrow = Long.parseLong("465789453149", 10);
    	LocalDate location_day1 = today.minusDays(72);//72 days ago
    	library.borrowBook(to_borrow, resident2, location_day1);//location
    	
    	long to_borrow2 = Long.parseLong("3326456467846", 10);
        try {
        	library.borrowBook(to_borrow2, resident2, LocalDate.now());//shouldn't be able to borrow again
        	assertTrue(false);
        }catch(HasLateBooksException e) {
        	assertTrue(true);
        }
        
    	long to_borrow3 = Long.parseLong("968787565445", 10);
    	LocalDate location_day2 = today.minusDays(36);//36 days ago, late for a student
    	library.borrowBook(to_borrow3, young_student1, location_day2);//location
    	
    	long to_borrow4 = Long.parseLong("46578964513", 10);
        try {
        	library.borrowBook(to_borrow4, young_student1, LocalDate.now());//shouldn't be able to borrow again
        	assertTrue(false);
        }catch(HasLateBooksException e) {
        	assertTrue(true);
        }
    }
}
