package fr.d2factory.libraryapp.member;

public class Student  extends Member {
	
	private int year;
	
	//setting default wallet to 300 for testing purposes
	public Student () {
		this.maxLocationDays = 30;
		this.year = 1;
		this.setWallet(300);
	}
	
	public Student(int year) {
		this.maxLocationDays = 30;
		this.year = year;
		this.setWallet(300);
	}
	
	public void setYear(int year) {//to use when the student graduates, not used in the project
		this.year = year;
	}
	
	public int getYear() {
		return year;
	}
	
	@Override
	public void payBook(int numberOfDays) {
		//numberOfDays should not be negative
		if(year == 1)
		{
			if(numberOfDays > 15)
				if(numberOfDays <= 30)
					setWallet((float) (getWallet() - (0.1*(numberOfDays - 15))));
				else 
					setWallet((float) (getWallet() - (0.15*(numberOfDays-30) + (0.1*15))));
		}
		else
		{
			if(numberOfDays <= 30)
				setWallet((float) (getWallet() - (0.1*numberOfDays)));
			else 
				setWallet((float) (getWallet() - (0.15*(numberOfDays-30) + (0.1*30))));
		}
	}

}
