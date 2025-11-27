package com.expense.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "expenses")
public class Expense {

    @Id
    private String id;
    private String title;
    private double amount;
    private String category;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date date; // Date field

    private String time; // Change time to String (HH:mm:ss format)

    // Constructors
    public Expense() {}

    public Expense(String title, double amount, String category, Date date) {
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.time = new SimpleDateFormat("HH:mm:ss").format(new Date()); // Set current time in HH:mm:ss format
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
}
