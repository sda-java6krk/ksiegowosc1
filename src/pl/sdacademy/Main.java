package pl.sdacademy;

import com.sun.org.apache.xpath.internal.SourceTree;
import pl.sdacademy.controllers.CompanyController;
import pl.sdacademy.exceptions.AccountantNotFoundException;
import pl.sdacademy.exceptions.AdminNotFoundException;
import pl.sdacademy.models.Accountant;
import pl.sdacademy.models.AccountantRegistry;
import pl.sdacademy.models.Admin;
import pl.sdacademy.models.AdminRegistry;

import java.util.Scanner;

public class Main {

    public enum State {
        INIT,
        LOGGING_IN_AS_ADMIN,
        LOGGING_IN_AS_ACCT,
        LOGGED_IN,
        CREATING_COMPANY,
        EXIT,
    }

    public static void main(String[] args) {
	    State state = State.INIT;
        Scanner scanner = new Scanner(System.in);

        Admin currentAdmin = null;

        Accountant currentAcct = null;

        while (state != State.EXIT) {
            switch(state) {
                case INIT: {
                    System.out.println("Dzień dobry, co chcesz zrobić?");
                    System.out.println(" 1 - zalogować się jako admin");
                    System.out.println(" 2 - zalogować się jako księgowy");
                    System.out.println(" 0 - wyjść z programu");

                    switch (scanner.nextInt()) {
                        case 1:
                            state = State.LOGGING_IN_AS_ADMIN;
                            scanner.nextLine();
                            break;

                        case 2:
                            state = State.LOGGING_IN_AS_ACCT;
                            scanner.nextLine();
                            break;

                        case 0:
                            state = State.EXIT;
                            scanner.nextLine();
                            break;

                        default:
                            System.out.println("Zła odpowiedź");
                            state = State.INIT;
                            scanner.nextLine();
                            break;
                    }
                    break;
                }

                case LOGGING_IN_AS_ADMIN: {
                    System.out.println("Podaj login:");
                    String login = scanner.nextLine();

                    System.out.println("Podaj hasło:");
                    String password = scanner.nextLine();

                    try {
                        currentAdmin = AdminRegistry.getInstance().findAdmin(login, password);
                        System.out.println("Dzień dobry " + currentAdmin.getLogin());
                        state = State.LOGGED_IN;

                    } catch (AdminNotFoundException e) {
                        System.out.println("Zły login lub hasło");
                        state = State.INIT;
                    }
                    break;
                }

                case LOGGING_IN_AS_ACCT: {
                    System.out.println("Podaj login:");
                    String login = scanner.nextLine();

                    System.out.println("Podaj hasło:");
                    String password = scanner.nextLine();

                    try {
                        currentAcct = AccountantRegistry.getInstance().findAccountant(login, password);
                        System.out.println("Dzień dobry " + currentAcct.getLogin());
                        state = State.LOGGED_IN;
                        // FIXME poprawic funkcje ksiegowego po zalogowaniu

                    } catch (AccountantNotFoundException e) {
                        System.out.println("Zły login lub hasło");
                        state = State.INIT;
                    }
                    break;
                }

                case LOGGED_IN: {
                    System.out.println("Co chcesz zrobić?");
                    System.out.println(" 1 - wypisać wszystkie firmy");
                    System.out.println(" 2 - dodać firmę");
                    System.out.println(" 0 - wyjść z programu");

                    switch (scanner.nextInt()) {
                        case 1:
                            CompanyController.listCompanies();
                            state = State.LOGGED_IN;
                            scanner.nextLine();
                            break;

                        case 2:
                            state = State.CREATING_COMPANY;
                            scanner.nextLine();
                            break;

                        case 0:
                            state = State.EXIT;
                            scanner.nextLine();
                            break;

                        default:
                            System.out.println("Zła odpowiedź");
                            state = State.INIT;
                            scanner.nextLine();
                            break;
                    }
                    break;
                }

                case CREATING_COMPANY: {
                    System.out.println("Podaj nazwę nowej firmy:");
                    String name = scanner.nextLine();

                    System.out.println("Podaj rok założenia nowej firmy:");
                    int yearFound = scanner.nextInt();
                    scanner.nextLine();

                    CompanyController.createCompany(name, yearFound);

                    state = State.LOGGED_IN;
                    break;
                }
            }
        }
    }
}
