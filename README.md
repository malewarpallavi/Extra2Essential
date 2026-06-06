# Extra2Essentials

> AI‑Driven CSR Management Platform that connects **Companies, NGOs, and Volunteers** to turn corporate surplus into community impact.

**Tech Stack:** Java 17 · Spring Boot 4 · Spring Security (JWT) · Spring Data JPA · MySQL · Maven · HTML/CSS/JS (static frontend)

**Status:** 🚧 Actively in development — *I'm currently working on this project.* The CSR module and the AI report‑generation layer are the remaining pieces.

---

## 🌱 About

**Extra2Essentials** started as a hackathon project (INC) and is being evolved into a scalable **B2B SaaS platform for corporate CSR compliance**. It bridges the gap between companies with surplus resources, NGOs that need them, and volunteers who help deliver the impact — while automating the reporting and compliance work that usually slows CSR teams down.

---

## ✨ Key Features

- 🔁 **Surplus Inventory Management** — Companies list extra food, supplies, equipment, etc.
- 📦 **Donation Workflows** — End‑to‑end flow from listing → NGO request → pickup → delivery confirmation.
- 🤝 **AI‑Powered NGO Matching** — Smart matching of donations to the most relevant NGOs based on category, location, and need.
- 📊 **Real‑Time CSR Tracking Dashboards** — Separate dashboards for Corporates, NGOs, Donors, and Volunteers.
- 🧾 **Automated CSR Report Generation** *(in progress)* — Reduces manual compliance effort for corporate CSR teams.
- 🗺️ **Map & Impact Views** — Visualize donations, beneficiaries, and overall impact.
- 🔐 **Secure Auth** — JWT‑based authentication, role‑based access (Corporate / NGO / Volunteer / Donor), password reset flow.

---

## 🧩 Modules

| Module | Description | Status |
|---|---|---|
| Authentication & Roles | Register/Login, JWT, password reset | ✅ Done |
| Donation Management | Create, list, claim, track donations | ✅ Done |
| NGO Matching (AI) | Recommend best NGO for a donation | ✅ Initial version |
| Dashboards | Corporate / NGO / Donor / Volunteer | ✅ Done |
| CSR Compliance Tracking | Org‑level CSR metrics & logs | 🚧 In progress |
| AI CSR Report Generation | Auto‑generate compliance‑ready reports | 🚧 In progress |
| Analytics & B2B SaaS layer | Multi‑tenant, advanced analytics | 🗺️ Planned |

---

## 🏗️ Project Structure

```
Extra2Essentials/
├── src/main/java/com/Extra2Essentials/Extra2Essentials/
│   ├── controller/      # REST controllers (Auth, Donation, PasswordReset)
│   ├── model/           # JPA entities (User, Donation, Role, Category, Status)
│   ├── repository/      # Spring Data JPA repositories
│   ├── security/        # JWT filter, util, Spring Security config
│   └── Extra2EssentialsApplication.java
├── src/main/resources/
│   ├── static/          # Frontend pages (login, dashboards, map, impact, ...)
│   └── application.properties
└── pom.xml
```

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.9+
- MySQL 8+

### 1. Clone
```bash
git clone https://github.com/malewarpallavi/Extra2Essentials.git
cd Extra2Essentials
```

### 2. Configure the database
Create a MySQL database and update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/extra2essentials
spring.datasource.username=YOUR_USER
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=update

# JWT
app.jwt.secret=YOUR_LONG_RANDOM_SECRET
app.jwt.expiration-ms=86400000
```

### 3. Run
```bash
./mvnw spring-boot:run
```
App boots at **http://localhost:8080** — open `/index.html` for the landing page.

---

## 🔌 API Overview (high‑level)

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/auth/register` | Register a user (Corporate / NGO / Volunteer / Donor) |
| POST | `/api/auth/login` | Login, returns JWT |
| POST | `/api/auth/forgot-password` | Trigger reset link |
| POST | `/api/auth/reset-password` | Reset password with token |
| POST | `/api/donations` | Create a donation |
| GET  | `/api/donations` | List donations (filterable) |
| PUT  | `/api/donations/{id}/status` | Update donation status |

> Include `Authorization: Bearer <token>` on protected endpoints.

---

## 🗺️ Roadmap

- [ ] Finish CSR compliance tracking module
- [ ] AI‑generated CSR reports (PDF export)
- [ ] Multi‑tenant B2B SaaS architecture
- [ ] Advanced analytics & impact scoring
- [ ] Mobile‑first volunteer app
- [ ] Email/WhatsApp notifications for matches

---

## 👨‍💻 Author

Built and actively maintained by **Pallavi Malewar**.
I'm currently working on extending this into a production‑grade SaaS — contributions, ideas, and feedback are very welcome.

---

## 📄 License

This project is currently unlicensed (hackathon origin). A proper open‑source / commercial license will be added as the SaaS version matures.
