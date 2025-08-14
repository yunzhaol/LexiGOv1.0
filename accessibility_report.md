## Accessibility Report
[← Back to Main README](./README.md)

**Audience.** Introductory-level English learners using a desktop vocabulary app.
**Model.** We follow the **social model of disability**: barriers live in the product environment, so we design the default UI for everyone—no separate “accessible mode”.

### Universal Design principles → current status

| Principle                           | Status        | What’s in LexiGO today                                                                                                                                       | Next steps (high-impact)                                                                                                       |
| ----------------------------------- | ------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------------------------------ |
| **1. Equitable Use**                | Partial       | One UI for all users; no segregated interface.                                                                                                               | Programmatic **name/role/state** for every control; avoid color-only cues; text alternatives for decorative images.            |
| **2. Flexibility in Use** *(focus)* | **Strong**    | **200% text zoom** without content loss; **Dark/Light themes**; mouse and most screens support keyboard.                                                     | Guarantee a **keyboard-only path** for all core tasks; shortcuts (Space=flip, ←/→=prev/next, Enter=submit).                    |
| **3. Simple & Intuitive** *(focus)* | **Strong**    | Plain labels (“Daily Check-in”, “Profile”); predictable flows.                                                                                               | 60-second first-run tips; clearer, actionable error messages.                                                                  |
| **4. Perceptible Information**      | Partial       | High-contrast themes; icons **and** text labels.                                                                                                             | Verify contrast ≥ **4.5:1** on both themes; label inputs via `setLabelFor`; accessible descriptions for logos/illustrations.   |
| **5. Tolerance for Error**          | **Improving** | **Logout confirmation dialog** prevents accidental sign-out (`JOptionPane.YES_NO_OPTION`; **Esc/No** cancels). Duplicate-password check; informative alerts. | Confirm/undo for other destructive actions; link form errors to fields; **Esc** closes dialogs and returns focus consistently. |
| **6. Low Physical Effort**          | **Strong**    | Few steps to start studying; no drag/long-press; generous click targets.                                                                                     | Optional “auto-start study” to cut routine clicks; keyboard shortcuts to reduce hand travel.                                   |
| **7. Size & Space for Use**         | Partial       | Resizable window; large-text mode keeps layout usable.                                                                                                       | Add a min-window size and scrolling in tiny layouts; wrap long labels; enlarge small toggles for touch devices.                |

**Per-principle notes**

* **Equitable Use:** One UI for all; themes and large text benefit everyone. • Future: complete AccessibleContext naming and alt text.
* **Flexibility in Use:** 200% zoom + keyboard/mouse on core screens. • Future: full keyboard paths + shortcuts.
* **Simple & Intuitive:** Clear labels and flows. • Future: brief first-run tips + actionable errors.
* **Perceptible Information:** High contrast, text + icon signals. • Future: verify 4.5:1 contrast; add input labelling and logo descriptions.
* **Tolerance for Error:** **Logout requires confirmation; Esc/No aborts**, plus duplicate-password check. • Future: confirm/undo elsewhere; field-linked errors; consistent Esc handling.
* **Low Physical Effort:** Minimal steps; no precision gestures. • Future: auto-start and shortcuts.
* **Size & Space:** Resizable; large text preserves layout. • Future: min size, scroll when tiny, bigger toggles for touch.

**Market / Audience.**
LexiGO targets **native English speakers aged 10+** who want a fast, low-friction way to build foreign-language vocabulary—students preparing for exams, adult self-learners, travelers, and language-club members. It suits users who prefer a **distraction-free desktop** experience with short daily sessions and gentle motivation (streaks and badges). Schools and clubs can deploy LexiGO in labs because it runs as a **single JAR** with local storage. Privacy-minded learners who dislike cloud lock-in appreciate **offline-friendly** study and on-device data.

**Access & ethics.**
Today the app may be **less usable for screen-reader and keyboard-only users** because some controls lack programmatic names/roles and a complete keyboard path. Learners whose **UI language isn’t supported** face an environmental barrier; we are expanding localization. We are closing gaps by adding accessible names, visible focus, dialog **Esc** handling, and field-linked error messages. Desktop-only distribution may also exclude people who rely solely on mobile; a lightweight web/desktop hybrid is on our roadmap.

---
