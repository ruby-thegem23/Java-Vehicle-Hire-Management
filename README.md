# 🚗 Vehicle Hire Management System

A Java coursework project developed for CSC8014 at Newcastle University.
Achieved a grade of **91.5 / 100**.

---

## 📝 Project Overview

A structured object-oriented system for managing a vehicle hire company.
The system handles vehicle fleets, customer records, and hire contracts
while enforcing real-world business rules around eligibility, service requirements, and inspections.

---

## ✨ Features

- **Vehicle Fleet Management** — Add and track cars and vans with unique auto-generated IDs
- **Customer Records** — Register customers with immutable records and duplicate detection
- **Hire Contracts** — Enforce eligibility rules before hiring any vehicle
- **Vehicle Return** — Update mileage, trigger servicing, and clear inspection flags on return
- **Service Tracking** — Automatically detect when a vehicle is due for service (10,000 miles for cars, 5,000 for vans)
- **Van Inspection** — Flag vans for inspection when hired for 10+ days; clears on return
- **Unmodifiable Collections** — Safe read-only access to customer hire records

---

## 📐 Design Highlights

- **Interface-based hierarchy** — `Vehicle` interface implemented via `AbstractVehicle`, extended by `Car` and `Van`
- **Factory method** — `AbstractVehicle.getInstance()` controls object creation
- **Unique ID generation** — `VehicleID` guarantees no two vehicles share the same ID (even numbers for cars, odd for vans)
- **Immutability** — `CustomerRecord` and `Name` are fully immutable; `Date` fields use defensive copying
- **Defensive programming** — Null checks, validation, and `IllegalArgumentException` throughout
- **Javadoc** — All public classes and methods documented

---

## 🛠️ Tech Stack

- Java (OOP, Collections Framework, Interfaces, Inheritance)
- Custom test framework (`Assertions` class)
- No external libraries

---

## 📁 Project Structure

```
src/
├── AbstractVehicle.java     # Abstract base class with shared vehicle logic
├── Vehicle.java             # Vehicle interface
├── Car.java                 # Car implementation (service every 10,000 miles)
├── Van.java                 # Van implementation (service every 5,000 miles, inspection)
├── VehicleID.java           # Unique ID generator for vehicles
├── CustomerRecord.java      # Immutable customer record
├── Name.java                # Immutable name class
├── VehicleManager.java      # Core management class (hire, return, fleet tracking)
└── test/
    ├── VehicleManagerTest.java     # Tests for VehicleManager
    └── CustomerRecordTest.java     # Tests for CustomerRecord
```

---

## ✅ Testing

Tests cover normal cases, boundary conditions, and exceptional cases including:

- Vehicle availability before and after hire/return
- Age and licence eligibility rules
- Maximum 3 vehicles per customer
- Van inspection flag triggered at 10+ day hire
- Service mileage thresholds (cars: 10,000 / vans: 5,000)
- Immutability of `CustomerRecord` via defensive copy testing
- Unmodifiable collection enforcement
- Exception handling for invalid inputs

---

## 🏫 Academic Context

Developed as assessed coursework for **CSC8014 — Software Development with Java**
at Newcastle University (February 2026).

> *AI tools were used only for brainstorming and debugging, in accordance with the module policy.*
