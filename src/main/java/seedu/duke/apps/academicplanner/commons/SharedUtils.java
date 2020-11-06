package seedu.duke.apps.academicplanner.commons;

import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import java.util.Scanner;
import seedu.duke.apps.academicplanner.exceptions.AcademicException;
import seedu.duke.global.objects.PartialModule;
import seedu.duke.global.objects.Person;

//@@author Khenus

/**
 * A class that handles verification for the addition, edition and removal of duplicated entries.
 */
public class SharedUtils {
    private static final int FROM_EDIT = 2;
    private static final int FROM_REMOVE = 3;

    /**
     * Default constructor for SharedUtils.
     */
    public SharedUtils() {
        //This constructor is intentionally left blank
    }

    /**
     * Return all occurrences of a certain modules code.
     *
     * @param currentPerson Person object containing all user information
     * @param moduleCode String containing the current module code to be checked
     *
     * @return ArrayList of Partial Module containing information of all occurrences of the module code
     */
    public static ArrayList<PartialModule> getAllOccurrencesOfModule(Person currentPerson, String moduleCode) {
        ArrayList<PartialModule> modulesAddedList = currentPerson.getModulesList();
        ArrayList<Integer> occurrenceList = currentPerson.getModulesAddedMap().get(moduleCode);

        ArrayList<PartialModule> allOccurrencesOfModule = new ArrayList<>();

        if (occurrenceList == null) {
            return allOccurrencesOfModule;
        }

        for (int i = 0; i < occurrenceList.size(); i++) {
            allOccurrencesOfModule.add(modulesAddedList.get(occurrenceList.get(i)));
        }

        return allOccurrencesOfModule;
    }

    /**
     * Prints all occurrences of a specific module without index.
     *
     * @param allOccurrence ArrayList containing information of all occurrence of a specific module
     */
    public static void printAllOccurrencesOfModule(ArrayList<PartialModule> allOccurrence) {
        System.out.println("\nPrevious attempt(s) for " + allOccurrence.get(0).getModuleCode() + ":");
        System.out.println("\t Semester    Grade");

        for (int i = 0; i < allOccurrence.size(); i++) {
            int currentSemester = allOccurrence.get(i).getSemesterIndex();
            String currentGrade = allOccurrence.get(i).getGrade();
            String toPrint = String.format("\t    %02d        %s  ", currentSemester, currentGrade);

            System.out.println(toPrint);
        }
    }

    /**
     * Prints all occurrences of a specific module with index.
     *
     * @param currentPerson Person object containing all user information
     * @param moduleCode String containing the current module code to be checked
     */
    public static void printAllOccurrencesOfModuleToEdit(Person currentPerson, String moduleCode) {
        ArrayList<PartialModule> allOccurrence = getAllOccurrencesOfModule(currentPerson, moduleCode);

        System.out.println("\nPrevious occurrence(s) for " + allOccurrence.get(0).getModuleCode() + ":");
        System.out.println("\tIndex   Semester    Grade");

        for (int i = 0; i < allOccurrence.size(); i++) {
            int currentSemester = allOccurrence.get(i).getSemesterIndex();
            String currentGrade = allOccurrence.get(i).getGrade();
            String toPrint = String.format("\t  %d.      %02d        %s  ",  i + 1, currentSemester, currentGrade);

            System.out.println(toPrint);
        }
    }

    /**
     * Returns the index of ModuleMap to be edited.
     *
     * @param in Unified scanner class from UI
     * @param moduleCode Module code to be edited
     * @param currentPerson Information regarding current user
     * @param from Location in which this function is called
     *
     * @return int containing the index of ModuleMap to be edited
     *
     * @throws AcademicException Throws exception when the entered value is invalid
     */
    public static int getEntryToBeEdited(Scanner in, String moduleCode, Person currentPerson, int from)
            throws AcademicException {
        ArrayList<Integer> moduleIndexList = currentPerson.getModulesAddedMap().get(moduleCode);

        int indexToUpdate = 0;

        if (moduleIndexList.size() > 1) {
            if (from == FROM_EDIT) {
                System.out.print("Please enter the index of entry to be edited:");
            } else if (from == FROM_REMOVE) {
                System.out.print("Please enter the index of entry to be removed:");
            }

            printAllOccurrencesOfModuleToEdit(currentPerson, moduleCode);

            String entryToEdit = in.nextLine().trim();

            try {
                indexToUpdate = parseInt(entryToEdit) - 1;
            } catch (Exception e) {
                throw new AcademicException("The value you entered is not an integer!");
            }

            if (!(indexToUpdate >= 0 && indexToUpdate < moduleIndexList.size())) {
                throw new AcademicException("The index you entered does not exist within the list!");
            }
        }

        return indexToUpdate;
    }

    /**
     * Checks if the semester to be edited already exist.
     *
     * @param semesterValue The semester value the user wish to change
     * @param currentPerson Information of the current user
     * @param moduleCode Module code to be edited
     * @throws AcademicException Throws exception if the semester to be change already exists
     */
    public static void verifyRepeatedSemester(int semesterValue, Person currentPerson, String moduleCode)
            throws AcademicException {
        ArrayList<Integer> moduleIndexList = currentPerson.getModulesAddedMap().get(moduleCode);

        for (int i = 0; i < moduleIndexList.size(); i++) {
            if (semesterValue == moduleIndexList.get(i)) {
                String errorMessage = String.format("%s already exist in semester %d!", moduleCode, semesterValue);
                throw new AcademicException(errorMessage);
            }
        }
    }
}
