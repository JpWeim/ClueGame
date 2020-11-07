package clueGame;

public class Room {
	private String name;
	private BoardCell centerCell;
	private BoardCell labelCell;
	
	public Room() {
		
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	public void setLabelCell(BoardCell cell) {
		labelCell = cell;
	}
	public BoardCell getLabelCell() {
		return labelCell;
	}

	public void setCenterCell(BoardCell cell) {
		centerCell = cell;
	}
	public BoardCell getCenterCell() {
		return centerCell;
	}

	
}
