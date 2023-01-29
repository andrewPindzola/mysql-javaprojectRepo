package projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectsService;

public class Projects {
	private Scanner scanner = new Scanner(System.in);
	private ProjectsService projectsService = new ProjectsService();
	private Project curProject;
	// @formatter:off
	private List<String> operations = List.of(
		"1) Create and populate all tables",
		"2) Add a project",
		"3) List projects",
		"4) Select current project"
	);
	// @formatter:on
	public static void main(String[] args) {
	new Projects().displayMenu();

	}
	private void displayMenu() {
		boolean done = false;
		while(!done) {
			
			
			try {
			int operation = getOperation();
			switch(operation) {
			case -1:
				done = exitMenu();
			break;
			
			case 1:
				createTables();
				break;
				
				default:
					System.out.println("\n" + operation + "is not valid. Try again.");
					break;
					
				case 2:
					addProject();
					break;
					
				case 3:
					listProjects();
					break;
					
				case 4:
					selectProject();
					break;
			}
			} catch(Exception e) {
				System.out.println("\nError: " + e.toString() + " Try again.");
				e.printStackTrace();
			}
		}
	}
	private void selectProject() {
		listProjects();
		Integer projectId = getIntInput("Enter a project ID to select a project");
		
		curProject = null;
		
		curProject = projectsService.fetchProjectById(projectId);
		
	}
	private void listProjects() {
		List<Project> projects = projectsService.fetchAllProjects();
		
		System.out.println("\nProjects:");
		
		projects.forEach(project -> System.out.println("  " + project.getProjectId() 
		+ ": " + project.getProjectName()));
		
	}
	public void printOperations() {
	System.out.println("\nThese are the available selections. Press the Enter key to quit:");
	System.out.println();
	System.out.println("Here is what you can do: ");
	operations.forEach(op -> System.out.println("   " + op));
	operations.forEach(line -> System.out.println("  " + line));
		if (Objects.isNull(curProject)) {
			System.out.println("\nYou are not working with a project");
		} else {
			System.out.println("\nYou are working with project: " + curProject);
		}
	}
	
	
	private void addProject() {
		String projectName = getStringInput("Enter the project name");
		BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours");
		BigDecimal actualHours = getDecimalInput("Enter the actual hours");
		Integer difficulty = getIntInput("Enter the project difficulty (1-5)");
		String notes = getStringInput("Enter the project notes");
		
		Project project = new Project();
		
		project.setProjectName(projectName);
		project.setEstimatedHours(estimatedHours);
		project.setActualHours(actualHours);
		project.setDifficulty(difficulty);
		project.setNotes(notes);
		
		Project dbProject = projectsService.addProject(project);
		System.out.println("You have successfully created project: " + dbProject);
	}
	private BigDecimal getDecimalInput(String prompt) {
		String input = getStringInput(prompt);
		if (Objects.isNull(input)) {
			return null;
		}
		try {
			return new BigDecimal(input).setScale(2);
		}
		catch(NumberFormatException e) {
			throw new DbException (input + " is not a valid decimal number.");
		}
	}
	
	private void createTables() {
		projectsService.createAndPopulateTables();
		System.out.println("\nTables created and populated!");
		
	}
	private boolean exitMenu() {
		System.out.println("\nExiting the menu. TTFN!");
		return true;
	}
	private int getOperation() {
		printOperations();
		
		Integer op = getIntInput("\nEnter an operation number (press Enter to quit");
		
		return Objects.isNull(op) ? -1 : op;
	}
	
	
	private Integer getIntInput(String prompt) {
		String input = getStringInput(prompt);
		
		if(Objects.isNull(input)) {
			return null;
		} try {
			return Integer.parseInt(input);
		}
		catch(NumberFormatException e) {
			throw new DbException(input + " is not a valid number.");
		}
	}
	private Double getDoubleInput(String prompt) {
		String input = getStringInput(prompt);
		
		if(Objects.isNull(input)) {
			return null;
		} try {
			return Double.parseDouble(input);
		}
		catch(NumberFormatException e) {
			throw new DbException(input + " is not a valid number.");
		}
	}
	private String getStringInput(String prompt) {
		System.out.print(prompt + ": ");
		String line = scanner.nextLine();
		
		return line.isBlank() ? null : line.trim();
	}

}
