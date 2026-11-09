# Assignment 1 — audit and submission checklist

**Audit date:** November 9, 2026  
**Source of requirements:** *SMD Assignment 1 Student Manual*, pp. 2, 4–6, 9, 13, 16, 19–22.  
**Scope:** ShopEase, an Android Java/XML application, **not** the ERP Televerce website.

## Verified GitHub milestones

The required five commits exist in order on the retained `assignment-1` branch, with the exact messages:

| Step | Commit | Exact message |
| --- | --- | --- |
| 1 | `6ce1fa9` | `chore: set up project and connect Firebase` |
| 2 | `478bd1d` | `feat(splash): add splash screen with login check` |
| 3 | `86d32b0` | `feat(auth): add login screen with validation and password reset` |
| 4 | `de1d44e` | `feat(auth): add sign up screen and save user profile` |
| 5 | `98ea51a` | `feat(products): add product list placeholder with logout` |

- **Merged assignment pull request:** [#1 — Assignment 1: Splash, Login, Sign Up](https://github.com/RaoFaisal10/ShopEase/pull/1). The five milestones were preserved; do not squash, rebase or rewrite history.
- **Code review:** Java and XML include splash/session routing, email/password login validation, password reset, four-field signup validation, Firebase Auth account creation, Firestore `users/{uid}` profile storage, greeting, coming-soon message, and logout.
- **Main branch:** Has the Firebase Android client file `app/google-services.json`, which was added in a later maintenance pull request (it is **not** in the historical `assignment-1` snapshot).
- **Build evidence:** The latest existing GitHub Actions run compiled and uploaded a debug APK, but the emulator smoke-test failed at `uiautomator dump` with exit code **137**. APK compilation is **not** proof that live Firebase flows work. This audit includes a proposed CI improvement that uses a lighter emulator and an activity-manager probe.
- **Not verified:** Whether every historical commit individually built, the actual Firebase Console settings and live signup, a successful current emulator/physical-device test, and instructor acceptance of the collaboration invitation.

## Still required for full compliance

- [ ] **Make this repository PRIVATE** on GitHub. At audit time it was **public**, contrary to the manual. GitHub website: Settings → General → Danger Zone → Change repository visibility → Private. Keep it private until marks are final. This change cannot be made by the available GitHub connection.
- [ ] **Rename the repository to `shopease-<your-roll-number>`** if your instructor requires the exact naming rule. GitHub: Settings → General → Repository name. For example, roll 23-CS-21 becomes `shopease-23cs21`. Confirm your exact roll number before renaming; GitHub will normally redirect old URLs, but submit the new links.
- [ ] **Verify the instructor `abdullahzafar-336` is an accepted collaborator**, not merely a public viewer or pending invite: Settings → Collaborators and teams → Manage access. Invite/reinvite if necessary.
- [ ] **Confirm Firebase Console setup:** Email/Password sign-in enabled, Cloud Firestore created, and `firebase.rules` published. The Android client JSON alone does not activate those settings.
- [ ] **Test the actual Android application** on API 26+ (preferably a phone and an emulator): splash lasts about 1.5 s and routes correctly; invalid login email/password show errors; wrong credentials are handled; password reset sends an email; signup validates all four fields, creates an Auth account and `users/{uid}` document; repeat signup reports an error; product page greets by saved name; logout clears the back stack. Test on a small and a large screen.
- [ ] **Confirm the corrected GitHub Actions run** reaches build and emulator smoke-test success. If emulator CI is resource-constrained, retain the successful compile/artifact evidence and include a successful local phone/emulator test separately. Do not misrepresent a failed CI run as a passed test.
- [ ] **Create release `v1.0` with an APK attached.** In Android Studio: Build → Build App Bundle(s) / APK(s) → Build APK(s). Rename `app/build/outputs/apk/debug/app-debug.apk` to `shopease-<roll-number>-v1.0.apk`; GitHub Releases → Draft a new release → tag `v1.0` → title `Assignment 1: Splash, Login, Sign Up` → attach APK → Publish. The audit found **no release**.
- [ ] **Submit on Google Classroom**: repository URL, `assignment-1` branch URL, PR #1 URL, `v1.0` release URL, and the last commit SHA. Do not delete the `assignment-1` branch or change/rewrite the assignment history until grading completes.

## Links to prepare for Classroom

- Repository: https://github.com/RaoFaisal10/ShopEase
- Assignment branch: https://github.com/RaoFaisal10/ShopEase/tree/assignment-1
- Merged assignment PR: https://github.com/RaoFaisal10/ShopEase/pull/1
- Release (create it first): https://github.com/RaoFaisal10/ShopEase/releases/tag/v1.0
- Last milestone SHA: `98ea51a2a0cfc95c88f094a2874c17979be7cea9`; last assignment-branch SHA: `5a25d725886295987fe37ec0d7b2cbb554978c9b`. When submitting the *last commit*, use the latest SHA on the branch requested by the instructor.

**Note:** The five milestone commit messages and PR history already comply. This checklist and CI maintenance commit are additional work and do not alter those graded milestones.
