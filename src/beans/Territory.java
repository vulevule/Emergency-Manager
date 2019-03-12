package beans;

public class Territory {
	private String name;
	private int areaSize;
	private int residentsNumber;
	
	public Territory() {}
	
	public Territory(String name, int areaSize, int residentsNumber) {
		//super();
		this.name = name;
		this.areaSize = areaSize;
		this.residentsNumber = residentsNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAreaSize() {
		return areaSize;
	}

	public void setAreaSize(int areaSize) {
		this.areaSize = areaSize;
	}

	public int getResidentsNumber() {
		return residentsNumber;
	}

	public void setResidentsNumber(int residentsNumber) {
		this.residentsNumber = residentsNumber;
	}

	@Override
	public String toString() {
		return "Territory [name=" + name + ", areaSize=" + areaSize + ", residentsNumber=" + residentsNumber + "]";
	}
	
	
	
}
