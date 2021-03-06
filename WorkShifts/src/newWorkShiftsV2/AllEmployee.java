package newWorkShiftsV2;

import java.util.ArrayList;
import com.google.gson.Gson;

public class AllEmployee {
	private ArrayList <Employee> allEmployee = new ArrayList<Employee>();

	/**
	 * @return the allEmployee
	 */
	public ArrayList <Employee> getAllEmployee() {
		return allEmployee;
	}

	/**
	 * @param allEmployee the allEmployee to set
	 */
	public void setEmployee(ArrayList <Employee> allEmployee) {
		this.allEmployee = allEmployee;
	}
	
	public void setOneEmployee(Employee employee) {
		this.allEmployee.add(employee);
	}
	
	public Employee findEmployeeFromID (long id) {
		for (int x = 0; x < allEmployee.size(); x++) {
			Employee employee = allEmployee.get(x);
			if (employee.getEmployeeID() == id) {
				return employee;
			}
				
		}
		return null;
	}
	
}
