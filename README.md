# Agora Android Application

[![Pipeline Status](https://gitlab.com/aossie/agora-android/badges/develop/pipeline.svg)](https://gitlab.com/aossie/agora-android/-/commits/develop)
[![Gitter](https://img.shields.io/badge/chat-on%20gitter-ff006f.svg?style=flat-square)](https://gitter.im/AOSSIE/Agora-Android)
![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)


_Android Application for Agora Web that uses [Agora](https://gitlab.com/aossie/Agora/): An Electronic Voting Library implemented in Scala. This application uses [Agora Web API](https://gitlab.com/aossie/Agora-Web) as backend application_

---
### Screenshots
<table>
  <tr>
    <td><img src="./screenshots/GETSTARTED.png" alt="6" width="248px" ></td>
    <td><img src="./screenshots/LOGIN.png" alt="2" width="248px" ></td>
    <td><img src="./screenshots/SIGNUP.png" alt="3" width="248px" ></td>
    <td><img src="./screenshots/DASHBOARD.png" alt="4" width="248px" ></td>
    <td><img src="./screenshots/CREATE.png" alt="5" width="248px" ></td>
  </tr>
  <tr>
    <td><img src="./screenshots/CALENDAR.png" alt="6" width="248px" ></td>
    <td><img src="./screenshots/SETTINGS.png" alt="7" width="248px" ></td>
    <td><img src="./screenshots/PROFILE.png" alt="8" width="248px" ></td>
    <td><img src="./screenshots/CONTACT.png" alt="9" width="248px" ></td>
    <td><img src="./screenshots/DETAIL.png" alt="10" width="248px" ></td>
  </tr>
</table>

---

## Contributing Guidelines
Please refer to [CONTRIBUTING.md](https://github.com/narendraanjana09/Agora-Android/CONTRIBUTING.md)

## Discussion
Join the discussion channels [Discord](https://discord.com/invite/6mFZ2S846n) [Gitter](https://gitter.im/AOSSIE/Agora-Android)

## Android specific guidelines
- Before committing code make sure that project [style](https://developer.android.com/studio/intro#style_and_formatting) is set to project.
- Use data-binding for any new code changes
- Write test cases ( both UI and unit as applicable for new features )
- Do-Not create any nested xml layouts, in case the view is complex to have more than two level nesting create a custom view
- Follow SOLID, clean code principles.
