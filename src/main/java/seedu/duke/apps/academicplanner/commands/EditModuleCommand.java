package seedu.duke.apps.academicplanner.commands;

import seedu.duke.apps.academicplanner.commons.EditUtils;
import seedu.duke.apps.academicplanner.commons.ModuleValidator;
import seedu.duke.apps.academicplanner.exceptions.AcademicException;
import seedu.duke.apps.moduleloader.ModuleLoader;
import seedu.duke.global.Command;
import seedu.duke.global.objects.Person;
import seedu.duke.storage.Storage;
import seedu.duke.ui.Ui;
import java.util.Scanner;

//@@author harryleecp
/**
 * Class representing an edit module command from the academic planner.
 */
public class EditModuleCommand extends Command {
    private static final String EDIT_SEMESTER = "1";
    private static final String EDIT_GRADE = "2";
    private static final String ERROR_INVALID_MODULE = "The module you entered is invalid";
    private static final String ERROR_NOT_ADDED = "You have not added this module into your list yet";
    private static final String ERROR_EDIT_OPTION = "Number entered does not correspond to any feature";
    public static final String PROMPT_EDIT_CHOICES = "Enter the number corresponding to the feature you wish to edit:\n"
            + "\t1) Semester\n"
            + "\t2) Grade";

    private EditUtils editUtils;
    private ModuleValidator moduleValidator;
    private Person currentPerson;
    private Ui ui;
    private Scanner in;
    private String moduleCode;
    private Storage storage;

    public EditModuleCommand(ModuleLoader allModules, Person currentPerson, Ui ui, String moduleCode, Storage storage) {
        this.editUtils = new EditUtils(allModules, currentPerson);
        this.moduleValidator = new ModuleValidator(allModules, currentPerson);
        this.ui = ui;
        this.in = ui.getScanner();
        this.moduleCode = moduleCode;
        this.currentPerson = currentPerson;
        this.storage = storage;
    }

    /**
     * Allows user to edit the module entry of his academic calendar.
     * Parameters allowed to change are semesterIndex or Grade.
     */
    @Override
    public void execute() throws AcademicException {
        if (!moduleValidator.isModOfferedByNus(moduleCode)) {
            throw new AcademicException(ERROR_INVALID_MODULE);
        } else if (moduleValidator.isModTakenByUser(moduleCode)) {
            System.out.println(PROMPT_EDIT_CHOICES);
            String choice = in.nextLine().trim();

            if (choice.equals(EDIT_SEMESTER)) {
                editUtils.editModuleSemester(in, moduleCode);
                storage.saver(currentPerson);
            } else if (choice.equals(EDIT_GRADE)) {
                editUtils.editModuleGrade(in, moduleCode);
                storage.saver(currentPerson);
            } else {
                throw new AcademicException(ERROR_EDIT_OPTION);
            }
        } else {
            throw new AcademicException(ERROR_NOT_ADDED);
        }
    }
}
