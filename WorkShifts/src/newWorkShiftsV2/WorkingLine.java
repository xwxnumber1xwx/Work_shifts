package newWorkShiftsV2;

import java.util.ArrayList;

public class WorkingLine {
	private ArrayList <WeekShifts> shift = new ArrayList<WeekShifts>();
	/**
	 * @return the shift
	 */
	public ArrayList <WeekShifts> getShift() {
		return shift;
	}
	/**
	 * @param shift the shift to set
	 */
	public void setShift(ArrayList <WeekShifts> shift) {
		this.shift = shift;
	}

}