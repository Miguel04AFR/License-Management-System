# Drivers License Management System  

A database system for managing driver's licenses, exams, and traffic violations.  

## Project Overview  
This project is a Driver's License Management System designed for a licensing center to efficiently handle:  
- Driver information (personal data, license status)  
- License issuance & renewals (types, expiration dates, restrictions)  
- Exams (theoretical, practical, medical)  
- Traffic violations (fines, points deducted, payment status)  
- Reports & analytics (license expirations, violation trends)  

Built with PostgreSQL for database design and Java for application logic.  

---

## Key Features  
- CRUD Operations - Add, update, and delete drivers, licenses, exams, and violations.  
- Automated Workflows - License status updates, point deductions for violations.  
- Exam Tracking - Link exams to drivers and external entities (clinics/driving schools).  
- Reporting - Generate PDF/Excel reports on licenses, violations, and pending renewals.  
- User Roles - Admin, License Manager, Examiner, and Citizen access levels.  

---

## Database Schema  
The system follows a 3NF (Third Normal Form) relational database design.  

Main Entities:  
- Drivers - Personal details, license status.  
- Licenses - Type, issue/expiry dates, restrictions.  
- Exams - Theory, practical, medical tests.  
- Violations - Type, date, penalty points, payment status.  
- External Entities - Clinics & driving schools.  

ER Diagram:  
https://i.imgur.com/uWKGbZK.jpeg
--- 

## Project Structure  
//

## Contributing  
Open to contributions!  
1. Fork the repository.  
2. Create a new branch (git checkout -b feature/your-feature).  
3. Commit changes (git commit -m 'Add feature').  
4. Push to the branch (git push origin feature/your-feature).  
5. Open a Pull Request.  
