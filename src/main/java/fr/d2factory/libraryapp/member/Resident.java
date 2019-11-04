package fr.d2factory.libraryapp.member;

public class Resident extends Member {
	
	//setting default wallet to 300 for testing purposes
	public Resident() {
		this.maxLocationDays = 60;
		this.setWallet(300);
	}

	@Override
	public void payBook(int numberOfDays) {
		//numberOfDays should not be negative
		if(numberOfDays <= 60)
			setWallet((float) (getWallet() - (0.1*numberOfDays)));//doing 0.1*numberOfDays and not the opposite to avoid Integer multiplication
		else 
			setWallet((float) (getWallet() - (0.2*(numberOfDays - 60) + (0.1*60))));//0.1*60 = 6, but I'll let it like that so it's more understandable
	}

}
