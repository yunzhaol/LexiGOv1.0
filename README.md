# LexiGO v1.0

*A Java‑Swing desktop app that helps learners master foreign languages through spaced‑repetition study sessions, daily streaks, and friendly competition.*

---

## 🧭 Overview

| Item            | Details                                        |
|-----------------|------------------------------------------------|
| **Domain**      | Foreign‑language learning                      |
| **Tech stack**  | Java 17 · Maven · Swing UI                     |
| **Architecture**| **Clean Architecture** + SOLID principles      |
| **Core Modules**| Authentication & Profile · Flash‑Card Study · Gamification |

---
## 🎬 Live Demos

| Platform | Link |
|----------|------|
| YouTube  | [▶ Watch the LexiGO demo](https://youtu.be/4ua4_WRky7s) |
| Bilibili | [▶ 哔哩哔哩演示视频](https://www.bilibili.com/video/BV1kb8ezxEbn/) |

---

## 👥 Team & Use‑Case Owners

| Member | GitHub | Primary Use‑Cases |
|--------|--------|-------------------|
| **Jacky Huo**      | `@Jackymn25` | Start‑check‑in • Study‑session |
| **Jacob Ke**       | `@Y0m1ya`    | View study history • Change password |
| **Jincheng Liang** | `@Godoftitan`| Rank leaderboard • Word detail |
| **Heyuan Zhou**    | `@HeyuanZ621`| Achievement system |
| **Yunzhao Li**     | `@yunzhaol`  | Profile settings • Finish check‑in |

> **Team story:** refine signup rules & polish registration / login flow (handled by the whole team).

---

## 📝 User‑Story Matrix

| # | As a…               | I want to…                                    | So that…                             | Owner | Team Story |
|---|---------------------|-----------------------------------------------|--------------------------------------|-------|-----------|
| 1 | Language learner    | Start a check‑in and study with flip cards    | Keep my daily learning streak        | Jacky |           |
| 2 | Security‑conscious  | Change my password with identity verification | Protect my account                   | Jacob |           |
| 3 | Reflective learner  | View my historical study stats                | Track progress over time             | Jacob |           |
| 4 | Competitive learner | See the leaderboard rankings                  | Compete with friends                 | Jincheng |         |
| 5 | Inquisitive learner | Inspect word details during a study session   | Deepen understanding of each word    | Jincheng |         |
| 6 | Motivated learner   | Earn achievements for milestones              | Feel rewarded and engaged            | Heyuan |           |
| 7 | Multilingual user   | Manage profile (username, languages)          | Switch between courses easily        | Yunzhao |           |
| 8 | Learner finishing session | Complete a session & finalize check‑in | Log progress and update streak       | Yunzhao |           |
| 9 | **First‑time user** | Sign up with （Security Question and Answer）(OPTIONAL)                 | Start learning immediately           | **Team** | ✔ |

---

## 🗄️ Domain Entities (high‑level)

| Entity      | Purpose |
|-------------|---------|
| **User**        | Credentials & unique identifier |
| **Profile**     | Display name, native / target language |
| **Word**        | Vocabulary item (text, audio, metadata) |
| **WordBook**    | A deck of words for a lesson |
| **Achievement** | Badge definition & unlock progress |

---

## 🛠 Project Setup (Maven)

```bash
# clone
git clone https://github.com/Jackymn25/LexiGOv1.0
cd LexiGOv1.0

# build & test
mvn clean verify

# run desktop app
mvn exec:java -Dexec.mainClass="app.Main"
