
[![Discord](https://badgen.net/badge/Discord/Join/5865F2?icon=discord)]()

# LexiGO v1.3

*A cross-platform Java desktop application combining adaptive flash-card drills, rich word references, and gentle gamification to help English-speakers build lasting foreign-language vocabulary.*

LexiGO uses a spaced-repetition scheduler to show each word just when you need a review. Around that core are:

- One-tap daily check-ins  
- example sentences, and translations on every card  
- Streaks, badges, and leaderboards (optional, friendly competition)

---

## Quick Facts

| Item             | Details                                                                                 |
|------------------|-----------------------------------------------------------------------------------------|
| **Domain**       | Vocabulary acquisition for native English speakers                                      |
| **Users**        | Age 10+; from casual hobbyists to exam-focused polyglots                                |
| **Tech stack**   | Java 17 · Maven · Swing · JSON persistence                                              |
| **Architecture** | Clean Architecture (entities & use-cases & adapters & Swing views)                      |
| **Modules**      | Auth & Profile · Check-In/Study · Word Reference · Stats Dashboard · Gamification        |

**Codebase structure at a glance:**  
```text
app/               start-up bootstrap & DI wiring  
data_access/       JSON- and in-memory gateways  
entity/            core domain models (User, Word, Achievement…)  
use_case/          interactors & boundaries per user-story  
interface_adapter/ controllers, presenters, view-models  
view/              Swing panels, navigation, theming  
```

---

## Live Demos

- YouTube: ▶ Watch the LexiGO demo  
*(Full URLs will be posted.)*

---

## Team & Use-Case Owners

| Member           | GitHub        | Primary Use-Cases                   |
|------------------|---------------|-------------------------------------|
| Jacky Huo        | `@Jackymn25`  | Start-check-in • Study-session       |
| Jacob Ke         | `@Y0m1ya`     | View history • Change password      |
| Jincheng Liang   | `@Godoftitan` | Leaderboard • Word detail           |
| Heyuan Zhou      | `@HeyuanZ621` | Achievement system                  |
| Yunzhao Li       | `@yunzhaol`   | Profile settings • Finish check-in  |

*Team story:* collectively refined signup rules and polished the registration/login flow.

---

## User-Story Matrix

| # | Persona                  | I want to…                                 | So that…                       | Owner    | Team Story |
|---|--------------------------|--------------------------------------------|--------------------------------|----------|------------|
| 1 | Language learner         | Start a check-in and study with flip cards | Keep my daily learning streak  | Jacky    |            |
| 2 | Security-conscious       | Change my password (with verification)     | Protect my account             | Jacob    |            |
| 3 | Reflective learner       | View my historical stats                   | Track progress over time       | Jacob    |            |
| 4 | Competitive learner      | See the leaderboard rankings               | Compete with friends           | Jincheng |            |
| 5 | Inquisitive learner      | Inspect word details during study          | Deepen understanding           | Jincheng |            |
| 6 | Motivated learner        | Earn achievements for milestones           | Feel rewarded and engaged      | Heyuan   |            |
| 7 | Multilingual user        | Manage profile (username, languages)       | Switch courses easily          | Yunzhao  |            |
| 8 | Learner finishing session| Complete a session & finalize check-in     | Log progress and update streak | Yunzhao  |            |
| 9 | **First-time user**      | Sign up with Security Q&A (optional)       | Start learning immediately     | **Team** | ✔          |

---

## Getting Started

Run these commands in your terminal:

```bash
# 1. Clone the repo
git clone https://github.com/Jackymn25/LexiGOv1.0
cd LexiGOv1.0

# 2. Build (includes style and dependency checks)
mvn -U clean verify

# 3. Launch the app
mvn exec:java -Dexec.mainClass="app.Main"
```

**Recommended Developer Environment:**
1. **IDE:** IntelliJ IDEA 2024.1+ with Google Java Style plugin  
2. **Java SDK:** AdoptOpenJDK 17 (LTS)  
3. **Static analysis:** `mvn spotbugs:check` – must pass before push  
4. **Unit tests:** JUnit 5 per use-case boundary (tests to arrive in v1.1)  
5. **Release packaging:** `mvn package` produces a self-contained JAR with launcher scripts  

---

## Project Health

| Metric        | Status                                                    |
|---------------|-----------------------------------------------------------|
| Test coverage | 97 % (whole project);    |
| CI pipeline   | GitHub Actions: build → checkstyle → spotbugs → package   |
| Open issues   | will be updated as needed                                     |

---

## Roadmap

| Milestone                 | Target       | Notes                                           |
|---------------------------|--------------|-------------------------------------------------|
| v1.1 – first build  | 15 July 2025  | group case set up   |
| v1.2 – complete basic cases         | 24 July 2025      | Personal case done!     |
| v1.3 – Refactors & Test coverages & UI enhancement | 7 Aug 2025         | Merges & bug fixing & Tests & UI!           |
| v1.4 - Extensions & UI !| Not started | |

---

## Contribution Guide

- feature branch → **small, focused PRs**  
- Reference a GitHub issue in every PR  
- Use Conventional Commits: `feat(scope): summary`  
- Run `mvn verify` locally before pushing

Note: Please dont be pressured to PR, we would fix minor bugs

**Welcome PR**  
A “welcome” PR for first-time contributors will go out after 20 Aug 2025, tracking new issues.

---

*Updated 7 Aug 2025 by the LexiGO Team*  
