package com.fixflow;

import com.fixflow.cli.InputHandler;
import com.fixflow.cli.MainMenu;
import com.fixflow.repository.*;
import com.fixflow.service.*;
import com.fixflow.util.DataSeeder;

/**
 * Application entry point for FixFlow – Smart Repair Management System.
 * Starts the CLI application with JSON-file-based persistence.
 */
public class Main {

    public static void main(String[] args) {
        // Initialize shared repository instances for data consistency
        CustomerRepository customerRepo = new CustomerRepository();
        DeviceRepository deviceRepo = new DeviceRepository();
        TechnicianRepository technicianRepo = new TechnicianRepository();
        InventoryRepository inventoryRepo = new InventoryRepository();
        PartUsageRepository usageRepo = new PartUsageRepository();
        RepairRepository repairRepo = new RepairRepository();
        PaymentRepository paymentRepo = new PaymentRepository();
        UserRepository userRepo = new UserRepository();

        // Wire shared repositories into services
        CustomerService.setRepositories(customerRepo, deviceRepo, repairRepo);
        DeviceService.setRepositories(deviceRepo, customerRepo, repairRepo);
        TechnicianService.setRepositories(technicianRepo, repairRepo);
        InventoryService.setRepositories(inventoryRepo, usageRepo);
        RepairService.setRepositories(repairRepo, customerRepo, deviceRepo, technicianRepo);
        PaymentService.setRepositories(paymentRepo, repairRepo, customerRepo, deviceRepo);
        UserService.setRepository(userRepo);
        ReportService.setRepositories(customerRepo, deviceRepo, technicianRepo, inventoryRepo, repairRepo, paymentRepo);

        // Ensure data directory exists and seed initial demo data if empty
        DataSeeder.seedIfEmpty(
                userRepo,
                customerRepo,
                deviceRepo,
                technicianRepo,
                inventoryRepo,
                repairRepo,
                usageRepo,
                paymentRepo
        );

        // Initialize terminal input handler and launch main menu loop
        InputHandler inputHandler = new InputHandler();
        MainMenu mainMenu = new MainMenu(inputHandler);
        mainMenu.start();
    }
}
