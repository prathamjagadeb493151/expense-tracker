package com.expense.controller;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expense.model.Expense;
import com.expense.service.ExpenseService;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin("*") // Enable CORS for frontend
public class ExpenseController {
    @Autowired
    private ExpenseService service;

    @GetMapping
    public List<Expense> getExpenses() {
        return service.getAllExpenses();
    }

    @PostMapping
    public Expense addExpense(@RequestBody Expense expense) {
        if (expense.getDate() == null) {
            expense.setDate(new Date(0)); // Set current date if not provided
        }
        return service.addExpense(expense);
    }

    @DeleteMapping("/{id}")
    public void deleteExpense(@PathVariable String id) {
        service.deleteExpense(id);

    }
    @GetMapping("/overview")
    public Map<String, Object> getOverviewStats() {
        return service.getOverviewStats();
    }

   
}
