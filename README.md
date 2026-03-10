# RAF Event Booker: Event Advertising and Review Platform

## Project Overview
RAF Event Booker is a comprehensive online platform designed for managing and discovering events. The system consists of two primary modules: an Event Management System (EMS) for authorized creators and administrators, and a Public Event Platform for general visitors. The project focuses on real-time event tracking, user interactions through comments and reactions, and secure administrative control.

---

## System Architecture
The platform is built as a full-stack application with a clear separation between the administrative backend and the public-facing frontend:

1. Event Management System (EMS): A secure dashboard for authenticated users to manage the lifecycle of events, categories, and user accounts.
2. Public Event Platform: A visitor-accessible interface for browsing, searching, and interacting with events without requiring a prior login.

---

## Core Functionalities

### 1. Administrative Controls (EMS)
* Authentication and Authorization: Secure login system with role-based access control for Event Creators and Administrators.
* Event Management: Full CRUD capabilities for events, including titles, descriptions, scheduling, locations, and tagging.
* Category Management: Organization of events into unique categories such as concerts, conferences, and workshops.
* User Administration: Exclusive tools for Administrators to create, edit, and manage the active status of system users.

### 2. Public Platform Features
* Event Discovery: Home page showcasing the latest events and a dedicated section for the most visited events from the past 30 days.
* Advanced Search: Full-text search functionality allowing visitors to find events by titles or descriptions.
* User Engagement: Capabilities for visitors to leave comments, like or dislike events and comments, and track view counts.
* RSVP System: Integrated registration for events with optional capacity limits and real-time availability tracking.
* Related Content: A Read More section that suggests similar events based on shared tags.

---

## Technical Specifications

### Data Management and Validation
* Relational Database: All entities, including users, events, categories, and comments, are stored in a relational database to ensure data persistence.
* Backend Validation: Strict validation for all inputs, ensuring unique emails, non-empty fields, and logical capacity limits.
* Pagination: Universal implementation of pagination across all tables and lists to ensure optimal performance and usability.

### Technology Stack
* Frontend: Developed using the React framework to provide a dynamic and responsive user interface.
* Backend: Built using technologies such as JAX-RS to handle API requests and business logic.
* Security: Passwords are stored exclusively as hash values, and visitor identification is managed through secure session or cookie mechanisms.

---

## Project Evaluation
This project is developed as a requirement for the Web Programming course. It is evaluated based on the functional implementation of the EMS, the public platform's interactivity, and the overall authenticity demonstrated during the defense process.

---
Developed for the Web Programming course curriculum.
