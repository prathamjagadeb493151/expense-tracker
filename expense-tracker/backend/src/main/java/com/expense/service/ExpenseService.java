package com.expense.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expense.model.Expense;
import com.expense.repository.ExpenseRepository;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository repository;

    public List<Expense> getAllExpenses() {
        return repository.findAll();
    }

    public Expense addExpense(Expense expense) {
        if (expense.getDate() == null) {
            expense.setDate(new Date()); // Set current date
        }
        if (expense.getTime() == null) {
            expense.setTime(new SimpleDateFormat("HH:mm:ss").format(new Date())); // Store time as HH:mm:ss string
        }
        return repository.save(expense);
    }

    public void deleteExpense(String id) {
        repository.deleteById(id);
    }

    public Map<String, Object> getOverviewStats() {
        List<Expense> expenses = repository.findAll();
        Map<String, Object> stats = new HashMap<>();

        double totalSpending = expenses.stream().mapToDouble(Expense::getAmount).sum();
        double thisMonthSpending = expenses.stream()
            .filter(exp -> isInCurrentMonth(exp.getDate()))
            .mapToDouble(Expense::getAmount)
            .sum();

        double lastMonthSpending = expenses.stream()
            .filter(exp -> isInLastMonth(exp.getDate()))
            .mapToDouble(Expense::getAmount)
            .sum();

        Map<String, Double> categoryWiseSpending = expenses.stream()
            .collect(Collectors.groupingBy(Expense::getCategory, Collectors.summingDouble(Expense::getAmount)));

        Map<String, Double> cashFlow = calculateMonthlyCashFlow(expenses);

        stats.put("totalSpending", totalSpending);
        stats.put("thisMonthSpending", thisMonthSpending);
        stats.put("lastMonthSpending", lastMonthSpending);
        stats.put("categoryWiseSpending", categoryWiseSpending);
        stats.put("cashFlow", cashFlow);

        return stats;
    }

    private Map<String, Double> calculateMonthlyCashFlow(List<Expense> expenses) {
        Map<String, Double> monthlyCashFlow = new HashMap<>();
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM yyyy");

        expenses.forEach(exp -> {
            String monthKey = monthFormat.format(exp.getDate());
            monthlyCashFlow.put(monthKey, monthlyCashFlow.getOrDefault(monthKey, 0.0) + exp.getAmount());
        });

        return monthlyCashFlow;
    }

    private boolean isInCurrentMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH);
        int currentYear = cal.get(Calendar.YEAR);
        cal.setTime(date);
        return cal.get(Calendar.MONTH) == currentMonth && cal.get(Calendar.YEAR) == currentYear;
    }

    private boolean isInLastMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        int lastMonth = cal.get(Calendar.MONTH);
        int lastYear = cal.get(Calendar.YEAR);
        cal.setTime(date);
        return cal.get(Calendar.MONTH) == lastMonth && cal.get(Calendar.YEAR) == lastYear;
    }
}
